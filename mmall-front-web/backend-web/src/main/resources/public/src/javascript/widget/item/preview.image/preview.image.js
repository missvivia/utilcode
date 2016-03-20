/*
 * --------------------------------------------
 * item实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        , '{lib}base/element.js'
        , '{lib}util/event.js'
        , '{lib}ui/item/item.js'
        , 'text!./preview.image.html'
        , 'text!./preview.image.css'
        ,'{lib}util/template/tpl.js'
       ], 
        function(u, v, e, t,ui,_html,_css,e0,p, o, f, r) {
	var pro,
		_seed_css = e._$pushCSSText(_css),
		_seed_html= e0._$addNodeTemplate(_html);
	
	/**
	 * 所有分类卡片
	 *
	 * @class   {nm.l._$$PreviewImageItem}
	 * @extends {nej.ui._$$Item}
	 *
	 *
	 */
	p._$$PreviewImageItem = NEJ.C();
	pro = p._$$PreviewImageItem._$extend(ui._$$Item);
	/**
	 * 控件重置
	 * @protected
	 * @method {__reset}
	 * @param  {Object} options 可选配置参数
	 */
	pro.__reset = function(options) {
		this.__supReset(options);
	};
	/**
	 * 销毁控件
	 */
	pro.__destroy = function() {
		this.__supDestroy();
	};
	/**
	 * 初使化节点
	 */
	pro.__initNode = function() {
		this.__supInitNode();
		var list = e._$getByClassName(this.__body, 'j-flag');
		this.canvas = list[0];
		this.ctx = this.canvas.getContext('2d');
		this.name = list[1];
		this.delBtn = list[2];
		v._$addEvent(this.delBtn,'click',this.__onDeleteBtnClick._$bind(this));
	};
	pro.__onDeleteBtnClick = function(event){
		v._$stop(event);
		this._$dispatchEvent('ondelete',this.__data);
	}
	/**
	 * 删除事件响应
	 * @protected
	 * @method {__onDeleteClick}
	 * @param {Object}  event
	 * @return {Void}
	 */
	pro.__onDeleteClick = function(event) {
		this._$dispatchEvent('ondelete', this.__data);
	};
	/**
	 * 动态构建控件节点模板
	 * @protected
	 * @method {__initNodeTemplate}
	 * @return {Void}
	 */
	pro.__initNodeTemplate = function() {
		this.__seed_html = _seed_html;
		this.__seed_css = _seed_css;
		
	};
	/**
	 * 刷新数据
	 */
	pro.__doRefresh = function(data) {
		//set data to dom
		var reader = new FileReader();
	    reader.onload = function(fileEvent){
	        var img = new Image();
	        img.src = fileEvent.target.result;
	        img.onload = function(event){
	        	var width = event.target.width,height=event.target.height,rwidth,rheight;
	            if(width>height){
	            	rwidth = 148
	                rheight = parseInt(height*148/width)
	            } else{
	            	rheight = 93,
	            	rwidth = parseInt((width*93)/height);
	            }
	            this.canvas.width = rwidth;
	            this.canvas.height = rheight;
	            this.ctx.drawImage(event.target,0,0,width,height,0,0,rwidth,rheight);
	        }._$bind(this)
	 
	    }._$bind(this)
	    reader.readAsDataURL(data);
	    this.name.innerText = data.name;
	};
	return p._$$PreviewImageItem;
})
