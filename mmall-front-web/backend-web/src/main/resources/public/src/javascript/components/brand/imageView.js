define([
  "text!./imageView.html",
  "{pro}widget/BaseComponent.js",
  "{pro}extend/util.js",
  '{pro}widget/layer/image/image.pick.js',
    "pro/components/notify/notify"
  ], function(tpl, BaseComponent, _,ImagePick,notify){

  var ImageView = BaseComponent.extend({
    config: function(data){
      _.extend(data, {
        imgs: [],
        limit: 10,
        descVisible:false,
        clazz:"m-imgview"
      })
    },
    name: "imageview",
    template: tpl,
    localReplace:function($event,img){
      switch($event.type){
        case "upload":
          var that = this;
          if($event.data){
            for(var i=0,len=$event.data.length;i<len;i++){
              var imageObj={src:$event.data[i].imgUrl};
               _.extend(img,imageObj,true);
          }
           _._$uploadImage2Category($event.data);
        }
       break;
      case "error":
          notify.notify({type:"error",message:"本地图片替换错误"});
       break;

      }
      

    },
    handleUpload: function($event){
      var data = this.data;
      // 根据upload传递的数据来判断处理逻辑
      switch($event.type){
        case "preview":
         
          break;
        case "upload":
          var that = this;
          if($event.data){
            for(var i=0,len=$event.data.length;i<len;i++){
              var imageObj={src:$event.data[i].imgUrl};
               that.data.imgs.push(imageObj);
          }
           _._$uploadImage2Category($event.data);
        }
         break;
        case "error":
            notify.notify({type:"error",message:"图片上传出错"});
          break;
      }
    },
    addImageTpl:function($event){
    	this.data.imgs.push({src:"",desc:""});
    },
    spaceReplace:function($event,img){
      var that=this;
      ImagePick._$allocate({max:1,onok:that.spaceReplaceCallback._$bind(that,$event,img)})._$show();
    },
    spaceReplaceCallback:function($event,img,obj){
      if(obj){
        var that = this;
        for(var i=0,len=obj.length;i<len;i++){
           this.$update(function(){
            var imageObj = {src:obj[i].imgUrl};
            _.extend(img,imageObj,true);
          });
        }
      }
    },
    spaceUpload:function($event,img){
    	var that=this;
    	ImagePick._$allocate({max:1,onok:that.spaceUploadCallBack._$bind(that,$event,img)})._$show();
    },
    spaceUploadCallBack:function($event,img,obj){
      if(obj){
        var that = this;
        for(var i=0,len=obj.length;i<len;i++){
           this.$update(function(){
            var imageObj = {src:obj[i].imgUrl};
            that.data.imgs.push(imageObj)
          });
        }
       
      }
    	
    },
    getImageList:function(){
      return this.data.imgs;
    }
  });

  return ImageView;

})