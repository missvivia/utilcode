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
        ,'{pro}widget/list/supply/picking.list.js'
        ,'{pro}widget/datePicker/datePicker.js'],
    function(ut,v,e,f,j,JSON,Module,pklist,dp,p) {
        var pro;
        p._$$PKListModule = NEJ.C();
        pro = p._$$PKListModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__initWidgets();
            this.__addEvent();
            
        };
        
        pro.__initWidgets = function(){
            //this.__pklist = pklist._$$PickingList._$allocate({node:e._$get('pklist'), pager:e._$get('pager')});
            dp._$$datePickerModule._$allocate({pcon:'searchform'});
            this.__form = f._$$WebForm._$allocate({form: e._$get('searchform')});
           
        };

        pro.__addEvent = function(){
            v._$addEvent(e._$get('submitBtn'), 'click', this.__doSubmit._$bind(this));
        };

        pro.__doSubmit = function(_event){
            if(this.__pklist){
                pklist._$$PickingList._$recycle(this.__pklist);
                this.__pklist = pklist._$$PickingList._$allocate({
                    node:e._$get('pklist'), 
                    pager:e._$get('pager'), 
                    dataForm:this.__form._$data()
                });
                this.__pklist.__mdl._$refreshWithClear();
            }else{
                this.__pklist = pklist._$$PickingList._$allocate({
                    node:e._$get('pklist'), 
                    pager:e._$get('pager'),
                    dataForm:this.__form._$data()
                });
            }
            
        }

        p._$$PKListModule._$allocate();
    });