/**
 * 详情专用的计数器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
NEJ.define([
  'pro/extend/config',
  'pro/extend/util',
  "{lib}base/element.js",
  "text!./sizepicker.html",
  "pro/components/notify/notify",
  "pro/widget/BaseComponent"
  ], function(config, _, e, tpl, notify, BaseComponent){
  var dom = Regular.dom;


  var url = {
      // 添加到购物车
      // 检查数量
      "CHECK_SIZE": "/detail/checkSize",
      "NOTIFY": "/detail/notifySize",
      "CHECK_NOTIFY": "/cart/userexistinremind",
      "TOGGLE_NOTIFY": "/cart/updateremindstorage"
  }

  var SizePicker = BaseComponent.extend({
    name: "numcount",
    template: tpl,
    data: {
      classMaps: {
        1: "u-sel",
        2: "u-sel u-sel-gray",
        3: "u-sel z-disabled"
      }
    },
    config: function(data){
      // 填满尺寸头为三的倍数
      _.extend(data,{
        sizes: []
      })
      this.configSize()
    },
    // 预处理size数据因为tip要撑满三的倍数, 
    configSize: function(){
      var data = this.data;
      var sizes = data.sizes;
      var self = this;
      var total=sizes.length;
      var chance = 0;
      var empty = 0;
      sizes.forEach(function(size){
        var len;
        if(!size.sizeTips){ //均码
          len =0;
          size.sizeTips =[]
        }else{
          len = size.sizeTips.length;
        }
        //商品详情页去掉默认选中第一个尺码，因为后面会在加入购物车时做选择尺码的优化
        // 假如没有选中的skuId， 我们默认选中第一个可选的size
        //if(!data.skuId && size.type == 1){
        //  data.skuId = size.skuId;
        //}
        if(size.type===3) empty++;
        if(size.type===2) chance++;
        if(len % 3){
          for(var i = 0; i < (3- len % 3); i++){
            size.sizeTips.push({});
          }
        }
      })
      if(total === chance+empty) setTimeout(this.$emit.bind(this,'empty', 4), 0)
    },
    init: function(){
      var data = this.data;
      if(data.skuId){
        // 我们根据传入的skuId来选中 , ?skuid=1
        var index = _.findInList(data.skuId, data.sizes, "skuId");
        if(~index && !data.disable){
          this.select(data.sizes[index]);
        }
      } 
    },
    //选中某个尺码
    select: function(size){
      var data = this.data;
      if(data.selected === size || size.type!==1) return;
      var self = this;
      // @REMOVE
      this.checkSize(function(err){
        //如果type变为非1，则不选择
        // 出现错误我们也不管，到购物车再做提醒。
        if(size.type === 1 || err){
          self.changeSelected(size);
        }
      })
      // 每次选中 我们都要检查有效性
      return ;
    },
    changeSelected: function(selected){
      this.data.selected = selected;
      this.$emit("selected", selected);
    },
    // 切换可见性, 由于需要一定的演示来确保可以操作面板，我们不直接让size消失
    toggle: function(size, isShow){
      if(size.type!==2 && (!size.sizeTips || !size.sizeTips.length)) return;
      if(isShow){
        size.isShow = true;
        clearTimeout(size.timeid);
        // 当notify还为null时, 我们需要异步去获取状态，黄陆谦懒成屎啊
        if(size.isNotified == null) {
          this.$request(url.CHECK_NOTIFY, {
            data: {skuId: size.skuId},
            onload: function(json){
              if(json.code == 200){
                size.isNotified = !!json.result;
              }
            }
          })
        }
      }else {
        size.timeid = this.$timeout(function(){
          size.isShow = false
        },80)
      }
    },
    // 同步库存
    checkSize: function(callback){
      var data = this.data;
      this.$request( url.CHECK_SIZE, {
        data: {id: data.id, scheduleId: data.scheduleId},
        onload: function(json){
          if(!json.result || !json.result.list) return callback(1);
          var sizes = json.result.list;
          var total = sizes.length;
          var empty = 0;
          var chance = 0
          // 我们先更新
          data.sizes.forEach(function(size, index){
            var msize = sizes[index];
            if(msize){
              _.extend(size, {
                cartNum: sizes[index].cartNum,
                num: sizes[index].num,
                type: sizes[index].type
              },true)
            }
            if(size.type === 2) {
              chance += 1;
            }
            if(size.type === 3) {
              empty += 1;
            }
          })
          if(total === chance+empty) this.$emit("empty", 4);
          if(data.selected && data.selected.type !==1){
            this.changeSelected(null);
          }
          // 第一个标志出错
          callback(null, data)
        },
        onerror: function(){
          // this.$emit('selected', size)
          // data.selected = size;
          callback(1)
        }
      })
    },
    // 通知订阅
    notify: function(size){
      this.$request(url.TOGGLE_NOTIFY, {
        data:{
          skuId: size.skuId,
          subscribed: !size.isNotified? 1:0
        },
        onload: function(json){
          if(json.code === 200){
            size.isNotified = !size.isNotified ? 1:0;
            // notify.notify(size.isNotified? '添加提醒成功': '取消提醒成功')
          }
        },
        onerror: function(json){
          notify.notify({
            type: "error",
            message: json.message || "操作失败"
          })
        }
      })
      return true;
    },
    // 显示错误
    toggleError: function(err){
      if(!err) this.$update("error", null);
      else{
        this.$update("error", typeof err === "string"? {msg: err} : err);
      }
    },
    disable: function(status){
      this.data.sizes.forEach(function(size){
        size.type = 3;
      })
      this.data.disable = true;
      this.$update();
    }
  }).use("timeout");

  return SizePicker;

})