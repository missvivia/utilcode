/**
 * Created by wuyuedong on 2014/10/10.
 */

NEJ.define([
  '{pro}extend/util.js',
  '{lib}base/element.js',
  '{pro}widget/BaseComponent.js',
  'text!./summary.html',
  'pro/components/notify/notify'
], function (_, e, BaseComponent, tpl, Notify, _p, _o, _f, _r) {
  var Summary = BaseComponent.extend({
    template: tpl,
    name: 'wgt-summary',
    config: function (data) {
    },
    init: function (data) {
      this.$watch(['receipt', 'receipttype', 'receiptValue'], this.changeReceipt);
    },

    changeReceipt: function () {
      var data = this.data,
        receipt = data.receipt,
        receiptType = data.receipttype,
        // 内部缓存的抬头
        receiptValue = data.receiptValue;

      // 不需要发票
      if(!receipt){
        data.orderdetail.receiptValue = ''
      }
      // 公司发票
      if (!!receipt && receiptType == 1 && receiptValue != null && _.trim(receiptValue) != '') {
    	if(receiptValue.length >= 30)Notify.notify("发票抬头字数超出！", "error");
        data.orderdetail.receiptValue = _.trim(receiptValue);
      }else if(!!receipt && receiptType == 0){
        data.orderdetail.receiptValue = '个人';
      }else{
        data.orderdetail.receiptValue = '';
      }
      this.$emit('change');
    },

    onBlur: function(){
      var receiptValue = this.data.receiptValue;
      if(receiptValue == null || _.trim(receiptValue) == ''){
        Notify.notify({
          type: "error",
          message: "请填写发票抬头！"
        });
      }
    }
  });

  return Summary;
})