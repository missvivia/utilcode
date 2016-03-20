/**
 * xx平台商务管理——档期列表页
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/po/polist.js',
    '{pro}widget/form.js',
    '{pro}components/notify/notify.js'
    ],
    function(_ut,_v,_e,Module,ScheduleList,_ut0,notify,p) {
        var pro;

        p._$$SizeModule = NEJ.C();
        pro = p._$$SizeModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            _ut0._$$WebForm._$allocate({form:_e._$get('search-form'),
            	onsubmit:function(data){
	            	if(!this.__sizeList){
		                this.__sizeList = new ScheduleList({
		                    data: {condition:data}
		                }).$inject("#m-actlist");
		            } else{
		            	if(!this.__isDateOK(data)){
		            		notify.show('请检查搜索时间')
		            		return;
		            	}
		            	this.__sizeList.refresh(data)
		            }
            	}._$bind(this)
            })
        };
        pro.__isDateOK = function(_data){
        	var dateArr = ['create','start','end'];
        	for(var i=0,l=dateArr.length;i<l;i++){
        		if(_data[dateArr[i]+'Begin']&&_data[dateArr[i]+'Stop']){
        			if(_data[dateArr[i]+'Begin']>_data[dateArr[i]+'Stop']){
        				return false;
        			}
        		}
        	}
        	return true;
        };
        p._$$SizeModule._$allocate();
    });