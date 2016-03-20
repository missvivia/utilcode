/**
 *
 * 工具类
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
/* pro/widget/util/util */
NEJ.define([
    'base/element',
    'base/event',
    'base/util'
],function(_e,_v,_u,_p,_o,_f,_r){
    /**
     * 检查email和手机号
     * @param  {[type]} _key [description]
     * @return {[type]}      [description]
     */
    _p._$checkEmailAndPhone = function(_value){
        var _value = _value.trim();
        if (/^[\w-\.]+@(?:[\w-]+\.)+[a-z]{2,6}$/i.test(_value)
            ||/^(13|15|18)\d{9}$/.test(_value)){
            return true;
        }
        return false;
    };


    return _p;
});
