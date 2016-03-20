/*
 * ------------------------------------------
 * 品购页模块实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/event',
    'base/element',
    'util/tab/tab',
    'util/toggle/toggle',
    'util/list/waterfall',
    'util/template/tpl',
    'pro/widget/module',
    'pro/extend/util',
    'pro/extend/request',
    'pro/widget/util/countdown',
    'pro/widget/ui/activities/activities',
    'pro/components/notify/notify',
    './cache.js'
],function(_k,_u,_v,_e,_t,_g,_l,_z,_m,_uu,_req,_y,_w,_x,_d,_p,_o,_f,_r,_pro){
    /**
     * 品购页模块
     *
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__config = _options.config||_o;
        this.__mopt = {
            limit:10,
            delta:50,
            sbody:window,
            parent:'product-box',
            item:{
                klass:'product-list',
                noprice:!!this.__config.noprice,
                toFixed:_u._$fixed,
                isSoldout:function(_list){
                    var _sum = 0;
                    _u._$forEach(
                        _list,function(v){
                            if (v.state==3){
                                _sum++;
                            }
                        }
                    );
                    return _sum>=_list.length;
                }
            },
            cache:{
                lkey:'schedule',clear:!0,
                klass:_d._$$ProductCache,
                data:{
                    order:0,desc:true,categoryId:'',
                    scheduleId:this.__config.scheduleId
                }
            },
            onbeforelistload:this.__onProductLoading._$bind(this),
            onafterlistrender:this.__onAfterProductUpdate._$bind(this)
        };
        this.__mask = _e._$create('div','m-pomsk');
        // init event
        this.__doInitDomEvent([[
            'follow-box','click',
            this.__onFollowAction._$bind(this)
        ],[
            'category-box','click',
            this.__onCategoryAction._$bind(this)
        ],[
            this.__mask,'click',
            this.__doCloseCategory._$bind(this)
        ]]);
        // init act show
        var _parent = _e._$get('activity-box'),
            _list = this.__config.alist;
        if (!!_parent&&_list.length>0){
            this.__actshow = _w._$$ActShow._$allocate({
                marquee:!0,
                parent:_parent,
                activities:_list
            });
        }
        // init count down
        var _parent = _e._$get('count-down-box');
        if (!!_parent&&this.__config.leftTime>0){
            _y._$countdown(
               'count-down-box',
               this.__config.leftTime,
               '${dd}天${HH}时${mm}分${ss}秒后结束'
            );
        }
        // init category
        this.__ctab = _t._$$Tab._$allocate({
            selected:'j-selected',
            list:_e._$getChildren('category-box','j-it')
        });
        this.__ctab._$setEvent(
            'onchange',this.__onCategoryChange._$bind(this)
        );
        // init sort type
        var _list = _e._$getChildren('type-box','j-it');
        this.__ttab = _t._$$Tab._$allocate({
            selected:'j-selected',list:_list
        });
        this.__ttab._$setEvent(
            'onchange',this.__onSortTypeChange._$bind(this)
        );
        _g._$toggle(_list[1],{element:_list[1],clazz:'j-toggle'});
        _g._$toggle(_list[2],{element:_list[2],clazz:'j-toggle'});
        // init list
        this.__doRefreshProductList();
    };
    /**
     * 更新商品列表
     * @return {Void}
     */
    _pro.__doRefreshProductList = function(){
        if (!!this.__module){
            this.__module._$recycle();
        }
        this.__module = _l.
            _$$ListModuleWF._$allocate(this.__mopt);
    };
    /**
     * 商品列表加载中提示
     * @return {Void}
     */
    _pro.__onProductLoading = function(_event){
        _event.value = _z._$getTextTemplate('product-loading');
    };
    /**
     * 列表绘制完成事件
     * @return {Void}
     */
    _pro.__onAfterProductUpdate = function(_event){
        this.__doLazyRefresh();
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
                    data:{brandId:this.__config.brandId},
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
    /**
     * 关闭类目选择框
     * @return {Void}
     */
    _pro.__doCloseCategory = function(){
        _e._$removeByEC(this.__mask);
        _e._$addClassName('category-box','f-dn');
        setTimeout(function(){
            _e._$delClassName(this.__mask,'j-show');
            _e._$delClassName('category-box','j-show');
        },0);
    };
    /**
     * 显示类目选择框
     * @return {Void}
     */
    _pro.__doShowCategory = function(){
        document.body.appendChild(this.__mask);
        _e._$delClassName('category-box','f-dn');
        setTimeout(function(){
            _e._$addClassName(this.__mask,'j-show');
            _e._$addClassName('category-box','j-show');
        },0);
    };
    /**
     * 关闭类目选择
     * @param {Object} _event
     */
    _pro.__onCategoryAction = function(_event){
        var _action = _e._$dataset(
            _v._$getElement(_event,'d:action'),'action'
        );
        if (_action=='close'){
            this.__doCloseCategory();
        }
    };
    /**
     * 类目变化事件
     * @param {Object} _event
     */
    _pro.__onCategoryChange = function(_event){
        this.__doCloseCategory();
        this.__mopt.cache.data.categoryId = _event.data||'';
        this.__doRefreshProductList();
    };
    /**
     * 排序方式变化事件
     * @return {Void}
     */
    _pro.__onSortTypeChange = function(_event){
        // save order
        var _data = this.__mopt.cache.data;
        _data.order = _event.data||'';
        _data.desc = true;
        // open category
        if (_event.index==3){
            this.__doShowCategory();
            return;
        }
        // repeat default
        if (_event.index==0&&
            _event.last==0){
            return;
        }
        // reset init toggle state
        if (_event.index!=_event.last){
            var _node = _event.list[_event.last];
            if (_e._$dataset(_node,'toggled')!='true'){
                _e._$delClassName(_node,'j-toggle');
            }else{
                _e._$addClassName(_node,'j-toggle');
            }
        }
        // order type for price/countdown
        var _index = _event.index;
        if (_index==1||_index==2){
            _data.desc = !_e._$hasClassName(
                _event.list[_index],'j-toggle'
            );
        }
        this.__ctab._$go(0,!0);
    };

    // init page
    _p._$$Module._$allocate({
        config:window.config
    });
});