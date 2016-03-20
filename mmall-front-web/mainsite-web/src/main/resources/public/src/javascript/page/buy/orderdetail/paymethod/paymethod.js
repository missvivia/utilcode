/**
 * Created by wuyuedong on 2014/10/10.
 */

NEJ.define([
  '{pro}extend/util.js',
  '{lib}base/util.js',
  '{pro}widget/BaseComponent.js',
  'text!./paymethod.html'
],function(_, u, BaseComponent, tpl, _p, _o, _f, _r) {
  var PayMethod = BaseComponent.extend({
    template: tpl,
    name: 'wgt-paymethod',
    config: function (data) {
      _.extend(data, {
        show: true,
        useHb: false
      });
    },

    /**
     * 初始化
     */
    init: function () {
      //this.$watch('orderdetail.currentPayMethodId', this.onPayMethodChange);
      this.$on('updatewgt', this.refresh._$bind(this));
    },

    /**
     * 外部组建调用
     */
    refresh: function(){
      var data = this.data,
        orderdetail = data.orderdetail,
        payMethodList = orderdetail.payMethodArray, index;
      // 0元订单不显示
      data.show = orderdetail.realCash>0 ? true: false;

      data.useHb = orderdetail.redPacketSPrice>0 ? true: false;
      // 使用红包
      // currentHbCash提交订单时使用的红包值，数值是上一次组单时使用的红包值
      orderdetail.currentHbCash = data.useHb ? orderdetail.canUseRedPackets: 0;

      // 0元订单
      if( !data.show ){
        orderdetail.currentPayMethod = payMethodList[0];
        orderdetail.currentPayMethodId = '' + payMethodList[0].value;
        return ;
      }

      index = u._$indexOf(payMethodList ,function(_item,_index,_list) {
        return _item.isSelected == true;
      });
      if(index !== -1){
        orderdetail.currentPayMethod = payMethodList[index];
        orderdetail.currentPayMethodId = '' + payMethodList[index].value;
      }else{
    	  orderdetail.currentPayMethod = null;
        orderdetail.currentPayMethodId = null;
      }

    },

    /**
     * 选择支付方式
     * @param newValue
     * @param oldValue
     */
    onPayMethodChange: function(newValue, oldValue){
      if(newValue != this.data.orderdetail.currentPayMethodId){
        this.$emit('change', {'payMethod': parseInt(newValue, 10)});
      }
    },

    useHb: function(type){
      var orderdetail = this.data.orderdetail;
      // currentHbCash组单时使用的红包值
      orderdetail.currentHbCash = !!type? orderdetail.canUseRedPackets: 0;
      this.$emit('change');
    }
  });

  return PayMethod;
})


