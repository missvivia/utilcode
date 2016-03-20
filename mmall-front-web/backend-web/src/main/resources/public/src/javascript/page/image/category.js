/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{pro}widget/module.js'
        ,'./category/list.js'],
    function(u,v,e,Module,List,p,o,f,r) {
        var pro;

        p._$$CategoryModule = NEJ.C();
        pro = p._$$CategoryModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            var list = new List();
            list.$inject('#category');
        };
        
       
        
       
        
        p._$$CategoryModule._$allocate();
    });