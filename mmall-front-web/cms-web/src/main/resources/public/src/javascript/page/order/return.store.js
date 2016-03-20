 /**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/order/returnstore.list.js',
    '{pro}widget/form.js'
    ],
    function(_ut,v,e,Module,ReturnList,_ut0,p) {
        var pro;

        p._$$SizeModule = NEJ.C();
        pro = p._$$SizeModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);

            this.__form = _ut0._$$WebForm._$allocate({form:'webForm',
                onsubmit: this.__onSearch._$bind(this)
            })
			// this.__getNodes();
			// this.__addEvent();
        };

        pro.__onSearch = function(data){
            if(!this.__returnList){
                data['lastId'] = 0;
                this.__returnList = new ReturnList({
                    data: {condition:data}
                }).$inject("#return-list-box");
            }else{
                var len = this.__returnList.data.list.length;
                data['lastId'] = this.__returnList.data.list[len-1].id;
                this.__returnList.refresh(data);
            }
        }

        p._$$SizeModule._$allocate();
    });