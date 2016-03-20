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
		 , 'text!./synchronize.window.html'
		 , '{lib}util/template/tpl.js'
		 , '{pro}/extend/request.js?v=1.0.0.0'
		 , '{lib}util/chain/NodeList.js?v=1.0.0.0'
		 ],
		function(u, v, e, t, Window,_html,e1,Request,$,p, o, f, r) {
			var pro,
//				_seed_css = e._$pushCSSText(_css),
				_seed_html= e1._$addNodeTemplate(_html);

			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$synchronizeWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$synchronizeWin = NEJ.C();
			pro = p._$$synchronizeWin._$extend(Window);
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
				options.title = options.title||'即将更新商品信息';
				this.__super(options);
				this.spuId = options.spuId;
				var url = "/item/spu/syncPre";
				this.txt.innerText = "加载中..."
				Request(url,{
	        		method : "get",
	        		data : {"spuId" : options.spuId},
	        		onload : function(json){
	        			this.txt.innerText = json.message;
        				this.__okBtn.style.display = "inline-block";
        				this.__ccBtn.style.display = "inline-block";
        				this.__closeBtn.style.display = "none";
	        		}._$bind(this),
	        		onerror : function(json){
	        			this.txt.innerText = json.message;
	        		}._$bind(this)
				});				
			};
			
			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
				// 在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
				// this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
				// 这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
				// this.__seed_html = 'wgt-ui-xxx';
//				this.__seed_css = _seed_css;
		        this.__seed_html = _seed_html;
			};
			/**
			 * 销毁控件
			 */
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				this.__super();
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.txt = list[0];
				this.__okBtn = list[1];
				this.__ccBtn = list[2];
				this.__closeBtn = list[3];
				v._$addEvent(list[1], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[2], 'click', this.__onCCClick._$bind(this));
				v._$addEvent(list[3], 'click', this.__onCCClick._$bind(this));
			};
			pro.__onOKClick = function(event) {
				v._$stop(event);
				var _self = this;
				document.getElementById("mask").style.display = "block";
				
				$(event.target)._$parent()._$parent()._$parent()._$prev()[0].style.display = "none";
				var listTr = e._$get(_self.spuId);
				var listBtn = e._$getByClassName(listTr,"j-update");
				Request("/item/spu/doSync",{
	        		method : "post",
	        		data : {"spuId" : _self.spuId},
	        		onload : function(json){
	        			document.getElementById("mask").style.display = "none";
	        			this.__okBtn.style.display = "none";
        				this.__ccBtn.style.display = "none";
	        			this.__closeBtn.style.display = "inline-block";
	        			this.txt.innerText = json.message;
	        			listBtn[2].innerText = "已同步";
	        			listBtn[2].className = "j-update j-disabled";
	        			$("#syncBtn")._$text("已同步");
	        			$("#syncBtn")._$attr("class","btn j-flag btn-disabled");
	        		}._$bind(this),
	        		onerror : function(json){
	        			document.getElementById("mask").style.display = "none";
	        			this.__okBtn.style.display = "none";
        				this.__ccBtn.style.display = "none";
	        			this.__closeBtn.style.display = "inline-block";
	        			this.txt.innerText = json.message;
	        		}._$bind(this)
				});
			};
			pro.__onCCClick = function(event) {
				v._$stop(event);
				this._$hide();
			};
			return p._$$synchronizeWin;
		})