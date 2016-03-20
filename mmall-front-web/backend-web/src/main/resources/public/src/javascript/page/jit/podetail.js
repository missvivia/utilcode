/**
 * 商家平台-jit管理——po详情页
 * author：hzzhengff(hzzhengff@corp.netease.com)
 */

define([ '{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'util/form/form'
        ,'util/ajax/xdr'
        ,'util/encode/json'
        ,'{pro}widget/module.js'
        ,'{pro}widget/list/posku.list.js'
        ,'{pro}widget/datePicker/datePicker.js'],
    function(ut,v,e,f,j,JSON,Module,posku,dp,p) {
        var pro;
        p._$$PODetailModule = NEJ.C();
        pro = p._$$PODetailModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__getNodes();
            this.__addEvent();
            
            dp._$$datePickerModule._$allocate({pcon:'searchform'});
            this.form = f._$$WebForm._$allocate({form: e._$get('searchform')});
            this.__onSearchBtnClick();
        };
        
        pro.__getNodes = function(){
        	var list = e._$getByClassName(document.body,'ztag'), i = 0;
            this.pkId = list[i++];
            this.invoiceId = list[i++];
            this.pkStatus = list[i++];
            this.invoiceStatus = list[i++];
            this.pkTime1 = list[i++];
            this.pkTime2 = list[i++];
            this.invoiceTime1 = list[i++];
            this.invoiceTime2 = list[i++];
            this.schBtn = list[i++];
            this.exportBtn = list[i++];
        };
        
        pro.__addEvent = function(){
        	v._$addEvent(this.schBtn,'click',this.__onSearchBtnClick._$bind(this));
        	v._$addEvent(this.exportBtn,'click',this.__onExportBtnClick._$bind(this));
        };
        
        pro.__onSearchBtnClick = function(event){
            if(this.posku){
            	posku._$$POSKU._$recycle(this.posku);
                this.posku = posku._$$POSKU._$allocate({
                    node:e._$get('poskulist'), 
                    pager:e._$get('pager'), 
                    dataForm:this.form._$data()
                });
                this.posku.mdl._$refreshWithClear();
            }else{
                this.posku = posku._$$POSKU._$allocate({
                    node:e._$get('poskulist'), 
                    pager:e._$get('pager'), 
                    dataForm:this.form._$data()
                });
            }
        };
        
        pro.__onExportBtnClick = function(event){
        	window.open('/jit/podetail/export?'+ut._$object2query(this.form._$data()));
        };

        p._$$PODetailModule._$allocate();
    });