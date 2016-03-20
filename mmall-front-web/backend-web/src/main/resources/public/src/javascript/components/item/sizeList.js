/**
 * 活动列表筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!./sizeList.html",
  "{pro}components/ListComponent.js",
  "{pro}extend/request.js",
  'pro/components/notify/notify',
  'pro/widget/layer/delete.win'
  ], function(tpl, ListComponent,Request,notify,DeleteWin){
  var SizeList = ListComponent.extend({
    url: "/rest/sizeTemplate/search",
    name: "m-sizelist",
    template: tpl,
    xdrOption: function(){
      return {method:'POST'}
    },
    // // @子类修改
    // getExtraParam: function(data){
    //   return {status: data.status}
    // },
    remove: function(item,index,event){
      if(!!this.__deleteWin){
        this.__deleteWin._$recycle();
      }
      this.__deleteWin = DeleteWin._$allocate({
        parent:document.body,
        onok:this.onDelOK._$bind(this,item)
      })._$show();

    },
    onDelOK: function(item){
      var data = {};
      data['id'] = item.id;
      Request('/sizeTemplate/delete',{
        data:data,
        method:'GET',
        onload:this.delSuccess._$bind(this),
        onerror:function(e){
          if(e.code == 501){
            notify.show({
              'type':'error',
              'message':'尺码模版被引用，无法删除！'
            })
          }else{
            notify.show({
              'type':'error',
              'message':'删除失败'
            });
          }
        }
      })
    },
    delSuccess: function(json){
      if(json.code == 200){
        this.__deleteWin._$hide();
        notify.show('删除成功'); 
        this.$emit('updatelist');
      }
    }
  });
  return SizeList;

})