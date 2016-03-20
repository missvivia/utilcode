/**
 * 详情专用的计数器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
NEJ.define([
  'pro/extend/util',
  "{lib}base/element.js",
  "text!./numcount.html",
  "pro/components/notify/notify",
  "pro/widget/BaseComponent"
  ], function(_, e, tpl, notify, BaseComponent){
  var dom = Regular.dom;

  var NumCount = BaseComponent.extend({
    name: "numcount",
    template: tpl,
    computed:{
      max: function(data){
        return Math.min(data.remain||0, 2) || 1;
      }
    },
    config: function(data){
      _.extend(data, {
        min: 1
      })
      if(!data.count)  data.count = data.min;
      // this.$watch("count", function(count, oldnum){
      //   if(!this.validate(count)){
      //     var self = this;
      //     notify.notify({
      //       type: "error",
      //       message: "数量输入不正确"
      //     })
      //   }
      // })
       
    },
    validate: function(count){
      return /\d+/.test(""+count) && count <= this.data.max && count >= 0;
    },
    setNum: function(count){
      if(this.data.disable) return;
      var data = this.data;
      if(data.remain == null){
        this.$emit("need_remain");
        return;
      }
      if(data.remain === 0) return;
      var max = this.$get("max");
      var min = data.min;
      if(count > max) return notify.notify({
        type: "error",
        message: max===1? "商品库存不足":"每款商品同尺码限购"+max+"件"
      });
      if(count <= 0) return notify.notify({
        type: 'error',
        message: "本商品"+ 1 +"件起售"
      });
      this.data.count = count;
    },
    setRemain: function(remain){
      var data = this.data;
      data.remain = remain;
      if( data.remain < data.count ){
        data.count = this.$get("max");
      }
      this.$update()
    },
    disable: function(){
      this.$update({
        "disable": true,
        "count": 1
      });
    }
  }).filter("remain", function(remain){
    if(typeof remain != "number" || remain >= 10) return ""; 
    if(remain >=5) return "仅剩<b>" + remain + "</b>件，抓紧时间抢购哦";
    if(remain >= 1) return "最后<b>"+remain+"</b>件，即将售罄";
    if(remain == 0) return "库存不足";
  })

  return NumCount;

})