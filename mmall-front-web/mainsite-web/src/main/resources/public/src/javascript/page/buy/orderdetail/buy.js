/*
 * ------------------------------------------
 * 订单确认页
 * @version  1.0
 * @author   hzwuyuedong@corp.netease.com
 * ------------------------------------------
 */
NEJ.define([
  '{pro}extend/util.js',
  '{lib}base/element.js',
  '{pro}widget/BaseComponent.js',
  '{pro}page/buy/layer/sure.order.js',
  'text!./buy.html',
  'pro/components/notify/notify',
  '{pro}page/buy/orderdetail/orderinfo/orderinfo.js',
  '{pro}page/buy/orderdetail/coupon/coupon.js',
  '{pro}page/buy/orderdetail/paymethod/paymethod.js',
  '{pro}page/buy/orderdetail/summary/summary.js'
],function(_, e, BaseComponent, SureOrderWindow, tpl, notify, _p, _o, _f, _r){
  var dom = Regular.dom;
  var BuyModule = BaseComponent.extend({
    //url: '/src/javascript/page/buy/data/buy.json',
    url: '/purchase/composeOrder',
    template: tpl,
    config: function (data) {
      _.extend(data, {
        'firstLoad': true,
        'agree': true,
        'payerr': '',
        'isPayed': false,
        'orderdetail': {},
        'cartEndTime': window['cartEndTime'],
        'currTime': window['currTime'],
        'cartIds': window['cartIds'],
        'requestId': window['requestId']
      });
    },
    init: function (data) {
      this.$watch('agree', this.checkValidate._$bind(this));
      this.$on('updatewgt', this.updatewgt._$bind(this));
    },

    updatewgt: function(event){
      this.data.orderdetail.currentAddress = event.currentAddress;
      //有选中的地址才做请求
      this.$update();
      if(!!event.currentAddress){
        this.fetch();
      }
    },

    // 获取数据
    // 第一次运行的时候内部组件没有被初始化currentCoupon currentAddress等都是undefined
    fetch: function(arg){
      var orderdetail = this.data.orderdetail,
        currentCoupon = orderdetail.currentCoupon || {},
        currentAddress = orderdetail.currentAddress,
        currentPayMethodId = orderdetail.currentPayMethodId,
        // currentHbCash可能等于0
        // 第一次组单传''， 后台自动使用红包
        currentHbCash = orderdetail.currentHbCash==null? '': orderdetail.currentHbCash,
        data = this.data,
        param = _.extend({
          'addressId': currentAddress.id,
          'userCouponId': currentCoupon.userCouponId||'',
          'payMethod':  currentPayMethodId||'', // currentPayMethodId 0会转换成"0"
          'cartEndTime': data.cartEndTime,
          'cartIds': data.cartIds,
          'hbCash': currentHbCash,
          't': +new Date()
        }, arg||{}, true);
      //超时状态
      if(data.payerr == '下单超时！'){
        return ;
      }
      this.$request(this.url, {
        data: param,
        onload: function(json){
          if(json.code == 200){
            if(!!data.firstLoad){
              data.firstLoad = false;
              this.$update();
            }
            this.refresh(json.result);
          }
        }._$bind(this),
        onerror: function(json){
          notify.notify({
            type: "error",
            message: json && json.message || '生成订单失败！'
          });
          if(json.code>=500)
        	  windw.location.href = '/purchase/orderfail?errorType='+(json.code-500);         
        }._$bind(this)
      });
    },

    //更具内部componet返回的数据刷新
    refresh: function(result){
      var data = this.data,
        orderdetail = data.orderdetail;
      _.extend(orderdetail, result.orderForm1VO , true);
      data.cartEndTime = result.cartEndTime;
      data.cartIds = result.cartIds;
      data.currTime = result.currTime;
      this.$refs['orderinfo'].$emit('updatewgt');
      this.$refs['coupon'].$emit('updatewgt');
      this.$refs['paymethod'].$emit('updatewgt');
      this.checkValidate();
    },

    //提交验证
    checkValidate: function(){
      var data = this.data,
        orderdetail = data.orderdetail;
      if(data.payerr == '下单超时！'){
        return ;
      }
      if(!orderdetail.currentPayMethod){
        data.payerr = '请选择支付方式！';
      }else if(!data.agree){
        data.payerr = '请同意平台服务协议！';
      }else if(!!this.$refs['summary'] && this.$refs['summary'].data.receipt && this.$refs['summary'].data.receipttype==1 && !this.$refs['summary'].data.receiptOK){
        data.payerr = '请确认公司发票抬头！';
      }else{
        data.payerr = '';
      }
      this.$update();
    },

    //去支付
    pay: function(event){
      var data = this.data;
      if(data.payerr!='' || !!data.isPayed){
        return false;
      }
      var form = e._$get('postForm');
      if(!!form){
        data.isPayed = true;
        // event.target.innerText = '已提交订单';
        form.submit();
        if(data.orderdetail.currentPayMethodId != 1){
          SureOrderWindow._$allocate({
            hdDisabled: dom.id('payforget1').disabled
          })._$show();
        }
      }
    }
  });

  return BuyModule;
});