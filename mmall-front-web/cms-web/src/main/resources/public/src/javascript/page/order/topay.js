/**
 * xx平台首页
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/order/topaylist.js?v=1.0.0.0',
    '{pro}components/datepicker/datepicker.js'],
    function(ut,v,e,Module,TopayList,p) {
        var pro;

        p._$$TopayModule = NEJ.C();
        pro = p._$$TopayModule._$extend(Module);
        
        pro.__init = function(_options) {
            _options.tpl = 'jstTemplate';
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
            this.__topaylist =  new TopayList().$inject('.j-topayform');

        };
        pro.__getNodes = function(){
          
        };
        pro.__addEvent = function(){

        };

        p._$$TopayModule._$allocate();
    });