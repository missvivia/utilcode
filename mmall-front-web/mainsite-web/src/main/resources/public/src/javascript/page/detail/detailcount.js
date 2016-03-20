/**
 * 详情专用的计数器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
NEJ.define([
  'pro/extend/util',
  "{lib}base/element.js",
  "text!./detailcount.html",
  'pro/components/countdown/countdown',
  "pro/components/notify/notify"
  ], function(_, e, tpl, CountDown ,notify){
  var dom = Regular.dom;

  // 显示最先结束的时间：
  // 如活动先结束，显示 “距活动结束：02：05:10:10”
  // 如档期先结束，显示“距抢购结束：02:05:10:10”
   
  // 如活动结束了档期还未结束，则时间从“活动结束”变成“抢购结束”
  // 如档期结束而活动未结束，则直接展示过期页面

  var DetailCount = CountDown.extend({
    template: tpl,
    config: function(data){
      data.time = data.scheduleTime;
      if(data.status !== 3){
        this.supr(data);
      }
    },
    init: function(){
    this.supr();
     this.$on("onchange", function(data){
        if(data.isdown) this.doEnd(data)
      }) 
    },
    doEnd: function(){
      // var data = this.data;
      // var time = data.time;
      // if(time === data.activeTime){
      //   if(data.scheduleTime) this.$update("time", data.scheduleTime - data.activeTime);
      // }else{
        this.$emit("end"); 
        this.$update("time", null)
      // }
    }
  });

  return DetailCount;

})