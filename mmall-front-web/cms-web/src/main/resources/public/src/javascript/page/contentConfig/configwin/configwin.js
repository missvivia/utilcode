define(
		['{lib}base/util.js'
		 ,'{lib}base/event.js'
		 ,'{lib}base/element.js'
		 ,'{lib}util/event.js'
		 ,'{lib}util/template/jst.js'
		 ,'{lib}util/template/tpl.js'
		 ,'{pro}widget/layer/window.js'
		 ,'{pro}extend/request.js'
		 ,'{pro}components/notify/notify.js'
		 ,'{pro}widget/layer/sure.window/sure.window.js'
		 ,'{lib}util/chain/NodeList.js'
		 ,'text!./configwin.html?v=1.0.0.0'
		 ,'text!./configwin.css'
		 ],
		function(u, v, _e, t, e2,_p, Window, Request, notify, SureWin, $,html,css,p ) {
			
			var _seed_css = _e._$pushCSSText(css);
		  
			var pro;
			p._$$InfoGroupWin = NEJ.C();
			pro = p._$$InfoGroupWin._$extend(Window);
			
			pro.__reset = function(options) {
				var formData = options.item;
				options.title = '首页配置-文件上传';
				
				var _seed_html=e2._$add(html);
				e2._$render(this.__body,_seed_html,{item:formData});
				this.__super(options);
				
				v._$addEvent('preview','click',this.__preview._$bind(this));
				v._$addEvent('issue','click',this.__issue._$bind(this));
				v._$addEvent('issueAgain','click',function(){
	            	_e._$get('issue').click();
	            }._$bind(this));
				v._$addEvent('goBack','click',function(){
					_e._$style('publishfail',{display:'none'});
					_e._$style('uploadsuccess',{display:'block'});
				}._$bind(this));
				v._$addEvent('reupload','click',function(){
					this._$hide();
				}._$bind(this));
			};
			
			pro.__showContent = function(flag,type,result){
				if(flag){
					_e._$style('progressUpload',{display:'none'});
					_e._$style('uploadsuccess',{display:'block'});
					this.type = type;
				}else{
					_e._$style('progressUpload',{display:'none'});
					_e._$style('uploadfail',{display:'block'});
					if(result != ""){
						_e._$get('msg').innerHTML = result;
					}
				}
			};
			
			pro.__preview = function(event){
				var code,
				    url,
				    type = this.type;
				 
				if(type > 3){
					Request('/content/buildCategoryHtml',{
	                    method:'GET',
	                    type:'JSON',
	                    sync:true,
	                    data:{fileType:type},
	                    onload:function(result){
	                    	if(result.code==200){
	                    		code = result.code;
	                    	}
	                    }._$bind(this),
	                    onerror:function(result){
	                    	notify.show(result.message);
	                    }._$bind(this)
	                });
					
                    if(code == 200){
                    	window.open('http://023.baiwandian.cn/templates/category/index.html','_blank');
                    }					
				}else if(type <= 3){
					Request('/content/previewIndex',{
	                    method:'GET',
	                    type:'JSON',
	                    sync:true,
	                    data:{fileType:type},
	                    onload:function(data){
	                    	if(data.code==200){
	                    		code = data.code;
	                    		url = data.result.preHtmlUrl;
	                    	}
	                    }._$bind(this),
	                    onerror:function(data){
	                    	notify.show(data.message);
	                    }._$bind(this)
	                });
					if(code == 200){
						window.open(url, '_blank');
					}
				}
			};
			
			pro.__issue = function(){
				var uploadsuccessGroup = _e._$get('uploadsuccess'),
        	        progressGroup = _e._$get('progressIssue'),
        	        publishsuccessGroup = _e._$get('publishsuccess'),
        	        publishfailGroup = _e._$get('publishfail'),
        	        type = this.type,
        	        url = '',
        	        text = '',
        	        that = this;
				
				if(type > 3){
					url = '/content/publishCategoryHtml';
					text = '发布当前首页类目配置文件将直接替换正在使用的首页类目，是否确认发布？';
				}else{
					url = '/content/publishIndex';
					text = '发布当前首页配置文件将直接替换正在使用的首页，是否确认发布？';
				}
        	    
	        	SureWin._$allocate({
	        		parent:document.body,
	        		title:'提示',
	        		text:text,
	        		mask:'none',
	        		onok:function(){
	        			this._$hide();
	        			that.__layer._$setTitle('首页配置-发布页面',true);
	        			_e._$style('publishfail',{display:'none'});
	        			_e._$style(uploadsuccessGroup,{display:'none'});
	        		    _e._$style(progressGroup,{display:'block'});
		        		Request(url,{
		                    method:'GET',
		                    type:'JSON',
		                    data:{fileType:type},
		                    onload:function(result){
		                    	if(result.code==200){
		                    		_e._$style(progressGroup,{display:'none'});
		                    		_e._$style(publishsuccessGroup,{display:'block'});
		                    	}
		                    }._$bind(this),
		                    onerror:function(result){
		                    	_e._$style(progressGroup,{display:'none'});
	     						_e._$style(publishfailGroup,{display:'block'});
	                    		_e._$get('publishmsg').innerHTML = result.message;
		                    }._$bind(this)
		                });
	        	    }
	        	})._$show();
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