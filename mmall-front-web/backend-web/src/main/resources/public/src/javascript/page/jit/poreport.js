/**
 * 商家平台-jit管理——po单报表页
 * author：hzzhengff(hzzhengff@corp.netease.com)
 */

define([ '{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{pro}widget/module.js'
        ,'{pro}widget/form.js'
        ,'{pro}components/jit/poreportList.js'
        ,'{pro}components/notify/notify.js'
        ],
    function(ut,v,e,Module,ut0,SizeList,notify,p) {
        var pro;
        p._$$POReportModule = NEJ.C();
        pro = p._$$POReportModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__initWidgets();
        };
        pro.__initWidgets = function(){
            this.__form = ut0._$$WebForm._$allocate({
            	form:e._$get('searchform'),
            	onsubmit:this.__onSubmit._$bind(this)
            });
        };
        pro.__onSubmit = function(_data){
        	var _stime = ut._$var2date(_data.createStartTime),
    			_etime = ut._$var2date(_data.createEndTime),
	        	_stime1 = ut._$var2date(_data.saleStartTime),
				_etime1 = ut._$var2date(_data.saleEndTime);
	    	if(_stime>_etime || _stime1>_etime1){
	    		notify.notify({
	                type: "error",
	                message: "开始时间必须小于等于结束时间"
	            });
	    		return;
	    	}
	    	
        	if(!this.__list){
        		this.__list = new SizeList({data:{condition:_data}});
        		this.__list.$inject('#size-list-box');
        	} else{
        		this.__list.refresh(_data);
        	}
        };
        
        p._$$POReportModule._$allocate();
});