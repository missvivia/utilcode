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
		 ,'{pro}widget/layer/window.js'
		 , 'text!./image.pick.css'
	     , 'text!./image.pick.html'
	     , 'base/config'
	     , '{pro}widget/ui/color/color.js'
	     , '{lib}util/template/tpl.js'
	     ,'{pro}/extend/util.js'
	     ,'{lib}util/list/module.pager.js'
	     ,'{pro}widget/item/image.pick/image.pick.js'
	     ,'{pro}widget/cache/image.cache.list.js'
	     ,'{pro}extend/request.js'
		 ],
		function(u, v, e, t, Window,css,html,c,Color, e1,ue,t1,pickItem,ImageCacheList,request,p, o, f, r) {
			var pro,
			_seed_css = e._$pushCSSText(css,{root:c._$get('root')}),
	        _seed_html= e1._$addNodeTemplate(html);


			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$ImagePickWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$ImagePickWin = NEJ.C();
			pro = p._$$ImagePickWin._$extend(Window);
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
				options.title = '选择图片';
				this.__super(options);
				this.max = options.max;
				this.__getCategory()
				
			};
			pro.__getCategory = function(){
				request('/rest/image/category',{onload:this.__cbGetCategory._$bind(this),onerror:this.__cbGetCategory._$bind(this)})
			};
			
			pro.__cbGetCategory = function(result){
				if(result.code==200){
					this.select.options.length=0;
					var list = result.result.list;
					for(var i=0,l=list.length;i<l;i++){
						var option = new Option(list[i].name,list[i].id);
						this.select.options.add(option);
					}
				}
				this.__onSelectChange();
			};
			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
				// 在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
				// this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
				// 这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
				// this.__seed_html = 'wgt-ui-xxx';
				this.__seed_css  = _seed_css;
		        this.__seed_html = _seed_html;
			};
			/**
			 * 销毁控件
			 */
			pro.__destroy = function() {
				this.__super();
			};
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				console.log(1)
				this.__super();
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.select = list[0];
				this.srchIpt = list[1];
				this.srchBtn = list[2];
				
				this.box = list[3];
				this.pager = list[4];
				v._$addEvent(this.srchBtn, 'click', this.__onSrchBtnClick._$bind(this));
				v._$addEvent(this.select, 'change', this.__onSelectChange._$bind(this));
				v._$addEvent(list[5], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[6], 'click', this.__onCCClick._$bind(this));
                this.cache = ImageCacheList._$allocate({});
			};
			/**
			 * 图片分类改变
			 */
			pro.__onSelectChange = function(){
				var id = this.select.options[this.select.selectedIndex].value;
				this.__initModule({categoryId:id});
			};
			/**
			 * 搜索响应
			 */
			pro.__onSrchBtnClick = function(){
				var key = this.srchIpt.value.trim();
				var id = this.select.options[this.select.selectedIndex].value;
				this.__initModule({categoryId:id,name:key});
			};

			/**
			 * 
			 */
			pro.__initModule = function(data){
				//请添加相关的cache脚本文件和item文件,
				//添加{lib}util/list/module.pager.js
				//添加{pro}widget/item/item.js
				this.__checked = [];
				if(this.mdl){
	        		this.mdl = this.mdl._$recycle();
	        	}
                this.cache._$clearListInCache('user-list');
				this.mdl = t1._$$ListModulePG._$allocate({
					limit : 18,
					parent : this.box, //列表容器节点
					item : {
						klass : pickItem,
						prefix : 'g-item',
						onchecked:this.__onItemChecked._$bind(this)
					}, // klass 可以是item对象，也可以是jst，如果是jst传入模板id就可，如果是item对象需要实现item对象
					cache : {
						key : 'id', // 此key必须是唯一的，对应了item中的值,也是删除选项的data-id
						lkey : 'user-list', // 此key必须是唯一的，对应了item中的值,也是删除选项的data-id
						data : data, //  列表加载时携带数据信息，此数据也可在cache层补全
						klass : ImageCacheList
					},
					ondelete : function(data) {
						alert('删除成功');
						this.mdl._$delete(data);
					}._$bind(this),
					onupdate : function(data) {
						alert('更新成功');
						this.mdl._$update(data);
					}._$bind(this),
					pager : {
						parent : this.pager,
						clazz:'pager'
					}
				});
			};
			
			pro.__onItemChecked = function(checked,id){
				var item = e1._$getItemById(id);
				var index = u._$indexOf(this.__checked,item);
				if(checked){
					if(this.__checked.length+1>this.max){
						if(index!=-1){
							return;
						}
						var items = this.__checked.splice(0,1);
						items[0]._$checked(false);
					} 
					this.__checked.push(item);
				} else{
					if(index!=-1){
						var item = this.__checked.splice(index,1);
					}
				}
			};
			pro.__onCCClick = function(event) {
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				var data =[];
				for(var i=0,l=this.__checked.length;i<l;i++){
					data.push(this.__checked[i]._$getData());
				}
				this._$dispatchEvent('onok',data);
				this._$hide();
			}
			return p._$$ImagePickWin;
		})