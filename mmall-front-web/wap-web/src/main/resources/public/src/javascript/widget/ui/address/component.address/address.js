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
		 'pro/extend/request',
		 'pro/extend/util',
		 'pro/components/notify/notify'
		 ],
		function(k, ut, _v, e, t, i, e1,  html,ut1,Cascade,_request,_,notify,p, o, f, r) {
			var pro, sup, seed_html = e1._$addNodeTemplate(html),
			_mobileReg = /1[3|5|7|8|][0-9]{9}/,
			_regionReg =/[0-9]{3,4}/,
			_regionTel =/[0-9]{7,8}/,
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
			 *
			 */
			pro.__reset = function(options) {
				options.parent = options.parent || document.body;
				this.__super(options);
				this.__default.checked = false;
				this.__form.id.value ='';
				_._$title('新建地址');
				if(options.address){
					this.__form.address.value = options.address.address;
					this.__form.consigneeName.value = options.address.consigneeName;
					this.__form.consigneeMobile.value = options.address.consigneeMobile;
					this.__form.id.value = options.address.id||'';
					if(options.address.consigneeTel){
						var list = options.address.consigneeTel.split('-');
						for(var i=0,l=list.length;i<l;i++){
							this.__form.consigneeTel[i].value = list[i];
						}
					}
					e._$dataset(this.__form.provinceId,'value',options.address.province)
					e._$dataset(this.__form.cityId,'value',options.address.city)
					e._$dataset(this.__form.sectionId,'value',options.address.section)
					e._$dataset(this.__form.streetId,'value',options.address.street||'我不知道')
					this.__default.checked =  options.address.isDefault||false;
					_._$title('修改地址');
				} 
				var _removeErrorClass = function(_target){
					var _parent = _findParent(_target,'LI');
					var _list = _parent.getElementsByTagName('label');
					e._$delClassName(_list[0],'err');
				}
				this.__webForm = ut1._$$WebForm._$allocate({
		        	form:this.__form,
		        	attr:{
						contact:function(_target){
							if(_target.name=='consigneeMobile'||_target.name=='consigneeTel'){
								var _mobile = this.__form.consigneeMobile.value.trim();
								var _list = this.__form.consigneeTel,tels=[];
								var isMobileEmpty=!_mobile;
								var isTelEmpty=!_list[0].value.trim()&&!_list[1].value.trim();
								if(!!isMobileEmpty && !!isTelEmpty){
									return -1;
								}
								else if(_target.name=='consigneeMobile' && !isMobileEmpty){
									if(!_mobileReg.test(_mobile)) notify.notify("手机号码填写错误！", "error");
									return (_mobileReg.test(_mobile))?null:-3;
								}else if(_target.name=='consigneeTel' && !isTelEmpty){
									return (_regionReg.test(_list[0].value.trim())&&_regionTel.test(_list[1].value.trim()))?null:-3;
								}
							}
						}._$bind(this)
					},
		        	oninvalid:function(_event){
						var _target = _event.target;
						var _parent = _findParent(_target,'LI');
						var _list = _parent.getElementsByTagName('label');
						e._$addClassName(_list[0],'err');
					}._$bind(this),
					onvalid:function(_event){
						_removeErrorClass(_event.target);
					}
		        });
				this.__cascade = Cascade._$allocate({
						        	province:this.__form.provinceId,
						        	city:this.__form.cityId,
						        	section:this.__form.sectionId,
						        	street:this.__form.streetId
						        })
			};
			
			
			/**
			 * 控件销毁
			 *
			 */
			pro.__destroy = function() {
				this.__form.reset();
				this.__webForm._$recycle();
				this.__cascade._$recycle();
				_._$title();
				this.__super();
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
				this.__form = _list[0];
				this.__default = _list[1];
				this.__submitBtn = _list[2];
				_v._$addEvent(this.__submitBtn,'click',this.__onSubmitClick._$bind(this));
				
				// 字数校验
				var _checkSize = e._$getByClassName(this.__body,'j-size');
				_v._$addEvent(_checkSize[0], 'keydown', function(){
					if(this.value.length >= 25) notify.notify("收件人字数超出！", "error");
				});
				_v._$addEvent(_checkSize[1], 'keydown', function(){
					if(this.value.length >= 100) notify.notify("详细地址字数超出！", "error");
				});
        _.fixfixed(this.__body);
			};
			
			pro.__onSubmitClick = function(){
		    	if(this.__webForm._$checkValidity()){
		    		var _data = this.__webForm._$data();
		    		_data.isDefault = this.__default.checked;
		    		if(_data.consigneeTel.length){
						if(_data.consigneeTel[2].trim()==''){
							_data.consigneeTel.splice(2,1);
						}
						if(_data.consigneeTel[1].trim()==''){
							_data.consigneeTel.splice(1,1);
						}
						_data.consigneeTel = _data.consigneeTel.join('-'); 
					}
		    		_request('/profile/address/update',{
		    			data:_data,
		    			method:'POST',
		    			type:'json',
		    			onload:function(_json){
		    				this._$dispatchEvent('onadd',_json.result);
		    			}._$bind(this),
		    			onerror:function(){
		    				
		    			}
		    		})
		    	}
		    };
		    
			pro._$checkValidity = function(){
				return this.__webForm._$checkValidity();
			}
			pro._$data = function(){
				if(this.__webForm._$checkValidity()){
					var _data = this.__webForm._$data();
					_data.province = this.__form.provinceId.options[this.__form.provinceId.selectedIndex].text;
		    		_data.city = this.__form.cityId.options[this.__form.cityId.selectedIndex].text;
		    		_data.section = this.__form.sectionId.options[this.__form.sectionId.selectedIndex].text;
		    		_data.street = this.__form.streetId.options[this.__form.streetId.selectedIndex].text;
					return _data;
				}
				
			}
			return p._$$Address;
		})