NEJ.define([
    'pro/extend/util',
    'base/element',
    'base/util',
	'lib/util/ajax/rest',
	'pro/page/coupon/widget/select/select',
	'pro/components/notify/notify',
    'text!./redpacketList.html',
    'pro/components/ListComponent',
    'pro/components/pager/ucenterpager'
],function(_,_e,_u,_j,Select,_notify,_html,ListComponent,ucenterpager){
    var format = function(_time){
        if (!_time) return '';
        _time = parseInt(_time);
        return _u._$format(new Date(_time),'yyyy.MM.dd');
    };
    return ListComponent.extend({
    	config: function(data){
  	      _.extend(data, {
  	        current: 1,
  	        limit: 100,
			state: -1,
			list: [],
			hasRedpacket:false
  	      });
  	      this.$watch(this.watchedAttr, function(){
  	        if(this.shouldUpdateList()) this.__getList();
  	      })
  	    },
        watchedAttr: ['state'],
        getExtraParam: function(data){
          return {state: data.state};
        },
        url:'/mycoupon/data/redpacketList.json',
        template:_html,
        __getList: function(){
			var data = this.data;
			this.$request(this.url, {
				data: this.getListParam(),
				onload: function(json){
					var list = json.result.list||[];
					this.$update("list",list);
					if(this.data.state==-1)
						this.$update("hasRedpacket",!!list.length);
					if(!this.__select){
						this.__select = new Select();
						this.__select.$inject('#redpacketSelect');
						this.__select.$on('selectChanged',this.__selectChanged._$bind(this));
					}
				},
				onerror: function(e){
					_notify.notify({
						type: "error",
						message:"获取红包失败,请稍后再试"
					});
				}
			});
	      },
		/**
		 * 选择红包类型事件
		 * @param _state
		 * @private
		 */
		__selectChanged:function(_state){
			this.$update("state",_state);
		},
		/**
		 * 红包点击事件
		 * @param _orderId
		 * @private
		 */
		__tap:function(_orderId){
			var item=this.data.list[_orderId];
			//删除cash为0的红包详情记录，目的是删除返红包时的初始记录
			if(item.dtos && item.dtos.length){
				for(var i=0;i<item.dtos.length;i++){
					if(item.dtos[i].cash<=0){
						item.dtos.splice(i,1);
					}
				}
			}
			this.$emit('tap',this.data.list[_orderId]);
		}
    }).filter('format',format);
});