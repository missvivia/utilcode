define(
		['{lib}base/util.js'
		 ,'{lib}base/event.js'
		 ,'{lib}base/element.js'
		 ,'{lib}util/template/jst.js'
		 ,'{lib}util/template/tpl.js'
		 ,'{pro}widget/layer/window.js'
		 ,'{pro}extend/request.js'
		 ,'{lib}util/form/form.js'
		 ,'{pro}components/notify/notify.js'
		 ,'{lib}util/chain/NodeList.js'
		 ,'text!./orderwin.html?v=1.0.0.0',
		 '{pro}components/datepicker/datepicker.js'
		 ],
		function(u, v, e,e2,e1, Window, Request, _t, notify, $,html,DatePick,d,css,p ) {
			
			var _seed_ui = e1._$parseUITemplate(html);
			
			var regMap = {
			    'mobile':/^(13|15|18|17)\d{9}$/i,
			    'email':/^[\w-\.]+@(?:[\w-]+\.)+[a-z]{2,6}$/i,
			    'zipcode': /^\d{6}$/i
			}
			var vdecimal=function(value){
					var str=/^[0-9]+(.[0-9]{1,2})?$/;
					if(str.test(value) && value>0){
						return true;
					}
					else{
						return false;
					}
				}
				
		  
			var pro;
			p._$$InfoGroupWin = NEJ.C();
			pro = p._$$InfoGroupWin._$extend(Window);
			
			pro.__reset = function(options) {
				var data = options.data;
				e2._$render(this.__body,_seed_ui[options.tmp],{item:data});

				this.__super(options);
				this.__initForm(options.tmp);
				//this.__addEvent();
			}
			
			pro.__initForm = function(tmp){
				var that=this;
				switch (tmp){
					case "modifyInfo":
						this.__modifyInfo();
						break;
					case "modifyInvoice":
						this.__addInvoice();
						break;

					case "modifyPrice":
						this.__modifyPrice();
						break;
					case "modifyLogistics":
						this.__modifyLogistics();
						break;
					case "modifyState":
						this.__modifyState();
						break;
					case "operateLog":
						this.__operateLog();
						break;
					case "modifyComment":
						this.__modifyComment();
						break;
				}
			}
			pro.__modifyPrice=function(){
				var that=this;
				this.__priceForm = _t._$$WebForm._$allocate({
					form:'priceForm',
					message:{
						cash100:"必须为两位小数的正实数"
					},
					oncheck:function(_event){
						if (_event.target.name=='totalCash' && !vdecimal(_event.target.value)){
							_event.value=100;
						}
					}
				});
				
				var jbtn=e._$getByClassName(e._$get('priceForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('priceForm'),"jcancel");
				
				$(jbtn)._$on("click",function(){
					if($(this)._$attr("once")) return;
					if(that.__priceForm._$checkValidity()){
			    		var data =that.__priceForm._$data();
			    		data["orderId"] = window.__basicInfo__.orderId;
						data["userId"] = window.__basicInfo__.userId;
						Request('/order/orderdetail/updateCash',{
			    			data:data,
			        		method:'POST',
			                onload:function(json){
			                	if(json.code=="200"){
			                		notify.show({
			    						'type':'success',
			    						'message':json.message
			    					});
			                		that._$hide();
			                		window.location.reload();
			                	}else{
			                		notify.show({
			    						'type':'info',
			    						'message':json.message
			    					});
			                	}
			                	$(this)._$attr("once",false);
			                },
			                onerror:function(data){
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                	$(this)._$attr("once",false);
			                }
			            });
						$(this)._$attr("once",true);
					}
				});
				$(jcancel)._$on("click",function(){
					that._$hide();
				});
				
			}
			pro.__addInvoice=function(){
				var that=this;
				this.__invoiceForm = _t._$$WebForm._$allocate({
					form:'invoiceForm'
				});
				var jbtn=e._$getByClassName(e._$get('invoiceForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('invoiceForm'),"jcancel");
				
				$(jbtn)._$on("click",function(){
					if($(this)._$attr("once")) return;
					if(that.__invoiceForm._$checkValidity()){
			    		var data =that.__invoiceForm._$data();
			    		if(window.__invoiceType__ == "add"){
							var url = '/order/orderdetail/addInvoice';
							data.userId = window.__basicInfo__.userId;
							data.orderId = window.__basicInfo__.orderId;
							data.businessId = window.__businessId__;
							delete data["id"];
						}else if(window.__invoiceType__ == "modify"){
							var url = "/order/orderdetail/updateInvoice";
						}
			    		Request(url,{
			    			data:data,
			        		method:'post',
			                onload:function(json){
			                	if(json.code=="200"){
			                		notify.show({
			    						'type':'success',
			    						'message':json.message
			    					});
			                		that._$hide();
			                		window.location.reload();
			                	}else{
			                		notify.show({
			    						'type':'info',
			    						'message':json.message
			    					});
			                	}
			                	$(this)._$attr("once",false);
			                },
			                onerror:function(data){
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                	$(this)._$attr("once",false);
			                }
			            });
			    		$(this)._$attr("once",true);
					}
				});
				$(jcancel)._$on("click",function(){
					that._$hide();
				});
				
			}

			pro.__modifyInfo = function(){
				var that=this;
				that.__infoForm = _t._$$WebForm._$allocate({
					form:'infoForm',
					message:{
						consigneeMobile100:"手机格式不正确",
						zipcode10:"邮编应为6位数字"
					},
					oncheck:function(_event){
						if (_event.target.name=='consigneeMobile' && !regMap['mobile'].test(_event.target.value)){
							_event.value=100;
						}
						if(_event.target.name=='zipcode' && !regMap['zipcode'].test(_event.target.value)){
						    _event.value=10;
						}
					}
				});
				var jbtn=e._$getByClassName(e._$get('infoForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('infoForm'),"jcancel");
				$(jbtn)._$on("click",function(){
					if($(this)._$attr("once")) return;
					if(that.__infoForm._$checkValidity()){
			    		var data = that.__infoForm._$data();
						dt={
							orderId: data.orderId,
							userId: data.userId,
							chgParam:{
								address:data.address,
								consigneeMobile:data.consigneeMobile,
								consigneeName:data.consigneeName,
								zipcode:data.zipcode
							}
						}
						Request('/order/query/orderdetail/setAddress',{
			    			data:dt,
			        		method:'POST',
			                onload:function(json){
			                	if(json.code=="200"){
			                		notify.show({
			    						'type':'success',
			    						'message':"修改地址成功"
			    					});
			                		that._$hide();
			                		window.location.reload();
			                	}else{
			                		notify.show({
			    						'type':'info',
			    						'message':"修改地址失败"
			    					});
			                	}
			                	$(this)._$attr("once",false);
			                },
			                onerror:function(data){
			                	notify.show({
			    					'type':'info',
			    					'message':"修改地址失败"
			    				});
			                	$(this)._$attr("once",false);
			                }
			            });
						$(this)._$attr("once",true);
					}
				});
				$(jcancel)._$on("click",function(){
					that._$hide();
				});
			};
			pro.__modifyLogistics = function(){
				var that=this;
				
				this.__logisticsForm = _t._$$WebForm._$allocate({
					form:'logisticsForm'
				});
				var jbtn=e._$getByClassName(e._$get('logisticsForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('logisticsForm'),"jcancel");
				$(jbtn)._$on("click",function(){
					if($(this)._$attr("once")) return;
					if(that.__logisticsForm._$checkValidity()){
			    		var dt =that.__logisticsForm._$data();
			    		var data = {
		    				"orderId" : dt.orderId,
		    				"businessId" : dt.businessId,
		    				"mailNO" : dt.mailNO,
		    				"expressCompany" : dt.expressCompany,
		    				"comment" : dt.comment
			    		};
			    		if(dt.expressId != "") {
			    			data.id = dt.expressId;
			    		}
						Request('/order/addOrUpdateOrderLogistics',{
			    			data:data,
			        		method:'post',
			                onload:function(json){
			                	if(json.code=="200"){
			                		notify.show({
			    						'type':'success',
			    						'message':"添加物流信息成功"
			    					});
			                		that._$hide();
			                		window.location.reload();
			                	}else{
			                		notify.show({
			    						'type':'info',
			    						'message':json.message
			    					});
			                	}
			                	$(this)._$attr("once",false);
			                },
			                onerror:function(data){
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                	$(this)._$attr("once",false);
			                }
			            });
						$(this)._$attr("once",true);
					}
				});
				$(jcancel)._$on("click",function(){
					that._$hide();
				});
			};
			pro.__modifyState = function(){
				var that = this;
				this.__stateForm = _t._$$WebForm._$allocate({
					form:'stateForm'
				});
				var jbtn=e._$getByClassName(e._$get('stateForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('stateForm'),"jcancel");
				$(jbtn)._$on("click",function(){
					if($(this)._$attr("once")) return;
					if(that.__stateForm._$checkValidity()){
			    		var data =that.__stateForm._$data();
			    		var dt={
							"userId": window.__basicInfo__.userId,
							"newState":{
								"intValue" : parseInt(data.newState)
							},
							"comment" : data.comment
						};
			    		if(data.orderCount > 1){
			    			dt["parentId"] = window.__basicInfo__.parentId;
			    		}else{
			    			dt["orderId"] = window.__basicInfo__.orderId;
			    		}
//			    		console.log(dt);
						Request('/order/orderdetail/modifyOrderState',{
			    			data:dt,
			        		method:'post',
			                onload:function(json){
			                	if(json.code=="200"){
			                		notify.show({
			    						'type':'success',
			    						'message':"修改订单状态成功"
			    					});
			                		that._$hide();
			                		window.location.reload();
			                	}else{
			                		notify.show({
			    						'type':'info',
			    						'message':json.message
			    					});
			                	}
			                	$(this)._$attr("once",false);
			                },
			                onerror:function(data){
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                	$(this)._$attr("once",false);
			                }
			            });
						$(this)._$attr("once",true);
					}
				});
				$(jcancel)._$on("click",function(){
					that._$hide();
				});
			};
			pro.__operateLog = function(){
				
				var jbtn=e._$getByClassName(e._$get('operateLogForm'),"a");
				$("#operateLogForm a")._$on("click",function(){
					$("#operateLogForm a")._$attr("class","");
					$(this)._$attr("class","active");
					var range = $(this)._$attr("data");
					pro.__searchLog(range);
				});
				$("#searchLog")._$on("click",function(){
					$("#operateLogForm a")._$attr("class","");
					pro.__searchLog();
				});
			};
			pro.__searchLog = function(range){
				var data = {};
				data.cmsOrderId = window.__basicInfo__.orderId;
				if(range){
					data.range = parseInt(range);
					data.startTime = 0;
					data.endTime = 0;
				}else{
					data.range = 1;
					data.startTime = window.startTime.data.select;
					data.endTime = window.endTime.data.select + (23*3600 + 59*60 + 59)*1000;
				}
				$("#logContent")._$html("<div class='tip'>加载中...</div>");
				Request('/order/operateLog',{
	    			data:data,
	        		method:'get',
	                onload:function(json){
	                	if(json.code=="200"){
	                		//console.log(json);
	                		//var tpl = e2._$get('jst-template',json.result);
	                		if(json.result == null ||  json.result.length == 0){
	                			$("#logContent")._$html("<div class='tip'>无相关日志</div>");
	                			return;
	                		}    
	                		var tpl = "<table class='table'>";
	                		var list = {
	                			"1" : "订单状态",
	                			"2" : "订单金额",
	                			"3" : "收货地址",
	                			"4" : "发票",
	                			"5" : "物流",
	                			"6" : "备注",
	                			"7" : "支付状态"
	                		};
	                		for(var i = 0 ; i < json.result.length;i++){
	                			var item = json.result[i];
	                			tpl += "<tr>";
	                			tpl += "<td width='30%'>"+ item.showTime +"</td>";
	                			if(item.perOperateContent == ""){
	                				tpl += "<td>新建" + list[item.operateType];
	                			}else{
	                				tpl += "<td>"+ list[item.operateType] + "由"+item.perOperateContent + "更改为" + item.curOperateContent;
	                			}
	                			tpl += "("+ item.operator +")<br/>"+ item.operateNote +"</td>";
	                		}
	                		$("#logContent")._$html(tpl);
	                	}else{
	                		$("#logContent")._$html("<div class='tip'>查询失败</div>");
	                	}
	                },
	                onerror:function(data){
	                	$("#logContent")._$html("<div class='tip'>查询失败</div>");
	                }
	            });
			};
			pro.__modifyComment = function(){
				var that = this;
				this.__commentForm = _t._$$WebForm._$allocate({
					form:'commentForm'
				});
				var jbtn=e._$getByClassName(e._$get('commentForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('commentForm'),"jcancel");
				$(jbtn)._$on("click",function(){
					if($(this)._$attr("once")) return;
					if(that.__commentForm._$checkValidity()){
			    		var data =that.__commentForm._$data();
			    		var dt={
							"orderId": window.__basicInfo__.orderId,
							"userId": window.__basicInfo__.userId,
							"comment" : data.comment
						};
						Request('/order/orderdetail/addOrUpdateComment',{
			    			data:dt,
			        		method:'post',
			                onload:function(json){
			                	if(json.code=="200"){
			                		notify.show({
			    						'type':'success',
			    						'message':"添加订单附言成功"
			    					});
			                		that._$hide();
			                		window.location.reload();
			                	}else{
			                		notify.show({
			    						'type':'info',
			    						'message':json.message
			    					});
			                	}
			                	$(this)._$attr("once",false);
			                },
			                onerror:function(data){
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                	$(this)._$attr("once",false);
			                }
			            });
						$(this)._$attr("once",true);
					}
				});
				$(jcancel)._$on("click",function(){
					that._$hide();
				});
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