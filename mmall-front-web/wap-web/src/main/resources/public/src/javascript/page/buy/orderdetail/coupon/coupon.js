/**
 * Created by jinze on 2014/10/10.
 */

NEJ.define([
  '{lib}base/util.js',
  '{pro}extend/util.js',
  '{pro}widget/BaseComponent.js',
  'pro/components/notify/notify',
  'text!./coupon.html',
  'util/chain/NodeList'
],function(u, _, BaseComponent, Notify, tpl,$, _p, _o, _f, _r) {
  var Coupon = BaseComponent.extend({
    url: '/mycoupon/bindCoupon/',
    template: tpl,
    name: 'wgt-coupon',
    config: function (data) {
      _.extend(data, {
        'selectedcoupon': undefined,
        'showInputCode': false
      });
    },
    init: function () {
      this.$on('updatewgt', this.refresh._$bind(this));
    },

    refresh: function(){
      this.$update('hidden', true);
      this.data.hidden = false;
      this.data.showInputCode = false;
      var data = this.data,
        orderdetail = data.orderdetail,
        couponList = orderdetail.couponList,
        index;

      index = u._$indexOf(couponList ,function(_item,_index,_list){
        return _item.isSelected == true;
      });
      if(index !== -1){
        orderdetail.currentCoupon = couponList[index];
        orderdetail.currentCouponId = couponList[index].userCouponId;
      }else{
        orderdetail.currentCoupon = null;
        orderdetail.currentCouponId = '';
      }
      //刷新的时候hide 优惠券选择
      //data.selectedcoupon = "";

      this.$update();
    },


    //使用优惠券
    changeCoupon: function(e){
      var val = e.target.value;
      var index = e.target.selectedIndex;
      var minusVal = $($(".sel option")[index])._$attr("minus");
      if(val == '-'){//"不使用"的选项
        val = '';
        this.$emit('change', {'userCouponId': val});
      }
      if (val == '+'){//"输入优惠券"的选项
        this.data.showInputCode = true;
        val = '';
        this.$emit('change', {'userCouponId': val});
     	$(".iptwp input")._$val("");
      }else{
        this.data.showInputCode = false;
        this.$emit('change', {'userCouponId': val,'minusPrice':minusVal});
      }
    },

    //使用优惠券
    useCoupon: function(userCouponId){
      this.$emit('change', {'userCouponId': userCouponId});
    },

    //取消当前优惠券
    cancelCoupon: function(){
      this.$emit('change', {'userCouponId': ''});
    },
    
    // 字数校验
    checkSize: function(code){
    	if(!!code && code.length >= 20)Notify.notify("优惠码位数超出！", "error");
    },

    //绑定优惠券
    bindCoupon: function(code){
      var data = this.data;
      var map = ['','该优惠券不存在', '该优惠券已过期', '该优惠券已被使用', '该优惠券不满足使用条件', '该优惠券已失效', '该优惠券使用次数为0', '该优惠券已被激活绑定', '该优惠券还未生效'];
      if(code==null || _.trim(code)==''){
    	  Notify.notify({
              type: "error",
              message: "请输入优惠码"
          });        
    	  return false;
      }
      this.$request(this.url, {
        data: {
          'couponCode' : _.trim(code)
        },
        onload: function(json){
          if(json.code == 200){
        	  this.$emit('change', 'addCoupon');
          }else{
            Notify.notify({
              type: "error",
              message: "优惠券码不正确"
            });
          }
        }._$bind(this),
        onerror: function(json){
          Notify.notify({
            type: "error",
            message: "优惠券码不正确"
          });
        }._$bind(this)
      });
    }
  });

  Coupon.filter("default", function(_val, _def){
    if(_val &&  _.trim(_val) != ""){
      return _val;
    }else{
      return _def;
    }
  });

  return Coupon;
})