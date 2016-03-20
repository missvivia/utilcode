/**
 * 下单没有地址时显示
 * @author   hzwuyuedong(hzwuyuedong@corp.netease.com)
 */
define([
    'base/klass',
    'base/element',
    'base/event',
    'pro/extend/util',
    'ui/base',
    'text!./address.html',
    'util/template/tpl',
    'pro/widget/ui/address/address',
    'pro/extend/request'
  ],
  function (_k, _e, _v, _, _Base, _html, _e1, AddressUI, _request,  _p, _o, _f, _rp) {
    var pro;
    // ui css text
    var _seed_html = _e1._$addNodeTemplate(_html),
      _findParent = function (_elm, _className) {
        while (_elm && !_e._$hasClassName(_elm, _className)) {
          _elm = _elm.parentNode;
        }
        return _elm;
      };

    /**
     * 下单没有地址时显示
     *
     */
    _p._$$Address = _k._$klass();
    pro = _p._$$Address._$extend(_Base._$$Abstract);

    /**
     * 控件重置
     */
    pro.__reset = function (options) {
      this.__super(options);
      this.__addrui = AddressUI._$allocate({parent: this.__box, type: options.type, address: options.address})
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
    pro.__destroy = function () {
      this.__addrui._$recycle();
      this.__super();
    };
    /**
     * 初使化节点
     */
    pro.__initNode = function () {
      this.__super();
      var list = _e._$getByClassName(this.__body, 'j-flag');
      this.__box = list[0];
      this.__addr1 = list[1];
      _v._$addEvent(list[2], 'click', this.__onOKClick._$bind(this));

    };

    pro.__onOKClick = function (event) {
      _v._$stop(event);
      if (this.__addrui._$checkValidity()) {
        var data = _.extend(this.__addrui._$data(), {'default': this.__addr1.checked});
        //TODO
        _request('/profile/address/add',{
          data: data,
          method:'POST',
          onload:function(result){
            this._$dispatchEvent('onok', result);
          }._$bind(this),
          onerror:_f
        })

      }
    }
    return _p;
  })