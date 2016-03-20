/*
 * ------------------------------------------
 * 商店地图控件
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
    'util/list/page',
    'util/toggle/toggle',
    './cache.js',
    'text!./shop.css',
    'text!./shop.html'
],function(_k,_v,_e,_u,_c,_i,_ip,_l,_x,_z,_y,_d,_css,_html,_p,_o,_f,_r,_pro){
    /**
     * 商店地图控件
     * 
     * @param    {Object} config   - 配置信息
     * @property {Number} brandId  - 品牌名称
     * @property {Array}  citys    - 有品牌店铺的城市列表
     * 
     */
    _p._$$ShopMap = _k._$klass();
    _pro = _p._$$ShopMap._$extend(_i._$$Abstract);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__mopt = {
            limit:5,
            item:{},
            cache:{
                data:{},
                klass:_d._$$ShopCache
            },
            pager:{
                clazz:'u-pager-m',
                number:5,
                label:{
                    prev:'&nbsp;',
                    next:'&nbsp;'
                }
            },
            onafterlistrender:this.__onShopUpdate._$bind(this),
            onbeforelistclear:this.__onShopClear._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // save shops
        var _list = _options.shops,
            _city = _d._$do(function(_cache){
                return _cache._$setShops(_list);
            });
        // init city list
        _x._$render(
            this.__cbox,
            this.__seed_city,{
                xlist:_city
            }
        );
        // init event
        this.__doInitDomEvent([[
            this.__wbox.parentNode,'click',
            this.__onCityChange._$bind(this)
        ],[
            document,'click',
            this.__doHideCitySelect._$bind(this)
        ]]);
        // init map
        this.__map = new BMap.Map(this.__mbox);
        this.__map.enableScrollWheelZoom();
        this.__map.addControl(new BMap.ScaleControl());
        this.__map.addControl(new BMap.NavigationControl());
        this.__map.addControl(new BMap.OverviewMapControl());
        // update shop list
        this.__doChangeCity('');
    };
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
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
            this.__seed_html = _seed_html.mtpl;
            this.__seed_city = _seed_html.ctpl;
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
        // 0 - map box
        // 1 - city show
        // 2 - city list box
        // 3 - list box
        // 4 - page box
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__mbox = _list[0];
        this.__wbox = _list[1];
        this.__cbox = _list[2];
        // city select toggle
        var _parent = this.__wbox.parentNode;
        _y._$toggle(
            _parent,{
                element:_parent
            }
        );
        this.__mopt.parent = _list[3];
        this.__mopt.pager.parent = _list[4];
    };
    /**
     * 切换城市
     */
    _pro.__doChangeCity = function(_city){
        this.__wbox.innerText = _city||'全部';
        var _data = this.__mopt.cache.data;
        if (_city==_data.city){
            return;
        }
        // refresh list
        _data.city = _city;
        this.__mopt.cache.lkey = _data.city||'all';
        if (!!this.__lmdl){
            this.__lmdl._$recycle();
        }
        this.__lmdl = _z.
            _$$ListModulePG._$allocate(this.__mopt);
    };
    /**
     * 隐藏城市选择
     * @return {Void}
     */
    _pro.__doHideCitySelect = function(){
        _e._$delClassName(this.__wbox.parentNode,'js-toggle');
    };
    /**
     * 城市变化事件
     * @param {Object} _event
     */
    _pro.__onCityChange = function(_event){
        _v._$stop(_event);
        // check city select
        var _city = _e._$dataset(
            _v._$getElement(_event,'d:value'),'value'
        );
        if (!_city) return;
        // update city
        this.__doChangeCity(_city=='all'?'':_city);
    };
    /**
     * 清除列表
     * @return {Void}
     */
    _pro.__onShopClear = function(_event){
        this.__mkr = {};
        this.__map.clearOverlays();
        _u._$forEach(
            _e._$getByClassName(
                _event.parent,'j-xit'
            ),function(_node){
                _v._$clearEvent(_node);
            }
        );
    };
    /**
     * 商铺列表更新事件
     * @return {Void}
     */
    _pro.__onShopUpdate = (function(){
        var _tmap = {
            red:{
                width:21,
                height:32,
                offset:0
            },
            blue:{
                width:24,
                height:36,
                offset:-73
            }
        };
        var _doMarker = function(_marker,_type,_index){
            if (!_marker) return;
            var _conf = _tmap[_type];
            _marker.setTop(_type=='blue');
            _marker.setIcon(
                new BMap.Icon(
                    _c._$get('root')+'images/marker_map.png',
                    new BMap.Size(_conf.width,_conf.height),{
                        imageOffset:new BMap.Size(
                            -_index*_conf.width,_conf.offset
                        )
                    }
                )
            );
        };
        var _onMarkerClick = function(_id){
            if (_e._$hasClassName(_id,'js-selected')){
                return;
            }
            // clear
            _u._$forIn(
                this.__mkr,function(_marker,_id){
                    _doMarker(
                        _marker,'red',
                        parseInt(_e._$dataset(_id,'index'))
                    );
                    _e._$delClassName(_id,'js-selected');
                },this
            );
            // select
            _doMarker(
                this.__mkr[_id],'blue',
                parseInt(_e._$dataset(_id,'index'))
            );
            _e._$addClassName(_id,'js-selected');
            // center to coordinate
            var _dd = _e._$dataset(_id,'coordinate')||'',
                _xy = _dd.split(',');
            this.__map.setCenter(new BMap.Point(_xy[0],_xy[1]));
        };
        var _onMarkerHoverIn = function(_id){
            _e._$addClassName(_id,'js-hover');
            _doMarker(
                this.__mkr[_id],'blue',
                parseInt(_e._$dataset(_id,'index'))
            );
        };
        var _onMarkerHoverOut = function(_id){
            _e._$delClassName(_id,'js-hover');
            if (_e._$hasClassName(_id,'js-selected')){
                return;
            }
            _doMarker(
                this.__mkr[_id],'red',
                parseInt(_e._$dataset(_id,'index'))
            );
        };
        return function(_event){
            var _root = _c._$get('root');
            _u._$forEach(
                _e._$getByClassName(
                    _event.parent,'j-xit'
                ),function(_node,_index){
                    var _dd = _e._$dataset(_node,'coordinate')||'',
                        _xy = _dd.split(','),
                        _id = _node.id;
                    // update action
                    _v._$addEvent(
                        _node,'click',
                        _onMarkerClick._$bind(this,_id)
                    );
                    _v._$addEvent(
                        _node,'mouseenter',
                        _onMarkerHoverIn._$bind(this,_id)
                    );
                    _v._$addEvent(
                        _node,'mouseleave',
                        _onMarkerHoverOut._$bind(this,_id)
                    );
                    // update map
                    if (_index==0){
                        this.__map.setCurrentCity(
                            _e._$dataset(_node,'city')||'北京市'
                        );
                        this.__map.centerAndZoom(
                            new BMap.Point(_xy[0],_xy[1]),13
                        );
                    }
                    var _marker = new BMap.Marker(
                        new BMap.Point(_xy[0],_xy[1]),{
                            title:_e._$dataset(_node,'title')
                        }
                    );
                    _doMarker(_marker,'red',_index);
                    _marker.addEventListener(
                        'click',_onMarkerClick._$bind(this,_id)
                    );
                    _marker.addEventListener(
                        'mouseover',_onMarkerHoverIn._$bind(this,_id)
                    );
                    _marker.addEventListener(
                        'mouseout',_onMarkerHoverOut._$bind(this,_id)
                    );
                    this.__mkr[_id] = _marker;
                    this.__map.addOverlay(_marker);
                },this
            );
        };
    })();
    
    return _p;
});
