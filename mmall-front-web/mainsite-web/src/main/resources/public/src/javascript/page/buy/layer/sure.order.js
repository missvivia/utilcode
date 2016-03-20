/**
 * hzwuyuedong 确认订单弹窗
 */

define(['base/klass',
    'pro/extend/config',
    'base/element',
    'base/event',
    'pro/extend/util',
    'pro/widget/layer/window',
    'text!./sure.order.html',
    'util/template/tpl',
    'pro/components/countdown/countdown'
  ],
  function (_k, _config, _e, _v, _, _BaseWindow, _html, _t, Countdown, _p) {
    var pro;
    // ui css text
    var _seed_html = _t._$addNodeTemplate(_html);

    /**
     * 确认订单弹窗
     * @param    {Function}  repay   - 重新支付
     *           {Number}    time    - 倒计时
     *
     */
    _p._$$SureOrderWindow = _k._$klass();
    pro = _p._$$SureOrderWindow._$extend(_BaseWindow);

    /**
     * 控件重置
     * @protected
     * @method {__reset}
     * @param  {Object} options 可选配置参数

     */
    pro.__reset = function (options) {
      options.clazz += ' p-win-order';
      options.title = '确认订单';
      options.draggable = false;
      if(options.hdDisabled){
        _e._$addClassName(this.__chbtnbox, 'f-dn');
      }
      this.__super(options);
      this.__initCountdown(options);
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

    pro.__initCountdown = function (options) {
      var countdown = new Countdown({
        data: {
          time: options.time || 7200000,
          content: '{{HH}}小时{{mm}}分{{ss}}秒',
          onchange: function (_options) {
            if (_options.isdown) {
              window.location.href = '/purchase/buyResultDiv?requestId=' + window['requestId'] + '&type=3';
            }
          }
        }
      });
      countdown.$inject('#ctd');
    };

    /**
     * 初使化节点
     */
    pro.__initNode = function () {
      this.__super();
      var _list = _e._$getByClassName(this.__body, 'j-flag');
      this.__okbtn = _list[0];
      this.__repaybtn = _list[1];
      this.__chbtnbox = _list[2];
      this.__chbtn = _list[3];
      this.__servicebtn = _list[4];
      this.__doInitDomEvent([
        //[this.__okbtn, 'click', this.__onOKClick._$bind(this)],
        //[this.__repaybtn, 'click', this.__onRePay._$bind(this)],
        //[this.__chbtn, 'click', this.__onChangePayMethod._$bind(this)],
        [this.__servicebtn, 'click', this.__onServiceClick._$bind(this)]
      ]);
      _e._$attr(this.__okbtn, 'href', '/purchase/buyResultDiv?requestId=' + window['requestId'] + '&type=2');
      _e._$attr(this.__repaybtn, 'href', '/purchase/buyResultDiv?requestId=' + window['requestId'] + '&type=0');
      _e._$attr(this.__chbtn, 'href', '/purchase/buyResultDiv?requestId=' + window['requestId'] + '&type=1');
    };

    /**
     * 修改支付方式
     * @param event
     * @private
     */
    pro.__onChangePayMethod = function (event) {
      _v._$stop(event);
      this._$dispatchEvent('repay');
    };

    /**
     * 重新支付
     * @param event
     * @private
     */
    pro.__onRePay = function (event) {
      _v._$stop(event);
      this._$dispatchEvent('repay');
    };

    /**
     * 支付成功
     * @param event
     * @private
     */
    pro.__onOKClick = function (event) {
      _v._$stop(event);

    };

    pro.__onServiceClick = function(event){
      _v._$stop(event);
      _._$openKefuWin();
    };

    return _p._$$SureOrderWindow;
  });