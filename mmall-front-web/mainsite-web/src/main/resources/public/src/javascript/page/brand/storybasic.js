
/*
 * ------------------------------------------
 * 品牌故事基本模块
 * @version  1.0
 * @author   hzzhangweidong(hzzhangweidong@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'pro/extend/util',
    'pro/widget/module',
    'util/es/array',
    'pro/widget/ui/cycler/story/cycler',
    'pro/widget/ui/shop/shop',
    'util/chain/chainable',
    'pro/extend/config',
    'pro/page/brand/merchant/merchant',
    'pro/components/notify/notify',
    'pro/widget/layer/login/login',
    "pro/components/schedule/scheduleList",
    'util/ajax/xdr'
], function (_k,_e,_v, _,_m, _a, _t, _i, $,config,Merchant,notify,login,_pl, _j, _p, _o, _f, _r, _pro) {
    /**
     * 品牌介绍页从backend过来
     *
     * @class   _$$StoryBasic
     * @extends _$$StoryBasic
     */
    _p._$$StoryBasic = _k._$klass();
    _pro = _p._$$StoryBasic._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function (_options) {
        this.__super(_options);
        this.__initCycle(_options);
        this.__initPoList(_options);
        this.__initNextPoList(_options);
        this.__initShopMap(_options);
        this.__initMerchant(_options);
        this.__bindEvents(_options);
    };

    _pro.__initCycle = function (_options) {
        var basic = window["g_return"].basic || window["g_return"].brandInfo.basic;
        var _list = basic.maxImages;
        var _imageList = [];
        _list.forEach(function (item) {
            if (item.src) {
                _imageList.push(item);
            }
        });
        this.__mainfade = _t._$$StoryCycle._$allocate({
            list: _imageList,
            parent: "maxImages"
        });
    };

    _pro.__initPoList=function(_options){
        if(window["g_return"].polist&&window["g_return"].polist.length){
            this.__poList=_pl._$$ScheduleList._$allocate({
                parent:$(".m-onsale")[0],
                limit:100,
                list:window["g_return"].polist,
                lkey:"scedule-list-data",
                onscroll:this.__doLazyRefresh._$bind(this)
            })
        }else{
            $("#list-box")._$remove();
        }

    };

    _pro.__initNextPoList=function(_options){
        if(window["g_return"].nextpolist&&window["g_return"].nextpolist.length){
            this.__nextPoList=_pl._$$ScheduleList._$allocate({
                parent:$(".m-next-onsale")[0],
                limit:100,
                isNext:true,
                list:window["g_return"].nextpolist,
                lkey:"scedule-list-data2",
                onscroll:this.__doLazyRefresh._$bind(this)
            })

        }else{
            $("#next-list-box")._$remove();
        }

    };

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
            $("#m-box-map")._$remove();
        }

    };

    _pro.__initMerchant=function(_options){
    	if(window["g_return"].businessInfo){
    		var _bi=window["g_return"].businessInfo;
    		this.__merchant = new Merchant({data:_bi});
    		this.__merchant.$inject("#g-bd .m-winshow","after");
    	}


    };

    _pro.__bindEvents = function () {
    	var that=this;
        var brandId = window["g_return"].brandId||window["g_return"].brandInfo.brandId,fav= $(".j-obsess"),unfav=$(".u-btn4-1");
        fav._$on("click", function (_event) {
        	_v._$stop(_event);
        	if(!(_.isLogin())){
        		login._$$LoginWindow._$allocate({parent:document.body})._$show();
        		return;
        	}
            var _obj = {brandId: brandId};
            _j._$request("/brand/follow", {
                headers: {
                    "Content-Type": "application/json;charset=UTF-8"
                },
                method:'post',
                type:"json",
                data:JSON.stringify(_obj),
                onload:function(_data) {
                    if(_data&&_data.code ==200)
                        fav._$insert2(unfav,"after");
                },
                onerror:function(_error) {
                	 notify.notify({
                         type: "error",
                         message: _error.message || '关注失败'
                       });
                }

            });
        })
        unfav._$on("click",function(_event){
        	_v._$stop(_event);
        	if(!(_.isLogin())){
        		login._$$LoginWindow._$allocate({parent:document.body})._$show();
        		return;
        	}
            var _obj = {brandId: brandId};
            _j._$request("/brand/unfollow", {
                headers: {
                    "Content-Type": "application/json;charset=UTF-8"
                },
                method:'post',
                type:"json",
                data:JSON.stringify(_obj),
                onload:function(_data) {
//                    if(_data&&_data.code ==200)
                    unfav._$insert2(fav,"after");
                },
                onerror:function(_error) {
                	 notify.notify({
                         type: "error",
                         message: _error.message || '取消失败'
                       });
                }
            });
        })

        $("body")._$on("click",function(_event){
        	if(_e._$hasClassName(_event.target,"m-mask")){
        		that.__merchant.hideBox();
        	}
        });
    }

    // init page
    return _p._$$StoryBasic;
});
