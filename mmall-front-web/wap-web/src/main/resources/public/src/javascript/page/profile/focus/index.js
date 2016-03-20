/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'base/event',
    'text!./index.html',
    'pro/components/pager/ucenterpager',
    'pro/components/ListComponent',
    'pro/extend/util',
    'pro/extend/request',
    'pro/components/notify/notify',
    'util/chain/NodeList'
],function(_ut,_v,_html,ucenterpager,ListComponent,_,request,notify,$,_p,_o,_f,_r){
    return ListComponent.extend({
//        url:'/brand/brandLike',
    	// test /src/javascript/page/profile/brand/mock/list.json
	    url:'/attention/product/collectList',
        api:'/brand/unfollow',
        template:_html,
        config: function(data){
            _.extend(data, {
                total: 1,
                current: 1,
                scrollCurrent:1,
                limit: 9,
                list: [],
            });
            this.$watch(this.watchedAttr, function(){
                if(this.shouldUpdateList()) this.__getList();
            })
            _v._$addEvent(window,'scroll',this.__onScrollCheck._$bind(this))
        },
        statusMap:function(_status){
        	var map ={"0":"敬请期待！","1":"正在抢购中"}
        	return map[_status];
        },
        getListParam: function(){
            var data = this.data;
            return _.extend({
                limit: data.limit,
                offset: data.limit * (data.scrollCurrent-1)
              }, this.getExtraParam(data));
          },
        __onScrollCheck:function(event){
        	var remainHeight = document.body.scrollHeight - document.body.scrollTop- window.innerHeight;
        	if(remainHeight<=100&&!this.__loading&&this.data.scrollCurrent<Math.ceil(this.data.total/this.data.limit)){
        		 this.__loading = true;
        		 var data = this.data;
        		 this.$update('loading',true);
        		 this.data.scrollCurrent +=1;
        	     request(this.url, {
        	        data: this.getListParam(),
        	        onload: function(json){
        	          var result = json.result,
        	            list = result.list||result;
        	          //_.mergeList(list, data.list, data.key || "id");
        	          [].push.apply(data.list,list);
        	          data.total = result.total;
        	          //data.list = list;
        	          this.__loading = false;
        	          this.$update('loading',false);
        	          if(data.total > 0){
	        	          var currentPage = this.data.scrollCurrent;
	                      var totalPage = Math.ceil(parseFloat(data.total)/data.limit);
	                      $("#page span")._$text(currentPage + "/" + totalPage);
	                      $("#page")._$style("display","block");
	                      setTimeout(function(){
	                   	   	$("#page")._$style("display","none");
	                      },1000);
        	          }
        	        }._$bind(this),
        	        // test
        	        onerror: function(json){
        	          // @TODO: remove
        	        }
        	      })
        	}
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
        unFocus:function(e,item){
        	var _self = this;
        	this.$request('/attention/product/unfollow',{
  			  data:{poId:item.skuId},
  			  method:'POST',
  			  onload:function(_result){
  				_self.__getList();
	  	          notify.notify("取消关注成功");
  			  },
  			  onerror:_f
  		  });
        }
    });
});