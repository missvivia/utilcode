/**
 *
 * slide功能模块
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
/* pro/widget/slide/slide */
NEJ.define([
    'base/klass',
    'base/element',
    'base/util',
    'base/event',
    'util/event',
    'util/animation/easeinout'
],function(_k,_e,_u,_v,_t,_t0,_p,_o,_f,_r) {
    var _pro;

    _p._$$Slide = _k._$klass();
    _pro = _p._$$Slide._$extend(_t._$$EventTarget);
    /**
     * 结构
     *
     *  _p._$$Slide._$allocate({
     *   slideBox:_e._$get('touchslide'),
     *   iconBox:_e._$get('ul'),
     *   selected:'z-active',
     *   width:document.body.scrollWidth,
     *   duration:500,
     *   stop:5000
     *  });
     *
     * 重置slide
     *                  slideBox    slide的节点，结构如上
     *                  width       一张图片的宽度
     *                              如果不传slide，
     *                              需要在slide节点添加data-width属性
     *                  selected    右下脚选中图片样式，默认为 j-idxcrt
     *                  duration    滚动动画持续时间
     *                  stop        一张图片滚动完后停留时间
     */
    _pro.__reset = function(_options) {
        var _doSetIndex = function(_list){
            for(var i=0,l= _list.length;i<l;i++){
                _e._$dataset(_list[i],'index',i);
            }
        };
        this.__supReset(_options);
        this.__slideBox = _options.slideBox;
        this.__iconBox = _options.iconBox;
        this.__selected = _options.selected||'j-idxcrt';
        this.__stop = _options.stop||4000;
        this.__duration = _options.duration||500;
        this.__width = -1 * _options.width||parseInt(_e._$dataset(this.__slideBox,'width'));
        this.__maxpx = Math.ceil(Math.abs(this.__width) / 4);
        this.__imgs = this.__slideBox;
        this.__manualSlide=_options.manualSlide||false;
        this.__ilist = this.__iconBox.children;
        if(this.__ilist.length==1){
            _e._$addClassName(this.__iconBox,'f-dn');
            return;
        }
        this.__initSlide();
        _doSetIndex(this.__ilist);
    };


    /**
     * 初使化slide
     */
    _pro.__initSlide = function(){
        this.__index = 0;
        this.__base = 0;
        this.__slideCount = this.__ilist.length-1;
        this.__addEvent();
        this.__beforeSetInt();
        if(!this.__manualSlide){
            this.__timer = setInterval(this.__doSlide._$bind(this,1),this.__stop);
        }
        _e._$addClassName(this.__ilist[this.__index],this.__selected);
    };

    _pro.__beforeSetInt = function(){
        if (!!this.__timer){
            this.__timer = clearInterval(this.__timer);
        }
    }
    /**
     * 添加鼠标事件
     */
    _pro.__addEvent = function(){
        this.__slideBox.addEventListener('touchstart',this.__onTouchstart._$bind(this),false);
         this.__slideBox.addEventListener('touchmove',this.__onTouchmove._$bind(this),false);
          this.__slideBox.addEventListener('touchend',this.__onTouchend._$bind(this),false);
         // [this.__slideBox,'touchstart',this.__onTouchstart._$bind(this)],
         // [this.__slideBox,'touchmove',this.__onTouchmove._$bind(this)],
         // [this.__slideBox,'touchend',this.__onTouchend._$bind(this)],
        this.__doInitDomEvent([
            [this.__slideBox,'transitionend',this.__onTransitionend._$bind(this)]
        ]);
    };

    _pro.__onTouchstart = function(_event){
        if (!!this.__lockAnim){
            return;
        }
        this.__timer = clearInterval(this.__timer);
        this.__x0 = _event.touches[0].pageX;
        this.__y0 = _event.touches[0].pageY;
        this.__move = 0;
        _e._$setStyle(this.__slideBox,'transitionDuration',0);
    };

    _pro.__onTouchmove = function(_event){
        if (!!this.__lockAnim){
            return;
        }
        var _touch = _event.touches[0],
            _x = _touch.pageX;
        this.__moveend = _x;
        if (!this.__movestart){
            var _vertical,_crosswise;
            _vertical = Math.abs(_touch.pageY - this.__y0);
            _crosswise= Math.abs(_x - this.__x0);
            this.__movestart = true;
            this.__notVertical = (_vertical <= _crosswise);
        }else{
            this.__move = _x - this.__x0;
        }
        this.__startTime = new Date();
        var _move = this.__move + this.__base,
            _value = 'translate3d('+ _move + 'px' +',0,0)';
        if (_move > 0 || _move < this.__width * this.__slideCount){
            this.__move = 0;
            return;
        }
        if (this.__notVertical){
            // 横
            _v._$stop(_event);
        }else{
            return;
        }
        _e._$setStyle(this.__slideBox,'transform',_value);
    };

    _pro.__onTouchend = function(_event){
        if (!!this.__lockAnim){
            return;
        }
        if (!this.__startTime){
            // 点击的情况
            var _target = _v._$getElement(_event),
                _link = _e._$dataset(_target,'link');
            if (!!_link){
                location.href = _link;
            }
        }else{
            this.__endpoint = _event.changedTouches[0].pageX;
            this.__movestart = false;
            var _move = Math.abs(this.__move),
                _endTime = new Date(),
                _time = _endTime.getTime() - this.__startTime.getTime(),
                _spead = ((this.__moveend - this.__endpoint) * 1000 / _time);
            if (_move > this.__maxpx || (Math.abs(_spead) > 100 && this.__move != 0) ){
                if (this.__move < 0){
                    this.__index++;
                }else{
                    this.__index--;
                }
            }
            this.__startTime = null;
            this.__doSlide(1,true,this.__index);
        }
        this.__beforeSetInt();
        if(!this.__manualSlide){
            this.__timer = setInterval(this.__doSlide._$bind(this,1),this.__stop);
        }
    };

    _pro.__onTransitionend = function(_event){
        _v._$stop(_event);
        _u._$forEach(this.__ilist,function(_item){
            _e._$delClassName(_item,this.__selected);
        }._$bind(this));
        this.__base = this.__width * this.__index;
        _e._$addClassName(this.__ilist[this.__index],this.__selected);
        this.__lockAnim = false;
    };

    /**
     * 开始slide
     */
    _pro.__doSlide = function(_count,_isManual,_index){
        // 第一或最后一张，或者竖的，不要锁
        if (!!_isManual){
            if (this.__move == 0 || !this.__notVertical){
                return;
            }
        }
        if (!!_isManual){
            this.__index = _index;
        }else{
            this.__index++;
        }
        if (this.__index == this.__ilist.length){
            this.__index = 0;
        }else if(this.__index < 0){
            this.__index = this.__ilist.length - 1;
        }
        this.__lockAnim = true;
        var _number = this.__index * this.__width + 'px',
            _value = 'translate3d('+_number+',0,0)';
        _e._$setStyle(this.__slideBox,'transitionDuration',this.__duration + 'ms');
        _e._$setStyle(this.__slideBox,'transform',_value);
    };
});