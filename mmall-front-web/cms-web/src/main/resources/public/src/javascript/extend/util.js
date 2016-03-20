/*
 * --------------------------------------------
 * 项目内工具函数集合，此页面尽量写注释
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * --------------------------------------------
 */
define([
    '{lib}util/query/query.js',
    '{lib}base/element.js',
    '{lib}base/util.js',
    '{lib}util/cache/cookie.js',
    '{lib}util/ajax/rest.js'
    ], function(e1, e,u, cookie,j) {

  var _ = {},
    noop = function(){};

  /**
   * 选择第一个符合选择器的父类元素
   * @param  {Node} node         初始节点
   * @param  {String|Function} match   选择其，如p.m-tag
   * @param  {Boolean} onlyOne   只需要第一个匹配. 默认true. 为false时，返回为一数组
   * @return {Node|Array}        
   *
   * @example
   *  _.findParent(node, "form[action=xx]", false)
   */
  _.findParent = function( node, match ,onlyOne ){
    if(typeof onlyOne === "undefined") onlyOne = true;
    if(typeof match === "string") match = function(node, selector){
      return nes.matches(node, selector)
    }._$bind2(this, match);
    var parent = node.parentNode, matches = [];
    while( parent ){
      if( match( node ) ){
         matches.push( parent );
         if(onlyOne) break;
      }
      parent = parent.parentNode;
    }
    return onlyOne? matches[0] : matches;
  }

  _.extend = function(o1, o2 ,override){
    for( var i in o2 ) if( o1[i] == undefined || override){
      o1[i] = o2[i]
    }
    return o1;
  }
//类型判断， 同typeof 
  _.typeOf = function (o) {
    return o == null ? String(o) : ({}).toString.call(o).slice(8, -1).toLowerCase();
  }
  _.merge = function(obj1, obj2){
    var 
      type1 = _.typeOf(obj1),
      type2 = _.typeOf(obj2),
      len;

    if(type1 !== type2) return obj2;
    switch(type2){
      case 'object': 
        for(var i in obj2){
          if(obj2.hasOwnProperty(i)){
            obj1[i] = _.merge(obj1[i], obj2[i]);
          }
        }
        break;
      case "array": 
        for(var i = 0, len = obj2.length; i < len; i++ ){
          obj1[i] = _.merge(obj1[i], obj2[i]);
        }
        break;
      default: 
        return obj2;
    }
    return obj1;
   }  // meregeList
	  
  _.mergeList = function(list, list2, ident){
    ident = ident || "id";
    var len = list.length;
    for(; len--;){
      for(var i = 0, len1 = list2.length; i < len1; i++){
        if(list2[i][ident]&&list2[i][ident] === list[len][ident]){
          list.splice(len, 1, _.merge(list2[i],list[len]));
          break;
        }
      }
    }
  }
  /**
   * var str ="font:normal normal 12px italic ;color:#ffffff;border:1px solid #ffffff;background-color#fffefe;opacity:1;"
   * @param str
   * @param split ;
   * @param equalSplit	:
   */
  _.string2Obj = function(str,split,equalSplit){
	  var list = str.split(split),obj={},equalSplit=equalSplit||':';
	  u._$forEach(list,function(innerStr){
		  var ilist = innerStr.split(equalSplit);
		  if(ilist.length>1){
			  obj[ilist[0]] = ilist[1];
		  }
	  });
	  return obj
  };
  /**
   * request
   */

  // min - max的随机整数
  _.random = function rd(min, max){
    return Math.floor(min + Math.random() * (max - min + 1))
  };

  _.getSearch = function(){
    var search = document.location.search;
    var obj = {}
    if(search){
      var params = search.substr(1).split('&') ;
      for ( var i = 0 ; i < params.length ; i++ ) {
        var param = params[i].split('=') ;
        var paramName = decodeURIComponent( param[0] ) ;
        var paramValue = decodeURIComponent( param[1] ) ;
        obj[ paramName ] = paramValue ;
      }
    }
    return obj
  }


//  // meregeList
//  
//  _.mergeList = function(list, list2, ident){
//    ident = ident || "id";
//    var len = list.length;
//    for(; len--;){
//      for(var i = 0, len1 = list2.length; i < len1; i++){
//        if(list2[i][ident] && (list2[i][ident] === list[len][ident])){
//          list.splice(len, 1, list2[i]);
//          break;
//        }
//      }
//    }
//  }

  _.findInList = function(id, list, ident){
    ident = ident || "id";
    var len = list.length;
    for(; len--;){
      if(list[len][ident] == id) return len
    }
    return -1;
  }

  
  _.getRemainTime = function(_time){
	  var second =1000, 
	  	  minute =60*second, 
	  	  hour=minute*60,
	  	  day = hour*24;
	  var days = Math.floor(_time/day);
	  var hours = Math.floor(_time%day/hour);
	  var minutes = Math.floor(_time%day%hour/minute);
	  var seconds = Math.floor(_time%day%hour%minute/second);
	  return {days:days,hours:hours,minutes:minutes,seconds:seconds}
  };
  /**
   * 设置一个时间 2014-9-11
   */
  _.setDate = function(str){
	  var d = new Date();
	  var list = str.split('-');
	  d.setFullYear(parseInt(list[0]),parseInt(list[1])-1,parseInt(list[2]));
	  d.setHours(0)
	  d.setMinutes(0);
	  d.setSeconds(0);
	  return d;
  };

  /**
   * 设置为当天零点的时间戳
   */
  _.setDateTOStart = function(date){
    date.setHours(0)
    date.setMinutes(0);
    date.setSeconds(0);
    return date.getTime();
  };

  /**
   * 设置为当天结束前一秒的时间戳
   */
  _.setDateTOEnd = function(date){
    date.setHours(23)
    date.setMinutes(59);
    date.setSeconds(59);
    return date.getTime();
  };

  /**
   * 平滑移动到某个截点
   */
  _.smoothTo = function(element, timeout){
    if(typeof element === "string") element = nes.one(element);
    if(!element) return;

    var to = e._$offset(element);
    var now = {x: window.scrollX, y: window.scrollY }
    var offset = {x: to.x - now.x, y: to.y-now.y}
    var rate = 1, timeout = timeout || 1000, step = timeout / 60;
    var move = 0.01;

    var timeoutid;

    document.onmousewheel = end;


    function next(){
      rate /= 1.06;
      if(rate < 0.005) rate = 0;
      window.scrollTo( to.x-offset.x * rate + now.x, to.y - rate * offset.y )

      if(rate > 0) timeoutid = setTimeout(next, step)
      else end();
    }

    function end(){
      clearTimeout(timeoutid)
      document.onmousewheel = null;
    }
    timeoutid = setTimeout(next,step)

    return this;
  }

  _.getDoc = function(){
    return (!document.compatMode || document.compatMode == 'CSS1Compat') ? document.documentElement : document.body;
  }

  _.getScroll = function(){
    var doc = _.getDoc();
    return {
      x: window.pageXOffset || doc.scrollLeft, 
      y: window.pageYOffset || doc.scrollTop
    };
  }



  /**
   * 判断是否登陆
   * 
   * @return {Boolean}
   */
  _._$isLogin = function() {
    return cookie._$cookie("XYLCMSSESS") != "";
  };
  
   /**
  * 删除两端空白字符和其他预定义字符
  * 
  * @return {String}
  */
  _._$trim = function(_value){
    return _value.replace(/(^\s*)|(\s*$)/g, '');
  };
  
  /**
   * 获取登陆用户名
   * 
   * @return {String}
   */
    _._$getFullUserName = function() {
    var _userName = decodeURIComponent(cookie._$cookie("XYLCMSUN")).replace(/(^\"*)|(\"*$)/g, '')||"",
        _indexOfUser = _userName.indexOf('|');
        
    if (_indexOfUser > 0) {
      _userName = _userName.substring(0, _indexOfUser)+"";
      
    }
    return _userName||"";
  };

  _._$preview = (function(){

  })
  _.formatDate = function(date, ptn) {
	  var CN_ZH_PATTERN = /[\u4E00-\u9FBF]/;
	  if (typeof date === 'string') {
			ptn = date;
			date = new Date();
		}
		ptn = ptn || "yyyy年MM月dd日 hh:mm";
		date = date || new Date();
		var dt = {
			// 年份
			"yyyy" : date.getFullYear(),
			// 月份
			"MM" : date.getMonth() + 1,
			// 日
			"dd" : date.getDate(),
			// 小时
			"hh" : date.getHours(),
			// 分
			"mm" : date.getMinutes(),
			// 秒
			"ss" : date.getSeconds(),
			// 季度
			"q" : Math.floor((date.getMonth() + 3) / 3),
			// 毫秒
			"SSS" : date.getMilliseconds()
		};
		dt.mm = padLeft(dt.mm, 2, '0');
		if (!CN_ZH_PATTERN.test(ptn)) {
			dt.MM = padLeft(dt.MM, 2, '0');
			dt.dd = padLeft(dt.dd, 2, '0');
		}
		for ( var key in dt) {
			ptn = ptn.replace(key, dt[key]);
		}
		function padLeft(s, l, c) {
			s = '' + s;
			if (l < s.length)
				return s;
			else
				return Array(l - s.length + 1).join(c || ' ') + s;
		}
		return ptn;
	};

  
  return _;

	
})