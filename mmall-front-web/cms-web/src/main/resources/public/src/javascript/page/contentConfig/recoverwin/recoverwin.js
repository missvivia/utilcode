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
		 ,'text!./recoverwin.html?v=1.0.0.0'
		 ,'text!./recoverwin.css'
		 ],
		function(u, v, _e, t, e2,_p, Window, Request, notify, SureWin, $,html,css,p ) {
			
			var _seed_css = _e._$pushCSSText(css);
		  
			var pro;
			p._$$InfoGroupWin = NEJ.C();
			pro = p._$$InfoGroupWin._$extend(Window);
			
			pro.__reset = function(options) {
				var formData = options.item;
				options.title = '首页配置-文件恢复';
				
				var _seed_html=e2._$add(html);
				e2._$render(this.__body,_seed_html,{item:formData});
				this.__super(options);
				
				var that = this;
				$('#recoverAgain')._$on("click",function(e){
					that._$dispatchEvent("onok",that.type);
				});
				
			};
			
			pro.__showContent = function(flag,type,result){
				if(flag){
					_e._$style('progressRecover',{display:'none'});
					_e._$style('recoverSuccess',{display:'block'});
				}else{
					_e._$style('progressRecover',{display:'none'});
					_e._$style('recoverfail',{display:'block'});
					if(result != ""){
						_e._$get('recoverMsg').innerHTML = result;
					}
					this.type = type;
				}
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