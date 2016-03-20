/* 
 * ------------------------------------------
 * 帮助中心
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'util/dispatcher/dispatcher',
    'pro/widget/module'
],function(_k,_e,_t,_m,_p,_o,_f,_r,_pro){
    /**
     * 页面模块
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 模块初始化
     * @param {Object} _options
     */
    _pro.__init = function(){
        this.__super();
        document.mbody = _e._$get('m-helpcenter-box');
        _t._$startup({
            rules:{
                rewrite:{
                    404:'/help/index/'
                },
                title:{
                    '/help/index/':'帮助中心-首页',
                    '/help/articlelist/':'帮助中心-文章列表',
                    '/help/resultlist/':'帮助中心-搜索结果'
                    
                },
                alias:{
                	'sidenav':'/?/sidenav/',
                	'search':'/?/search/',
                    
                    'app':'/help',
                    'index':'/help/index/',
                    'articlelist':'/help/articlelist/',
                    'resultlist':'/help/resultlist/'
                }
            },
            modules:{
            	'/?/sidenav/':'sidenav/module.html',
            	'/?/search/':'search/module.html',
                
                '/help':{
                	module:'layout/module.html',
                    composite:{
                    	sidenav:'/?/sidenav/',
                    	search:'/?/search/'
                    }
                },
                '/help/index/':'index/module.html',
                '/help/articlelist/':'articlelist/module.html',
                '/help/resultlist/':'resultlist/module.html',
                
            }
        });
    };
   
    
    _p._$$Module._$allocate();
});
