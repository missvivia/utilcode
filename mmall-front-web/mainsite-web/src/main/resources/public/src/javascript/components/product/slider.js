/**
 * 首页slider小图，应急写的，都是写死的伪组件
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
define([
  'pro/extend/util',
  'pro/components/notify/notify',
  "pro/widget/BaseComponent"
  ], function(_, notify,BaseComponent){

  var dom = Regular.dom;

  var DetailSlider = BaseComponent.extend({
    // 先写死step
    step: 74 + 15,
    init: function(){
      this.current = 0;

      var data = this.data;
      var left = this.left = nes.one('.cur.cur-left', this.parent);
      var right = this.right = nes.one('.cur.cur-right', this.parent);
      dom.on(left, 'click', this.prev.bind(this))
      dom.on(right, 'click' ,this.next.bind(this))
      var lists = this.lists = nes.all('.list li', this.parent);
      this.list = nes.one('.list ul', this.parent);
      this.checkState();
    },
    checkState: function(){
      dom[this.current + 5 >= this.lists.length? "addClass": "delClass"](this.right, 'cur-dis');
      dom[this.current <= 0? "addClass" : "delClass"](this.left, 'cur-dis');
      this.list.style.top = (-this.current * this.step) + "px";
    },
    next: function(){
      if(dom.hasClass(this.right, 'cur-dis')) return;
      this.current += 1;
      this.checkState();
    },
    prev: function(){
      if(dom.hasClass(this.left, 'cur-dis')) return;
      this.current -= 1;
      this.checkState();
    }

  });

  return DetailSlider;

})