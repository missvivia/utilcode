/**
 * 活动列表筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "{pro}extend/util.js",
  "{lib}base/element.js",
  "text!./userGroupList.html",
  "{pro}components/ListComponent.js",
  "{pro}widget/layer/add.user.group.js",
  'pro/widget/layer/delete.win',
  "{pro}/extend/request.js",
  'util/chain/NodeList'
  ], function(_, e, tpl, ListComponent, UserGroupWin, DeleteWin, Request){
  var userGroupList = ListComponent.extend({
    url: "/authority/userGroup/getlist",
    name: "m-authorityform",
    template: tpl,
    // @子类修改
    // config: function(data){
    //   _.extend(data, {
    //     total: 1,
    //     current: 1,
    //     limit: 10,
    //     list: []
    //   });
    // },
    remove: function(id,_index){
      if(!!this.__deleteWin){
        this.__deleteWin._$recycle();
      }
      this.__deleteWin = DeleteWin._$allocate({
        parent:document.body,
        onok:this.onDelOK._$bind(this,id,_index)
      })._$show();
    },
    onDelOK: function(id,_index){
      var data = {};
          data['groupId'] = id;
      Request('/authority/userGroup/delete',{
        data:data,
        onload:this.delSuccess._$bind(this,_index),
        onerror:function(e){
          console.log(e);
        }
      })
    },
    delSuccess: function(_index,json){
      var list = this.data.list
      if(json.code == 200){
        list.splice(_index,1);
        this.data.total -= 1;
        this.$update();
      }
      this.__deleteWin._$hide();
    },
    updateList: function(json){
      if(json.code == 200){
        this.$emit('updatelist');
      }
    },
    edit: function(item){
      var list = {},
          name = e._$getByClassName(document,'j-flag');
      if(!item){//add
        this.showWin(list);
      }else{//edit
        var data = {};
        data['groupId'] = item.id;
        Request('/authority/userGroup/edit',{
          data:data,
          onload:this.getList._$bind(this,item),
          onerror:function(e){
            console.log(e);
          }
        });
      }
    },
    getList: function(item,json){
      var list = json.result.accessList;
      this.showWin(list,item);
    },
    showWin: function(list,item){
      if(!!this.userGroupWin){
        this.userGroupWin._$recycle();
      };
      this.userGroupWin = UserGroupWin._$allocate({
        parent:document.body,
        group:item,
        data:list,
        onok:this.onAddOK._$bind(this,item)
      })._$show();
      $(".js-error")[0].style.display = "none";
    },
    onAddOK: function(item,data){
//      if(!!item){//edit
//        item.name = data.name;
//        var date = new Date();
//        item.editTime = date.valueOf();
//      }else{//add
//        this.data.total += 1;
//        var list = this.data.list;
//        if(list.length == 10){
//          list.pop();
//        }
//          list.unshift(data);
//      }
		if(this.data.current == 1){
			this.$emit('updatelist');
		}else{
			this.data.current = 1;
			this.$update();
		}
    }
  });
  return userGroupList;

})