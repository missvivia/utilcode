/**
 * Created by hzwuyuedong on 2014/9/22.
 */

define([
    '{lib}base/klass.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}page/buy/address/address.js',
    '{pro}page/buy/orderdetail/buy.js',
    '{pro}page/buy/address.none/address.js',
    '{pro}/components/countdown/countdown.js'
  ],
  function (k, e, t, Address, OrderDetail, AddressNone, Countdown,  p, o, f, r, pro) {
    p.$$BuyModule = k._$klass();
    pro = p.$$BuyModule._$extend(t._$$Module);

    /**
     * 初始化
     * @param _options
     */
    pro.__init = function (_options) {
      this.__supInit(_options);
      this.__address = new Address();
      this.__address.$inject('#address' || document.body);
      this.__address.$on('change', this.__onAddressChange._$bind(this));
      this.__orderDetail = new OrderDetail();
      this.__orderDetail.$inject('#order-detail' || document.body);
      this.__initCountdown(_options);
    };

    /**
     * 地址模块change
     * @param event
     */
    pro.__onAddressChange = function(event){
      if(!event.currentAddress){
    	if(this.__addressNone){
    		this.__addressNone = this.__addressNone._$recycle();
    	}
        this.__addressNone = AddressNone._$$Address._$allocate({
          parent: e._$get('address-none'),
          type: 0,
          onok: this.__onSaveAddressOK._$bind(this)
        });
      }else if(!!event.currentAddress && !!this.__addressNone){
        this.__addressNone._$recycle();
        this.__addressNone = null;
      }

      this.__orderDetail.$emit('updatewgt', event);
    };

    /**
     * 用户没有地址，第一次保存地址成功
     * @param data  //保存成功的address对象
     */
    pro.__onSaveAddressOK = function(data){
      this.__address.$emit('updatelist');
    };

    /**
     * 倒计时
     * @param options
     * @private
     */
    pro.__initCountdown = function (options) {
      //F5刷新的时候，如果超时不会进入该页面。这里没处理

      var leftTime = window['cartEndTime']-window['currTime'];
      var countdown = new Countdown({
        data: {
          time: leftTime,
          content: '{{mm}}分{{ss}}秒',
          onchange: function (_options) {
            if (_options.isdown) {
              var elms = e._$getChildren(e._$get('tipbar'));
              e._$addClassName(elms[0], 'f-dn');
              e._$delClassName(elms[1], 'f-dn');
              this.__orderDetail.$update('payerr', '下单超时！');
            }
          }._$bind(this)
        }
      });
      countdown.$inject('#remain');
    };

    p.$$BuyModule._$allocate();

    return p;
  });