/**
 * 档期管理列表
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  "text!./pagelist.html",
  "{pro}components/ListComponent.js",
  "{pro}extend/util.js",
  "{pro}extend/config.js",
  "base/util",
  "pro/components/notify/notify",
  "pro/widget/layer/sure.window/sure.window"
  ], function(tpl, ListComponent,_,config,ut,notify,SureWindow){
  var ActList = ListComponent.extend({
    url: "/schedule/pages/search",
    api:'/schedule/pages/submit',
    data:{config:config},
    template: tpl,
    // @子类修改
    getExtraParam: function(data){
      return _.extend(this.data.condition,{status: data.status},true);
    },
    xdrOption: function(){
        return {method:'POST'}
      },
    remove: function(index){
      this.data.list.splice(index,1)
      this.$emit('updatelist');
    },
    submit:function(item){
    	SureWindow._$allocate({text:'确认提交',title:'确认提交',onok:function(){
    		this.$request(this.api,{
            method:'POST',
            data:{id:item.pageId,scheduleId:item.id},
            onload:function(_json){
            	if(_json.code==200){
            		notify.show('操作成功');
                	item.status = 2;
                	item.reason ='';
            	} else{
            		notify.show(_json.message);
            	}
                //this.$emit('updatelist');
            },
            onerror:function(_error){
                notify.showError('提交失败');
            }
        });
    	}._$bind(this)})._$show();
    }
  });
  
  ActList.filter('statusMap',function(_status){
	 var map = {
			 '1':'待提交',
			 '2':'审核中',
			 '3':'审核通过',
			 '4':'审核未通过',
			 '-1':'失效'
	 } 
	 return map[_status]
  });
  return ActList;

})