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
		 ,'{pro}extend/request.js'
	 	 ,'{lib}util/form/form.js'
	 	 ,'pro/components/notify/notify'],
		function(u, v, e, t, e2, e3, Window, Tree, Request, _t, notify, p, o, f, r) {
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
				var list = options.data;
				if(!!options.group){
					this.name.value = options.group.name;
					options.title = '修改角色';
				}else{
					options.title = '添加角色';
					this.name.value = '';
				}
				// this.name.value = !!options.group ? options.group.name:'';
				this.group = options.group;
				this.tree = Tree._$allocate({parent:this.treeBox,tree:__accessList__});
				if(!!list){
					for(var i=0,len=list.length;i<len;i++){
						this.tree._$setTree(list[i].id);
					}
				}
				this.__super(options);
			};
			/**
			 * 动态构建控件节点模板
			 * @protected 
			 * @method {__initNodeTemplate}
			 * @return {Void}
			 */
			// pro.__initNodeTemplate = function() {
			// 	this.__seed_html = 'ntp-add-usergroup-window';
			// };
			
			pro.__initXGui = function() {
				// 在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
				// this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
				// 这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
				// this.__seed_html = 'wgt-ui-xxx';
				this.__seed_html = 'ntp-add-usergroup-window';
			};
			/**
			 * 销毁控件
			 */
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				this.__super();
				
				//e3._$getNodeTemplate('ntp-add-usergroup-window');
				//e2._$render(this.__body, 'jst-usergroup-tree', {items:tree});
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.form = this.__body;
				this.name = list[0];
				this.treeBox = list[1];

				v._$addEvent(list[2], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[3], 'click', this.__onResetClick._$bind(this));
				this.__initForm();
			};
			pro.__initForm = function(){
				this._form = _t._$$WebForm._$allocate({
          form:this.form,
          message:{
          }
        });
			};
			pro.__destroy = function(event) {
				this.tree&&this.tree._$recycle();
				this.__super();
			};
			pro.__onResetClick = function(event) {
				this.tree._$clearTree();
			};
			pro.__onOKClick = function(event) {
				this.__dataSubmit();
				// this.tree = this.tree._$recycle();
			}
			pro.__dataSubmit = function(){
				var data = {};
				if(!!this.group){
					data['id'] = this.group.id;
				}
				data['accessList'] = this.tree._$getData();
				data['name'] = this.name.value;
				if(this._form._$checkValidity()){
					if(!!data['accessList'].length){
						Request('/authority/userGroup/save',{
							data:data,
							method:'POST',
							type:'JSON',
							onload:this.__saveSuccess._$bind(this),
							onerror:function(e){
								if(e.code == 600){
									notify.show({'type':'error','message':e.message});
								}else{
									notify.show({
										'type':'error',
										'message':'保存失败！'
									})
								}
							}
						});
					}else{
						notify.show({
							'type':'error',
							'message':'请选择角色权限！'
						})
					}
				}
			}
			pro.__saveSuccess = function(json){
				var item = json.result;
				this._$dispatchEvent("onok", item);
				this._$hide();
			}

			return p._$$AddUserGroupWin;
		})