/**
 * 选择po
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */


define([
  "pro/extend/util",
  '{lib}base/event.js',
  "text!./poSelect.html",
  "pro/components/notify/notify",
  "pro/extend/config",
  "{pro}widget/BaseComponent.js",
  "{pro}components/po/poModal.js",
  'pro/widget/util/datanotify',
  ], function(_, _v, html,notify, config,BaseComponent,PoModal,_t){
  var PoSelect  = BaseComponent.extend({
    name: "po-select",
    template:html,
    config: function(data){
      _.extend(data,{
        item:{},
        endTime:(new Date).getTime(),
        startTime:(new Date).getTime(),
      });
    },
    init: function(){
      this.supr();
      _v._$addEvent(_t._$$DataNotify, 'barchange', this.__onDateTimeSelected._$bind(this));
    },
    __onDateTimeSelected:function(_data){
      var _type = _data.datetype;
      if(_type){
        this.data.endTime = _data.time;
      }else{
        this.data.startTime = _data.time;
      }
    },
    // 选择档期
    selectSchedule: function(){
      var self = this;
      if(!self.data.startTime){
        notify.notify({
          type: "error",
          message: "请先选择活动有效时间" 
        });
        return false;
      }
      var modal = new PoModal({
        data: {
          selected: self.data.item.schedules,
          endTime:self.data.endTime?self.data.endTime:(new Date).getTime(),
          startTime:self.data.startTime?self.data.startTime:(new Date).getTime()
        },
        events: {
          confirm: function(data){
            // 覆盖原选择项
        	self.data.item.schedules = data.selected;
            self.$update();
            this.destroy();
          },
          "close": function(){
            this.destroy();
          }
        }
      });
    },
    checkAndGetData:function(){
      var item = this.data.item;
      if(!item.schedules || !item.schedules.length){
    	  notify.notify({
	        type: "error",
	        message: "至少选择一个档期" 
	      });
    	  return false;
      }
      return item.schedules;
    }
    
  })

  return PoSelect;

})