/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'text!./brand.html',
    'pro/components/pager/ucenterpager',
    'pro/components/ListComponent',
    'pro/extend/util'
],function(_ut,_html,ucenterpager,ListComponent,_,_p,_o,_f,_r){
    return ListComponent.extend({
//        url:'/brand/brandLike',
    	// test /src/javascript/page/profile/brand/mock/list.json
	    url:'/attention/product/collectList?asc=false&orderColumn=createDate',
        api:'/brand/unfollow',
        template:_html,
        config: function(data){
            _.extend(data, {
                total: 1,
                current: 1,
                limit: 6,
                list: []
            });
            this.$watch(this.watchedAttr, function(){
                if(this.shouldUpdateList()) this.__getList();
            })
        },
        statusMap:function(_status){
        	var map ={"0":"敬请期待！","1":"正在抢购中"}
        	return map[_status];
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
        	///src/javascript/page/profile/brand/mock/unfollow.json
        	this.$request('/attention/product/unfollow',{
	  			  data:{"poId":item.skuId},
	  			  method:'POST',
	  			  onload:function(_result){
	  				  if(_result.code==200){
	  					  //$.message.alert("已取消收藏！", "success");
	  					  this.data.list.splice(index,1);
	  					  window.location.reload();
	  				  }
	  			  },
	  			  onerror:_f
	  		  })
        }
    });
});