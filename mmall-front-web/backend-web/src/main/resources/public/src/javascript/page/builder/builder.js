/*
 * ------------------------------------------
 * 构建器模块实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'util/dispatcher/dispatcher',
    'pro/widget/module',
    './cache/widget.js',
    './widget/all.js',
    './util.js'
],function(_k,_e,_t,Module,_wd,_z,_x,_p,_o,_f,_r,_pro){
    /**
     * 页面模块
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(Module);
    /**
     * 模块初始化
     * @param {Object} _options
     */
    _pro.__init = function(_options){
        this.__super(_options);
        this.__setLayout(_options.layout);
        document.mbody = _e._$get(_options.parent);
        _t._$startup({
            rules:{
                rewrite:{
                    404:'/builder/'
                },
                alias:{
                    app:'/builder/',
                    main:'/?/main/',
                    side:'/?/side/',
                    image:'/?/image/',
                    widget:'/?/widget/',
                    product:'/?/product/'
                }
            },
            modules:{
                '/?/side/':'frame/side/module.html',
                '/?/image/':{
                    gid:'side',
                    module:'image/module.html'
                },
                '/?/product/':{
                    gid:'side',
                    module:'product/module.html'
                },
                '/?/widget/':{
                    gid:'side',
                    module:'widget/module.html'
                },
                '/?/main/':'main/module.html',
                '/builder/':{
                    module:'frame/layout/module.html',
                    composite:{
                        side:'/?/side/',
                        main:'/?/main/'
                    }
                }
            }
        });
    };
    /**
     * 保存布局信息
     * @return {Void}
     */
    _pro.__setLayout = function(_data){
        var _layout = [];
        _data = _data||_o;
        // 背景组件
        _layout.push({
            type:'a',
            bgImgId:_data.bgImgId,
            setting:_data.bgSetting||null
        });
        // 带倒计时BANNER组件
        _layout.push({
            type:'b',
            // 背景图
            headerImgId:_data.headerImgId,
            setting:_data.headerSetting||null
        });
        // 自定义组件配置
        var _list = _data.udSetting||null;
        if (!!_list&&_list.length>0){
            _layout.push.apply(_layout,_list);
        }
        // 全部商品组件
        _layout.push({
            type:'c',
            allListPartVisiable:_data.allListPartVisiable!==!1,
            setting:_data.allListPartOthers||null
        });
        // 商店地图组件
        _layout.push({
            type:'d',
            setting:null,
            mapPartVisiable:_data.mapPartVisiable!==!1
        });
        // 缓存布局信息
        _wd._$do(function(_cache){
            _cache._$setLayout({
                id:_data.id,
                layout:_layout,
                brandId:_data.brandId,
                scheduleId:_data.scheduleId
            });
        });
    };
    
    return _p;
});
