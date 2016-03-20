NEJ.define([
    'pro/extend/util',
    'base/element',
    'base/util',
    'lib/util/ajax/rest',
    'pro/page/coupon/widget/select/select',
    'pro/components/notify/notify',
    'text!./couponList.html',
    'pro/components/ListComponent'
],function(_,_e,_u,_j,Select,_notify,_html,ListComponent){
    var format = function(_time){
        if (!_time) return '';
        _time = parseInt(_time);
        return _u._$format(new Date(_time),'yyyy.MM.dd');
    };
    return ListComponent.extend({
    	config: function(data){
	      _.extend(data, {
            current:1,
	        limit: 100,
			state: -1,
            list:[],
            hasCoupon:false
	      });
	      this.$watch(this.watchedAttr, function(){
	        if(this.shouldUpdateList()) {this.__getList();}
	      })
	    },
        watchedAttr: ['state'],
        getExtraParam: function(data){
          return {state: data.state};
        },
        url:'/mycoupon/data/couponList.json',
        template:_html,
        /**
         * 获取优惠券列表
         * @private
         */
        __getList: function(){
            this.$request(this.url, {
                data: this.getListParam(),
                onload: function(json){
                    var list =  json.result.list||[];
					this.$update("list",list);
                    if(this.data.state==-1)
                        this.$update("hasCoupon",!!list.length);
                        //this.$emit("couponListChanged",list.length);
                    if(!this.__select){
                        this.__select = new Select();
                        this.__select.$inject('#couponSelect');
                        this.__select.$on('selectChanged',this.__selectChanged._$bind(this));
                    }
                },
                onerror: function(e){
                    _notify.notify({
                        type: "error",
                        message:"获取优惠券失败,请稍后再试"
                    });
                }
            });
        },
        /**
         * 选择优惠券类型事件
         * @param _state
         * @private
         */
        __selectChanged:function(_state){
            this.$update("state",_state);
        },
        /**
         * 优惠券点击事件
         * @param _orderId
         * @private
         */
        __tap:function(_orderId){
            this.$emit('tap',this.data.list[_orderId]);
        }
    }).filter('format',format);
});