/*
 * ------------------------------------------
 * 活动列表显示控件实现文件 
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'base/util',
    'ui/base',
    'util/template/tpl',
    'util/template/jst',
    'text!./activities.html'
],function(_k,_v,_e,_u,_i,_l,_ll,_html,_p,_o,_f,_r,_pro){
    /**
     * 活动列表显示控件
     * 
     * @class   _$$ActShow
     * @extends _$$Abstract
     * 
     * @param  {Array}   activities - 活动标题列表
     * @param  {String}  align      - 对齐方式，left/right，默认为left
     * @param  {Boolean} marquee    - 是否滚动动画，默认不动画
     */
    _p._$$ActShow = _k._$klass();
    _pro = _p._$$ActShow._$extend(_i._$$Abstract);
    /**
     * 控件重置
     * @param {Object} _options
     */
    _pro.__reset = (function(){
        var _amap = {right:'m-act-right'};
        var _dump = function(_list,_all){
            if (!_all){
                var _item = _list[0]||'';
                return _item.desc||_item;
            }
            var _arr = [];
            _u._$forEach(
                _list,function(v){
                    _arr.push(v.desc||v);
                }
            );
            return _arr.join('，');
        };
        return function(_options){
            this.__super(_options);
            // init event
            this.__doInitDomEvent([[
                this.__body,'click',
                this.__onActivityAction._$bind(this)
            ],[
                document,'click',
                this.__doCloseActivity._$bind(this)
            ]]);
            // init act list
            var _mrq = !!_options.marquee;
            var _list = _options.activities||_r;
            this.__cbox.innerText = _dump(_list,_mrq);
            if (_list.length>1){
                _e._$addClassName(this.__body,'j-show');
            }
            // init act show
            _ll._$render(
                this.__sbox,
                this.__seed_list,{
                    xlist:_list
                }
            );
            this.__body.insertAdjacentElement(
                'afterEnd',this.__sbox
            );
            // init align class
            var _class = _amap[_options.align];
            if (!!_class){
                _e._$addClassName(this.__sbox,_class);
                _e._$addClassName(this.__body,_class);
            }
            // init act marquee
            if (!!_mrq){
                this.__doMarqueeAct();
            }
        };
    })();
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        this.__super();
        this.__sbox.innerHTML = '&nbsp;';
        this.__cbox.innerText = '';
        _e._$removeByEC(this.__sbox);
        _e._$delClassName(this.__sbox,'m-act-right');
        _e._$delClassName(this.__body,'j-show m-act-right');
        this.__timer = window.clearTimeout(this.__timer);
    };
    /**
     * 初始化外观
     * @return {Void} 
     */
    _pro.__initXGui = (function(){
        var _seed_html = _l._$parseUITemplate(_html);
        return function(){
            this.__seed_html = _seed_html.tmdl;
            this.__seed_list = _seed_html.tlst;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - text list
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__cbox = _list[0];
        this.__sbox = _e._$create('div','m-act-list');
    };
    /**
     * 滚动显示活动
     * @return {Void}
     */
    _pro.__doMarqueeAct = (function(){
        var _left = 0,
            _interv = 50;
        var _doMarquee = function(_delta){
            if (!!this.__timer){
                this.__timer = window.clearTimeout(this.__timer);
            }
            _left = (_left+1)%_delta;
            if (_left==0){
                this.__cbox.scrollLeft = _delta;
                this.__timer = window.setTimeout(
                    _doMarquee._$bind(this,_delta),800
                );
                return;
            }
            this.__cbox.scrollLeft = _left;
            this.__timer = window.setTimeout(
                _doMarquee._$bind(this,_delta),_interv
            );
        };
        return function(_node){
            var _node = this.__cbox,
                _delta = _node.scrollWidth-
                         _node.clientWidth;
            if (!_node||_delta<=0){
                return;
            }
            this.__timer = window.setTimeout(
                _doMarquee._$bind(this,_delta),_interv
            );
        };
    })();
    /**
     * 显示活动列表
     * @return {Void}
     */
    _pro.__doCloseActivity = function(){
        _e._$delClassName(this.__sbox,'j-show');
        _e._$delClassName(this.__body,'j-open');
    };
    /**
     * 打开活动列表
     * @param {Object} _event
     */
    _pro.__onActivityAction = function(_event){
        _v._$stop(_event);
        _e._$addClassName(this.__sbox,'j-show');
        _e._$addClassName(this.__body,'j-open');
    };
    
    return _p;
});