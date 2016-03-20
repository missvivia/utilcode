NEJ.define([	
	'pro/extend/util',                                      //  对应对象   _u       (基本工具)
    'util/cache/cookie',                                    //  对应对象   _j       (cookie)
    'pro/extend/request',                                   //  对应对象   _request (请求相关)
    'util/chain/chainable',                                 //  对应对象   _$       (节点链式调用相关)
    'pro/widget/layer/login/login',							//  对应对象   _l       (登录弹窗)
    'pro/page/activity/layer/giftbag/giftbag'               //  对应对象   _giftbag (优惠券详情遮罩)
], function (_u, _j, _request, _$, _l, _giftbag, _p, _o, _f, _r, _pro) {

	_p._$getcoupon = function (_nolimit) {
    	
		var _islogin = _u.isLogin();
    	var _isshow = !!_j._$cookie('ACTIVITY_REMIND');
    	var _able= !_j._$cookie('P_INFO').match(/corp.netease.com/igm);
    	
    	if(_islogin){
    		if((!!_nolimit || !_isshow) && _able){
	    	
                var __remind = _giftbag._$$Mask._$allocate({
                    parent: document.body,
                    draggable : false,
                    nolimit: !!_nolimit
                });
                __remind._$show();
	                   
    		}else{
    			return true;
    		}
    	}else{
    		var _window = _l._$$LoginWindow._$allocate({
                parent:document.body,
                draggable : false
            });
            _window._$show();
    	}
    }

	return _p;
	
})