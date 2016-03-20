/*
 * ------------------------------------------
 * 侧栏目录模块
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'util/dispatcher/module',
    'pro/page/helpcenter/module',
    'pro/page/helpcenter/widget/sideNav'
],function(_k,_v,_e,_x,_m,SideNav,_p,_o,_f,_r,_pro){
    /**
     * 布局模块
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 构建模块
     * @return {Void}
     */
    _pro.__doBuild = function(){
        this.__super({
            tid:'helpcenter-module-sidenav'
        });
        
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        
    };
    /**
     * 显示模块
     * @return {Void}
     */
    _pro.__onShow = function(_options){
        this.__super(_options);
        var _categoryId = _options.param.categoryId||"",
        _subCategoryId = _options.param.subCategoryId||"";
        if(!this.__sideNav){
        	this.__sideNav = new SideNav({data:{
        		selCateId:_categoryId,
        		selSubCateId:_subCategoryId
        	}
        	}).$inject('#help-sdnav');
        }
    };
    
    /**
     * 刷新模块
     * @return {Void}
     */
    _pro.__onRefresh = function(_options){
        this.__super(_options);
        var _categoryId = _options.param.categoryId||"",
            _subCategoryId = _options.param.subCategoryId||"";
        if(!!this.__sideNav){
    		this.__sideNav.$update(function(data){
    			data.selCateId = _categoryId;
    			data.selSubCateId = _subCategoryId;
    		});
        }
    };
    
    // regist module
    _x._$regist('sidenav',_p._$$Module);
});
