/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'text!./list.html',
    'pro/components/ListComponent',
    'pro/components/notify/notify',
    '{pro}widget/layer/brand/create.brand.js'
],function(_ut,_html,ListComponent,notify,createBrandWin,_p,_o,_f,_r){
    return ListComponent.extend({
        url:'/item/brand/list',
        api:'/item/brand/update',
        template:_html,
        updateBrand:function(brand,index){
        	var _url = this.api;
        	createBrandWin._$allocate({type:1,data:brand,onok:function(data){
//        		brand.logo = data.logo;
//        		brand.brandVisualImgWeb = data.brandVisualImgWeb;
//        		brand.brandVisualImgApp = data.brandVisualImgApp;
//        		brand.brandProbability = data.brandProbability;
        		this.data.list[index] = data;
        		this.$update();
        	}._$bind(this)})._$show();
        },
        data:{
            status:0
        },
        changeStatus: function(status){
            this.data.current = 1;
            this.data.status = status
        },
        getExtraParam:function(){
            return {index:this.data.status};
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
        createBrand:function(){
        	var _url = this.api;
        	createBrandWin._$allocate({onok:function(data){
        		this.$emit('updatelist');
        	}._$bind(this)})._$show();
        },
        remove:function(brand,index){
        	this.$request('/item/brand/remove',{
        		data:{id:brand.brandId},
        		method:'POST',
        		onload:function(json){
        			if(json.code==200){
        				notify.show('删除成功');
        				this.data.list.splice(index,1);
        			} else{
        				notify.show(json.message||'删除失败');
        			}
        		}._$bind(this),
        		onerror:function(json){
        			notify.show(json.message||'删除失败');
        		}
        	})
        }
    });
});