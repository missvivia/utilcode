/*
 * --------------------------------------------
 * xx控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define([ 'base/klass', 
         'base/util', 
         '{pro}widget/layer/window.js',
         'pro/page/activity/components/lotteryresult/lotteryresult',
         'base/event', 
         'base/element', 
         'util/event',
		 'ui/base', 
		 'util/template/tpl', 
		 'text!./resultwindow.html',
		 'text!./resultwindow.css'
		 ],
		function(k, ut,_BaseWindow,LyResult, v, _e, t, i,_t,_html,_css ,p, o, f, r) {
			var pro, sup, 
			_seed_html = _t._$addNodeTemplate(_html),
			_seed_css = _e._$pushCSSText(_css);

			/**
			 * 全局状态控件
			 * @class   {nm.i._$$ResultWindow}
			 * @extends {nej.ui._$$Abstract}
			 */
			p._$$ResultWindow = k._$klass();
			pro = p._$$ResultWindow._$extend(_BaseWindow);
			sup = p._$$ResultWindow._$supro;

			/**
			 * 重置控件
			 * @param  {[type]} options [description]
			 *
			 */
			pro.__reset = function(options) {
				options.parent = options.parent || document.body;
				this.__super(options);
				_e._$removeByEC(this.__layer.__dopt.mbar);
				this.__initResult(options.result);
			};
			
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
				//在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
				//this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
				//这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
				this.__seed_html = _seed_html;
				this.__seed_css = _seed_css;
			};
			/**
			 * 节点初使化
			 *
			 */
			pro.__initNode = function() {
				this.__super();
//				var list = e._$getByClassName(this.__body,'j-flag');
//				v._$addEvent(this.__ball,'click',this.__onBallBroken._$bind(this));
			};
			
			pro.__initResult=function(_result){
				var that=this;
//				_result.type=105;
				this.__lyResult=new LyResult({data:_result});
				this.__lyResult.$inject(this.__body);
				this.__lyResult.$on("close",function(){
					that._$hide();
				});
			};
			
			return p._$$ResultWindow;
		})