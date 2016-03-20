/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define([
  '{lib}base/element.js',
  '{pro}widget/module.js',
  '{pro}components/product/helper.list.js',
  '{pro}extend/request.js'
  ],
  function(e, Module, HelperList, request) {
    var pro;

    $$HelperModule = NEJ.C();
    pro = $$HelperModule._$extend(Module);
    
    pro.__init = function(_options) {
      this.__supInit(_options);
    }

    pro.__initComponent = function(){
      this.__helpers = new HelperList({}).$inject(".j-list")
    }

    $$HelperModule._$allocate();
});