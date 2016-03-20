/*
 * ------------------------------------------
 * 我的订单首页实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/widget/module',
    'pro/page/order/orderList',
    'pro/page/order/widget/tab/tab',
    'pro/extend/request'
],function(_k,_w,orderList,tab,_request,_p,_o,_f,_r){
    var _pro,
        _baseuri = '/myorder/orderlist';

    _p._$$Index = _k._$klass();
    _pro = _p._$$Index._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
        this.__super();
        this.__baseURI = '';
        this.__orderList = new orderList();
        this.__orderList.$inject('#order-body'||document.body);
        this.__initTab();
    };

    /**
     * [__initTab description]
     * @return {[type]} [description]
     */
    _pro.__initTab = function(){
        this.__tab = new tab();
        this.__tab.$inject('#tab-box');
        this.__tab.$on('change',this.__onTabChange._$bind(this));
        var mystate = location.hash.split('=')[1];
        this.__tab.go(mystate||0);
        this.__tab.$update();
    };
    /**
     * 切换tab
     * @param  {[type]} _index [description]
     * @return {[type]}        [description]
     */
    _pro.__onTabChange = function(_index){
        location.hash = '#state=' + _index;
        this.__orderList.url = _baseuri;
        this.__orderList.setCondition({
            search:'',
            type:_index
        });
    };

    _p._$$Index._$allocate({});

    return _p;
});