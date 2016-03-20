/**
 * scrollspy组件，
 * 
 * author hzzhenghaibo@corp.netease.com
 */

define([
  "{pro}extend/util.js",
  "base/element",
  "{pro}widget/BaseComponent.js"
], function(_, e, BaseComponent, tpl, p,o,f,r){



  /**
   * 定义
   */
  var dom = Regular.dom;

  var ScrollSpy = BaseComponent.extend({
    //pre compile
    config: function(data){
      _.extend(data, {
        speed: 50
      })
    },
    // after compile
    init: function(){
      var data = this.data;
      var elem = data.elem;
      this.elem = elem;
      this.elemTop = e._$offset(elem).y;
      this.links = nes.all('[data-target]', elem);
      this.refresh();
      // 处理scroll
      var handleScroll = this.handleScroll.bind(this)
      var self = this;
      // 处理点击跳转
      function smoothTo(ev){
        var link = this;
        var href = dom.attr(link, 'data-target');
        var target = e._$get(href);
        self.refresh();
        if(target){
          _.smoothTo(target);
          ev.preventDefault();
        }
      }
      dom.on(window, 'scroll', handleScroll);
      dom.on(window, 'load', this.refresh.bind(this));
      this.links.forEach(function(link){
        dom.on(link, "click", smoothTo)
      })

      /**
       * 销毁组件
       */
      this.$on("destroy", function(){
        dom.off(window, 'scroll', handleScroll)
        this.links.forEach(function(link){
          dom.off(link, smoothTo)
        })
      })

      handleScroll();
    },
    //刷新position位置
    refresh: function(){
      // 参考线
      var guides = this.guides = [];
      var links = this.links;
      this.links.forEach(function(link){
        var target = e._$get(dom.attr(link, 'data-target'))
        if(target){
          var offset = e._$offset(target)
          guides.push({
            link: link,
            distance: offset.y - 50,
            target: target
          })
        }
      })
    },
    // 处理scroll时的spy
    handleScroll: function(){
      var guides = this.guides;
      var scrollTop = this.getScrollTop();
      // 处理elem的fixed
      if(scrollTop > this.elemTop) {
        e._$addClassName(this.elem, 'z-fixed')
        this.$emit("fix", true);
      } else{
        e._$delClassName(this.elem, 'z-fixed')
        this.$emit("fix", false);
      }
        

      for(var i = 0, len = guides.length; i < len ;i++){
        // 当scrollTop的大小
        if(scrollTop <= guides[i].distance + guides[i].target.offsetHeight){
          this.touch(guides[i].link);
          break;
        }
      }
    },
    // 处理link的active状态
    touch: function(link){
      if(!e._$hasClassName(link, 'active')){
        this.links.forEach(function(link){
          e._$delClassName(link, "active")
        })
        e._$addClassName(link, "active");
      }
    },
    getScrollTop: function(){
      return Math.max(document.body.scrollTop, document.documentElement.scrollTop);
    }
  })


  return ScrollSpy;

})