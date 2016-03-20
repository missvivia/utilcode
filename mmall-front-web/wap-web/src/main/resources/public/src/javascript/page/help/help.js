/**
 *帮助中心：
 * 兼容性：Wphone(IE10+)
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
    'base/klass',  // 基类 对应元素：_k
    'pro/widget/module'  // 公共类库 对应元素：_m
], function(_k,_m,_p,_o,_f,_r,_pro) {
    /**
     * 帮助页模块
     *
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){

        this.__supReset(_options);
        this.__list = document.querySelector('.list');
        this.__tabs = [].slice.call(document.querySelectorAll('.pack'));
        this.__tabH = 45;

        // 初始化下拉菜单位置
        this.__tabs.forEach(function(el){
            var mList = el.querySelector('.mList');
            mList.style.top = - mList.offsetHeight + 'px';
        })

        this.__list.addEventListener('click', this.__checkHelp._$bind(this),false);

    }
    /**
     *transition结束回调函数
     * @param  {Element}   arg0 - transition对象
     * @param  {Fuction}   arg1 - transition结束回调函数
     * @param  {Boolean}   arg2 - 标记是否取消注册
     */
    _pro.__atTransitionEnd = function(_el, _callback, _permanent) {

        // 事件兼容性
        var __events = ['webkitTransitionEnd', 'transitionend', 'oTransitionEnd', 'MSTransitionEnd', 'msTransitionEnd'];

        function __fireCallBack() {
            _callback();
            if (!_permanent) {
                for (var i = 0, l = __events.length; i < l; i++) {
                    _el.removeEventListener(__events[i], __fireCallBack, false)
                }
            }
        }

        if (!!_callback) {
            for (var i = 0, l = __events.length; i < l; i++) {
                _el.addEventListener(__events[i], __fireCallBack, false)
            }
        }
    }
    /**
     *下拉菜单操作
     * @param  {object}    arg0 - 事件对象
     */
    _pro.__checkHelp = function(_event){

        var __target = _event.target;
        var __tab = __target.parentNode;
        var __mList = __tab.querySelector('.mList');

        // 下拉显示的高度
        var __downH = !!__mList? __mList.offsetHeight: 0;

        if(__target.className === 'cnt1'){

            __mList.style.zIndex = -999;

            __tab.classList.toggle("check");
            __mList.classList.toggle("show");

            // 二级菜单展示
            if (__mList.classList.contains("show")) {

                __mList.style.top = 0;
                __tab.style.height = __downH + this.__tabH + 'px';

                this.__atTransitionEnd(__mList, function() {

                    if (__mList.classList.contains("show")) {

                        __mList.style.zIndex = 999;
                    }
                })

            // 二级菜单隐藏
            } else {

                __mList.style.top = -__downH + 'px';
                __tab.style.height = this.__tabH + 'px';
                __mList.style.zIndex = -999;
            }
        }
    }

    _p._$$Module._$allocate();

})