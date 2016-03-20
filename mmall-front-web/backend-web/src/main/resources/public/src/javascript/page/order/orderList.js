/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define([
    '{lib}base/event.js?v=1.0.0.0',
    '{lib}base/element.js?v=1.0.0.0',
    '{pro}extend/util.js?v=1.0.0.0',
    '{pro}widget/module.js?v=1.0.0.0',
    './list.js?v=1.0.0.1',
    '{pro}widget/form1.js?v=1.0.0.0',
    '{pro}components/notify/notify.js?v=1.0.0.0'
    ],
    function(v,e,_,Module,SizeList,ut0,notify,p) {
        var pro;

        p._$$SizeModule = NEJ.C();
        pro = p._$$SizeModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__addEvent();
            
            this.__form = ut0._$$WebForm._$allocate({form:e._$get('search-form'),
			            	onsubmit:function(data){
			            		if(data.searchType == "orderId" && data.searchKey != ""){
			             		   if(!(/^\d+$/.test(data.searchKey))){
			             			   notify.show("订单号只能为整数");
			             			   return;
			             		   }
			             	    }
				            	if(data.stime > data.etime){
				            		notify.show("结束时间必须晚于开始时间");
				            		return;
				            	}
				            	if(!document.getElementById("orderStatus")){
				            		data.orderStatus = 6;
				            	}
				            	data.etime = parseInt(data.etime) + (24*3600*1000 - 1);
			            		if(!this.__sizeList){
					                this.__sizeList = new SizeList({
					                    data: {condition:data}
					                }).$inject(".j-list");
					            } else{
					            	this.__sizeList.refresh(data);
					            }
			            	}._$bind(this)
			            })
        };
        
        pro.__addEvent = function(){
        	var searchKey = e._$get('searchKey');
        	v._$addEvent(searchKey,'keyup',this.__onKeyUp._$bind(this));
        };
        
        pro.__onKeyUp = function(_event){
        	if(_event.keyCode == 13){
        		var btn = e._$get('submitBtn');
        		btn.click();
        	}
        };
        
        p._$$SizeModule._$allocate();
    });