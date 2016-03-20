/*
 * --------------------------------------------
 * 项目内工具函数集合，此页面尽量写注释
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * --------------------------------------------
 */
define([
    'pro/extend/config',
    '{lib}base/element.js',
    '{lib}util/query/query.js',
    '{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}util/cache/cookie.js',
    '{lib}util/animation/easeout.js',
    '{lib}util/ajax/rest.js'
    ], function(config, e, e1, u, v, cookie, easeout, j) {

  var _ = {},
    noop = function(){},
    user;//{"nickname":"lyray2003","birthDay":0,"gender":0,"birthYear":0,"birthMonth":0,"account":"lyray2003@163.com"}




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

  // 类型判断， 同typeof 
  _.typeOf = function (o) {
    return o == null ? String(o) : ({}).toString.call(o).slice(8, -1).toLowerCase();
  }


  _.extend = function(o1, o2 ,override){
    for( var i in o2 ) if( o1[i] == undefined || override){
      o1[i] = o2[i]
    }
    return o1;
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

  // meregeList

   /**
   * 深度merge，根据不同类型
   */
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
        obj1.length = obj2.length;
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

  _.findInList = function(id, list, ident){
    ident = ident || "id";
    var len = list.length;
    for(; len--;){
      if(list[len][ident] === id) return len
    }
    return -1;
  };

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
   * 平滑移动到某个截点
   */

  _.smoothTo = function(element, timeout){
    if(typeof element === "string") element = nes.one(element);
    if(!element) return;
    var now = {
      x: Math.max(document.body.scrollLeft, document.documentElement.scrollLeft),
      y: Math.max(document.body.scrollTop, document.documentElement.scrollTop)
    }

    var to = e._$offset(element);
    to.y -= 40;

    var offset = {x: to.x - now.x, y: to.y-now.y}
    var rate = 1, timeout = timeout || 500, step = timeout / 60;
    var move = 0.01;

    var timeoutid;

    document.onmousewheel = end;


    function next(){
      move += 0.05
      if(move > 1) move = 1; 
      window.scrollTo( offset.x * move + now.x, now.y + move * offset.y )
      if(move < 1 ) timeoutid = setTimeout(next, step)
      else end();
    }

    function end(){
      clearTimeout(timeoutid)
      document.onmousewheel = null;
    }
    timeoutid = setTimeout(next,step)

    return this;
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
   * 判断是否登陆
   * 
   * @return {Boolean}
   */
  _.isLogin = function() {
    return cookie._$cookie("S_INFO") != "" || cookie._$cookie("S_OINFO") != "";
  };
  
   /**
  * 删除两端空白字符和其他预定义字符
  * 
  * @return {String}
  */
  _.trim = function(_value){
    return _value.replace(/(^\s*)|(\s*$)/g, '');
  };
  
  /**
   * 获取登陆用户名
   * 
   * @return {String}
   */
  _.getFullUserName = function() {
	  if(user){
		  return user.nickname;
	  }
	var _type = "";
	if(cookie._$cookie("S_INFO") != ""){
		_type = "P_INFO";
	}else if(cookie._$cookie("S_OINFO") != ""){
		_type = "P_OINFO";
	}else{
		return "";
	}
    var _userName = decodeURIComponent(cookie._$cookie(_type)).replace(/(^\"*)|(\"*$)/g, '')||"",
        _indexOfUser = _userName.indexOf('|');
        
    if (_indexOfUser > 0) {
      _userName = _userName.substring(0, _indexOfUser)+"";
      
    }
    var _nickName = decodeURIComponent(cookie._$cookie('userNickName'));
    return _nickName||_userName;


  };
  _.setFullUserName = function(_user){
	  user = _user;
  };
  /**
   * list0去重list1的包含的元素 bywuyuedong
   * @param list0
   * @param list1
   * @returns {Array}
   * @private
   */
  _._$grepArray = function(list0, list1){
    var result = [];
    u._$forEach(list0, function(item, index){
      if(u._$indexOf(list1, item) === -1){
        result.push(item);
      }
    });
    return result;
  };

  /**
   * 函数节流 bywuyuedong
   * @param method
   * @param scope
   * @private
   */
  _._$throttle = function(method, scope) {
    clearTimeout(method.tId);
    method.tId= setTimeout(function(){
      method.call(scope);
    }, 100);
  };

  /**
   * 页面滑动到scrollTop指定位置 by wuyuedong
   * @param _options
   * @param _duration
   * @private
   */
  _._$scrollTopTo = function(_options, _duration) {
    var _html = document.documentElement,
      _body = document.body,
      _scrollTop = e._$getPageBox().scrollTop;
    if(_scrollTop == _options.scrollTop){
      return false;
    }
    easeout._$$AnimEaseOut._$allocate({
      from: {
        offset: _scrollTop
      },
      to: {
        offset: _options.scrollTop
      },
      duration: _duration || 300,
      onupdate: function (_event) {
        //_html.scrollTop = Math.ceil(_event.offset);
        //_body.scrollTop = Math.ceil(_event.offset);
        window.scrollTo(0, Math.ceil(_event.offset));
      },
      onstop: function () {
        this._$recycle();
      }
    })._$play();
  };
  
  
  /**
   *反编码html代码，'&amp;lt;' -> '<' 
   *
   * @param  {String} arg0 - 待反编码串
   * @return {String}        反编码后的串
   */
  _._$unescape = (function(){
      var _map = {r:/\&(?:lt|gt|amp|#39|quot|#34)\;|\<br\/\>/gi,'&lt;':'<','&gt;':'>','&amp;':'&','&#39;':"'",'&quot;':'"','<br/>':'\n','&#34;':'"'};
      return function(_content){
          return u._$encode(_map,_content);
      };
  })();

  /**
   * open客服聊天窗口
   * @private
   * =========================================
   *  type = 1   【在线咨询】或【留言反馈】
   *  type = 2   【咨询记录】
   * ========================================
   */
  _._$openKefuWin = function(type){
    var _box = e._$getPageBox();
    var width = 550,
      height = 540,
      iTop = Math.floor((_box.clientHeight - height)/2),
      iLeft = Math.floor((_box.clientWidth - width)/2);
    switch (type){
      case 1:
        window.open (config.DOMAIN_URL+ '/ex/entrance' , '_blank' ,'height=' + height +',width='+width+',top='+iTop+', left='+iLeft + ', scrollbars=no,location=no');
        break;
      case 2:
        window.open (config.DOMAIN_URL+ '/ex/history' , '_blank' ,'height=' + height +',width='+width+',top='+iTop+', left='+iLeft + ', scrollbars=no,location=no');
        break;
      default:
        window.open (config.DOMAIN_URL+ '/ex/entrance' , '_blank' ,'height=' + height +',width='+width+',top='+iTop+', left='+iLeft + ', scrollbars=no,location=no');
        break;
    }

  };

  /**
   * 容器属性变化transition 配合css3使用
   * 属性改变前后各调用一次
   * @param elem
   */
  _.transition = function(elem, prop, defaultValue){
    if (!elem || (typeof window.getComputedStyle == "undefined")) return;
    var origin = window.getComputedStyle(elem)[prop];
    elem.style[prop] = defaultValue;
    var target = window.getComputedStyle(elem)[prop];
    elem.style[prop] = origin;
    setTimeout(function() {
      elem.style[prop] = target;
    }, 15);
  };
  
  /***
   * 获得url里面所有的querystring参数,xxx?t=1&t2=3;将会返回{t:1,t2:3};
   */
  _._$queryStrings=function(){
	   var vars = [], hash;
	      var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
	      for(var i = 0; i < hashes.length; i++)
	      {
	          hash = hashes[i].split('=');
	          vars.push(hash[0]);
	          vars[hash[0]] = hash[1];
	      }
	      return vars;
  };

  _.noop = function(){};
  return _;

});