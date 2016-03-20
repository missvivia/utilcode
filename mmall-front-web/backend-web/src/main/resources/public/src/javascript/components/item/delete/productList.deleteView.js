define([
  "{pro}extend/util.js",
  "{pro}widget/BaseComponent.js",
  'text!./productList.deleteView.html'
], function(_u, BaseComponent, tpl){

  var ProductDeleteModal = BaseComponent.extend({
    template: tpl,
    onShelveCheck:function(){
    	//var elm = this.$refs.onShelveCheck;
    	
    },
    unShelveCheck:function(){
    	
    }
  })


  return ProductDeleteModal;
})