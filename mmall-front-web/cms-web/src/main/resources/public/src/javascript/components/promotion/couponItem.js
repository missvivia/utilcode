/**
 * 条件筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */



define([
  "{pro}components/notify/notify.js?v=1.0.0.0",
  "{pro}extend/util.js",
  "text!./couponItem.html?v=1.0.0.0",
  "{pro}widget/BaseComponent.js?v=1.0.0.0",
  "{pro}components/po/poModal.js?v=1.0.0.0",
  "{pro}components/coupon/couponModal.js?v=1.0.0.0",
  "{pro}components/form/btnSelect.js?v=1.0.0.0"], 
    function(notify , _, tpl, BaseComponent, PoModal, CouponModal, BtnSelect){

  var CouponItem = BaseComponent.extend({
    name: "coupon-item",
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
      var afterResult = [{}, {}, {}, {}]
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
        item.schedules = item.schedules||[]
      })
    },
    // 新建一个优惠项目
    add: function(){
      // 新建
      this.data.items.push({
        schedules: [],
        condition:{type: (this.data.type==0)?1:2},
        result: this.configResult([])
      })
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
    // 检查并返回整理后的数据
    checkAndGetData: function(){
      var testNumber = /^[1-9]\d*(\.\d+)?$/,
          testNumber2 = /^[0-9](\.\d+)?$/;
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
        if(condition.type ==1 && (!testNumber.test(condition.minPrice) && !testNumber2.test(condition.minPrice))){
          valid = showError( i,"请输入正确的最小价格" )
        }

        if(condition.type ==2 && (!testNumber.test(condition.minNum) && !testNumber2.test(condition.minNum))){
          valid = showError( i,"最小数字不能为空" )
        }

        // 处理result
        afterResult = [];
        var res = item.result[condition.type - 1];
        if(condition.type == 1){
        	if(!testNumber.test(res.value) && !testNumber2.test(res.value)){
        		valid = showError( 0,"请输入正确的直降金额" );
  			}
        }
        if(condition.type == 2 && (!testNumber.test(res.value) && !testNumber2.test(res.value))){
        	valid = showError( 0,"折扣格式错误" );
        }
        afterResult.push({
        	type : condition.type,
        	value : res.value
        })
        afterItems.push({
//          id: item.id || -1,
          condition: {type: condition.type, value: condition.value},
          result: afterResult,
          schedules: item.schedules.map(function(sche){
            return sche;
          })
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
        }
      },
      "condition": function(condition){
        var type = String(condition.type);
        var value;

      }
    }

    
  });


  CouponItem.directive("nz-focus", function(elem, value){
    this.$watch(value, function(){
      if(!!value){
        _.smoothTo(elem);
      }
    })
  })

  return CouponItem;

})