/*
 * ------------------------------------------
 * 登录页
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'pro/widget/module',
    'base/element',
    'util/tab/tab',
    './brand/brand.js?v=2015083111'
],function(_k,_ut,_m,_e,_t,BrandList,ScheduleList,_p,_o,_f,_r){
    /**
     * 页面模块基类
     * 
     * @class   _$$Module
     * @extends _$$Module
     */
	var _pro;
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__brandBox = _e._$get('brand');
  //      this.__scheduleBox = _e._$get('schedule');
//        var _tab = _t._$$Tab._$allocate({
//                     list:_e._$getChildren(_e._$get('tab')),
//                     selected:'active',
//                     index:index,
//                     onchange:this.__onTabChange._$bind(this)
//                 });
        if(!this.__brandList){
			this.__brandList = new BrandList();
			this.__brandList.$inject('#brand');
		} else{
			this.__brandList.refresh();
		}
    };
    
//    _pro.__onTabChange = function(event){
//    	var index= event.index;
//    	if(index==0){
//    		_e._$delClassName(this.__brandBox,'f-dn');
//    		_e._$addClassName(this.__scheduleBox,'f-dn');
//    		if(!this.__brandList){
//    			this.__brandList = new BrandList();
//    			this.__brandList.$inject('#brand');
//    		} else{
//    			this.__brandList.refresh
//    		}
//    	} else{
//        		_e._$addClassName(this.__brandBox,'f-dn');
//        		_e._$delClassName(this.__scheduleBox,'f-dn');
//        		if(!this.__scheduleList){
//        			this.__scheduleList = new ScheduleList();
//        			this.__scheduleList.$inject('#schedule');
//        		} else{
//        			this.__scheduleList.refresh
//        		}
//    	}
//    }
    // init page
    
    // 设置side选中状态
    _pro.__setActive = function (classs) {
    	 var sideList = document.getElementById("m-side").getElementsByTagName("li");
    	 for ( var i = 0; i < sideList.length; i++) {
    		 if (sideList[i].getAttribute("class") == classs) {
    			 sideList[i].className = sideList[i].className + " active";
    		 }
    	 }
    };
    _pro.__setActive("follow");
    
    
    
    _p._$$Module._$allocate();
});