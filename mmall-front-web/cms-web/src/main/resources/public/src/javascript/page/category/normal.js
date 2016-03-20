/**
 * 商品分类管理页
 * author durianskh(shaokehua@xinyunlian.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}/widget/util/search.select.js',
    './normal.list/list.js?v=1.0.0.1',
    ],
    function(ut,v,e,Module,searchForm,List,p) 
    {
        var pro;
        
        p._$$BrandModule = NEJ.C();
        pro = p._$$BrandModule._$extend(Module);
        
        pro.__init = function(_options) 
        {
            this.__supInit(_options);
            this.__list =  new List().$inject('#normallist');
        };
        
        p._$$BrandModule._$allocate();
    });