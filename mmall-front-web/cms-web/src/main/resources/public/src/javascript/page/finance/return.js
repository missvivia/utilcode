/**
 * cms平台退款查询
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}components/finance/returnlist.js',
    '{pro}widget/module.js',
    '{pro}components/datepicker/datepicker.js'],
    function(ut,v,e,returnForm,Module,p) {
        var pro;

        p._$$ReturnModule = NEJ.C();
        pro = p._$$ReturnModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
						this.__getNodes();
						this.__addEvent();
        };
        pro.__getNodes = function(){
          
        };
        pro.__addEvent = function(){

        };

        p._$$ReturnModule._$allocate();
    });