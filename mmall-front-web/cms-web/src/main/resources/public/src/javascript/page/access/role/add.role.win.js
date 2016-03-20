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
		 ,'{pro}widget/ui/tree/tree.js'
		 ,'{lib}util/form/form.js'
		 ,'{pro}extend/request.js'
		 ,'pro/components/notify/notify'],
		function(u, v, e, t, e2, e3, Window, Tree, _t, Request, notify, p, o, f, r) {
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
				var item = options.list;
				if(!!item){
					this.__itemId = item.id;
					this.__parentId = item.parent;
					this.__selectedList = item.accessList;
					this.__isAdd = 0;
					options.title = '修改角色';
				}else{
					options.title = '添加角色';
					item = {};
					this.__itemId = 0;
					this.__isAdd = 1;
				};
				this.__initRoleList(item);
				this.__super(options);
			};
			pro.__initRoleList = function(item){
				var data = {};
				if(!this.__isAdd){
					data['parentId'] = item.parent;
				}
				Request('/access/role/getUserRole',{
					data:data,
					onload: this.__renderWin._$bind(this,item),
					onerror: function(e){
						console.log(e);
					}
				})
			};
			pro.__renderWin = function(item,json){
				item['roleList'] = json.result.list;
				e3._$getNodeTemplate('ntp-addrole-box-window');
				e2._$render(this.__body, 'ntp-addrole-window',{item:item});
				this.__initForm();
				this.__addEvent();
				this.__initAccessTree(this.__parentId);
			};
			pro.__initAccessTree = function(parentId,event){
				var data = {};
				data['roleId'] = this.__select.value;
				if(!!parentId){
					data['roleId'] = this.__parentId;
				}
				Request('/access/role/getAccessList',{
					data:data,
					onload:this.__renderAccessTree._$bind(this),
					onerror:function(e){
						console.log(e);
					}
				})
			};
			pro.__renderAccessTree = function(json){
				if(!!this.__tree){
					this.__tree._$recycle();
				}
				this.__tree = Tree._$allocate({parent:this.__treeBox,tree:json.result});
				if(!this.__isAdd){
					for(var i=0,len=this.__selectedList.length;i<len;i++){
						this.__tree._$setTree(this.__selectedList[i].id);
					}
				}
			};
			pro.__addEvent = function(){
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.__select = list[0];
				this.__treeBox = list[1];
				v._$addEvent(list[0], 'change', this.__initAccessTree._$bind(this,0));
				v._$addEvent(list[2], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[3], 'click', this.__onResetClick._$bind(this));
			}
			/**
			 * 动态构建控件节点模板
			 * @protected 
			 * @method {__initNodeTemplate}
			 * @return {Void}
			 */
			// pro.__initNodeTemplate = function() {
			// 	this.__seed_html = 'ntp-add-usergroup-window';
			// };
			
			// pro.__initXGui = function() {
			// 	// 在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
			// 	// this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
			// 	// 这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
			// 	// this.__seed_html = 'wgt-ui-xxx';
			// 	this.__seed_html = 'ntp-add-role-window';
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
			pro.__initForm = function(){
				this.__form = _t._$$WebForm._$allocate({
					form:'webForm'
				});
			};
			pro.__destroy = function(event) {
				this.__tree&&this.__tree._$recycle();
				this.__super();
			};
			pro.__onResetClick = function(event) {
				this.__tree._$clearTree();
				
			};
			pro.__onOKClick = function(event) {
				if(this.__form._$checkValidity()){
					var data = this.__packData();
					if(!!data.accessList.length){
						Request('/access/role/save',{
							data:data,
							progress:{},
							method:'POST',
							type:'JSON',
							onload:this.__saveSuccess._$bind(this),
							onerror:function(e){
								if(e.code == 601){
									notify.show({
										'type':'error',
										'message':e.message
									});
								}
								if(e.code == 417){
									notify.show({
										'type':'error',
										'message':e.message
									});
								}
							}
						})
					}else{
						notify.show({
							'type':'error',
							'message':'请选择权限！'
						})
					}
				}
			};
			pro.__saveSuccess = function(json){
				if(json.code == 200){
					var data = json.result;
					this._$dispatchEvent("onok", data);
					this.__tree = this.__tree._$recycle();
					this._$hide();
				}
			}
			pro.__packData = function(){
				var data = this.__form._$data(),
						selectedSiteList = e._$getByClassName(this.__body,'j-site');
				data['accessList'] = this.__tree._$getData();
				if(!!this.__itemId) data['id'] = this.__itemId;
				return data;
			}

			return p._$$AddUserGroupWin;
		})