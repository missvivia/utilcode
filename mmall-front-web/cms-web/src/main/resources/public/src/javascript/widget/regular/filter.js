/**
 * 表单字段生成组件
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

NEJ.define([
    'base/util'
], function(_u,_p){
    // common filter
    _p.format = function(date, format){
        return _u._$format(date, format || "yyyy-MM-dd")
    }
    _p.escape = _u._$escape;
    _p.status = (function(){
        var _smap = ['','未审核','审核中','审核通过','审核拒绝'];
        return function(_status){
            return _smap[_status]||_smap[0];
        };
    })();
    return _p;
});