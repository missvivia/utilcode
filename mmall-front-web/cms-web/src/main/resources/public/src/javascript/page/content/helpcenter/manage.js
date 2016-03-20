/*
 * ------------------------------------------
 * 帮助中心管理
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
    'pro/page/content/helpcenter/module',
    '{lib}util/form/form.js'
  ],
  function(_k,_e,_v,_u,_$$Module,module,_i1,_p,_o,_f,_r) {
    var _pro;

    _p._$$HelpCenterModule = _k._$klass(),
    _pro = _p._$$HelpCenterModule._$extend(_$$Module);

    /**
     * 初始化方法
     * @param  {Object} _options - 配置对象
     * @return {Void}
     */
    _pro.__init = function(_options) {
      this.__supInit(_options);
      this.__module = new module({});
      this.__module.$inject('#module-cnt');
      this.__categoryList=window["g_categoryList"]||[];
      this.__doInitDomEvent([
        ['doquery','click',this.__doQuery._$bind(this)],
        ['category','change',this.__onCategoryChange._$bind(this,{target:_e._$get("category")})],
      ]);
      this.__form = _i1._$$WebForm._$allocate({
        form:'form-id'
      });
    };
    
    /**
	 * 类目切换
	 */
    _pro.__onCategoryChange = function(_obj){
    	var _target=_obj.target;
    	console.log(_target.selectedIndex);
    	var _c_container=_e._$get("c-child");
    	_c_container.innerHTML="";
    	if(_target.selectedIndex>0&&this.__categoryList[_target.selectedIndex-1].children){
    		var _children=this.__categoryList[_target.selectedIndex-1].children,
    		_select=' <select autocomplete ="off" class="form-control" ><option value="-1">请选择</option>';
    		for(var i=0;i<_children.length;i++){
    			var _option="";
    			if(_children[i].id==_obj.cId)
    				_option='<option selected="selected" value="'+_children[i].id+'">'+_children[i].name+'</option>';
    			else
    				_option='<option value="'+_children[i].id+'">'+_children[i].name+'</option>';
    			_select+=_option;
    		}
    		_select+='</select>';
    		_c_container.innerHTML=_select;
    	}
    };
    /**
     * 保存
     */
    _pro.__doQuery = function(){
    	if(this.__form._$checkValidity()){
    		_data=this.__getData();
    		if(_data){
    			//todo
    	        this.__module.getList(_data);
    		}
    	}
    };
    

    /**
	 * 获取数据
	 */
    _pro.__getData = function(){
    	var _data = this.__form._$data();
		var _select=_e._$get("category"),_select2=_e._$get("c-child").getElementsByTagName("select")[0];
		if(_data.publishType==-1&&(_select.selectedIndex==0)){
			 alert("平台跟类目至少选择一项");
			 return null;
		}
		if(_select.selectedIndex>0)
			_data.categoryId=_select.options[_select.selectedIndex].value;
		if(_select2&&_select2.selectedIndex>0)
			_data.categoryId=_select2.options[_select2.selectedIndex].value;
		console.log(_data);
		return _data;
    };
    
    _p._$$HelpCenterModule._$allocate({
      data: {}
    });

    return _p;
  });