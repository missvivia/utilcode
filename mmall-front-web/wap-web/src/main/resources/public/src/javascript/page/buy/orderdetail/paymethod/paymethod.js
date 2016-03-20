/**
 * Created by wuyuedong on 2014/10/10.
 */

NEJ.define([
  '{pro}extend/util.js',
  '{pro}extend/config.js',
  '{lib}base/util.js',
  '{pro}widget/BaseComponent.js',
  'text!./paymethod.html?v=1.0.0.0'
],function(_, config, u, BaseComponent, tpl, _p, _o, _f, _r) {
  var PayMethod = BaseComponent.extend({
    template: tpl,
    paymethodMap: config.PAYMETHOD_MAP,
    name: 'wgt-paymethod',
    config: function (data) {
      _.extend(data, {});
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
      var orderdetail = this.data.orderdetail,
        payMethodList = orderdetail.payMethodArray, index;

      index = u._$indexOf(payMethodList ,function(_item,_index,_list) {
        return _item.isSelected == true;
      });
      if(index !== -1){
        orderdetail.currentPayMethod = payMethodList[index];
        orderdetail.currentPayMethodId = '' + payMethodList[index].value;
      }
    },

    /**
     * 选择支付方式
     * @param newValue
     * @param oldValue
     */
    onPayMethodChange: function(newValue){
    	var oldValue = this.data.orderdetail.currentPayMethodId;
    	if(newValue != oldValue){
        document.getElementById("paymethod"+newValue).src="/src/svg/img/checked.svg";
    	document.getElementById("paymethod"+oldValue).src="/src/svg/img/check.svg"; 
        this.$emit('change', {'payMethod': parseInt(newValue, 10)});
      }
    }
  });

  return PayMethod;
})