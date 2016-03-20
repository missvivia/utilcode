/**
 * 图片列表 + 上传组件
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
define([
  "text!../templates/imageView.html",
  "{pro}widget/BaseComponent.js"
  ], function(tpl, BaseComponent){

  var ImageView = BaseComponent.extend({
    config: function(data){
      Regular.util.extend(data, {
        imgs: []
      })
    },
    name: "m-imageview",
    template: tpl,
    handleUpload: function($event){
      var data = this.data;
      // 根据upload传递的数据来判断处理逻辑
      switch($event.type){
        case "preview":
          this.$update(function(data){
            data.percent = 0.1;
            data.preview = $event.data;
          })
          break;
        case "upload":
        case "error":
          //todo: 联调要删除这个
          console.log("haha");
          this.$update(function(){
            data.imgs.push({
              src: data.preview
            });
            data.preview = null;
            data.percent = 1;
          })
          break;
      }
      console.log($event)
    }
  });

  return ImageView;

})