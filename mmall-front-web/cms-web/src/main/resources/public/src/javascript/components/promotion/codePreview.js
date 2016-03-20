define(
		['{lib}base/util.js'
		 ,'{lib}base/event.js'
		 ,'{lib}base/element.js'
		 ,'{lib}util/event.js'
		 ,'{lib}util/template/jst.js'
		 ,'{pro}widget/layer/window.js'
		 ,'{pro}extend/request.js'
		 ,'{pro}components/notify/notify.js'
		 ,'{lib}util/chain/NodeList.js'
		 ,'text!./codePreview.html?v=1.0.0.2'
		 ],
		function(u, v, _e, t, e2, Window, Request, notify, $,html,p ) {
		  
			var pro;
			p._$$InfoGroupWin = NEJ.C();
			pro = p._$$InfoGroupWin._$extend(Window);
			
			pro.__reset = function(options) {
			    
			    var _seed_html=e2._$add(html);
				e2._$render(this.__body,_seed_html,{item:options.item});
				this.__super(options);
				var _self = this;
				$(".j-flag")._$on("click",function(e){
					_self.__onOKClick(e);
				});
			}
			
		    
//			pro.__initXGui = function() {
//				this.__seed_css  = _seed_css;
//			};
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
			pro.__onOKClick = function(event) {
				v._$stop(event);
				this._$dispatchEvent('onok', '关闭窗体');
				this._$hide();
			}
			
			return p._$$InfoGroupWin;
		});