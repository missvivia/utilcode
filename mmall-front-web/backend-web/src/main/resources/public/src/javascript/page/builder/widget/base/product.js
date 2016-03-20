/*
 * ------------------------------------------
 * 自定义组件 - 商品组件基类
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/element',
    'util/sort/horizontal',
    '../../cache/product.js',
    '../../ui/droper/droper.js',
    '../../ui/product/product.js',
    '../../util.js',
    './widget.js'
],function(_k,_u,_e,_t,_d,_y,_x,_z,_i,_p,_o,_f,_r,_pro){
    /**
     * 自定义组件 - 商品组件基类
     * 
     * @class   _$$Product
     * @extends _$$Widget
     */
    _p._$$Product = _k._$klass();
    _pro = _p._$$Product._$extend(_i._$$Widget);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__plist = [];
        this.__ropt = {
            clazz:'prd-'+_u._$uniqueID(),
            placeholder:_e._$create('div','j-holder'),
            thumbnail:_e._$create('div','m-bd-dragger'),
            onthumbupdate:this.__onSortThumbUpdate._$bind(this),
            onholderupdate:this.__onSortHolderUpdate._$bind(this)
        };
        this.__tbop = {
            title:'商品组件',
            acts:['sort','remove']
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        var _bids = _options.bannerIds||_r,
            _pids = _options.productIds||_r;
        // init banner show
        if (!!this.__bopt){
            this.__bopt.dataset.boxT1 = _bids.shift()||'';
            this.__bdrop = 
                _y._$$Droper._$allocate(this.__bopt);
        }
        // build product show
        for(var i=0,l=this.__popt.count,_conf;i<l;i++){
            _conf = _u._$merge({},this.__popt);
            _conf.bid = _bids[i]||'';
            _conf.pid = _pids[i]||'';
            _conf.clazz = this.__ropt.clazz;
            this.__plist.push(
                _x._$$Product._$allocate(_conf)
            );
        }
        // init sortable
        this.__sorter = _t.
            _$$HSortable._$allocate(this.__ropt);
    };
    /**
     * 控件回收
     * @return {Void}
     */
    _pro.__destroy = function(){
        _u._$reverseEach(
            this.__plist,function(_inst,_index,_list){
                _inst._$recycle();
                _list.splice(_index,1);
            }
        );
        this.__super();
    };
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // get banner parent
        var _list = _e._$getByClassName(this.__wrap,'j-banner');
        if (!!_list&&_list.length>0){
            this.__bopt.clazz = 'jx-banner';
            this.__bopt.parent = _list[0];
            this.__bopt.dataset = {boxMatch:1};
            this.__bopt.onerror = this.__onError._$bind(this);
        }
        // get product list parent
        var _list = _e._$getByClassName(this.__wrap,'j-product')||_r;
        this.__ropt.parent = this.__wrap;
        this.__popt.parent = _list[0]||this.__wrap;
        this.__popt.onerror = this.__onError._$bind(this);
    };
    /**
     * 取布局信息
     * @return {Object} 布局信息
     */
    _pro._$getLayout = (function(){
        var _dumpIDs = function(_list,_type){
            var _arr = [];
            _u._$forEach(
                _list,function(_node){
                    _arr.push(parseInt(_e._$dataset(
                        _node,'boxT'+_type
                    ))||0);
                }
            );
            return _arr;
        };
        return function(){
            return {
                spaceTop:_z._$getNumber(this.__body,'paddingTop'),
                bannerIds:_dumpIDs(_e._$getByClassName(this.__wrap,'jx-banner'),1),
                productIds:_dumpIDs(_e._$getByClassName(this.__wrap,'jx-product'),2)
            };
        };
    })();
    /**
     * 排序缩略图
     * @param {Object} _event
     */
    _pro.__onSortThumbUpdate = function(_event){
        // TODO
    };
    /**
     * 排序占位符
     * @param {Object} _event
     */
    _pro.__onSortHolderUpdate = function(_event){
        // TODO
    };
    
    return _p;
});
