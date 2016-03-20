/**
 * 优惠券弹窗选择列表
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */


define([
  "text!./couponSelect.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  "{pro}components/modal/modal.js?v=1.0.0.0",
  '{pro}components/notify/notify.js?v=1.0.0.0',
  "{pro}extend/util.js?v=1.0.0.0"
  ], function(selectTpl ,ListComponent, Modal, notify,_){


  var CouponModal = Modal.extend({
    name: "coupon-modal",
    data: {
      title: "选择优惠券"
    },
    content: "<coupon-select selected={{selected}}></coupon-select>",
    init: function(){
      this.supr();
    }
  });

  // * 私有组件
  // * 优惠券弹窗选择列表

  var CouponSelect  = ListComponent.extend({
    url: "/coupon/listData.json",
    template: selectTpl ,
    config: function(data){
        _.extend(data, {
          total: 1,
          current: 1,
          limit: 5,
          list: []
        });

        this.$watch(this.watchedAttr, function(){
          if(this.shouldUpdateList()) this.__getList();
        })
      },
    getExtraParam: function(data){
      return { state: 2, qvalue:data.qvalue||'' }
    },
    select: function(coupon){
      var data = this.data;
      data.selected = coupon
    },
    __getList: function(){
        var data = this.data;
        this.$request(this.url, {
          progress: true,
          data: this.getListParam(),
          onload: function(json){
            data.total = json.total;
            data.list = json.list;
          },
          onerror: function(json){
          	notify.notify({
                  type: "error",
                  message: "网络异常，稍后再试！"
                });
          }
        })
      }
  })

  CouponModal.component("coupon-select", CouponSelect);

  return CouponModal;

})