/*
 *  日期控件
 *  @author cheng-lin(cheng-lin@corp.netease.com)
 */
NEJ.define(['{lib}util/calendar/calendar.js',
	    '{lib}util/chain/chainable.js',
	 	'{pro}widget/date/hourcard.js',
	 	'{pro}widget/date/minutecard.js',
	 	'{pro}widget/date/secondcard.js'],function(){
	var _  = NEJ.P;
		_e = _('nej.e'),
		_p = _('du.ui'),
		_v = _('nej.v'),
		_u = _('nej.u'),
		$ = _('nej.$'),
		_ut= _('nej.ut');
	_p._$$Calendar = NEJ.C();
    _proCalendar = _p._$$Calendar._$extend(_ut._$$Calendar);

    _proCalendar.__reset = function(_options){
    	this.__supReset(_options);
    	this.__hour = _options.hour;
    	this.__minute = _options.minute;
    	this.__second = _options.second;
    	this.__doInitDomEvent([
            [_options.hour,'focus',this.__onHourFocus._$bind(this)]
           ,[_options.minute,'focus',this.__onMinuteFocus._$bind(this)]
           ,[_options.second,'focus',this.__onSecondFocus._$bind(this)]
           ,[_options.today,'click',this.__onToday._$bind(this)]
           ,[_options.ok,'click',this.__onOK._$bind(this)]
           ,[_options.pbody,'click',this.__onHideChildCard._$bind(this)]
        ]);
        this.__lastHour = 0;
        this.__lastMinute = 0;
        this.__lastSecond = 0;
        $(this.__hour)._$on('keyup',this.__onHourKeyUP._$bind(this));
        $(this.__minute)._$on('keyup',this.__onMinuteKeyUP._$bind(this));
        $(this.__second)._$on('keyup',this.__onSecondKeyUP._$bind(this));
    };

    _proCalendar.__onHideChildCard = function(_event){
    	var _target = _v._$getElement(_event);
    	if(_e._$hasClassName(_target,'ztime') && _target.nodeName == 'INPUT'){
    		return;
    	}
        this.__hideChildCard();
    };

    _proCalendar.__hideChildCard = function(){
        if(this.__hourCard) this.__hourCard._$recycle();
        if(this.__minuteCard) this.__minuteCard._$recycle();
        if(this.__secondCard) this.__secondCard._$recycle();
    };

    _proCalendar.__onDateChange = function(_event){
    	var _element = _v._$getElement(_event);
        if (_e._$hasClassName(
            _element,this.__disabled))
            return;
        this.__year  = parseInt(_e._$dataset(_element,'y'));
        this.__month = parseInt(_e._$dataset(_element,'m'));
        this.__date  = parseInt(_e._$dataset(_element,'d'));
        this.__doSyncDateShow();
    };

    _proCalendar.__onToday = function(){
    	var _today = _u._$format(new Date(),'yyyy-MM-dd');
    	this._$setDate(_today);
    	this._$initTime();
    };

    _proCalendar.__onOK = function(){
    	var _date = _u._$format(this._$getDate(),'yyyy-MM-dd'),
    		_h = this.__hour.value.trim(),
    		_m = this.__minute.value.trim(),
    		_s = this.__second.value.trim();
    	var _time = (_h == 0 ? '00' : _h) + ':'
			      + (_m == 0 ? '00' : _m) + ':'
			      + (_s == 0 ? '00' : _s);
    	this._$dispatchEvent('ontimeselected',{date:_date,time:_time});
    };

    _proCalendar.__onSecondKeyUP = function(_event){
         if(_event.which == 37 || _event.which == 38 || _event.which == 8) return;
    	var _value = _v._$getElement(_event).value;
    	if(_value == '') return;
    	if(!isNaN(_value)){
    		if(_value>60||_value<0){
    			_v._$getElement(_event).value = this.__lastSecond;
    			return;
    		}
    		if(Number(_value) === 0){
                _v._$getElement(_event).value = 0;
            }else{
                _v._$getElement(_event).value = Number(_value);
            }
    		this.__lastSecond = _v._$getElement(_event).value;
    	}else{
    		_v._$getElement(_event).value = this.__lastSecond;
    	}
    };

    _proCalendar.__onMinuteKeyUP = function(_event){
         if(_event.which == 37 || _event.which == 38 || _event.which == 8) return;
    	var _value = _v._$getElement(_event).value;
    	if(_value == '') return;
    	if(!isNaN(_value)){
    		if(_value>60||_value<0){
    			_v._$getElement(_event).value = this.__lastMinute;
    			return;
    		}
    		if(Number(_value) === 0){
                _v._$getElement(_event).value = 0;
            }else{
                _v._$getElement(_event).value = Number(_value);
            }
    		this.__lastMinute = _v._$getElement(_event).value;
    	}else{
    		_v._$getElement(_event).value = this.__lastMinute;
    	}
    };

    _proCalendar.__onHourKeyUP = function(_event){
        if(_event.which == 37 || _event.which == 38 || _event.which == 8) return;
    	var _value = _v._$getElement(_event).value;
    	if(_value == '') return;
    	if(!isNaN(_value)){
    		if(_value>23||_value<0){
    			_v._$getElement(_event).value = this.__lastHour;
    			return;
    		}
    		if(Number(_value) === 0){
                _v._$getElement(_event).value = 0;
            }else{
                _v._$getElement(_event).value = Number(_value);
            }
    		this.__lastHour = _v._$getElement(_event).value;
    	}else{
    		_v._$getElement(_event).value = this.__lastHour;
    	}
    };

    _proCalendar.__onHourFocus = function(_event){
    	var _node = _v._$getElement(_event).parentNode.parentNode;
    	this.__hideChildCard();
    	this.__hourCard = _p._$$HourCard._$allocate({
    		parent: document.body,
            clazz: 'm-time',
            onhourchange:this.__onHourChange._$bind(this)
    	});
    	this.__hourCard._$showByReference({
    		align:'top left',
    		target:_node,
    		delta:{top:0,left:0},
    		fitable:true
    	})
    };

    _proCalendar.__onMinuteFocus = function(_event){
    	var _node = _v._$getElement(_event).parentNode.parentNode;
    	this.__hideChildCard();
    	this.__minuteCard = _p._$$MinuteCard._$allocate({
    		parent: document.body,
            clazz: 'm-time',
            onminutechange:this.__onMinuteChange._$bind(this)
    	});
    	this.__minuteCard._$showByReference({
    		align:'top left',
    		target:_node,
    		delta:{top:0,left:0},
    		fitable:true
    	})
    };

    _proCalendar.__onSecondFocus = function(_event){
    	var _node = _v._$getElement(_event).parentNode.parentNode;
    	this.__hideChildCard();
    	this.__secondCard = _p._$$SecondCard._$allocate({
    		parent: document.body,
            clazz: 'm-time',
            onsecondchange:this.__onSecondChange._$bind(this)
    	});
    	this.__secondCard._$showByReference({
    		align:'top left',
    		target:_node,
    		delta:{top:0,left:0},
    		fitable:true
    	})
    };

    _proCalendar.__onSecondChange = function(_value){
    	this.__second.value = _value;
    };

    _proCalendar.__onMinuteChange = function(_value){
    	this.__minute.value = _value;
    };

    _proCalendar.__onHourChange = function(_value){
    	this.__hour.value = _value;
    };

    _proCalendar._$initTime = function(){
    	this.__hour.value = 0;
    	this.__minute.value = 0;
    	this.__second.value = 0;
    };
});