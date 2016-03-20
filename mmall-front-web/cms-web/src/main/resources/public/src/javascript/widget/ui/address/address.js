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
		 '{lib}util/template/tpl.js',  
		 'text!./address.html?v=1.0.0.1',
		 '{lib}util/form/form.js?v=1.0.0.0',
		 '{pro}widget/util/address.js?v=1.0.0.2'],
		function(k, ut, v, e, t, i, e1,  html,ut1,Address, p, o, f, r) {
			var pro, sup, seed_html = e1._$addNodeTemplate(html),
			_mobileReg = /1[3|5|7|8|][0-9]{9}/,
			_regionReg    =/[0-9]{3,4}/,
			_regionTel =/[0-9]{6,8}/,
			_findParent = function(_elm,_className){
				while(_elm&&!e._$hasClassName(_elm,_className)){
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
				if(options.type==1){
					this.form.consigneeName.value = options.address.consigneeName;
					this.form.address.value = options.address.address;
					this.form.zipcode.value = options.address.zipcode;
					this.form.consigneeMobile.value = options.address.consigneeMobile;
					
					//if(options.address.isDefault){
						//this.form.isdefault.checked = true;
					//}
					var list =options.address.consigneeTel.split('-');
					for(var i=0,l=list.length;i<l;i++){
						this.form.consigneeTel[i].value = list[i];
					}
					this.form.consigneeName.value = options.address.consigneeName;
					// cms地址中没有邮编字段
					// this.form.zipcode.value = options.address.zipcode;
					this.__data ={province:options.address.province,
								  provinceId:options.address.provinceId,
								  city:options.address.city,
								  cityId:options.address.cityId,
								  section:options.address.section,
								  sectionId:options.address.sectionId,
								  street:options.address.street,
								  streetId:options.address.streetId
							};
					this.__addr =  Address._$allocate({address:this.address,
							data:this.__data,onchange:this.__onChange._$bind(this),url : options.url
					})
				} else{
					this.__data ={};
					this.__addr =  Address._$allocate({address:this.address,onchange:this.__onChange._$bind(this),url : options.url})
				}
				this.__form = ut1._$$WebForm._$allocate({
					form:this.form,
					oncheck:function(_event){
						
					},
					attr:{
						contact:function(_target){
							if(_target.name=='consigneeMobile'||_target.name=='consigneeTel'){
								var _mobile = this.form.consigneeMobile.value.trim();
								var _list = this.form.consigneeTel,tels=[];
								if(_list[0].value.trim()!=''){
									tels.push(_list[0].value.trim())
								}
								if(_list[1].value.trim()!=''){
									tels.push(_list[1].value.trim())
								}
								var pass = (_regionReg.test(_list[0].value.trim())&&_regionTel.test(_list[1].value.trim()))?null:1;
								if(!pass){//电话已经通过，不做验证，
									e._$removeByEC(this.__mobileError);
									return pass;
								} else{//电话已经通不过，验证手机号，
									if(_mobileReg.test(_mobile)){
										e._$removeByEC(this.__mobileError);
										return null;
									} else{
										return 2;
									}
								}
							}
						}._$bind(this)
					},
					oninvalid:function(_event){
						var _target = _event.target;
						var _parent = _findParent(_target,'fitm');
						var _list = _parent.getElementsByTagName('label');
						e._$addClassName(_list[0],'err');
						if(_target.name=='consigneeMobile'){
							if(_event.code===2){
								if(!this.__mobileError){
									this.__mobileError = e._$create('div','err');
									this.__mobileError.innerText ='手机号格式错误';
								}
								var fiptNode =  _findParent(_target,'fipt');
								fiptNode.appendChild(this.__mobileError);
								
							} else{
								e._$removeByEC(this.__mobileError);
							}	
						}
						
					}._$bind(this),
					onvalid:function(_event){
						var _target = _event.target;
						var _parent = _findParent(_target,'fitm');
						var _list = _parent.getElementsByTagName('label');
						e._$delClassName(_list[0],'err');
					}
				})
			};
			
			pro.__onChange = function(_data,_isEnd){
				var _parent = _findParent(this.address,'fitm');
				var _list = _parent.getElementsByTagName('label');
				this.__isEnd = _isEnd;
				this.__data = _data;
				if(_isEnd){
					e._$delClassName(_list[0],'err');
				} else if(!_data.street){
					e._$addClassName(_list[0],'err');
				} else{
					e._$delClassName(_list[0],'err');
				}
			};
			/**
			 * 控件销毁
			 *
			 */
			pro.__destroy = function() {
				this.form.reset();
				this.__form._$recycle();
//				this.__addr._$recycle();
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
				var list = e._$getByClassName(this.__body,'j-flag');
				this.form = this.__body;
				this.address = list[0];
			};
			
			pro._$checkValidity = function(){
				var pass = true;
				pass = this.__form._$checkValidity();
				if(this.__isEnd){
					var _parent = _findParent(this.address,'fitm');
					var _list = _parent.getElementsByTagName('label');
					e._$delClassName(_list[0],'err');
				} else if(!this.__data.street){
					var _parent = _findParent(this.address,'fitm');
					var _list = _parent.getElementsByTagName('label');
					e._$addClassName(_list[0],'err');
					pass = false;
				} 
				return pass;
				//var _data = this.__form._$data();
			}
			pro._$data = function(){
				var _zhiXiaCities = ['北京市','上海市','天津市','重庆市'];
				if(this.__form._$checkValidity()){
					var _data = this.__form._$data();
					if(_data.consigneeTel.length){
						if(_data.consigneeTel[2].trim()==''){
							_data.consigneeTel.splice(2,1);
						}
						_data.consigneeTel = _data.consigneeTel.join('-'); 
					}
//					if(ut._$indexOf(_zhiXiaCities,this.__data.province)!=-1){
//						this.__data.cityId = this.__data.provinceId;
//						this.__data.city = this.__data.province;
//					}
					return ut._$merge(_data,this.__data);
				}
				
			}
			return p._$$Address;
		})