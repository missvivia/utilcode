/*
 * ------------------------------------------
 * 预告模块实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/element',
    'pro/widget/module'
], function(_k,_u,_e,_m,_p,_o,_f,_r,_pro) {
    /**
     * 预告模块
     *
     * @class   _$$Module
     * @extends _$$EventTarget
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param {Object} _options
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // not fix timer if width>540
        if (document.body.clientWidth>540){
            return;
        }
        // do fix timer
        this.__tpost = [];
        this.__tlist = _e._$getByClassName(
            'trailer-box','j-timer'
        );
        _u._$forEach(
            this.__tlist,function(_node){
                var _offset = _e._$offset(_node,'trailer-box').y-12;
                console.log(_offset);
                this.__tpost.push(_offset);
            },this
        );
        // init timer fix check
        this.__doInitDomEvent([[
            window,'scroll',
            this.__onTimeFixCheck._$bind(this)
        ]]);
        this.__onTimeFixCheck();
    };
    /**
     * 固定时间线
     * @return {Void}
     */
    _pro.__doFixTimer = function(_index,_delta){
        if (_index==this.__fxindex){
            return;
        }
        this.__doClearFixTimer();
        this.__fxindex = _index;
        var _node = this.__tlist[_index];
        this.__fxtimer = _node.cloneNode(!0);
        _e._$setStyle(_node,'visibility','hidden');
        _e._$setStyle(this.__fxtimer,'top',_delta+'px');
        _e._$addClassName(this.__fxtimer,'j-tfix');
        document.body.appendChild(this.__fxtimer);
    };
    /**
     * 清除固定时间线
     * @return {Void}
     */
    _pro.__doClearFixTimer = function(){
        if (!this.__fxtimer) return;
        _e._$remove(this.__fxtimer);
        _e._$setStyle(
            this.__tlist[this.__fxindex],
            'visibility','visible'
        );
        delete this.__fxindex;
        delete this.__fxtimer;
    };
    /**
     * 时间轴固定检查
     * @return {Void}
     */
    _pro.__onTimeFixCheck = function(){
        var _delta = 50,
            _header = _e._$get('paopao-header');
        if (!!_header){
            _delta = _header.offsetHeight;
        }
        // check last
        var _top = _e._$getPageBox().scrollTop-12,
            _last = this.__tpost[this.__fxindex];
        if (_last!=null&&_last>_top){
            this.__doClearFixTimer();
        }
        // check current
        _u._$reverseEach(
            this.__tpost,function(_offset,_index){
                if (_offset<_top){
                    this.__doFixTimer(_index,_delta);
                    return !0;
                }
            },this
        );
    };
    // init module
    _p._$$Module._$allocate();
});
