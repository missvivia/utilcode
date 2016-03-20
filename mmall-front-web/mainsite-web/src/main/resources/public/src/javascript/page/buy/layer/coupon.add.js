/**
 * hzwuyuedong 确认订单弹窗
 */

define(['base/klass',
    'pro/extend/config',
    'base/element',
    'base/event',
    'util/form/form',
    'pro/extend/util',
    'pro/widget/layer/window',
    'pro/extend/request',
    'text!./coupon.add.html',
    'util/template/tpl'
  ],
  function (_k, _config, _e, _v, WebForm, _, _BaseWindow, Request, _html, _t, _p) {
    var pro;
    // ui css text
    var _seed_html = _t._$addNodeTemplate(_html);

    var map = [
      '',
      '该优惠券不存在',
      '该优惠券已过期',
      '该优惠券已被使用',
      '该优惠券不满足使用条件',
      '该优惠券已失效',
      '该优惠券使用次数为0',
      '该优惠券已被激活绑定',
      '该优惠券还未生效'
    ];
    _p._$$CouponAddWindow = _k._$klass();
    pro = _p._$$CouponAddWindow._$extend(_BaseWindow);

    /**
     * 控件重置
     * @protected
     * @method {__reset}
     * @param  {Object} options 可选配置参数

     */
    pro.__reset = function (options) {
      options.clazz += ' p-win-order';
      options.title = '输入优惠码';
      options.draggable = false;
      this.__cartIds = options.cartIds||'';
      this.__super(options);
    };

    /**
     * 初使化UI
     */
    pro.__initXGui = function () {
      this.__seed_html = _seed_html;
    };

    /**
     * 初使化UI
     */
    pro.__destory = function () {
      this.__super();
    };


    pro.__initNode = function(){
      this.__super();
      var list = _e._$getByClassName(this.__body, 'j-flag');
      this.__eform = list[0];
      this.__eok = list[1];
      this.__ecc = list[2];
      this.__doInitDomEvent([
        [this.__eok, 'click', this.__onOKClick._$bind(this)],
        [this.__ecc, 'click', this.__onCCClick._$bind(this)]
      ]);
      this.__form = WebForm._$$WebForm._$allocate({
        form: this.__eform
      });
    };

    pro.__onOKClick = function (event) {
      _v._$stop(event);
      var code;
      if (this.__form._$checkValidity()){
        code = _.trim(this.__form._$get('n00').value);
        Request('/purchase/bindcoupon', {
          data: {
            'cartIds': this.__cartIds,
            'couponCode': code
          },
          onload: function(json){
            if(json.result.state == 0){
              this._$dispatchEvent('onok', json.result.userCouponId);
              this._$hide();
            }else{
              this.__form._$showMsgError('n00', map[parseInt(json.result.state, 10)]);
            }
          }._$bind(this),
          onerror: function(json){
            this.__form._$showMsgError('n00', '使用优惠券失败');
          }._$bind(this)
        });
      }
    };

    pro.__onCCClick = function (event) {
      _v._$stop(event);
      this._$hide();
    };

    return _p._$$CouponAddWindow;
  });