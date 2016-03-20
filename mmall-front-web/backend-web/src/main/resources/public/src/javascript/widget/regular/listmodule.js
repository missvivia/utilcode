define([
  "text!../templates/prodMan.html",
  "{pro}widget/BaseComponent.js"
  ], function(tpl, BaseComponent){

  var ProdBaseForm = BaseComponent.extend({
    name: "m-pbform",
    template: tpl
  });

  return ProdBaseForm;

})