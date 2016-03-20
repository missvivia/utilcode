/*
 * ------------------------------------------
 * 帮助中心模块
 * @version  1.0
 * @author   xiangwenbin(xiangwenbin@corp.netease.com)
 * ------------------------------------------
 */

NEJ.define([
  'base/util',
  'pro/extend/util',
  'pro/widget/BaseComponent',
  'pro/components/notify/notify',
  'pro/components/window/base',
  'text!./module.html'
], function(_u,_,BaseComponent,notify,win0,_html0){

    return BaseComponent.extend({
      url:'/content/helpcenter/article/search',
      baseurl:'/content/helpcenter/article/',
      template: _html0,
      config: function(data){
        _.extend(data, {
          result: {}
        });
      },
      init: function(){
        // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
        //this.$on('updatelist', this.getList.bind(this));
        // this.getList();
      },
      del:function(index){
        var modal = new win0({
          data:{
            'content': '是否删除该条记录',
            'title': '删除记录'
          }
        });

        modal.$on('confirm', function(){
          //确认删除
          this.$request(this.baseurl + 'delete', {
            data: {id:this.data.list[index].id},
            onload: function(json){
              if (json.code == 200){
                this.data.list.splice(index, 1);
                this.$update();
              }
            }._$bind(this),
            // test
            onerror: function(json){
              notify.showError('删除失败');
            }
          });
        }._$bind(this));
        modal.$inject('body');
      },
      release:function(index){
    	  this.$request(this.baseurl + 'publish', {
              data: {id:this.data.list[index].id},
              onload: function(json){
                if (json.code == 200){
                  this.data.list[index]=json.result;
                  this.$update();
                }
              }._$bind(this),
              // test
              onerror: function(json){
                notify.showError('发布失败');
              }
            });
      },
      offline:function(index){
    	  this.$request(this.baseurl + 'revoke', {
              data: {id:this.data.list[index].id},
              onload: function(json){
                if (json.code == 200){
                  this.data.list[index]=json.result;
                  this.$update();
                }
              }._$bind(this),
              // test
              onerror: function(json){
                notify.showError('发布失败');
              }
            });
      },
      //获取数据
      getList: function(_condition){
    	_condition=_.merge(_condition,{limit:100,offset:0});
    	var _data={};
        this.$request(this.url, {
          data: _condition,
          onload: function(json){
        	  if(json.result){
        		  _data.result = json.result;
                  _data.list = json.result.list;
        	  }else{
        		  _data.result=null;
        		  _data.list=[];
        	  }
        	  this.data=_data;
          },
          onerror: function(json){
            // @TODO: remove
          }
        });
      }
  });
});