/*
 * ------------------------------------------
 * 图片列表模块
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/element',
    'util/cache/share',
    'util/dispatcher/module',
    'pro/page/builder/module',
    'pro/page/builder/cache/product'
],function(_k,_u,_e,_y,_x,_m,_d,_p,_o,_f,_r,_pro){
    /**
     * 图片列表模块
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
        this.__mopt = {
            limit:15,
            cache:{klass:_d._$$CacheProduct,clear:!0},
            item:{klass:'builder-module-product-list'}
        };
        this.__super({
            tid:'builder-module-product'
        });
        this.__cache = _d._$$CacheProduct._$allocate({
            oncategoryload:this.__onCategoryLoad._$bind(this)
        });
        _y.localCache._$addEvent(
            'oncachechange',
            this.__onProductStateChange._$bind(this)
        );
        // init category list
        this.__cache._$getCategory();
    };
    /**
     * 初始化分类信息
     * @return {Void}
     */
    _pro.__onCategoryLoad = function(_event){
        var _list = _event.list||_r,
            _select = this.__form._$get('categoryId');
        _u._$forEach(
            _list,function(_item){
                _select.add(new Option(_item.name,_item.id));
            }
        );
    };
    /**
     * 商品状态变化事件
     * @param {Object} _event
     */
    _pro.__onProductStateChange = function(_event){
        if ('product-state'!=_event.key){
            return;
        }
        // update state
        var _data = _event.newValue||_o;
            _id = 'side-prd-'+_data.id;
        if (!_data.used){
            _e._$delClassName(_id,'j-disabled');
        }else{
            _e._$addClassName(_id,'j-disabled');
        }
    };
    /**
     * 提交查询条件
     * @param {Object} _data
     */
    _pro.__onSubmit = function(_data){
        this.__mopt.item.cache = 
            _y.localCache._$get('products')||_o;
        // if (!_data.categoryId){
            // delete _data.categoryId;
        // }
        // if (!_data.productName){
            // delete _data.productName;
        // }
        this.__super(_data);
    };
    // regist module
    _x._$regist('product',_p._$$Module);
});
