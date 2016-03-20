/**
 * xx平台首页
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/datepicker/datepicker.js',
    '{pro}components/finance/queryform.js'],
    function(ut,v,e,Module,Datepick,QueryCnt,p) {
        var pro;

        p._$$SalequeryModule = NEJ.C();
        pro = p._$$SalequeryModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
        };
        pro.__getNodes = function(){
          
        };
        pro.__addEvent = function(){

        };

        p._$$SalequeryModule._$allocate();
    });