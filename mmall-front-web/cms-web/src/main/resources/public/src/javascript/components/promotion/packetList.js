/**
 * 活动列表筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!./packetList.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  "{pro}components/notify/notify.js?v=1.0.0.0",
  "{pro}components/promotion/packetEdit.js?v=1.0.0.0",
  "{pro}components/modal/modal.js?v=1.0.0.0",
  "{pro}extend/util.js?v=1.0.0.0",
  ], function(tpl, ListComponent, notify, PacketEdit,Modal, _){

 

  var PacketList = ListComponent.extend({
    url: "/packet/listData.json",
    //url: "/src/data/packet.json",
    name: "packet-list",
    api: "/packet/",
    template: tpl,
    config: function(data){
      _.extend(data, {
        total: 1,
        current: 1,
        limit: 10,
        list: [],
        auditState:data.audit == 0?-1:1,
        list: []
      });

      this.$watch(this.watchedAttr, function(){
        if(this.shouldUpdateList()) this.__getList();
      })
    },

    // @子类修改
    watchedAttr: ['current', 'auditState', 'down'],
    getExtraParam: function(data){
      return {state: data.auditState, apply:(data.audit == 0?1:0)}
    },
    onChange: function(e){
      var _node = e.target;
      this.data.current = 1;
      this.data.auditState = _node.value;
    },
    remove: function(index){
      this.data.list.splice(index,1)
      this.$emit('updatelist');
    },
    addPacketList: function(_act){
      _act = _act || {};
      var editor = new PacketEdit({data: _act});
      editor.$on("confirm", function(){

      })
    },
    operate: function(_id,_opt,_auditValue){
      var self=this;
      if(_opt=="discard") {
        var modal = new Modal({
          data: {
            title: "撤消确认",
            coloseText: "不撤消",
            confirmTitle: "确定"
          },
          content: "<div style='padding:15px 0;font-size:14px'>撤消已审核通过的红包，有可能影响用户体验！<br>你确定要撤消？</div>",
          events: {
            confirm: function () {
              this.destroy();
              var _url = self.api + _opt,
                  _data = {id:_id,auditValue:_auditValue};

              self._sendReq(_url,_data);
            },
            "close": function () {
              this.destroy();
            }
          }
        });
      }else{
        var _url = this.api + _opt,
            _data = {id:_id,auditValue:_auditValue};
        this._sendReq(_url,_data);
      }
    },
    _sendReq:function(_url,_data){
      this.$request(_url,{
        method:'post',
        query:_data,
        onload:function(_json){
          notify.notify({
            type: "success",
            message: _json.message
          });
          this.$emit('updatelist');
        },
        onerror:function(_error){
          notify.notify({
            type: "error",
            message: _error.message
          });
        }
      });
    },
    __getList: function(){
      var data = this.data;
      this.$request(this.url, {
        progress: true,
        data: this.getListParam(),
        onload: function(json){
          data.total = json.total;
          data.list = json.list;
        },
        onerror: function(json){
          notify.notify({
            type: "error",
            message: "网络异常，稍后再试！"
          });
        }
      })
    }
  });

  var statusMap = {
    "0": "新建",
    "1": "审核中",
    "2": "审核通过",
    "3": "审核拒绝",
    "4": "已删除"
  };
  PacketList.filter("statusName", function(code){
    return statusMap[code]||"未知状态";
  });
  var ruleMap = {
    "RANDOM": "随机",
    "EQUALLY": "均分"
  };
  PacketList.filter("ruleName", function(rule){
    return ruleMap[rule]||"未知规则";
  });
  return PacketList;

})