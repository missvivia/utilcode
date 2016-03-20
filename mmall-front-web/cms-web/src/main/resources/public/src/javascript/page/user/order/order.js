/*
 * ------------------------------------------
 * 订单列表
 * @version  1.0
 * @author   chenlei(chenlei@xyl.com)
 * ------------------------------------------
 */
NEJ.define([
	'{pro}extend/util.js',
    '{lib}base/util.js',
    '{pro}components/ListComponent.js',
],function(_,ut,ListComponent,_html0){

    var order = ListComponent.extend({
        url:'/userInfo/userOrder',
        template: "#orderListTpl",
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
    })

    return order;
});