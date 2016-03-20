/*
 * ------------------------------------------
 * 文字颜色执行命令封装实现文件
 * @version  1.0
 * @author   weiwenqing(wqwei@corp.netease.com)
 * ------------------------------------------
 */
define(['{lib}util/editor/command/color.js', '{lib}ui/editor/command/color.simple.js'], function() {
  var p = NEJ.P('nej.ut.cmd');
  var i = NEJ.P('nej.ui.cmd');
  var e = NEJ.P('nej.e');
  if ( !! p._$$ForeColor) return;
  /**
   * 文字颜色执行命令封装
   * @class   {nej.ut.cmd._$$ForeColor} 文字颜色执行命令封装
   * @extends {nej.ui.cmd._$$Color}
   * @param   {Object} 可选配置参数，已处理参数列表如下
   *
   */
  p._$$ForeColor = NEJ.C();
  var proto = p._$$ForeColor._$extend(p._$$Color);
  /**
   * 命令名称
   * @type String
   */
  p._$$ForeColor.command = 'foreColor';

  /**
   * 控件重置
   * @protected
   * @method {__reset}
   * @param  {Object} 可选配置参数
   * @return {Void}
   */
  proto.__reset = function(_options) {
    this.__supReset(_options);
    this.__crtColorNode = this.__toolbar.__command.crtColor;
  };
  /**
   * 卡片内容变化回调
   * @protected
   * @method {__onChange}
   * @param  {String} 颜色值
   * @return {Void}
   */
  proto.__onChange = function(_color) {
    var colorNode;
    this.__editor.crtColor = _color;
    if ( !! this.__crtColorNode) {
      colorNode = e._$getChildren(this.__crtColorNode);
      colorNode[0].style.backgroundColor = _color;
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
  p._$$ForeColor._$regist();
});