/*
 * ------------------------------------------
 * 插入图片执行命令实现文件
 * @version  1.0
 * @author   weiwenqing(wqwei@corp.netease.com)
 * ------------------------------------------
 */
define(['{lib}util/editor/command/card.js' ], function() {
  var using = NEJ.P;
  var p = using('nej.ut.cmd');
  var dw = using('dd.widget');
  var du = using('dd.util');
  if ( !! p._$$InsertImage) return;
  /**
   * 插入图片执行命令封装
   * @class   {nej.ut.cmd._$$InsertImage} 插入图片执行命令
   * @extends {nej.ut.cmd._$$SimpleCommand}
   * @param   {Object} 可选配置参数，已处理参数列表如下
   */
  p._$$InsertImage = NEJ.C();
  var proto = p._$$InsertImage._$extend(p._$$CardCommand);
  /**
   * 命令名称
   * @type String
   */
  p._$$InsertImage.command = 'insertImage';


  /**
   * 显示卡片，一般子类重写
   * @protected
   * @method {__doShowCard}
   * @return {Void}
   */
  proto.__doShowCard = function() {
    console.log('hhaa')
  };
  /**
   * 确认选择图片的回调函数
   * @param  {Object} data 图片数据
   * @return {Void}
   */
  proto.__onConfirmImage = function(data) {

    this.__onChange('inserthtml', data.uri);
  };
  /**
   * 卡片内容变化回调，子类实现具体业务逻辑
   * @protected
   * @method {__onChange}
   * @param  {Object} 插入图片命令
   * @param  {String} 图片地址
   * @return {Void}
   */
  proto.__onChange = function(command, src) {
    //this.__editor._$focus(2);
    var html = '<img src=' + du.transUri(src) + ' />';
    // @TODO: ie throw error , bug works as expect , waiting for fei ge
    try {
      this.__editor._$execCommand(command, html);
    } catch (e) {

    }

  };

  // regist command implemention
  p._$$InsertImage._$regist();
});