/**
 * 活动列表筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * @changelog 删除以下逻辑
 *  //<div class="input-group input-group-sm input-group-fix1">
    //   <input type="text" class="form-control  form-control-sm" r-model='headerDraft' on-enter={{this.addCol(headerDraft)}}>
    //   <span class="input-group-btn">
    //     <button on-click={{this.addCol(headerDraft)}}
    //      class="btn btn-default" type="button">+1列
    //    </button>
    //   </span>
    // </div><!-- /input-group -->

 */

define([
  "text!./sizeTemplate.html",
  "{pro}widget/BaseComponent.js",
  "{pro}components/notify/notify.js",
  "{pro}extend/util.js"
  ], function(tpl, BaseComponent, notify,  _){
  var arr = [];



  var DEFAULT_TEMPLATE = 1;
  var SIZE_TEMPLATE = 2;
  var CUSTOM_TEMPLATE = 3;
  var NONE_TEMPLATE = 4;


  var SizeTemplate = BaseComponent.extend({
    template: tpl,
    config: function(data){
      var template = data.template;
      _.extend(template, {
        sizeTemplate: [],
        // defaultTemplate: {},
        sizeHeader: []
        // 非自定义时
      })
      if(!data.sizeTemplateId && data.sizeType === DEFAULT_TEMPLATE){
        data.sizeTemplateId = -1;
      }

      // data.token是用来标示切换到初始编辑tab时，我们需要保留编辑数据！！
      data.token = this.getToken();

      _.extend(data, {
        skuList: [],
        skuList2: [],
        sizeType: DEFAULT_TEMPLATE 
      })

      //保存之前的编辑数据！ 
      data.preSkuList = _.clone(data.skuList);

      this.configData(data);


      // 
    },
    // 初始化模板的数据
    // 
    configData: function(data){
      var  template = data.template;
      // 加入为均码
      if(data.sizeType === DEFAULT_TEMPLATE){
        // 1. 由于与尺码模版是一致的参数，所以我们要做下预处理
        if(data.skuList2 && data.skuList2[0]){
          data.defaultId = data.skuList2[0].id;
          data.defaultCode = data.skuList2[0].barCode;
        }
        // data.selectedSize = template.defaultTemplate;
      }else if(data.sizeType === SIZE_TEMPLATE){
        // 2. 如果是尺码模版，我们需要从中选取与sizeTemplateId一致的项
        var index = _.findInList(data.sizeTemplateId, template.sizeTemplate, 'id');
        data.selectedSize = template.sizeTemplate[index];
      }
      // 3. 如果是指自定义（第一栏）
      if(data.sizeType ===CUSTOM_TEMPLATE ){ // 如果是自定义模版
        var token = this.getToken();
        // 我们要回退之前的数据
        if(token === data.token){ // 我们需要保存原来的skulist定义！！！
          data.skuList = _.clone(data.preSkuList);//每次都要clone，避免影响
        }else{
          data.skuList = [];
        }
      }
      // 4. 新需求，默认出现一行的自定义模版
      if(!data.skuList || !data.skuList.length){
        data.skuList = [{
          id:0,
          barCode: "",
          body:[]
        }]
      }
      if(data.selectedSize) this.configSelectedSize(data.selectedSize)
    },
    //预配置选择的尺码模版
    configSelectedSize: function(size){
      var data = this.data;
      // 获取唯一标识，如果和之前一样
      var token = this.getToken(size);
      var data = this.data;
      // jira 701: 当一个都没有选中时候，全选他们
      if(size.list  && data.token === token){
        size.list.forEach(function(size){
          // 找到是否之前是属于编辑项，我们要保留信息
          var index = _.findInList(size.id, data.skuList2, 'sizeId');
          if(~index){
            size.selected = true;
            size.barCode = data.skuList2[index].barCode;
            size.skuId = data.skuList2[index].id;
          }
        })
      }
      // jira 701 默认全部选中，如果
      if(size.list){
        if(size.list.filter(function(s){return s.selected}).length==0){
          size.list.forEach(function(s){
            s.selected = true;
          })
        }
      }
    },
    // 返回唯一标识，根据categoryId
    getToken: function(size){
      var data = this.data;
      return this.data.categoryId + "-" +
          (data.sizeType === CUSTOM_TEMPLATE? -10 :
          size && size.id || this.data.sizeTemplateId) // 自定义或者尺码模版

    },
    // 刷新数据
    refresh: function(data, categoryId){
      var template = this.data.template;
      // template.defaultTemplate = data.defaultTemplate;

      this.data.categoryId = categoryId;

      _.extend(template, {
        sizeTemplate: data.sizeTemplate,
        sizeHeader: data.sizeHeader
        // 非自定义时
      },true)
      // 假如当前选择了自定义，由于更新了sizeTemplate之后，
      // sizeTemplate被更新，所以我们要迁移到defaultTemplate上
      if(this.data.sizeType === SIZE_TEMPLATE){
        this.data.selectedSize=null;
        this.data.sizeType = DEFAULT_TEMPLATE;
      }
      this.configData(this.data);
      this.$update();
    },
    // 选择尺码模板，为false 则认为是默认尺码模板
    selectSize: function(size){
      var data = this.data;
      if(size === false){
        // size = data.template.defaultTemplate;
        // data.sizeType = DEFAULT_TEMPLATE;
      }else{
        data.sizeType = SIZE_TEMPLATE;
      }
      data.selectedSize = size;
      this.configSelectedSize(size);
      data.showMenu = false;
      return false;
    },
    findSelected: function(selected){
      var data = this.data;
      if(!selected) return;
      return selected.list.filter(function (size){
        return !!size.selected;
      })
    },
    // 增加一列
    addCol: function(headerDraft){
      var data = this.data;
      if(!headerDraft){
        return notify.notify({
          message: "列标题不能为空",
          type: "error"
        })
      }
      data.template.sizeHeader.push({
        id: -1,
        name: headerDraft,
        isRequired: true
      }) 
      data.headerDraft = "";
    },
    // 删除一列
    delRow: function(index){
      var skuList = this.data.skuList;
      if(skuList.length <= 1){
        notify.notify({
          message: "删除失败，至少需要一行",
          type: "error"
        })
      }else{
        skuList.splice(index, 1);
      }
      return false;
    },
    // 增加一行
    addRow: function(){
      this.data.skuList.push({
        id:0,
        barCode: "",
        body:[]
      })
      return false;
    },
    checkBarCode: function(barCode){
      barCode = barCode && barCode.trim();
      if(!barCode) return {
        code: 400,
        message: "条形码不能为空"
      }
      if(!(/^.{1,32}$/.test(barCode) )) return {
        code:400,
        message: "条形码不能超过32个字符"
      }
      return true
    },
    // h获取数据，
    // -------------------------
    getData: function(){
      var data = this.data;
      var obj = {},barCode, error;
      var tmpCode = [];
      var checkBarCode = this.checkBarCode.bind(this);
      switch(data.sizeType){
        case DEFAULT_TEMPLATE:
          barCode = data.defaultCode && data.defaultCode.trim();
          var tBarCode = this.checkBarCode(barCode);
          if(tBarCode!==true) return tBarCode;
          obj.skuList2= [
            {barCode: barCode, id: data.defaultId}
          ]
          break;
        case SIZE_TEMPLATE:
          var error;
          var list = data.selectedSize.list
          obj.skuList2 = list.filter(function(size){
            return size.selected
          }).map(function(size){
            barCode = size.barCode;
            var tBarCode = checkBarCode(size.barCode);
            if(tBarCode!==true) error= tBarCode
            tmpCode.push(barCode);
            return {
              barCode: barCode,
              sizeId: size.id,
              id: size.skuId || 0
            }
          })
          if(!obj.skuList2.length){
            return {
                code: 400,
                message: "至少填写一个尺码"
            }
          }
          break;
        case CUSTOM_TEMPLATE:
          obj.skuList = data.skuList;
          obj.sizeHeader = data.template.sizeHeader;
          if(!obj.skuList){
            return {
              code: 400,
              message: "至少填写一个自定义尺码"
            }
          }
          // 实时的看下哪几个头部是必填项。
          var requires = [];
          obj.sizeHeader.forEach(function(hd, index){
            if(hd.required) requires.push(index);
          })
          for(var i =0,len=data.skuList.length; i< len;i++){
            var barCode = (data.skuList[i].barCode);
            var body = (data.skuList[i].body); 
            // 除了检查条形码，我们还需要检查body里的必填项
            barCode = barCode && barCode.trim();
            requires.forEach(function(rq){
              if(!body[rq]){
                error = {
                  code: 400,
                  message: "自定义尺码您有未填写的必填项"
                }
              }
            })
            var tBarCode = checkBarCode(barCode);
            if(tBarCode!==true) error= tBarCode
            tmpCode.push(barCode);
          }
          break
        case NONE_TEMPLATE: // 未选择
      }
      // 如果有刚才在map和forEach中无法中断的异常
      if(error) return error
      if( (data.sizeType === CUSTOM_TEMPLATE || 
          data.sizeType === SIZE_TEMPLATE) && tmpCode ){
        var preLength = tmpCode.length;
        if(preLength != distinct(tmpCode).length) return{
          code: 400,
          message: "条形码不能重复哦"
        }
      }
      obj.sizeType = data.sizeType;
      if(obj.sizeType == SIZE_TEMPLATE){
        obj.sizeTemplateId = data.selectedSize.id;
      }
      
      return obj;
    }



    // @子类修改
  });
// 最简单的数组去重算法 :(
  function distinct(array) {
    for (var i = array.length; i--;) {
      var n = array[i]
      // 先排除 即 如果它是清白的 后面就没有等值元素
      array.splice(i, 1, null)
      if (~array.indexOf(n)) {
        array.splice(i, 1); //不清白
      } else {
        array.splice(i, 1, n); //不清白
      }
    }
    return array
  }
  return SizeTemplate;
})