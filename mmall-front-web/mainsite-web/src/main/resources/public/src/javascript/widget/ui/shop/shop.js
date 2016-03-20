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
    'util/selector/cascade',
    './cache.js',
    'text!./shop.css',
    'text!./shop.html'
],function(_k,_v,_e,_u,_c,_i,_ip,_l,_x,_z,_y,_w,_d,_css,_html,_p,_o,_f,_r,_pro){
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
    	this.__copt = {
    		onchange:this.__onAreaChange._$bind(this)
    	};
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
        var _list = _options.shops;
        this.__copt.data = _d._$do(
        	function(_cache){
	            return _cache._$setShops(_list);
	        }
	    );
        // init map
        var _map = new AMap.Map(this.__mbox);
        _map.plugin([
            'AMap.Scale',
            'AMap.ToolBar',
            'AMap.OverView'
        ],function(){
            _map.addControl(new AMap.Scale());
            _map.addControl(new AMap.ToolBar());
            _map.addControl(new AMap.OverView());
        });
        this.__map = _map;
        // update shop list
        this.__selector = _w.
        	_$$CascadeSelector._$allocate(this.__copt);
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
        // 1 - province selector
        // 2 - city selector
        // 3 - list box
        // 4 - page box
        var _list = _e._$getByClassName(
            this.__body,'j-flag'
        );
        this.__mbox = _list[0];
        this.__copt.select = [_list[1],_list[2]];
        this.__mopt.parent = _list[3];
        this.__mopt.pager.parent = _list[4];
    };
    /**
     * 区域变化事件
     * @return {Void}
     */
    _pro.__onAreaChange = function(_event){
    	//console.log(_event);
    	var _prvc = _event['map-p'],
    	    _city = _event['map-c'];
    	this.__mopt.cache.lkey = 
    		_prvc+(!_city?'':'-')+_city;
        if (!!this.__lmdl){
            this.__lmdl._$recycle();
        }
        this.__lmdl = _z.
            _$$ListModulePG._$allocate(this.__mopt);
    };
    /**
     * 清除列表
     * @return {Void}
     */
    _pro.__onShopClear = function(_event){
        this.__mkr = {};
        this.__map.clearMap();
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
                new AMap.Icon({
                    image:_c._$get('root')+'images/marker_map.png',
                    size:new AMap.Size(_conf.width,_conf.height),
                    imageOffset:new AMap.Pixel(-_index*_conf.width,_conf.offset)
                })
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
            this.__map.setCenter(new AMap.LngLat(_xy[0],_xy[1]));
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
                        this.__map.setCity(
                            _e._$dataset(_node,'city')||'北京市'
                        );
                    }
                    var _marker = new AMap.Marker({
                        map:this.__map,
                        title:_e._$dataset(_node,'title'),
                        position:new AMap.LngLat(_xy[0],_xy[1])
                    });
                    _doMarker(_marker,'red',_index);
                    AMap.event.addListener(
                        _marker,'click',
                        _onMarkerClick._$bind(this,_id)
                    );
                    AMap.event.addListener(
                        _marker,'mouseover',
                        _onMarkerHoverIn._$bind(this,_id)
                    );
                    AMap.event.addListener(
                        _marker,'mouseout',
                        _onMarkerHoverOut._$bind(this,_id)
                    );
                    this.__mkr[_id] = _marker;
                },this
            );
        };
    })();
    
    return _p;
});
