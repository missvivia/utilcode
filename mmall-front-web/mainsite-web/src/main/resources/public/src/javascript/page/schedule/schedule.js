/*
 * ------------------------------------------
 * 品购页模块
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'util/hover/hover',
    'pro/extend/util',
    'pro/extend/request',
    'pro/widget/module',
    'pro/widget/util/fav',
    'pro/widget/util/countdown',
    'pro/widget/layer/login/login',
    'pro/widget/ui/shop/shop',
    'pro/widget/ui/product/product'
],function(_k,_v,_e,_h,_xu,_req,_m,_w,_y,_yy,_z,_x,_p,_o,_f,_r,_pro){
    /**
     * 品购页模块
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 控件初始化
     * @param {Object} _options
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        var _conf = window.config||_o;
        this.__config = _conf;
        // init product hover action
        _h._$hover(_e._$getByClassName(
            'page-widget-box','j-iprd'
        ));
        // init count down
        var _parent = _e._$get('count-down-box');
        if (!!_parent&&_conf.leftTime>0){
            _y._$countdown(
               'count-down-box',_conf.leftTime,
               '剩余：${dd}天${HH}时${mm}分${ss}秒'
            );
        }
        // init follow action
        var _parent = _e._$get('brand-follow-box');
        if (!!_parent){
            this.__nflow = _parent;
            _v._$addEvent(
                _parent.parentNode,'click',
                this.__doFollowAction._$bind(this)
            );
        }
        // init product list 
        var _parent = _e._$get('product-list-box');
        var _isPreview = (location.href.indexOf('/show')!=-1||location.href.indexOf('/preview')!=-1)?true:false;
        if (!!_parent){
            _x._$$ProductList._$allocate({
                lkey:'schedule',
                parent:_parent,
                scheduleId:_conf.scheduleId,
                showPriced:_conf.showPriced,
                preview:_isPreview,
                onupdate:this.__doLazyRefresh._$bind(this)
            });
        }
        // init shop map
        var _parent = _e._$get('shop-map-box');
        if (!!_parent){
            _z._$$ShopMap._$allocate({
                parent:_parent,
                shops:window.shops
            });
        }
    };
    /**
     * 关注行为
     * @return {Void}
     */
    _pro.__doFollowAction = (function(){
        var _class = 'j-follow';
        var _doFollow = function(_node,_followed){
            var _ntxt = _e._$getSibling(_node);
            if (!_followed){
                _e._$delClassName(_node,_class);
                _ntxt.innerText = '关注品牌';
                _node.innerHTML = '&#xe607;';
            }else{
                _e._$addClassName(_node,_class);
                _ntxt.innerText = '取消关注';
                _node.innerHTML = '&#xe606;';
            }
        };
        var _doShowError = function(){
            // TODO
        };
        return function(){
            // check login
            if (!_xu.isLogin()){
                var _win = _yy._$$LoginWindow._$allocate({
                    parent:document.body
                });
                _win._$show();
                return;
            }
            // follow action
            var _parent = this.__nflow,
                _flwed = _e._$hasClassName(_parent,_class);
            _req(
                !_flwed?'/brand/follow':'/brand/unfollow',{
                    method:'POST',
                    data:{brandId:this.__config.brandId},
                    onload:function(_json){
                        if (_json.code==200){
                            _doFollow(_parent,!_flwed);
                            if (!_flwed){
                                // animation to followed box
                                _w._$doFav(_parent,{duration:1});
                            }
                        }else{
                            _doShowError();
                        }
                    },
                    onerror:function(){
                        _doShowError();
                    }
                }
            );
        };
    })();
    
    // init page
    _p._$$Module._$allocate();
});
