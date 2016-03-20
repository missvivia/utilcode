/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ '{lib}base/util.js?v=1.0.0.0'
		 ,'{lib}base/event.js?v=1.0.0.0'
		 ,'{lib}base/element.js?v=1.0.0.0'
		 ,'{lib}util/event.js?v=1.0.0.0'
		 ,'{pro}widget/layer/window.js?v=1.0.0.0'
		 ,'{pro}widget/ui/address/address.js?v=1.0.0.0'],
		function(u, v, e, t, Window, AddressUI, p, o, f, r) {
			var pro;

			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$CancelOrderWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$AddressWin = NEJ.C();
			pro = p._$$AddressWin._$extend(Window);
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
				options.title = '修改收货地址';
				this.__super(options);
				if(!!this.__addrui){
					this.__addrui._$recycle();
				}
				this.__addrui = AddressUI._$allocate({parent:this.__box,type:options.type,address:options.address});
			};
			
			/**
			 * 动态构建控件节点模板
			 * @protected
			 * @method {__initNodeTemplate}
			 * @return {Void}
			 */
			pro.__initNodeTemplate = function() {
				this.__seed_html = 'ntp-delivery-win';
			};
			/**
			 * 销毁控件
			 */
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				this.__super();
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.__box = list[0];
				v._$addEvent(list[1], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[2], 'click', this.__onCCClick._$bind(this));
			};
			pro.__onCCClick = function(event) {
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				var data = {};
				if(this.__addrui._$checkValidity()){
					data = this.__addrui._$data();
					this._$dispatchEvent('onok',data);
				}
			}
			return p._$$AddressWin;
		})