/*
 * ------------------------------------------
 * 边栏功能实现文件
 * @version  1.0
 * @author   hzwuyuedong@corp.netease.com
 * ------------------------------------------
 */
NEJ.define([
  'pro/extend/config',
  'base/klass',
  'pro/extend/util',
  'base/element',
  'base/event',
  'base/util',
  'ui/base',
  'util/tab/tab',
  'util/effect/api',
  'util/template/tpl',
  'util/timer/interval',
  'pro/widget/util/datanotify',
  'pro/page/minicart/minicart',
  'pro/page/minibrand/minibrand',
  'pro/page/kefu/kefu',
  'pro/components/remind/remind',
  'pro/widget/layer/login/login',
  'util/cache/cookie',
  'pro/widget/util/push',
  'text!./sidebar.html'
], function (_config, _k, _, _e, _v, _u, _t, _t0, _t1, _t2, _t3, _t4,  _cartModule, _brandMoudle, _kefuModule, _Remind, _Login, _j, _push, _tpl, _p, _o, _f, _r, _pro) {
  var _seed_html = _t2._$addNodeTemplate(_tpl),
    _map = ['cart', 'follow', 'app', 'kefu'],
    _mapClass = [_cartModule, _brandMoudle, _.noop, _kefuModule],
    _kefuOnlineClazz = 'u-icon-sservice1',
    _barw0 = 300,
    _barw1 = 59;

  /**
   * 边栏功能控件封装
   *
   * @class   _$$FrmSideBar
   * @extends _$$Abstract
   */
  _p._$$FrmSideBar = _k._$klass();
  _pro = _p._$$FrmSideBar._$extend(_t._$$Abstract);

  /**
   * 控件初始化
   * @param  {Object} 配置参数
   * @return {Void}
   */
  _pro.__reset = function (_options) {
    this.__super(_options);
    this.__doInitDomEvent([
      [window,'resize',this.__onResize._$bind(this)],
      [document.body, 'click', this._$hideLayer._$bind(this)],
      [this.__body, 'click', this.__onWidgetClick._$bind(this)],
      [this.__topBtn, 'click', this.__toTop._$bind(this)],
      [_t4._$$DataNotify, 'barchange',  this.__updatewgt._$bind(this)]
    ]);
    this.__initModule();
  };

  /**
   * 初始化外观信息，子类实现具体逻辑
   *
   * @protected
   */
  _pro.__initXGui = function () {
    this.__seed_html = _seed_html;
  };

  /**
   * 初始化节点，子类重写具体逻辑
   * @protected
   */
  _pro.__initNode = function () {
    this.__super();
    this.__layers = _e._$getByClassName(this.__body, 'j-sd-layer');
    this.__tabs = _e._$getByClassName(this.__body, 'j-sd-nav');
    this.__topBtn = _e._$getByClassName(this.__body, 'j-sd-top')[0];
    this.__kefuIcon = _e._$getByClassName(this.__body, 'u-icon-sservice')[0];
    this.__cartCount = _e._$getByClassName(this.__body, 'j-sd-count')[0];
  };

  /**
   * 初始化模块切换组件
   * @private
   */
  _pro.__initModule = function(){
    this.__navTabs = _t0._$$Tab._$allocate({
      list: this.__tabs,
      index: -1,
      onchange: this.__onNavClick._$bind(this)
    });

    if(_.isLogin()){
      _push._$$Push._$allocate({
       'onpush': this.__doPush._$bind(this)
      });
    }
  };

  /**
   * 处理服务器推送的内容
   * @param _data
   * @private
   */
  _pro.__doPush = function(_data){
    // type#内容 购物车没有#
    var _typeMap = {
      'exService': 'kefu'
    };
    var _list = _data[0].content.split('|'),
      _content = _list[0],
      _from = _list[1],
    _type = !!(_from == null)? 'cart': _typeMap[_from];
    this.__remind({
      content: '<span class="s-fc19 f-fw1">' + _content || "" +'</span>'
    }, _type);
  };

  /**
   * 模块切换
   * @protected
   */
  _pro.__onNavClick = function(_event){
    var _flag,
      _lastLayer = this.__layers[_event.last],
      _currentLayer = this.__layers[_event.index];
    //toggle
    if(_event.last === _event.index &&  !!_currentLayer){
      _flag = _e._$dataset(_currentLayer, 'collapse')=='true'? true: false;
      if(_flag) {
        _e._$delClassName(this.__tabs[_event.index], 'selected');
        this.__hideLayer(_currentLayer);
      }else{
        _e._$addClassName(this.__tabs[_event.index], 'selected');
        this.__showLayer(_currentLayer, _event.index);
      }
      _e._$dataset(_currentLayer, 'collapse', (!_flag).toString());
      return false;
    }

    if(!!_lastLayer && _e._$dataset(_lastLayer, 'collapse')=='true'){
      _e._$dataset(_lastLayer, 'collapse', 'false');
      _e._$delClassName(this.__tabs[_event.last], 'selected');
      this.__hideLayer(_lastLayer);
    }
    if(!!_currentLayer && _e._$dataset(_currentLayer, 'collapse')=='false'){
      _e._$dataset(_currentLayer, 'collapse', 'true');
      _e._$addClassName(this.__tabs[_event.index], 'selected');
      this.__showLayer(_currentLayer, _event.index);
    }
  };

  /**

  _pro.__onServiceClick = function(_event){
    if(!(this.__checkLogin())) {
      return false;
    }
    _._$openKefuWin();
  };
   */

  /**
   * resize
   * @private
   */
  _pro.__onResize = function(){
    var doResize = function(){
      this.__navTabs._$go(-1);
    };
    _._$throttle(doResize, this);
  };


  _pro.__onWidgetClick = function(_event){
    _v._$stopBubble(_event);
  };

  /**
   * 显示不同模块
   */
  _pro.__showLayer = (function(){
    var _resetModule = function(_box, _index){
      try{
        var _module = this['__'+_map[_index]+'Module'];
        if(!_module){
          if(!!this['__'+_map[_index]+'Timer']){
            _t3.cancelInterval(this['__'+_map[_index]+'Timer']);
          }
          switch (_index){
            case 0:
            case 1:
              // cart和我的关注
              this['__'+_map[_index]+'Module'] = new _mapClass[_index]({data:{'height':_box.clientHeight}});
              this['__'+_map[_index]+'Module'].$on('close', this._$hideLayer._$bind(this));
              this['__'+_map[_index]+'Module'].$on('remind', this.__remind._$bind(this));
              break;
            case 3:
              // 客服
              this['__'+_map[_index]+'Module'] = new _mapClass[_index]({data:{'kefuStatus': _e._$hasClassName(this.__kefuIcon, _kefuOnlineClazz)}});
              break;
          }
          this['__'+_map[_index]+'Module'].$inject(_box);
        }else{
          _module.data.height = _box.clientHeight;
          _module.$emit('updatewgt');
          console.log('update' + _map[_index]);
        }
        this.__destoryRemind(_map[_index]);
      }catch (e) {
        throw 'error module '+ _map[_index];
      }
    };
    return function(_box, _index){
      switch (_index){
        case 0:
        case 1:
        case 3:
          if(!(this.__checkLogin())) {
            return false;
          }
          _resetModule.call(this, _box, _index);
          break;
        case 2:
          break;
      }
      // 计算layer dddtop
      switch (_index){
        case 2:
        case 3:
          _e._$style(_box,{top: _e._$offset(this.__tabs[_index], _e._$get('sidebar-box')).y-_box.clientHeight/2 + this.__tabs[_index].clientHeight/2+'px'});
          break;
      }
      var _right = 'right:' + _barw1;
      _t1._$stopEffect(_box);
      _t1._$slide(_box ,_right,{
        timing:'ease-in-out',
        delay:0,
        duration:0.3
      });
    }
  })();

  /**
   * 登陆check
   * @returns {Boolean}
   */
  _pro.__checkLogin = function(){
    var _mark = _.isLogin();
    if(!_mark){
      _Login._$$LoginWindow._$allocate({
        parent: document.body,
        redirectURL: window.location.href
      })._$show();
    }
    return _mark;
  };

  /**
   * 返回顶部
   * @private
   */
  _pro.__toTop = function(){
    _._$scrollTopTo({scrollTop: 0}, 350);
  };

  /**
   *
   * @param _box
   * @private
   */
  _pro.__hideLayer = function(_box){
    _t1._$stopEffect(_box);
    _t1._$slide(_box,'right:-'+_barw0+'px',{
      timing:'ease-in-out',
      delay:0,
      duration:0.3
    });
  };

  /**
   * 气泡提示
   * @private
   */
  _pro.__remind = function(_data, _type){
    var _map = {
      'cart': 'sidebar-carticon',
      'follow': 'sidebar-followicon',
      'kefu': 'sidebar-kefu'
    };
    var _offset = _e._$offset(_e._$get(_map[_type]), this.__body);
    this.__destoryRemind(_type);
    if(!!_offset){
      this['__'+_type+'Remind'] = new _Remind({
        data: _.extend({
          start: [_offset.y, _offset.x + 100],
          end: [_offset.y + 1, _offset.x - 222],
          parent: this.__body,
          duration: 0.5
        }, _data, true)
      });
    }
  };

  /**
   * 销毁气泡提示
   * @private
   */
  _pro.__destoryRemind = function(_type){
    var _remind = this['__'+_type+'Remind'];
    (!!_remind) &&  _remind.destroy();
  };

  /**
   * 用户没有展开购物车的时候提示倒计时
   */
  _pro.__reRreshCartLeftTime = (function(){
    var _leftTime = 0;
    var _timer = function(){
      _leftTime = _leftTime - 1000;
      if (_leftTime <= 0) {
        this.__remind({
          content:'<span class="s-fc19 f-fw1">商品已超时，请重新购买</span>'
        }, 'cart');
        _t3.cancelInterval(this.__cartTimer);
      }else if(_leftTime == 300000){
        this.__remind({
          content:'',
          time: 300000,
          timecnt:'<span class="s-fc19 f-fw1 ">请在 {{mm}}分{{ss}}秒 内付款</span>'
        }, 'cart');
      }
    };
    return function(_cartLeftTime){
      if(_cartLeftTime>0){
        _leftTime = _cartLeftTime;
        if(!!this.__cartTimer){
          _t3.cancelInterval(this.__cartTimer);
        }
        this.__cartTimer = _t3.requestInterval(_timer._$bind(this), 1000);
      }
    }
  })();


  /**
   * 购物车数量
   * @param _cartCount
   * @private
   */
  _pro.__reRreshCartCount = function(_cartCount){
    var _parent = this.__cartCount.parentNode;
    if(_cartCount > 0){
      this.__cartCount.innerText = _cartCount;
      _e._$delClassName(_parent, 'f-dn');
    }else{
      this.__cartCount.innerText = 0;
      _e._$addClassName(_parent, 'f-dn');
    }
  };


  /**
   * 当前用户关注的品牌或档期是否在今天已经开始
   * @param _hasActive
   * @private
   */
  _pro.__reRreshFollowTip = function(_hasActive){
    var _key = 'S_PA_ACTIVE',
      _cookie = _j._$cookie(_key);
    if(_hasActive && _cookie !== 'true'){
      this.__remind({
        content:'<span class="s-fc19 f-fw1">关注的品牌有活动中</span>'
      }, 'follow');
      _j._$cookie(_key,{value:'true',expires:1});
    }
  };

  /**
   * 控件销毁
   *
   */
  _pro.__destroy = function () {
    this.__super();
    this.__navTabs._$recycle();
  };

  /**
   * 更新组建
   * @param _data
   * @private
   */
  _pro.__updatewgt = function(_data){
    var _cartLeftTime = _data.cartLeftTime,
      _cartCount = _data.cartCount,
      _hasActive = _data.hasActive,
      _exCustomerServiceStatus = _data.exCustomerServiceStatus;
    if(_cartLeftTime != null){
      this.__reRreshCartLeftTime(_cartLeftTime);
    }
    if(_cartCount != null){
      this.__reRreshCartCount(_cartCount);
    }
    if(_hasActive != null){
      this.__reRreshFollowTip(_hasActive);
    }
    if(_exCustomerServiceStatus !=null){
      _exCustomerServiceStatus && _e._$addClassName(this.__kefuIcon, _kefuOnlineClazz);
    }

  };

  /**
   * 更新组建
   * @param _data
   * @private
   */
  _pro._$updatewgt = function(_data){
    this.__updatewgt(_data);
  };


  /**
   * 对外接口--hide
   * @public
   */
  _pro._$hideLayer = function(){
    this.__navTabs._$go(-1);
  };

  /**
   * 对外接口--指定type显示layer
   * @public
   */
  _pro._$showLayer = function(_type){
    var _index = _u._$indexOf(_map, _type);
    if(_index!==-1){
      this.__navTabs._$go(_index);
    }
  };

  return _p;
});
