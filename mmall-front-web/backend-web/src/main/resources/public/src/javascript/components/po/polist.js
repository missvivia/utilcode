/**
 * 档期管理列表
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  "text!./polist.html",
  "{pro}components/ListComponent.js",
  "{pro}components/notify/notify.js",
  '{pro}extend/request.js'
  ], function(tpl, ListComponent,notify,Request){
  var ActList = ListComponent.extend({
    url: "/schedule/search",
    api:'/schedule/productlist/submit',
    template: tpl,
    config:function(data){
    	this.supr(data);
    },
    resetTime:function(_date){
    	_date.setHours(0);
    	_date.setMinutes(0);
    	_date.setSeconds(0);
    	return _date;
    },
    // @子类修改
    getExtraParam: function(data){
      return this.data.condition;
    },
    xdrOption:function(){
      return {method:'post'}
    },
    remove: function(_item,_index){
    	this.$request('/schedule/productlist/remove',{
            method:'POST',
            data:{id:act.id},
            onload:function(_json){
              if(_json.code==200){
            	  this.data.list.splice(_index,1)
                  //this.$emit('updatelist');
              } else{
                notify.show('删除失败');
              }
            },
            onerror:function(_error){
                notify.showError('审核操作失败');
            }
        });
      
    },
    isTimeOK:function(act){
    	var now = +new Date();
    	var startTime = new Date(act.startTime);
    	this.resetTime(startTime);
    	if((startTime - now)/(1000*60*60*24)<=4){
    		return false
    	} else{
    		return true;
    	}
    },
    judge:function(act){

      var time = act.startTime-act.curTime;
      if(true){

       var item = {};
      //item['id'] = act.id;
      this.$request(this.api,{
          method:'POST',
          data:{id:act.id},
          onload:function(_json){
            if(_json.code==400){
              notify.show('请添加售卖商品后提交审核');
            } else{
              item.status = 201
              notify.show('审核操作成功');
              this.$emit('updatelist');
            }
          },
          onerror:function(_error){
            if(_error.code == 400){
              notify.show({
                'type':'error',
                'message':'请添加售卖商品后提交审核'
              });
            }else{
              notify.showError('审核操作失败');
            }
          }
      });

      }
      else{
        notify.showError('离档期开始时间过短，无法提交审核');
      }
      
    }
  });
  
  var statusMap = {
        	"1":"待提交",
        	"2":"审核中",
        	"3":"审核通过",
        	"4":"审核未通过",
        	"-1":"失效"
      }
  ActList.filter("statusName", function(code){
    return statusMap[code]||code;
  })
      
      
  return ActList;

})