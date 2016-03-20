/*
 * ------------------------------------------
 * 设置当前背景颜色命令封装实现文件
 * @version  1.0
 * @author   luzhongfang(luzhongfang@corp.netease.com)
 * ------------------------------------------
 */
var f = function() {
    var _p = NEJ.P('nej.ut.cmd');
    var _t = NEJ.P('nej.ut');
    var _e = NEJ.P('nej.e');
    var _proCrtColor;
    if ( !! _p._$$CrtBgColor) return;
    /**
     * 加粗执行命令封装
     * @class   {nej.ut.cmd._$$CrtBgColor} 加粗执行命令封装
     * @extends {nej.ut.cmd._$$SimpleCommand}
     * @param   {Object} 可选配置参数，已处理参数列表如下
     */
    _p._$$CrtBgColor = NEJ.C();
    _proCrtColor = _p._$$CrtBgColor._$extend(_t._$$EditorCommand);
    /**
     * 命令名称
     * @type String
     */
    _p._$$CrtBgColor.command = 'crtBgColor';
    /**
     * 控件重置
     * @protected
     * @method {__reset}
     * @param  {Object} 可选配置参数
     * @return {Void}
     */
    _proCrtColor.__reset = function(_options) {
        this.__supReset(_options);
        var bgColorNode;
        //初始化颜色
        this.__crtBgColorNode = this.__toolbar.__command.crtBgColor;
        if ( !! this.__crtBgColorNode) {
            bgColorNode = _e._$getChildren(this.__crtBgColorNode);
            bgColorNode[0].style.backgroundColor = '#000';
        }
    };
    /**
     * 执行命令
     * @method {_$execute}
     * @param  {Object} 执行参数
     * @return {nej.ut.cmd._$$SimpleCommand}
     */
    _proCrtColor._$execute = function() {
        var _color = this.__editor.crtBgColor || '#000';
        this.__editor._$execCommand('hiliteColor', _color);
        return this;
    };

    // regist command implemention
    _p._$$CrtBgColor._$regist();
};
NEJ.define(['{lib}util/editor/command/simple.js'], f);