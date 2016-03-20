/*
 * --------------------------------------------
 * xx控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define([ 'base/klass', 
         'base/util', 
         'base/event', 
         'base/element', 
         'util/event',
		 'ui/base', 
		 'util/template/tpl',  
		 'text!./address.html',
		 'util/form/form',
		 'pro/widget/util/address.cascade',
		 './component.address/address.js',
		 'pro/components/address/address',
		 'pro/extend/request'
		 ],
		function(k, ut, _v, e, t, i, e1,  html,ut1,Cascade,Address,AddressList,_request, p, o, f, r) {
			var pro, sup, seed_html = e1._$addNodeTemplate(html),
			_mobileReg = /1[3|5|7|8|][0-9]{9}/,
			_findParent = function(_elm,tagName){
				while(_elm&&_elm.tagName.toLowerCase()!=tagName.toLowerCase()){
					_elm = _elm.parentNode;
				}
				return _elm;
			};

			/**
			 * 全局状态控件
			 * @class   {nm.i._$$Address}
			 * @extends {nej.ui._$$Abstract}
			 */
			p._$$Address = k._$klass();
			pro = p._$$Address._$extend(i._$$Abstract);
			sup = p._$$Address._$supro;

			/**
			 * 重置控件
			 * @param  {[type]} options [description]
			 * 						id 当前选中的id
			 *
			 */
			pro.__reset = function(options) {
				options.parent = options.parent || document.body;
				this.__super(options);
				this.__addressList = new AddressList({data:{id:options.id}});
		        this.__addressList.$inject(this.__addressBox);
		        this.__addressList.$on('change',function(_item){
		        	this._$dispatchEvent('change',_item);
		        }._$bind(this));
		        this.__addressList.$on('editaddress',function(_item){
		        	e._$addClassName(this.__addressBox,'f-dn');
		        	if(this.__addrui){
		        		this.__addrui._$recycle();
		        	}
		        	this.__addrui = Address._$allocate({'onadd':this.__onAddAddress._$bind(this),parent:this.__addAddressBox,address:_item});
		        }._$bind(this))
			};
			
			pro.__onAddAddress = function(_item){
		    	e._$delClassName(this.__addressBox,'f-dn');
		    	//this._$dispatchEvent('change',_item);
		    	if(this.__addrui){
		    		this.__addrui = this.__addrui._$recycle();
		    	}
		    	this.__addressList.$emit('updatelist');
		    };
			/**
			 * 控件销毁
			 *
			 */
			pro.__destroy = function() {
				this.__super();
				this.__addressList.destroy();
				if(this.__addrui){
					this.__addrui._$recycle();
				}
			};

			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
				//在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
				//this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
				//这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
				this.__seed_html = seed_html;
			};
			/**
			 * 节点初使化
			 *
			 */
			pro.__initNode = function() {
				this.__super();
				var _list = e._$getByClassName(this.__body,'j-flag');
				this.__addressBox = _list[0];
				this.__addAddressBox = _list[1];
			};
			
			return p._$$Address;
		})