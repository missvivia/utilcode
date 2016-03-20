/**
 * 放大镜效果
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
NEJ.define([
  "util/chain/chainable",
  "base/element",
  "text!./magnifier.html",
  "pro/widget/BaseComponent"
  ], function($, e ,tpl, BaseComponent){
  var dom = Regular.dom;

  var Magnifier = BaseComponent.extend({
    name: "magnifier",
    thumb: null,
    template: tpl,
    // is called before compile. 一般用来处理数据
    config: function(data){
      if(typeof this.thumb === "string") this.thumb = nes.one(this.thumb);
      if(!this.thumb) throw "必须传入触发节点"
      if(!data.width)  data.width = parseFloat(e._$getStyle(this.thumb, "width"));
      if(!data.height) data.height = parseFloat(e._$getStyle(this.thumb, "height"));
      data.ratio =  data.ratio || 2;
      data.thumboffset = e._$offset(this.thumb);
      data.url = this.thumb.src;
      data.offsetX = 0;
      data.offsetY = 0;
      var self = this;
      dom.on(window, 'resize', function(){
        data.thumboffset = e._$offset(self.thumb);
      })
    },
    init: function(){
      this.createOvery();

      $(this.thumb.parentNode)._$on({
        'mouseenter': this.onStart.bind(this),
        "mousemove": this.onMove.bind(this),
        "mouseleave": this.onEnd.bind(this)
      });
      this.$inject(document.body);
    },
    // 创建遮盖层
    createOvery: function(){
      var data = this.data;
      this.overy = dom.create("div");
      this.overy.className = "magnifier-cursor";
      this.overy.style.width = data.width / data.ratio + "px";
      this.overy.style.height = data.height / data.ratio+ "px";
    },
    // 重写或者 new Magnifier 时传入
    getLargeUrl: function(url){
      var data = this.data;
      return url.split("?")[0] + "?imageView&quality=90&thumbnail="+ data.ratio * data.width +"x"+ data.ratio * data.height;
    },
    getPosition: function(){
      var data =this.data;
      var res= {
        left: -Math.floor(data.width * data.ratio * data.offsetX) + "px",
        top: -Math.floor(data.height * data.ratio * data.offsetY) + "px",
        width: Math.floor(data.width * data.ratio) + "px",
        height: Math.floor(data.height * data.ratio) + "px"
      }
      return res;
    },
    onStart: function(ev){
      this.start();
      this.onMove(ev);
    },
    onMove: function(ev){
      var data = this.data;
      var ratio = data.ratio;
      var offsetX = ev.pageX - data.thumboffset.x;
      var offsetY = ev.pageY - data.thumboffset.y;
      data.offsetX = offsetX/ data.width -0.5/ratio;
      data.offsetY = offsetY/ data.height -0.5/ratio;

      if(data.offsetX < 0) data.offsetX = 0;
      if(data.offsetX > 1- 1/ratio) data.offsetX = 1- 1/ratio;
      if(data.offsetY < 0) data.offsetY = 0;
      if(data.offsetY > 1- 1/ratio) data.offsetY = 1- 1/ratio;

      this.overy.style.left = data.offsetX * data.width  + "px";
      this.overy.style.top = data.offsetY * data.height + "px";
      this.$update();
    },
    onEnd: function(ev){
      var data = this.data;
      dom.remove(this.overy);
      data.show = false;
      this.$update();
    },
    start: function(){
      var data = this.data;
      data.url = this.getLargeUrl(this.thumb.src);
      data.show = true;
      dom.inject(this.overy, this.thumb.parentNode);
    }
  });

  return Magnifier;

})