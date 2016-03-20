/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./packlist.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  '{pro}extend/request.js?v=1.0.0.0',
  '{pro}components/notify/notify.js?v=1.0.0.0',
  '{pro}components/order/win/refuse.pack.win.js?v=1.0.0.0',
  '{pro}components/order/win/lose.pack.win.js?v=1.0.0.0',
  '{pro}components/order/win/signed.pack.win.js?v=1.0.0.0'
  ], function(tpl, ListComponent, Request, notify, RefuseWin, LoseWin, SignedWin){
  var PdList = ListComponent.extend({
    url: "/order/query/packlist",
    name: "m-packlist",
    template: tpl,
    data: {
      statusMap:{
        1: "待发货",
        2: "配送中",
        3: "已签收",
        4: "拒签"
      }
    },
    // @子类修改
    getExtraParam: function(data){
      return {
        type: window.__type__,
        key:  window.__key__
      }
    },
    options: function(obj,tag){
      switch (tag){
        case 2:
          if(!!this.refuseWin){
            this.refuseWin._$recycle();
          }
          this.refuseWin = RefuseWin._$allocate({parent:document.body,onok:this.onPackOption._$bind(this,obj,tag)})._$show();
          break;
        case 3:
          if(!!this.lostWin){
            this.lostWin._$recycle();
          }
          this.lostWin = LoseWin._$allocate({parent:document.body,onok:this.onPackOption._$bind(this,obj,tag)})._$show();
          break;
        case 4:
          if(!!this.signedWin){
            this.signedWin._$recycle();
          }
          this.signedWin = SignedWin._$allocate({parent:document.body,onok:this.onPackOption._$bind(this,obj,tag)})._$show();
          break;
        default:
          this.onPackOption(obj,tag);
      }
    },
    onPackOption: function(obj,tag){
      var urlMap = {
        0: '/order/query/orderdetail/cancelpkg',
        1: '/order/query/orderdetail/reopenreturn',
        2: '/order/query/orderdetail/setpkgrefused',
        3: '/order/query/orderdetail/setpkglost',
        4: '/order/query/orderdetail/consignpkg'
      },data = {
        ordPkgId: obj.packNum,
        userId: window.__basicInfo__.userId
      },self = this;
      Request(urlMap[tag],{
        data:data,
        onload:function(json){
          if(json.code == 200){
            notify.show({
              type:'success',
              message:'操作成功'
            })
            self.$emit('updatelist');
          }
        },
        onerror:function(){
          notify.show({
            type:'error',
            message:'操作失败'
          })
        }
      })
    }
  });
  return PdList;

})