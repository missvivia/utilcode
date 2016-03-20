/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./rolelist.html",
  "{pro}components/ListComponent.js",
  '{pro}page/access/role/add.role.win.js',
  '{pro}page/access/account/delete.win.js',
  '{pro}extend/request.js'
  ], function(tpl, ListComponent, RoleWin, DeleteWin, Request){
  var RoleList = ListComponent.extend({
    url: "/access/role/getlist",
    name: "m-rolelist",
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
      data['roleId'] = item.id;
      Request('role/delete',{
        data:data,
        onload: this.delSuccess._$bind(this),
        onerror: function(e){
          console.log(e);
        }
      });
    },
    delSuccess: function(json){
      if(json.code == 200){
        this.$emit('updatelist');
      };
      this.__deleteWin._$hide();
    },
    updateList: function(json){
      if(json.code == 200){
        this.$emit('updatelist');
      }
    },
    edit: function(item){
      if(!item){ //add
        // var item = {};
        this.showWin(item);
      }else{ //edit
        var data = {};
        data['roleId'] = item.id;
        Request('/access/role/edit',{
          data:data,
          onload:this.getItem._$bind(this),
          onerror:function(e){
            console.log(e);
          }
        });
      }
    },
    getItem: function(json){
      var item = json.result;
      this.showWin(item);
    },
    showWin: function(item){
      if(!!this.__roleWin){
        this.__roleWin._$recycle();
      }
      this.__roleWin = RoleWin._$allocate({
        parent:document.body,
        list:item,
        onok:this.onAddOK._$bind(this,item)
      })._$show();
    },
    onAddOK: function(item,data){
      // if(!!item) item.name = data.name;//edit
      // else{//add
      //   var list = this.data.list;
      //   if(list.length == 10){
      //     list.pop();
      //   }
      //   list.unshift(data);
      // }
      this.$emit('updatelist');
    }
  });
  return RoleList;

})