/**
 * 详情专用的计数器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
NEJ.define([
  "{lib}base/element.js",
  "text!./magnifier.html",
  "pro/widget/BaseComponent"
  ], function(e, tpl, BaseComponent){
  var dom = Regular.dom;

  var Magnifier = BaseComponent.extend({
    name: "amountfiled",
    init: function(){
      
    }
  });

  return Magnifier;

})