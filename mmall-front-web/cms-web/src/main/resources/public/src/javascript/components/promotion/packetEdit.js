/**
 * 红包新建、编辑页
 * author jinze(jinze@corp.netease.com)
 */


define([
  "text!./packetEdit.html?v=1.0.0.0",
  "util/form/form.js?v=1.0.0.0",
  "{pro}extend/util.js?v=1.0.0.0",
  "{pro}widget/BaseComponent.js?v=1.0.0.0",
  "{pro}components/notify/notify.js?v=1.0.0.0",
  "{pro}components/datepicker/datepicker.js?v=1.0.0.0"
], function(tpl, _webForm, _, BaseComponent, notify){

  var numReg = /^\d+(\.\d+)?$/;

  var PacketEdit = BaseComponent.extend({
    name: "packet-edit",
    template: tpl,
    config: function(data){
      if(!data.vo){
        data.vo = {};
      }
      _.extend(data.vo, {
        name: "",
        description:""
      });
    },
    computed:{
      shareString:{
        get: function(data){
          if(data.vo.share === true){
            return "1";
          }else if(data.vo.share === false){
            return "0";
          }else{
            return undefined;
          }
        },
        set: function(value, data){
          if(value == "1"){
            data.vo.share = true;
          }else if(value == "0"){
            data.vo.share = false;
          }else{
            data.vo.share = undefined;
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
      },
      "share": function(share){
        if(share=== undefined) return "请选择是否分享"
      },
      "validDay": function(validDay, vo){
        if(vo.share === true){
          validDay = validDay || "";
          var _validDay = validDay.toString();
          if(!_validDay) return "可用天数不能为空"
          if(!_validDay.match(/^[1-9]\d*$/)) return "可用天数应该是正整数"
        }
      },
      "cash": function(cash){
        cash = cash || "";
        var _cash = cash.toString();
        if(!_cash) return "红包金额不能为空"
        if(!_cash.match(numReg)) return "红包金额应该是数字"
      },
      "count": function(count, vo){
        count = count || "";
        var _count = count.toString();
        if(vo.share === true){
          if(!_count) return "红包数量不能为空"
          if(!_count.match(/^[1-9]\d*$/)) return "红包数量应该是正整数"
        }
      },
      "distributeRule": function(distributeRule, vo){
        if(vo.share === true){
          if(!distributeRule) return "裂变方式不能为空"
        }
      },
      "copies": function(copies, vo){
        copies = copies || "";
        var _copies = copies.toString();
        if(vo.share === true){
          if(!_copies) return "裂变数量不能为空"
          if(!_copies.match(/^[1-9]\d*$/)) return "裂变数量应该是正整数"
        }
      },
      "binderType": function(binderType, vo){
        if(vo.share === false){
          if(!binderType) return "绑定方式不能为空"
        }
      },
      "users": function(users, vo){
        if(vo.share === false){
          if(vo.binderType=='USER_BINDER'){
            if(!users) return "用户名单不能为空"
          }
        }
      },
      "endTime": function(endTime, vo){
        if(endTime < vo.startTime){
          return "结束时间不能小于开始时间"
        }
        if(endTime < (new Date).getTime()){
          return "结束时间不能小于当前时间";
        }
      }
    },
    confirm: function(){
      var data = _webForm._$$WebForm._$allocate({form: 'editForm'})._$data();
      var vo = this.data.vo;
      if(vo.id){
        data.id = vo.id;
      }
      if(data.shareString == "1"){
        data.share=true;
      }
      else if(data.shareString == "0"){
        data.share=false;
      }
      delete data.shareString;
      //处理结束时间
      data.endTime = _.setDateTOEnd(new Date(data.endTime));
      var _valid = this.valid(data);
      if(_valid === true) this._save(data);
      else{
        notify.notify({
          message: _valid,
          type: "error",
          duration: 2000
        })
      }
    },
    valid: function(_data){
      var _key, _result;
      for(_key in this._validators){
        _result = this._validators[_key](_data[_key], _data);
        if (_result){
          return _result;
        }
      }
      return true;
    },
    _save:function(_data){
      this.$request('/packet/save',{
        method:'post',
        data:_data,
        onload:_callBack,
        onerror:_callBack
      });

      function _callBack(json){
        if(json && json.code === 200){
          notify.notify({
            type: "success",
            message: "保存红包成功, 三秒后跳转到红包列表"
          });
          setTimeout(function(){window.location= "/promotion/packet";},3000);
        }else{
          notify.notify({
            type: "error",
            message: json && json.message || "保存红包失败"
          });

        }
      }
    }
  });


  return PacketEdit;

})