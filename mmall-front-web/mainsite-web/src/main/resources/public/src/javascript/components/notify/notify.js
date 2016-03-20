/*
 * --------------------------------------------
 * 全局notify接口
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
  "text!./notify.html",
  "{pro}lib/regularjs/dist/regular.js",
  '{pro}extend/util.js',
  '{pro}extend/config.js'
  ], function(tpl, R, _ , config){

  var Notify = Regular.extend({
    template: tpl,
    //默认时间 
    duration: 2000,
        
    // icon对应
    iconMap: {
      "error": "remove-circle",
      "success": "ok-sign",
      "warning": "warning-sign",
      "info": "info-sign",
      "loading": "info-sign"
    },
    config: function(data){
      _.extend(data, {
        position: 'right',
        isHide:true
      })
    },
    // 初始化后的函数
    init: function(){
      // 证明不是内嵌组件
      if(this.$root == this) this.$inject(document.body);
    },
    /**
     * 增加一个提醒，添加到队伍前方
     * @param  {String|Object} message 消息或消息对象
     *      -type: error, info ,warning, success, 默认为info
     *      -title: 信息标题，默认为空
     *      -message: notify的内容
     *      -duration: 信息停留时间，-1 为无限. 默认2秒
     * @return {Function}              不等待定时器，删除掉此提醒
     */
    notify: function(message){
      if(typeof message === "string"){
        message = {
          message: message
        }
      }
      _.extend(message,{
        type: 'info',
        duration: this.duration
      })
      this.data.isHide = false;
      this.data.message = message;
      this.$update();
      var clearFn = this.clear.bind(this, message);
      this.$timeout(clearFn, message.duration==-1? 1000*3600 * 1000: message.duration );
      return clearFn;
    },
    clear: function(message){
      this.$update("isHide", true);
    }
    // 使用timeout模块
  }).use('timeout');

  
  // 单例, 直接初始化
  var notify = new Notify({});

  notify.Notify = Notify;

  return notify;



  /**
   * 使用:
   *    notify.notify(msg) 开始进度条
   *    notify.show(msg)   显示信息
   *    notify.showError(msg) 显示错误 , show的简便接口
   */

})