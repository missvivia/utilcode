/**
 * 商品基本信息组件
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!../templates/prodMan.html",
  "{pro}widget/BaseComponent.js",
  "{pro}extend/util.js"
  ], function(tpl, BaseComponent, _ ){

  var ProdBaseForm = Regular.extend({
    name: "m-pbform",
    template: tpl,
    config: function(data){
      _.extend(data, {
        templates: []
      })
    },
    addTemplate: function(code, size){
      var data = this.data;
      var templates = this.data.templates;
      data.code = "";
      data.size = "";

      templates.push({
        code: code,
        size: size
      })
    }
  });

  return ProdBaseForm;

})