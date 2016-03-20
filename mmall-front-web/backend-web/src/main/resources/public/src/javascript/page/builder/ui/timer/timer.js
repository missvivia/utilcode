/*
 * ------------------------------------------
 * 计时器控件
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
    'util/dragger/dragger',
    '../setting/timer.js',
    '../../util.js',
    'text!./timer.html'
],function(_k,_v,_e,_u,_i,_t0,_t1,_x,_y,_html,_p,_o,_f,_r,_pro){
    /**
     * 计时器控件
     */
    _p._$$Timer = _k._$klass();
    _pro = _p._$$Timer._$extend(_i._$$Abstract);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__dopt = {
            overflow:!1
        };
        this.__wopt = {
            onok:this.__doUpdateStyle._$bind(this)
        };
        this.__sopt = {
            parent:document.body,
            onsubmit:this.__doUpdateStyle._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // init event
        this.__doInitDomEvent([[
            this.__nedit,'mousedown',
            this.__onEdit._$bind(this)
        ]]);
        this.__dragger = _t1.
            _$$Dragger._$allocate(this.__dopt);
        this.__doUpdateStyle(_options);
        this.__doUpdatePosition(_options);
    };
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        this.__doClearComponent();
        this.__super();
    };
    /**
     * 初始化外观
     * @return {Void} 
     */
    _pro.__initXGui = (function(){
        var _seed_html = _t0._$addNodeTemplate(_html);
        return function(){
            this.__seed_html = _seed_html;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - timer show
        // 1 - edit button
        // 2 - dragger bar
        var _list = _e._$getChildren(this.__body);
        this.__nshow = _list[0];
        this.__nedit = _list[1];
        this.__dopt.body = this.__body;
    };
    /**
     * 更新计时器样式
     * @return {Void}
     */
    _pro.__doUpdateStyle = function(_options){
        _u._$merge(
            this.__sopt,_u._$fetch({
                bgColor:'#ffffff',
                fontFamily:'微软雅黑',
                fontWeight:'normal',
                fontColor:'#000000',
                fontSize:20,
                opacity:80,
                borderColor:'#aaaaaa',
                borderStyle:'none',
                borderWidth:0
            },_options)
        );
        _e._$style(
            this.__nshow,{
                fontFamily:this.__sopt.fontFamily,
                fontWeight:this.__sopt.fontWeight,
                fontSize:this.__sopt.fontSize+'px',
                color:this.__sopt.fontColor,
                backgroundColor:this.__sopt.bgColor,
                opacity:this.__sopt.opacity/100
            }
        );
        _e._$style(
            this.__body,{
                borderColor:this.__sopt.borderColor,
                borderStyle:this.__sopt.borderStyle,
                borderWidth:this.__sopt.borderWidth+'px'
            }
        );
    };
    /**
     * 更新位置
     * @return {Void}
     */
    _pro.__doUpdatePosition = function(_options){
        if (_options.top!=null){
            _e._$setStyle(
                this.__body,'top',
                _options.top+'px'
            );
        }
        // align center default
        var _left = _options.left;
        if (_left==null){
            _left = (this.__parent.clientWidth-this.__nshow.offsetWidth)/2;
        }
        _e._$setStyle(this.__body,'left',_left+'px');
    };
    /**
     * 编辑计时器效果
     * @return {Void}
     */
    _pro.__onEdit = function(_event){
        _v._$stop(_event);
        _x._$$Timer._$allocate(this.__sopt);
    };
    /**
     * 添加到容器
     * @param {Object} _parent
     */
    _pro._$appendTo = function(_parent){
        this.__super(_parent);
        this.__dopt.view = this.__parent;
    };
    /**
     * 调整计时器位置
     * @return {Void}
     */
    _pro._$adjustPostion = function(_height){
    	var _top = _y._$getNumber(this.__body,'top'),
    	    _oht = this.__body.offsetHeight,
    	    _bottom = _top+_oht;
    	if (_bottom>_height){
    		_e._$setStyle(
    			this.__body,'top',
    			(_height-_oht)+'px'
    		);
    		return !0;
    	}
    };
    /**
     * 取配置信息
     * @return {Void}
     */
    _pro._$getSetting = function(){
        return {
            top:_y._$getNumber(this.__body,'top'),
            left:_y._$getNumber(this.__body,'left',420),
            fontFamily:_e._$getStyle(this.__nshow,'fontFamily'),
            fontWeight:_e._$getStyle(this.__nshow,'fontWeight'),
            fontSize:_y._$getNumber(this.__nshow,'fontSize',20),
            fontColor:_y._$getColor(this.__nshow,'color'),
            bgColor:_y._$getColor(this.__nshow,'backgroundColor'),
            opacity:_y._$getOpacity(this.__nshow),
            borderColor:_y._$getColor(this.__body,'borderColor'),
            borderStyle:_e._$getStyle(this.__body,'borderStyle'),
            borderWidth:_y._$getNumber(this.__body,'borderWidth')
        };
    };
    
    return _p;
});
