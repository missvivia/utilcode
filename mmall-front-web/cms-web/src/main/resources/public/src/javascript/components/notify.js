/*
 * --------------------------------------------
 * 全局进度条控件
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
  "{pro}lib/regularjs/dist/regular.js",
  '{pro}extend/config.js'
  ], function(R, config){

  var Notify = Regular.extend({
    template: 
        '<div class="notify-{{directStr}}">\
          {{#list messages as message}}\
          <div class="notify notify-error">\
            <button class="notify-close-button" role="button">×</button>\
            <div class="notify-title" r-hide={{!title}}>{{title}}</div>\
            <div class="notify-message">Inconceivable!</div>\
          </div>\
          {{/list}}
        </div>',
    // 默认属性
    // 计算属性
    computed: {
      directStr: function(data){
        var channels = mix(data.startColor, data.endColor, 100 - data.percent);
        return "rgb(" + channels[0] + "," + channels[1] + "," +channels[2] + ")";
      } 
    },
    // 初始化后的函数
    init: function(){
      // 证明不是内嵌组件
      if(this.$root == this) this.$inject(document.body);
    },
    showMessage: function(){

    }
    // 使用timeout模块
  }).use('timeout');


  // 单例, 直接初始化
  return new Notify();

  /**
   * 使用:
   *    progress.start() 开始进度条
   *    progress.end(isError) 结束进度条
   *    progress.move() 移动到某个进度条位置，最大100
   */

})