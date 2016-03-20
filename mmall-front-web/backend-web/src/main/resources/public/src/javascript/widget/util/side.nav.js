NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'util/event'
],function(_k,_v,_e,_t,_p,_o,_f,_r,_pro){
    /**
     * 查询条件表单控件
     */
    _p._$$SideNav = _k._$klass();
    _pro = _p._$$SideNav._$extend(_t._$$EventTarget);
    /**
     * 重置控件
     * @param {Object} _options
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        _v._$addEvent('nav','click',this.__onNavClick._$bind(this));
    };
    _pro.__onNavClick = function(_event){
    	
    	var closeOpenedNav = function(parent){
    		var opened = _e._$getByClassName(parent,'open');
    		if(opened.length){
    			_e._$delClassName(opened[0],'open')
    		}
    	}
    	var findLiNode = function(_elm){
    		var _liNode = _elm.parentNode;
    		while(!_e._$hasClassName(_liNode,'snav_item')){
    			_liNode = _liNode.parentNode
    		}
    		return _liNode;
    	}
    	var openNav = function(_elm){
    		closeOpenedNav('nav');
    		_e._$addClassName(_elm,'open');
    		var list = _e._$getByClassName(_elm,'glyphicon-chevron-down');
    		if(list.length){
    			_e._$replaceClassName(list[0],'glyphicon-chevron-down','glyphicon-chevron-up');
    		}
    	};
    	var closeNav = function(_elm){
    		_e._$delClassName(_elm,'open');
    		var list = _e._$getByClassName(_elm,'glyphicon-chevron-up');
    		if(list.length){
    			_e._$replaceClassName(list[0],'glyphicon-chevron-up','glyphicon-chevron-down');
    		}
    	}
    	var elm = _v._$getElement(_event);
    	var li  = findLiNode(elm);
    	if(!_e._$hasClassName(li,'active')){
    		if(elm.tagName!='A'||_e._$hasClassName(elm,'j-firstnav')){
    			_v._$stop(_event);
    		}
    		if(elm.tagName=='A'&&elm.className==''){
    			return;
    		}
	    	if(_e._$hasClassName(li,'open')){
	    		closeNav(li)
	    	} else{
	    		openNav(li)
	    	}
    	} else{
    		if(elm.tagName=='LI'||_e._$hasClassName(elm,'j-firstnav')||_e._$hasClassName(elm,'glyphicon')){
    			_v._$stop(_event);
    		}
    	}
    	
    };
    
    
    return _p._$$SideNav;
});
