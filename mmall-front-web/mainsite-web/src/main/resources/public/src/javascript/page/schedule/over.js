/*
 * ------------------------------------------
 * 品购过期模块
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'pro/extend/util',
    'pro/extend/request',
    'pro/widget/module',
    'pro/widget/util/fav',
    'pro/widget/layer/login/login'
],function(_k,_v,_e,_xu,_req,_m,_w,_yy,_p,_o,_f,_r,_pro){
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
        this.__config = window.config||_o;
        _v._$addEvent(
            'brand-follow-box','click',
            this.__doFollowAction._$bind(this)
        );
    };
    /**
     * 关注行为
     * @return {Void}
     */
    _pro.__doFollowAction = (function(){
        var _class = 'j-follow';
        var _doFollow = function(_node,_followed){
            if (!_followed){
                _e._$delClassName(_node,_class);
                _node.innerText = '关注品牌';
            }else{
                _e._$addClassName(_node,_class);
                _node.innerText = '取消关注';
            }
        };
        var _doShowError = function(){
            // TODO
        };
        return function(_event){
            _v._$stop(_event);
            // check login
            if (!_xu.isLogin()){
                var _win = _yy._$$LoginWindow._$allocate({
                    parent:document.body
                });
                _win._$show();
                return;
            }
            // follow action
            var _parent = _e._$get('brand-follow-box'),
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
