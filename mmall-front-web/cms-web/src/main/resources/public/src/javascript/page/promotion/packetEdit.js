/**
 * xx平台活动编辑——商品添加页
 * author jinze@corp.netease.com
 */

define([
    "pro/extend/util",
    'pro/widget/module',
    'pro/components/promotion/packetEdit'
    ],
    function(_, Module, e, c) {
        var pro, 
          $$CustomModule = NEJ.C(),
          pro = $$CustomModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__addEvent();
        };
        
        pro.__addEvent = function(){};

        $$CustomModule._$allocate({
            data: window['__data__']
        });

        return $$CustomModule;
    });