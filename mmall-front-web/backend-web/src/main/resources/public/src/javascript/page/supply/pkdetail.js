/**
 * 拣货单详情页
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    "{pro}components/notify/notify.js",
    'util/ajax/xdr',
    '{pro}widget/module.js',
    '{pro}components/supply/skuListInPick.js'
    ],
    function(_ut,_v,_e,notify,_j,Module,SkuListInPick,p) {
        var pro;

        p._$$PickDetailModule = NEJ.C();
        pro = p._$$PickDetailModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__skuList = window["g_skuList"]||[];
            if(this.__skuList.length >0 ){
                this.__skuListInPick = new SkuListInPick({
                    data:{lists:this.__skuList}
                }).$inject("#skuListInPick-box");
            }
           
            this.__exportBtn = _e._$get('exportBtn');
            _v._$addEvent(this.__exportBtn, 'click', this.__onExportBtnClick._$bind(this));
        };

        pro.__onExportBtnClick = function(){
            _j._$request('/supply/addexportone.json', {
                type: 'json',
                method: 'get',
                data:{t:+new Date},
                query: {
                    id: _e._$attr(this.__exportBtn, 'data-id')
                },
                onload: function(data){
                    if(data.result){
                        notify.show('操作成功！');
                    }else{
                        notify.showError('操作失败，请稍后再试！');
                    }
                }._$bind(this),
                onerror: function(error) {}
            });
        }
        

        p._$$PickDetailModule._$allocate();
    });