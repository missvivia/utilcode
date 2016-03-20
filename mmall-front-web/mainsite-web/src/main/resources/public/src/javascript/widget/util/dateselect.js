/**
 * ==========================================================================================
 * 日期下拉选择控件<br/>
 * 代码书写规范简述：<br/>
 * <pre>
 *    变量/接口前缀        描述                                           发布时是否混淆
 * ------------------------------------------------------------------------------------------
 *    _                  接口内局部变量或者传递的参数                            Y
 *    _$                 对象外可访问的接口或者属性                             Y/N
 *                       此类接口不允许以字符串形式出现
 *                       如果项目所有js文件一起混淆可以考虑混淆
 *    _$$                类对象，同_$前缀的处理                                Y/N
 *    __                 对象外不可访问的接口或者属性                            Y
 *    无                 没有前缀的接口或者属性可以在对象外访问                     N
 *                       代码中可以以字符串的形式出现
 *    X                  单个大写字母命名表示集合了一些通用的属性和接口的对象
 *                       代码中禁止出现单个大写字母命名的变量                      N
 * ------------------------------------------------------------------------------------------
 * </pre>
 * @version  1.0
 * @author   huxueliang(huxueliang@163.org)
 * ==========================================================================================
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'util/event',
    'base/util'
],function(_k,_e,_v,_t,_u,_p,_o,_f,_r){
	var _pro,
	__chdays  = [31,28,31,30,31,30,31,31,30,31,30,31];
/**
 * 获得某一个月的天数
 * @param {Object} _year  年
 * @param {Object} _month 月
 * @return {Number} 返回所在月的天数
 */
var __getDaysInMonth = function(_year,_month){
    if(!_year||!_month)return null;
    if((_month==2)&&((_year%400==0)||(_year%4==0)&&(_year%100!=0))){
        return 29;
    }else{
        return __chdays[_month-1];
    }
};
/**
 * 下拉日期对象
 * @constructor
 * @class   下拉日期对象
 *  		参数说明
 *			    year      年份下拉对象
 *				month     月份下拉对象
 *				day		  日期下拉对象
 *				begin     开始年份
 *				count     初始化年份数
 *				date	  默认选中日期(yyyy-mm-dd)
 * @extends P(N.ut)._$$Event
 */
_p._$$DateSelect = _k._$klass();
_pro = _p._$$DateSelect._$extend(_t._$$EventTarget);


_pro.__init = function(_options){
	this.__year = _e._$get(_options.year);
	this.__month = _e._$get(_options.month);
	this.__day = _e._$get(_options.day);
	var _initYear,_initMonth,_initDay;
	if(!!_options.date&&_u._$isDate(_options.date)){
		_initYear = _options.date.getFullYear();
		_initMonth = _options.date.getMonth()+1;
		_initDay = _options.date.getDate();
	}else if(!!_options.date&&_u._$isString(_options.date)){
		var _dates = _options.date.split('-');
		if(_dates.length==3){
			_initYear = _dates[0];
			_initMonth= _dates[1];
			_initDay  = _dates[2];
		}
	}
	if(!this.__year||!this.__month||!this.__day){return;}
	_v._$addEvent(this.__year,'change',this.__onYearChange._$bind(this));
	_v._$addEvent(this.__month,'change',this.__onMonthChange._$bind(this));
	this.__initYear(_options.begin||1940,_options.count||110,_initYear||1980);
	this.__initMonth(_initMonth||0);
	this.__initDay(_initDay||0);
};
/**
 * 初始化年
 * @param {String} _beginYear 初始化开始年份
 * @param {String} _num 年份数目
 * @param {String} _initYear 初始设置年
 * @return {Void} 
 */
_pro.__initYear = function(_beginYear,_num,_initYear){
	for(var i=this.__year.options.length;i>0;i--){
		this.__year.remove(i-1);
	}
	for(var i=0;i<_num;i++){
		this.__year.options.add(new Option(_beginYear+i,_beginYear+i));
		if(!!_initYear&&(_beginYear+i)==_initYear){
			this.__year.selectedIndex = i;
		}
	}
};
/**
 * 初始化月
 * @param {String} _initMonth 初始化设置月
 * @return {Void} 
 */
_pro.__initMonth = function(_initMonth){
	for(var i=this.__month.options.length;i>0;i--){
		this.__month.remove(i-1);
	}
	for(var i=1;i<13;i++){
		this.__month.options.add(new Option(i,i));
		if(!!_initMonth&&(i==_initMonth)){
			this.__month.selectedIndex = i-1;
		}
	}
}
/**
 * 初始化天
 * @param {String} _initDay 初始设置天
 * @return {Void} 
 */
_pro.__initDay = function(_initDay){
	var _year = this.__year.value,
		_month= this.__month.value,
		_days = __getDaysInMonth(_year,_month);
	for(var i=this.__day.options.length;i>0;i--){
		this.__day.remove(i-1);
	}
	for(var i=1;i<_days+1;i++){
		this.__day.options.add(new Option(i,i));
		if(!!_initDay&&(i==_initDay)){
			this.__day.selectedIndex = i-1;
		}
	}
}
/**
 * 年份change事件
 * @param {String} _event 事件对象
 * @return {Void} 
 */
_pro.__onYearChange = function(_event){
	this.__initDay();
};
/**
 * 月份change事件
 * @param {String} _event 事件对象
 * @return {Void} 
 */
_pro.__onMonthChange = function(_event){
	this.__initDay();
};
/**
 * 获得当前日期值
 * @return {Date} 当前日期值
 */
_pro._$getDate = function(){
	return new Date(this.__year.value,this.__month.value-1,this.__day.value);
};
/**
 * 获得当前日期值
 * @return {Date} 当前日期值
 */
_pro._$getValue = function(){
	return this.__year.value+'-' + this.__month.value+'-'+this.__day.value;
};

return _p;
});