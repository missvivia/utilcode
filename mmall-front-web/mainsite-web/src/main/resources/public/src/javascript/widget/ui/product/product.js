/*
 * ------------------------------------------
 * 商品列表控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'base/util',
    'base/config',
    'ui/base',
    'ui/pager/pager',
    'util/template/tpl',
    'util/template/jst',
    'util/tab/tab',
    'util/list/page',
    'util/toggle/toggle',
    'util/hover/hover',
    './cache.js',
    'text!./product.css',
    'text!./product.html'
],function(_k,_v,_e,_u,_c,_i,_ip,_l,_x,_y,_z,_w,_h,_d,_css,_html,_p,_o,_f,_r,_pro){
    /**
     * 商品列表控件
     * 
     * @param    {Object} config  - 配置信息
     * @property {Array} category - 类目及数量信息，[{id:1,name:'xxx',count:10},...]
     * 
     */
    _p._$$ProductList = _k._$klass();
    _pro = _p._$$ProductList._$extend(_i._$$Abstract);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = (function(){
        // 3 - sold out
        // 2 - opportunity
        // 1 - for sale
        var _getSoldState = function(_list){
            // for list 
            var _map = {'3':0,'2':0,'1':0};
            _u._$forEach(_list,function(_item){
                var _state = _item.state||1;
                _map[_state]++;
            });
            // check result
            var b = (_list||_r).length,
                a = _map['3'], // sold out
                c = _map['1']; // for sale
            return a>=b?3:(c>0?1:2);
        };
        var _formatPrice = function(_price){
            var _arr = (''+_price).split('.');
            return '<em>'+_arr[0]+'</em><b>.'+
                   ((_arr[1]||'0')+'0').substr(0,2)+'</b>';
        };
        return function(){
            this.__mopt = {
                limit:64,
                item:{
                    toFixed:_u._$fixed,
                    getState:_getSoldState,
                    formatPrice:_formatPrice
                },
                cache:{
                    data:{},clear:!0,
                    klass:_d._$$ProductCache
                },
                pager:{clazz:'u-pager'},
                onbeforelistload:this.__onProductLoading._$bind(this),
                onbeforelistclear:this.__onBeforeProductClear._$bind(this),
                onafterlistrender:this.__onAfterProductUpdate._$bind(this)
            };
            this.__copt = {};
            this.__topt = {event:'mousedown'};
            this.__dopt = {
                oncategoryload:this.__onCategoryLoad._$bind(this)
            };
            this.__super();
        };
    })();
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__mopt.cache.lkey = _options.lkey;
        this.__mopt.cache.data.scheduleId = _options.scheduleId;
        this.__mopt.item.priced = !!_options.showPriced;
        if(_options.preview){
        	this.__mopt.cache.data.preview = _options.preview;
        }
        // init event
        this.__doInitDomEvent([[
            this.__nsbtn,'click',
            this.__onPriceChange._$bind(this)
        ],[
            this.__nform,'enter',
            this.__onPriceChange._$bind(this)
        ]]);
        // init widget
        this.__cache = _d._$$ProductCache._$allocate(this.__dopt);
        this.__ttaber = _y._$$Tab._$allocate(this.__topt);
        this.__ttaber._$setEvent(
            'onchange',this.__onSortTypeChange._$bind(this)
        );
        // show list
        this.__doRefreshCategory();
        this.__doRefreshList();
    };
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        delete this.__copt.list;
        this.__doClearComponent();
        this.__super();
    };
    /**
     * 初始化外观
     * @return {Void} 
     */
    _pro.__initXGui = (function(){
        var _seed_css = _e._$pushCSSText(_css,{
                root:_c._$get('root')
            }),
            _seed_html = _l._$parseUITemplate(_html);
        return function(){
            this.__seed_css  = _seed_css;
            this.__seed_ldg  = _seed_html.dtpl;
            this.__seed_cat  = _seed_html.ctpl;
            this.__seed_html = _seed_html.mtpl;
            this.__mopt.item.css = _seed_css;
            this.__mopt.item.klass = _seed_html.ltpl;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - category list box
        // 1 - sort type box
        // 2 - price search form
        // 3 - seatch button
        // 4 - product list box
        // 5 - pager box
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__ncat = _list[0];
        // sort type
        var _xlist = _e._$getChildren(_list[1]);
        this.__topt.list = _xlist;
        _u._$forEach(
            _xlist,function(_node,_index){
                if (_e._$hasClassName(_node,'j-it')){
                    _w._$toggle(_node,{
                        element:_node,
                        ontoggle:this.__onOrderTypeChange._$bind(this),
                        onbeforetoggle:this.__onToggleCheck._$bind(this)
                    });
                }else{
                    _v._$addEvent(_node,'click',_v._$stop);
                }
            },this
        );
        // TODO toggle sort
        this.__nform = _list[2];
        this.__nsbtn = _list[3];
        this.__mopt.parent = _list[4];
        this.__mopt.pager.parent = _list[5];
    };
    /**
     * 刷新类目
     * @return {Void}
     */
    _pro.__doRefreshCategory = function(){
        this.__cache._$getCategoryCount(
            this.__mopt.cache.data.scheduleId
        );
    };
    /**
     * 刷新列表
     * @return {Void}
     */
    _pro.__doRefreshList = function(_key,_value){
        // update condition
        if (!!_key){
            var _data = this.__mopt.cache.data;
            _data[_key] = _value==null?'':_value;
        }
        // update list
        if (!!this.__lmdl){
            this.__lmdl._$recycle();
        }
        this.__lmdl = _z.
            _$$ListModulePG._$allocate(this.__mopt);
    };
    /**
     * 类目列表载入完成
     * @return {Void}
     */
    _pro.__onCategoryLoad = (function(){
        var _doSum = function(_list){
            var _ret = 0;
            _u._$forEach(
                _list,function(_item){
                    _ret += _item.count||0;
                }
            );
            return _ret;
        };
        return function(_list){
            _list = _list||_r;
            _x._$render(
                this.__ncat,
                this.__seed_cat,{
                    xlist:_list,
                    total:_doSum(_list)
                }
            );
            this.__copt.list = _e._$getByClassName(
                this.__ncat,'j-flag'
            );
            this.__ctaber = _y._$$Tab._$allocate(this.__copt);
            this.__ctaber._$setEvent(
                'onchange',this.__onCategoryChange._$bind(this)
            );
        };
    })();
    /**
     * 类目变化事件
     * @param  {Object} 
     * @return {Void}
     */
    _pro.__onCategoryChange = function(_event){
        _event.stopped = !0;
        if (_event.last==_event.index){
            return;
        } 
        this.__doRefreshList('categoryId',_event.data);
    };
    /**
     * 排序字段变化事件
     * @param {Object} _event
     */
    _pro.__onSortTypeChange = function(_event){
        if (_event.last==_event.index){
            return;
        }
        // set default desc
        var _list = this.__ttaber._$getList(),
            _node = _list[_event.index],
            _data = this.__mopt.cache.data,
            _desc = _e._$dataset(_node,'desc');
        if (!!_desc){
            _data.desc = _desc=='true';
        }else{
            delete _data.desc;
        }
        // clear toggle state
        this.__tcount = 0;
        _e._$delClassName(_list[_event.last],'js-toggle');
        // refresh list
        this.__doRefreshList('order',_event.data);
    };
    /**
     * 升降序变化事件
     * @return {Void}
     */
    _pro.__onOrderTypeChange = function(_event){
        var _isdesc = _e._$dataset(_event.target,'desc')=='true';
        this.__doRefreshList('desc',_isdesc?!_event.toggled:_event.toggled);
    };
    /**
     * 切换状态判断
     * @return {Void}
     */
    _pro.__onToggleCheck = function(_event){
        _v._$stop(_event.event);
        _event.stopped = !this.__tcount;
        this.__tcount = (this.__tcount||0)+1;
    };
    /**
     * 价格变化事件
     * @param {Object} _event
     */
    _pro.__onPriceChange = function(_event){
        _v._$stop(_event);
        var _data = this.__mopt.cache.data;
        _u._$forEach(
            this.__nform.elements,function(_node){
                delete _data[_node.name];
                var _value = _node.value.trim();
                if (!!_value){
                    _value = parseInt(_value);
                    if (!isNaN(_value)){
                        _data[_node.name] = _value;
                    }
                }
            }
        );
        this.__doRefreshList();
    };
    /**
     * 商品列表清除之前事件
     * @param {Object} _event
     */
    _pro.__onBeforeProductClear = function(_event){
        _h._$unhover(_e._$getChildren(
            _event.parent,'j-pitm'
        ));
    };
    /**
     * 商品列表更新后事件
     * @param {Object} _event
     */
    _pro.__onAfterProductUpdate = function(_event){
        // hover product
        _h._$hover(_e._$getChildren(
            _event.parent,'j-pitm'
        ));
        this._$dispatchEvent('onupdate');
    };
    /**
     * 设置加载状态
     * @param {Object} _event
     */
    _pro.__onProductLoading = function(_event){
        _event.value = _l._$getTextTemplate(this.__seed_ldg);
    };
    
    return _p;
});
