/**
 * 商家平台-jit管理——拣货单列表页
 * author：zzj(hzzhangzhoujie@corp.netease.com)
 */

define([ 'base/util'
        ,'base/event'
        ,'base/element'
        ,'util/form/form'
        ,'util/ajax/xdr'
        ,'util/encode/json'
        ,'{pro}widget/module.js'
        ,'{pro}components/jit/pick/pickList.js'
        ,'{pro}widget/datePicker/datePicker.js'
        ,'{pro}components/notify/notify.js'
        ,'pro/components/datepicker/datepicker'],
    function(ut,v,e,f,j,JSON,Module,PickList,dp,notify,DatePicker,p) {
        var pro;
        p._$$PKListModule = NEJ.C();
        pro = p._$$PKListModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__initWidgets();
            this.__addEvent();
            this.__doSubmit();
            
        };
        
        pro.__initWidgets = function(){
            //dp._$$datePickerModule._$allocate({pcon:'searchform'});
            this.__doInitFormCtrl(e._$get('searchform'));
            this.__form = f._$$WebForm._$allocate({
            	form: e._$get('searchform')
            });
           
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

        pro.__addEvent = function(){
            v._$addEvent(e._$get('submitBtn'), 'click', this.__doSubmit._$bind(this));
        };

        pro.__doSubmit = function(_event){
        	var _data = this.__form._$data(),
        		_stime = _data.createStartTime,
        		_etime = _data.createEndTime;
        	if(_stime>_etime){
        		notify.notify({
                    type: "error",
                    message: "开始时间必须小于等于结束时间"
                });
        		return;
        	}
        	_data.createStartTime = ut._$format(_stime,"yyyy-MM-dd");
        	_data.createEndTime = ut._$format(_etime,"yyyy-MM-dd");
        	if(!this.__list){
        		this.__list = new PickList({data:{condition: _data}});
        		this.__list.$inject('#pick-list-box');
        	} else{
        		this.__list.refresh(_data);
        	}
        };

        p._$$PKListModule._$allocate();
    });