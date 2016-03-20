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
		  , 'text!./calendar.css'
	      , 'text!./calendar.html'
	      , '{lib}util/template/jst.js'
	      ,'{pro}extend/request.js'
	      , 'util/sort/vertical'],
		function(u, v, e, t, i,t1,e1,css,html,e2,Request,ut0,p, o, f, r) {
			var pro,
			_seed_css = e._$pushCSSText(css),
			_seed_html,
	        _seed_ui = e1._$parseUITemplate(html),
	        _seed_date = _seed_ui['seedDate'],
	        _seed_action = _seed_ui['seedAction'],
	        _monthDays =[31,28,31,30,31,30,31,31,30,31,30,31];
			var test={
			            1:['秋水','红蜻蜓','巴拉','水星','压根儿'],
			            2:['秋水','红蜻蜓','巴拉','水星','压根儿'],
			            3:['秋水','红蜻蜓','巴拉','水星','压根儿']
			        }
			/**
			 * 全局状态控件
			 * 
			 * @class {nm.i._$$Calendar}
			 * @extends {nej.ui._$$Abstract}
			 */
			p._$$Calendar = NEJ.C();
			pro = p._$$Calendar._$extend(i._$$Abstract);


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
				this.province = options.province;
				this.now = new Date();
				
				this.__getMonthPO();
				
			};
			
			
			pro.__onError = function(){
				
			};
			pro.__getMonthPO = function(year,month){
				var year = this.now.getFullYear();
				var month = this.now.getMonth();
				this.year.innerText = year;
				this.month.innerText = month+1;
				Request('/schedule/list/month',{
					method:'POST',
					type:'json',
					norest:true,
					data:u._$object2query({province:this.province,year:year,month:month+1}),
					onload:this.__cbGetMonthPO._$bind(this),
					onerror:this.__onError._$bind(this)})
			}
			pro._$setProvince = function(province){
				this.province = province;
				this.__getMonthPO();	
			}
			pro.__cbGetMonthPO = function(result){
				if(result.code==200){
					var poData = result.result.data;
					var days = this.__getMonthDays(poData,this.now);
					if(this.sortList&&this.sortList.length){
						for(var i=this.sortList.length;i>=0;i--){
							this.sortList[i] = this.sortList[i]&&this.sortList[i]._$recycle();
							this.sortList.splice(i,1);
						}
					}
					e2._$render(this.monthTbl,_seed_date,{data:days});
					var list = e._$getByClassName(this.monthTbl,'j-polist');
					this.sortList =[];
					for(var i=0,l=list.length;i<l;i++){
						var placeholder = e._$getByClassName(list[i],'def')[0],
							date = e._$dataset(list[i],'date'),
							sort;
						(function(sort,parent,placeholder,date,_province){
							sort = ut0._$$VSortable._$allocate({
					               clazz:'itm-'+e._$dataset(parent,'index'),
					               parent:parent,
					               placeholder:placeholder,
					               onsortchange:function(){
					            	   var ids = sort._$getSortList();
					            	   
					            	   Request('/schedule/sort',{
									   					method:'POST',
									   					type:'json',
									   					data:{curSupplierAreaId:_province,date:date,poIds:ids},
									   					onload:f,
									   					onerror:f
					            	   })
					               }
								})
						})(sort,list[i],placeholder,date,this.province);
						this.sortList.push(sort);
					 };
				}
			};
			pro.__getMonthDays = function(poData,date){
				date.setDate(1);
				var day = date.getDay();
				var month = date.getMonth();
				if(month==1){
					days = this.__getFebruaryDays(date);
				} else{
					days = _monthDays[date.getMonth()];
				}
				var list = this.__getPreMonthDays(date,day);
				for(var i=1;i<=days;i++){
					var cloneDate = new Date();
					cloneDate.setMonth(month);
					cloneDate.setDate(i);
					list.push({state:0,date:i,data:poData[i],day:cloneDate});
				}
				var remains = (7-list.length%7+1);

				for(var i=1;i<remains;i++){
					var cloneDate = new Date();
					cloneDate.setMonth(month+1);
					cloneDate.setDate(i);
					list.push({state:-1,date:i,day:cloneDate});
				}
				return list
			};
			pro.__getFebruaryDays = function(date){
				return new Date(date.getFullYear() , 2 , 0).getDate();
			};
			pro.__getPreMonthDays = function(date,day){
				var list = [];
				var tdata = new Date(date);
				tdata.setDate(0);
				var preMontLastDate = tdata.getDate();
				for(var i=preMontLastDate-day+1;i<=preMontLastDate;i++){
					var cloneDate = new Date();
					cloneDate.setMonth(tdata.getMonth());
					cloneDate.setDate(preMontLastDate);
					list.push({state:-1,date:i,day:cloneDate});
				}
				return list
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
				// 在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
				// this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
				// 这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
				// this.__seed_html = 'wgt-ui-xxx';
				this.__seed_css  = _seed_css;
		        this.__seed_html = _seed_html;
			};

			/**
		     * 动态构建控件节点模板
		     *
		     * @protected
		     * @method module:ui/datepick/datepick._$$DatePick#__initNodeTemplate
		     * @return {Void}
		     */
		    pro.__initNodeTemplate = function(){
		        _seed_html = e1._$addNodeTemplate(e1._$getTextTemplate(_seed_action));
		        this.__seed_html = _seed_html;
		    };
			/**
			 * 节点初使化
			 * 
			 */
			pro.__initNode = function() {
				this.__super();
				var list = e._$getByClassName(this.__body, 'z-tag');
				this.preMonth = list[0];
				this.nxtMonth = list[1];
				this.year = list[2];
				this.month = list[3];
				this.monthTbl = list[4];
				v._$addEvent(this.preMonth,'click',this.__onPreMonthClick._$bind(this));
				v._$addEvent(this.nxtMonth,'click',this.__onNxtMonthClick._$bind(this));
			};
			pro.__onPreMonthClick = function(){
				var month = this.now.getMonth();
				if(month==0){
					this.now.setFullYear(this.now.getFullYear()-1,11);
				} else{
					this.now.setMonth(month-1);
				}
				this.__getMonthPO();
			};
			pro.__onNxtMonthClick = function(){
				var month = this.now.getMonth();
				if(month==11){
					this.now.setFullYear(this.now.getFullYear()+1,0);
				} else{
					this.now.setMonth(month+1);
				}
				this.__getMonthPO();
			};
			return p._$$Calendar;
		})