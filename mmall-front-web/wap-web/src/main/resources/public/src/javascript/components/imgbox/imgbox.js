/*
 * --------------------------------------------
 * 全局notify接口
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
  "base/element",
  "pro/extend/util",
  "pro/widget/BaseComponent",
  "pro/widget/gesture/gesture",
  "regular!./imgbox.html"
  ], function(e, _, BaseComponent, gesturify , tpl){


  return BaseComponent.extend({
    template: tpl,
    duration: 2000,
    config: function(data){
      var box = e._$getPageBox();
      _.extend(data, {
        index:0,
        scale: 1,
        ratio: 1,
        translate: {
          x:0,
          y:0
        },
        tbase: {
          x:0,
          y:0
        },
        slides: [
        ],
        clazz:'hide',
        box: box,
        slideStyle: {
          width: box.clientWidth + "px",
          height: box.clientHeight + "px"
        },
        innerStyle: {
          "lineHeight": box.clientHeight + "px"
        }

      })
    },
    transform: function(idx){
      var data = this.data;
      if(idx !== this.data.index) return "scale(1) translate3d(0,0,0)";
      else{
        return "translate3d("
            +(data.translate.x + data.tbase.x)+"px,"+(data.translate.y+data.tbase.y)+ "px,0)" 
            +"scale("+ ((data.scale||1)*data.ratio).toFixed(3)+")"
      }
    },
    resetTransform: function(){
      var data = this.data;
        data.tbase = {
            x: 0,
            y: 0
          }
          data.translate = {x:0, y:0}
          data.scale = 1;
          data.ratio = 1;
    },
    close: function(ev){
      // ev.event.preventDefault();
      this.destroy();
    },
    // 初始化后的函数
    nav: function(index){
      this.data.index = index;
    },
    init: function(){
      // 证明不是内嵌组件
      if(this.$root == this) this.$inject(document.body);

      var el = gesturify(this.$refs.sliders, {stop: false})  
      var self = this;
      var data = this.data;
      var holder = false;
      var transform;


      el.addEventListener("dragmove", function(e){
        e.preventDefault();
        data.duration = false;
        data.isDrag = true;

        if(data.scale <= 1 || transform) return;
        data.translate = {x:e.x, y:e.y}
        self.$update();
      })
      el.addEventListener("dragend", function(e){
        data.isDrag = false;
        if(data.scale <= 1 || transform){

        }else{
          data.tbase = {
            x: data.translate.x + data.tbase.x,
            y: data.translate.y + data.tbase.y
          }
        }
        if(Math.abs(data.tbase.x) > 240 || Math.abs(data.tbase.y) > 240){
          self.resetTransform();
        }else{
        data.translate = {x:0, y:0}
        }
        self.$update();
      })
      el.addEventListener("transform", function(e){
        data.duration = false;
        if(data.isDrag) {

          data.isDrag = false;
          if(data.scale <= 1 || transform){

          }else{
            data.tbase = {
              x: data.translate.x + data.tbase.x,
              y: data.translate.y + data.tbase.y
            }
          }
          if(Math.abs(data.tbase.x) > 240 || Math.abs(data.tbase.y) > 240){
            self.resetTransform();
          }else{
          data.translate = {x:0, y:0}
          }
        }
        transform = true;
        var ratio = (e.scale-1) / 2 + 1;
        data.ratio = ratio;
        self.$update();
      })

      el.addEventListener("touchend", function(ev){

        transform = false;
        if(ev.touches && ev.touches.length > 0) return;
        transform = false;
        data.duration = true;

        data.scale *= data.ratio 
        data.ratio = 1;
        if(data.scale < 1){
          self.resetTransform();
        } 
        if(data.scale > 2) data.scale = 2;


        self.$update();

      })
      el.addEventListener("touchstart", _.throttle(function(ev){
        ev.preventDefault();
        if(ev.touches && ev.touches.length > 1) return;
        data.duration = false;
        self.$update();
      },50))

      el.addEventListener("swip", function(e){

        e.preventDefault();
        if(data.scale !=1) return;
        self.resetTransform();
        var deltY = e.end.y-e.start.y;
        var deltX = e.end.x-e.start.x;
        if(Math.abs(deltY/deltX) > 3) return;
        data.index += deltX<0? 1:-1;
        // data.index = (data.index + data.slides.length) % data.slides.length
        if(data.index >= data.slides.length) data.index = data.slides.length-1;
        if(data.index < 0) data.index =0;
      })
      el.addEventListener("dbtap", function(e){
        if(data.scale === 1){
          self.data.scale = 2;
        }else{
          self.resetTransform();
        }
        
        self.$update();
      })

      // // el.addEventListener("tap", function(){
      // //   log("window 的tap也触发了")
      // // })
      // window.addEventListener("dbtap", function(e){
      //   log("window 的dbtap也触发了")
      // })

      // window.addEventListener("hold", function(){
      //   log("window 的hold也触发了")
      // })

      // window.addEventListener("swip", function(){
      //   log("window 的swip")
      // })

    }
    // 使用timeout模块
  }).directive("r-transform", function(elem, value){
    this.$watch(value, function(val){
      _.transform(elem, val);
    })
  })

function log(msg){
  // if(!msg) nes.one("#logbox").innerHTML = "";
  // nes.one("#logbox").innerHTML += "<p>" + msg + "</p>"
}



})