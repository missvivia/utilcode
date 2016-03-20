/** 
 *倒计时：
 *
 *简单调用 时间戳到当前时间倒计时
 *```javascript
 *timer._$start(时间戳);
 *```
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
    '{pro}/lib/wap/wap.js'// 对应对象_wap(基础工具函数)
], function(_wap, p, o, f, r) {
	/**************************************************
	 *倒计时模块 计时单位:秒
	 ****************************************************/
	
	// 时间格式验证
	function _$checkTime(i) {
		if (i < 10) {
			i = "0" + i;
		}
		return i;
	}
	
	// 倒计时组件
	var timer = {

		/**
		 *开始倒计时
		 * @param  {Element}   arg0 - 倒计时容器
		 * @param  {Number}    arg1 - 结束时间戳
		 */
		_$start : function(showArea, endTimeStamp) {
			this._$end = endTimeStamp;
			this._$area = showArea;
	    	
	    	// 开始倒计时
	    	setInterval(this._$countdown._$bind(this), 1000);
	    },
	    
	    // 倒计时
	    _$countdown : function() {
	    	var count = this._$end - new Date().valueOf();
	    	
	    	// 可进行倒计时
	    	if(count > 0){
		    	// 时间换算
				var 
					// 计算剩余的天数
					_$d = parseInt(count / 1000 / 60 / 60 / 24),

					// 计算剩余的小时数
					_$h = parseInt(count / 1000 / 60 / 60 % 24),

					// 计算剩余的分钟数
					_$m = parseInt(count / 1000 / 60 % 60),

					// 计算剩余的秒数
					_$s = parseInt(count / 1000 % 60);
				
				// 时间格式调整
				_$d = _$checkTime(_$d);
				_$h = _$checkTime(_$h);
				_$m = _$checkTime(_$m);
				_$s = _$checkTime(_$s);
				
				// 计时结果注入
				this._$area.innerHTML = '剩余' + _$d + '天' + _$h + '时' + _$m + '分' + _$s + '秒';
	    	}else{
	    		
	    		//档期结束提醒
				this._$area.innerHTML = '档期已结束';
	    	}
	    }
	}
	
	// change to NEJ module 
	return timer;
})