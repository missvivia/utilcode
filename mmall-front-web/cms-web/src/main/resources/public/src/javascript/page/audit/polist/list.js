/*
 * ------------------------------------------
 * 档期商品审核列表
 * @version  1.0
 * @author   xwb(xiangwenbin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
		'base/util',
	    'text!./list.html',
	    '../widget/list.js',
	    'pro/components/notify/notify',
	    'pro/extend/config'
	    ],function(_u,  _html,AuditList,notify,config,_p,_o,_f,_r){
    var List =  AuditList.extend({
		        url:'/schedule/rest/productlist',
		        api:'/schedule/rest/',
		        template:_html,
		        data:{config:config,limit:50},
		        config:function(data){
		        	this.supr(data);
		        	data.key= 'skuId';
		        },
		        xdrOption:function(){
		        	return {method:'GET'};
		        },
		        audit:function(_item,_passed){
		        	this._doAudit({poList:[_item.poId],list:[_item.skuId]},_passed);
		        },
		        computed:{
		        },
		        statusData:function(_item){
		        	if(_item.isValid){
		        		if(_item.status==2){
		        			return "待审核";
			        	} else if(_item.status==3){
			        		return "审核通过";
			        	} else if(_item.status==4){
		        			return "审核不通过";
			        	}
		        	}else{
		        		return "失效";
		        	}
		        }
		    });
    List.filter("skuId", function(code){
        return statusMap[code]||"未知状态";
      });
    
    return List;
});