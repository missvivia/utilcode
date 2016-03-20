/**
 * 档期banner列表
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define([
  "text!./list.html",
  "{pro}components/notify/notify.js",
  "{pro}components/ListComponent.js",
  '{pro}widget/regular/imageView.js',
  '{pro}extend/util.js'
  ], function(tpl, notify, ListComponent, ImageView,ut){
  var BannerList = ListComponent.extend({
    name: "m-bannerlist",
    template: tpl,
    config: function(data){
        ut.extend(data, {
          total: 1,
          current: 1,
          limit: 5,
          list: []
        });
        
        this.$watch(this.watchedAttr, function(){
          if(this.shouldUpdateList()) this.__getList();
        });
    },
    xdrOption:function(){
    	return {method:"post"};
    },
    init: function(){
      this.supr();

    },
    _sendReq:function(_url,_data){
        this.$request(_url,{
            method:'post',
            data:{
            	scheduleId: _data.id,
            	bannerStartImg: _data.banner.homeBannerImgUrl,
            	bannerNewImg: _data.banner.preBannerImgUrl
            },
            onload:function(_json){
                notify.show('操作成功');
                this.$emit('updatelist');
            },
            onerror:function(_error){
                notify.showError('操作失败');
            }
        });
    },
    _doSubmit:function(_id){
        var _name = 'submit.json',
            _url = this.api+_name,
            _data = this.data.list[_id];
        if( !!_data.banner && !!(_data.banner.preBannerImgUrl) && !!(_data.banner.homeBannerImgUrl)){
        	this._sendReq(_url,_data);
        }else{
        	notify.showError('必须上传两种banner图');
        }
        
    },
    submit:function(_id){
        this._doSubmit(_id);
    },
    handleUpload: function(id,type,$event){
      var data = this.data;
      // 根据upload传递的数据来判断处理逻辑
      switch($event.type){
        case "preview":
          this.$update(function(data){
            //data.list[id].percent = 0.1;
          });
          break;
        case "upload":
            this.$update(function(){
            	if(!(data.list[id].banner)){
       			    data.list[id].banner ={};
       		    }
            	if(type==0){
            		 data.list[id].banner.homeBannerImgUrl = $event.data[0].imgUrl;
            	}else{
            		 data.list[id].banner.preBannerImgUrl = $event.data[0].imgUrl;
            	}
            	//data.list[id].percent = 1;
        	});
            notify.show('上传成功');
            ut._$uploadImage2Category($event.data);
        	break;
        case "error":
          notify.showError('操作失败');
          this.$update(function(){
            //data.list[id].percent = 1;
          });
          break;
      }
      console.log($event);
    }
  });
  return BannerList;

});