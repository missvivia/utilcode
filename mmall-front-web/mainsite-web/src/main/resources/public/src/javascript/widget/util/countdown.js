/**
 *
 * 倒计时实现文件
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
NEJ.define([
    'base/element',
    'base/event',
    'base/util',
    'util/template/tpl',
    'util/template/jst',
    'util/timer/interval'
],function(_e,_v,_u,_t0,_t1,_t2,_p,_o,_f,_r){
    var _step = 1000,
        _deftpl = '<p><span>${dd}天</span><span>${HH}小时</span><span>${mm}分钟</span><span>${ss}秒</span></p>',
        _timermap = {};

    /**
     * 组装时间数据
     * @param  {[type]} _time [description]
     * @return {[type]}       [description]
     */
    _p.__getMeta = function(_t){
        var _time = _t / 1000,
            _s = Math.floor(_time%60),
            _m = _time/60,
            _h = _time/3600,
            _d = _time/86400,
            _m = Math.floor(_m)%60,
            _h = Math.floor(_h)%24,
            _d = Math.floor(_d);
        var _ss = _s > 9 ? _s.toString() : '0' + _s,
            _mm = _m > 9 ? _m.toString() : '0' + _m,
            _hh = _h > 9 ? _h.toString() : '0' + _h,
            _dd = _d > 9 ? _d.toString() : '0' + _d,
            _meta = {
                'dd'  : _dd,
                'HH'  : _hh,
                'mm'  : _mm,
                'ss'  : _ss
            };
        return _meta;
    }

    /**
     * 清除倒计时
     * @param  {[type]} _key [description]
     * @return {[type]}      [description]
     */
    _p._$clearCountdown = function(_key){
        if (_timermap[_key] && _timermap[_key].stil){
           _timermap[_key].stil = _t2.cancelInterval(_timermap[_key].stil);
           _timermap[_key] = {};
           delete _timermap[_key];
        }
    };

    _p.__formatTime = function(_endtime){
        var _t = Math.ceil((_endtime.endTime - new Date().getTime()) / 1000) * 1000;
            _t = _t <= 0 ? 0 : _t;
        return _t;
    }
    /**
     * 倒计时
     * @param  {Number}       _time - 初始时间毫秒数
     * @param  {String|Node}  _tpl  - 倒计时模版
     * @param  {Function}     _callback - 倒计时模版
     * @return {Object}       obj   - html:模版填充结果,key:倒计时的模版,meta：倒计时数值对象,down:是否倒计时结束
     */
    _p._$countdown = (function(){
        var _setContent = function(_key,_meta,_isdown,_element){
            _html = _t1._$get(_key,_meta);
            var _opt = {html:_html,key:_key,meta:_meta,isdown:_isdown};
            if (_element){
                _element.innerHTML = _html;
            }
            _timermap[_key].callback(_opt);
        };
        var _timer = function(_key,_element){
            var _t = _p.__formatTime(_timermap[_key]);
            if (_t <= 0){
                var _meta = {
                    'dd'  : '00',
                    'HH'  : '00',
                    'mm'  : '00',
                    'ss'  : '00'
                };
                _setContent.call(this,_key,_meta,true,_element);
                _p._$clearCountdown(_key);
                return;
            }else{
                var _meta = _p.__getMeta(_t);
                _setContent.call(this,_key,_meta,false,_element);
            }
        };
        return function(_element,_lefttime,_options){
            var _key,
                _realtpl,
                _lefttime = parseInt(_lefttime),
                _startTime = new Date().getTime(),
                _endTime = new Date(_startTime + _lefttime).getTime(),
                _callback = _f;
            _element = _e._$get(_element);

            if (_u._$isString(_options)){
                _realtpl = _options;
            }else{
                _realtpl  = _options.format||_deftpl;
                _callback = _options.onchange||_f;
            }
            _key = !!_t1._$get(_realtpl) ? _realtpl : _t1._$add(_realtpl);
            if (!!_timermap[_key]){
               _p._$clearCountdown(_key);
            }
            _timermap[_key] = {}
            _timermap[_key].endTime = _endTime;
            _timermap[_key].callback = _callback;
            var _html = _t1._$get(_key,_meta),
                _end = _p.__formatTime(_timermap[_key]),
                _meta = _p.__getMeta(_end),
                _opt = {html:_html,key:_key,meta:_meta,isdown:_end<=0 ? true: false};
            _timermap[_key].callback(_opt);
            if (_end > 0){
                _timermap[_key].stil = _t2.requestInterval(_timer._$bind(null,_key,_element),_options.updatetime||_step);
            }
            return _key;
        };
    })();

    return _p;
});
