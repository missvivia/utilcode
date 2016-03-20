/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',    
    '{pro}/widget/util/search.select.js',
    '{pro}components/datepicker/datepicker.js',
    './model/list.js'
    ],
    function(ut,v,e,Module,f,Datepick,List,p) {
        var pro;

        p._$$modelModule = NEJ.C();
        pro = p._$$modelModule._$extend(Module);
        
        pro.__init = function(_options) {
        	
        	this.__supInit(_options);
        	
        	v._$addEvent('searchValue','keyup',function(_event){
        		if(_event.keyCode == 13){
        			e._$get('searchBtn').click();
        		}
        	});
        	
    	    this.__startTime = new Datepick().$inject('#starttime');
            this.__endTime = new Datepick().$inject('#endtime');
            var _form = f._$allocate({
                form:'search-form',
                onsearch:function(_data){
                	if(!this.__list){
                		//_data["startTime"]=this.__startTime.data.select;
                		//_data["endTime"]=this.__endTime.data.select;
                		this.__list = new List({
                			data:{
    	        				condition:_data
    	        				}
                		});
                		this.__list.$inject(_options.parent||'#modellist');
                	}else{
                		_data["startTime"]=this.__startTime.data.select;
                		_data["endTime"]=this.__endTime.data.select + (24*3600*1000 - 1);
                		this.__list.refresh(_data);
                	}
                }._$bind(this)
            });
            
            
        }

        p._$$modelModule._$allocate();
    });