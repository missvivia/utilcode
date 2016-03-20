/**
 * 条件筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */



define([
  "{pro}components/notify/notify.js?v=1.0.0.0",
  "{pro}extend/util.js?v=1.0.0.0",
  "text!./activityItem.html?v=1.0.0.0",
  "{pro}widget/BaseComponent.js?v=1.0.0.0",
  "{pro}components/po/poModal.js?v=1.0.0.0",
  "{pro}components/activity/packetModal.js?v=1.0.0.0",
  "{pro}components/form/btnSelect.js?v=1.0.0.0"], 
    function(notify , _, tpl, BaseComponent, PoModal, PacketModal, BtnSelect){

  var ActivityItem = BaseComponent.extend({
    name: "activity-item",
    template: tpl,

    config: function(data){
      _.extend(data, {
        items: [],
        type:0
      })
      this.configData(data);
    },
    // 扁平化数据
    configResult: function(result){
      var afterResult = [{}, {}, {}, {}, {}]
      if(Array.isArray(result)){
        // 根据效果最终反推数据
        result.forEach(function(res){
          var tmpvalue = res.value;
          afterResult[res.type-1].type = res.type;
          switch(parseInt(res.type)){
            case 1:
              afterResult[0].selected = true;
              afterResult[0].value = tmpvalue;
              break;
            case 2:
              afterResult[1].selected = true;
              afterResult[1].value = tmpvalue;
              break;
            case 3:
              afterResult[2].selected = true;
              break;
            case 4:
              afterResult[3].selected = true;
              afterResult[3].value = tmpvalue;
              afterResult[3].name = res.name;
              break;
          }
        })
      }
      return afterResult;
    },
    // 预处理输入item的condition
    // split canshu
    configCondition: function(condition){
      var tmpValues = (condition.value||"").split("-");
      switch(condition.type){
        case 1:
          condition.minPrice = tmpValues[0] ||"";
          condition.maxPrice = tmpValues[1] ||"";
          break;
        case 2:
          condition.minNum = tmpValues[0] ||"";
          condition.maxNum = tmpValues[1] ||"";
      }
    },
    // 配置参数
    configData: function(data){
      var self = this;
      data.items.forEach(function(item){
        self.configCondition(item.condition)
        item.result = self.configResult(item.result)
      })
    },
    // 新建一个优惠项目
    add: function(){
      // 新建
      this.data.items.push({
        condition:{type: (this.data.type==0)?1:2},
        result: this.configResult([])
      })
      console.log("haha")
      return false;
    },
    /**
     * 由于直降和折扣冲突， 红包和优惠券冲突，我们需要这个函数来单独处理
     * @return {[type]} [description]
     */
    pickConflict: function($event, type, result){
      var checked = $event.target.checked;
      if(checked){
        result[type-1].selected = true
        switch(type){
          case 1:
            result[1].selected = false
            break;
          case 2:
            result[0].selected = false
            break;
          default:
        }
      }else{
        result[type-1].selected = false
      }
    },
    // 选择红包...
    selectCoupon: function(item){
      var data = this.data;
      var self = this;
      var modal = new PacketModal({
        data: {
          // 优惠券与档期的区别就是优惠券的
          selected: {id:item.value,name:item.name}
        },
        events: {
          confirm: function(data){
            item.value = data.selected.id;
            item.name = data.selected.name;
            self.$update();
            this.destroy();
          },
          "close": function(){
            this.destroy();
          }
        }
      })
    },
    // 检查并返回整理后的数据
    checkAndGetData: function(){
      var showError = this.showError;
      var data = this.data, valid = true;
      var items = data.items;
      var afterItems = [], afterResult, afterSchedule;
      for(var i = 0, len = items.length; i < len ; i++){
        var item = items[i];
        var condition = item.condition; 
        // 处理condition
        if(condition.type == null || !condition){
          valid = showError( i,"条件不符合规则" )
        }
        condition.value = condition.type==1? (condition.minPrice||"") + '-' +(condition.maxPrice||""):
          (condition.minNum||"") + '-' +(condition.maxNum||"");

        if(condition.type ==1 && (isNaN(condition.minPrice) || !condition.minPrice)){
          valid = showError( i,"最小价格格式错误" )
          valid = false;
        }

        if(condition.type ==2 && (isNaN(condition.minNum) || !condition.minNum)){
          valid = showError( i,"最小数字格式错误" )
        }

        // 处理result
        afterResult = [];
        item.result.forEach(function(res, index){
          if(res.selected == true){
            switch(index+1){
              case 1:
                if(isNaN(res.value)||!res.value) valid = showError( i,"直降金额格式错误" )
                break
              case 2:
                if(isNaN(res.value)||!res.value) valid = showError( i,"折扣格式错误" )
                break;
              case 4: 
                if(!res.value) valid = showError( i,"你必须选择一张优惠卷" )
                break
            }
            afterResult.push({
              type: index + 1,
              value: res.value,
              name: res.name
            })
          }
        })

        afterItems.push({
//          id: item.id || -1,
          condition: {type: condition.type, value: condition.value},
          result: afterResult
        })
        if(!valid) return false;

      }
      return afterItems;
    },
    showError: function(i, msg){

      notify.notify({
        type: "error",
        message: "第" + (i+1) + "项: " +msg 
      })
      return false;
    },
    process: {
      "result": function(result){
        var type = String(result.type);
        var value;
        switch(type){
          case "1": 
          case "2":
          case "3":
            value = result.value;
            break;
          case "4":
            value = result.coupon
        }
      },
      "condition": function(condition){
        var type = String(condition.type);
        var value;

      }
    }

    
  });


  ActivityItem.directive("nz-focus", function(elem, value){
    this.watch(value, function(){
      if(!!value){
        _.smoothTo(elem);
      }
    })
  })

  return ActivityItem;

})