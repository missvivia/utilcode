/*
 * --------------------------------------------
 * nej, webform 迁移
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
  "{pro}extend/util.js",
  "{pro}extend/config.js",
  "{lib}util/form/form.js",
  "{pro}lib/regularjs/dist/regular.js"
  ], function( _ , config, ut ){

  // Regular中的dom帮助函数
  var dom = Regular.dom;

  var proxyEvent = ['oninvalid', 'onvalid', 'oncheck']


  function formModule(Component){
    // 代理四方法, 由于混淆的问题，我们无法动态生成代理方法
    Component.implement({
      _$submit: function(){
        return this._form._$submit();
      },
      _$data: function(){
        return this._form._$data();
      },
      _$form: function(){
        return this._form._$form();
      },
      _$checkValidity: function (field){
        return this._form._$checkValidity(field);
      }
    }).directive("nz-form", function(form, value ){
      var self = this;
      var _form = this._form = ut._$$WebForm._$allocate({
        form: form
      })
      proxyEvent.forEach(function(name){
        _form._$addEvent(name, self.$emit.bind(self, name))
      })

      return _form._$recycle._$bind(_form);
    })

  }




  return formModule;

})