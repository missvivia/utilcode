/*
 * ------------------------------------------
 * 我的订单详情模块实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'pro/widget/module',
    'util/template/tpl',
    'util/template/jst',
    'pro/page/order/widget/mylayer/cancelorder',
    'pro/extend/request',
    'pro/components/countdown/countdown',
    'pro/components/notify/notify',
    'pro/page/order/widget/mylayer/paymethod',
    'pro/widget/layer/sure.window/sure.window'
],function(_k,_e,_v,_u,_w,_t0,_t1,_u0,_request,Countdown,_notify,pay,Suerwindow,_p,_o,_f,_r){
    var _pro;

    _p._$$Detail = _k._$klass();
    _pro = _p._$$Detail._$extend(_w._$$Module);


    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
        // var _myfake = new _fake();
        // _myfake.$inject('#order-detail')
        if (countdownTime > 0){
            this.__countdown = new Countdown({
                data:{
                    time:countdownTime,
                    content:'(<span class="lasttime">距离交易关闭还有<span id="hournode" class="light2">{{HH}}</span>小时<span id="minnode" class="light2">{{mm}}</span>分<span id="secnode" class="light2">{{ss}}</span>秒</span>)',
                    onchange:this.__onCountdown._$bind(this)
                }
            });
            this.__countdown.$inject('#countdown-box');
        }
        _v._$addEvent(document.body,'click',this.__action._$bind(this));
        this.__super();
    };

    _pro.__onCountdown = function(_opt){
        if (_opt.isdown){
            _e._$get('countdown-box').innerHTML = '';
        }
    };

    /**
     * 全局鼠标点击事件
     * @return {[type]} [description]
     */
    _pro.__action = function(_event){
        var _node = _v._$getElement(_event),
            _action = _e._$dataset(_node,'action');
        if (_action == 'unfold'){
            var _detail = _e._$getSibling(_node.parentNode);
            if (_e._$hasClassName(_detail,'f-dn')){
                _e._$delClassName(_detail,'f-dn');
            }else{
                _e._$addClassName(_detail,'f-dn');
            }
        }else if(_action == 'cancel'){
        	var flag=0,
        	    _parentNode = _e._$get("parentId"),
        	    _parentId = _e._$dataset(_parentNode,'parentid');
        	_request('/myorder/getSubOrderIds?parentId=' + _parentId,{
                method:'GET',
                type:'JSON',
                sync: true,
                onload:function(_result){
                	if(_result.code == 200){
                		if(_result.result.length > 1){
                			flag = 1;
                		}
                	}
                },
                onerror: function(_result){
                }
            });
            var _intvalue = _e._$dataset(_node,'simple'),
                _id = _e._$dataset(_node,'id'),
                _isSimple =  parseInt(_intvalue,10) < 30;
            if(flag == 1){
            	this.__cancelOrder0 = _u0._$$CancelOrder._$allocate({
                	flag:flag,
                    simple:_isSimple,
                    onok:this.__doFirstCancel._$bind(this,_id,_parentId,flag,_isSimple)
                });
                this.__cancelOrder0._$show();
            }else{
                this.__cancelOrder = _u0._$$CancelOrder._$allocate({
                    simple:_isSimple,
                    onok:this.__doCancel._$bind(this,_id,_parentId,flag)
                });
                this.__cancelOrder._$show();
            }
            
        }else if(_action == 'gopay'){
            var _id = _e._$dataset(_node,'id'),
                _flag = parseInt(_e._$dataset(_node,'flag'));
            // if(!!this.__payway){
            //     this.__payway._$recycle();
            // }
            this.__payway = pay._$$PayMethod._$allocate({
                parent:document.body,
                title:'请选择付款方式',
                clazz:'w-win w-win-paymethod',
                canCod:!_flag,
                onok:this.__doPayMethod._$bind(this,_id)
            });
            this.__payway._$show();
        } else if (_action == 'getGoods') {
        	var that = this;
        	$.message.confirm("您确定要确认收货吗？","info",function(data){
        		if(data){
        			that.__doGetGoods(_node);
        		}
        	});
        }
    };
    
    _pro.__doGetGoods = function(_node){
    	var _id = _e._$dataset(_node,'id');
    	_request('/myorder/confirm?orderId=' + _id,{
            method:'GET',
            type:'JSON',
            onload:function(_result){
                if (_result.code==200) {
                	$.message.alert("收货成功！", "success");
                	window.location.reload();
                }else if(_result.code==201){
                	$.message.alert(_result.message, "fail");
                }
            	//console.log(_request);
            }
        });
    }

    /**
     * 选择支付
     * @param  {Object} 
     * @return {Void}
     */
    _pro.__doPayMethod = function(_id,_data){
            var _type = parseInt(_data.paytype||'2');
            if(_type == 0){
                location.href = '/purchase/repay?orderId=' + _id + '&pm=0';  //wyb
            }else if(_type == 1){
                location.href = '/purchase/toCod?orderId='  + _id;  //hd
            }else if(_type == 2){
                location.href = '/purchase/repay?orderId=' + _id + '&pm=2';  //zfb
            }
            
        },
     
    /**
     * 子订单取消时，弹框点确定按钮执行此方法
     */   
    _pro.__doFirstCancel = function(_id,_parentId,flag,_isSimple){
        	this.__cancelOrder0._$hide();
        	this.__cancelOrder = _u0._$$CancelOrder._$allocate({
                simple:_isSimple,
                onok:this.__doCancel._$bind(this,_id,_parentId,flag)
            });
            this.__cancelOrder._$show();	
    }

    /**
     * 确定取消订单
     * @param  {Object} data - 取消订单的理由
     * @return {Void}
     */
    _pro.__doCancel = function(_id,_parentId,flag,_state){
        var id = 0;
        this.__cancelURL = '/myorder/cancel';
        if(flag==1){
        	id = _parentId;
        }else{
        	id = _id;
        }
        var _data = _u._$merge(_state,{orderId:id});
        _data.type = _data.type == 0 ? 0 : 1;
        _request(this.__cancelURL,{
            data:_data,
            onload:function(_result){
                if (_result.code == 200){
                    window.location.reload();
                }else{
                    _notify.notify({type: "error", message: '订单取消失败，请稍后再试'});
                    window.location.reload();
                }
            }._$bind(this),
            onerror:function(){

            }
        })
    };
    
    var couponHover = document.getElementById("coupon-hover"),
    	couponOrder = document.getElementById("order-coupon");
    
    if(couponHover!= null) {
	    couponHover.onmouseover = function (e) {
	    	couponOrder.style.display = "block";
	    };
	    couponHover.onmouseout = function (e) {
	    	couponOrder.style.display = "none";
	    };
    }
    
    _p._$$Detail._$allocate({});

    return _p;
});