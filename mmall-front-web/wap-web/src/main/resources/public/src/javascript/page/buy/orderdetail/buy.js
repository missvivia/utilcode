/*
 * ------------------------------------------
 * 订单确认页
 * @version  1.0
 * @author   hzwuyuedong@corp.netease.com
 * ------------------------------------------
 */
NEJ.define([
  'base/element',
  '{pro}extend/util.js',
  '{lib}base/element.js',
  '{pro}widget/BaseComponent.js',
  'text!./buy.html',
  'pro/components/notify/notify',
  'pro/page/buy/orderdetail/orderinfo/orderinfo',
  'pro/page/buy/orderdetail/coupon/coupon',
  'pro/page/buy/orderdetail/paymethod/paymethod',
  'pro/page/buy/orderdetail/summary/summary'
],function(_e,_, e, BaseComponent, tpl, Notify, _p, _o, _f, _r){
  var dom = Regular.dom;
  var BuyModule = BaseComponent.extend({
    //url: '/src/javascript/page/buy/data/buy.json',
    url: '/cart/listmini',
    template: tpl,
    config: function (data) {
      _.extend(data, {
        'firstLoad': true,
        'agree': true,
        'payerr': '',
        'orderdetail': {},
        'isPayed': false,
        'cartEndTime': window['cartEndTime'],
        'currTime': window['currTime'],
        'cartIds': window['cartIds'],
        'requestId': window['requestId'],
        'formToken' : window['formToken']
      });
    },
    init: function (data) {
      this.$watch('agree', this.checkValidate._$bind(this));
      this.$on('updatewgt', this.updatewgt._$bind(this));
    },

    updatewgt: function(event){
      this.data.orderdetail.currentAddress = event.currentAddress;

      this.data.orderdetail.currentPayMethodId = 1;
      this.data.orderdetail["cartInfoVO"] = {};
      this.data.orderdetail.cartInfoVO.totalPrice = 0;
      this.data.orderdetail.cartInfoVO.realPrice = 0;
      this.data.orderdetail.cartInfoVO.minusPrice = 0;
      //有选中的地址才做请求
      this.$update();
      if(!!event.currentAddress){
        this.fetch();
      }
    },

    //获取数据
    fetch: function(arg){
      var data = this.data,
        currentCoupon = data.currentCoupon || {};
//        currentAddress = orderdetail.currentAddress,
	      if(arg && arg != 'addCoupon'){
	    	  if(arg.payMethod){
	    		  data.orderdetail["currentPayMethodId"] = arg.payMethod;  
	    	  }else if(arg.userCouponId){
		    	  data.orderdetail["currentCouponId"] = arg.userCouponId;
		    	  data.orderdetail.cartInfoVO.realPrice = data.orderdetail.cartInfoVO.totalPrice - arg.minusPrice;
		    	  data.orderdetail.cartInfoVO.minusPrice = arg.minusPrice;
		      }else{
		    	  delete data.orderdetail["currentCouponId"];
		    	  data.orderdetail.cartInfoVO.realPrice = data.orderdetail.cartInfoVO.totalPrice;
		    	  data.orderdetail.cartInfoVO.minusPrice = 0;
		      }
	    	  this.$update();
	      }
	      
	      
        // currentHbCash可能等于0
        // 第一次组单传''， 后台自动使用红包
//        currentHbCash = orderdetail.currentHbCash==null? '': orderdetail.currentHbCash,
        data = this.data,
        param = {
          'cartIds': data.cartIds,
          't': +new Date()
        };
      //超时状态
      if(data.payerr == '下单超时！'){
        return ;
      }
      if(data.firstLoad || arg == "addCoupon"){
	      this.$request(this.url, {
	        data: param,
	        onload: function(json){
	          if(json.code == 200){
	            if(!!data.firstLoad){
	              data.firstLoad = false;
	            }
	            if(arg == "addCoupon"){
	            	data.firstLoad == true;
	            }
	            data.orderdetail.cartInfoVO.totalPrice = json.result.cartInfoVO.totalPrice;
	            data.orderdetail.cartInfoVO.realPrice = data.orderdetail.cartInfoVO.totalPrice;
	            this.$request("/mycoupon/data/couponList/canuse/",{
	            	 data : {
	            		"districtId" : this.data.orderdetail.currentAddress.sectionId,
	            		"grossPrice" : json.result.cartInfoVO.totalPrice
	            	 },
	            	 onload : function(couponData){
	            		if(couponData.code == 200){
		            		var orderdetail = this.data.orderdetail;
		            		if(couponData.result.total == 0){
		            			orderdetail["currentCouponId"] = "";
		            			orderdetail.cartInfoVO.minusPrice = 0;
		            		}else{
			            		 var max = 0;
			            		 for(var i= 0; i < couponData.result.list.length;i++){
			            			 var coupon = couponData.result.list[i];
			            			 coupon.items = JSON.parse(coupon.items);
			            			 var value = coupon.items[0].result[0].value;
			            			 if(value > max){
			            	    		  max = value;
			            	    		  orderdetail["currentCouponId"] = coupon["userCouponId"];
						            	  orderdetail.cartInfoVO.minusPrice = coupon.items[0].result[0].value;
			            	    	 }
			            		 }
			            		 this.data.orderdetail["couponList"] = couponData.result.list;
		            		}
		            		orderdetail.cartInfoVO.realPrice = orderdetail.cartInfoVO.totalPrice - orderdetail.cartInfoVO.minusPrice;
	            		}
	            	 }
	            });
	            this.$update();
	
	            this.refresh(json.result);
	          }
	        }._$bind(this),
	        onerror: function(json){
	          Notify.notify({
	            type: "error",
	            message: json && json.message || '生成订单失败！'
	          });
	          if(json.code>=500)
	        	  windw.location.href = '/purchase/orderfail?errorType='+(json.code-500);         
	        }._$bind(this)
	      });
      }
    },

    //更具内部componet返回的数据刷新
    refresh: function(result){
        var data = this.data,
        orderdetail = data.orderdetail;
      _.extend(orderdetail, result , true);
      	orderdetail.cartInfoVO.realPrice = orderdetail.cartInfoVO.totalPrice;
      	orderdetail.cartInfoVO.minusPrice = 0;
//      data.cartEndTime = result.cartEndTime;
//      data.cartIds = result.cartIds;
//      data.currTime = result.currTime;
      this.$refs['orderinfo'].$emit('updatewgt');
      this.$refs['coupon'].$emit('updatewgt');
      this.$refs['paymethod'].$emit('updatewgt');
      //this.$refs['summary'].$emit('updatewgt');
      this.checkValidate();
    },

    //提交验证
    checkValidate: function(){
      var data = this.data,
        orderdetail = data.orderdetail;
      if(data.payerr == '下单超时！'){
        return ;
      }
      if(!orderdetail.currentPayMethodId){
        data.payerr = '请选择支付方式！';
      }else if(orderdetail.currentCouponId == '+'){
        data.payerr = '优惠券尚未确认使用';
      }else{
        data.payerr = '';
      }
      this.$update();
    },
    makeData : function (list) {
        var selectedPrice = [];
    	for (var i = list.length - 1, l = 0; i >= l; i--) {
          var tmp = list[i];
          for (var m = 0, n = tmp.skulist.length; m < n; m++) {
            
            var sku = tmp.skulist[m];
            selectedPrice.push(sku.id + "|" + sku.cartPrice);
          }
        }
        this.data.orderdetail.selectedPrice = selectedPrice;
        this.$update();
    },
    //去支付
    pay: function(){
      this.checkValidate();
      var data = this.data;
      if(data.payerr && data.payerr!=''){
        Notify.notify({
          type: "error",
          message: data.payerr
        });
        return false;
      }
      if(!!data.isPayed) return false;
      var form = e._$get('postForm');
      if(!!form){
        data.isPayed = true;
        // 遍历cartStoreList
        if(!!data.orderdetail.cartStoreList){
          this.makeData(data.orderdetail.cartStoreList);
        }
        form.submit();	
      }
    }
  });

  return BuyModule;
});