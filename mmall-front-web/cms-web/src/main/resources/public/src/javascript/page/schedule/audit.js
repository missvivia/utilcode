/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{pro}widget/module.js'
        ,'{pro}widget/ui/calendar/calendar.js'
        ,'./audit.list/list.js'
        ,'{pro}page/audit/widget/tab/tab.js'
        ,'{pro}/widget/util/search.select.js'],
    function(ut,v,e,Module,Calendar,List,Tab,searchForm,p,o,f,r) {
        var pro;
        
        p._$$AuditModule = NEJ.C();
        pro = p._$$AuditModule._$extend(Module);
        
        pro.__init = function(_options) {
        	_options.onchange = this.__onChange._$bind(this);
        	
            this.__supInit(_options);
			this.__index =2;
			// init search form
		    this.__form = searchForm._$allocate({
		        form:'search-form',
		        onsearch:function(_data){
		        	if(this.__list){
		        		this.__list.refresh(
		        			ut._$merge(_data,{status:this.__index})
		        		);
		        	} else{
		        		this.__list = new List({
		        			data:{
		        				condition:ut._$merge(_data,{status:this.__index})
		        				}
		        		});
		        		this.__list.$inject('#auditlist');
		        	}
		        }._$bind(this)
		    });
			
		    v._$addEvent(e._$get("btn-query"),"click",function(){
		    	this.__list.refresh(ut._$merge(
	        			this.__form._$data(),{status:this.__index}
	        	));
		    }._$bind(this));
        };
        
        pro.__onChange = function(_index){
        	this.__index = (_index==0?2:3);
        	this.__list.refresh(ut._$merge(
        			this.__form._$data(),{status:this.__index}
        	));
        }
        p._$$AuditModule._$allocate();
    });