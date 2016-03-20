/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
	'base/event',
    'base/element',
    'base/util',
    'text!./list.html',
    'pro/components/ListComponent',
    'pro/components/notify/notify',
    '{pro}components/modal/modal.js',
    '{pro}extend/request.js'
],function(v,e,_ut,_html,ListComponent,notify,Modal,Request,_p,_o,_f,_r){
    return ListComponent.extend({
        url:'/business/busiuser/list',
        template:_html,
        data: {
        	total: 1,
            current: 1,
            limit: 10
        },
        getExtraParam:function(){
            return this.data.condition;
        },
        shouldUpdateList: function(data){
            return true;
        },
      
        init:function(){
        	this.$on("updatelist", this.__getList.bind(this));
        },
        deleteUser: function(_userId){
			Request('/business/deleteBusiUserRelation/'+_userId,{
                onload:function(_result){
                	if(_result.code==200){
                		notify.show('删除指定用户成功');
                		this.data.current = 1;
                        this.$emit('updatelist');
                	}
                }._$bind(this),
                onerror:function(_error){
                	notify.show('删除指定用户失败');
                }
            });
		},
        refresh:function(_data){
        	this.data.current = 1;
            this.$emit('updatelist');
        },
        getList:function(filter){
            return this.data.list.filter(function(_item){
                return !!_item[filter];
            });
        }
    });
});