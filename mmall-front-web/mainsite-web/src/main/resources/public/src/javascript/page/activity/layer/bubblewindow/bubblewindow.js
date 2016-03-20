/*
 * --------------------------------------------------
 * mmall活动页弹出框
 * @version  1.0
 * @author   hzzhangweidong(hzzhangweidong@corp.netease.com)
 * --------------------------------------------------
 */
NEJ.define([
    'base/klass',
    'util/event',
    'pro/extend/util',
    'util/chain/chainable',   
    'ui/base',                                              //  对应对象   _ui      (遮罩层ui)
    'base/element',                                         //  对应对象   _e       (节点工具)
    'util/template/tpl',                                    //  对应对象   _t       (页面模板组件)
    'text!./bubblewindow.css',                                  //  对应对象   _css     (注入内容css)
    'text!./bubblewindow.html',                                  //  对应对象   _html    (注入内容html)
    'pro/page/activity/components/activitybubble/activitybubble',
    'pro/page/activity/components/activitybubble/colorbubble/colorbubble'
], function (_k,_i,_,_$,_ui, _e, _t, _css, _html, ActitityBubble,ColorBubble,_p, _o, _f, _r, _pro) {

    var __css = _e._$pushCSSText(_css), 
    __html = _t._$addNodeTemplate(_html);
    /**
     * 页面模块基类
     *
     * @class   _$$BubbleWindow
     * @extends _$$Abstract
     */
    _p._$$BubbleWindow = _k._$klass();
    _pro = _p._$$BubbleWindow._$extend(_ui._$$Abstract);
    /**
     * 控件重置
     * @protected
     * @method {__reset}
     * @param  {Object} 可选配置参数
     * @return {Void}
     */
    _pro.__reset = function (_options) {
    	this.__paopao=_options.paopao;
    	_options.clazz="m-paopao-wrap";
    	this.__finalPosition=this.__initRandomCoordinate(_options);
    	var _parent=_$(this.__finalPosition.parent)[0];
    	_options.parent=_parent;
    	_e._$addClassName(_parent,'f-pr');
    	this.__super(_options);
        this.__createBubble(this.__paopao);
        this.__adjustPosition();
       
    };
    
    /**
     * 获得随机坐标
     * @protected
     * @method {__reset}
     * @param  {Object} 可选配置参数
     * @return {Void}
     */
    _pro.__initRandomCoordinate=function(_options){
    	this.__coordinates=this.__initCoordinates(_options);
    	return this.__coordinates[this.__paopao.position];
    };
    
    
    _pro.__initDefaultData=function(_options){
    	//1 用于首页的位置 2用于品购页
    	this.__position1=this.__position1||[{top:200,left:100,parent:".g-bd"},{top:400,left:650,parent:".g-bd"},{top:420,left:80,parent:".g-bd .g-mn"},{top:580,left:750,parent:".g-bd .g-mn"}];
    	this.__position2=this.__position2||[{top:100,left:100,parent:".g-schedule .g-bd"},{top:150,left:600,parent:".g-schedule .g-bd"},{left:0,top:50,parent:".g-schedule .pdlist"},{left:666,top:200,parent:".g-schedule .pdlist"}];
    };
    
    _pro.__initCoordinates=function(_options){
    	this.__initDefaultData(_options);
    	if(window.location.href.indexOf("schedule")!=-1){
    		return this.__position2;
    	}else
    		return this.__position1;
    		
//    	if(_options&&_options.type=="index"){
//    		return this.__position1;
//    	}
//    	else{
//    		return this.__positon2;
//    	}
//    	
//    	return this.__position3;
    };
    
    /*
     *创建mmall 
     */
    _pro.__createBubble=function(_paopao){
    	var that=this;
    	if(_paopao.type==0){
    		this.__bubble=new ActitityBubble({data:{paopao:_paopao}});
    	}
    	else{
    		this.__bubble=new ColorBubble({data:{paopao:_paopao}});
    	}
    
    	this.__bubble.$inject(this.__body);
    	this.__bubble.$on("knockbubble",function(_result){
    		var _pp=_e._$getByClassName(that.__body,"m-paopao")[0];
    		_e._$removeByEC(_pp);
    		that._$dispatchEvent("knockCallBack",_result);
    	});
    };
    
    /**
     * 初使化UI
     */
    _pro.__initXGui = function () {
        this.__seed_css = __css;
        this.__seed_html = __html;
    };
    
    /**
     * 窗口位置调整
     *
     * @protected
     * @method module:ui/layer/window._$$Window#__doPositionAlign
     * @return {Void}
     */
    _pro.__adjustPosition=function(){
    	_e._$setStyle(this.__body,'top',this.__finalPosition.top+"px");
    	_e._$setStyle(this.__body,'left',this.__finalPosition.left+"px");
    };

    return _p._$$BubbleWindow;
});