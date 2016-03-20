/*
 * ------------------------------------------
 * 热区选择器实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/event',
    'base/element',
    'base/constant',
    'ui/base',
    'util/template/tpl',
    'util/range/range',
    'util/resize/resize',
    './holder.js',
    'text!./hotspot.css',
    'text!./hotspot.html'
],function(_k,_u,_v,_e,_g,_i,_l,_rg,_rs,_rh,_css,_html,_p,_o,_f,_r,_pro){
    /**
     * 热区选择器
     * 
     * @class   _$$HotSpot
     * @extends _$$Abstract
     */
    _p._$$HotSpot = _k._$klass();
    _pro = _p._$$HotSpot._$extend(_i._$$Abstract);
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__init = function(){
        this.__holders = [];
        this.__hopt = {
            onremove:this.__onHolderRemove._$bind(this)
        };
        this.__ropt = {
            onchange:this.__onRangeChange._$bind(this),
            onafterchange:this.__onRangeStop._$bind(this),
            onbeforechange:this.__onRangeStart._$bind(this)
        };
        this.__sopt = {
        	min:{width:100,height:100},
            onresize:this.__onRangeChange._$bind(this),
            onmove:this.__onRangeChange._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__simg = _e._$get(_options.image);
        this.__doInitDomEvent([[
            this.__nact,'click',
            this.__onAction._$bind(this)
        ]]);
        this.__doPositionHotspot();
        // init holders
        _u._$forEach(
            _options.holders,function(_box){
                this.__doAppendHolder(_box);
            },this
        );
    };
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        this.__doClearClip();
        this.__doClearHolder();
        this.__doClearComponent();
        this.__super();
    };
    /**
     * 初始化外观
     * @return {Void}
     */
    _pro.__initXGui = (function(){
        var _seed_css = _e._$pushCSSText(
                _css,{blankimage:_g._$BLANK_IMAGE}
            ),
            _seed_html = _l._$addNodeTemplate(_html);
        return function(){
            this.__seed_css = _seed_css;
            this.__seed_html = _seed_html;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - clipper image
        // 1 - resizer body
        // 2 - action box
        var _list = _e._$getByClassName(this.__body,'j-flag');
        this.__nimg = _list[0];
        this.__nact = _list[2];
        // range config
        this.__ropt.sbody  = _list[1];
        this.__ropt.pbody  = _list[1].parentNode;
        this.__ropt.parent = this.__body;
        // resizer config
        this.__sopt.sbody = _list[1];
        this.__sopt.body  = _list[1].parentNode;
        this.__sopt.view  = this.__body;
        // clear clip info
        this.__doClearClip();
    };
    /**
     * 定位热区
     * @return {Void}
     */
    _pro.__doPositionHotspot = function(){
        var _offset = _e._$offset(
            this.__simg,this.__parent
        );
        _e._$style(this.__body,{
            top:_offset.y+'px',
            left:_offset.x+'px',
            width:this.__simg.offsetWidth+'px',
            height:this.__simg.offsetHeight+'px'
        });
        if (!!this.__range){
            this.__range._$recycle();
        }
        this.__range = _rg._$$Range._$allocate(this.__ropt);
    };
    /**
     * 清除裁剪信息
     * @return {Void}
     */
    _pro.__doClearClip = function(){
        _e._$setStyle(this.__simg,'opacity',1);
        this.__nimg.src = _g._$BLANK_IMAGE;
        _e._$removeByEC(this.__nimg);
        _e._$removeByEC(this.__ropt.pbody);
        if (!!this.__resizer){
            this.__resizer = 
                this.__resizer._$recycle();
        }
    };
    /**
     * 清理占位符
     * @return {Void}
     */
    _pro.__doClearHolder = function(){
        _u._$reverseEach(
            this.__holders,function(_inst,_index,_list){
                _inst._$recycle();
                _list.splice(_index,1);
            }
        );
    };
    /**
     * 开始选择范围
     * @return {Void}
     */
    _pro.__onRangeStart = function(_event){
        _v._$stop(_event);
        if (this.__nimg.parentNode==this.__body){
            _event.stopped = !0;
            this.__doClearClip();
            return;
        }
        // start clip
        this.__body.appendChild(this.__nimg);
        this.__body.appendChild(this.__ropt.pbody);
        _e._$setStyle(this.__simg,'opacity',0.2);
        this.__nimg.src = this.__simg.src;
    };
    /**
     * 范围变化触发事件
     * @param {Object} _event
     */
    _pro.__onRangeChange = function(_event){
        var _arr = [
            _event.top+'px',
            _event.left+_event.width+'px',
            _event.top+_event.height+'px',
            _event.left+'px'
        ];
        _e._$setStyle(
            this.__nimg,'clip',
            'rect('+_arr.join(' ')+')'
        );
    };
    /**
     * 范围变化结束触发事件
     * @param {Object} _event
     */
    _pro.__onRangeStop = function(_event){
        if (!this.__resizer){
            this.__resizer = _rs.
                _$$Resize._$allocate(this.__sopt);
        }
    };
    /**
     * 追加热区
     * @return {Void}
     */
    _pro.__doAppendHolder = function(_box){
        _box.parent = this.__parent;
        _u._$merge(_box,this.__hopt);
        this.__holders.push(
            _rh._$$HotHolder._$allocate(_box)
        );
    };
    /**
     * 删除热区
     * @return {Void}
     */
    _pro.__onHolderRemove = function(_event){
        var _index = _u._$indexOf(
            this.__holders,_event.inst
        );
        if (_index>=0){
            this.__holders.splice(_index,1);
        }
    };
    /**
     * 判断是否存在热区重叠
     * @return {Boolean} 是否有重叠
     */
    _pro.__hasRangeOver = function(_box){
        var _index = _u._$indexOf(
            this.__holders,function(_holder){
                var _hbx = _holder._$getSetting();
                // right1<left2 or left1>right2 or 
                // bottom1<top2 or top1>bottom2
                return !(_box.top>(_hbx.top+_hbx.height)||
                         _box.left>(_hbx.left+_hbx.width)||
                        (_box.top+_box.height)<_hbx.top||
                        (_box.left+_box.width)<_hbx.left);
            },this
        );
        return _index>=0;
    };
    /**
     * 范围操作按钮
     * @return {Void}
     */
    _pro.__onAction = function(_event){
        var _element = _v._$getElement(_event),
            _action = _e._$dataset(_element,'action');
        switch(_action){
            case 'cc':
                this.__doClearClip();
            break;
            case 'ok':
                var _box = this.__resizer._$getResizeBox(),
                    _min = this.__sopt.min;
                if (_box.width<_min.width||
                	_box.height<_min.height){
                	this._$dispatchEvent('onerror',{
                        message:'热区的最小尺寸为'+
                        _min.width+'*'+_min.height+'px'
                    });
                    return;
                }
                if (this.__hasRangeOver(_box)){
                    this._$dispatchEvent('onerror',{
                        message:'热区不能重叠'
                    });
                    return;
                }
                this.__doClearClip();
                this.__doAppendHolder(_box);
            break;
        }
    };
    /**
     * 更新热区容器
     * @return {Void}
     */
    _pro._$update = function(){
        this.__doPositionHotspot();
    };
    /**
     * 是否有热区超出
     * @return {Void}
     */
    _pro._$getOverflow = function(){
        var _ret = [],
            _max = this.__body.offsetHeight;
        _u._$forEach(
            this.__holders,function(_inst){
                var _set = _inst._$getSetting();
                if (_set.top+_set.height>_max){
                    _ret.push(_inst);
                }
            }
        );
        return _ret.length>0?_ret:null;
    };
    /**
     * 取热区信息
     * @return {Array} 热区信息
     */
    _pro._$getSetting = function(){
        var _arr = [];
        _u._$forEach(
            this.__holders,function(_inst){
                _arr.push(_inst._$getSetting());
            }
        );
        return _arr;
    };
    
    return _p;
});
