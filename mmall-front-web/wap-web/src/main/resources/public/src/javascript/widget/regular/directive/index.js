/*
 * --------------------------------------------
 * BaseComponent 指令扩展
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
  "{pro}extend/util.js",
  "{pro}extend/config.js",
  "{pro}widget/gesture/gesture.js",
  '{lib}util/ajax/xdr.js',
  '{lib}base/event.js',
  "{pro}lib/regularjs/dist/regular.js"
], function( _ , config, gesturify ,j, t0, Scrollspy){

  // Regular中的dom帮助函数
  var dom = Regular.dom;

  var directives = {
    /**
     * lazymodel用来处理，当r-model表达式也是由表达式生成的的情况，
     * 我们先通过get获取字符串，然后用`r-model`处理
     * @return {Function} [distroy]
     */
    "r-lazy-model": function(elem, value){
      var dmodel = Regular.directive("r-model"),
        value = this.$get(value);
        
      var destroy = dmodel.link.call(this.$context, elem, value, "r-model");

      window.app = this.$context;
      return destroy;
    }
  }

  var events = {

    // 使用nej的mouseenter
    "mouseenter": function(elem, fire, attrs){
      v._$addEvent(elem, "mouseenter", fire)
      return function(){
        v._$delEvent(elem, "mouseenter", fire)
      }
    },
    // 使用nej的mouseleave
    "mouseleave": function(elem, fire, attrs){
      v._$addEvent(elem, "mouseleave", fire)
      return function(){
        v._$delEvent(elem, "mouseleave", fire)
      }
    },
    "tap": function(node, fire){
      gesturify(node);
      node.addEventListener("tap", fire)
    }


  }






  return {
    events: events,
    directives: directives
  };

})