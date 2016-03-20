/**
 * 活动助手筛选页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "pro/components/notify/notify",
  "text!./helper.list.html",
  "{pro}components/ListComponent.js",
  "{pro}components/product/modal.helper.js"
  ], function(notify, tpl, ListComponent, HelperModal){
    var REST_URL = "/rest/helpers"
  var HelperList = ListComponent.extend({
    url: "/rest/helpers",
    template: tpl,
    // @子类修改
    remove: function(prod, index){
      var data = this.data;
      this.$request( REST_URL + "/" + prod.id , {
        method:'delete',
        onload: function(json){
          if(json && json.code === 200){
            notify.notify({
              type: "success",
              message: "删除尺码助手成功"
            });
            data.list.splice(index,1);
            return
          }
        },
        onerror: function(json){
          var error = "删除尺码助手失败";
          if(json && json.code === 501){
            error = "无法删除已经被使用的尺码助手"
          }
          notify.notify({
            type: "error",
            message: error
          })
        }
      })
    },
    preview: function(helper){
      if(!helper.body) helper.body = [];
      new HelperModal({
        data: {
          helper :helper,
          title: "预览尺码助手",
          width: 920
        }
      })
    }
  });
  return HelperList;

})