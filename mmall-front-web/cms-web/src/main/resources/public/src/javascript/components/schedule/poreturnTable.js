/**
 * 档期管理列表
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  "text!./poreturnTable.html",
  "{pro}components/ListComponent.js",
  '{pro}extend/request.js',
  "{pro}components/modal/modal.js"
], function(tpl, ListComponent,request,Modal){
  var ActList = ListComponent.extend({
    url: "/oms/return/list",
    name: "m-actlist",
    template: tpl,
    xdrOption:function(){
    	return {method:'POST'}
    },
    remove: function(index){
      this.data.list.splice(index,1)
      this.$emit('updatelist');
    },
    createReturnedNote:function(index){
      var that=this;
      var poId=this.data.list[index].scheduleId;
      request('/oms/returnOrder/create/'+poId,{
        method:'GET',
        type:'json',
        onload:function(json){
          if(json&&json.code==200){
            that.remove(index);//删除列表中生成退货单的那项
            var modal = new Modal({
              data: {
                title: "生成退货单",
                coloseText:"继续生成",
                confirmTitle:"去查看"
              },
              content:"<div style='padding:15px 0;font-size:14px'>已成功生成退货单"+json.result+"张</div>",
              events: {
                confirm: function(data){//查看po退货单
                  location.href= '/schedule/returnnote';
                  this.destroy();
                },
                "close": function(){//继续生成
                  this.destroy();
                }
              }
            });
          }else{
            showFailModal();
          }
        },
        onerror:function(){
          showFailModal();
        }
      });
      //生成退货单失败弹窗
      function showFailModal(){
        var modal = new Modal({
          data: {
            title: "生成退货单",
            coloseText:"稍后再来",
            confirmTitle:"重新生成"
          },
          content:"<div style='padding:15px 0;font-size:14px'>生成退货单失败</div>",
          events: {
            confirm: function(data){//重新生成退货单
              that.createReturnedNote(poId);
              this.destroy();
            },
            "close": function(){//稍后再来
              this.destroy();
            }
          }
        });
      }
    }
  });
  return ActList;

})