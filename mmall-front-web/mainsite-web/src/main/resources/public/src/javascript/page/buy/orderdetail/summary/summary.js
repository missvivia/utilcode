/**
 * Created by wuyuedong on 2014/10/10.
 */

NEJ.define([
  '{pro}extend/util.js',
  '{pro}extend/config.js',
  '{lib}base/element.js',
  '{pro}widget/BaseComponent.js',
  '{lib}util/form/form.js',
  'text!./summary.html'
], function (_, config, e, BaseComponent, WebForm, tpl, _p, _o, _f, _r) {
  var Summary = BaseComponent.extend({
    template: tpl,
    paymethodMap: config.PAYMETHOD_MAP,
    name: 'wgt-summary',
    config: function (data) {
      _.extend(data, {
        'receiptOK': false
      });
    },
    init: function (data) {
      this.$watch(['receipt', 'receipttype', 'receiptValue'], this.changeReceipt);
      //this.$on('$inject', this.rendered);
    },

    changeReceipt: function () {
      var data = this.data, iptElem,
        receipt = data.receipt,
        receiptType = data.receipttype,
        // 内部缓存的抬头
        receiptValue = data.receiptValue;

      // 不需要发票
      if(!receipt){
        data.orderdetail.receiptValue = ''
      }
      // 公司发票
      if (!!receipt && receiptType == 1 && receiptValue != null) {
        iptElem = e._$get('receipt-value');
        !!receiptValue && _.trim(receiptValue)!=''?
          e._$delClassName(iptElem, 'js-invalid'):
          e._$addClassName(iptElem, 'js-invalid');
        data.orderdetail.receiptValue = _.trim(receiptValue);
      }else if(!!receipt && receiptType == 0){
        data.orderdetail.receiptValue = '个人';
      }
      this.$emit('change');
    },

    sureReceipt: function(){
      var data = this.data, iptElem,
        receipt = data.receipt,
        receiptType = data.receipttype,
        receiptValue = data.receiptValue;
      if (!!receipt && receiptType == 1) {
        iptElem = e._$get('receipt-value');
        !!receiptValue && _.trim(receiptValue)!=''?
          (data.receiptOK = !data.receiptOK):
          e._$addClassName(iptElem, 'js-invalid');
      }
      this.$emit('change');
    },


    onBlur: function(){
      var receiptValue = this.data.receiptValue,
        iptElem = e._$get('receipt-value');
      if(receiptValue == null){
        e._$addClassName(iptElem, 'js-invalid');
      }
    }
  });

  return Summary;
})