/*
 * ------------------------------------------
 * 消息管理
 * @version  1.0
 * @author   xiangwenbin(xiangwenbin@corp.netease.com)
 * ------------------------------------------
 */

NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'pro/widget/module',
    'pro/page/app/pmessage/module',
    'pro/widget/form'
  ],
  function(_k,_e,_v,_u,_$$Module,module,_i1,_p,_o,_f,_r) {
    var _pro;

    _p._$$PMessageModule = _k._$klass(),
    _pro = _p._$$PMessageModule._$extend(_$$Module);

    /**
     * 初始化方法
     * @param  {Object} _options - 配置对象
     * @return {Void}
     */
    _pro.__init = function(_options) {
      this.__supInit(_options);
      this.__module = new module({});
      this.__module.$inject('#module-cnt');
      this.__doInitDomEvent([
        ['doquery','click',this.__doQuery._$bind(this)]
      ]);
      this.__form = _i1._$$WebForm._$allocate({
        form:'form-id'
      });
    };

    /**
     * 查询
     * @return {[type]} [description]
     */
    _pro.__doQuery = function(){
		_data=this.__getData();
		if(_data){
			//todo
	        this.__module.getList(_data);
		}
    };
    
    /**
	 * 获取数据
	 */
    _pro.__getData = function(){
    	var _data = this.__form._$data();
    	_data.areaId=_data.areaId==0?"":_data.areaId;
    	_data.os=_data.os==-1?"":_data.os;
		console.log(_data);
		return _data;
    };
    
    _p._$$PMessageModule._$allocate({
      data: {}
    });

    return _p;
  });