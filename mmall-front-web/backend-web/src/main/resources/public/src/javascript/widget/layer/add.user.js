/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ '{lib}base/util.js'
		 ,'{lib}base/event.js'
		 ,'{lib}base/element.js'
		 ,'{lib}util/event.js'
		 ,'{lib}util/template/jst.js'
		 ,'{lib}util/template/tpl.js'
		 ,'{pro}widget/layer/window.js'
		 ,'{lib}util/form/form.js'
		 ,'pro/components/notify/notify'
		 ,'{pro}extend/request.js'
		 ,'{pro}extend/util.js'],
		function(u, v, e, t, e2, e3, Window, _t, notify, Request,util, p, o, f, r) {
			var pro;

			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$AddUserWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$AddUserWin = NEJ.C();
			pro = p._$$AddUserWin._$extend(Window);
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
				this.__isAdd = 1;
				if(!!options.info.id){
					this.__isAdd = 0;
					this.__itemId = options.info.id;
					options.title = '修改账户';
				}else{
					this.__itemId = 0;
					options.title = '添加账户';
					this.__displayNamePrefix += ".";
				};
				this.__userInfo = options.info;
				this.__userInfo['group'] = __userGroupList__;
				this.__userInfo['namePrefix'] = util._$getFullUserName().replace("@st.xyl","");
				e3._$getNodeTemplate('ntp-adduser-box-window');
				e2._$render(this.__body, 'ntp-adduser-window', {items:this.__userInfo});
				this.__initForm();
				this.__addEvent();
				this.__super(options);
			};

			pro.__initForm = function(){
				var validep=/^[a-zA-Z0-9]{6,20}$/;
				this._form = _t._$$WebForm._$allocate({
		          form:'webForm',
		          message:{
						password100:"6-20位字母和数字",
						password101:"请输入新密码"
				  },			  
				  oncheck:function(_event){
					if (_event.target.name=='password'){
						if(_event.target.value=="******"&&e._$dataset(_event.target,'id')){
							this._$showMsgPass(_event.target,'');
						}
						else if(!validep.test(_event.target.value)){
							_event.value = 100;
						}
					 }
				  }
		        });				
			};
			pro.__addEvent = function(){
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.__initCheckBox(this.__userInfo.groupList);
				v._$addEvent(list[0], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[1], 'click', this.__onCCClick._$bind(this));
			}
			
			/**
			 * 动态构建控件节点模板
			 * @protected
			 * @method {__initNodeTemplate}
			 * @return {Void}
			 */
			// pro.__initNodeTemplate = function() {
			// 	this.__seed_html = 'ntp-adduser-window';
			// };
			/**
			 * 销毁控件
			 */
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				this.__super();
				
			};
			pro.__initCheckBox = function(list){
				if(!!list){
					var	checkList = e._$getByClassName(this.__body,'j-check');
					for(var j=0,jlen=list.length;j<jlen;j++){
						for(var i=0,ilen=checkList.length;i<ilen;i++){
							if(list[j].id == checkList[i].id){
								checkList[i].checked = true;
							}
						}
					}
				}
			}
			pro.__onCCClick = function(event) {
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				this.__dataSubmit();
			}
			pro.__dataSubmit = function(){
				if(this._form._$checkValidity()){
					var data = this.__packData();
					if(!data){
						notify.show({
							'type': 'error',
							'message': '请选择用户组！'
						});
					}else{
						if(data.id){
							if(data.password=="******"){
								data['passwordIsChange']= "n";
							}else{
								data['passwordIsChange']= 'y';
							}
						} else {
							if(data.password==null || data.password == ""){
								data['passwordIsChange']= "n";
							}else{
								data['passwordIsChange']= 'y';
							}
							
							data["displayName"] = util._$getFullUserName().replace("@st.xyl",".")+ data["displayName"] + "@st.xyl";
						}
//						console.log(data);
						Request('user/save',{
							data:data,
							method:'POST',
							type:'JSON',
							progress:true,
							onload:this.__cbSaveOK._$bind(this),
							onerror:function(e){
								if(e.code == 600){
									notify.show({'type':'error','message':e.message})
								}else{
									notify.show({
										'type':'error',
										'message':'保存失败'
									})
								}
							}
						});
					}
				}
			}
			pro.__cbSaveOK = function(json){
				var item = json.result;
				this._$dispatchEvent('onok',item,this.__isAdd);
				this._$hide();
			}
			pro.__packData = function(){
				var data = this._form._$data(),
						checkList = e._$getByClassName(this.__body,'j-check'),
						list = [];
				for(var i=0,len=checkList.length;i<len;i++){
					if(checkList[i].checked == true){
						var item = {};
						item['id'] = checkList[i].id;
						list.push(item);
					}
				}
				data['groupList'] = list;
				if(!!this.__itemId){
					data['id'] = this.__itemId;
				}
				//验证groupList不为0
				if(!data.groupList.length){
					return 0;
				};
				return data;
			}
			return p._$$AddUserWin;
		})