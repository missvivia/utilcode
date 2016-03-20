/*
 * ------------------------------------------
 * 品牌详情
 * @version  1.0
 * @author   hzzhangweidong(hzzhangweidong@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'pro/widget/module',
    'base/util',
    'pro/components/countdown/countdown',
    'pro/extend/util',
    'pro/components/notify/notify',
    'pro/extend/request',
    'util/template/jst',
    'pro/widget/ui/shop/shop'
//    'pro/widget/ui/shop/shop'
],function(_k,_e,_w,_u,_countdown,_uu,_x,_req,_t,_i,_p,_o,_f,_r){
    var _pro;

    _p._$$BrandStory = _k._$klass();
    _pro = _p._$$BrandStory._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    
    _pro.__init=function(_options){
    	this.__super(_options);
    	this.__bcds = _e._$getByClassName(document,'j-bcd');
    };

    /**
     * 重置方法
     * @param  {Object} _options - 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
//        this.__doInitDomEvent([
//            [this.__nodes[2],'click',this.__goTop._$bind(this)]
//        ]);
    	this.__brandId=window["g_return"].brandInfo.brandId;
        this.__super(_options);
        this.__countDown(_options);
        this.__initShopMap(_options);
        this.__doInitDomEvent([
          [
            'follow-box','click',
            this.__onFollowAction._$bind(this)
        ]
        ]);
    };
    
    _pro.__initXGui = (function(_options){
    	this.__super(_options);
    	 this.__seed_css  = _seed_css2;
        
    });
    
    _pro.__countDown=function(_options){
    	_u._$forEach(this.__bcds,function(_node){
            var _time = _e._$dataset(_node,'countdown');
            var _ct = new _countdown({
                data:{
                    content:'',
                    time:_time,
                    updatetime:60000,
                    onchange:function(_opt){
                        if (_opt.isdown){
                            var _pd = _node.parentNode.parentNode.parentNode.parentNode;
                            _e._$addClassName(_pd,'f-dn');
                        }else{
                            if (_opt.meta.dd == '00' && _opt.meta.HH == '00' && _opt.meta.mm == '00'){
                                _node.innerHTML = _opt.meta.ss + '秒';
                            }else if (_opt.meta.dd == '00' && _opt.meta.HH == '00'){
                                _node.innerHTML = _opt.meta.mm +'分钟';
                            }else if(_opt.meta.dd == '00'){
                                _node.innerHTML = _opt.meta.HH +'小时';
                            }else{
                                _node.innerHTML = _opt.meta.dd +'天';
                            }
                        }
                    }._$bind(_node)
                }
            });
            _ct.$inject(_node);
        }._$bind(this));
    };
    
    /**
     * 关注行为
     * @return {Void}
     */
    _pro.__onFollowAction = (function(){
        var _class = 'j-follow';
        var _doFollow = function(_node,_followed){
            var _ntxt = _e._$getChildren(_node)[1];
            if (!_followed){
                _e._$delClassName(_node,_class);
                _ntxt.innerText = '关注品牌';
            }else{
                _e._$addClassName(_node,_class);
                _ntxt.innerText = '取消关注';
            }
        };
        var _doRedirectLogin = function(){
            location.href = '/login?redirectURL='+encodeURIComponent(location.href);
        };
        var _doShowError = function(_flwed){
            _x.notify((!_flwed?'加入关注失败':'取消关注失败')+'，请重试','fail');
        };
        return function(){
            // check login
            if (!_uu.isLogin()){
                _doRedirectLogin();
                return;
            }
            // follow action
            var _parent = _e._$get('follow-box'),
                _flwed = _e._$hasClassName(_parent,_class);
            _req(
                !_flwed?'/brand/follow':'/brand/unfollow',{
                    method:'POST',
                    data:{brandId:this.__brandId},
                    onload:function(_json){
                        if (_json.code==200){
                            _doFollow(_parent,!_flwed);
                            _x.notify(!_flwed?'已加入关注':'已取消关注');
                        }else{
                            _doShowError(_flwed);
                        }
                    },
                    onerror:function(){
                        _doShowError(_flwed);
                    }
                }
            );
        };
    })();

    _pro.__initShopMap = function (_options) {
        var _shops = window["g_return"].shops || window["g_return"].brandInfo.shops;
        var brandId = window["g_return"].brandId || window["g_return"].brandInfo.brandId;
        if(_shops&&_shops.length){
            this.__map = _i._$$ShopMap._$allocate({
                brandId: brandId,
                parent: "map",
                shops: _shops
            });
        }else{
            _e._$remove('m-box-map',false);
        }

    };
//   _pro.__showMap=function(_options){
//	   this.__map=_pl.__$$ShopMap._$allocate({
//		   parent:"map",
//		   shops:[]
//	   })
//   };
//    /**
//     * 滚动到顶部
//     * @type {[type]}
//     */
//    _pro.__goTop = function(_event){
//        _v._$stop(_event);
//        window.scrollTo(0,0);
//    };

//    _p._$$BrandStory._$allocate({});

    _p._$$BrandStory._$allocate({});
    return _p;
});