/**
 * 邮箱大师专享
 * 
 * @author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([ 
	'base/klass', // 基类 对应元素：_k
	'pro/widget/module', // 公共类库 对应元素：_m
	'util/chain/chainable' // 节点链式调用相关 对应对象 _$
], function(_k, _m, _$, _p, _o, _f, _r, _pro) {
	/**
	 * 页面模块基类
	 * 
	 * @class _$$Module
	 * @extends _$$Module
	 */
	_p._$$Module = _k._$klass();
	_pro = _p._$$Module._$extend(_m._$$Module);
	/**
	 * 控件重置
	 * 
	 * @param {Object}
	 *            配置参数
	 * @return {Void}
	 */
	_pro.__reset = function(_options) {
		this.__super(_options);
		this.__initDom();
		this.__initEvent();
	};
	/**
	 * 初始化节点
	 */
	_pro.__initDom = function() {
		this.__btn = _$('.shopping');
	}
	/**
	 * 初始化事件
	 */
	_pro.__initEvent = function() {
		this.__btn._$on('click', function() {
			var userAgentInfo = navigator.userAgent;
			var Agents = new Array("Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod");
			var isPC = true;
			for (var v = 0; v < Agents.length; v++) {
				if (userAgentInfo.indexOf(Agents[v]) > 0) {
					isPC = false;
					break;
				}
			}
			location.href = 'http://' + (isPC ? '' : 'm.023.baiwandian.cn') + '';
		});
	}

	// init page
	_p._$$Module._$allocate();

})