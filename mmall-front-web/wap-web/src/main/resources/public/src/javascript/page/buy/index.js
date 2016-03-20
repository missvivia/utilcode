/**
 * Created by jinze on 2014/12/15.
 */

define([
      'base/klass',
      'base/element',
      'pro/widget/module',
      'pro/widget/ui/address/address',
      '{pro}page/buy/orderdetail/buy.js?v=1.0.0.0',
      'pro/page/buy/address.none/address',
      'pro/page/buy/address/address'
    ],
    function (k, e, t, Address, OrderDetail, AddressNone,SelectedAddress, p, o, f, r, pro) {
      p.$$BuyModule = k._$klass();
      pro = p.$$BuyModule._$extend(t._$$Module);

      /**
       * 初始化
       * @param _options
       */
      pro.__init = function (_options) {
        this.__supInit(_options);
        //this.__address = new Address();
        //this.__address.$inject('#address' || document.body);
        //this.__address.$on('change', this.__onAddressChange._$bind(this));
        //this.__address.$on('select', this.__onAddressSelect._$bind(this));
        this.__addressui = Address._$allocate({parent:'address','change':this.__onAddressChange._$bind(this)});
        this.__orderDetail = new OrderDetail();
        this.__orderDetail.$inject('#order-detail' || document.body);
      };

      /**
       * 地址模块change
       * @param event
       */
      pro.__onAddressChange = function(address){
        if(!!address){
        	this.__addressComponent = new SelectedAddress({data:{address:address}});
        	this.__addressComponent.$on('select',this.__onAddressSelect._$bind(this));
        	this.__addressComponent.$inject('#address');
        	this.__addressui._$recycle();
        } else{
        	
        }
        this.__selectedAddress = address;
        this.__orderDetail.$emit('updatewgt', {currentAddress:address});
      };

      //选择地址时，置空内容
      pro.__onAddressSelect = function(){
        this.__orderDetail.$emit('updatewgt', {});
        this.__addressComponent.destroy();
        this.__addressui = Address._$allocate({parent:'address','change':this.__onAddressChange._$bind(this),id:this.__selectedAddress.id});
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


      p.$$BuyModule._$allocate();

      return p;
    });