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
		 ,'../tree/tree.js'],
		function(u, v, e, t, e2,_p, Window, Request, _t, notify, $,Tree,p ) {
			var pro;
					
			p._$$AddAttrGroupWin = NEJ.C();
			pro = p._$$AddAttrGroupWin._$extend(Window);
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
					options.title = '选择分类';
				//this.__initTree();
				this.__initPage();
				$(this.__body)._$style({"width":"700px","height":"500px"});
				this.__super(options);
				this.__addEvent();
				//this.__initForm();
			};
			pro.__initPage=function(){
				var that=this;
				if(categrodList.length>0){
					that.__initTree(categrodList);
				}else{
					Request('/category/normal/list',{
			    		data : {isPage : -1},
						method:'GET',
						sync:false,
			            onload:function(json){
			            	if(json.code=="200"){
			            		//console.log(json.result.list);
			            		categrodList=json.result.list;
			            		that.__initTree(json.result.list);
			            	}else{
			            		notify.show({
									'type':'info',
									'message':json.message
								});
			            	}
			            },
			            onerror:function(data){
			            	notify.show({
								'type':'info',
								'message':data.message
							});
			            }
			        })
				}
			}
			pro.__initTree=function(data){
				var that=this;
		    	if(!!this.__Tree){
		            this.__Tree._$recycle();
		          }
		    	$(this.__body)._$attr("id","categoryId");
		        this.__Tree = Tree._$allocate({
			      	parent:document.body,
			      	setting:{
			      		tree:data,
				      	Template:"cateTree",
				      	wrap:this.__body,
				      	isopen:false,
						check:{
							enable:true,
							ischeck:false
						},
						callback:{
							onInitAfter:function(obj){
								//console.log(selectedList);
								//树初始化完成时的操作
								obj._$setSelectBox(selectedList);
							},
							onSubmit:function(obj){
								selectedList=obj._$getData();
								//console.log(selectedList);
								that.__renderWrap();
								that._$hide();
							}
						}
			      	}
			     });
		    }
			pro.__renderWrap=function(){
				$("#categrod-wrap")._$html("");
				for(var i=0;i<selectedList.length;i++){
					var $li=$("<li data-id="+selectedList[i].id+" data-name="+selectedList[i].name+">"+selectedList[i].name+"<span class='glyphicon glyphicon-remove-circle close' style='top:4px;'></li>");
					$("#categrod-wrap")._$insert($li,"append");
				}
				var closes=e._$getByClassName(e._$get("categrod-wrap"),'close');
				$(closes)._$off("click");
		    	$(closes)._$on("click",function(){
		    		var $parent=$(this)._$parent(),
		    			id=e._$dataset($parent[0],'id');
		    		
		    		for(var i=0;i<selectedList.length;i++){
		    			if(selectedList[i].id==id){
		    				selectedList.splice(i,1);
		    			}
		    		}
		    		e._$remove($parent[0],false);
		    	});
			}
			pro.__addEvent = function(){

			};
			pro.__initForm = function(){
				this.__Form = _t._$$WebForm._$allocate({
					form:'attrForm'
				});
			};
			pro.__initXGui = function() {
				//this.__seed_css  = _seed_css;
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
				this.__super();
			};
			
			return p._$$AddAttrGroupWin;
		})