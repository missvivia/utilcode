/*
 * ------------------------------------------
 * 超链接执行命令封装实现文件
 * @version  1.0
 * @author   luzhongfang(luzhongfang@corp.netease.com)
 * ------------------------------------------
 */

define(['{lib}util/editor/command/card.js', '{lib}ui/editor/command/link.js'],
    function() {
        var _ = NEJ.P,
            _f = NEJ.F,
            _p = _('nej.ut.cmd'),
            _u = _('nej.u'),
            _i = _('nej.ui.cmd');
        if ( !! _p._$$Link) return;
        /**
         * 超链接执行命令封装
         * @class   {nej.ut.cmd._$$Link } 超链接执行命令封装
         * @extends {nej.ut._$$EditorCommand}
         * @param   {Object} 可选配置参数，已处理参数列表如下
         *
         */
        _p._$$Link = NEJ.C();
        var proto = _p._$$Link._$extend(_p._$$CardCommand);
        /*
         * 命令名称
         * @type {String}
         */
        _p._$$Link.command = 'link';

        /**
         * 卡片内容变化回调，子类实现具体业务逻辑
         * @protected
         * @method {__onChange}
         * @param  {Object} _link 链接地址
         * @return {Void}
         */
        proto.__onChange = function(_link) {
            if (!_link)
                return;
            var _text = (_link.name != '') ? _link.name : _link.href;
            var htmlstr = '<a target="_blank" href="' + _link.href + '">';
            htmlstr += _u._$unescape(_text) + '</a>';
            this.__editor._$execCommand('inserthtml', htmlstr);
            this.__editor._$focus();
        };
        /**
         * 链接错误提示
         * @param  {Object} type 错误类型
         * @return {Void}
         */
        proto.__onError = function() {
            this.__linkCard.__showErrorTips('请输入合法的链接地址（http://或https://）');
        };
        /**
         * 显示卡片，一般子类重写
         * @protected
         * @method {__doShowCard}
         * @return {Void}
         */
        proto.__doShowCard = function() {
        try{
            var text = document.querySelectorAll('iframe')[1]
                .contentDocument.getSelection().getRangeAt(0).startContainer
        }catch(e){


        }
        var parentNode = text && text.parentNode;
        if(parentNode && parentNode.nodeName === 'A') var value = parentNode.href;
        
            
            var selection = this.__editor._$getSelectHtml();
            var clz = 'm-layer m-layer-link ';
            if ( !! selection) {
                clz += ' z-norow';
            }
            this.__fopt.name = selection;
            this.__linkCard = _i._$$LinkCard._$allocate({
                draggable: true,
                destroyable: true,
                maskclazz: 'm-mask m-mask-link',
                name: value || this.__fopt.name,
                clazz: clz,
                title: '添加超链接',
                onchange: this.__onChange._$bind(this),
                onErrorLink: this.__onError._$bind(this)
            });
            this.__linkCard._$show();
            this.__linkCard._$doFocus();
        };

        // regist command implemention
        _p._$$Link._$regist();
    });