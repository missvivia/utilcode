/**
 * 商家平台-jit管理——发货单列表页
 * author：zzj(hzzhangzhoujie@corp.netease.com)
 */

define([ 'base/util'
        ,'base/event'
        ,'base/element'
        ,'util/form/form'
        ,'util/ajax/xdr'
        ,'{pro}widget/module.js'
        ,'{pro}widget/list/supply/invoice.list.js'
        ,'{pro}widget/datePicker/datePicker.js'],
    function(ut,v,e,f,j,Module,inlist,dp,p) {
        var pro;
        p._$$InvoiceListModule = NEJ.C();
        pro = p._$$InvoiceListModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__addEvent();
            this.__initWidgets();
            
        };
        

        pro.__initWidgets = function(){
            //this.__inlist = inlist._$$InvoiceList._$allocate({node:e._$get('inlist'), pager:e._$get('pager')});
            dp._$$datePickerModule._$allocate({pcon:'searchform'});
            this.__form = f._$$WebForm._$allocate({form: e._$get('searchform')});
           
        };

        pro.__addEvent = function(){
            var _node = e._$get('confirmBtn');
            v._$addEvent(e._$get('submitBtn'), 'click', this.__doSubmit._$bind(this));
            
        };

        pro.__doSubmit = function(_event){
            if(this.__inlist){
                inlist._$$InvoiceList._$recycle(this.__inlist);
                this.__inlist = inlist._$$InvoiceList._$allocate({
                    node:e._$get('inlist'), 
                    pager:e._$get('pager'), 
                    dataForm:this.__form._$data()
                });
                this.__inlist.__mdl._$refreshWithClear();
            }else{
                this.__inlist = inlist._$$InvoiceList._$allocate({node:e._$get('inlist'), pager:e._$get('pager'), dataForm:this.__form._$data()});
            }
            
        };


        
        p._$$InvoiceListModule._$allocate();
    });