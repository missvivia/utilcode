define(
		['{lib}base/util.js'
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
		 ,'{pro}components/datepicker/datepicker.js'
		 ,'../tab/tab.js'
		 ,'text!./updatainfo.html?v=1.0.0.1'
		 ,'text!./updatainfo.css?v=1.0.0.0'
		 ,'../user.list/list.js'
		 ,'{lib}util/file/select.js'
		 ,"{pro}widget/layer/window.js"
		 ,'{lib}util/ajax/xdr.js'
		 ,'{pro}extend/request.js'
		 ,'{pro}components/modal/modal.js'
		 ],
		function(u, v, _e, t, e2,_p, Window, Request, _t, notify, $,Datepick,Tab,html,css,List,f,Dialog,j,Request,Modal,p ) {
			
			var trim = function(str){ //删除左右两端的空格
			    return str.replace(/(^\s*)|(\s*$)/g, "");
			};
			
			var _seed_css = _e._$pushCSSText(css);
		  
			var pro;
			p._$$InfoGroupWin = NEJ.C();
			pro = p._$$InfoGroupWin._$extend(Window);
			
			pro.__reset = function(options) {
				var formData = options.item;
			    options.title = '管理指定用户';
				var _seed_html=e2._$add(html);
				e2._$render(this.__body,_seed_html,{item:formData});
				this.businessId = formData.businessId;
				this.__super(options);
				this.__getNodes();
				
				this.__tab = new Tab({
		            data:{}
		        });
				
		        this.__tab.$inject('#tab-box');
		        this.__tab.$on('change',this.__onTabChange._$bind(this));
		        
		        this.__list = new List({data:{condition:{businessId:this.businessId,userName:''}}}).$inject('#userlist');
		        
		        this.__addEvent();
			}
			
			pro.__getNodes = function(){
				this.__addBox = _e._$get('add-box');
				this.__addUserInput = _e._$get('addUserInput');
				this.__addUserInput.focus();
				this.__addUser = _e._$get('addUser');
				this.__userlistBox = _e._$get('userlist-box');
				this.__importBox = _e._$get('import-box');
				this.__searchUserInput = _e._$get('searchUserInput');
				this.__searchUserList = _e._$get('searchUserList');
				this.__importUser = _e._$get('importUser');
				this.__exportUser = _e._$get('exportUser');
			}
			
			pro.__onTabChange = function(_index){
		        if(_index == 0){
		        	_e._$replaceClassName(this.__addBox,'hidden','show');
		        	_e._$replaceClassName(this.__userlistBox,'show','hidden');
		        	_e._$replaceClassName(this.__importBox,'show','hidden');
		        	this.__addUserInput.value = '';
		        	this.__addUserInput.focus();
		        }else if(_index == 1){
		        	_e._$replaceClassName(this.__addBox,'show','hidden');
		        	_e._$replaceClassName(this.__userlistBox,'hidden','show');
		        	_e._$replaceClassName(this.__importBox,'show','hidden');
		        	this.__list.refresh();
		        }else if(_index == 2){
		        	_e._$replaceClassName(this.__addBox,'show','hidden');
		        	_e._$replaceClassName(this.__userlistBox,'show','hidden');
		        	_e._$replaceClassName(this.__importBox,'hidden','show');
		        }
		    };
		    
		    pro.__addEvent = function(){
		    	v._$addEvent(this.__addUser,'click',this.__addUsers._$bind(this));
				v._$addEvent(this.__searchUserInput,'keyup',this.__searchSelectUsers._$bind(this));
		        v._$addEvent(this.__searchUserInput,'click',this.__searchSelectUsers._$bind(this));
		        v._$addEvent(this.__searchUserInput.parentNode,'mouseleave',this.__showSearchUserList._$bind(this,false));
		        v._$addEvent(this.__importUser,'click',this.__importUsers._$bind(this));
		        v._$addEvent(this.__exportUser,'click',this.__exportUsers._$bind(this));
			};
		    
		    pro.__addUsers = function(){
		    	var userName = this.__addUserInput.value;
		    	if(trim(userName).length==0){
		    		notify.show('请输入账号');
		    		return;
		    	}
		    	Request('/business/addBusiUserRelation',{
                    method:'POST',
                    type:'JSON',
                    data:{
                    	userName:userName,
                    	businessId:this.businessId,
                    },
                    onload:this.__loadAddUsers._$bind(this),
                    onerror:this.__loadAddUsers._$bind(this)
                })
		    };
		    
		    pro.__loadAddUsers = function(result){
		    	if(result.code == 200){
		    		notify.show(result.result.msg);
		    		this.__addUserInput.value = '';
		    		this.__addUserInput.focus();
		    	}else{
		    		notify.show(result.result.msg);
		    	}
		    	
		    };
		    
		    pro.__searchSelectUsers = function(){
				var text = this.__searchUserInput.value;
				if(text.length < 4){
					this.__showSearchUserList(false);
				}
				else {
					// TODO:分页实现
					Request('/business/busiuser/list?businessId='+this.businessId+'&userName='+text+'&limit=100&offset=0',{
	                    method:'GET',
	                    type:'JSON',
	                    onload:this.__loadSearchUsers._$bind(this),
	                    onerror:this.__loadSearchUsers._$bind(this)
	                })
				}           
			};
			
			pro.__loadSearchUsers = function(_result){
				if(_result.code==200){
				    this.__showSearchUserList(true);
					
					var list = _result.result.list;
	                var dom='';
	                if(list.length == 0){
	                	this.__searchUserList.innerHTML = "<div style='height:200px;line-height:200px;text-align:center;'>未找到相关用户</div>";
	                }else{
	                	for(var i=0;i<list.length;i++){
							var user = list[i];
							dom += '<li class="liElement"><label class="label-width">'+user.userName+'</label><a class="btn btn-link j-flag">删除</a></li>';
						}
		                this.__searchUserList.innerHTML=dom;
		                
		                var elements = this.__searchUserList.getElementsByTagName('a');
		                for(var i=0;i<elements.length;i++){
		                	var user = list[i];
		                	v._$addEvent(elements[i],'click',this.__deleteUser._$bind(this,user.id));
		                }
	                }
				}
			};
			
			pro.__showSearchUserList = function(_show){
		            this.__searchUserList.innerHTML="";
		                
					if(_show) {
						_e._$replaceClassName(this.__searchUserList,'userList-hide','userList-show');
					}
					else {
						_e._$replaceClassName(this.__searchUserList,'userList-show','userList-hide');
					}
			};
			
			pro.__deleteUser = function(_userId){
				Request('/business/deleteBusiUserRelation/'+_userId,{
	                onload:function(_result){
	                	if(_result.code==200){
	                		notify.show('删除指定用户成功');
	                		this.__searchSelectUsers();
	                		this.__list.refresh();
	                	}
	                }._$bind(this),
	                onerror:function(_error){
	                	notify.show('删除指定用户失败');
	                }
	            });
			},
			
			pro.__importUsers = function(){
				var _id = f._$bind('importUser',{
			         name : "file",
			         accept : "xls,xlsx",
			         param:{'businessId':this.businessId},
			    	 onchange:this.__onFileUpload._$bind(this) 
			    });
			};
			
			pro.__onFileUpload = function(event){
				this.__loadingWin = Dialog._$allocate({
		           	clazz : "loading m-window",
		           	parent:document.body
	           })._$show();
	           $(".loading .zcnt")[0].innerText = "导入中,请稍候...";				
				var form = event.form;
				form.action =  '/business/user/import';
				j._$upload(form,{
					type:"text",
					onload:function(result){
						this.__loadingWin._$hide();
						var list = result.split("\n");
						var html = list.join("</div><div>");
						html = "<div>" + html + "</div>";
						var modal = new Modal({
				        	data:{
				          	'title':'提示',
				          	'content':html,
				          	'width':500,
				        	}
				      	});
					}._$bind(this),
					onerror:function(result){
						this.__loadingWin._$hide();
						notify.show(result);
					}._$bind(this)
				})
			};
			
			pro.__exportUsers = function(){
				this.__exportUser.href = "/business/export/"+this.businessId;
			};
		    
			pro.__initXGui = function() {
				this.__seed_css  = _seed_css;
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
			
			return p._$$InfoGroupWin;
		});