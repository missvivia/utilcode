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
  "{pro}lib/regularjs/dist/regular.js"
  ], function( _ , config){

  // Regular中的dom帮助函数
  var dom = Regular.dom,
    noop = function(){};


  function requestModule(Component){
    Component.implement({
      $request: function(url, options){
        var 
        _.request(url)
      }
    })
  }




  return {
    request: requestModule
  }

})