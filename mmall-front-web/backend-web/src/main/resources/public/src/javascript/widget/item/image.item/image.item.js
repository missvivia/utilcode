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
        ,'{lib}util/template/tpl.js'
        ,'text!./image.item.html'
        ,'pro/extend/util'
        ,'util/clipboard/clipboard'
        ,'pro/components/notify/notify',
        '{lib}util/chain/NodeList.js'
        ], 
        function(u, v, e, t,ui,e1,_html,_,_e1,notify,$,p, o, f, r) {
	var pro, sup,
	_seed_html= e1._$addNodeTemplate(_html);
	// ui css text
	/**
	 * 所有分类卡片
	 *
	 * @class   {nm.l._$$ImageItem}
	 * @extends {nej.ui._$$Item}
	 *
	 *
	 */
	var $$ImageItem = NEJ.C();
	pro = $$ImageItem._$extend(ui._$$Item);
	sup = $$ImageItem._$supro;
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
		e._$delClassName(this.img.parentNode,'imgchecked');
		this.__supDestroy();
	};
	/**
	 * 初使化节点
	 */
	pro.__initNode = function() {
		this.__supInitNode();
		var list = e._$getByClassName(this.__body, 'j-flag');
		this.img = list[0];
		this.hover = list[1];
		this.checked = list[2];
		this.name = list[3];
		this.copylink = list[4];
		this.copycode = list[5];
		
		//v._$addEvent(this.copylink,'click',this.__onCopylinkClick._$bind(this));
		var elm = _e1._$copy(this.copylink,this.__onCopylinkClick._$bind(this));
		e._$style(elm,{height:'100%',width:'100%'});
		elm = _e1._$copy(this.copycode,this.__onCopyCodeClick._$bind(this));
		e._$style(elm,{height:'100%',width:'100%'});
		v._$addEvent(this.img.parentNode,'click',this.__onImageClick._$bind(this));
	};
	pro.__onImageClick = function(){
		if(e._$hasClassName(this.img.parentNode,'imgchecked')){
			e._$delClassName(this.img.parentNode,'imgchecked');
			document.getElementById("select-all").checked = false;
		} else{
			e._$addClassName(this.img.parentNode,'imgchecked');
			var imgSelected = $('.imgchecked');
			var imgItem = $('.m-imgitem');
			document.getElementById("select-all").checked = (imgSelected.length == imgItem.length);
		}
	};
	pro._$checked = function(checked){
		if(checked){
			e._$addClassName(this.img.parentNode,'imgchecked');
		} else{
			e._$delClassName(this.img.parentNode,'imgchecked');
		}
	};
	pro._$isChecked = function(checked){
		return e._$hasClassName(this.img.parentNode,'imgchecked');
	};
	/**
	 * 开始拖动
	 */
	pro.__onCopylinkClick = function(event){
		notify.show('复制成功');
		return this.__data.imgUrl;
	};
	/**
	 * 开始拖动
	 */
	pro.__onCopyCodeClick = function(event){
		notify.show('复制成功');
		return '<img src="'+this.__data.imgUrl +'"/>';
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
		this.__seed_html = _seed_html;
	};
	/**
	 * 刷新数据
	 */
	pro.__doRefresh = function(data) {
		//set data to dom
		this.img.src = _._$image(data.imgUrl,150,150);
		this.name.innerText = data.imgName;
		this.name.title = data.imgName;
	};
	return $$ImageItem;
})
