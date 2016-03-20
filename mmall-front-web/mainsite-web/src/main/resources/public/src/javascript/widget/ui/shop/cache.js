/*
 * ------------------------------------------
 * 商店店铺列表缓存控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'util/cache/abstract',
    'pro/extend/request'
],function(_k,_u,_t,_req,_p,_o,_f,_r,_pro){
    /**
     * 商店店铺列表缓存控件
     * 
     */
    _p._$$ShopCache = _k._$klass();
    _pro = _p._$$ShopCache._$extend(_t._$$CacheListAbstract);
    /**
     * 从服务器端载入列表，子类实现具体逻辑
     * 
     * @abstract
     * @method   module:util/cache/abstract._$$CacheListAbstract#__doLoadList
     * @param    {Object}   arg0   - 请求信息
     * @property {String}   key    - 列表标识
     * @property {Number}   offset - 偏移量
     * @property {Number}   limit  - 数量
     * @property {String}   data   - 请求相关数据
     * @property {Function} onload - 列表项载入回调
     * @return   {Void}
     */
    _pro.__doLoadList = function(_options){
        var _callback = _options.onload;
        _req('/brand/shops',{
            method:'GET',
            data:_options.data,
            onload:function(_json){
                _callback(_json.result);
            },
            onerror:function(_error){
                _callback(null);
            }
        });
    };
    /**
     * 设置店铺列表
     * @param {Object} _list
     */
    _pro._$setShops = function(_list){
        this._$setListInCache('all',_list||_r);
        // category by city
        var _pcmap = {},
        	_spmap = {};
        _u._$forEach(
            _list,function(_shop){
                var _data = _shop.province,
                    _city = _shop.city,
                	_prvc = _pcmap[_data.id];
                // init province cache
                if (!_prvc){
                	_prvc = {
                		id:_data.id,
                		name:_data.name,
                		list:[]
                	};
                	_pcmap[_data.id] = _prvc;
                }
                // push shop to province list
                var _list = _spmap[_data.id];
                if (!_list){
                	_list = [];
                	_spmap[_data.id] = _list;
                }
                _list.push(_shop);
                // check city
                if (_city.id>0){
                	// push city
                	var _index = _u._$indexOf(
                		_prvc.list,function(_item){
                			return _item.id==_city.id;
                		}
                	);
                	if (_index<0){
                		_prvc.list.push(_city);
                	}
	                // push shop to city list
	                var _lkey = _data.id+'-'+_city.id,
	                	_list = _spmap[_lkey];
	                if (!_list){
	                	_list = [];
	                	_spmap[_lkey] = _list;
	                }
	                _list.push(_shop);
                }
            },this
        );
        //console.log(_pcmap);
        //console.log(_spmap);
        // save list to cache
        _u._$forIn(
            _spmap,function(_list,_lkey){
                this._$setListInCache(_lkey,_list);
            },this
        );
        return this.__setAreaList(_pcmap);
    };
    /**
     * 设置省市列表
     * @return {Void}
     */
    _pro.__setAreaList = function(_map){
    	var _arr = [];
    	_u._$loop(
    		_map,function(_item){
    			_item.list.unshift({
    				id:'',
    				name:'全部'
    			});
    			_arr.push(_item);
    		}
    	);
    	return _arr;
    };
    /**
     * 单次执行动作
     * @param {Object} _func
     */
    _p._$do = function(_func){
        var _ret;
        var _inst = _p._$$ShopCache._$allocate();
        try{
            _ret = _func.call(null,_inst);
        }catch(ex){
            // ignore
        }
        _inst._$recycle();
        return _ret;
    };
    
    return _p;
});