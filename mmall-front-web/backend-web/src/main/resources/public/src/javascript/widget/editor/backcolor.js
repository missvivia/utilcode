/*
 * ------------------------------------------
 * 背景色执行命令封装实现文件
 * @version  1.0
 * @author   weiwenqing(wqwei@corp.netease.com)
 * ------------------------------------------
 */
define(['{lib}util/editor/command/color.js', '{lib}ui/editor/command/color.simple.js'], function() {
  var p = NEJ.P('nej.ut.cmd');
  var i = NEJ.P('nej.ui.cmd');
  var e = NEJ.P('nej.e');
  if ( !! p._$$BackColor) return;
  /**
   * 背景色执行命令封装
   * @class   {nej.ut.cmd._$$BackColor} 背景色执行命令封装
   * @extends {nej.ui.cmd._$$Color}
   * @param   {Object} 可选配置参数，已处理参数列表如下
   *
   */
  p._$$BackColor = NEJ.C();
  var proto = p._$$BackColor._$extend(p._$$Color);
  /**
   * 命令名称
   * @type String
   */
  p._$$BackColor.command = 'hiliteColor';
  /**
   * 控件重置
   * @protected
   * @method {__reset}
   * @param  {Object} 可选配置参数
   * @return {Void}
   */
  proto.__reset = function(_options) {
    this.__supReset(_options);
    this.__crtBgColorNode = this.__toolbar.__command.crtBgColor;
  };
  /**
   * 卡片内容变化回调
   * @protected
   * @method {__onChange}
   * @param  {String} 颜色值
   * @return {Void}
   */
  proto.__onChange = function(_color) {
    var bgColorNode;
    this.__editor.crtBgColor = _color;
    if ( !! this.__crtBgColorNode) {
      bgColorNode = e._$getChildren(this.__crtBgColorNode);
      bgColorNode[0].style.backgroundColor = _color;
    }
    this.__editor._$execCommand(this.__name, _color);
  };
  /**
   * 显示卡片
   * @protected
   * @method {__doShowCard}
   * @return {nej.ui._$$CardWrapper} 卡片实例
   */
  proto.__doShowCard = function() {
    this.__fopt.clazz = 'm-colorPick';
    i._$$SimpleColorCard._$allocate(this.__fopt)._$show();
  };
  // regist command implemention
  p._$$BackColor._$regist();
});