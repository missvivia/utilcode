/**
 * 基于NEJ和bootstrap的日期选择器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "pro/extend/util",
  'text!./returnMoney.html',
   "pro/widget/BaseComponent",
   'base/util'
  ], function(_,_html,BaseComponent,_u){

  var ReturnMoneyComponent = BaseComponent.extend({
    template:_html,
    init:function(){
      this.supr();
    },
    setData:function(_data){
      this.data=_data;
      this.$update();
    }
  });


  return ReturnMoneyComponent;

})