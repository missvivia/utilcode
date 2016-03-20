/*
 * ------------------------------------------
 * 优惠券列表
 * @version  1.0
 * @author   liuqing
 * ------------------------------------------
 */
NEJ.define([
	'{pro}extend/util.js',
    '{lib}base/util.js',
    '{pro}components/ListComponent.js',
],function(_,ut,ListComponent){
	var strToArray = function(str){
        return eval(str);
    };
	
    var coupon = ListComponent.extend({
        url:'/userInfo/userCoupon',
        template: "#couponListTpl",
        data: {
	        couponStatus:{
	           "0":"可用",
	           "1":"不存在",
	           "2":"过期",
	           "3":"已被使用",
	           "4":"不匹配",
	           "5":"已失效",
	           "6":"使用次数为0",
	           "7":"已被激活",
	           "8":"未生效"
	        }
        },
        getExtraParam:function()
        {
            return this.data.condition;
        },
        refresh:function(_data){
            this.data.current = 1;
	    	if (!!_data.url){
	            this.url = _data.url;
	            delete _data.url;
	        }
	        this.data.condition = _data;
	        this.$emit('updatelist');
	    }
    }).filter('eval',strToArray);

    return coupon;
});