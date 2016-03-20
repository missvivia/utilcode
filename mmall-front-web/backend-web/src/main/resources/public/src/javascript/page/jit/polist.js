/**
 * 商家平台-jit管理——po列表页
 * author：zhengff(hzzhengff@corp.netease.com)
 */

define([ '{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'util/form/form'
        ,'util/ajax/xdr'
        ,'util/encode/json'
        ,'{pro}widget/module.js'
        ,'{pro}widget/list/po.list.js'
        ,'pro/components/datepicker/datepicker'
        ,'{pro}components/notify/notify.js'],
    function(ut,v,e,f,j,JSON,Module,polist,DatePicker,notify,p) {
        var pro;
        p._$$POListModule = NEJ.C();
        pro = p._$$POListModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__getNodes();
            this.__addEvent();
            
            //dp._$$datePickerModule._$allocate({pcon:'searchform'});
            this.__doInitFormCtrl(e._$get('searchform'));
        	this.__form = f._$$WebForm._$allocate({form: e._$get('searchform')});
        	this.__onSearchBtnClick();
        };
        
        pro.__getNodes = function(){
        };
        
        pro.__addEvent = function(){
        	v._$addEvent(e._$get('searchBtn'),'click',this.__onSearchBtnClick._$bind(this));
        };
        
        pro.__doInitFormCtrl = function(_form){
            // init datepick
            ut._$forEach(
                e._$getByClassName(_form,'j-datepick'),
                function(_parent){
	                var _name = e._$dataset(_parent,'name'),
	                    _value = e._$dataset(_parent,'value'),
	                    _time = e._$dataset(_parent,'time'),
	                    _picker = new DatePicker({
	                        data:{name:_name,select:_value,time:_time}
	                    });
	                _picker.$inject(_parent);
	                _form[_name].defaultValue = _value;
                	
                }._$bind(this)
            );
        };
        
        pro.__onSearchBtnClick = function(event){
        	v._$stop(event);
        	var _data = this.__form._$data(),
	    		_stime = _data.createStartTime,
	    		_etime = _data.createEndTime,
	    		_stime1 = _data.saleStartTime,
	    		_etime1 = _data.saleEndTime;
	    	if(_stime>_etime || _stime1>_etime1){
	    		notify.notify({
	                type: "error",
	                message: "开始时间必须小于等于结束时间"
	            });
	    		return;
	    	}
	    	_data.createStartTime = ut._$format(_stime,"yyyy-MM-dd");
	    	_data.createEndTime = ut._$format(_etime,"yyyy-MM-dd");
	    	_data.saleStartTime = ut._$format(_stime1,"yyyy-MM-dd");
	    	_data.saleEndTime = ut._$format(_etime1,"yyyy-MM-dd");
        	if(this.polist){
	        	polist._$$POList._$recycle(this.polist);
	            this.polist = polist._$$POList._$allocate({
	                node:e._$get('polist'), 
	                pager:e._$get('pager'), 
	                dataForm:_data
	            });
	            this.polist.mdl._$refreshWithClear();
        	}else{
        		this.polist = polist._$$POList._$allocate({
        			node:e._$get('polist'), 
	                pager:e._$get('pager'), 
	                dataForm:_data
                });
        	}
        };
        
        p._$$POListModule._$allocate();
});