/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}/widget/util/search.select.js',
    './brand.list/list.js'
    ],
    function(ut,v,e,Module,searchForm,List,p) {
        var pro;

        p._$$BrandModule = NEJ.C();
        pro = p._$$BrandModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			// init search form
		    // var _form = searchForm._$allocate({
		    //     form:'search-form',
		    //     onsearch:function(_data){
		    //     	if(!this.__list){
		    //     		this.__list = new List({});
		    //     		this.__list.$inject('#brandlist');
		    //     	} else{
		    //     		this.__list.refresh(_data);
		    //     	}
		    //     }._$bind(this)
		    // });
            this.__list =  new List().$inject('#brandlist');
        };
        

        p._$$BrandModule._$allocate();
    });