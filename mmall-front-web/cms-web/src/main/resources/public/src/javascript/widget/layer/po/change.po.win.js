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
		 , 'text!./change.po.win.css'
     , 'text!./change.po.win.html'
     , '{lib}util/template/tpl.js'
     , '{lib}ui/datepick/datepick.js'
     , '{pro}/components/notify/notify.js'
     , 'util/form/form'
     , 'pro/extend/request'
		 ],
		function(u, v, e, t, Window,css,html, e1,ut,notify,ut1,request,p, o, f, r) {
			var pro,
			_seed_css = e._$pushCSSText(css),
	        _seed_html= e1._$addNodeTemplate(html);


			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$ChangePOWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$ChangePOWin = NEJ.C();
			pro = p._$$ChangePOWin._$extend(Window);
			/**
			 * 控件重置
			 * @protected
			 * @method {__reset}
			 * @param  {Object} options 可选配置参数
			 *                           type 1		有时间输入
			 *                           type 0		无时间输入
			 */
			pro.__reset = function(options) {
				options.title = '调整';
				this.__poFollowUser.value = options.schedule.poFollowerUserName;
				this.__dateIpt.value = u._$format(options.schedule.startTime,'yyyy-MM-dd');
				this.__schedule = options.schedule;
				this.__super(options);
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
				this.__dateIpt = list[1];
				this.__poFollowUser = list[2];
				this.__webform = ut1._$$WebForm._$allocate({form:this.form});
				v._$addEvent(list[3], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[4], 'click', this.__onCCClick._$bind(this));
				v._$addEvent(this.__dateIpt, 'click', this.__onDatePickShow._$bind(this));
			};
			
			pro.__onDatePickShow = function(event){
				v._$stop(event);
				if(this.datePick){
					this.datePick = this.datePick._$recycle();
				}
				this.datePick = ut._$$DatePick._$allocate({parent:this.__dateIpt.parentNode,
									clazz:'m-datepick',
									range:[new Date(),0],
									onchange:this.__onDateChange._$bind(this)})
			};
			pro.__onDateChange = function(date){
				this.__dateIpt.value = u._$format(date,'yyyy-MM-dd');
			}
			pro.__onCCClick = function(event) {
				this._$hide();
			};
			pro.__onOKClick = function(event) {
				if(this.__webform._$checkValidity()){
					var data = this.__webform._$data();
					this._$dispatchEvent('onok',data);
					this._$hide();
//					request('/schedule/checksupplieracctvalid',{
//						data:{supplierAcct:data.poFollowerUserName,scheduleId:this.__schedule.id},
//						onload:function(_json){
//							if(_json.code==200){
//								if(_json.result){
//									this._$dispatchEvent('onok',data);
//									this._$hide();
//								} else{
//									notify.show('跟进人不存在')
//								}
//							}
//						}._$bind(this)
//						
//					})
				}
			}
			return p._$$ChangePOWin;
		})