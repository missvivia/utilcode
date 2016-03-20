/*
 * --------------------------------------------
 * 实时的表单验证系统，基于数据驱动, 类似angular的 ngForm
 * 
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */

// 需要注意的是，
// 这里只是简化版，暂时无法做到大而全，只能说根据angular文档的api提供类似功能

define([
  "{pro}extend/util.js?v=1.0.0.0",
  "{pro}lib/regularjs/dist/regular.js?v=1.0.0.0"
  ], function( _ , config, ut ){

  // Regular中的dom帮助函数, 
  var dom = Regular.dom;


  // REGEXP
  var REG = {
    "required": "/[!\s]*/",
    // 直接从angularjs剽窃的正则
    "url": /^(ftp|http|https):\/\/(\w+:{0,1}\w*@)?(\S+)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?$/,
    "email":  /^[a-z0-9!#$%&'*+/=?^_`{|}~.-]+@[a-z0-9-]+(\.[a-z0-9-]+)*$/i,
    "number" : /^\s*(\-|\+)?(\d+|(\d*(\.\d*)))\s*$/
  }

  // 我们先提取出Regular中的directive， 我们需要覆盖它
  var rModel = Regular.directive("r-model");

  //基础权重, 指令有权重，我们需要让部分指令运行在某些指令之前
  var basePriority = 1;


  // setError 很简单
  function setError($model, name){
    $model.$field.$error[name] = true;
    $model.$form.$error[name] = true;
    $model.$form.$valid = false;
    $model.$field.$valid = false;
  }
  function checkError($form){

  }
  // removeError就相对复杂了 需要便利判断
  function removeError($model, name){
    $model.$field.$error[name] = null;
    var $form = $model.$form, isError=false, isFieldError=false, isAllError = false;
    var $field = $model.$field;

    var fError = $field.$error;

    // 检查是否有其它的error
    for(var j in fError){
      if(fError.hasOwnProperty(j)){
        if(fError[j]){
           isFieldError = true;
           break;
        }
      }
    }
    if(isFieldError) $field.$valid = false;
    else $field.$valid = true;


    // 然后全局检查是否还有其它的字段的是否有类似name的错误 
    for(var i in $form){
      if($form.hasOwnProperty(i)){
        // 还剩下一个字段为error，我们就认为它是错误的
        if($form[i].$error && $form[i].$error[name]){
          isError = true;
        }
        if($form[i] && "$error-$valid".indexOf(i) == -1 && !$form[i].$valid){

          isAllError = true;
        }
      }
    }
    if(!isError){
      $form.$error[name] = null;
    }else{
      $form.$error[name] = true;
    }


    // 最后判断是否仍有其它字段的valid为fasle
    if(!isAllError){
      $form.$valid = true;
    }else{
      $form.$valid = false;
    }
  }

  var directive = {
    // 首先我们要收集名字, 名字要在r-maxLength 等之前
    "name": {
     priority: 10,
  // 由于regularjs 中是先compile 内部子节点，再处理父节点属性, 所以这里我们要
  // 做一点小处理
     link: function(element, value, name, attrs){
        dom.attr(element, "name", value);
        var data = this.data;
        if(!value) throw "you need specified a value for [name]"
        var tagName = element.tagName.toLowerCase(), tmpData = data.$tmpData;


        switch(tagName){
          case 'form':
            var $form = data[value] = {
              $valid: true,
              $error: {}
            }
            for(var i in tmpData){
              if(tmpData[i]) {
                tmpData[i].$form = $form;
                $form[i] = tmpData[i].$field;
              }
            }
            data.$tmpData = {};
            break;
          case 'input':
          case 'select':
          case 'input':
            break;
       }
     }
   },
   "r-model": {
      priority: 9,
      link: function(element, value, name, attrs){
        var data = this.data;
        var tmpData  = data['$tmpData'] || (data['$tmpData'] = {});
        var fieldName = element.name? element.name: 
          (typeof value === 'string'? value: "")

        if(!fieldName) "r-model need a fieldName";

        var $model = Regular.expression(value);

        $model.$field = {
          $error: {},
          $valid: true
        }

        tmpData[fieldName] = $model;

        var type = element.type;
        // 自动处理 url之类的出错
        if(~"url-email-number".indexOf(type)){
          this.$watch(element.$model, function(value){
            value = value || "";
            if(!REG[type].test(value)) {
              setError($model, type);
            }else{
              removeError($model, type)
            }
          }, {force: true})
        }
        element.$model = $model;
        // 交由r-model 处
        var destroy = rModel.link.call(this,element, value, fieldName, attrs);
        return function(){
          destroy();
          element.$model = null;
        }
      }
   },
   // 以下几个逻辑可以提取， 先脏代码写着，后续重构
   "required": function(element, value){
      var rmodel = element.$model;
      if(rmodel){
        this.$watch(element.$model, function(value){
          if(!value){
            setError(rmodel, "required");
          }else{
            removeError(rmodel, "required");
          }       
        }, {force: true})
      }
      dom.attr("required", true);
   },
   "r-minlength": function(element, min){
      min = Regular.expression(min).get(this);
      if(typeof min !== "number") throw "r-maxLength accepet Number";
      var rmodel = element.$model;
      if(rmodel){
        this.$watch(element.$model, function(value){
          if(typeof value !=="string") return;
          if(value && value.length < min){
            setError(rmodel, "minlength");
          }else{
            removeError(rmodel, "minlength");
          }
        }, {force: true})
      }
   },
   "r-maxlength": function(element,max){
      max = Regular.expression(max).get(this);
      if(typeof max !== "number") throw "r-maxLength accepet Number";
      var rmodel = element.$model;
      if(rmodel){
        this.$watch(element.$model, function(value){
          if(typeof value !=="string") return;
          if(value && value.length > max){
            setError(rmodel, "maxlength");
          }else{
            removeError(rmodel, "maxlength");
          }       
        }, {force: true})
      }
   },
   "r-pattern": function(element, pattern){
      pattern = new RegExp(pattern.replace(/^\/|\/$/, ''));
      var rmodel = element.$model;
      if(rmodel){
        this.$watch(element.$model, function(value){
          value = value || "";
          if(!pattern.test(value)){
            setError(rmodel, "pattern");
          }else{
            removeError(rmodel, "pattern");
          }       
        }, {force: true})
      }

   }
  } 



  function formModule(Component){
    Component.directive(directive)
  }

  // 注册form插件
  // 
  // Component.use("$form")用于激活组件的form支持
  Regular.plugin("$form", formModule);


  return formModule;

})