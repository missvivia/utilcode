/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * --------------------------------------------
 */
define(['base/klass',
        'base/element',
        'base/event',
    'pro/extend/util',
		'pro/widget/layer/window',
		'text!./address.css',
		'text!./address.html?20150906',
		'util/template/tpl',
		'util/form/form',
		'{pro}widget/ui/address/address.js?20150907'],
		function(_k,_e,_v,_, _BaseWindow,_css,_html,_e1,ut,AddressUI,_p) {
			var pro, sup;
			// ui css text
			var _seed_css = _e._$pushCSSText(_css),
				_seed_html= _e1._$addNodeTemplate(_html),
				_findParent = function(_elm,_className){
					while(_elm&&!_e._$hasClassName(_elm,_className)){
						_elm = _elm.parentNode;
					}
					return _elm;
				};
			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$AddressWindow}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			_p._$$AddressWindow = _k._$klass();
			pro = _p._$$AddressWindow._$extend(_BaseWindow);
			sup = _p._$$AddressWindow._$supro;
			/**
			 * 控件重置
			 * @protected
			 * @method {__reset}
			 * @param  {Object} options 可选配置参数
			 *                           clazz
			 *                           draggable
			 *                           mask
			 */
			pro.__reset = function(options) {
				options.clazz +=' w-win w-win-1 w-win-1-2';
				if(options.type==1){
					options.title = "修改收货地址";
					this.__addr1.checked = options.address.isDefault;
				} else{
					options.title = "增加收货地址";
				}
				this.__supReset(options);
//				if(!options.address){
//					options.type=1;
//					options.address={address: "江汉路599",
//						consigneeMobile: "13588231066",
//						consigneeName: "俞棋军",
//						isDefault: true,
//						province: "北京市",
//						provinceId: 11,
//						section: "崇文区",
//						sectionId: 110103,
//						street: "天坛街道",
//						streetId: 110103006,
//						consigneeTel: ""}
//				}
				this.__addrui = AddressUI._$allocate({parent:this.__box,type:options.type,address:options.address})
				
			};
			
			pro.__onChange = function(_data){
				if(!_data.street){
					var _parent = _findParent(this.address,'fitm');
					var _list = _parent.getElementsByTagName('label');
					_e._$addClassName(_list[0],'err');
				} else{
					this.__data = _data;
					var _parent = _findParent(this.address,'fitm');
					var _list = _parent.getElementsByTagName('label');
					_e._$delClassName(_list[0],'err');
				}
			};
			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
				this.__seed_html = _seed_html;
				this.__seed_css = _seed_css;
			};
			
			/**
			 * 初使化UI
			 */
			pro.__destory = function() {
				this.__addrui._$recycle();
				this.__super();
			};
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				this.__super();
				var list = _e._$getByClassName(this.__body, 'j-flag');
				this.__box = list[0];
				this.__addr1 = list[1];
				_v._$addEvent(list[2], 'click', this.__onOKClick._$bind(this));
				_v._$addEvent(list[1], 'click', this.__onCCClick._$bind(this));
			};
			
			pro.__onCCClick = function(event) {
				_v._$stop(event);
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				_v._$stop(event);
				if(this.__addrui._$checkValidity()){
					var data = _.extend(this.__addrui._$data(), {'isDefault': this.__addr1.checked});
					this._$dispatchEvent('onok',data);
					this._$hide();
				}
			}
			return _p._$$AddressWindow;
		})