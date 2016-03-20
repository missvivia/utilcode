
/**
 * 首页射击动画实现文件
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
/* pro/widget/util/shoot */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'util/event',
    'util/effect/api',
    'pro/widget/util/util',
    'util/placeholder/placeholder',
    'pro/extend/request',
    'pro/components/bubble/bubble'
],function(_k,_e,_v,_u,_t,_t0,_t1,_t2,request,_bubble,_p){
    var _pro,
        _sttime = 200;

    _p._$$Shoot = _k._$klass();
    _pro = _p._$$Shoot._$extend(_t._$$EventTarget);

    _pro.__init = function(_options){
        this.__state = 'out';
        this.__ul = _options.ulbox;
        this.__lis = _e._$getChildren(this.__ul);
        this.__active = _options.active||'z-active';
        this.__boxes = _e._$getByClassName(this.__ul,_options.boxkey||'m-infolayer');

        this.__fadeBox = _e._$get('fadebox');
        var _cnts = _e._$getByClassName(this.__fadeBox,'j-cnt');
        this.__img = _cnts[0];
        this.__go = _cnts[1];
        this.__phd = _cnts[2];
        this.__callme = _cnts[3];
        this.__error = _cnts[4];

        _t2._$placeholder(this.__phd);
        _v._$addEvent(this.__ul,'mouseover',this.__onBrandOver._$bind(this));
        _v._$addEvent(this.__ul,'mouseout',this.__onBrandOut._$bind(this));
        _v._$addEvent(this.__fadeBox,'mouseover',this.__onBoxOver._$bind(this));
        _v._$addEvent(this.__phd,'focus',this.__clearError._$bind(this));
        _v._$addEvent(this.__callme,'click',this.__onBDCallMe._$bind(this));
        _v._$addEvent(this.__fadeBox,'mouseout',this.__onBoxOut._$bind(this));
        this.__super();
    };

    _pro.__clearError = function(){
        if (!!this.__bubble){
            this.__bubble.destroy();
        }
    };

    _pro.__onBDCallMe = function(){
        var _phone = this.__phd.value.trim(),
            _type = _phone.indexOf('@') > -1 ? 1 : 0;
        if (_t1._$checkEmailAndPhone(_phone)){
            // 提醒我接口
            this.__doCallMe(this.__liTag,0,_type,_phone);
        }else{
            if (!!this.__bubble){
                this.__bubble.destroy();
            }
            this.__bubble = new _bubble({
                data:{
                    content:'输入正确的邮箱或手机号',
                    clazz:'m-notecb-red'
                }
            });
            this.__bubble.$inject(this.__error);
            this.__error.style.display = 'none';
            this.__error.style.display = 'block';
        }
    };

    _pro.__doCallMe = function(_id,_activeType,_type,_value,_timeStr){
        var _uri = '/user/active/tellme';
        request(_uri,{
            data:{'activeId':_id,'activeType':_activeType,'type':_type,'value':_value,timeStr:_timeStr||''},
            onload:function(_result){
                if (_result.code == 200){
                    if (!!this.__bubble){
                        this.__bubble.destroy();
                    }
                    this.__bubble = new _bubble({
                        data:{
                            content:'添加提醒成功',
                            clazz:'m-notecb'
                        }
                    })
                    this.__bubble.$inject(this.__error);
                    this.__error.style.display = 'none';
                    this.__error.style.display = 'block';
                }
            }._$bind(this),
            onerror:function(){

            }
        })
    };

    _pro.__findStop = function(_node){
        var _tagName = _node.tagName;
        if (_tagName == 'UL'){
            return null;
        }
        if (_tagName == 'LI'){
            return _node;
        }else{
            return this.__findStop(_node.parentNode);
        }
    };

    /**
     * 检查是否有box已经显示了
     * @return {[type]} [description]
     */
    _pro.__checkLiBoxState = function(){
        return !_e._$hasClassName(this.__fadeBox,'f-dn');

        var _flag = false;
        _u._$forEach(this.__boxes,function(_box){
            if (!_e._$hasClassName(_box,'f-dn')){
                _flag = true;
                return _flag;
            }
        }._$bind(this));
        return _flag;
    };

    /**
     * [__setContent description]
     * @return {[type]} [description]
     */
    _pro.__setContent = function(){
        this.__img.src = this.__liImg;
        this.__go.href= '/mainbrand/story?id=' + this.__liTag;
        this.__clearError();
        this.__phd.value = '';
    };


    /**
     * 快显示
     * @param  {[type]} _node [description]
     * @return {[type]}       [description]
     */
    _pro.__doQuickShow = function(){
        _e._$delClassName(this.__fadeBox,'f-dn');
        _e._$setStyle(this.__fadeBox,'opacity',1);
        this.__hasShowBox = this.__fadeBox;
        this.__nowTag = this.__liTag;
        this.__setContent();
    };

    /**
     * 快隐藏
     * @param  {[type]} _node [description]
     * @return {[type]}       [description]

    _pro.__doQuickHide = function(_node){
        _e._$addClassName(_node||this.__fadeBox,'f-dn');
        _e._$setStyle(_node||this.__fadeBox,'opacity',0);
    };*/

    /**
     * 慢显示，也就是fade动画
     * @return {[type]} [description]
     */
    _pro.__doSlowShow = function(){
        _e._$delClassName(this.__fadeBox,'f-dn');
        _t0._$fadeStop(this.__fadeBox);
        _e._$setStyle(this.__fadeBox,'opacity',0);
        this.__setContent();
        _t0._$fadeIn(this.__fadeBox,{
            delay:0,
            duration:0.2,
            opacity:1,
            onstop:function(_event){
                // console.log('fadein stop')
                this.__hasShowBox = this.__fadeBox;
                this.__nowTag = this.__liTag;
                // _fdo(_box);
            }._$bind(this),
            onplaystate:function(_event){
                // css3 fixed do _$stop
                if (_event.opacity >= 1){
                    _t0._$fadeStop(this.__fadeBox);
                }
                // console.log('fadein' + _event.opacity)
            }._$bind(this)
        })
    };

    /**
     * 慢隐藏,也就是fade动画
     * @return {[type]} [description]
     */
    _pro.__doSlowHide = function(){
        _t0._$fadeStop(this.__fadeBox);
        _t0._$fadeOut(this.__fadeBox,{
            delay:0,
            duration:0.2,
            opacity:0,
            onstop:function(_event){
                // console.log('fadeout stop')
                _e._$addClassName(this.__fadeBox,'f-dn');
                // _fdo(_box);
            }._$bind(this),
            onplaystate:function(_event){
                // css3 fixed do _$stop
                if (_event.opacity <= 0.1){
                    _t0._$fadeStop(this.__fadeBox);
                }
                // console.log('fadeout' + _event.opacity)
            }._$bind(this)
        })
    };

    /**
     * 鼠标移除ul事件
     * @param  {[type]} _event [description]
     * @return {[type]}        [description]
     */
    _pro.__onBrandOver = function(_event){
        var _target = _v._$getElement(_event),
        _li = this.__findStop(_target);
        if (!!this.__outst){
            this.__outst = clearTimeout(this.__outst);
        }
        if (!!this.__overst2){
            this.__overst2 = clearTimeout(this.__overst2);
        }
        if (!!this.__boxout){
            this.__boxout = clearTimeout(this.__boxout);
        }
         if (!!this.__boxover){
            this.__boxover = clearTimeout(this.__boxover);
        }

        _e._$addClassName(_li,this.__active);

        this.__overst = setTimeout(function(){
            // console.log(this.__state + 'to ---> over');
            this.__state = 'over';
            if (!_li && _target.nodeName == 'UL'){
                // 缺一个角的情况
                this.__onBrandOut();
                return;
            }
            // 检查是否有box显示，用来判断快显示和慢动画显示
            var _hasShow = this.__checkLiBoxState();
            // 50是为了可以用clear，避免调用了stop，造成闪动
            var _time = 50;
            if (_hasShow){
                _time = 50;
            }else{
                _time = 500;
            }
            // 0.05秒后开始动画
            this.__overst2 = setTimeout(function(){
                // 找到要显示的box

                // this.__fadeBox = _e._$getChildren(_li)[1];

                this.__liTag = _e._$dataset(_li,'tag');
                this.__liImg = _e._$dataset(_li,'img');
                var _isself2 = this.__nowTag == this.__liTag;

                var _offset = _e._$offset(_li);
                _e._$setStyle(this.__fadeBox,'position','absolute');
                _e._$setStyle(this.__fadeBox,'top',_offset.y+'px');
                _e._$setStyle(this.__fadeBox,'left',_offset.x-335+'px');
                // console.log('要显示的box ' + this.__fadeBox);
                // console.log('上一次显示的box ' + this.__hasShowBox);
                // var _isself = this.__hasShowBox == this.__fadeBox;
                // console.log('两个是否相同' + _isself)
                // console.log('两个是否相同' + _isself2)
                // console.log(_isself2 == _isself)
                // console.log('hasShow' + _hasShow)
                if (_isself2 && _hasShow){
                    this.__doQuickShow();
                    return;
                }

                // 先停止动画
                _t0._$fadeStop(this.__fadeBox);

                // 如果有显示的，用快显示,否则用慢显示
                if (_hasShow){
                    // 第一次还没显示的情况，强制干掉所有,暴力处理
                    this.__clearAllShow();
                    // this.__doQuickHide(this.__hasShowBox);
                    this.__doQuickShow();
                }else{
                    this.__doSlowShow();
                }
            }._$bind(this),_time);
        }._$bind(this),_sttime);
    };


    _pro.__onBoxOver = function(){
        if (!!this.__outst){
            this.__outst = clearTimeout(this.__outst);
        }
        if (!!this.__overst2){
            this.__overst2 = clearTimeout(this.__overst2);
        }
        if (!!this.__overst){
            this.__overst = clearTimeout(this.__overst);
        }
        if (!!this.__boxout){
            this.__boxout = clearTimeout(this.__boxout);
        }
        this.__boxover = setTimeout(function(){
            // box 的over

        }._$bind(this),_sttime);
    };

    _pro.__onBoxOut = function(){
        if (!!this.__outst){
            this.__outst = clearTimeout(this.__outst);
        }
        if (!!this.__overst2){
            this.__overst2 = clearTimeout(this.__overst2);
        }
        if (!!this.__overst){
            this.__overst = clearTimeout(this.__overst);
        }
        if (!!this.__boxover){
            this.__boxover = clearTimeout(this.__boxover);
        }

        this.__boxout = setTimeout(function(){
            // box 的out
            this.__onBrandOut();
        }._$bind(this),_sttime);
    };

    _pro.__onBrandOut = (function(){
        var _doHide = function(){
            if (!!this.__overst){
                this.__overst = clearTimeout(this.__overst);
            }
            if (!!this.__overst2){
                this.__overst2 = clearTimeout(this.__overst2);
            }
            if (!!this.__boxout){
                this.__boxout = clearTimeout(this.__boxout);
            }
             if (!!this.__boxover){
                this.__boxover = clearTimeout(this.__boxover);
            }

            this.__outst = setTimeout(function(){
                // console.log(this.__state + 'to ---> out');
                this.__state = 'out';
                // 先停止动画
                if (!this.__fadeBox){
                    return;
                }
                _t0._$fadeStop(this.__fadeBox);

                // out 的情况只有--慢隐藏需要做
                this.__doSlowHide();
                // 检查是否有box显示，用来判断快隐藏和慢动画隐藏
                // var _hasShow = this.__checkLiBoxState();
                // 如果有显示的，用快隐藏,否则用慢隐藏
                // if (_hasShow){
                //     this.__doQuickHide();
                // }else{
                //     this.__doSlowHide();
                // }
            }._$bind(this),_sttime);
        }
        return function(_event){
            this.__clearLiActive();
            _doHide.call(this);
        };
    })();


    /**
     * 清理所有box动画并且隐藏掉
     * @return {[type]} [description]
     */
    _pro.__clearAllShow = function(){
        _u._$forEach(this.__boxes,function(_box){
            _t0._$fadeStop(_box);
            _e._$addClassName(_box,'f-dn');
            _e._$setStyle(_box,'opacity',0);
        }._$bind(this));
    };

    /**
     * 清理所有LI的active样式
     * @return {[type]} [description]
     */
    _pro.__clearLiActive = function(){
        _u._$forEach(this.__lis,function(_li){
            _e._$delClassName(_li,this.__active);
        }._$bind(this));
    };
});