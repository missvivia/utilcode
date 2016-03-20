/**
 *
 * pomelo push消息
 * @author hzwuyuedong@corp.netease.com
 *
 */
NEJ.define([
  'base/klass',
  'pro/extend/util',
  'pro/extend/request',
  'base/util',
  'util/event',
  'pro/extend/config',
  'pro/widget/util/pomelo/socket.io',
  'pro/widget/util/pomelo/pomeloclient'
], function (_k, _, _request, _u, _t, _config, _p, _o, _f, _r) {

  var pomelo_client = window.pomelo;

  _p._$$Push = _k._$klass();
  _pro = _p._$$Push._$extend(_t._$$EventTarget);

  _pro.__reset = function (_options) {
    this.__super(_options);
    this.__getConnectData();
  };

  /**
   * 后台去链接需要的参数
   * @private
   */
  _pro.__getConnectData = function(){
    _request('/message/getsignature', {
      data: {'t': +new Date()},
      onload: function(json){
        this.__doConnect(json.result);
      }._$bind(this),
      onerror: _f
    });
  };

  /**
   * 初始化长连接并连接服务器
   * 将产品信息发送到推送服务器
   * 将用户信息发送到推送服务器
   * @param _data
   * @private
   */
  _pro.__doConnect = function(_data){
    pomelo_client.init(_config.PUSH_HOST, _config.PUSH_PORT, true, function(err) {
      if ( !! err) console.log("connect error.");
      else {
        // 产品注册
        console.log('初始化长连接并连接服务器');
        var _param = _.extend({
          domain: _config.DOMAIN,
          productKey: _config.PUSH_PRODUCT_KEY
        }, _data);
        pomelo_client.registerAndBind(_param, function(data) {
          if (data.code === 200) {
            console.log('将注册信息发送到推送服务器');
            this.__bindEvent();
          }
          else {
            console.log("registerAndBind error");
          }
        }._$bind(this));
      }
    }._$bind(this));
  };

  /**
   * 绑定事件
   * @private
   */
  _pro.__bindEvent = function(){
    //私信为specify, 离线私信为reconnectMessage
    pomelo_client.on('specify', function(data) {
      pomelo_client.ackMessage(_config.DOMAIN, data);  // 对收到的私信消息进行确认
      this._$dispatchEvent('onpush', data);
    }._$bind(this));
  };

  return _p;
});
