/**
 * 代客下单
 * author liuqing
 *
 */

NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'pro/widget/module',
    '{pro}/widget/util/search.select.js',
    '{pro}components/notify/notify.js',
    './proxylist/proxylist.js?v=1.0.0.1'
  ],
  function(_k,_e,_v,_$$Module,f,notify,List,_p) {
    var _pro;

    _p._$$ProxyModule = _k._$klass(),
    _pro = _p._$$ProxyModule._$extend(_$$Module);

    _pro.__init = function(_options) {
        this.__super(_options);
        /*var _form = f._$allocate({
            form:'search-form',
            onsearch:function(_data){
	        	  if(!_data.searchValue){
	        		  delete _data["searchValue"];
	        	  }
	      	      this.data = _data;
	      	      this.__searchList();
            }._$bind(this)
        });*/
        
        _v._$addEvent('searchBtn','click',this.__searchList._$bind(this));
        _v._$addEvent('searchValue','keyup',this.__onKeyUp._$bind(this));
    };
    
    _pro.__searchList = function(){
  	    var searchValue = _e._$get('searchValue').value,
  	        data = {};
    
  	    if(searchValue == ""){
  	    	notify.show("请输入关键字查找");
  	    	return;
  	    }
  	    
  	    data = {searchValue:searchValue};
  	    
    	if(!this.__list){
    		this.__list = new List({
    			data:{
  				    condition:data
  				}
    		});
    		this.__list.$inject('#proxyList');
    	}else{
    		this.__list.refresh(data);
    	}    	
    };
    
    _pro.__onKeyUp = function(_event){
    	if(_event.keyCode == 13){
    		_e._$get('searchBtn').click();
    		return false;
    	}
    };
    
    _p._$$ProxyModule._$allocate();

  });