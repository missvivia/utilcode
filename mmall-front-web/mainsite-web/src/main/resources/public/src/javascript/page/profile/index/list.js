/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
	"pro/extend/util",
    'base/util',
    'text!./list.html',
    'pro/components/ListComponent'
],function(_,_ut,_html,ListComponent,_p,_o,_f,_r){
    var List = ListComponent.extend({
	//        url:'/profile/focus/brand/list',
		    url:'/profile/summary',
	        api:'/profile/focus/unfollow',
	        template:_html,
	        config: function(data){
	            _.extend(data, {
	              total: 1,
	              current: 1,
	              limit: 10,
	              orderList: [],
	              couponList: [],
	              focusList: []
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
	        	this.$request('/product/unfollow',{
		  			  data:{"poId":item.skuId},
		  			  method:'POST',
		  			  onload:function(_result){
		  				  if(_result.code==200){
		  					  $.message.alert("已取消收藏！", "success");
		  					  this.data.focusList.splice(index,1);
		  				  }
		  			  },
		  			  onerror:_f
		  		  })
	        },
	     // update loading
	        __getList: function(){
	        	var data = this.data;
	          this.$request(this.url, {
	            data: this.getListParam(),
	            onload: function(json){
	              var result = json.result;
	              data.total = 0;
	              data.orderList = result.orderList||[];
	              data.couponList = result.couponList||[];
	              data.focusList = result.focusList||[];
	            },
	            // test
	            onerror: function(json){
	              // @TODO: remove
	            }
	          });
	        }
	    });
    List.filter('couponStatus',function(_status){
    	var map ={'0':'可用','8':'未生效','2':'已过期','3':'已使用','5':'已失效'};
    	return map[_status]||('未知状态'+_status);
    })
    return List;
});