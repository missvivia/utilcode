NEJ.define([
    'base/klass',
    'base/event',
    'base/util',
    'base/element',
    'util/event',
    'util/form/form'
],function(_k,_v,_u,_e,_t,_t0,_p,_o,_f,_r,_pro){
    /**
     * 查询条件表单控件
     */
    _p._$$SearchForm = _k._$klass();
    _pro = _p._$$SearchForm._$extend(_t._$$EventTarget);
    /**
     * 重置控件
     * @param {Object} _options
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        var _form = _e._$get(_options.form);

        document.onkeypress =  function stopRKey(evt) { 
      	  var node = _v._$getElement(evt); 
    	  if ((evt.keyCode == 13) && (node.type=="text"))  {return false;} 
    	};
        var list = _form.getElementsByTagName('select');
        for(var i=0,l=list.length;i<l;i++){
        	if(!list[i].getAttribute("data-changeDisabled"))
        		_v._$addEvent(list[i],'change',this.__onSearch._$bind(this));
        }
        _v._$addEvent(
                _form['btn-search'],'click',
                this.__onSearch._$bind(this)
            );
        this.__form = _t0._$$WebForm._$allocate({
            form:_form
        });
        this.__onSearch();
    };
    
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        this.__form._$recycle();
        delete this.__form;
        this.__super();
    };
    /**
     * 提交查询
     * @param {Object} _event
     */
    _pro.__onSearch = function(_event){
        _v._$stop(_event);
        if (this.__form._$checkValidity()){
            this._$dispatchEvent(
                'onsearch',
                this._$data()
            );
        }
    };
    /**
     * 重置表单
     * @return {Void}
     */
    _pro._$reset = function(){
        this.__form._$reset();
    };
    /**
     * 取表单数据
     * @return {Void}
     */
    _pro._$data = function(){
    	var _data = this.__form._$data();
    	_u._$loop(_data,function(_item,_key){
    		if(_u._$isString(_item[_key])){
    			_item[_key] = _item[_key].trim();
    		}
    	})
        return _data;
    };
    
    return _p._$$SearchForm;
});
