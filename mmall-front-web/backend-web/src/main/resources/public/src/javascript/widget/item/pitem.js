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
        ,'{lib}util/template/tpl.js'], 
        function(u, v, e, t,ui,e1, p, o, f, r) {
	var pro, sup;
	// ui css text
	var seed_css = e._$pushCSSText('\
			.#<uispace>{line-height:26px;font-size:12px;}\
			.#<uispace>-parent .itm{border-bottom:1px solid #ccc;}\
			.#<uispace> .col{float:left;margin-left:10px}\
	');
	/**
	 * 所有分类卡片
	 *
	 * @class   {nm.l._$$PItem}
	 * @extends {nej.ui._$$Item}
	 *
	 *
	 */
	p._$$PItem = NEJ.C();
	pro = p._$$PItem._$extend(ui._$$Item);
	sup = p._$$PItem._$supro;
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
	 * 初使化UI
	 */
	pro.__initXGui = function() {
		//在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
		//this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
		//这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
		//this.__seed_html = 'wgt-item-xxx';
		this.__seed_css = seed_css;
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
		this.img = list[0];
		this.desc = list[1];
		v._$addEvent(this.__body,'dragstart',this.__onDragstart._$bind(this));
		v._$addEvent(this.__body,'dragover',this.__onDragover._$bind(this));
	};
	/**
	 * 开始拖动
	 */
	pro.__onDragstart = function(event){
		event.dataTransfer.effectAllowed = 'move';
		event.dataTransfer.setData('text/html', JSON.stringify(this.__data));
	};
	/**
	 * 开始拖动
	 */
	pro.__onDragover = function(event){
		v._$stop(event);
	};
	/**
	 * 编辑事件响应
	 * @method {__onEditClick}
	 * @param {Object}  event
	 * @return {Void}
	 */
	pro.__onEditClick = function(event) {
		this._$dispatchEvent('onupdate', this.__data);
	};
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
		this.__seed_html = 'ntp-product-item';
	};
	/**
	 * 刷新数据
	 */
	pro.__doRefresh = function(data) {
		//set data to dom
		this.img.src = data.url;
		e._$dataset(this.img,'width',data.width);
		e._$dataset(this.img,'height',data.height);
		this.desc.innerText = data.name;
	};
	return p._$$PItem;
})
