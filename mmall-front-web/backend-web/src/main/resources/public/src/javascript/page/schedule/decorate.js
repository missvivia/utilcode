/*
 * ------------------------------------------
 * 构建器页面入口实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    '../builder/builder.js',
    '../builder/cache/image.js',
    '../builder/cache/product.js'
],function(_u,_m,_di,_dp){
    var _config = window.config,
        _layout = _config.layout,
        _lconf = _u._$fetch({
            id:0,brandId:0,scheduleId:0
        },_layout);
    // init category
    _di._$do(function(_cache){
        _cache._$setCategory(_config.imageCategory||[]);
        _cache._$setLayoutId(_lconf);
        _cache._$push(_layout.mapPartOthers);
    });
    _dp._$do(function(_cache){
        _cache._$setLayoutId(_lconf);
    });
    // init data
    var _images = _config.images;
    if (!!_images&&_images.length){
        _di._$do(function(_cache){
            _cache._$merge(_images);
        });
    }
    var _products = _config.products;
    if (!!_products&&_products.length){
        _dp._$do(function(_cache){
            _cache._$merge(_products);
        });
    }
    // init page
    _m._$$Module._$allocate({
        layout:_layout,
        parent:document.body
    });
});
