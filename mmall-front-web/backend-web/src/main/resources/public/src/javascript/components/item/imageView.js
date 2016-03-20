/**
 * 图片列表 + 上传组件
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
define([
  'pro/extend/util',
  '{pro}components/notify/notify.js',
  "text!./imageView.html",
  "{pro}widget/BaseComponent.js"
  ], function(_, notify, tpl, BaseComponent){

  var ImageView = BaseComponent.extend({
    name: "imageview1",
    template: tpl,
    config: function(data){
      _.extend(data, {
        imgs: [],
        limit: 10,
        edit:false,
      })
    },
    del:function(index){
      // 编辑只要一张图片不能删除
      if(this.data.edit && (this.data.imgs.length==1)){
        notify.show("至少保留一张商品图片");
        return;
      }
	
      var img = this.data.imgs[index];
      // 如果是刚添加的图片，直接删除
      if(!img.prodPicId){
        this.data.imgs.splice(index,1);
        return;
      }

      this.$request("/item/product/delPic?skuId="+img.skuId+"&prodPicId="+img.prodPicId,{
        method:'GET',
        type:'json',
        onload:function(_json){
          if(_json.code==200){
              this.data.imgs.splice(index,1);
              notify.show("删除图片成功");
          }
        },
        onerror:function(_error){
          notify.show("删除图片失败");
        }
      })
      
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
              // 暂时没有替换功能
            }else{
              // 添加
              var img = new Object();
              img.picPath = imgs.imgUrl;
              data.imgs.push(img);
            }
          })
          break;
        case "error":
          notify.notify({
            type: "error",
            message: "上传失败, 可能由于文件超出限制2M"
          })
      }
    }
  });

  return ImageView;

})