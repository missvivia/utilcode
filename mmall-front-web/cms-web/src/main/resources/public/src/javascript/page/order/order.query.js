/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
        '{lib}base/event.js',
        '{lib}base/element.js',
        '{pro}widget/module.js',
        '{pro}components/datepicker/datepicker.js',
        '{pro}components/notify/notify.js',
        '{pro}components/ListComponent.js',
        'util/chain/NodeList',
        './list.js?v=1.0.0.2'],
    function(_,v,e,Module,Datepick,notify,ListComponent,$,list,p,o,f,r) {
        var pro;

        p._$$QueryModule = NEJ.C();
        pro = p._$$QueryModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__addEvent();
            this.__initDate();
            this.__searchList();
        };
        pro.__initDate = function(){
        	pro.__startTime = new Datepick({data:{select:""}}).$inject('#startTime');
            pro.__endTime = new Datepick({data:{select:""}}).$inject('#endTime');
       };
        pro.__addEvent = function(){
           $("#search")._$on("click",function(){
        	   var queryType = $("#queryType")._$val(),
        	       key = $("#searchKey")._$val();
        	   if(queryType == "5" && key != ""){
        		   if(!(/^\d+$/.test(key))){
        			   notify.show("订单号只能为整数");
        			   return;
        		   }
        	   }
        	   pro.__searchList(); 
           });
           
           $("#orderColumn-btn")._$on("click",function(){
        	   $("#orderColumn-menu")._$style("display","block");
           });
           
           $(".orderColumn-asc-li")._$on("click",function(){
        	   var ascEle = $(this)._$children(".orderColumn-asc-txt"),
        	       text = ascEle._$text(),
        	       val =  e._$dataset(ascEle[0],'value');
        	   $("#orderColumn-btn-txt")._$text(text);
        	   e._$dataset($("#orderColumn-btn-txt")[0],'asc',val);
           });
           
           $("#useCoup")._$on("click",function(){
        	   var checkStatus = $(this)[0].checked;
        	   e._$dataset($("#orderColumn-btn-txt")[0],'coupon',checkStatus);
           });
           
           $(".orderColumn-group")._$on("mouseleave",function(){
        	   $("#orderColumn-menu")._$style("display","none");
           });
           
        };
        pro.__searchList = function(){
        	var end,
        	    start = pro.__startTime.data.select,
	            queryType = $("#queryType")._$val(),
	            key = $("#searchKey")._$val(),
	            orderStatus = $("#orderStatus")._$val(),
        	    payStatus = $("#payStatus")._$val(),
	            orderColumn = e._$dataset($("#orderColumn-btn-txt")[0],'asc'),
	            useCoupon = e._$dataset($("#orderColumn-btn-txt")[0],'coupon'),
	            data = {};
        	
        	if(start != ""){
        		end = parseInt(pro.__endTime.data.select) + (24*3600*1000 - 1);
        	}else{
        		end = "";
        	}
        	
        	if(key==""){
        		data.queryType = "";
        	}else{
        		data.queryType = queryType;
        	}
            
        	if(queryType == 7){
        		data.businessAccount = key;
        	}else if(queryType == 6){
        		data.userName = key;
        	}else if(queryType == 5){
        		data.orderId = key;
        	}
        	
        	data.stime = start;
        	data.etime = end;
        	data.orderColumn = orderColumn;
        	data.useCoupon = useCoupon;
        	data.orderStatus = orderStatus;
        	data.payStatus = payStatus;
        	if(!pro.__orderList){
                pro.__orderList = new list({data: {condition:data}}).$inject('.j-list');
            }else{
            	pro.__orderList.refresh(data);
            }
        };

        p._$$QueryModule._$allocate();
    });