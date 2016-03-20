/**
 * 图片列表 + 上传组件
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
define([
  'pro/extend/util',
  '{pro}widget/layer/image/image.pick.js',
  '{pro}components/notify/notify.js',
  "text!./imageView.html",
  "{pro}widget/BaseComponent.js",
  "{pro}extend/util.js"
  ], function(_, ImagePick, notify, tpl, BaseComponent, _){

  var ImageView = BaseComponent.extend({
    config: function(data){
      _.extend(data, {
        imgs: [],
        limit: 10
      })

      data.imgs.forEach(function(img, index){
        if(typeof img === "string"){
          data.imgs[index] = {src: img}
        }
      })
    },
    name: "imageview",
    template: tpl,
    getFromSpace: function(img_index){
      var data = this.data;
      var self = this;
      ImagePick._$allocate({
        max: img_index!=null? 1: data.limit - data.imgs.length,
        onok: function(list){
          if(Array.isArray(list)){
            list.forEach(function(img){
              if(!img) return;
              if(img_index!=null){
                data.imgs[img_index].src = img.imgUrl;
              }else{
                data.imgs.push({src:img.imgUrl})
              }
              
            })
            self.$update();
          }
          
        }
      })._$show();
    },
    handleUpload: function($event, index){
      var data = this.data;
      var imgs;
      // 根据upload传递的数据来判断处理逻辑
      switch( $event.type ){
        case "preview":
          this.$update(function(data){
            data.percent = 0.1;
            data.preview = $event.data;
          })
          break;
        case "upload":
          imgs = $event.data[0];
      	  notify.notify({
            type: "success",            
            message: "上传成功"
          })
          
          this.$update(function(){
            if(index!=null){
              data.imgs[index].src = imgs.imgUrl
            }else{
              data.imgs.push({src:imgs.imgUrl});
            }
              
          })
          _._$uploadImage2Category([imgs]);
          break;
        case "error":
          notify.notify({
            type: "error",
            message: "上传失败, 可能由于文件超出限制2M"
          })
          
          //todo: 联调要删除这个

      }
    }
  });

  return ImageView;

})