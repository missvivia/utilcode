/**
 * 基于NEJ和bootstrap的日期选择器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "pro/extend/util",
  'text!./epay.html',
   "pro/page/return/steps/payMethod/payMethodComponent",
   'base/util'
  ], function(_,_html,BaseComponent,_u){

  var EpayComponent = BaseComponent.extend({
    template:_html,
    init: function (data) {
    	var that=this;
        this.$watch('refundType',function(){
        	that.$emit("checkSubmit");
        	that.$emit("calculate")
        });
    },
    checkValidity:function(){
      if(this.data.refundType!=-1)
        return true;
      else{
        return false;
      }
    }
  });


  return EpayComponent;

})