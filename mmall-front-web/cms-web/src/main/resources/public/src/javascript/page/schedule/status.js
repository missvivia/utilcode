/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{pro}widget/module.js'
        ,'./status/list.js'
        ,'pro/components/notify/notify'
        ,'{pro}/widget/form.js'],
    function(ut,v,e,Module,List,notify,ut1,p,o,f,r) {
        var pro;
        
        p._$$StatusModule = NEJ.C();
        pro = p._$$StatusModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			// init search form
		    this.__form = ut1._$$WebForm._$allocate({
		        form:'search-form',
		        onsubmit:function(_data){
		        	_data.flag = 0;
		        	if(this.__list){
		        		if((_data.endDate - _data.startDate)/(1000*60*60*24)>60){
		        			notify.show('搜索时间间隔太长')
		        		} else if((_data.endDate - _data.startDate)<0){
		        			notify.show('搜索时间有误')
		        		} else{
			        		this.__list.refresh(
			        			ut._$merge(_data,{status:this.__index})
			        		);
		        		}
		        	} else{
		        		this.__list = new List({
		        			data:{condition:_data}
		        		});
		        		this.__list.$inject('#module-cnt');
		        	}
		        }._$bind(this)
		    });
			
        };
        
        p._$$StatusModule._$allocate();
    });