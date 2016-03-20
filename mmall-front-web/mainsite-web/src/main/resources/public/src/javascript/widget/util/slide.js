/**
 *
 * slide功能模块
 * @author yuqijun(yuqijun@corp.netease.com)
 *
 */
/* pro/widget/util/slide */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'util/event',
    'util/animation/easeinout'
],function(_k,_e,_v,_t,_t0,_p,_o,_f,_r) {
    var _pro;

    _p._$$Slide = _k._$klass();
    _pro = _p._$$Slide._$extend(_t._$$EventTarget);
    /**
     * 结构
     *      <div class="m-slider" id="nem-slider" data-width="880">
     *          <ul class="m-slidebox"
     *           style="width:${880*bannerList?size}px">
     *            <#list bannerList as item>
     *              <li class="item f-fl"><a href="${item.url}"
     *               target="_blank"><img src="${item.img}"
     *                title="${item.title}"/></a></li>
     *            </#list>
     *          </ul>
     *          <ul class="m-icnidx">
     *            <#list bannerList as item>
     *              <li class="f-fl f-sep5"
     *              data-index="${item_index}">${item_index+1}</li>
     *            </#list>
     *          </ul>
     *         </div>
     *
     * 重置slide
     *                  slideBox    slide的节点，结构如上
     *                  width       一张图片的宽度
     *                              如果不传slide，
     *                              需要在slide节点添加data-width属性
     *                  selected    右下脚选中图片样式，默认为 j-idxcrt
     *                  stop            一张图片滚动完后停留时间
     */
    _pro.__reset = function(_options) {
        var _doSetIndex = function(_list){
            for(var i=0,l= _list.length;i<l;i++){
                _e._$dataset(_list[i],'index',i);
            }
        };
        this.__supReset(_options);
        this.__initLeft = _options.initleft;
        this.__slideBox = _options.slideBox;
        this.__iconBox = _options.iconBox;
        this.__prev = _options.prev;
        this.__next = _options.next;
        this.__selected = _options.selected||'j-idxcrt';
        this.__stop = _options.stop||4000;
        this.__duration = _options.duration||500;
        this.__width = _options.width||parseInt(_e._$dataset(this.__slideBox,'width'));
        this.__imgs = this.__slideBox;
        this.__ilist = this.__iconBox.children;
        if(this.__ilist.length==1){
            return;
        }
        this.__initSlide();
        _doSetIndex(this.__ilist);
    };


    /**
     * 初使化slide
     */
    _pro.__initSlide = function(){
        this.__slideBox.style.position ='relative';
        this.__slideBox.style.width = this.__width +'px';
        this.__slideBox.style.left = this.__initLeft +'px';
        this.__imgs.style.width = this.__width*this.__ilist.length +'px';
        this.__imgs.style.position ='absolute';
        this.__index = 0;
        this.__forward = 1;
        this.__slideCount = this.__ilist.length-1;
        this.__addEvent();
        this.__timer = setInterval(this.__doSlide._$bind(this,1),this.__stop);
        _e._$addClassName(this.__ilist[this.__index],this.__selected);
    };
    /**
     * 添加鼠标事件
     */
    _pro.__addEvent = function(){
        this.__doInitDomEvent([
            [this.__iconBox,'mouseover',this.__onMouseOver._$bind(this)],
            [this.__iconBox,'mouseout',this.__onMouseOut._$bind(this)],
            [this.__slideBox,'mouseover',this.__onMouseOver._$bind(this,true)],
            [this.__slideBox,'mouseout',this.__onMouseOut._$bind(this)],
            [this.__prev,'click',this.__onPrev._$bind(this)],
            [this.__next,'click',this.__onNext._$bind(this)]
        ]);
    };

    _pro.__onPrev = function(){
        var _to = this.__index-1;
        if (_to < 0){
            _to = this.__slideCount;
        }
        this.__doChange(_to);
    };

    _pro.__doChange = function(_index){
        this.__count = 1;
        this.__timer = clearInterval(this.__timer);
        this.__easeinout&&this.__easeinout._$recycle();
        this.__doSlide(this.__count,true,_index);
    }

    _pro.__onNext = function(){
        var _to = this.__index+1;
        if (_to > this.__slideCount){
            _to = 0;
        }
        this.__doChange(_to);
    };

    _pro.__onMouseOut = function(){
        this.__timer2 = setTimeout(function(){
            this.__timer = clearInterval(this.__timer);
            this.__timer = setInterval(this.__doSlide._$bind(this,1),this.__stop);
        }._$bind(this),300);
    };
    /**
     * 鼠标移入响应
     */
    _pro.__onMouseOver = function(_event,_flag){
        if (!!this.__timer2){
            this.__timer2 = clearTimeout(this.__timer2);
        }
        if (_flag){
            this.__timer = clearInterval(this.__timer);
            return;
        }
        var _elm = _v._$getElement(_event);
        var _index = _e._$dataset(_elm,'index');
        if(_index!=undefined){
            this.__count = parseInt(_index) - this.__index;
            if(this.__count==0){
                return;
            }
            this.__count = Math.abs(this.__count);
            this.__timer = clearInterval(this.__timer);
            this.__easeinout&&this.__easeinout._$recycle();
            this.__doSlide(this.__count,true,_index);
        }
    };
    /**
     * 开始slide
     */
    _pro.__doSlide = function(_count,_isManual,_index){
        _e._$delClassName(this.__ilist[this.__index],this.__selected);
        var _forward,
            _offsetfrom = this.__width*this.__index,
            _offsetto;
        if (!_isManual){
            if (this.__index>= this.__slideCount){
                this.__resetIndex = true;
                _offsetto = 0;
            }else{
                this.__resetIndex = false;
                _offsetto = this.__width*(this.__index+this.__forward*(_count||1));
            }
        }else{
            _offsetto = this.__width*_index;
        }
        var options = {
            from:{
               offset:_offsetfrom
            },
            to:{
               offset:_offsetto
            },
            duration:this.__duration,
            onupdate: function(_event){
                this.__imgs.style.left = -_event.offset + this.__initLeft + 'px';
            }._$bind(this),
            onstop: function(){
                this.__easeinout = _t0._$$AnimEaseInOut._$recycle(this.__easeinout);
                this.__count = 1;
                // if (_isManual){
                //     this.__timer = setInterval(this.__doSlide._$bind(this,1),this.__stop);
                // }
                _e._$addClassName(this.__ilist[this.__index],this.__selected);
           }._$bind(this)
        };
        if (!_isManual){
           if(!this.__resetIndex){
                this.__index = this.__index + this.__forward*(_count||1);
            } else {
                this.__index = 0;
            }
        }else{
            this.__index = parseInt(_index);
        }
        _e._$addClassName(this.__ilist[this.__index],this.__selected);
        // 创建减速动画实例
        this.__easeinout  = _t0._$$AnimEaseInOut._$allocate(options);
        // 开始动画
        this.__easeinout._$play();
    };
});