/**
 *
 * 活动缓存实现文件
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
NEJ.define([
    'base/klass',
    'base/util',
    'util/ajax/xdr',
    'util/cache/abstract'
],function(_k,_u,_j,_t,_p){
    var _pro;
    // 自定义列表缓存
    _p._$$ScheduleCache = _k._$klass();
    _pro = _p._$$ScheduleCache._$extend(_t._$$CacheListAbstract);

    // 实现数据载入逻辑
    _pro.__doLoadList = function(_options){
        var _onload = _options.onload;
        // 补全请求数据，也可在模块层通过cache参数传入
        var _data = _options.data||{};
        _u._$merge(_data,{limit:_options.limit,offset:_options.offset});
        if (_data.list && _data.list.length > 0){
          _onload(_data.list);
          return;
        }
        switch(_options.key){
          case 'scedule-list-data':
                var _url = _options.ext.url;
                // TODO load list from server
                _j._$request(_url,{
                   type:'json',
                   data:_u._$object2query(_data),
                   onload:function(_json){
                       // _json.code
                       // _json.result
                       _onload(_json.code==200?_json.result.list:null);
                   },
                   onerror:_onload._$bind(null)
               });
          break;
          case 'scedule-list-data2':
                  var _url = _options.ext.url;
                  // TODO load list from server
                  _j._$request(_url,{
                     type:'json',
                     data:_u._$object2query(_data),
                     onload:function(_json){
                         // _json.code
                         // _json.result
                         _onload(_json.code==200?_json.result.list:null);
                     },
                     onerror:_onload._$bind(null)
                 });
          break;
          default :
                _onload(_data.list);
          break;
        }
    };
});
