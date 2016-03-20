/**
 * Created by hzwuyuedong on 2014/9/22.
 */

define([
    '{lib}base/element.js',
    '{pro}extend/util.js',
    '{pro}widget/BaseComponent.js',
    'pro/components/notify/notify',
    'text!./minicart.html',
    'pro/widget/util/datanotify',
    'pro/components/remind/remind',
    '{pro}/components/countdown/countdown.js'
  ],
  function(_e, _, BaseComponent, notify, tpl, _t, p, o, f, r, pro) {
    var cartModule = BaseComponent.extend({
      urlMap: {
        'LOAD': '/cart/listmini',
        'DELETE': '/cart/deletemini'
      },
      template: tpl,
      config: function (data) {
        _.extend(data, {
          "firstLoad": true,
          "form": {},
          "format": '请在<span class="s-fc19 f-fw1"> {{mm}}分{{ss}}秒 </span>内付款',
          "cartInfoVO": {
            "totalPrice": 0.00
          }
        });
      },
      init: function (data) {
        this.$on('updatewgt', this.fetch);
        this.$emit('updatewgt');
      },

      //删除
      onDelete: function(product){
        var data = this.data;
        this.$request(this.urlMap['DELETE'], {
          data: {'ids': [product.id]},
          method: 'POST',
          onload: function(json){
            this.refresh(json.result);
          }._$bind(this),
          onerror: function(json){
            notify.notify({
              type: "error",
              message: json && json.message || "删除商品失败！"
            });
          }._$bind(this)
        });
      },

      //更具内部componet返回的数据刷新cart
      refresh: function(result){
        var data = this.data, totalCount=0;
        _.extend(data, result, true);

        if(data.cartInfoVO.leftTime < 0){
          data.cartInfoVO.leftTime = 0;
        }

        //遍历poList
        if(!!data.poList){
          for (var i = 0, l = data.poList.length; i < l; i++) {
            var tmp = data.poList[i];
            for(var m= 0, n= tmp.skulist.length; m<n; m++ ){
              totalCount += tmp.skulist[m].count;
            }
          }
        }

        //遍历activations
        if(!!data.activations){
          for (var i = 0, l = data.activations.length; i < l; i++) {
            var tmp = data.activations[i];
            for(var m= 0, n=tmp.poList.length; m<n; m++){
              for(var j= 0, k =tmp.poList[m].skulist.length; j<k; j++ ){
                totalCount += tmp.poList[m].skulist[j].count;
              }
            }
          }
        }
        data.cartInfoVO.totalCount = totalCount;
        //用datanotify通知
        _t._$refreshCart({'cartCount': totalCount});
        this.$update();
      },

      //获取数据
      fetch: function(){
        var data = this.data;
        this.$request(this.urlMap['LOAD'], {
          data: {'t': +new Date()},
          onload: function(json){
            data.firstLoad = false;
            this.refresh(json.result);
          }._$bind(this),
          onerror: function(json){
            notify.notify({
              type: "error",
              message: "获取购物车数据失败！"
            });
          }._$bind(this)
        });
      },

      //倒计时
      onCountDown: function(time){
        var data = this.data;
        if (!!time.isdown && data.cartInfoVO.leftTime != 0) {
          data.cartInfoVO.leftTime = 0;
          this.$emit('remind', {
            content:'<span class="s-fc19 f-fw1">商品已超时，请重新购买</span>'
          }, 'cart');
          this.$emit('updatewgt');
        }else if(!!time.meta && time.meta.mm== '05' && time.meta.ss == '00'){
          this.$emit('remind', {
            content:'',
            time: 300000,
            timecnt:'<span class="s-fc19 f-fw1 ">请在 {{mm}}分{{ss}}秒 内付款</span>'
          }, 'cart');
        }
      },

      close: function(){
        this.$emit('close');
      }
    });
    return cartModule;
  });