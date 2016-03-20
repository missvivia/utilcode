/**
 * 出仓确认
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */


define([
  "{pro}components/modal/modal.js"
  ], function(Modal){

  var OutModal = Modal.extend({
    data: {
      title: "出仓确认"
    },
    content: "此操作不可逆，是否确认出仓？",
    close: function(){
	    this.$emit('close');
	    this.destroy();
	 },

	 confirm: function(){
	    this.$emit('confirm', this.data);
	    this.destroy();
	 }

    });


  return OutModal;


})