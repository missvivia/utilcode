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
		 ,'text!./updatainfo.html?v=1.0.0.1'
		 ,'text!./updatainfo.css'
		 ],
		function(u, v, _e, t, e2,_p, Window, Request, _t, notify, $,Datepick,html,css,p ) {
			
			var regMap = {
			    'password':/^[a-zA-Z0-9]{6,20}$/,
				'mobileNumber':/^(13|15|18|17)\d{9}$/i,
			    'emailAddress':/^[\w-\.]+@(?:[\w-]+\.)+[a-z]{2,6}$/i,
			    'account' : /^[a-zA-Z0-9_\-\.]{4,20}$/,
			    'nick' : /^[a-zA-Z0-9\u4E00-\u9FFF]{2,20}$/,
			    'licence' : /^[a-zA-Z0-9]{1,20}$/
			}
			
			var _seed_css = _e._$pushCSSText(css);
		  
			var pro;
			p._$$InfoGroupWin = NEJ.C();
			pro = p._$$InfoGroupWin._$extend(Window);
			
			pro.__reset = function(options) {
				var formData = options.item;
				if(formData.uid){
					options.title = '修改用户信息';
				}else{
					options.title = '新建买家';
				}
				var _seed_html=e2._$add(html);
				e2._$render(this.__body,_seed_html,{item:formData});
				this.__super(options);
				this.__initForm();
				this.__addEvent(formData.birth);
			}
			pro.__initForm = function(){
				this.__Form = _t._$$WebForm._$allocate({
					form:'infoForm',
					message:{
						password100:"6-20位字母或数字",
						password101:"请输入新密码",
						mobileNumber100 : "手机号码格式不正确",
						account100 : "4-20位字符,可使用字母数字下划线或点号",
						emailAddress100 : "邮箱格式不正确",
						nick100 : "2-20位中文、字母或数字",
						licence100 : "最多20位字母或数字"
					},
					oncheck:function(_event){
						var evalue = _event.target.value;
						var ename = _event.target.name;
						if (ename =='password'){
							if(evalue=="******"){
								this._$showMsgPass(_event.target,'ok');
							}
							else if(!regMap['password'].test(evalue)){
								_event.value = 100;
							}
						 }else{
							if(evalue != "" && !_event.target.disabled && !regMap[ename].test(evalue)){
								_event.value = 100;
							}else{
								this._$showMsgPass(_event.target,"ok");
							}
						 }
					}
				});
			};

			pro.__addEvent = function(_value){
				var that=this;
				
				var initYear = new Date().getFullYear(),
					yearTpl = "",
					monthTpl = "",
					dayTpl = "";
				for(var i = 1; i <= 70;i++){
					yearTpl += "<option>"+(initYear - i + 1) +"</option>";
					if(i <= 31){
						dayTpl += "<option>"+ (i<10 ? "0"+i : i) +"</option>";
					}
					if(i <= 12){
						monthTpl += "<option>"+ (i<10 ? "0"+i : i) +"</option>";
					}
				}
				$("#year")._$html(yearTpl);
				$("#month")._$html(monthTpl);
				$("#day")._$html(dayTpl);
				if(_value && _value != "" && _value != "0-00-00"){
					var dateList = (_value+"").split("-");
					$("#year")._$val(dateList[0]);
					$("#month")._$val(dateList[1]);
					$("#day")._$val(dateList[2]);
				}
				
				//this.__Time._$setDate(_e._$dataset(_e._$get('datepick-box'),'init'));
				$('#infosubmit')._$on("click",function(e){
					if(that.__checkValid()){
						that._$dispatchEvent("onok",that.data);
					}
				});
				
			}
			pro.__checkValid = function(){
				if(this.__Form._$checkValidity()){
					var data = this.__Form._$data();
			        var __year  = $("#year")._$val(),
			        __month = $("#month")._$val(),
			        __day  = $("#day")._$val();
					data['birth']=__year+"-"+__month+"-"+__day;
					this.data = data;
					return true;
				}else{
					return false;
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