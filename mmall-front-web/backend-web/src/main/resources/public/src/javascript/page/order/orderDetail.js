/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js?v=1.0.0.0',
    '{lib}base/event.js?v=1.0.0.0',
    '{lib}base/element.js?v=1.0.0.0',
    '{pro}widget/module.js?v=1.0.0.0',
    '{lib}util/chain/NodeList.js?v=1.0.0.0',
    '{lib}util/form/form.js?v=1.0.0.0',
    './orderwin/orderwin.js?v=1.0.0.0',
    '{pro}extend/request.js?v=1.0.0.0',
    '{pro}components/notify/notify.js?v=1.0.0.0',
    '{lib}util/template/jst.js?v=1.0.0.0'
    ],
    function(ut,v,e,Module,$,f,OrderWin,Request,notify,e2,p) {
        var pro;
        
      //倒计时
		var  getRTime=function(obj,t){
			var ctime=$(obj);
			var d,h,m,s,tcopy;
			if(t>=0){
				d=Math.floor(t/1000/60/60/24);
		        h=Math.floor(t/1000/60/60%24);
		        m=Math.floor(t/1000/60%60);
		        s=Math.floor(t/1000%60);
			}else{
				window.location.reload();
//				tcopy=-t;
//				d=-Math.floor(tcopy/1000/60/60/24);
//		        h=-Math.floor(tcopy/1000/60/60%24);
//		        m=-Math.floor(tcopy/1000/60%60);
//		        s=-Math.floor(tcopy/1000%60);
			}
	        
	    	ctime._$children(".digit-d")._$text(d);
	        ctime._$children(".digit-h")._$text(h);
	        ctime._$children(".digit-m")._$text(m);
	        ctime._$children(".digit-s")._$text(s);
	        return t;
	    }
	   var timer=function(t){
		   var ts=e._$get("timer");
		   setInterval(function(){ t=getRTime(ts,t)-1000;},1000);
		}
        
        
        p._$$detailModule = NEJ.C();
        pro = p._$$detailModule._$extend(Module);
        
        pro.__init = function(_options) {
        	this.__supInit(_options);
        	this.__Form = f._$$WebForm._$allocate({
				form:'orderform'
			});
        	this.data = this.__Form._$data();
        	this.__addEvent();
        	if(this.data.payCloseCD>0){
        		timer(this.data.payCloseCD);
        	}
        	this.__initOperationLog();
        }
        pro.__addEvent = function(){
        	var sendGood=$(".sendGood"),wl=$(".addWl"),closeOrder=$("#closeOrder"),modifyInfo=$("#modifyInfo"),modifyInvoice=$(".modifyInvoice"),addBz=$("#addBz"),modifyPrice=$("#modifyPrice");
        	var that=this;
        	sendGood._$on("click",function(){
        		var companylist=[];
        		for(var i=0;i<that.data.expressCompany.length;i++){
        			var obj=new Object();
        			var obj={
        				name:that.data.expressCompany[i],
        				value:that.data.expressValue[i]
        			}
        			companylist.push(obj);
        		}
        		var data={
    				userId:that.data.userId,
        			orderId:that.data.orderId,
        			cartRPrice:that.data.cartRPrice,
        			orderFormPayMethod:that.data.orderFormPayMethod,
        			expressCompany:companylist
        		}
        		console.log(data);
//        		that.__showOrderWin(that.sendGood,"发货","sendGood",data)
        		//e2._$render(this.wrap,_seed_ui[this.setting.Template], {item});
        		that.__submitSendGood(data);
        	});
        	wl._$on("click",function(){
        		var companylist=[];
        		for(var i=0;i<that.data.expressCompany.length;i++){
        			var obj=new Object();
        			var obj={
        				name:that.data.expressCompany[i],
        				value:that.data.expressValue[i]
        			}
        			companylist.push(obj);
        		}
        		var data={
    				userId:that.data.userId,
        			orderId:that.data.orderId,
        			expressCompany:companylist
        		}
        		that.__showOrderWin(that.sendGood,"新增物流","wl",data)
        		//e2._$render(this.wrap,_seed_ui[this.setting.Template], {item});
        	});
        	modifyInfo._$on("click",function(){
        		var data={
        			userId:that.data.userId,
        			orderId:that.data.orderId,
        			province:that.data.province,
        			city:that.data.city,
        			section:that.data.section,
        			street:that.data.street,
        			consigneeName:that.data.consigneeName,
        			consigneeMobile:that.data.consigneeMobile,
        			consigneeTel:that.data.consigneeTel,
        			areaCode:that.data.areaCode,
        			address:that.data.address,
        			zipcode:that.data.zipcode
        		}
        		that.__showOrderWin(that.modifyInfo,"修改收货信息","modifyInfo",data)
        	});
        	
        	modifyInvoice._$on("click",function(){
        		var data={
            			userId:that.data.userId,
            			orderId:that.data.orderId,
            			invoiceTitle:that.data.invoiceTitle
            		}
            		that.__showOrderWin(that.modifyInvoice,"开发票","modifyInvoice",data)
            	});
        	
        	addBz._$on("click",function(){
        		var comment=that.data.comment || "";
        		var data={
            			userId:that.data.userId,
            			orderId:that.data.orderId,
            			comment:comment
            		}
        		if(comment!=""){
        			that.__showOrderWin(that.addBz,"修改备注","addBz",data);
        		}else{
        			that.__showOrderWin(that.addBz,"新增备注","addBz",data);
        		}
        		
        	});
        	modifyPrice._$on("click",function(){
        		var data={
            			userId:that.data.userId,
            			orderId:that.data.orderId,
            			cash:that.data.realCash
            		}
        		that.__showOrderWin(that.modifyPrice,"修改金额","modifyPrice",data)
        	});
            $("#viewCoupon")._$on({
            	"mouseover" : function(){
            		$(".couponBox")._$style("display","block");
            	},
            	"mouseout" : function(){
            		$(".couponBox")._$style("display","none");
            	}
            });
        }
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
          }
        pro.__initOperationLog=function(){
        	Request('/order/orderdetail/queryOperateLog',{
    			data:{
    				orderId:this.data.orderId
    			},
        		method:'GET',
                onload:function(json){
                	if(json.code=="200"){
            			var tmp=e2._$add('operationlog');
                		e2._$render("operationwrap",tmp,{result:json.result});
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
        };
        pro.__submitSendGood = function(data){
			Request('/order/deliver',{
    			data:{
    				"userId" : data.userId,
    				"orderId" : data.orderId
    			},
        		method:'POST',
                onload:function(json){
                	if(json.code=="200"){
                		notify.show({
    						'type':'success',
    						'message':"发货成功"
    					});
                		window.location.reload();
                	}else{
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
            });        	
        };
        p._$$detailModule._$allocate();
    });