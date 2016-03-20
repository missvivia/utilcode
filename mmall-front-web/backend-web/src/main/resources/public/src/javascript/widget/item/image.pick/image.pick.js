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
	     , 'text!./image.pick.html'
	     , 'base/config'
	     , 'pro/extend/util'], 
        function(u, v, e, t,ui,e1,html,c,_, p, o, f, r) {
	var pro,
    _seed_html= e1._$addNodeTemplate(html);
	// ui css text
	/**
	 * 所有分类卡片
	 *
	 * @class   {nm.l._$$ImagePickItem}
	 * @extends {nej.ui._$$Item}
	 *
	 *
	 */
	var $$ImagePickItem = NEJ.C();
	pro = $$ImagePickItem._$extend(ui._$$Item);
	sup = $$ImagePickItem._$supro;
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
		this.__super();
	};
	/**
	 * 初使化UI
	 */
	pro.__initXGui = function() {
		// 在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
		// this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
		// 这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
		// this.__seed_html = 'wgt-ui-xxx';
        this.__seed_html = _seed_html;
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
		this.size = list[3];
		v._$addEvent(this.img.parentNode,'click',this.__onImageClick._$bind(this));
	};
	pro.__onImageClick = function(){
		if(e._$hasClassName(this.img.parentNode,'imgchecked')){
			e._$delClassName(this.img.parentNode,'imgchecked');
			this._$dispatchEvent('onchecked',false,this.__id);
		} else{
			e._$addClassName(this.img.parentNode,'imgchecked');
			this._$dispatchEvent('onchecked',true,this.__id);
		}
	};
	pro._$checked = function(checked){
		if(checked){
			e._$addClassName(this.img.parentNode,'imgchecked');
		} else{
			e._$delClassName(this.img.parentNode,'imgchecked');
		}
	};
	/**
	 * 刷新数据
	 */
	pro.__doRefresh = function(data) {
		//set data to dom
		this.img.src = _._$image(data.imgUrl,100,100);
		this.size.innerText = data.width +'X' + data.height;
	};
	return $$ImagePickItem;
})
