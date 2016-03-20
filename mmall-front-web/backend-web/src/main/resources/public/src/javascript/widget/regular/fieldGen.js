/**
 * 表单字段生成组件
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!../templates/fieldGen.html",
  "{pro}widget/BaseComponent.js",
  "{pro}extend/fake.js"
  ], function(tpl, BaseComponent, fake){

  var FiledGen = BaseComponent.extend({
    name: "m-fieldgen",
    config: function(data){
      // @TODO: remove. 使用假数据
      if(!data.attrs){
        data.attrs = fake.attrs;
      }
    },
    template: tpl,
    init: function(){
      window.app = this;
    }
  });

  return FiledGen;

})