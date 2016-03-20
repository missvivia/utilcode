/*
 * ------------------------------------------
 * 我的订单详情实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/util',
    'base/event',
    'pro/widget/module',
    'pro/page/order/widget/mylayer/paymethod',
    'pro/page/order/widget/noreturn/noreturn',
    'pro/extend/config',
    "{pro}widget/layer/sure.window/sure.window.js",
    'util/chain/NodeList',
    'pro/page/order/widget/mylayer/cancelorder',
    '{pro}extend/request.js',
    'pro/components/notify/notify'
],function(_k,_e,_u,_v,_w,_d,_noreturn,_config,SureWin,$,_u0,Request,_notify,_p,_o,_f,_r){
    var _pro,
        _shareurl = _config.DOMAIN_URL,
        _pic = 'http://paopao.nos.netease.com/6688527b-79ff-4f11-9bbf-cd655a97054a',
        _desc = '送你mmall红包，抢专柜折扣！那件幸福的小事，是每天都有新衣服！',
        _doredp;

    _p._$$OrderDetail = _k._$klass();
    _pro = _p._$$OrderDetail._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
        var _self = this;
    	this.orderId = $("#orderId")._$text();
        this.__super();
        var _modify = _e._$get('modify'),
            _doreturn = _e._$get('detail');
            _doredp   = _e._$get('redp');


        if (!!_doredp){
            _v._$addEvent(_doredp,'click',this.__onRedP._$bind(this));
        }
        _v._$addEvent(_modify,'click',this.__onModify._$bind(this));
//        $("#pay-btn")._$on("click",function(e){
//        	var parentId = $(e.target)._$attr("data-oid");
//        	_self.__goPay(parentId);
//        });
        $("#delete-btn")._$on("click",function(){
        	_self.__ondelete();
        });
        $("#cancel-btn")._$on("click",function(e){
        	_self.onCancelOrder();
        });
        $("#confirm-btn")._$on("click",function(){
        	_self.__onConfirm();
        });
    };



    _pro.__onRedP = function(){
        if (!_doredp){
            return;
        }
        _shareurl += _e._$dataset(_doredp,'url')||'';
        _shareurl = encodeURIComponent(_shareurl);
        _pic = encodeURIComponent(_pic);
        _noreturn._$allocate({cnt:'<section class="u-share-weibo">\
                <a href="http://service.weibo.com/share/share.php?url='+_shareurl+'&pic='+_pic+'&sharebutton&type=icon&language=zh_cn&title='+_desc+'&searchPic=true&style=simple"><i><span>微博</span></i></a></section>\
                <section class="u-share-yixin">\
                <a href="https://open.yixin.im/share?type=webpage&userdesc='+_desc+'&pic='+_pic+'&url='+_shareurl+'&title=mmall"><i><span>易信</span></i></a></section>\
                <section class="u-share-qq">\
                <a href="http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url='+_shareurl+'&pics='+_pic+'&title=mmall&summary='+_desc+'"><i><span>QQ空间<span></i></a></section>',oktxt:'<i class="u-share-cancel">取 消</i>',clazz:'w-share',align:'center bottom'})._$show({});
    };

    /**
     * 修改支付方式
     * @return {[type]} [description]
     */
    _pro.__onModify = function(_event){
        var _orderId = _e._$dataset(_v._$getElement(_event),'orderid')||'0';
        _d._$$Paymethod._$allocate({
            orderId:_orderId
        })._$show();
    };
    _pro.__ondelete = function(e){
    	SureWin._$allocate({txt:'确定删除？',onok:function(){
			Request('/myorder/delete',{
				data : {"orderId" : this.orderId},
				type:'json',
				onload:function(_json){
					if(_json.code==200){
						_notify.show('操作成功');
						location.href="/myorder#state=0"
					}else{
						_notify.show('操作失败');
					}
				},onerror:function(){
					_notify.show('操作失败');
				}
			})
		}._$bind(this)})._$show();      	
    };
    _pro.__onConfirm = function(){
    	SureWin._$allocate({txt:'请确认是否已收到货物？',okText:'我已收到',onok:function(){
			Request('/myorder/confirm',{
				data : {"orderId" : this.orderId},
				type:'json',
				onload:function(_json){
					if(_json.code==200){
						_notify.show('操作成功');
						location.reload();
					}else{
						_notify.show('操作失败');
					}
				},onerror:function(){
					_notify.show('操作失败');
				}
			})
    	}._$bind(this)})._$show();
    };
    _pro.__goPay = function(parentId){
    	var _orderForm = _e._$get("orderForm");
    	Request('/pay/getPayRequestParam?parentId=' + parentId + "&now=" + new Date().getTime(),{
            method:'GET',
            type:'JSON',
            onload:function(_result){
            	if(_result.code == 200){
            		var data = _result.result;
            		_orderForm.version.value = data.version || "";
            		_orderForm.serial_id.value = data.serial_id || "",
            		_orderForm.start_time.value = data.start_time || "",
            		_orderForm.expire_time.value = data.expire_time || "",
            		_orderForm.customer_ip.value = data.customer_ip || "",
            		_orderForm.order_details.value = data.order_details || "",
            		_orderForm.total_amount.value = data.total_amount || "",
            		_orderForm.type.value = data.type || "",
            	    _orderForm.buyer_id.value = data.buyer_id || "",
            		_orderForm.paymethod.value = data.paymethod || "",
            		_orderForm.org_code.value = data.org_code || "",
            		_orderForm.currency_code.value = data.currency_code || "",
            		_orderForm.direct_flag.value = data.direct_flag || "",
            		_orderForm.borrowing_marked.value = data.borrowing_marked || "",
            		_orderForm.coupon_flag.value = data.coupon_flag || "",
            		_orderForm.least_pay.value = data.least_pay || "",
            		_orderForm.coupon.value = data.coupon || "",
            		_orderForm.return_url.value = data.return_url || "",
            		_orderForm.notice_url.value = data.notice_url || "",
            	    _orderForm.partner_id.value = data.partner_id || "",
            		_orderForm.cashier_type.value = data.cashier_type || "",
            		_orderForm.split_rule_code.value = data.split_rule_code || "",
            		_orderForm.split_rule.value = data.split_rule || "",
            		_orderForm.bonus.value = data.bonus || "",
            		_orderForm.settle_amount.value = data.settle_amount || "",
            		_orderForm.token.value = data.token || "",
            		_orderForm.remark.value = data.remark || "",
            		_orderForm.charset.value = data.charset || "",
            		_orderForm.sign_type.value = data.sign_type || "",
            		_orderForm.sign_msg.value = data.sign_msg || "";
            	    _orderForm.action = data.requestUrl || "";
            	   
            	    _orderForm.submit();
            	}
            }
        });
    };
    _pro.onCancelOrder = function(){
        this.__cancelOrder = _u0._$$CancelOrder._$allocate({
        	oplist : ['买错商品了','订单信息填写错误','红包/优惠券使用问题','支付遇到问题','不想买了','其他原因'],
        	onok:this.__doCancel._$bind(this)
        });
        this.__cancelOrder._$show();
    };
    _pro.__doCancel = function(_state){
        console.log(_state);
    	if(!!this.__lockCancel){
            return;
        }
        var _orderId = this.orderId;
        this.__lockCancel = true;
        this.__cancelURL = '/myorder/cancel';
        var _data = _u._$merge(_state,{orderId:_orderId});
        _data.type = _data.type == 0 ? 0 : 1;
        Request(this.__cancelURL,{
            data:_data,
            onload:function(_result){
                this.__lockCancel = false;
                if (_result.code == 200){
                	location.reload();
                    if (!!this.__cancelOrder){
                        this.__cancelOrder._$hide();
                    }
                }else{
                    _notify.notify({type: "error", message: '订单取消失败，请稍后再试'});
                }
            }._$bind(this),
            onerror:function(){
                this.__lockCancel = false;
            }._$bind(this)
        })
    };
    _p._$$OrderDetail._$allocate({});

    return _p;
});