/**
 * 红包弹窗选择列表
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */


define([
  "text!./packetSelect.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  "{pro}components/modal/modal.js?v=1.0.0.0",
  '{pro}components/notify/notify.js?v=1.0.0.0',
  "{pro}extend/util.js?v=1.0.0.0"
  ], function(selectTpl ,ListComponent, Modal, notify,_){


  var PacketModal = Modal.extend({
    name: "packet-modal",
    data: {
      title: "选择红包"
    },
    content: "<packet-select selected={{selected}}></packet-select>",
    init: function(){
      this.supr();
    }
  });

  // * 私有组件
  // * 红包弹窗选择列表

  var PacketSelect  = ListComponent.extend({
    url: "/packet/listData.json",
    template: selectTpl ,
    config: function(data){
        _.extend(data, {
          total: 1,
          current: 1,
          limit: 5,
          list: []
        });

        this.$watch(this.watchedAttr, function(){
          if(this.shouldUpdateList()) this.__getList();
        })
      },
    getExtraParam: function(data){
      return { state: 2, qvalue:data.qvalue||'', apply:1}
    },
    select: function(packet){
      var data = this.data;
      data.selected = packet;
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
  })

  PacketModal.component("packet-select", PacketSelect);

  return PacketModal;

})