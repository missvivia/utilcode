/**
 * 
 */

define([
  "pro/extend/util",
   "pro/widget/BaseComponent",
   'base/util'
  ], function(_,BaseComponent,_u){

  var PayMethodComponent = BaseComponent.extend({
    checkValidity:function(){
    	throw new Error("no check");
    },
    getRequestParams:function(){}
  });


  return PayMethodComponent;

})