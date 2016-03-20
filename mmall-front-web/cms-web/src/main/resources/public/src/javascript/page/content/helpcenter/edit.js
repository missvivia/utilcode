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
    'pro/widget/editor/editor',
    'pro/widget/module',
    '{lib}util/form/form.js',
    '{pro}extend/request.js',
    '{pro}components/notify/notify.js'
  ],
  function(_k,_e,_v,_u,_temp,_$$Module,_i1,_request,_notify,_p,_o,_f,_r) {
    var _pro,dw = NEJ.P('dd.widget');
    _p._$$HelpCenterEditModule = _k._$klass(),
    _pro = _p._$$HelpCenterEditModule._$extend(_$$Module);

    /**
     * 初始化方法
     * @param  {Object} _options - 配置对象
     * @return {Void}
     */
    _pro.__init = function(_options) {
      this.__supInit(_options);
      this.__categoryList=window["g_categoryList"]||[];
      this.__helpArticle=window["g_helpArticle"]||null;
      this.__doInitDomEvent([
        ['save','click',this.__onSave._$bind(this)],
        ['release','click',this.__onRelease._$bind(this)],
        ['cancel','click',this.__onCancel._$bind(this)],
        ['category','change',this.__onCategoryChange._$bind(this,{target:_e._$get("category")})],
        
      ]);
     this.__editor=dw._$$Editor._$allocate({
          parent:_e._$get('editor'),
          focus:false,
          clazz: 'editor',
          content:this.__helpArticle?this.__helpArticle.content:""
      });
      this.__form = _i1._$$WebForm._$allocate({
        form:'form-id'
      });
	  this.__initEidtArticle();
    };
    
    /**
	 * 设置编辑文章
	 */
    _pro.__initEidtArticle = function(){
    	if(this.__helpArticle){
			//设置子类目
			if(this.__helpArticle.parentCategoryId>0){
				this.__onCategoryChange({target:_e._$get("category"),cId:this.__helpArticle.categoryId});
			}
			this.__form._$setValue("title",this.__helpArticle.title);
			this.__form._$setValue("keywords",this.__helpArticle.keywords);
			this.__editor._$setContent(this.__helpArticle.content);
			if(this.__helpArticle.publishType==3){
				var _plateforms=this.__form.__form["publishType"];
				_plateforms[0].checked="checked";
				_plateforms[1].checked="checked";
			}else
				this.__form._$setValue("publishType",this.__helpArticle.publishType);
    	}else{
    		this.__form._$reset();
    	}
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
    _pro.__onSave = function(){
    	if(this.__form._$checkValidity()){
    		_data=this.__getData();
    		if(_data){
    	      _request('/content/helpcenter/article/save',{
		      		data:_data,
		      		method:'POST',
		      		type:'json',
		      		onload:function(_json){
		      			if(_json&&_json.code==200){
		      				_notify.show("保存成功");
		      				window.setTimeout(function(){window.location.reload();}, 1000);
		      			}
		      		},onerror:function(){
		      			_notify.showError("保存失败");
		      		}
      			});
    		}
    	}
    };
    
    /**
     * 发布
     */
    _pro.__onRelease = function(){
    	if(this.__form._$checkValidity()){
    		_data=this.__getData();
    		if(_data){
    			_request('/content/helpcenter/article/savepublish',{
		      		data:_data,
		      		method:'POST',
		      		type:'json',
		      		onload:function(_json){
		      			if(_json&&_json.code==200){
		      				_notify.show("已经发布成功");
		      				window.setTimeout(function(){window.location.reload();}, 1000);
		      			}
		      		},onerror:function(){
		      			_notify.showError("保存失败");
		      		}
      			});
    		}
    	}
    };
    
    /**
     * 取消
     */
    _pro.__onCancel = function(){
    	this.__form._$reset();
    	_e._$get("c-child").innerHTML="";
    	this.__editor._$setContent("");
    	this.__initEidtArticle();
    };
    
    /**
	 * 获取数据
	 */
    _pro.__getData = function(){
    	var _data = this.__form._$data();
		var _select=_e._$get("category"),_select2=_e._$get("c-child").getElementsByTagName("select")[0];
		if(_select.selectedIndex==0||(_select2&&_select2.selectedIndex==0)){
			 _notify.showError("类目不能为空");
			 return null;
		}
		if(!_data.publishType){
			 _notify.showError("平台不能为空");
			 return null;
		}
		if(!this.__editor._$getContent()){
			 _notify.showError("正文不能为空");
			 return null;
		}
		_data.categoryId=_select2?_select2.options[_select2.selectedIndex].value:_select.options[_select.selectedIndex].value;
		_data.publishType=(_data.publishType instanceof  Array)?3:_data.publishType;
		_data.content=this.__editor._$getContent();
		if(this.__helpArticle){
			_data.id=this.__helpArticle.id;
		}
		console.log(_data);
		return _data;
    };
    
    _p._$$HelpCenterEditModule._$allocate({
      data: {}
    });

    return _p;
  });