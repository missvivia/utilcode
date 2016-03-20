/**
 * author hzwuyuedong@corp.netease.com
 */

define([
  "{pro}extend/util.js?v=1.0.0.0",
  "{lib}base/element.js?v=1.0.0.0",
  "{pro}widget/BaseComponent.js?v=1.0.0.0",
  'text!./modal.html?v=1.0.0.0'
], function(_, e , BaseComponent, tpl, p,o,f,r){

  var dom = Regular.dom;

  var anchor = e._$html2node("<div class='u-modal-anchor'></div>");
  dom.inject( anchor, document.body ,'top');


  var contentModal = BaseComponent.extend({
    template: tpl,
    config: function(data){
      _.extend(data, {
        autofix: true
      })
    },
    init: function(){
      // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
      if(this.$root === this)
        this.inject(document.body)

      //anchor是用来定位的，因为一旦overflow设置为hidden，页面就被跳动到顶部，
      // 我们需要一个anchor来帮助我们定位
      this.topMargin = _.getScroll().y;

      if(!dom.msie || dom.msie > 7){
        anchor.style.marginTop = "-" + this.topMargin + "px";
        anchor.style.display = "";
      }
       
      if(this.data.autofix){
        dom.addClass(document.body, "z-overy")
      }
      
    },

    //$子类重写
    show: function($event){
      //this.$update();
    },

    close: function(){
      this.$emit('close');
      this.destroy();
    },

    confirm: function(){
      this.$emit('confirm', this.data);
      this.destroy();
    },
    destroy: function(){
      this.supr();
      dom.delClass(document.body, "z-overy")
      if(!dom.msie || dom.msie > 7){
        anchor.style.display = "none";
        window.scrollTo(0, this.topMargin);
      }
    }


  })


  return contentModal;

})