/**
 * xx平台活动编辑——商品添加页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
    '{pro}widget/module.js', 
    '{pro}components/coupon/list.js?v=1.0.0.1'
    ],
    function(Module, CouponList, e, c) {
        var pro, 
          $$CustomModule = NEJ.C(),
          pro = $$CustomModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__audit = window["g_audit"];
            this.__addEvent();
        };
        
        pro.__addEvent = function(){
        	this.__list = new CouponList({
                data: {
                	audit:this.__audit
                }
             }).$inject(".j-it");
        	
        };


        $$CustomModule._$allocate({
          data: {}
        });

        return $$CustomModule;
    });