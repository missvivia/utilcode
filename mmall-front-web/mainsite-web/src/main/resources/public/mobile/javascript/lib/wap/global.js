/**
 *全局函数拓展
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define(function (_p, _o, _f, _r) {

    /**
     *字符串前后空白去除
     * @return {String}         - 去除空白后的字符串
     *
     */
    String.prototype._$trim = function () {
        return this.replace(/(^\s*)|(\s*$)/g, "");
    }

    /**
     *当前函数this拓展
     * @param  {Object}    arg0 - 函数内this
     * @return {Function}       - 绑定后的函数
     *
     */
    Function.prototype._$bind = function () {
        var
            _r = [], // nej兼容
            _args = arguments, // 获取参数
            _object = arguments[0], // 获取目标
            _function = this; // this赋值

        // 参数绑定
        return function () {
            var _argc = _r.slice.call(_args, 1);
            _r.push.apply(_argc, arguments);
            return _function.apply(_object || null, _argc);
        };
    }

    /**
     *数组遍历 return false中断
     * @param  {Function}  arg0 - 回调函数
     * @param  {Object}    arg1 - 回调函数内this 可空
     *
     * ```javascript
     * [1,2,3,4,5]._$(function (value, index, array) {
     *     //something
     *     if(index == 3) return false;
     * })
     * ```
     *
     */
    Array.prototype._$forEach = function (_callback, _thisArg) {
        if (this == null || {}.toString.call(_callback) != "[object Function]") {
            return false;
        }
        for (var i = 0, __len = this.length; i < __len; i++) {
            if (_callback.call(_thisArg, this[i], i, this) === false) break;
        }
    };

})