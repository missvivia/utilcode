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
		  , '{lib}util/animation/easeinout.js'
		  , '{lib}util/template/tpl.js'
		  , 'text!./warning.css'
	      , 'text!./warning.html'
	      ,'base/config'],
		function(u, v, e, t, i,t1,e1,css,html,c,p, o, f, r) {
			var pro,
			_seed_css = e._$pushCSSText(css,{root:c._$get('root')}),
	        _seed_html= e1._$addNodeTemplate(html);

			/**
			 * 全局状态控件
			 * 
			 * @class {nm.i._$$Warning}
			 * @extends {nej.ui._$$Abstract}
			 */
			p._$$Warning = NEJ.C();
			pro = p._$$Warning._$extend(i._$$Abstract);


			/**
			 * 重置控件
			 * 
			 * @param {[type]}
			 *            options [description]
			 * 
			 */
			pro.__reset = function(options) {
				options.parent = options.parent || document.body;
				this.__super(options);
				this.txt.innerText = options.text;
				this.__body.style.left = (document.body.clientWidth-this.__body.clientWidth)/2 +'px';
				this.config= {to:{},from:{}}
				this.config.to.offset =1;
				this.config.from.offset =0;
				console.log(this.__body)
				this.config.onupdate = this.__doAnimation._$bind(this);
				this.config.onstop = this.__doShowStopAnimation._$bind(this);
				this.__doShowAnimiation();
				
			};
			
			pro.__doShowAnimiation = function(){
				this.__anim = t1._$$AnimEaseInOut._$allocate(this.config);
				this.__anim._$play();
			};
			pro.__doShowStopAnimation = function(){
				if (!!this.__anim) {
	                this.__anim._$recycle();
	                delete this.__anim;
	            }
				this.timer = setTimeout(this.__doHideAnimation._$bind(this),3000);
			}
			pro.__doAnimation = function(event){
				var offset = event.offset;
				console.log('offset:',offset)
				this.__body.style.top = (offset*90 -65 ) +'px';
			}
			pro.__doHideAnimation = function(){
				this.config.to.offset =0;
				this.config.from.offset =1;
				this.config.onstop = this.__doHideStopAnimation._$bind(this);
				this.__anim = t1._$$AnimEaseInOut._$allocate(this.config);
				this.__anim._$play();
			};
			pro.__doHideStopAnimation = function(){
				if (!!this.__anim) {
	                this.__anim._$recycle();
	                delete this.__anim;
	            }
				this._$hide();
			};
			/**
			 * 控件销毁
			 * 
			 */
			pro.__destroy = function() {
				if (!!this.__anim) {
	                this.__anim._$recycle();
	                delete this.__anim;
	            }
				if(this.timer){
					clearTimeout(this.timer);
				}
				this.__super();
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
			 * 节点初使化
			 * 
			 */
			pro.__initNode = function() {
				this.__super();
				//var list = e._$getByClassName(this.__body, 'j-flag');
				this.txt = this.__body;
			};
			
			return p._$$Warning;
		})