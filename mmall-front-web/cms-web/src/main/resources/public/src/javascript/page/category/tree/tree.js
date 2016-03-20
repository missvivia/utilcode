/*
 * --------------------------------------------
 * xx控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ '{lib}base/util.js'
		  , '{lib}base/event.js'
		  , '{lib}base/element.js'
		  ,	'{lib}util/event.js'
		  , '{lib}ui/base.js'
		  ,'{lib}util/chain/NodeList.js'
		  , '{lib}util/template/tpl.js'
		  , 'text!./tree.css'
	    , 'text!./tree.html'
	    , '{lib}util/template/jst.js'
		  ],
		function(u, v, e, t, i,$,e1,css,html,e2,p, o, f, r) {
			var pro,
				_seed_css = e._$pushCSSText(css);
		    var _seed_ui = e1._$parseUITemplate(html);
		    var  caches={};
		    var  extend = function(o1, o2 ,override){
			        for( var i in o2 ) if( o1[i] == undefined || override){
			            o1[i] = o2[i]
			           }
		            return o1;
		         },
			     tools = {
		    		apply: function(obj,fun, param, defaultValue) {
		    			if ((typeof fun) == "function") {
		    				return fun.apply(obj, param?param:[]);
		    			}
		    			return defaultValue;
		    		},
		    		eqs: function(str1, str2) {
		    			return str1.toLowerCase() === str2.toLowerCase();
		    		},
		    		getNodeMainDom:function(target) {
		    			return target.parentNode;
		    		},
		    		getNodeMainDom2:function(target) {
		    			return target.parentNode.parentNode;
		    		},
		    		isArray: function(arr) {
		    			return Object.prototype.toString.apply(arr) === "[object Array]";
		    		}
			     },
			     data ={
	        		getNodeCache: function(setting, tId) {
	        			if (!tId) return null;
	        			var n = caches[setting.treeId].nodes[data.getNodeCacheId(tId)];
	        			return n ? n : null;
	        		},
	        		getNodeCacheId: function(tId) {
	        			return tId.substring(tId.lastIndexOf("_")+1);
	        		},
	        		getCache: function(setting) {
	        			return caches[setting.treeId];
	        		},
	        		setCache: function(setting, cache) {
	        			caches[setting.treeId] = cache;
	        		}
		         },
		         view ={
	        		 switchNode: function(node) {
	        			var isopen=e._$dataset(node,"open")=="true" ? true:false;
    					view.expandCollapseNode(node,isopen);
        			 },
        			 switchALL: function(node,expandFlag){
        				 view.expandCollapseNode(node,expandFlag);
        			 },
        			 expandCollapseNode: function(node,expandFlag){
        				 var isParent=e._$dataset(node,"parent")=="true" ? true : false;
        				 if(isParent){
        					 var $node_parent=$(node)._$children("ul");
        					 if($node_parent){
        						 if(expandFlag){
        							 $node_parent._$style("display","none");
        							 e._$dataset(node,"open","false");
        							 var $span=$(node)._$children()._$children("span.glyphicon");
        							 e._$replaceClassName($span[0],"glyphicon-folder-open","glyphicon-folder-close");
        						 }else{
        							 $node_parent._$style("display","block"); 
        							 e._$dataset(node,"open","true");
        							 var $span=$(node)._$children()._$children("span.glyphicon");
        							 e._$replaceClassName($span[0],"glyphicon-folder-close","glyphicon-folder-open");
        						 }
        						 
        					 }
        				 }else{
    						 if(expandFlag){
    							 e._$dataset(node,"open","false");
    							 var $span=$(node)._$children()._$children("span.glyphicon");
    							 e._$replaceClassName($span[0],"glyphicon-folder-open","glyphicon-folder-close");
    						 }else{
    							 e._$dataset(node,"open","true");
    							 var $span=$(node)._$children()._$children("span.glyphicon");
    							 e._$replaceClassName($span[0],"glyphicon-folder-close","glyphicon-folder-open");
    						 }
    					 }
        			 }
		         };
			/**
			 * 全局状态控件
			 * 
			 * @class {nm.i._$$Tree}
			 * @extends {nej.ui._$$Abstract}
			 */
		    
			p._$$Tree = NEJ.C();
			pro = p._$$Tree._$extend(i._$$Abstract);
			sup = p._$$Tree._$supro;


			/**
			 * 重置控件
			 * 
			 * @param {[type]}
			 *            options [description]
			 * 
			 */
			pro.__default={
				event: {
					CLICK: "ztree_click",
					EXPAND: "ztree_expand",
					COLLAPSE: "ztree_collapse",
					ASYNC_SUCCESS: "ztree_async_success",
					ASYNC_ERROR: "ztree_async_error",
					REMOVE: "ztree_remove"
				},
				callback: {
					onInitAfter:null,
					onClick:null,
					onDblClick:null,
					onExpand:null,
					onAdd:null,
					onEdit:null,
					onDel:null,
					onDown:null,
					onUp:null
				},
				isopen:true,
				check:{
					enable:false,
					ischeck:false
				},
				associate:{
					parent:false
				}
			}
			
			pro.__reset = function(options) {
				options.parent = options.parent || document.body;
				this.__initTree(options);
				this.__super(options);
			};
			pro.__initTree = function(options){
				this.setting=extend(this.__default,options.setting,true);
				this.wrap=e._$get(this.setting.wrap);
				e2._$render(this.wrap,_seed_ui[this.setting.Template], {items:this.setting.tree,isopen:this.setting.isopen,check:this.setting.check});
				this.treeObj=e._$get(this.setting.wrap);
				this.setting.treeId=this.setting.wrap;
				//this.__initCache(this.setting);
				this.__unbindEvent();
				this.__bindEvent();
				//this.__initNode();
				
				if(!this.setting.check.enable){
					var $input=$(".chk");
					$input._$style("display","none");
				}
				
				//初始化回调函数
			   if(this.setting.callback.onInitAfter !=null){
				   tools.apply(this,this.setting.callback.onInitAfter, [this]);
			   }
			
			}
			
			/**
			 * 节点初使化
			 * 
			 */
//			pro.__initNode=function(){
//				var node=e.$one('.tree li');
//				alert(node.length);
//			}
			pro.__initCache = function(setting) {
				var c = data.getCache(setting);
				if (!c) {
					c = {};
					data.setCache(setting, c);
				}
				c.nodes = [];
				c.doms = [];
			},
			pro.__unbindEvent = function(){
				var o=this.treeObj;
				v._$clearEvent(o);
			}
			pro.__bindEvent = function(){
				var o=this.treeObj;
				var that=this;
				v._$addEvent(o, 'click',function(event){that.__eventProxy(event)});
				v._$addEvent(o, 'dblclick',function(event){that.__eventProxy(event)});
			}
				//default event proxy of core
			pro.__eventProxy=function(event) {
					var target = event.target,
						setting = this.setting,
						tId = "",
						node = null,
						nodeEventType = "", 
						treeEventType = "",
						that = this;
					if (tools.eqs(event.type, "mousedown")) {
						treeEventType = "mousedown";
					} else if (tools.eqs(event.type, "mouseup")) {
						treeEventType = "mouseup";
					} else if (tools.eqs(event.type, "contextmenu")) {
						treeEventType = "contextmenu";
					} else if (tools.eqs(event.type, "click")) {
						if (tools.eqs(target.tagName, "div") && target.getAttribute("treenode") == "switch") {
							tId = tools.getNodeMainDom(target).id;
							nodeEventType = "switchNode";
						}
						if (tools.eqs(target.tagName, "span") && target.getAttribute("treenode") == "switch") {
							tId = tools.getNodeMainDom2(target).id;
							nodeEventType = "switchNode";
						}
						if (tools.eqs(target.tagName, "span") && target.getAttribute("treenode") == "add") {
							tId = tools.getNodeMainDom2(target).id;
							nodeEventType = "addNode";
						}
						if (tools.eqs(target.tagName, "span") && target.getAttribute("treenode") == "up") {
							tId = tools.getNodeMainDom2(target).id;
							nodeEventType = "upNode";
						}
						if (tools.eqs(target.tagName, "span") && target.getAttribute("treenode") == "down") {
							tId = tools.getNodeMainDom2(target).id;
							nodeEventType = "downNode";
						}
						if (tools.eqs(target.tagName, "span") && target.getAttribute("treenode") == "edit") {
							tId = tools.getNodeMainDom2(target).id;
							nodeEventType = "editNode";
						}
						if (tools.eqs(target.tagName, "span") && target.getAttribute("treenode") == "del") {
							tId = tools.getNodeMainDom2(target).id;
							nodeEventType = "delNode";
						}
						if (tools.eqs(target.tagName, "input")) {
							tId = tools.getNodeMainDom2(target).id;
							nodeEventType = "checkbox";
						}
						if (tools.eqs(target.tagName, "span") && target.getAttribute("treenode") == "submit") {
							tools.apply(this,this.setting.callback.onSubmit, [this]);
						}
					} else if (tools.eqs(event.type, "dblclick")) {
						treeEventType = "dblclick";
						if(tools.eqs(target.tagName, "li")){
							tId = target.id;
							nodeEventType = "switchNode";
						}
					}
					// event to node
					if (tId.length>0) {
						node = e._$get(tId);
						node.isParent=e._$dataset(node,"parent")=="true"?true:false;
						switch (nodeEventType) {
							case "switchNode" :
								if (!node.isParent) {
									nodeEventType = "";
								} else if (tools.eqs(event.type, "click")) {
									view.switchNode(node);
								} else {
									nodeEventType = "";
								}
								break;
							case "addNode" :
								tools.apply(this,this.setting.callback.onAdd, [this.setting.treeId, node]);
								break;
							case "upNode" :
								tools.apply(this,this.setting.callback.onUp, [this.setting.treeId, node]);
								break;
							case "downNode" :
								tools.apply(this,this.setting.callback.onDown, [this.setting.treeId, node]);
								break;
							case "editNode" :
								tools.apply(this,this.setting.callback.onEdit, [this.setting.treeId, node]);
								break;
							case "delNode" :
								tools.apply(this,this.setting.callback.onDel, [this.setting.treeId, node]);
								break;
							case "checkbox" :
								that.__onCheckBoxClick(event,node);
								break;
						}
					}
			}
			
			pro.__onCheckBoxClick = function(event,node){
				var _elm = v._$getElement(event),
					_check = _elm.checked;
				
				if(_elm.type == 'checkbox'){
					this.__recheckTree(node,_check);
				}
			}
			pro.__recheckTree = function(node,_check){
				var _list = e._$getByClassName(node,'chk');
				//处理孩子节点
				if(_list.length>0){
					this.__checkChild(node,_check);
				}
				if(this.setting.associate.parent){
					//处理父节点
					this.__checkParent(node,_check);
				}
			}
			pro.__checkChild = function(node,_check){
				var _list = e._$getByClassName(node,'chk');
				
				for(var i=0,len=_list.length; i<len; i++){
					_list[i].checked = _check;
				}
			}
			pro.__checkParent = function(node,_check){
				var _id = e._$attr(node,'id'),
					_parent = e._$dataset(node,'parentid');
				   
				    var obj=e._$get(_parent);
				    var _list = e._$getByClassName(obj,'chk');
				    _list[0].checked =_check;
				        
				    var objid=e._$dataset(obj,'parentid')||"";
				    if(objid!="0"){
				    	this.__checkParent(obj,_check);
				    }
			}
			pro.__getChecks = function(){
				var data = [],
					_list = e._$getByClassName(this.treeObj,'chk');
				for(var i=0,len=_list.length; i<len; i++){
					var level=e._$dataset(tools.getNodeMainDom2(_list[i]),'level');
					if(_list[i].checked && level==3){
						var id=tools.getNodeMainDom2(_list[i]).id;
						var name=e._$dataset(tools.getNodeMainDom2(_list[i]),'name');
						var obj=new Object();
						obj.id=id;
						obj.name=name;
						data.push(obj);
					}
				}
				return data;
			}
			
			//以[{"id":11},{"id":112},{"id":13}]的方式返回
			pro._$getData = function(){
				return this.__getChecks();
			};

			pro._$setSelectBox = function(data){
				var that=this;
				if(data.length<0) return false;
				
				for(var i=0;i<data.length;i++){
					that._$setCheck(data[i].id);
				}
				
			}
			pro._$setCheck=function(id){
				var _list = e._$getByClassName(this.treeObj,'chk');
				for(var i=0,len=_list.length;i<len;i++){
					if(e._$dataset(_list[i],'id') == id){
						_list[i].checked = true;
					}
				}
			}
			pro._$district=function(){
				var _list = e._$getByClassName(this.treeObj,'district');
				for(var i=0,len=_list.length;i<len;i++){
					var data=e._$dataset(_list[i],'district'),dataArray=data.split(",");
					this.__renderDistrict(_list[i],dataArray);
				}
			}
			pro.__renderDistrict=function(obj,data){
				var $wrap=$("<div class='d-wrap'><div class='d-wrap-title'><span>"+data[0]+"</span><span class='glyphicon glyphicon-chevron-down'></span></div></div>");
				var $ul=$("<ul class='d-ul'></ul>");
				var that=this;
				for(var i=0,len=data.length;i<len;i++){
					var $li=$("<li>"+data[i]+"</li>");
					$ul._$insert($li,"append");
				}
				$wrap._$insert($ul,"append");
				$(obj)._$insert($wrap,"append");
				$wrap._$on("click",function(_event){
					v._$stop(_event);
					var _ul=$(this)._$children("ul");
					var _list = e._$getByClassName(that.treeObj,'d-ul');
      				for(var i=0,len=_list.length;i<len;i++){
      					if(_list[i]!=_ul[0] && _list[i].style.display=="block"){
      						_list[i].style.display="none";
      					}
	 				}
					
					if(_ul._$style("display")=="block"){
						_ul._$style("display","none");
					}else{
						_ul._$style("display","block");
					}
					
				});
			}
			pro._$expandAll=function(expandFlag){
				//var nodes=$("#tree")._$children("li");
				var nodes=e._$getChildren(e._$get("tree"));
				for(var i=0,len=nodes.length;i<len;i++){
					view.switchALL(nodes[i],expandFlag);
				}
			}
			/**
			 * 控件销毁
			 * 
			 */
			pro.__destroy = function() {
				this.__super();
			};
			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
				this.__seed_css  = _seed_css;
			};
			return p._$$Tree;
		})