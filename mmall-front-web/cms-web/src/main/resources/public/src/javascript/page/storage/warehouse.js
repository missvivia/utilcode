/**
 * 仓库统计
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define(['{lib}base/util.js',
        '{lib}base/event.js',
        '{lib}base/element.js',
        '{pro}widget/module.js',
        '{pro}components/storage/warehouse/list.js',
        '{pro}widget/form.js',
        '{pro}components/notify/notify.js'
    ],
    function(_ut,_v,_e,Module,List,_ut0,notify,p) {
        var pro;

        p._$$Module = NEJ.C();
        pro = p._$$Module._$extend(Module);
        
        pro.__init = function(_options) {
            this.__super(_options);
            var _form = _e._$get('search-form');
            _v._$addEvent(_form["type"], 'change', this.__onChangeType._$bind(this));
            _ut0._$$WebForm._$allocate({
            	form:_form,
            	onsubmit:function(data){
            		if(isNaN(data.startTime)){
            			data.startTime =0;
            		}
            		if(isNaN(data.endTime)){
            			data.endTime =2524579200000;
            		}
            		if(data.startTime > data.endTime){
            			 notify.notify({
        	                type: "error",
        	                message: "结束时间不能小于开始时间"
            	         });
            			 return;
            		}
	            	if(!this.__list){
		                this.__list = new List({
		                    data: {condition:data}
		                }).$inject("#m-warehouselist");
		            } else{
		            	this.__list.refresh(data);
		            }
            	}._$bind(this)
            });
        };
        
        pro.__onChangeType = function(_event) {
        	var _node = _v._$getElement(_event);
        	    _selectedVal = _node.options[_node.selectedIndex].value,
        	    _wareNd = _e._$get("warehouse-sel"),
        	    _exportNd = _e._$get("export-ct");
        	//是否隐藏仓库select框
        	if(_selectedVal == "1"){
        		if(!_e._$hasClassName(_wareNd,"f-dn")){
        			_e._$addClassName(_wareNd,"f-dn");
        		}
        	}else{
        		if(_e._$hasClassName(_wareNd,"f-dn")){
        			_e._$delClassName(_wareNd,"f-dn");
        		}
        		
        	}
        	//是否隐藏导出按钮
//        	if(_selectedVal == "5" || _selectedVal == "7"){
//        		if(_e._$hasClassName(_exportNd,"f-dn")){
//        			_e._$delClassName(_exportNd,"f-dn");
//        		}
//        	}else{
//        		if(!_e._$hasClassName(_exportNd,"f-dn")){
//        			_e._$addClassName(_exportNd,"f-dn");
//        		}
//        	}
        	
        };
        
        p._$$Module._$allocate();
    });