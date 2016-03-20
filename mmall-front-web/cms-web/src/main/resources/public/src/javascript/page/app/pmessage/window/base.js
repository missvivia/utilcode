/**
 * author hzwuyuedong@corp.netease.com
 */

define([
  "{pro}extend/util.js",
  "{pro}widget/BaseComponent.js",
  'text!./base.html'
], function(_, BaseComponent, tpl, p,o,f,r){

  var contentModal = BaseComponent.extend({
    template: tpl,
    init: function(){
      // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
      if(this.$root === this)
        this.inject(document.body)
    },

    //$子类重写
    show: function($event){
      //this.$update();
    },

    close: function(){
      this.$emit('close');
      this.destroy();
    },

  })


  return contentModal;

})