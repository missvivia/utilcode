/**
 * 档期管理列表
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  "text!./returnlist.html",
  "{pro}components/ListComponent.js",
  "{pro}widget/layer/sell.return.js",
  '{pro}widget/layer/sure.window/sure.window.js',
  '{pro}components/notify/notify.js'
  ], function(tpl, ListComponent,ReturnWin,SureWin,notify,_p){
  var ActList = ListComponent.extend({
    url: "/oms/returnOrder/list",
    template: tpl,
    // @子类修改
    xdrOption:function(){
    	return {method:'POST'};
    },
    remove: function(index){
      this.data.list.splice(index,1)
      this.$emit('updatelist');
    },
    okreturn:function(_item){
    	SureWin._$allocate({text:'确认退货后，新云联百万店会将退货商品打包发货到您的仓库',title:'确认',onok:function(){
    		this.$request('/oms/returnOrder/confirm/'+_item.returnPoOrderId,{
    			type:'json',
    			onload:function(_json){
    				if(_json.code==200){
    					_item.state = 'CONFIRM';
    					notify.show('操作成功')
    				}
    			},onerror:function(){}
    		})
    	}._$bind(this)})._$show();
    },
    receive:function(_item){
    	SureWin._$allocate({text:'确认已收到退货商品？',title:'确认',onok:function(){
    		this.$request('/oms/returnOrder/ok/'+_item.returnPoOrderId,{
    			type:'json',
    			onload:function(_json){
    				if(_json.code==200){
    					_item.state = 'RECEIPTED';
    					notify.show('操作成功')
    				}
    			},onerror:function(){}
    		})
    	}._$bind(this)})._$show();
    },
    exportReturn: function(_item){
    	this.$request('/oms/returnOrder/export/'+_item.returnPoOrderId,{
			type:'json',
			onload:function(_json){
				
			},onerror:function(){}
		})
    }
  });
  
  ActList.filter('statusMap',function(_state){
	  var map ={'NEW':'未确认','CONFIRM':'已确认','SHIPPED':'对方已退货','RECEIPTED':'退货完成'};
	  return map[_state]||('未知状态'+_state);
  })
  return ActList;

})