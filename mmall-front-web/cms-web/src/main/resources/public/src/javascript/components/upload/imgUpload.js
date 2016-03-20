/**
 * 图片列表 + 上传组件
 * author hzwuyuedong@corp.netease.com
 */
define([
  "text!./imgUpload.html",
  "{pro}widget/BaseComponent.js",
  '{pro}components/notify/notify.js'
], function(tpl, BaseComponent, notify){

  var imageUpload = BaseComponent.extend({
    config: function(data){
      data.img = data.img || '';
    },
    name: "imgupload",
    template: tpl,
    handleUpload: function($event){
      var data = this.data;
      // 根据upload传递的数据来判断处理逻辑
      switch($event.type){
         case "preview":
          this.$update(function(data){
            data.percent = 0.1;
            if ($event.data[0]){
              data.preview = $event.data[0].imgUrl;
            };
          })
          break;
        case "upload":
          this.$update(function(data){
            data.img = $event.data[0].imgUrl;
            data.preview = null;
            data.percent = 1;
          })
          break;
        case "error":
          notify.showError('上传图片失败！');
          break;
      }
      console.log($event)
    }
  });
  return imageUpload;

})