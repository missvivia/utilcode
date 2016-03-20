/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./tradelist.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  '{pro}components/order/win/cancel.order.win.js?v=1.0.0.0'
  ], function(tpl, ListComponent, CancelOrderWin){
  var TradeList = ListComponent.extend({
    url: "/src/javascript/components/order/tradelist.json",
    name: "m-tradelist",
    template: tpl,
    // @子类修改
    getExtraParam: function(data){
      return {status: data.status}
    },
    cancelOrder: function(_id,_event){
      _event.preventDefault();
      if(!!this.cancelOrderWin){
          this.cancelOrderWin._$recycle();
      }
      this.cancelOrderWin = CancelOrderWin._$allocate({parent:document.body,onok:this.onCancelOK._$bind(this,_id)})._$show();
      this.$emit('updatelist');
    },
    onCancelOK: function(_id){
      var _form = document.forms[0],
          _list = _form.return,
          _data = {};
      for(var i=0,len=_list.length;i<len;i++){
        if(_list[i].checked){
          _data['return'] = i+1;
        }
      }
      _data['id'] = _id;
      console.log(_data);
    }
  });
  return TradeList;

})