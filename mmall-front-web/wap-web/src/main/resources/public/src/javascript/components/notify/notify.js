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
      "success": "u-ok",
      "fail": "u-fail",
      "error": "u-fail",
      "warning": "u-warning"
    },
    config: function(data){
      _.extend(data, {
        messages: '',
        isHide: true,
        type:'',
        clazz:'hide'
      })
    },
    // 初始化后的函数
    init: function(){
      // 证明不是内嵌组件
      if(this.$root == this) this.$inject(document.body);
    },
    /**
     * 汽泡提示
     * @param  {String|Object} message 消息或消息对象
     *      -type: warning, success,fail 默认为success
     *      -message: notify的内容
     *      -duration: 信息停留时间，-1 为无限. 默认2秒
     * @return {Function}              不等待定时器，删除掉此提醒
     */
    notify: function(message,type){
      if(message && typeof message === "object"){
        type = message.type;
        message = message.message;
      }
      this.data.isHide = false;
      
      this.data.message = message;
      this.data.type = type||'success';
      this.$update();
      
      var clearFn = this.clear.bind(this, message);
      this.$timeout(function(){
    	  this.data.clazz = 'show'; //动画触发，动画时间为 5秒
      }._$bind(this), 0 );
      this.$update();
      if(!!this.__timer){
    	  clearInterval(this.__timer);
      }
      this.__timer = this.$timeout(clearFn, this.duration==-1? 1000*3600 * 1000: this.duration );
      return clearFn;
    },
   
    /**
     * 汽泡提示，与notify一致，但是会清理所有消息，用于唯一的消息提醒
     * @param  {String|Object} message 消息或消息对象
     *      -type: warning, success,fail 默认为success
     *      -message: notify的内容
     *      -duration: 信息停留时间，-1 为无限. 默认2秒
     * @return {Function}              不等待定时器，删除掉此提醒
     */
    show: function(message, type){
      return this.notify(message,type);
    },
    /**
     * 与notify一致，但是会清理所有消息，用于唯一的消息提醒
     * @param  {String|Object} message 消息或消息对象
     * @return {Function}              不等待定时器，删除掉此提醒
     */

    showError: function(message,options){
      options = _.extend(options||{}, {
        type: 'error'
      })
      return this.show(message, options);
    },
    clear: function(message){
      this.data.clazz = 'hide';
      this.$update();
      this.$timeout(function(){
    	  this.data.isHide = true;
      }._$bind(this), 500 );
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