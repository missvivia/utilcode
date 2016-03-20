/**
 *
 * 活动气泡
 * @author hzzhangweidong(hzzhangweidong@corp.netease.com)
 *
 */
NEJ.define([
    'text!./activitybubble.html',
    'pro/extend/util',
    'pro/extend/config',
    'pro/widget/layer/login/login',
    'pro/extend/request',
    'pro/page/activity/layer/lottery/resultwindow',
    'util/cache/cookie',
    'base/element',
    'base/util'
],function(_html,_,config,login,_request,ResultWindow,_j,_e,_u,_p,_o,_f,_r){
    var ActitityBubble = Regular.extend({
        template: _html,
        init:function(){
        	var _cId=_j._$cookie(this.data.paopaoId);
        	if(!!_cId){
        		this.sendKnockRequest(_cId,this.showResultWindow._$bind(this));
        		_j._$cookie(this.data.paopaoId,"");
        	}
        	if(this.data.paopao.type==1){
        		this.data.clazz="m-paopao m-paopao-2";
        		this.$update();
        	}
        },
        config: function(data){
          _.extend(data, {
            clazz:'m-paopao m-paopao-1',
            paopaoId:"paopaoId",
            ks:false,
            paopao:{
            	id:1111,
            	type:0,
            	position:2
            }
          })
        },
        knockpaopao:function(){
        	if(!(_.isLogin())){
        		_j._$cookie(this.data.paopaoId,this.data.paopao.id);
        		login._$$LoginWindow._$allocate({parent:document.body})._$show();
        		return;
        	}
        	this.sendKnockRequest(this.data.paopao.id,this.showResultWindow._$bind(this));
        	 // 统计信息
        	 _gaq.push(['_trackEvent', 'xinchonghui', 'clickbubble'])
        },
        sendKnockRequest:function(_paopaoId,callBack){
        	_request('/activity/knockpaopao',{
                data: "id="+_paopaoId,
                method:'GET',
                onload:function(_data){
                	callBack.call(null,_data.result);
                },
                onerror:function(_result){
                	console.log(_result);
                }
              })
        },
        showResultWindow:function(_result){
//        	var tt=Object {bubbelNum: 0, type: -1, success: false};
//        	_result.success=true;
//        	_result.bubbleNum=4;
        	var that=this;
        	this.data.clazz=this.data.clazz+" m-paopao-scale";
        	this.data.close=true;
        	this.$update();
        	setTimeout(function(){
        	 	if(_result.success){
            		if(_result.bubbleCoupon){
            			_result.type="20";
            			that.__resultWindow=ResultWindow._$allocate({result:_result})._$show();
            			that.data.ks=false;
            		}else{
            			that.data.ks=true;
            		}
            		that.$update();
            	}else{
            		that.__resultWindow=ResultWindow._$allocate({result:_result})._$show();
            	}
            	that.$emit("knockbubble",_result);
        	},500)
       
    		
        }
        
        
    });

    return ActitityBubble;
});