/**
 * Created by wuyuedong on 2014/10/10.
 */

NEJ.define([
  '{lib}base/util.js',
  '{pro}extend/util.js',
  '{pro}widget/BaseComponent.js',
  '{pro}page/buy/layer/coupon.add.js',
  'text!./coupon.html'
],function(u, _, BaseComponent, CouponAddWin, tpl, _p, _o, _f, _r) {
  var Coupon = BaseComponent.extend({
    template: tpl,
    name: 'wgt-coupon',
    config: function (data) {
      _.extend(data, {
        viewCount: 4
      });
    },
    init: function () {
      this.$on('updatewgt', this.refresh._$bind(this));
    },

    refresh: function(){
      var data = this.data, couponList, index;

      if(!data.orderdetail.couponList){
        data.orderdetail.couponList = [];
      }
      couponList = data.orderdetail.couponList;
      index = u._$indexOf(couponList ,function(_item,_index,_list){
        return _item.isSelected == true;
      });

      // 首次实例化组件，viewCount默认为4，不显示全部优惠券
      if(data.viewCount >4 ){
        data.viewCount = couponList.length;
      }

      if(index !== -1){
        data.orderdetail.currentCoupon = couponList[index];
        data.orderdetail.currentCouponId = couponList[index].userCouponId;
      }else{
        data.orderdetail.currentCoupon = null;
        data.orderdetail.currentCouponId = '';
      }
      this.$update();
    },

    //使用优惠券
    useCoupon: function(userCouponId){
      if(this.data.orderdetail.currentCouponId == userCouponId){
        this.cancelCoupon();
      }else{
        this.$emit('change', {'userCouponId': userCouponId});
      }
    },

    //取消当前优惠券
    cancelCoupon: function(){
      this.$emit('change', {'userCouponId': ''});
    },

    //绑定优惠券
    bindCoupon: function(){
      var data = this.data;
      CouponAddWin._$allocate({
        cartIds: data.cartIds,
        onok: function(userCouponId){
          data.viewCount = data.orderdetail.couponList.length + 1;
          this.useCoupon(userCouponId);
        }._$bind(this)
      })._$show();
    },

    show: function(){
      var data = this.data;
      data.viewCount = data.orderdetail.couponList.length;
    }

  });

  return Coupon;
})