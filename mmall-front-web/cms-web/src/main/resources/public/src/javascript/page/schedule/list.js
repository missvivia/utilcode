/*
 * ------------------------------------------
 * 档期列表页面
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    '{pro}/widget/form.js',
    './schedule.list/list.js',
    '{pro}widget/module.js'
],function(ut,List,Module,_p,_o,_f,_r){
	var pro;
	_p._$$ListModule = NEJ.C();
	pro = _p._$$ListModule._$extend(Module);
	
	pro.__init = function(_options) {
		this.__supInit(_options);
		
		
	    
	 // init search form
	    var _form = ut._$$WebForm._$allocate({
	        form:'search-form',
	        onsubmit:function(_data){
	        	if(!this.__list){
	        		this.__list = new List({data:{condition:_data}});
	        		this.__list.$inject('#schedulelist');
	        	} else{
	        		this.__list.refresh(_data);
	        	}
	        }._$bind(this)
	    });
    };
    
    _p._$$ListModule._$allocate();
});
