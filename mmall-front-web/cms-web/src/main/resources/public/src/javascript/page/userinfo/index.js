/**
 * 用户信息模块
 * author cheng-lin(cheng-lin@corp.netease.com)
 *
 */

NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'pro/widget/module',
    'pro/extend/request',
    'pro/page/userinfo/userlist/userlist',
    'util/form/form'
  ],
  function(_k,_e,_v,_$$Module,_request,userlist,_t,_p,_o,_f,_r) {
    var _pro;

    _p._$$Index = _k._$klass(),
    _pro = _p._$$Index._$extend(_$$Module);

    /**
     * 初始化方法
     * @param  {[type]} _options [description]
     * @return {[type]}          [description]
     */
    _pro.__init = function(_options) {
      this.__super(_options);
      var _nodes = _e._$getByClassName(document,'j-node');
      this.__key = _nodes[0];
      this.__value = _nodes[1];
      this.__ok = _nodes[2];
      this.__cnt = _nodes[3];
      this.__form = _t._$$WebForm._$allocate({
        form:_e._$get('form')
      })
    };

    /**
     * 重置
     * @param  {[type]} _options [description]
     * @return {[type]}          [description]
     */
    _pro.__reset = function(_options){
      this.__doInitDomEvent([
        [this.__ok,'click',this.__onOK._$bind(this)]
      ]);
    };

    /**
     * 查询用户列表
     * @return {[type]} [description]
     */
    _pro.__onOK = function(){
      if (!this.__form._$checkValidity()){
        return;
      }
      var _data = {};
      _data.type = this.__key.value;
      _data.search = this.__value.value.trim();
      if (!this.__userList){
        this.__userList = new userlist({
          data:{
            condition:_data
          }
        })
        this.__userList.$inject(this.__cnt);
      }else{
        this.__userList.data.condition = _data;
        this.__userList.$emit('updatelist');
      }
    };


    _p._$$Index._$allocate({
      data: {}
    });

    return _p;
  });