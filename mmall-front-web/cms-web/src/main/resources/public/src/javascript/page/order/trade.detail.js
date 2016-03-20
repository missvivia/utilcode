/**
 * xx平台首页
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/order/tradelist.js'],
    function(ut,v,e,Module,PdList,CancelOrderWin,p) {
        var pro;

        p._$$TradeDetailModule = NEJ.C();
        pro = p._$$TradeDetailModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
        };
        pro.__getNodes = function(){
          
        };
        pro.__addEvent = function(){

        };

        p._$$TradeDetailModule._$allocate();
    });