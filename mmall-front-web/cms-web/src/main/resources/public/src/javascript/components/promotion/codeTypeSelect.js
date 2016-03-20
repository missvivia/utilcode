/**
 * 条件筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */



define([
  "{pro}components/notify/notify.js?v=1.0.0.0",
  "{pro}extend/util.js?v=1.0.0.0",
  "text!./codeTypeSelect.html?v=1.0.0.2",
  "{pro}widget/BaseComponent.js?v=1.0.0.0",
  "{pro}components/form/btnSelect.js?v=1.0.0.0"], 
    function(notify , _, tpl, BaseComponent, BtnSelect){
  var numberReg = /^[1-9]\d{0,9}$/,
      couponCodeReg = /^[0-9a-zA-Z]{1,20}$/;
  var codeTypeSelect = BaseComponent.extend({
    name: "codetype-select",
    template: tpl,

    config: function(data){
      _.extend(data.couponVO, {codeType:"PUBLIC",binderType:"USER_BINDER"});
      this.configData(data);
    },
    
    // 配置参数
    configData: function(data){
    },
    
    // 检查并返回整理后的数据
    checkAndGetData: function(){
      var showError = this.showError;
      var data = this.data, valid = true;
      var coupon = data.couponVO, returnCoupon = {}; 
      returnCoupon.codeType = coupon.codeType;
      if(returnCoupon.codeType =="PUBLIC"){
    	  //优惠券代码校验
    	  if(!coupon.couponCode){
              valid = showError("优惠券代码不能为空" );
              valid = false;
          }else if(coupon.couponCode && !couponCodeReg.test(coupon.couponCode)){
        	  valid = showError("请输入正确的优惠券代码" );
              valid = false;
          }else{
        	  returnCoupon.couponCode = coupon.couponCode;
          }
    	  
    	  //使用次数校验
    	  if(!coupon.times || coupon.times == 0){
              valid = showError("使用次数不能为空" );
              valid = false;
          }else if(coupon.times && !numberReg.test(coupon.times)){
        	  valid = showError("请输入正确的使用次数（最长为10个字符）");
              valid = false;
          }
          /* if(coupon.times && !numberReg.test(coupon.times)){
        	  valid = showError("使用次数应为大于0的数字，且不能超过10位数");
              valid = false;
          }else if(numberReg.test(coupon.times))*/
          else{
        	  returnCoupon.times = parseInt(coupon.times);
          }
    	  //用户名单校验
    	  returnCoupon.binderType = coupon.binderType;
    	  if(returnCoupon.binderType == "USER_BINDER"){
    		  if(!coupon.users){
                  valid = showError("用户名单不能为空" );
                  valid = false;
              }else{
            	  returnCoupon.users = coupon.users;
              }
    		  
    	  }
    	  //清空其他数据
    	  returnCoupon.randomCount = 0;
      }else{
    	  //申请次数校验
    	  if(!coupon.randomCount){
              valid = showError("申请次数不能为空" );
              valid = false;
          }else{
        	  returnCoupon.randomCount = parseInt(coupon.randomCount);
          }
    	  
    	  //清空其他数据
    	  returnCoupon.couponCode = "";
    	  returnCoupon.users = "";
    	  returnCoupon.times = 0;
      }
      if(!valid)return false;
      return returnCoupon;
    },
    showError: function(msg){

      notify.notify({
        type: "error",
        message: msg 
      })
      return false;
    }
    
  });


  return codeTypeSelect;

})