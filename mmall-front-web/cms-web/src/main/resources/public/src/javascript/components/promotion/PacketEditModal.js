/**
 * 优惠券弹窗选择列表
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */


define([
  "text!./packetEditModal.html?v=1.0.0.0",
  "{pro}extend/util.js?v=1.0.0.0",
  "{pro}components/modal/modal.js?v=1.0.0.0",
  "{pro}components/notify/notify.js?v=1.0.0.0",
  "{pro}components/datepicker/datepicker.js?v=1.0.0.0"
], function(tpl, _, Modal, notify){


  var PacketEdit = Modal.extend({
    name: "packet-edit",
    needValidate: [],
    content: tpl,
    config: function(data){
      _.extend(data, {
        name: "",
        description:""
      })
    },
    computed:{
      shareString:{
        get: function(data){
          if(data.share === true){
            return "1";
          }else if(data.share === false){
            return "0";
          }else{
            return undefined;
          }
        },
        set: function(value, data){
          if(value == "1"){
            data.share = true;
          }else if(value == "0"){
            data.share = false;
          }else{
            data.share = undefined;
          }
        }
      }
    },
    // 表单字段验证规则
    _validators: {
      "name": function(name){
        if(!name) return "名字不能为空";
        if(name.length > 15) return "名字长度不能超过15"
      },
      "description": function(description){
        if(!description) return "描述不能为空";
        if(description.length > 60) return "描述长度不能超过60"
      }
    },
    confirm: function(){
      var data = this.data,
          _valid = this.valid();
      if(_valid === true) alert('xxx');
      else{
        notify.notify({
          message: _valid,
          type: "error",
          duration: 2000
        })
      }
    },
    valid: function(){
      var _key, _result;
      for(_key in this._validators){
        _result = this._validators[_key](this.data[_key]);
        if (_result){
          return _result;
        }
      }
      return true;
    },
    close: function(){
      this.destroy();
    }
  });


  return PacketEdit;

})