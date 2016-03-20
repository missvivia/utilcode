/*
 * ------------------------------------------
 * 焦点图管理页
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
    'pro/page/focuspicture/manage',
    'pro/widget/form',
    'util/template/jst',
    'pro/extend/request'
  ],
  function(_k,_e,_v,_u,_$$Module,manage,_i1,_t,_request,_p,_o,_f,_r) {
    var _pro;

    _p._$$IndexModule = _k._$klass(),
    _pro = _p._$$IndexModule._$extend(_$$Module);

    /**
     * 初始化方法
     * @param  {Object} _options - 配置对象
     * @return {Void}
     */
    _pro.__init = function(_options) {
      this.__supInit(_options);
      this.__manage = new manage({});
      this.__manage.$inject('#module-cnt');
      this.__dcity = _e._$get('dcity');
      this.__citys = _e._$get('citys');
      this.__form = _i1._$$WebForm._$allocate({
        form:'form-id'
      });
      _t._$add('template-box');
      this.__getCitys();
    };

    _pro.__getCitys = function(){
      _request('/focuspicture/getProvinceList',{
        onload:function(_json){
          if (_json.code == 200){
            var _list = _json.result,
                _html = _t._$get('template-box',{result:_list});
            if (!_list || !_list.length){
                return;
            }
            this.__citys.innerHTML = _html;
            this.__dcity.innerText =  _list[0].areaName;
            this.__doInitDomEvent([
              ['doquery','click',this.__doQuery._$bind(this)],
              ['city','change',this.__onChangeCity._$bind(this)]
            ]);
          }
        }._$bind(this),
        onerror:function(){

        }
      })

    }

    /**
     * 改变城市
     * @return {Void}
     */
    _pro.__onChangeCity = (function(){
        var _findTxt = function(_value,_select){
            var _txt;
            _u._$forIn(_select,function(_option){
                if (_option.value == _value){
                    _txt = _option.innerText;
                    return true;
                }
            });
            return _txt;
        };
        return function(_event){
            var _select = _v._$getElement(_event);
            var _value = _select.value;
            this.__dcity.innerText = _findTxt(_value,_select);
        }
    })();

    /**
     * 查询焦点图
     * @return {[type]} [description]
     */
    _pro.__doQuery = function(_event){
      _event.preventDefault();
      var _data = this.__form._$data(),
          _cityId = _data.city,
          _qtime = _data.qtime;
      this.__manage.getList(_cityId,_qtime);
    };

    _p._$$IndexModule._$allocate({
      data: {}
    });

    return _p;
  });