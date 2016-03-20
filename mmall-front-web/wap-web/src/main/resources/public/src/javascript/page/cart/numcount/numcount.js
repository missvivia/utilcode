/**
 * 计数器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
NEJ.define([
  'pro/extend/util',
  "{lib}base/element.js",
  "text!./numcount.html?v=1.0.0.0",
  "pro/components/notify/notify",
  "pro/widget/BaseComponent"
  ], function(_, e, tpl, notify, BaseComponent){
  var dom = Regular.dom;

  var NumCount = BaseComponent.extend({
    name: "numcount",
    template: tpl,
    config: function(data){
      _.extend(data, {
        min: 1
      })
      if(!data.count)  data.count = data.min;
    },
    validate: function(count){
      return /\d+/.test(""+count) && count <= this.data.max && count >= 0;
    },
    setNum: function($event,count){
    	if(!count){
    		count = $event.target.value;
    	}
    	if($event.target.className.indexOf("z-dis") > -1 || $event.target.parentNode.className.indexOf("z-dis") > -1){
    		return;
    	}
      var data = this.data;

      var max = this.$get("max");
      var min = data.min;
      if(count < min){
    	  count = min;
    	  $event.target.value = count;
      }
      this.data.count = parseInt(count,10);
      this.$emit('change', count);
    },
    disable: function(){
      this.$update({
        "disable": true,
        "count": 1
      });
    }
  })

  return NumCount;

})