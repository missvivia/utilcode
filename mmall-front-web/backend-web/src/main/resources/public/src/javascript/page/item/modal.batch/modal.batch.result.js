/*
 * --------------------------------------------
 * 弹窗组件
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
  "{pro}components/modal/modal.js",
  "{pro}components/notify/notify.js",
  '{pro}extend/util.js',
  'text!./modal.batch.result.html'
  ], function(Modal, notify ,_, tpl){


  return Modal.extend({
    // 默认属性
    // 计算属性
    content: tpl,
    data:{
      count:'',
      message:''
    },
    init: function(){
      this.supr();
      this.$on({
        "close": this.destroy.bind(this), 
      })
    },
    confirm: function(){
		this.$emit("confirm");
		this.destroy();
	},
	close: function(){
		this.$emit("confirm");
		this.destroy();
    }
  });

})