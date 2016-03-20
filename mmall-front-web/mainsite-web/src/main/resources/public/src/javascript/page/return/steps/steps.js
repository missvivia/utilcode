/*
 * ------------------------------------------
 * 我的退货
 * @version  1.0
 * @author   xxx(xxx@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
       'base/klass', 
       'pro/widget/module', 
        'util/chain/chainable',
		'base/event',
		'base/element',
		'util/tab/tab',
		'util/event',
		'pro/page/return/steps/step1',
		'pro/page/return/steps/step2',
		'pro/page/return/steps/step3',
		'pro/page/return/steps/step4-1',
		'pro/page/return/steps/step4-2' 
], function(_k, _w, $, _v, _e, _tab,_u, StepOne, StepTwo, StepThree, StepFourPass, StepFourFail, _p, _o,_f, _r) {
	var _pro;

	_p._$$Step = _k._$klass();
	_pro = _p._$$Step._$extend(_u._$$EventTarget);

	/**
	 * 初始化方法
	 * 
	 * @return {Void}
	 */
	_pro.__init = function(_options) {
		this.__super();
		this.__initTab(window["g_return"]);
	};

	_pro.__initTab = function(_initData) {
		var that = this;
		this.__tabData = _initData;
		this.__tab = _tab._$$Tab._$allocate({
			list : _e._$getChildren('tab-box'),
			selected : "active",
			index : that.__determineIndex(_initData),
			event : "change",
			onchange : function(_event) {
				$(".step")._$addClassName('f-dn');
				_e._$delClassName(_event.data, 'f-dn');
				switch (_event.index) {
				case 0:
					that.__initStep1(that.__tabData);
					break;
				case 1:
					that.__initStep2(that.__tabData);
					break;
				case 2:
					that.__initStep3(that.__tabData);
					break;
				case 3:
					that.__initStep4(that.__tabData);
					break;
				}
			}
		});

	};

	_pro.__determineIndex = function(_initData) {
		var index = 0, retState = _initData.returnState;
		if (_initData.fromMyOrder) {
			switch (retState && retState.intValue) {
			case 1:
				index = 1;
				break;
			case 2:
			case 3:
			case 5:
			case 7:
			case 9:
				index = 2;
				break;
			case 4:
			case 6:
			case 8:	
			case 10:
			case 11:
				index = 3;
				break;
			default:
				index = 2;
				break;
			}
		}
		return index;

	};

	_pro.__initStep1 = function(_data) {
		var _parent = "step1", that = this;
		this.__step1 = StepOne._$allocate({
			parent : _parent,
			data : _data,
			onnext : function(_event) {
				that.__tabData = _event.nextData;
				that.__tab._$go(1);
				_e._$remove(_parent, false);
			}
		});

	};

	_pro.__initStep2 = function(_data) {
		var _parent = "step2", that = this;
		// _pro.__showTarget("#"+_parent);
		this.__step2 = StepTwo._$allocate({
			parent : _parent,
			data : _data,
			onnext : function(_event) {
				that.__tabData = _event.nextData;
				that.__tab._$go(2);
				_e._$remove(_parent, false);
			}
		});

	};

	_pro.__initStep3 = function(_data) {
		var _parent = "step3", that = this;
		this.__step3 = StepThree._$allocate({
			parent : _parent,
			data : _data
		});
	};
	_pro.__initStep4 = function(_data) {
		var _parent = "step4", that = this;
		if (_data.returnState.intValue == 6||_data.returnState.intValue==10) {
			this.__step4 = StepFourPass._$allocate({
				parent : _parent,
				data : _data
			});
		} else {
			this.__step4 = StepFourFail._$allocate({
				parent : _parent,
				data : _data
			});
		}
	};

	return _p._$$Step;
});