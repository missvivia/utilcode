/**
 * xx平台首页
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/order/packlist.js',
    '{pro}components/order/pdlist.js',
    '{pro}components/order/invoicelist.js',
    '{pro}components/order/win/cancel.order.win.js',
    '{pro}components/order/win/topay.pass.win.js',
    '{pro}components/order/win/delivery.address.win.js',
    '{lib}util/form/form.js',
    '{lib}util/template/jst.js',
    '{pro}components/notify/notify.js',
    '{pro}extend/request.js',
    'util/chain/NodeList',
    './orderwin/orderwin.js?v=1.0.0.0',
    '{pro}components/datepicker/datepicker.js',
    'util/ajax/xdr',
    '{pro}components/modal/modal.js',
    '{pro}components/progress/progress.js'
    ],
    function(ut,v,e,Module,PackList,PdList,InvoiceList,CancelOrderWin,CancelOrderWin2,AddressWin,_t,e2,notify,Request,$,OrderWin,Datepick,xdr,Modal,progress,p) {
        var pro;

        p._$$OrdDetailModule = NEJ.C();
        pro = p._$$OrdDetailModule._$extend(Module);
        var URL = {
        		
        };
        pro.__init = function(_options) {
            _options.tpl = 'jstTemplate';
            this.__supInit(_options);
            this.__getNodes();
            this.__addEvent();
        };

        pro.__getNodes = function(){
            var _list = e._$getByClassName(document,'j-flag');
            this.__cancelOrder = e._$get('cancelOrder');
            this.__openReturn = e._$get('openReturn');
            this.__editAddress = e._$get('addressEdit');
        };
        pro.__addEvent = function(){
            v._$addEvent(this.__editAddress,'click',this.__onEditAddrClick._$bind(this));
            v._$addEvent(e._$get('edit'),'click',this.__onEditOrder._$bind(this));
            v._$addEvent(e._$get('addLogistics'),'click',this.__onEditLogsticsClick._$bind(this));
            $("#search")._$on("click",function(){
            	var orderId = $("#key")._$val();
            	var reg = /^\d+$/;
            	if(reg.test(orderId)){
            		location.href = "/order/getorderdetail?orderId=" + orderId;
            	}else{
            		notify.show({
                        type:'error',
                        message:'必须输入数字！'
                    })
            	}
            });
            $("#orderStatusList")._$children()._$on("click",function(){
            	var type = $(this)._$val();
            	//console.log(type);
            });
            $("#orderStatusList a")._$on("click",function(){
            	var val = $(this)._$attr("data");
            	var data = {
            		newState : val,
            		orderCount : 1
            	};
            	if(window.__basicInfo__.status.intValue == 0){
	    	        progress.start(); 
            		Request('/order/getSubOrderIds?parentId='+window.__basicInfo__.parentId,{
    	                  onerror:function(json){
                              progress.end();
    	                	  if(json.baseJsonVO.result.length > 1){
                          		data.orderCount = json.baseJsonVO.result.length;
    	                		var resultArr = [];
                          		for(var i = 0; i < json.baseJsonVO.result.length;i++){
                          			resultArr.push(json.baseJsonVO.result[i]);
                          		}
                          		var _html = "<p>该订单已与其他订单合并付款，此操作将同时更改其他订单状态！</p><p>关联订单：" + resultArr.join(", ") +"</p>";
                              	var modal = new Modal({
  	                	          	data:{
  	                	            	title: "确认提示",
  	                	          		content : _html
  	                	            }
  	                	    	});
                              	modal.$on('confirm',function(){
                              		pro.__showOrderWin(pro.__stateWin,"操作备注",'modifyState',data);
                              	});
                              }else{
                              	pro.__showOrderWin(pro.__stateWin,"操作备注",'modifyState',data);	
                              }
    	                  }
    	              });
            	}else{
            		pro.__showOrderWin(pro.__stateWin,"操作备注",'modifyState',data);
            	}
            	
            });
            var currentStatus = $("#chageOrderType")._$attr("value");

            $(".cancel")._$on("click",function(){
            	var pId = "#" + $(this)._$parent(".panel")._$attr("id");
            	$(pId + " .editing")._$style("display","none");
            	$(pId + " .detail")._$style("display","block");
            	pro.__setAddress(pro.__getAddress());
            });
            $("#basicInfo .save")._$on("click",function(){
            	pro.saveBasicInfo();
            });
            $("#deliveryInfo .save")._$on("click",function(){
            	pro.__addressSave();
            });
            $("#invoice .edit")._$on("click",function(){
            	var index = parseInt($(this)._$attr("index"),10);
            	var item = window.__invoice__[index];
            	var data = {
            		id : item.id,
            		title : item.title,
            		invoiceNo : item.invoiceNo,
            		state : item.state
            	};
            	window.__invoiceType__ = "modify";
            	pro.__showOrderWin(pro.__invoiceWin,"修改发票",'modifyInvoice',data);
            });
            $("#addInvoice")._$on("click",function(){
            	window.__invoiceType__ = "add";
            	var data = {
            		title : "",
            		invoiceNo : ""
            	};
            	pro.__showOrderWin(pro.__invoiceWin,"新增发票",'modifyInvoice',data);
            });
            $("#editPrice")._$on("click",function(){
            	var data = {};
            	pro.__showOrderWin(pro.__PriceWin,"修改总价",'modifyPrice',data);
            });
            $("#viewLog")._$on("click",function(){
            	pro.__showOrderWin(pro.__logWin,"查看操作日志",'operateLog',{});
            	window.startTime = new Datepick().$inject('#startTime');
            	window.endTime = new Datepick().$inject('#endTime');
            });
            $(".logisticsEdit")._$on("click",function(){
            	var index = $(this)._$attr("index");
            	var dt = window.__logistics__[index];
            	var data = {
        			orderId : window.__basicInfo__.orderId,
        			businessId : window.__businessId__,
        			mailNO : dt.mailNO,  
        			expressCompany : window.__expressCompany__,
        			expressCode : dt.expressCompany.code,
        			comment : '',
        			expressId : dt.id
            	};
            	pro.__showOrderWin(pro.__logisticsWin,"编辑物流信息",'modifyLogistics',data);
        		this.sendTime = new Datepick().$inject('#sendTime');
            });
            $("#print")._$on("click",function(){
            	window.print();
            });
            $("#commentEdit")._$on("click",function(){
            	
            	pro.__showOrderWin(pro.__CommentWin,"更新订单附言",'modifyComment',{comment:window.__data__.comment});
            });
            $("#viewCoupon")._$on({
            	"mouseover" : function(){
            		console.log(1);
            		$(".couponBox")._$style("display","block");
            	},
            	"mouseout" : function(){
            		$(".couponBox")._$style("display","none");
            	}
            });
        };
        pro.__onEditAddrClick = function(_event){
            v._$stop(_event);
            var data = window.__address__;
            this.__showOrderWin(this.__addressWin,"修改收货信息","modifyInfo",data);
        };
        pro.__onEditLogsticsClick = function(_event){
        	v._$stop(_event);
        	var data = {
    			orderId : window.__basicInfo__.orderId,
    			businessId : window.__businessId__,
    			mailNO : '',  
    			expressCompany : window.__expressCompany__,
    			comment : ''	
        	};
        	this.__showOrderWin(this.__logisticsWin,"新增物流信息",'modifyLogistics',data);
    		this.sendTime = new Datepick().$inject('#sendTime');
        };
        pro.__getAddress = function(){
            var address = {};
            if(!!this.__addr){
                address = this.__addr;
            }else{
            	for(var i in window.__address__){
            		address[i] = window.__address__[i];
                }
            }
            return address;
        }
        pro.__onCancelOK = function(rtype){
            var data = {},
                self = this;
            data['rtype'] = rtype || 0;
            data['userId'] = window.__basicInfo__.userId;
            data['orderId'] = window.__basicInfo__.orderId;
            Request('/order/query/orderdetail/delete',{
                data:data,
                onload:function(json){
                    if(json.code == 200){
                        notify.show('成功');
                        self.__cancelOrderWin._$hide();
                        window.location.reload();
                    }else{
                        notify.show({
                            type:'error',
                            message:'取消订单失败！'
                        })
                    }
                },
                onerror:function(){
                    notify.show({
                        'type':'error',
                        'message':'失败'
                    });
                }
            })
        };
        pro.__addressSave = function(){
            var _data = {},
                self = this;
            _data['userId'] = parseInt(window.__basicInfo__.userId);
            _data['orderId'] = parseInt(window.__basicInfo__.orderId);
            _data['chgParam'] = {};
            this.__deliveryForm = _t._$$WebForm._$allocate({form:'deliveryInfoForm'});
            if(this.__deliveryForm._$checkValidity()){
	            $("#deliveryInfo input")._$forEach(function(_node,_index){
	            	var val = $(_node)._$val();
	            	var key = $(_node)._$attr("name");
	            	_data['chgParam'][key] = val;
	            });
            	Request('/order/query/orderdetail/setAddress',{
	                data:_data,
	                method:'POST',
	                type:'JSON',
	                onload: function(json){
	                    if(json.code == 200){
	                        notify.show('修改地址成功');
	                        self.__addr = _data;
	                        self.__setAddress(_data['chgParam']);
//	                        self.__addressWin._$hide();
	                        $("#deliveryInfo .editing")._$style("display","none");
	                        $("#deliveryInfo .detail")._$style("display","block");
	                    }else{
	                    	notify.show({
		                        'type':'error',
		                        'message':'修改地址失败'
		                    });
	                    }
	                },
	                onerror: function(){
	                    notify.show({
	                        'type':'error',
	                        'message':'修改地址失败'
	                    });
	                }
	            });
            }
        };
        pro.__setAddress = function(addr){
        	var _list = e._$getByClassName(document,'j-addr');
            _list[0].innerText = addr.consigneeName;
            _list[1].innerText = addr.consigneeMobile;
            _list[2].innerText = addr.consigneeTel;
            _list[2].innerText = addr.province + addr.city + addr.section + addr.street + addr.address;
        };
        //后台不支持传省市区街道的id,addr参数去掉id
        pro.__changeData = function(addr){
            return {
                address: addr.address,
                city: addr.city,
                consigneeMobile: addr.consigneeMobile,
                consigneeName: addr.consigneeName,
                consigneeTel: addr.consigneeTel,
                province: addr.province,
                section: addr.section,
                street: addr.street
            };
        }
        pro.__onEditOrder = function(){
        	this.__cancelOrderWin = CancelOrderWin2._$allocate({
        		title:'取消订单',
        		parent:document.body,
        		onok:this.__onCancelOK._$bind(this)
        	})._$show();
        };
        pro.__showOrderWin=function(obj,title,tmp,data){
            if(!!obj){
            	obj._$recycle();
            }
            obj =OrderWin._$allocate({
            	parent:document.body,
            	title:title,
            	tmp:tmp,
            	data:data
            })._$show();
          };
        p._$$OrdDetailModule._$allocate();
    });