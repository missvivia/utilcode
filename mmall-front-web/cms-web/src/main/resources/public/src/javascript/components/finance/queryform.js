/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define(["{pro}extend/util.js?v=1.0.0.0",
  "text!./queryform.html?v=1.0.0.0",
  "{pro}widget/BaseComponent.js?v=1.0.0.0",
  '{lib}base/element.js?v=1.0.0.0',
  '{pro}extend/request.js?v=1.0.0.0',
  '{pro}components/notify/notify.js?v=1.0.0.0',
  '{lib}util/form/form.js?v=1.0.0.0',
  '{lib}base/util.js?v=1.0.0.0'
  ], function(_, tpl, BaseComponent, e, Request, notify, _t, _u){

  
  var queryForm = BaseComponent.extend({
    url: "/order/topay/getlist",
    name: "m-queryform",
    template: tpl,
    config: function(data){
      _.extend(data, {
        companyList:window.__companyList__,
        storeList:window.__storeList__,
        orderPermitted:window.__orderPermitted__,
        freightPermitted:window.__freightPermitted__,
        payPermitted:window.__payPermitted__
      });
    },
    search: function(tag,event){
      var startTime = this.data['startTime'+tag],
          endTime = this.data['endTime'+tag]+24*3600*1000-1,
          urlMap = {
            0:'/finance/order/list?',
            1:'/finance/trade/list?',
            2:'/finance/queryOrderRefund?',
            3:'/oms/freight/exportReceive?',
            4:'/oms/freight/exportPay?'
          },param;
      switch(tag){
        case 0: 
          param = 'startDate=' + _u._$format(startTime, "yyyy-MM-dd") 
                  + '&endDate=' + _u._$format(endTime, "yyyy-MM-dd");
          break;
        case 1:
          param = 'startDate=' + _u._$format(startTime, "yyyy-MM-dd") 
                  + '&endDate=' + _u._$format(endTime, "yyyy-MM-dd");
          break;
        case 3:
          param = 'startDate=' + startTime + '&endDate=' + endTime + '&warehouseId=' 
                  + this.data['store1'] + '&expressCompany=' + this.data['express1'];
          break;
        case 4:
          param = 'startDate=' + startTime + '&endDate=' + endTime + '&warehouseId=' 
                  + this.data['store2'] + '&expressCompany=' + this.data['express2'];
          break;
        default:
          param = 'startDate=' + startTime + '&endDate=' + endTime;
      }
      if(startTime > endTime){
        notify.show({
          type:'error',
          message:'请输入正确的时间范围！'
        });
        event.preventDefault();
        // return false;
      }else{
        event.target.href = urlMap[tag] + param;
      }
    },
    searchPay: function(isFull){
      var poNum = this.data.poNum || '';
      if(this.isOK){
        if(!isFull){
          location.href = '/finance/firstpay?poId='+poNum;
        }else{
          location.href = '/finance/fullpay?poId='+poNum;
        }
      }else{
        notify.show({
          type:'error',
          message:'请输入正确格式的poId！'
        })
      }
    },
    checkId: function(event){
      var poId = event.target.value,
          poNum = this.data.poNum || '';
      if(/^[1-9][0-9]*$/.test(poId)){
        e._$delClassName(event.target,'j-invalid');
        this.isOK = true;
      }else{
        e._$addClassName(event.target,'j-invalid');
        this.isOK = false;
      }
    },
    onInput: function(event){
      e._$delClassName(event.target,'j-invalid');
    }
  }).use("$form");
  return queryForm;

})


