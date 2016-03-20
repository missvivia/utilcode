/*
 * ------------------------------------------
 * 商品展示控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'base/util',
    'base/constant',
    'ui/base',
    'util/template/tpl',
    'util/cache/share',
    '../droper/droper.js',
    '../confirm/confirm.js',
    '../../cache/product.js',
    'text!./product.html'
],function(_k,_v,_e,_u,_g,_i,_l,_d,_x,_z,_y,_html,_p,_o,_f,_r,_pro){
    /**
     * 商品展示控件
     */
    _p._$$Product = _k._$klass();
    _pro = _p._$$Product._$extend(_i._$$Abstract);
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__init = function(){
        this.__dopt = {
            dataset:{},
            clazz:'cwd jx-product',
            title:'删除商品',
            onerror:this.__onError._$bind(this),
            ondrop:this.__onResDrop._$bind(this),
            onremove:this.__onResRemove._$bind(this)
        };
        this.__copt = {
            message:'更换图片？',
            onok:this.__onReplaceImgOK._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = (function(){
        var _keys = {1:'bid',2:'pid'};
        return function(_options){
            this.__super(_options);
            // reset options
            var _match = ''+(_options.match||2),
                _dataset = this.__dopt.dataset;
            _dataset.boxMatch = _match;
            this.__dopt.limit = _options.limit;
            this.__dopt.tip = _options.tip||
                            ((_match==2?'商品图<br/>':'banner<br/>')
                            + this.__dopt.limit.width+'*'
                            + this.__dopt.limit.height);
            this.__dopt.clazz = 'cwd jx-product'+
                (_match.indexOf('1')>=0?' jx-banner':'');
            _u._$forEach(
                _match.split(','),function(_type){
                    _dataset['boxT'+_type] = _options[_keys[_type]]||'';
                },this
            );
            this.__droper = _x._$$Droper._$allocate(this.__dopt);
        };
    })();
    /**
     * 控件清理
     * @return {Void}
     */
    _pro.__destroy = function(){
        this.__doClearComponent();
        this.__doUpdateProduct();
        this.__super();
    };
    /**
     * 初始化外观
     * @return {Void}
     */
    _pro.__initXGui = (function(){
        var _seed_html = _l._$addNodeTemplate(_html);
        return function(){
            this.__seed_html = _seed_html;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - product name
        // 1 - sale price
        // 2 - discount
        // 3 - market price
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__nname = _list[0];
        this.__nsale = _list[1];
        this.__ndsct = _list[2];
        this.__nmkpc = _list[3];
        this.__doUpdateProduct();
        var _body = _e._$getChildren(this.__body)[0];
        this.__dopt.parent = function(_node){
            _body.insertAdjacentElement('afterBegin',_node);
            return _body;
        };
    };
    /**
     * 更新商品信息
     * @return {Void}
     */
    _pro.__doUpdateProduct = function(_product){
        _product = _product||_o;
        var _sale = _product.salePrice||0,
            _mrkt = _product.marketPrice||1000,
            _count = _u._$fixed(_sale/_mrkt*10,1);
        this.__nname.innerText = _product.productName||'商品名称';
        this.__nsale.innerText = '¥'+_sale;
        this.__ndsct.innerText = '('+_count+'折)';
        this.__nmkpc.innerText = '¥'+_mrkt;
    };
    /**
     * 错误回调
     * @return {Void}
     */
    _pro.__onError = function(_error){
        this._$dispatchEvent('onerror',_error);
    };
    /**
     * 更新商品状态
     * @return {Void}
     */
    _pro.__doUpdateProductState = function(_id,_used){
        var _cache = _d.localCache;
        // update cache
        var _data = _cache._$get('products')||{};
        _cache._$set('products',_data);
        _data[_id] = !!_used;
        // trigger state change
        _cache._$set(
            'product-state',{
                id:_id,used:!!_used
            }
        );
    };
    /**
     * 清除放入的资源
     * @return {Void}
     */
    _pro.__onResRemove = function(_event){
        this.__doUpdateProductState(
            _event['2'],!1
        );
        this.__doUpdateProduct();
    };
    /**
     * 放入资源
     * @return {Void}
     */
    _pro.__onResDrop = function(_event){
        var _res = _o;
        if (!!this.__droper){
            _res = this.__droper._$getResource();
        }
        // drop product
        if (_event.type==2){
        	if (!!_res['2']){
        		this.__doUpdateProductState(
    				_res['2'],!1
                );
        	}
            this.__doUpdateProductState(
                _event.id,!0
            );
            // for product
            var _product = _y._$do(function(_cache){
                return _cache._$getItemInCache(_event.id);
            });
            // update product information
            this.__doUpdateProduct(_product);
            // update product banner show
            var _limit = this.__dopt.limit,
                _url = ((_product||_o).listShowPicList||_r)[0]||'';
            _event.value = _url+(_url.indexOf('?')<0?'?':'&')
                         + 'imageView&thumbnail='
                         + _limit.width+'x'+_limit.height;
            _event.nolimit = !0;
            // ignore if banner image setted
            _event.stopped = _res['1'];
            return;
        }
        // drop image
        if (_event.type==1){
            // confirm to replace
            if (!!_res['1']||!!_res['2']){
                _event.cancel = !0;
                var _opt = _u._$merge({
                    data:_event,
                    parent:this.__droper._$getBody()
                },this.__copt);
                this.__confirm = _z.
                    _$$Confirm._$allocate(_opt);
            }
        }
    };
    /**
     * 确认替换图片
     * @param {Object} _event
     */
    _pro.__onReplaceImgOK = function(_event){
        this.__droper._$resumeImage(_event.data);
    };
    
    return _p;
});
