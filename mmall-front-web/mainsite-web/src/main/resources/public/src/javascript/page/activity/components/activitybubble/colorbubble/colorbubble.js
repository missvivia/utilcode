/**
 *
 * 活动气泡
 * @author hzzhangweidong(hzzhangweidong@corp.netease.com)
 *
 */
NEJ.define([
    'text!./colorbubble.html',
    'pro/extend/util',
    'pro/extend/config',
    'pro/widget/layer/login/login',
    'pro/page/activity/components/activitybubble/activitybubble',
    'pro/page/activity/layer/lottery/resultwindow',
    'pro/extend/request',
    'base/element',
    'base/util'
],function(_html,_,config,login,BaseComponent,ResultWindow,_request,_e,_u,_p,_o,_f,_r){
    var ActitityBubble = BaseComponent.extend({
    	template: _html,
        showResultWindow:function(_result){
//        	var tt=Object {bubbelNum: 0, type: -1, success: false};
//        	_result.success=true;
//        	_result.bubbleNum=4;
//        	_result.type="cp1";
        	var that=this;
        	this.data.clazz=this.data.clazz+" m-paopao-scale";
        	this.data.close=true;
        	this.$update();
        	
        	setTimeout(function(){
        	 	if(_result.success){
        	 		_result.type="cp1"
        	 		that.__resultWindow=ResultWindow._$allocate({result:_result})._$show();
            	}else{
            		_result.type="cp2"
            		that.__resultWindow=ResultWindow._$allocate({result:_result})._$show();
            	}
            	that.$emit("knockbubble",_result);
        	},500)
       
    		
        }
        
        
    });

    return ActitityBubble;
});