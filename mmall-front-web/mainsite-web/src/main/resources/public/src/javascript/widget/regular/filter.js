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
        var _smap = ['未审核','审核中','审核通过','审核拒绝'];
        return function(_status){
            return _smap[_status]||_smap[0];
        };
    })();

    /**
     * by hzwuyuedong
     * 字符串截取， 中英文都算一个len
     */
    _p.cutstr = function(str, len) {
      var temp,
        icount = 0,
        patrn = /[^\x00-\xff]/,
        strre = "";
      for (var i = 0; i < str.length; i++) {
        if (icount < len - 1) {
          temp = str.substr(i, 1);
          if (patrn.exec(temp) == null) {
            icount = icount + 1
          } else {
            icount = icount + 2
          }
          strre += temp
        } else {
          break;
        }
      }
      return strre + "..."
    };


    _p.concatObjValue = function(_object, _str){
      var _join = [];
      _u._$forIn(_object,function(_item,_index,_this){
        _join.push(_item);
      });
      return _join.join(_str);
    };

    /**
     * by hzwuyuedong
     * 浮点数值保留指定位数小数点
     */
    _p.fixed = function(_data, _len){
      var _num= parseFloat(_data, 10), _tmp;
      if(_len == null){
        _len = 2;
      }
      if(_u._$isNumber(_num)){
        _tmp = _u._$fixed(_num, 2);
        var _s = _tmp.toString();
        var _rs = _s.indexOf('.');
        if (_rs < 0) {
          _rs = _s.length;
          _s += '.';
        }
        while (_s.length <= _rs + _len) {
          _s += '0';
        }
        return _s;
      }else{
        return '';
      }
    };

    return _p;
});