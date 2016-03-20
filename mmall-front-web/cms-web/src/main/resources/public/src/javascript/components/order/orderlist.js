/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./orderlist.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0"
  ], function(tpl, ListComponent){
  if(!!__key__){
    var url = '/order/query/getOrderList',
        param = {
          type: __type__,
          key: __key__
        };
  }else{
    var url = '/order/query/getOrderList2',
        param = {
          type: __type__,
          start: __startTime__,
          end: __endTime__
        };
  }
  var OrderList = ListComponent.extend({
    url: url,
    name: "m-orderlist",
    template: tpl,
    data:{
      statusMap:{
        '0':'等待付款',
        '1':'待发货',
        '2':'待发货',
        '5':'待发货',
        '6':'待发货',
        '9':'已发货',
        '10':'已发货',
        '15':'交易完成',
        '20':'取消中',
        '21':'已取消',
        '25':'审核未通过(货到付款)'
      }
    },
    // @子类修改
    getExtraParam: function(data){
      return param;
    }
  });
  return OrderList;

})