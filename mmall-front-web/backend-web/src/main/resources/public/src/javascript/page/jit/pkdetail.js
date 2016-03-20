/**
 * 拣货单详情页
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    "{pro}widget/layer/sure.window/sure.window.js",
    "{pro}components/notify/notify.js",
    'util/ajax/xdr',
    '{pro}widget/module.js',
    '{pro}components/jit/skuListInPick.js'
    ],
    function(_ut,_v,_e,SureWin,notify,_j,Module,SkuListInPick,p) {
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
            this.__confirmPick=_e._$get('confirmPick');
            if(!!this.__confirmPick){
                _v._$addEvent(this.__confirmPick, 'click', this.__onConfirmPick._$bind(this));
            }
        };
        pro.__onConfirmPick=function(){
            var pkId= _e._$attr(this.__confirmPick, 'data-id');
            SureWin._$allocate({text:'确定仓库已发货？',title:'确认',onok:function(){
                _j._$request('/jit/pkUpdate/'+pkId, {
                    type: 'json',
                    headers:{ 'Accept':'application/json'},
                    onload: function(_json){
                        if(_json.code==200){
                            notify.show('操作成功');
                            location.reload();
                        }else{
                            notify.show('操作失败');
                        }
                    }._$bind(this),
                    onerror: function(error) {
                        notify.show('操作失败');
                    }
                });
            }._$bind(this)})._$show();
        };
        pro.__onExportBtnClick = function(){
            _j._$request('/jit/addexportone.json', {
                type: 'json',
                method: 'get',
                data:{t:+new Date},
                query: {
                    id: _e._$attr(this.__exportBtn, 'data-id')
                },
                onload: function(data){
                	if(data &&data.result){
                  	  notify.notify({
                            type: "success",
                            message: "操作成功, 2秒后刷新页面"
                        });
                        setTimeout(function(){window.location.reload();},2000);
                    }else{
                  	  notify.notify({
                            type: "error",
                            message: "操作失败，请稍再试"
                        });
                        
                    }
                }._$bind(this),
                onerror: function(error) {}
            });
        }
        

        p._$$PickDetailModule._$allocate();
    });