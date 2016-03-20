/*
 * ------------------------------------------
 * 消息管理模块
 * @version  1.0
 * @author   xiangwenbin(xiangwenbin@corp.netease.com)
 * ------------------------------------------
 */

NEJ.define([
  'base/util',
  'pro/extend/util',
  'pro/widget/BaseComponent',
  "pro/components/pager/pager",
  'pro/components/notify/notify',
  'text!./module.html',
], function(_u,_,BaseComponent,_p,notify,_html0){

    return BaseComponent.extend({
      url:'/app/feedback/list/',
      baseurl:'/app/feedback/',
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
      onPageChange:function(_page){
    	  this.condition.offset=5*(_page-1);
    	  this.$request(this.url, {
              data: this.condition,
              onload: function(json){
            	  var _data={};
            	  if(json.result){
//            		  _data.result = json.result;
                      _data.list =json.result.list;
                      _data.total=json.result.total;
                      _data.current=_page;
            	  }else{
//            		  _data.result=null;
            		  _data.list=[];
            	  }
            	  this.data=_data;
              },
              onerror: function(json){
                // @TODO: remove
              }
            });
      },
      //获取数据
      getList: function(_condition){
    	_condition=_.merge(_condition,{limit:5,offset:0});
    	this.condition=_condition;
        this.$request(this.url, {
          data: this.condition,
          onload: function(json){
        	  var _data={};
        	  if(json.result){
//        		  _data.result = json.result;
                  _data.list = json.result.list;
                  _data.total= json.result.total;
                  _data.current=1;
        	  }else{
//        		  _data.result=null;
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