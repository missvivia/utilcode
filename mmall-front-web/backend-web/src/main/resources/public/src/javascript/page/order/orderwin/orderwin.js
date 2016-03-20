define(
		['{lib}base/util.js?v=1.0.0.0'
		 ,'{lib}base/event.js?v=1.0.0.0'
		 ,'{lib}base/element.js?v=1.0.0.0'
		 ,'{lib}util/template/jst.js?v=1.0.0.0'
		 ,'{lib}util/template/tpl.js?v=1.0.0.0'
		 ,'{pro}widget/layer/window.js?v=1.0.0.0'
		 ,'{pro}extend/request.js?v=1.0.0.0'
		 ,'{lib}util/form/form.js?v=1.0.0.0'
		 ,'{pro}components/notify/notify.js?v=1.0.0.0'
		 ,'{lib}util/chain/NodeList.js?v=1.0.0.0'
		 ,'text!./orderwin.html?v=1.0.0.0'],
		function(u, v, e,e2,e1, Window, Request, _t, notify, $,html,css,p ) {
			
			var _seed_ui = e1._$parseUITemplate(html);
			
			var regMap = {
			    'mobile':/^(13|15|18|17)\d{9}$/i,
			    'email':/^[\w-\.]+@(?:[\w-]+\.)+[a-z]{2,6}$/i
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
				case "sendGood":
					this.__sendGood();
					break;
				case "wl":
					this.__wlGood();
					break;
				case "closeOrder":
					this.__Form = _t._$$WebForm._$allocate({
						form:'sForm'
					});
					break;
				case "modifyInfo":
					this.__modifyInfo();
					break;
				case "modifyInvoice":
					this.__addInvoice();
					break;
				case "addBz":
					this.__addBz();
					break;
				case "modifyPrice":
					this.__modifyPrice();
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
						if (_event.target.name=='cash' && !vdecimal(_event.target.value)){
							_event.value=100;
						}
					}
				});
				var jbtn=e._$getByClassName(e._$get('priceForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('priceForm'),"jcancel");
				
				$(jbtn)._$on("click",function(){
					if(that.__priceForm._$checkValidity()){
			    		var data =that.__priceForm._$data();
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
			                },
			                onerror:function(data){
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                }
			            })
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
					if(that.__invoiceForm._$checkValidity()){
			    		var data =that.__invoiceForm._$data();
						Request('/order/orderdetail/addInvoice',{
			    			data:data,
			        		method:'GET',
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
			                },
			                onerror:function(data){
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                }
			            })
					}
				});
				$(jcancel)._$on("click",function(){
					that._$hide();
				});
				
			}
			pro.__addBz = function(){
				var that=this;
				this.__bzForm = _t._$$WebForm._$allocate({
					form:'bzForm'
				});
				var jbtn=e._$getByClassName(e._$get('bzForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('bzForm'),"jcancel");
				$(jbtn)._$on("click",function(){
					if(that.__bzForm._$checkValidity()){
			    		var data =that.__bzForm._$data();
						Request('/order/orderdetail/addOrUpdateComment',{
			    			data:data,
			        		method:'GET',
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
			                },
			                onerror:function(data){
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                }
			            })
					}
				});
				$(jcancel)._$on("click",function(){
					that._$hide();
				});
			}
			pro.__sendGood = function(){
				var that=this;
				this.__sendForm = _t._$$WebForm._$allocate({
					form:'sendForm'
				});
				var jbtn=e._$getByClassName(e._$get('sendForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('sendForm'),"jcancel");
				$(jbtn)._$on("click",function(){
					if(that.__sendForm._$checkValidity()){
			    		var data = that.__sendForm._$data();
						Request('/order/deliver',{
			    			data:data,
			        		method:'POST',
			                onload:function(json){
			                	if(json.code=="200"){
			                		notify.show({
			    						'type':'success',
			    						'message':"发货成功"
			    					});
			                		that._$hide();
			                		window.location.reload();
			                	}else{
			                		$(this)._$attr("disabled","");
			                		notify.show({
			    						'type':'info',
			    						'message':json.message
			    					});
			                	}
			                },
			                onerror:function(data){
			                	$(this)._$attr("disabled","");
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                }
			            })
					}
				});
				$(jcancel)._$on("click",function(){
					that._$hide();
				});
			}
			pro.__wlGood = function(){
				var that=this;
				this.__wlForm = _t._$$WebForm._$allocate({
					form:'wlForm'
				});
				var jbtn=e._$getByClassName(e._$get('wlForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('wlForm'),"jcancel");
				$(jbtn)._$on("click",function(){
					if(that.__wlForm._$checkValidity()){
			    		var data = that.__wlForm._$data();
						Request('/order/addOrUpdateOrderLogistics',{
			    			data:data,
			        		method:'POST',
			                onload:function(json){
			                	if(json.code=="200"){
			                		notify.show({
			    						'type':'success',
			    						'message':"新增物流成功"
			    					});
			                		that._$hide();
			                		window.location.reload();
			                	}else{
			                		$(this)._$attr("disabled","");
			                		notify.show({
			    						'type':'info',
			    						'message':json.message
			    					});
			                	}
			                },
			                onerror:function(data){
			                	$(this)._$attr("disabled","");
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                }
			            })
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
						consigneeMobile100:"手机格式不正确"
					},
					oncheck:function(_event){
						if (_event.target.name=='consigneeMobile' && !regMap['mobile'].test(_event.target.value)){
							_event.value=100;
						}
					}
				});
				var jbtn=e._$getByClassName(e._$get('infoForm'),"jbtn");
				var jcancel=e._$getByClassName(e._$get('infoForm'),"jcancel");
				$(jbtn)._$on("click",function(){
					if(that.__infoForm._$checkValidity()){
			    		var data = that.__infoForm._$data();
						dt={
							orderId: data.orderId,
							userId: data.userId,
							chgParam:{
								address:data.address,
								city:data.city,
								consigneeMobile:data.consigneeMobile,
								areaCode:data.areaCode,
								zipcode:data.zipcode,
								consigneeName:data.consigneeName,
								consigneeTel:data.consigneeTel,
								province:data.province,
								section:data.section,
								street:data.street
							}
						}
						Request('/order/orderdetail/changeAddress',{
			    			data:dt,
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
			                },
			                onerror:function(data){
			                	notify.show({
			    					'type':'info',
			    					'message':data.message
			    				});
			                }
			            })
					}
				});
				$(jcancel)._$on("click",function(){
					that._$hide();
				});
			}
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