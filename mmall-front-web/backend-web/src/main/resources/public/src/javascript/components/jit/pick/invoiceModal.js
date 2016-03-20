/**
 * 发货确认
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */


define([
  'text!./invoiceModal.html',
  '{pro}widget/BaseComponent.js',
  "{lib}util/form/form.js",
  '{lib}util/query/query.js',
  'pro/components/notify/notify',
  '{pro}widget/datePicker/datePicker.js'
  ], function(tpl,BaseComponent,ut,c,notify,dp){

  var InvoiceModal = BaseComponent.extend({
    data: {
      title: "发货确认",
      confirmTitle: "确认发货",
      width:800
    },
    init:function(){
    	if(this.$root == this) this.$inject(document.body);
    	this.__form = ut._$$WebForm._$allocate({
            form: nes.one(".invoiceForm")
        });
    	dp._$$datePickerModule._$allocate({pcon:'invoiceForm'});
    },
    template:tpl,
    close: function(){
	   this.destroy();
	},

    confirm: function(){
    	if (!this.__form._$checkValidity()) {
			notify.showError('带*为必填项，并输入正确的格式');
			return;
		}
		this.$request('/jit/invoice/submitform.json', {
			data: {
				shipOrderDTO : this.__form._$data()
			},
			method : 'post',
			type : 'json',
			onload : function(json) {
				
			}
		});
		this.$emit("confirm",this.__form._$data());
		this.destroy();
	}

  });


  return InvoiceModal;


})