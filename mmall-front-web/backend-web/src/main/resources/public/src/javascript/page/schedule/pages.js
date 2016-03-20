/**
 * xx平台商务管理——档期列表页
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/po/pagelist.js',
    '{pro}widget/form.js'
    ],
    function(_ut,_v,_e,Module,SizeList,_ut0,p) {
        var pro;

        p._$$SizeModule = NEJ.C();
        pro = p._$$SizeModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            _ut0._$$WebForm._$allocate({
            	form:_e._$get('search-form'),
            	onsubmit:function(_data){
            		_data.status =0;
            		if(!this.__list){
            			this.__list = new SizeList({data:{condition:_data,statusList:statusList}});
            			this.__list.$inject('#m-actlist');
            		} else{
            			this.__list.refresh(_data);
            		}
            	}
            })
        };
        
        

        p._$$SizeModule._$allocate();
    });