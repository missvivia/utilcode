/*
 * --------------------------------------------
 * 弹窗组件
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
  "{pro}components/modal/modal.js",
  "{pro}components/item/helper.view.js",
  '{pro}extend/util.js'
  ], function(Modal, HelperView , _){


  /**
   * 弹窗组件
   * 直接调用, this.destroy() 来关闭弹窗和回收
   * Event:
   *   -close (data): 关闭事件, 事件对象即data
   *   -confirm (data): 确认时间，时间对象即data
   * 配置: 
   *   content: 即弹窗body处的内容
   */


  var HelperModal = Modal.extend({
    // 默认属性
    // 计算属性
    content: '<helper-view preview=1 hidebar=1 vaxis={{@(helper.vaxis)}} haxis={{@(helper.haxis)}} body={{@(helper.body)}}></helper-view>',
    init: function(){
      this.supr();
      this.$on({
        "close": this.destroy.bind(this), 
        "confirm": this.destroy.bind(this)

      })
    }
  }).component("helper-view", HelperView)



  return HelperModal;

  /**
   * 使用:
   *    progress.start() 开始进度条
   *    progress.end(isError) 结束进度条
   *    progress.move() 移动到某个进度条位置，最大100
   */

})