/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js'
    ],
    function(ut,v,e,Module,p) 
    {
        var pro;

        p._$$PublishSuccessModule = NEJ.C();
        pro = p._$$PublishSuccessModule._$extend(Module);
        
        pro.__init = function(_options){ 
            this.__supInit(_options);
        };
        
        p._$$PublishSuccessModule._$allocate();
    });