/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define([
  '{lib}base/element.js',
  '{pro}widget/module.js',
  '{pro}components/product/helper.view.js',
  '{pro}extend/request.js'
  ],
  function(e, Module, HelperView, request) {
    var pro;


    var $$HelperModule = NEJ.C();
    pro = $$HelperModule._$extend(Module);
    
    pro.__init = function(_options) {
      this.data = window.__data__ || {};
      this.__supInit(_options);
    }

    pro.__initComponent = function(){
      this.__hview = new HelperView({
        data: this.data 
      }).$inject(".j-hview")
    }

    $$HelperModule._$allocate();
});