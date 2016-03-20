/*
 * ------------------------------------------
 * 固定组件 - 全部商品组件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/event',
    'base/element',
    'util/template/tpl',
    'util/toggle/toggle',
    '../base/widget.js',
    '../../widget.js',
    '../../util.js',
    '../../cache/product.js',
    '../../ui/setting/sort.js',
    '../../ui/setting/order.js',
    '../../ui/setting/background.js',
    'text!./w.html'
],function(_k,_u,_v,_e,_l,_x,_i,_w,_n,_d,_t,_z,_y,_html,_p,_o,_f,_r,_pro){
    /**
     * 固定组件 - 全部商品组件
     * 
     * @class   _$$Widget
     * @extends _$$Widget
     */
    _p._$$Widget = _k._$klass();
    _pro = _p._$$Widget._$extend(_i._$$Widget);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__gopt = {
            clazz:'open',
            ontoggle:this.__onSortTypeToggle._$bind(this)
        };
        this.__topt = {
            parent:document.body,
            onsubmit:this.__doUpdateOrderList._$bind(this,2)
        };
        this.__dopt = {
            parent:document.body,
            onsubmit:this.__doUpdateOrderList._$bind(this,1)
        };
        this.__sopt = {
            parent:document.body,
            onsubmit:this.__doUpdateBackground._$bind(this)
        };
        this.__copt = {
            onsortupdate:this.__doUpdateSortType._$bind(this)
        };
        this.__tbop = {
            title:'商品列表组件',
            acts:[{n:'edit',t:'设置背景'},'view'],
            onactbuild:this.__doInitSortMenu._$bind(this),
            onedit:this.__onBgSetting._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // init event
        this.__doInitDomEvent([[
            this.__nodb,'click',
            this.__onSortTypeChange._$bind(this)
        ],[
            document,'click',
            this.__doCloseSortTypeMenu._$bind(this)
        ]]);
        // init widget
        var _setting = _options.setting||_o;
        this.__cache = _d.
            _$$CacheProduct._$allocate(this.__copt);
        this.__cache._$updateSortType(
            _setting.sortType||0,
            _setting.sortList||''
        );
        // reset data
        this.__doUpdateVisible(_options.allListPartVisiable);
        this.__doUpdateBackground(_setting);
        this.__doUpdateSortType();
    };
    /**
     * 初始化外观
     * @return {Void}
     */
    _pro.__initXGui = (function(){
        var _seed = _l._$parseUITemplate(_html);
        return function(){
            this.__dataset = {boxInsert:'top'};
            this.__seed_css = 'm-bd-widget-fx m-bd-widget-c';
            this.__seed_html = _seed.mdl;
            this.__seed_menu = _seed.menu;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - background show
        var _list = _e._$getByClassName(
            this.__wrap,'j-flag'
        );
        this.__nbgs = _list[0];
    };
    /**
     * 初始化排序菜单
     * @return {Void}
     */
    _pro.__doInitSortMenu = function(_event){
        var _parent = _event.parent;
        _parent.insertAdjacentHTML(
            'afterBegin',_l._$getTextTemplate(
                this.__seed_menu
            )
        );
        // get sort type node
        // 0 - sort type list box
        var _list = _e._$getByClassName(_parent,'j-flag');
        this.__nodb = _list[0];
        this.__nfom = this.__nodb.parentNode;
        _x._$toggle(this.__nfom['btn-sort'],this.__gopt);
    };
    /**
     * 更新是否可见
     * @return {Void}
     */
    _pro.__doUpdateVisible = function(_visible){
        this.__toolbar._$setVisible(!!_visible);
    };
    /**
     * 更新排序方式
     * @return {Void}
     */
    _pro.__doUpdateSortType = function(){
        var _type = this.__cache._$getSortType();
        this.__nfom.orderby[_type||0].checked = !0;
    };
    /**
     * 更新背景设置
     * @return {Void}
     */
    _pro.__doUpdateBackground = function(_options){
        _u._$merge(
            this.__sopt,_u._$fetch({
                opacity:100,
                bgColor:'#FFFFFF'
            },_options)
        );
        _e._$style(
            this.__nbgs,{
                opacity:this.__sopt.opacity/100,
                backgroundColor:this.__sopt.bgColor
            }
        );
    };
    /**
     * 背景设置事件
     * @param {Object} _event
     */
    _pro.__onBgSetting = function(_event){
        _y._$$Background._$allocate(this.__sopt);
    };
    /**
     * 更新类目排序
     * @return {Void}
     */
    _pro.__doUpdateOrderList = function(_type,_event){
        this.__cache._$updateSort({
            type:_type,
            seq:_event.value
        });
    };
    /**
     * 关闭排序选项卡菜单
     */
    _pro.__doCloseSortTypeMenu = function(){
        _e._$delClassName(this.__nfom,this.__gopt.clazz);
    };
    /**
     * 切换排序菜单
     */
    _pro.__onSortTypeToggle = function(_event){
        _v._$stopBubble(_event.event);
    };
    /**
     * 排序方式变化事件
     * @param {Object} _event
     */
    _pro.__onSortTypeChange = function(_event){
        _e._$delClassName(this.__nfom,this.__gopt.clazz);
        var _node = _v._$getElement(_event);
        switch(_node.value){
            case '0':
                this.__cache._$updateSort({type:0});
            break;
            // sort by category
            case '1':
                _z._$$Order._$allocate(this.__dopt);
            break;
            // sort by user define
            case '2':
                _t._$$Sorter._$allocate(this.__topt);
            break;
        }
    };
    /**
     * 取布局信息
     * @return {Object} 布局信息
     */
    _pro._$getLayout = function(){
        return {
            allListPartVisiable:this.__toolbar._$getVisible(),
            allListPartOthers:JSON.stringify({
                sortType:this.__cache._$getSortType(),
                sortList:this.__cache._$getSortList(),
                bgColor:_n._$getColor(this.__nbgs,'backgroundColor'),
                opacity:_n._$getOpacity(this.__nbgs),
                spaceTop:_n._$getNumber(this.__body,'paddingTop')
            })
        };
    };
    
    // regist widget impl
    _w._$regist('c',_p._$$Widget);
    
    return _p;
});
