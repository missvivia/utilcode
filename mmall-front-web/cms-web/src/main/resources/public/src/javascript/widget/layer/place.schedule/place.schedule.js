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
		 , 'text!./place.schedule.css'
	     , 'text!./place.schedule.html'
	     , '{lib}util/template/tpl.js'
	     , '{pro}widget/ui/warning/warning.js'
	     ,'{lib}ui/datepick/datepick.js'
	     ,'util/form/form'
	     ,'{pro}extend/request.js'
	     ,'pro/components/notify/notify',
		 ],
		function(ut, v, e, t, Window,css,html,e1,Warning,ui,t0,Request,Notify,p, o, f, r) {
			var pro,
			_seed_css = e._$pushCSSText(css),
	        _seed_html= e1._$addNodeTemplate(html);


			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$PlaceScheduleWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$PlaceScheduleWin = NEJ.C();
			pro = p._$$PlaceScheduleWin._$extend(Window);
			/**
			 * 控件重置
			 * @protected
			 * @method {__reset}
			 * @param  {Object} options 可选配置参数
			 */
			pro.__reset = function(options) {
				options.title = '置顶档期';
				this.__super(options);
				this.id = options.id;
				this.__form = t0._$$WebForm._$allocate({
		            form:this.form,
		            
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
				this.__super();
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.form = list[0];
				v._$addEvent(list[1], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[2], 'click', this.__onCCClick._$bind(this));
				v._$addEvent(this.form.startTime, 'click', this.__onDatePick._$bind(this,this.form.startTime));
				v._$addEvent(this.form.endTime, 'click', this.__onDatePick._$bind(this,this.form.endTime));
			};
			
			pro.__onDatePick = function(elm,event){
				v._$stop(event);	
				ui._$$DatePick._$allocate({parent:elm.parentNode,clazz:'calendar',onchange:this.__onDateChange._$bind(this,elm)})
			}
			pro.__onDateChange = function(elm,date){
				elm.value = ut._$format(date,'yyyy-MM-dd');
			}
			pro.__onCCClick = function(event) {
				this.__form._$recycle();
				this._$hide();
			};
			
			pro.__onOKClick = function(event) {
				if (this.__form._$checkValidity()){
		            var data = this.__form._$data();
		            Request('/schedule/top',{
						data:data,
						onload:this.__cbScheduleTop._$bind(this),
						onerror:this.__onError._$bind(this)})
		        }
				
			}
			pro.__cbScheduleTop = function(result){
				if(result.code==200){
					this.__form._$recycle();
					Notify.show('置顶成功');
					this._$dispatchEvent('onok');
					this._$hide();
				} else{
					Notify.show('置顶失败');
				}
			}
			pro.__onError = function(){
				Notify.show('置顶失败');
			}
			return p._$$PlaceScheduleWin;
		})