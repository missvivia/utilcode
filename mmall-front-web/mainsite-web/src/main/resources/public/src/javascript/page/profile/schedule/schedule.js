/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'text!./schedule.html',
    'pro/components/ListComponent'
],function(_ut,_html,ListComponent,_p,_o,_f,_r){
    return ListComponent.extend({
    	url:'/schedule/favlist',
        api:'/schedule/unfollow',
        template:_html,
        init: function(){
            this.supr();
            this.$on("updatelist", this.__getList.bind(this));

        },
        getExtraParam:function(){
            return this.data.condition;
        },
        refresh:function(_data){
            if (!!_data.url){
                this.url = _data.url;
                delete _data.url;
            }
        	this.data.current = 1;
            this.data.condition = _data;
            this.$emit('updatelist');
        },
        unfollow:function(item,index){
        	this.$request(this.api,{
        		  data:{scheduleId:item.scheduleId},
        		  method:'POST',
	  			  onload:function(_result){
	  				this.data.list.splice(index,1);
	  			  }._$bind(this),
	  			  onerror:_f
	  		  })
        },
        xdrOption:function(){
        	return {method:get}
        }
    });
});