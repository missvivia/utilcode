NEJ.define([
    'pro/extend/util',
    'base/element',
    'base/util',
    'text!./redpacketList.html',
    'pro/components/ListComponent',
    'pro/components/pager/ucenterpager',
    'pro/widget/layer/redpacket/usedDetail'
],function(_,_e,_u,_html,ListComponent,ucenterpager,win){
    var format = function(_time){
        if (!_time) return '';
        _time = parseInt(_time);
        return _u._$format(new Date(_time),'yyyy-MM-dd');
    };
    
    return ListComponent.extend({
    	config: function(data){
  	      _.extend(data, {
  	        total: 1,
  	        current: 1,
  	        limit: 10,
  	        list: [],
  	        state: -1
  	      });
  	      this.$watch(this.watchedAttr, function(){
  	        if(this.shouldUpdateList()) this.__getList();
  	      })
  	    },
        watchedAttr: ['current','state'],
        getExtraParam: function(data){
          return {state: data.state};
        },
        onChange: function(e){
        	var _node = e.target;
        	this.data.current = 1;
        	this.data.state = _node.value;
        },
        url:'/mycoupon/data/redpacketList.json',
        template:_html,
        showDetailWin:function(item){
        	win._$$UsedDetailWindow._$allocate({data:item})._$show();
        },
        __getList: function(){
	        var data = this.data;
	        this.$request(this.url, {
	          data: this.getListParam(),
	          onload: function(json){
	            var result = json.result,
	              list = result.list||result;
	
	            data.total = result.total;
	            data.list = list;
	          },
	          // test
	          onerror: function(json){
	            // @TODO: remove
	          }
	        });
	      }
    }).filter('format',format);
});