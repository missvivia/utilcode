/*
 * ------------------------------------------
 * 订单列表实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */

NEJ.define([
    'pro/extend/util',
    'base/element',
    'base/util',
    'base/event',
    'util/template/jst',
    'util/template/tpl',
    'pro/page/order/widget/mylayer/cancelorder',
    'pro/components/order/orderCache',
    'util/list/waterfall',
    'text!./orderList.html',
    'text!./orderListCache.html?v=1.0.0.1',
    'pro/components/ListComponent',
    'pro/extend/request',
    'pro/widget/layer/sure.window/sure.window',
    'pro/components/notify/notify',
    'util/chain/NodeList'
],function(_,_e,_u,_v,_t3,_t2,_u0,_t0,_t1,_html,_html0,ListComponent,request,Suerwindow,_notify,$,_p,_o,_f,_r){

    var pkgStateMap = {'0':{'nickname':'待发货'},'1':{'nickname':'待发货'},'5':{'nickname':'已发货'},'6':{'nickname':'交易完成'},'7':{'nickname':'已发货'},'8':{'nickname':'交易完成'},'9':{'nickname':'待发货'},'13':{'nickname':'交易完成'},'14':{'nickname':'交易完成'},'15':{'nickname':'已取消','case':'商品缺货'},'16':{'nickname':'已取消','case':'包裹拒收'},'17':{'nickname':'已取消'},'18':{'nickname':'已取消','case':'包裹丢件'},'19':{'nickname':'已取消'}},
        orderStateMap = {'0':{'nickname':'等待付款'},'1':{'nickname':'待审核'},'2':{'nickname':'待发货'},'5':{'nickname':'待发货'},'6':{'nickname':'待发货'},'9':{'nickname':'已发货'},'10':{'nickname':'已发货'},'15':{'nickname':'交易完成'},'20':{'nickname':'取消中'},'21':{'nickname':'已取消'},'22':{'nickname':'已取消'},'25':{'nickname':'审核未通过(货到付款)'}},
        _seed_ui = _t2._$parseUITemplate(_html0),
        _seed_list = _seed_ui['seedList'],
        _seed_loading = _seed_ui['seedLoading'],
        _seed_item = _seed_ui['seedOrderItem'],
        _seed_state = _seed_ui['seedState'];

    var format = function(_time){
        if (!_time) return '';
        _time = parseInt(_time);
        return _u._$format(new Date(_time),'yyyy-MM-dd HH:mm:ss');
    };
    var countH = function(_list){
        var _h = 0;
        _u._$forEach(_list,function(_item){
            _h += _item.cartList.length;
        });
        return _h;
    };

    return ListComponent.extend({
        config: function(data){
            _.extend(data, {
                total: 1,
                current: 1,
                limit: 10,
                list: [],
                lkey:'order-list-data-0',
                condition:{'type':'0','search':'-start-'},
                emptMap:{
                    '1':'还没有待支付订单',
                    '2':'还没有待发货订单',
                    '3':'还没有已发货订单'
                }
            });
            this.$watch(this.watchedAttr, function(){
                // 主动操作或页面刷新
                // if (this.__isSetCondition == undefined || this.__isSetCondition){
                //     this.__isSetCondition = false;
                //     return;
                // }
                // if(this.shouldUpdateList()) this.__getList();
            })
        },
        watchedAttr: ['current','condition.type','condition.search'],
        url:'/myorder/orderlist?queryType=0',
        template:_html,
        onCancelOrder:function(_oid,_intValue,_node){
            this.__cancelOrder = _u0._$$CancelOrder._$allocate({
            	oplist : ['买错商品了','订单信息填写错误','红包/优惠券使用问题','支付遇到问题','不想买了','其他原因'],
            	onok:this.__doCancel._$bind(this,_oid,_node)
            });
            this.__cancelOrder._$show();
        },
        init: function(){
            this.data.temp = {};
            this.box = _e._$get('order-body');
            if(!this.url) throw "ListModule未指定url";
            // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
            this.$on("updatelist", this.__getList.bind(this));
            // this.$on("update",function(_id,_a,_b,_c){
            //     if (!this.data.loading){
            //         this.shouldUpdateList();
            //         this.__afterUpdateList();
            //     }
            // })
            _v._$addEvent(this.box,'click',function(_event){
                var _target = _v._$getElement(_event),
                    _action = _e._$dataset(_target,'action'),
                    _intValue = _e._$dataset(_target,'intvalue')||0,
                    _node = _target.parentNode.parentNode.parentNode;
                if (!!_action){
                    var _oid = _e._$dataset(_target,'oid');
                    if (_action == 'delete'){
                        this.onDelete(_oid,_node);
                    }else if(_action == 'cancel'){
                        this.onCancelOrder(_oid,_intValue,_node);
                    }else if(_action == 'confirm'){
                    	this.onConfirm(_oid);
                    }else if(_action == 'reBuy'){
                    	this.onRebuy(_oid);
                    }
                }
            }._$bind(this));
        },

        __getList:function(){
            if (!!this.__listmodule){
                this.__listmodule = this.__listmodule._$recycle();
            }
            this.initListModule();
        },

        initListModule:function(){
            var _limit = 5,_options = {};
            _options.url   = this.url;
            this.data.lkey = 'order-list-' + this.data.condition.type;
            this.__listmodule = _t1._$$ListModuleWF._$allocate({
                limit:_limit,
                sbody:window,
                parent:this.box,
                delta:30,
                item:{klass:_seed_list,pkgStateMap:pkgStateMap,orderStateMap:orderStateMap}, // 这里也可以传自己实现的item类型
                cache:{
                    clear:true,
                    ext:{url:_options.url||''},
                    lkey:this.data.lkey,// 此key必须是唯一的，对应了item中的值,也是删除选项的data-id
                    data:{queryType:this.data.condition.type,t:+new Date()}, // <--- 列表加载时携带数据信息，此数据也可在cache层补全
                    klass:_t0._$$OrderCache
                },
                onbeforelistload:function(_event){
                    _event.stopped = !0;
                    if(!!this.__ntip){
                        _e._$removeByEC(this.__ntip);
                    }   
                    this.__ntip = _t2._$getNodeTemplate(_seed_loading);
                    this.box.insertAdjacentElement('beforeEnd',this.__ntip);
                }._$bind(this),
                onafterlistrender:function(_options){
                    if(!!this.__ntip){
                        _e._$removeByEC(this.__ntip);
                    }
                    _v._$dispatchEvent(window,'scroll');
                    var _list = _e._$getByClassName(this.box,'j-cd');
                    _list = _list.slice(_options.offset,_options.offset + _limit);
                    var w = parseInt($(".w-order")[0].clientWidth) - 12;
                    var orderW = parseInt($(".w-order")._$style("padding-left"));  
                    var marginR = parseInt($(".img")._$style("margin-right"));
                    var imgW = (w-5)/5 - marginR;
                    $(".img")._$style("width",imgW+"px");
                }._$bind(this),
                onemptylist:function(_event){
                    if (!!this.__ntip){
                        _e._$removeByEC(this.__ntip);
                    }
                    var _type = this.data.condition.type,
                        _state = _t2._$getNodeTemplate(_seed_state),
                        _txtbox = _e._$getByClassName(_state,'txt')[0];
                    _txt = '<div><p>您还没有下过单呢</p><p>去首页看看吧</p></div>';
                    if (_type == 1){
                        _txt = '目前没有待支付订单';
                    }else if(_type == 2){
                        _txt = '目前没有待发货订单';
                    }else if(_type == 3){
                        _txt = '目前没有已发货订单';
                    }
                    _txtbox.innerHTML = _txt;
                    this.box.insertAdjacentElement('beforeEnd',_state);
                    _event.stopped = !0;
                }._$bind(this),
                pager:{parent:'pager-box'}
            });
        },
        setCondition:function(_data){
            this.data.current = 1;
            this.__isSetCondition = true;
            var _temp = this.data.condition.type;
            if (!_data.isSearch){
                this.data.condition.search = '';
                this.data.condition.type = _data.type;
            }else{
                this.data.condition.type = 4;
                this.data.condition.search = _data.search;
            }
            this.data.tab = this.data.condition.type == 4 ? 0 : this.data.condition.type;
            this.$emit('updatelist');
        },
        __doCancel:function(_orderId,_node,_state){
            if(!!this.__lockCancel){
                return;
            }
            this.__lockCancel = true;
            this.__cancelURL = '/myorder/cancel';
            var _data = _u._$merge(_state,{orderId:_orderId});
            _data.type = _data.type == 0 ? 0 : 1;
            this.$request(this.__cancelURL,{
                data:_data,
                onload:function(_result){
                    this.__lockCancel = false;
                    if (_result.code == 200){
                    	this.__getList();
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
        },
        __doDelete:function(_orderId,_node){
            this.__deleteURL = '/myorder/delete';
            this.$request(this.__deleteURL,{
                data:{orderId:_orderId},
                onload:function(_result){
                    if (_result.code == 200){
                        _e._$addClassName(_node,'f-dn');
                        _v._$dispatchEvent(window,'scroll');
                        // 删除成功刷新页面
                        // this.$emit("updatelist")
                    }
                }._$bind(this),
                onerror:function(){
                	_notify.notify({type: "error", message: '订单删除失败，请稍后再试'});
                }
            })
        },
        onPay : function(parentId){
        	var _orderForm = _e._$get("orderForm");
        	this.$request('/pay/getPayRequestParam?parentId=' + parentId + "&now=" + new Date().getTime(),{
                method:'GET',
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
        },
        onDelete:function(_orderId,_node){
            this.__showDelete = Suerwindow._$allocate({
                txt:'确定删除订单吗？',
                onok:this.__doDelete._$bind(this,_orderId,_node)
            });
            this.__showDelete._$show();
        },
        onConfirm : function(_orderId){
            this.__showConfirm = Suerwindow._$allocate({
                txt:'请确认是否已收到货物？',
                onok:this.__doConfirm._$bind(this,_orderId)
            });
            this.__showConfirm._$show();        	
        },
        __doConfirm:function(_orderId){
            if(!!this.__lockConfirm){
                return;
            }
            this.__lockConfirm = true;
            this.__confirmURL = '/myorder/confirm';
            
            this.$request(this.__confirmURL,{
                data:{"orderId":_orderId},
                onload:function(_result){
                    this.__lockConfirm = false;
                    if (_result.code == 200){
                    	this.__getList();
                        if (!!this.__showConfirm){
                            this.__showConfirm._$hide();
                        }
                    }else{
                        _notify.notify({type: "error", message: '操作失败，请稍后再试'});
                    }
                }._$bind(this),
                onerror:function(){
                    this.__showConfirm = false;
                }._$bind(this)
            })
        },
        onRebuy:function(orderId){
        	var imgs = $(".m-img");
        	var param = {"orderId":orderId,"cartItemDTOs" : []};
        	for(var i = 0; i < imgs.length;i++){
        		if($(imgs[i])._$attr("orderId") == orderId){
	        		var sku = {};
	        		sku.skuid = $(imgs[i])._$attr("skuid");
	        		sku.count = $(imgs[i])._$attr("count");
	        		param.cartItemDTOs.push(sku);
        		}
        		
        	}
        	this.$request("/cart/buyAgain",{
        		method:"post",
        		data : param,
        		onload : function(_result){
        			if(_result.code == 200){
        				location.href="/cartlist";
        			}else{
                        _notify.notify({type: "error", message: _result.message});
                    }
        		}._$bind(this),
                onerror:function(_result){
                	_notify.notify({type: "error", message: _result.message});
                }._$bind(this)
        	});
        }
    }).filter('format',format).filter('countH',countH);
});