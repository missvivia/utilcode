/**
 * 发货单详情页
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    'util/ajax/xdr',
    "{pro}components/notify/notify.js",
    '{pro}components/supply/skuListInInvoice.js',
    'pro/components/jit/outModal'
    ],
    function(_ut,_v,_e,Module,_j,notify,SkuListInInvoice,OutModal,p) {
        var pro;

        p._$$InvoiceDetailModule = NEJ.C();
        pro = p._$$InvoiceDetailModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			      this.__skuList = window["g_skuList"]||[];
            if(this.__skuList.length >0 ){
              this.__skuListInInvoice = new SkuListInInvoice({
                    data: {lists:this.__skuList}
                }).$inject("#skuListInInvoice-box");

            }
            this.__confirmBtn = _e._$get('confirmBtn');
            _v._$addEvent(this.__confirmBtn, 'click', this.__onConfirmBtnClick._$bind(this,this.__confirmBtn));
        };

        pro.__onConfirmBtnClick = function(_btn, _event){
            var _id = _e._$attr(_btn,'data-id');
            var modal = new OutModal();
        	modal.$on('confirm', function () {
        		_j._$request('/supply/confirm.json', {
                    query: {id:_id},
                    data:{t:+new Date},
                    method:'get',
                    type: 'json',
                    onload: function(_data){
                      if(_data &&_data.result){
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
                    }._$bind(this)
                });
        	}._$bind(this));
        };

        

        p._$$InvoiceDetailModule._$allocate();
    });