/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./returnlist.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  "{lib}base/element.js?v=1.0.0.0",
  "{pro}extend/request.js?v=1.0.0.0",
  "{pro}components/notify/notify.js?v=1.0.0.0"
  ], function(tpl, ListComponent, e, Request, notify){

  var ReturnList = ListComponent.extend({
    url: "/order/return/getlist",
    name: "m-returnform",
    template: tpl,
    data: {
      status:7,
      statusMap: {
        9: '待退款',
        10: '已退款'
      }
    },
    changeStatus: function(status){
      this.data.current = 1;
      this.data.status = status
    },
    // @子类修改
    getExtraParam: function(data){
      return {
        status: data.status,
        timeRange:{
          startTime: data.startTime,
          endTime: data.endTime + 24*3600*1000 - 1
        },
        tag:data.tag
      }
    },
    xdrOption: function(){
      return {method:'POST'}
    },
    search: function(tag){
      this.data.current = 1;
      this.data['tag'] = tag;
      this.$emit('updatelist');
    },
    checkAll: function(event){
      var list = e._$getByClassName('wtable','j-check');
      if(event.target.checked){
        for(var i=0,len=list.length;i<len;i++){
          list[i].checked = true;
        }
      }else{
        for(var i=0,len=list.length;i<len;i++){
          list[i].checked = false; 
        }
      }
    },
    confirm: function(obj){
      var data = this.packData(obj),
          self = this;
      if(!!data.batchParam.length){
        Request('/order/return/cwbatchconfirm',{
          data:data,
          method:'POST',
          onload:function(json){
            if(json.code == 200){
              self.$emit('updatelist');
              if(!json.result.length){
                notify.show({
                  type:'success',
                  message:'确认成功!'
                })
              }else{
                notify.show({
                  type:'error',
                  message:'部分未确认成功！'
                })
              }
            }else{
              notify.show({
                type:'error',
                message:'确认失败！'
              })
            }
          },
          onerror: function(e){
            notify.show({
              type:'error',
              message:'确认失败'
            })
          }
        })
      }else{
        notify.show({
          type: 'error',
          message: '请勾选要确认的条目'
        })
      }
    },
    packData: function(obj){
      if(!obj){
        var checklist = e._$getByClassName('wtable','j-check'),
            list=[];
        for(var i=0,len=checklist.length;i<len;i++){
          if(checklist[i].checked){
            list.push({
              retPkgId: e._$dataset(checklist[i],'retid'),
              userId: e._$dataset(checklist[i],'userid')
            })
          }
        }
      }else{
        var item = {
          retPkgId: obj.retId,
          userId: obj.userId
        },list=[];
        list.push(item);
      }
      return {
        batchParam: list
      }
    }
  });
  return ReturnList;

})