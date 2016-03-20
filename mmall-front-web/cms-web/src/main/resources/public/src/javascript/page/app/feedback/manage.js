/*
 * ------------------------------------------
 * 意见反馈管理
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
    'pro/page/app/feedback/module',
    'pro/widget/form'
  ],
  function(_k,_e,_v,_u,_$$Module,module,_i1,_p,_o,_f,_r) {
    var _pro;

    _p._$$MFeedbackModule = _k._$klass(),
    _pro = _p._$$MFeedbackModule._$extend(_$$Module);

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
        ['doquery','click',this.__doQuery._$bind(this)],
         ["export",'click',this.__export._$bind(this)]
      ]);
      this.__form = _i1._$$WebForm._$allocate({
        form:'form-id'
      });
      this.__doQuery();
    };

    /**
     * 查询
     */
    _pro.__doQuery = function(){
		_data=this.__getData();
		if(_data){
			//todo
	        this.__module.getList(_data);
		}
    };
    
    /**
     * 查询
     */
    _pro.__export = function(){
		_data=this.__getData();
		if(_data){
			//todo
			var _param=_u._$object2query(_data);
			window.open("/app/feedback/export?"+_param);
//			http://miss.163.com/app/feedback/list/?startTime=1408982400000&endTime=1416758400000&areaId=0&system=&version=&key=&limit=100&offset=0
		}
    };

    /**
	 * 获取数据
	 */
    _pro.__getData = function(){
    	var _data = this.__form._$data();
    	_data.system=_data.system==-1?"":_data.system;
    	_data.version=_data.version==-1?"":_data.version;
		console.log(_data);
		return _data;
    };

    _p._$$MFeedbackModule._$allocate({
      data: {}
    });
    return _p;
  });