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
    '{lib}util/chain/NodeList.js',
    'text!./orderlist.html',
    'pro/components/ListComponent',
    'pro/components/pager/orderpager',
    'pro/page/order/widget/mylayer/paymethod',
    'pro/page/order/widget/express/express',
    'util/template/jst',
    'pro/components/countdown/countdown',
    'pro/page/order/widget/mylayer/cancelorder',
    'pro/extend/request',
    'pro/widget/layer/sure.window/sure.window',
    'util/placeholder/placeholder',
    'pro/components/notify/notify'
],function(_,_e,_u,_v,$,_html,ListComponent,orderpager,pay,express,_t0,Countdown,_u0,request,Suerwindow,_t1,_notify,_p,_o,_f,_r){
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
                condition:{'queryType':'0','orderId':'-start-'},
                emptMap:{
                    '1':'还没有待支付订单',
                    '2':'还没有待发货订单',
                    '3':'还没有已发货订单'
                },
                pkgStateMap:{'0':{'nickname':'待发货'},'1':{'nickname':'待发货'},'5':{'nickname':'已发货'},'6':{'nickname':'交易完成'},'7':{'nickname':'已发货'},'8':{'nickname':'交易完成'},'9':{'nickname':'待发货'},'13':{'nickname':'交易完成'},'14':{'nickname':'交易完成'},'15':{'nickname':'已取消','case':'商品缺货'},'16':{'nickname':'已取消','case':'包裹拒收'},'17':{'nickname':'已取消'},'18':{'nickname':'已取消','case':'包裹丢件'},'19':{'nickname':'已取消'}},
                orderStateMap:{'-1':{'nickname':'即将出库'},'0':{'nickname':'等待付款'},'1':{'nickname':'待审核'},'2':{'nickname':'待发货'},'5':{'nickname':'待发货'},'6':{'nickname':'待发货'},'9':{'nickname':'已发货'},'10':{'nickname':'已发货'},'15':{'nickname':'交易完成'},'20':{'nickname':'取消中'},'21':{'nickname':'已取消'},'22':{'nickname':'已取消'},'25':{'nickname':'审核未通过(货到付款)'}}
            });
            this.$watch(this.watchedAttr, function(){
                // 主动操作或页面刷新
                if (this.__isSetCondition == undefined || this.__isSetCondition){
                    this.__isSetCondition = false;
                    return;
                }
                if(this.shouldUpdateList()) this.__getList();
            })
        },
        watchedAttr: ['current','condition.queryType','condition.orderId'],
        url:'/myorder/orderlist?queryType=0',
        template:_html,
        onCancelOrder:function(_x,_index,flag){
            var _isSimple = _x.orderFormState.intValue < 30;
            this.__cancelOrder = _u0._$$CancelOrder._$allocate({
                simple:_isSimple,
                onok:this.__doCancel._$bind(this,_x,_index,flag)
            });
            this.__cancelOrder._$show();
        },
        init: function(){
            this.data.temp = {};
            if(!this.url) throw "ListModule未指定url";
            // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
            this.$on("updatelist", this.__getList.bind(this));
            this.$on("onnumchange", this.data.onnumchange);
            this.$on("update",function(_id,_a,_b,_c){
                if (!this.data.loading){
                    this.shouldUpdateList();
                    this.__afterUpdateList();
                }
            });
        },

        __parseItem: function(_item){
            var _hasReturnState = !1;
            var _hasReturnStateDown = !0;
            var _isrealpackage = !1;
            _u._$forEach(_item.packageList||[],function(pkg){
                var _value = pkg.orderPackageState.intValue;
                var _pckId = pkg.packageId;
                if (_value == 13){
                    _hasReturnState= !0;
                    _hasReturnStateDown = !1;
                }else if (_value == 14){
                    _hasReturnState = !0;
                }
                if (_pckId > 0){
                    _isrealpackage = !0;
                }
            }._$bind(this))
            _item.hasReturnState = ((!!_hasReturnState && !!_hasReturnStateDown) || !_hasReturnState);
            _item.isRealPackage = _isrealpackage;
        },

        __parseList: function(_list){
            _u._$forEach(_list,function(_item){
                this.__parseItem(_item);
            }._$bind(this));
            return _list;
        },

        // update loading
        __getList: function(){
          var data = this.data;
          var option = {
            data: this.getListParam(),
            onload: function(json){
              var result = json.result,
                list = result.list||[];
              _.mergeList(list, data.list,data.key||'id')
              var _list = this.__parseList(list);
              data.total = result.total;
              data.list = _list;
            }._$bind(this),
            onerror: function(json){
              // @TODO: remove
            }
          }
          if(this.xdrOption){
              var xdrOpt = this.xdrOption();
              if(xdrOpt.norest){
                option.data = _ut._$object2query(this.getListParam());
                option.norest = true;
              }
              option.method = xdrOpt.method||'GET';
          }
          this.$request(this.url,option)
        },

        // __findPckState:function(){
        //     _u._$forEach(this.data.list,function(_item){
        //         var _flag = false;
        //         _u._$forEach(_item.packageList,function(_pkg){
        //             if (_pkg.orderPackageState.intValue == 5){
        //                 _flag = true;
        //                 return false;
        //             }
        //         });
        //         _item.orderFormState2 = _flag;
        //     });
        // },
        setCondition:function(_data){
            this.data.current = 1;
            this.__isSetCondition = true;
            var _temp = this.data.condition.queryType;
            if (!_data.isSearch){
                //this.data.condition.orderId = '';
            	delete this.data.condition.orderId;
                this.data.condition.queryType = _data.type;
            }else{
                this.data.condition.queryType = 5;
                this.data.condition.orderId = _data.search;
            }
            this.data.tab = this.data.condition.queryType == 5 ? 0 : this.data.condition.queryType;
            this.$emit('updatelist');
        },
        shouldUpdateList:function(){
            // 翻页前，清理倒计时
            if (this.data.temp){
                _u._$forIn(this.data.temp,function(_temp){
                    if (_temp.countdown){
                        _temp.countdown.destroy();
                    }
                }._$bind(this));
            }
            return true;
        },

        __refreshItem:function(_id,_index){
            this.$request('/myorder/detail',{
                data:{orderId:_id},
                method:'POST',
                onload:function(_result){
                    var _item =_result.result;
                    this.__parseItem(_item);
                    this.data.list[_index] = _item;
                    this.$emit("updatelist");
                    this.$emit('onnumchange');
                }._$bind(this),
                onerror:function(e){}
            })
        },
        __oncountdown: function(_id,_index,_opt){
            if (_opt.isdown){
                // 倒计时不用清理
                this.data.temp[_id].countdown.destroy();
                // _e._$get(_id).innerText = '订单超时';
                // this.__doCancel(_id,_index,{reason:"请选择取消理由"});
                this.__refreshItem(_id,_index);
                // this.shouldUpdateList();
                // this.__getList();
                //     _x.payCloseCD = 0;
                //     _x.
                // this.data.list[_index] =
            }
            // console.log(_id + 'is : ' + _meta.mm + '---' + _meta.ss + 'isdown' + _isdown);
        },
        __afterUpdateList: function(){
            _u._$forEach(this.data.list,function(_item,_index){
            	if(_item.orderId > 0){
            		var _id = _item.orderId;
            	}else{
            		var _id = _item.parentId;
            	}
                this.data.temp[_id] = {};
                this.data.temp[_id].meta = _item;
                if (this.data.temp[_id].meta.payCloseCD > 0){
                    this.data.temp[_id].countdown = new Countdown({
                        data:{
                            time:this.data.temp[_id].meta.payCloseCD,
                            content:'<span>距离交易关闭还有</span><span class="light2">{{HH}}</span>小时<span class="light2">{{mm}}</span>分<span class="light2">{{ss}}</span>秒',
                            onchange:this.__oncountdown._$bind(this,_id,_index)
                        }
                    });
                    this.data.temp[_id].countdown.$inject('#'+_id);
                }
            }._$bind(this));
            this.data.onscroll();
        },
        __doCancel:function(_x,_index,flag,_state){
            this.__cancelURL = '/myorder/cancel';
            var id = 0;
            if (flag == 1){
            	id = _x.parentId;
            }else{
            	id = _x.orderId;
            }
            var _data = _u._$merge(_state,{orderId:id});
            _data.type = _data.type == 0 ? 0 : 1;
            this.$request(this.__cancelURL,{
                data:_data,
                onload:function(_result){
                    this.__cancelOrder._$hide();
                    if (_result.code == 200){
                        /*var _item = _result.result;
                        this.__parseItem(_item);
                        // _x.payState.intValue = 0;
                        this.data.list[_index] = _item;*/
                        this.$emit("updatelist");
                        this.$emit('onnumchange');
                    }else{
                        _notify.notify({type: "error", message: '订单取消失败，请稍后再试'});
                    }
                }._$bind(this),
                onerror:function(){

                }
            })
        },
       /* __doDelete:function(_x){
            this.__deleteURL = '/myorder/delete';
            this.$request(this.__deleteURL,{
                data:{orderId:_x.orderId},
                onload:function(_result){
                    if (_result.code == 200){
                        // 删除成功刷新页面
                        this.$emit("updatelist")
                    }
                }._$bind(this),
                onerror:function(){

                }
            })
        },*/
        returncancel: function(_p,_index){
            // 取消退货,index为订单的index整个刷新
            this.__cancelWin = Suerwindow._$allocate({
                title:' ',
                clazz:'m-window2',
                txt:'您确定要取消退货申请吗？<br /><p class="two">取消后如已超过7天退货期限，将不可再申请退货</p>',
                onok:this.__doReturnCancel._$bind(this,_p,_index)
            });
            this.__cancelWin._$show();

        },

        __doReturnCancel: function(_p,_index){
            this.__cancelreturn = '/_returnorder/cancelreturn';
            this.$request(this.__cancelreturn,{
                data:{ordPkgId:_p.packageId},
                onload:function(_result){
                    this.__cancelWin._$hide();
                    if (_result.code == 200){
                        var _item = _result.result;
                        this.__parseItem(_item);
                        this.data.list[_index] = _item;
                        this.$emit("updatelist");
                        this.$emit('onnumchange');
                    }else{
                        _notify.notify({type: "error", message: '取消退货失败，请稍后再试'});
                    }
                }._$bind(this),
                onerror:function(){

                }
            })
        },
        __doPayMethod:function(_x,_data){
            var _type = parseInt(_data.paytype||'2');
            if(_type == 0){
                location.href = '/purchase/repay?orderId=' + _x.orderId + '&pm=0';  //wyb
            }else if(_type == 1){
                location.href = '/purchase/toCod?orderId='  + _x.orderId;  //hd
            }else if(_type == 2){
                location.href = '/purchase/repay?orderId=' + _x.orderId + '&pm=2';  //zfb
            }
            
        },
        gopay:function(_x,_flag){
            // if (!!this.__payway){
            //     this.__payway._$show();
            //     return;
            // }
            /*this.__payway = pay._$$PayMethod._$allocate({
                parent:document.body,
                title:'请选择付款方式',
                clazz:'w-win w-win-paymethod',
                canCod:_flag,
                onok:this.__doPayMethod._$bind(this,_x)
            })
            this.__payway._$show();*/
        	var parentId = _x.parentId,
        	    _orderForm = _e._$get("orderForm");
        	request('/pay/getPayRequestParam?parentId=' + parentId + "&now=" + new Date().getTime(),{
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
        },
        getGoods:function (_x) {
        	this.$emit('getGoods',_x);
        },
        onDelete:function(_x){
        	this.$emit('delete',_x);
        },
        buyAgain: function (_x) {
        	this.$emit('buyAgain',_x);
        },
        onExpress:function(_flag,_p){
            if (_flag){
                if (!!_p.hideExpress) {
                     _p.hideExpress = clearTimeout(_p.hideExpress);
                }
                if(!_p.express){
                    _p.express = new express({
                        data:{
                            pid: _p.packageId
                        }
                    });
                    _p.express.$inject('#express-box-' + _p.packageId + '-' + _p.packageIndex);
                }
            }else{
                if(!_p.hideExpress){
                    _p.hideExpress = setTimeout(function(){
                        _p.express = _p.express.destroy()
                    },200);
                }
            }
        },

        onShowCase:function(_id){
            var _p = _e._$get('case-'+_id),
                _node = _e._$getChildren(_p)[1];
            if (!!_p.hideCase){
                _p.hideCase = clearTimeout(_p.hideCase);
            }
            _e._$delClassName(_node,'f-dn');
        },
        onHideCase:function(_id){
            var _p = _e._$get('case-'+_id),
                _node = _e._$getChildren(_p)[1];
            _p.hideCase = setTimeout(function(){
                var _p = _e._$get('case-'+_id),
                    _node = _e._$getChildren(_p)[1];
                _e._$addClassName(_node,'f-dn');
            }._$bind(this),200);
        }
    }).filter('format',format).filter('countH',countH);
});
