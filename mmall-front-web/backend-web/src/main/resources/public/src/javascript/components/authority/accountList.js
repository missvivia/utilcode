/**
 * 活动列表筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!./accountList.html?v=1.0.0.0",
  "{pro}components/ListComponent.js",
  '{pro}widget/layer/add.user.js?v=1.0.0.0',
  'pro/widget/layer/delete.win',
  '{pro}extend/request.js',
  'pro/components/notify/notify',
  'util/chain/NodeList'
  ], function(tpl, ListComponent, AddUserWin, DeleteWin, Request, notify,$){
  var AccountList = ListComponent.extend({
    url: "/authority/user/getlist",
    name: "m-accountform",
    template: tpl,
    // @子类修改
    remove: function(item){
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
      Request('user/delete',{
        data:data,
        progress:true,
        onload:this.delSuccess._$bind(this),
        onerror:function(e){
          console.log(e);
        }
      });
    },
    delSuccess: function(json){
      if(json.code == 200){
        this.$emit('updatelist');
      }
      this.__deleteWin._$hide();
    },
    updateList: function(json){
      if(json.code == 200){
        this.$emit('updatelist');
      }
    },
    edit: function(item){
      if(!item){ //add
        var item = {};
        this.showWin(item);
      }else{ //edit
        // this.showWin(item);
        var data = {};
        data['id'] = item.id;
        Request('user/edit',{
          data:data,
          onload:this.getList._$bind(this),
          onerror:function(e){
            notify.show({
              'type':'error',
              'message':'获取信息失败'
            })
          }
        });
      }
    },
    getList: function(json){
      var item = json.result;
      this.showWin(item);
    },
    showWin: function(item){
      if(!!this.addUserWin){
        this.addUserWin._$recycle();
      }
      this.addUserWin = AddUserWin._$allocate({parent:document.body,info:item,onok:this.onAddOK._$bind(this,item)})._$show();
      $(".js-error")[0].style.display = "none";
    },
    onAddOK: function(item,data,isAdd){
      // if(!isAdd){//edit
      //   var date = new Date();
      //   item.name = data.name;
      //   item.displayName = data.displayName;
      //   item.department = data.department;
      //   item.editTime = date.valueOf();
      // }else{//add
      //   var list = this.data.list;
      //   if(list.length == 10){
      //     list.pop();
      //   };
      //   list.unshift(data);
      // }
		if(this.data.current == 1){
			this.$emit('updatelist');
		}else{
			this.data.current = 1;
			this.$update();
		}
    }
  });
  return AccountList;

})