/**
 * 首页商品推荐
 * author liuqing
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}/extend/util.js',
    '{pro}widget/module.js',
    '{lib}util/chain/NodeList.js'
    ],
    function(ut,v,e,_,Module,$,p) {
        var pro;

        p._$$recommendationModule = NEJ.C();
        pro = p._$$recommendationModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
        }
        p._$$recommendationModule._$allocate();
    });