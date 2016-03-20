/*
 * --------------------------------------------
 * 项目内工具函数集合，此页面尽量写注释
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * --------------------------------------------
 */
define([
    '{lib}base/element.js',
    '{lib}util/query/query.js',
    '{lib}base/util.js',
    '{lib}util/ajax/rest.js',
    '{lib}util/cache/cookie.js',
    '{pro}widget/ui/warning/warning.js',
    '{pro}extend/request.js'
    ], function(e, e1, u, j,cookie,Warning,request) {

  var _ = {},
    noop = function(){};


  // 类型判断， 同typeof 
  _.typeOf = function (o) {
    return o == null ? String(o) : ({}).toString.call(o).slice(8, -1).toLowerCase();
  }

  /**
   * 选择第一个符合选择器的父类元素
   * @param  {Node} node         初始节点
   * @param  {String} selector   选择其，如p.m-tag
   * @param  {Boolean} onlyOne   只需要第一个匹配. 默认true. 为false时，返回为一数组
   * @return {Node|Array}        
   *
   * @example
   *  _.findParent(node, "form[action=xx]", false)
   */
  _.findParent = function( node, selector ,onlyOne ){
    if(typeof onlyOne === "undefined") onlyOne = true;
    var parent = node.parentNode, matches = [];
    while( parent ){
      if( nes.matches( node, selector ) ){

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

  /**
   * 获取search参数
   * @return {Object} 获取search参数
   */
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


  _.findInList = function(id, list, ident){
    ident = ident || "id";
    var len = list.length;
    for(; len--;){
      if(list[len][ident] == id) return len
    }
    return -1;
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
        if(list2[i][ident] != null&&list2[i][ident] === list[len][ident]){
          list.splice(len, 1, _.merge(list2[i],list[len]));
          break;
        }
      }
    }
  }
  // 深度clone
  _.clone = function(obj){
    var type = _.typeOf(obj);
    switch(type){
      case "object": 
        var cloned = {};
        for(var i in obj){
          cloned[i] = _.clone(obj[i])
        }
        return cloned;
      case 'array':
        return obj.map(_.clone);
      default:
        return obj;
    }
    return obj;
  }

  _.showError = (function(){
	  var warning;
	  return function(text){
		  if(warning){
			  warning = warning._$recycle();
		  }
		  warning = Warning._$getInstance({text:text||''});
	  }
  })();



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

  /**
   * 判断是否登陆
   * 
   * @return {Boolean}
   */
  _._$isLogin = function() {
    return cookie._$cookie("XYLBACKENDSESS") != "";
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
    var _userName = decodeURIComponent(cookie._$cookie("XYLBACKENDUN")).replace(/(^\"*)|(\"*$)/g, '')||"",
        _indexOfUser = _userName.indexOf('|');
        
    if (_indexOfUser > 0) {
      _userName = _userName.substring(0, _indexOfUser)+"";
      
    }
    return _userName||"";
  };

  _._$uploadImage2Category = function(list,categoryId,callback){
    callback = callback || noop;
	  var data ={};
	  if(categoryId){
		  data.categoryId = parseInt(categoryId);
	  }
	  data.list = list;
	  request('/image/upload',{
		  data:data,
		  method:'post',
		  onload:function(){
			  console.log('uploaded to category');
              if(callback)
			     callback();
		  },
		  onerror:function(){
			  console.log('uploaded to category error');
			  callback();
		  }
	  });
  };
  
  _._$image = function(url,width,height){
	    return url+'?imageView&thumbnail='+width+'x'+height;
  };
  return _;

	
})