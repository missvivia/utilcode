/**
 * 档期选择列表
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */


define([
  "pro/extend/util",
  "text!./poSearchList.html",
  "{pro}components/ListComponent.js",
  "{pro}components/po/provinceSelect.js",
  'pro/components/notify/notify'
  ], function(_, tpl ,ListComponent, Modal,notify){


  // 档期选择弹框
  var PoSearchList  = ListComponent.extend({
    url: "/schedule/validlist",
    template: tpl,
    data: {
      limit: 15
    },
    config: function(data){
      this.supr(data);
      _.extend(data, {
        selected: []
      })
    },
    init: function(){
      this.supr();
      this.$on("updatelist", function(){
        this.firstSearch = true;
      })
    },
    isChecked: function(sche){
      var data = this.data;
      return _.findInList(sche.id, data.selected) > -1;
    },
    shouldUpdateList: function(){
      return !!this.firstSearch;
    },
    getExtraParam: function(data){
      var _ds = new Date(),
          _de = new Date();
      _ds.setTime(data.startTime);
      _de.setTime(data.endTime);
      return { 
        curSupplierAreaId: data.provinceCode,
        startDate: _.setDateTOStart(_ds),
        endDate: _.setDateTOEnd(_de)
       }
    },
    select: function(sche){
      var data = this.data;
      var index = _.findInList(sche.id, data.selected);
      var _ds = new Date(),
          _de = new Date();
      _ds.setTime(data.startTime);
      _de.setTime(data.endTime);
      if(~index){
        data.selected.splice(index, 1);
      }else{
    	  this.$request("/promotion/checkpo", {
              method:"post",
              progress: true,
              query: {id:(_.getSearch().id)||0,poId:sche.id,start:_.setDateTOStart(_ds),end:_.setDateTOEnd(_de)},
              onload: function(json){
            	  if(json.code == 200){
            		  data.selected.push(sche);
            	  }else{
            		  notify.notify({
                          type: "error",
                          message: json.message
                        });
            	  }
            	  
            	  
              },
              // test
              onerror: function(json){
            	  notify.notify({
                      type: "error",
                      message: json.message
                    });
              }
            });
            
      }
    },
    __getList: function(){
    	var query = this.getListParam();
    	if (query.startDate > query.endDate) {
    		notify.notify({
                type: "error",
                message: "开始时间不能大于结束时间"
              });
    		return;
        }
        var data = this.data;
        this.$request(this.url, {
          method:"post",
          progress: true,
          data: this.getListParam(),
          onload: function(json){
        	var list = json.result||[],
        		resultList = [];
            for(var i=0;i<list.length;i++){
            	resultList.push({id:list[i].id,title:list[i].title,startTime:list[i].startTime,areaName:list[i].areaName});
            }
            data.total = json.total;
            data.list = resultList;
            
          },
          // test
          onerror: function(json){
        	  notify.notify({
                  type: "error",
                  message: "网络异常，稍后再试！"
                });
        	  
          }
        })
      }
  })

  return PoSearchList;

})