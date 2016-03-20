/**
 * fade切换图片实现文件
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'util/event',
    'util/effect/effect.api'
],function(_k,_e,_v,_u,_t,_t0,_p,_o,_f,_r){
	var _pro;

    _p._$$Fade = _k._$klass();
    _pro = _p._$$Fade._$extend(_t._$$EventTarget);

    _pro.__reset = function(_options){
        this.__indexBox = _options.indexBox;
        this.__contentBox = _options.contentBox;
        this.__stop = _options.stop||6000;
        this.__contents = _e._$getChildren(_e._$getChildren(this.__contentBox)[0]);
        this.__indexLis = _e._$getChildren(this.__indexBox);
        this.__max = this.__indexLis.length - 1;
        if (this.__max <= 0){
            return;
        }
        this.__prev = _options.prev;
        this.__next = _options.next;
        this.__addEvent();

        this.__initState();
    };

    _pro.__addEvent = function(){
        this.__doInitDomEvent([
            [this.__indexBox,'mouseover',this.__onMouseOver._$bind(this)],
            [this.__indexBox,'mouseout',this.__onMouseOut._$bind(this)],
            [this.__contentBox,'mouseover',this.__onMouseOver._$bind(this,true)],
            [this.__contentBox,'mouseout',this.__onMouseOut._$bind(this)],
            [this.__prev,'click',this.__onPrev._$bind(this)],
            [this.__next,'click',this.__onNext._$bind(this)]
        ]);
    };

    _pro.__checkIndex = function(_index){
        if (_index > this.__max){
            return 0;
        }
        if (_index < 0){
            return this.__max;
        }
        return _index;
    };

    _pro.__onPrev = function(){
        var _index = parseInt(this.__index) - 1;
        _index = this.__checkIndex(_index);
        this.__doFade(_index,true);
    };

    _pro.__onNext = function(){
        var _index = parseInt(this.__index) + 1;
        _index = this.__checkIndex(_index);
        this.__doFade(_index,true);
    };

    _pro.__initState = function(){
        this.__index = 0;
        _u._$forEach(this.__contents,function(_c,_index){
            _e._$dataset(_c,'index',_index);
            _e._$dataset(this.__indexLis[_index],'index',_index);
        }._$bind(this));
        this.__autotimer = setInterval(this.__doFade._$bind(this),this.__stop)
    };

    _pro.__clearFade = function(){
        // 清理所有fade事件
        _u._$forEach(this.__contents,function(_c){
            _t0._$fadeStop(_c);
        }._$bind(this))
    };

    _pro.__doFadeIn = function(_index){
        var _in = this.__contents[_index],
            _out = this.__contents[this.__index];
        if(!_in || !_out){
            return;
        }
        _e._$setStyle(_in,'z-index',2);
        _e._$setStyle(_out,'z-index',1);
        _e._$delClassName(this.__indexLis[this.__index],'z-active');
        // 确保要动画的没有do-transform
        _e._$delClassName(_in,'do-transform');
        _e._$setStyle(_in,'opacity',0);
        _e._$delClassName(_in,'f-dn');
        _t0._$fadeIn(_in,{
             opacity:1,
             timing:'ease-out',
             delay:0,
             duration:0.5,
             onstop:function(){
                _e._$addClassName(_in,'do-transform');
                var _iword = _e._$getByClassName(_in,'j-word')[0];
                if (!!_iword){
                    _e._$addClassName(_iword,'do-tram');
                }
                _e._$setStyle(_out,'opacity',0);
                _e._$addClassName(_out,'f-dn');
                _e._$delClassName(_out,'do-transform');
                var _oword = _e._$getByClassName(_out,'j-word')[0];
                if (!!_oword){
                    _e._$delClassName(_oword,'do-tram');
                }
             }
        });
        this.__index = _index;
        _e._$addClassName(this.__indexLis[this.__index],'z-active');
    };

    _pro.__doFade = function(_index,_hand){
        if (this.__index == _index){
            return;
        }
        // 动画前的清理工作
        this.__clearFade();
        if (!_hand){
            // 自动切换的情况
            _index = parseInt(this.__index) + 1;
            _index = this.__checkIndex(_index);
        }
        this.__doFadeIn(_index);

    };

    _pro.__onMouseOver = function(_event,_flag){
        if (!!this.__autotimer){
            this.__autotimer = clearInterval(this.__autotimer);
        }
        if (!!this.__timer){
            this.__timer = clearTimeout(this.__timer);
        }
        var _elm = _v._$getElement(_event),
            _index = _e._$dataset(_elm,'index');
        if (_flag){
            // 清理自动切换的timer
            this.__autotimer = clearInterval(this.__autotimer);
        }
        if (!_index){
            return;
        }
        this.__doFade(_index,true);
    };

    _pro.__onMouseOut = function(_event){
        // 准备开始新的自动切换
        this.__timer = setTimeout(function(){
            this.__autotimer = clearInterval(this.__autotimer);
            this.__autotimer = setInterval(this.__doFade._$bind(this),this.__stop);
        }._$bind(this),300);
    };

    return _p;
})