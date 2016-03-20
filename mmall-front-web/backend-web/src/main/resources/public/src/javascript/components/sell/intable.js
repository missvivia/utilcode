/**
 * 发票列表
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "pro/extend/util",
  "text!./intable.html",
  "pro/components/ListComponent",
  "pro/components/notify/notify"
  ], function(_, tpl, ListComponent, notify){
  var ActList = ListComponent.extend({
    url: "/sell/invoiceSearch",
    watchedAttr: ["stateInt"],
    computed: {
      //计算属性主要是处理 全选逻辑的双向帮顶
      allChecked: {
        get: function(data){
          if(!data.list || !data.list.length) return false;
          // 我们认为所有子类都选中时，即为全选
          return this.getChecked().length === data.list.length;
        },
        // 当全选被点击时， 我们将所有对应子类进行全选反选操作
        set: function(sign, data){
          data.list.forEach(function(item){
            item.checked = sign;
          })
        }
      }
    },
    shouldUpdateList: function(){
      return !!this.enable;
    },
    getExtraParam: function(data){
      return _.extend ({stateInt: data.stateInt}, this.savedParam || {});
    },
    search: function(data){
      // 保存上次搜索的参数，备用
      this.enable = true;
      this.savedParam = data;
      // 添加this.data;
      this.refresh()
    },
    // 有tpl提交单个 ，没有则提交全部
    submit: function(tpl){
      var data = this.data;
      var ids, error;
      if(tpl) ids= [{orderId:tpl.orderId, expressCompanyName: tpl.expressCompanyName, barCode: tpl.barCode, userId: tpl.userId}]
      else ids = this.getChecked().map(function(item){
        return {
          userId: item.userId,
          orderId:item.orderId,
          expressCompanyName: item.expressCompanyName.trim(),
          barCode: item.barCode.trim()
        }
      })

      ids.forEach(function(item){
        if(!item.expressCompanyName || !item.barCode) error = "地址和快递公司都不能为空";
      })

      if(error) return notify.notify({
        type: "error", 
        message: error
      })

      if(!ids.length) return notify.notify({
        type: "error",
        message: "未选择发票"
      })
      this.$request("/sell/invoice/batchSubmit", {
        method: "post",
        data: ids,
        onload: function(json){
          if(json.code == 200){
            notify.notify("提交成功")
            if(tpl && data.stateInt == 1 && tpl.stateInt == 0){
              tpl.stateInt = 1;
            }else{
              this.refresh();
            }
          }else{
            notify.notify({
              type: 'error',
              message: "提交失败"
            })
          }
        }
      });
    },
    // 获取所有选中项
    getChecked: function(){
      return this.data.list.filter(function(item){
        return item.checked === true
      })
    },
    name: "intable",
    template: tpl
    // @子类修改
  });
  return ActList;

})