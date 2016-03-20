/*
 * ------------------------------------------
 * 主站首页实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'pro/widget/module',
    'pro/page/order/widget/express/express',
    'pro/extend/request'
],function(_k,_e,_v,_u,_w,_express,_request,_p,_o,_f,_r){
    var _pro;

    _p._$$Express = _k._$klass();
    _pro = _p._$$Express._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
        this.__super();
        this.__box = _e._$get('package-box');
    };

    /**
     * 重置方法
     * @param  {Object} _options - 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__doInitDomEvent([
            [this.__box,'click',this.__onUnfold._$bind(this)]
        ]);
        this.__super(_options);
        this.__parseParams();
        this.__getExpress();
    };

    _pro.__parseParams = function(){
        var _search = location.search,
            _params = _search.split('=')[1];
        this.__nolist = _params.split('|');
        this.__nolist.shift();
    };

    _pro.__getExpress = function(){
        if (!this.__nolist || this.__nolist.length == 0){
            return;
        }
        var _list = [];
        _u._$forEach(this.__nolist,function(_pid){
            if (_pid != 0){
                _list.push(_pid);
            }
        }._$bind(this))
        _u._$forEach(_list,function(_pid,_index){
            var _exp = new _express({
                    data:{
                        pid:_pid,
                        index:_index,
                        length:_list.length
                    }
                })
                _exp.$inject('#package-box');
        }._$bind(this));
    };

    /**
     * [__onUnfold description]
     * @param  {[type]} _event [description]
     * @return {[type]}        [description]
     */
    _pro.__onUnfold = function(_event){
        var _target = _v._$getElement(_event),
            _action = _e._$dataset(_target,'action');
        if (!!_action){
            var _node = _e._$getSibling(_target.parentNode),
                _flag = _e._$hasClassName(_node,'f-dn'),
                _cmap = _flag ? ['f-dn','']:['','f-dn'],
                _pmap = _flag ? ['','up-arrow']:['up-arrow',''];
            _e._$replaceClassName(_node,_cmap[0],_cmap[1]);
            _e._$replaceClassName(_node.parentNode,_pmap[0],_pmap[1]);
        }
    };

    _p._$$Express._$allocate({});

    return _p;
});