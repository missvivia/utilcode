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
		 ,'{pro}extend/request.js'
		 ,'{lib}util/form/form.js'
		 ,'{pro}components/notify/notify.js'
		 ,'{lib}util/chain/NodeList.js'
		 ,'{pro}widget/district/siteSelector.js',
		 ],
		function(u, v, e, t, e2, e3, Window, Request, _t, notify,$,SiteSelector, p, o, f, r) {
			var pro;
			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$AddUserGroupWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$AddUserGroupWin = NEJ.C();
			pro = p._$$AddUserGroupWin._$extend(Window);
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
				var formData = options.item;
				if(!formData){
					options.title = '添加帐号';
					formData = {};
					this.__isAdd = 1;
					this.__itemId = 0;
					$("#account-areaList-box")._$style("display","none");
				}else{
					options.title = '修改帐号';
					this.__itemId = formData.id;
					this.__selectedRoleList = options.item.roleList||[];
					this.__isAdd = 0;
				}
				formData['adminList'] =  window.__AdminList__;
				e3._$getNodeTemplate('add-account-win-box');
				e2._$render(this.__body, 'add-account-win',{item:formData});
				this.__initForm();
				this.__initRoleList();
				this.__addEvent();
				this.__super(options);
				//编辑状态获取当前站点下的区域
				if(!this.__isAdd){
					this.__onSiteSelect();
				}
			};
			pro.__initRoleList = function(){
				this.__form = e._$get('webForm');
				this.__adminId = parseInt(this.__form.adminId.value);
				this.__packRoleData();
			};

			pro.__packRoleData = function(){
				var data = {};
				this.__dataList = [];
				this.__siteList =  window.__SiteList__;
				this.__roleList = window.__RoleList__;
				
				data['siteList'] = this.__siteList;
				data['roleList'] = this.__roleList;
				this.__dataModule = data;
				this.__dataList.push(data);
				var list = this.__dataList;
				if(!this.__isAdd){
//					list = this.__setRoleList();
					if(this.__selectedRoleList.length > 0){
						if(this.__selectedRoleList[0].siteList){
							list[0]["currentSiteId"] = this.__selectedRoleList[0].siteList[0].siteId;
						}
					}
				}
				this.__renderRoleList(list);
				
			};
			pro.__setRoleList = function(){
				return this.__dataChange(this.__selectedRoleList);
			};
			//刷新角色列表
			pro.__renderRoleList = function(list){
//				console.log(list);
				e2._$render('account-roleList-box', 'account-roleList',{items:list});
				e2._$render('account-siteList-box', 'account-siteList',{items:list});
			}
			pro.__initForm = function(){
				var validep=/^[a-zA-Z0-9]{6,20}$/;
				var nameReg = /^[a-zA-Z0-9_\-]{4,20}$/;
				this.__Form = _t._$$WebForm._$allocate({
					form:'webForm',
					message:{
						password100:"6-20位字母和数字",
						password101:"请输入新密码",
						displayName100 : "4-20位字母数字或下划线"
					},
					oncheck:function(_event){
						if (_event.target.name=='password'){
							if(_event.target.value=="******"&&e._$dataset(_event.target,'id')){
								this._$showMsgPass(_event.target,'');
							}
							else if(!validep.test(_event.target.value)){
								_event.value = 100;
							}
						 }else if(_event.target.name=='displayName' && !_event.target.disabled){
							 if(_event.target.value != "" && !nameReg.test(_event.target.value)){
								 _event.value = 100;
							 }
						 }
					}
				});
			};
			pro.__addEvent = function(){
				var list = e._$getByClassName(this.__body, 'j-flag');
				v._$addEvent(list[0], 'change', this.__onAdminSelect._$bind(this));
				v._$addEvent(list[1], 'change', this.__onSiteSelect._$bind(this));
//				v._$addEvent(list[2], 'click', this.__onRoleListClick._$bind(this));
				v._$addEvent(list[3], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[4], 'click', this.__onCClick._$bind(this));
			};
			pro.__onAdminSelect = function(){
				this.__initRoleList();
			};
			pro.__onSiteSelect = function(){
				this.__form = e._$get('webForm');
				this.__siteId = this.__form.site.value;
				var data = {"siteId":this.__siteId,"userId":this.__itemId};
				if(this.__siteId != ""){
					Request('/access/account/getAreaList',{
						data:data,
						onload:function(json){
							$("#account-areaList-box")._$style("display","inline-block");
							e2._$render('area', 'area-box',{items:json.result});
						}
					});
				}else{
					$("#account-areaList-box")._$style("display","none");
				}
			};
			pro.__onRoleListClick = function(event){
				var elm = v._$getElement(event),
						dataList = this.__saveRoleList(),
						index;
				if(e._$hasClassName(elm,'j-del')){
					v._$stop(event);
					dataList = this.__dataChange(dataList);
					index = parseInt(e._$dataset(elm,'index'));
					dataList.splice(index,1);
					this.__renderRoleList(dataList);
				}
				if(e._$hasClassName(elm,'j-chkall')){
					this.__checkAll(elm,dataList);
				}
			};
			pro.__checkAll = function(elm,dataList){
				var index = parseInt(e._$dataset(elm,'index')),
						list = e._$getByClassName(elm.parentNode.parentNode,'j-check');
				dataList = this.__dataChange(dataList);
				if(!!elm.checked){
					for(var i=0;i<list.length;i++){
						list[i].checked = true;
						list[i].disabled = true;
					}
				}else{
					for(var j=0;j<list.length;j++){
						list[j].checked = false;
						list[j].disabled = false;
					}
				}
			};
			pro.__onAddRoleClick = function(event){
				v._$stop(event);
				var dataList = this.__saveRoleList()
						data = {};
				dataList = this.__dataChange(dataList);
				dataList.push(this.__dataModule);
				this.__renderRoleList(dataList);
			};
			pro.__dataChange = function(list){
				var data = [];
				for(var i=0,len=list.length;i<len;i++){
					var newModule = {},
							selectedIds = list[i].siteList,
							siteList;
					if(selectedIds){
						siteList = this.__addCheck(selectedIds);
					}
					newModule['siteList'] = siteList;
					newModule['roleList'] = this.__roleList;
					newModule['id'] = list[i].id;
					data.push(newModule);
				}
				return data;
			};
			pro.__addCheck = function(selectedList){
				//数组深拷贝
				var sitelist = this.__siteList;
				var list = [];
				for(var k=0,len3=sitelist.length;k<len3;k++){
					var item = {};
					item['siteId'] = sitelist[k]['siteId'];
					item['siteName'] = sitelist[k]['siteName'];
					list.push(item);
				}
				for(var j=0,len2=selectedList.length;j<len2;j++){
					for(var i=0,len=list.length;i<len;i++){
						if(list[i].siteId == selectedList[j].id){
							list[i]['checked'] = true;
						}
					}
				}
				return list;
			};
			pro.__onCClick = function() {
				// this.tree._$clearTree();
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				if(this.__Form._$checkValidity()){
					var param = this.__packData();
					if(param.id){
						if(param.password=="******"){
							param['isModifyPassword']=0;
						}else{
							param['isModifyPassword']=1;
						}
					}
					
					if(param != 0){
						Request('account/save',{
							data:param,
							progress:{},
							method:'POST',
							type:'JSON',
							onload:this.__submitOK._$bind(this),
							onerror:function(e){
								if(e.code != 200){
									notify.show({
										'type':'error',
										'message':e.message
									});
								}
							}
						})
					}
				}
			};
			pro.__submitOK = function(json){
				if(json.code == 200){
					this._$dispatchEvent("onok");
				}
			}
			pro.__packData = function(){
				var data = this.__Form._$data();
				var param={};
				if(data.site == ""){
					notify.show({
						'type':'error',
						'message':'必须选择站点！'
					})
					return 0;
				}
				if(!data.area){
					notify.show({
						'type':'error',
						'message':'必须选择区域!'
					});
					return 0;
				}
				var areaList = [];
				var area = document.getElementsByName("area");
				for(var i = 0; i<area.length;i++){
					if(area[i].checked){
						areaList.push({"areaId":area[i].value,"areaName":$(area[i])._$attr("text")});
					}
				}
				var siteList = [];
				siteList.push({"siteId":data.site,"areaList":areaList});
				param['roleList'] = [];
				param['roleList'].push({"id" : data.role,"siteList":siteList});
				if(!!this.__itemId) param['id'] = this.__itemId;
				param["accountNum"] = data.accountNum;
				param["department"] = data.department;
				param["mobile"] = data.mobile;
				param["adminId"] = data.adminId;
				param["displayName"]= data.displayName;
				param["name"] = data.name;
				param["password"] = data.password;
				
//				console.log(param);
				return param;
			}
			
			/**
			 * 初使化节点
			 */
			pro.__initNode = function() {
				this.__super();
			};
			/**
			 * 销毁控件
			 */
			pro.__destroy = function(event) {
				// this.tree&&this.tree._$recycle();
				this.__super();
			};
			return p._$$AddUserGroupWin;
		})
