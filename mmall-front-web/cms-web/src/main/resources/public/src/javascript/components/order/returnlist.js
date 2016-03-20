/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./returnlist.html?v=1.0.0.1",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  "{lib}base/element.js?v=1.0.0.0",
  "{pro}extend/request.js?v=1.0.0.0",
  "{pro}components/notify/notify.js?v=1.0.0.0",
  "{pro}components/order/win/reject.win.js?v=1.0.0.0",
  "{pro}components/order/win/return.pass.win.js?v=1.0.0.0",
  "{pro}components/order/win/topay.pass.win.js?v=1.0.0.0",
  "{pro}components/order/win/cancel.request.win.js?v=1.0.0.0",
  "{lib}util/form/form"
  ], function(tpl, ListComponent, e, Request, notify, RejectWin, PassWin, GobackWin, CancReqWin, _t){

  var ReturnList = ListComponent.extend({
    url: "/order/return/getlist",
    name: "m-returnform",
    template: tpl,
    data: {
      status:0,
      typeList:__typeList__,
      statusMap:{
        2:"待收货",
        3:"异常件待退款",
        4:"拒绝",
        6:"已退款",
        7:"异常件待退款",
        8:"拒绝",
        9:"正常件待退款",
        10:"已退款",
        11:"已取消"
      },
      startTime:new Date().setTime(1420041600000),//设置默认起始时间为2015-01-01
      endTime:new Date().getTime()
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
        search:{
          searchType:data.searchType,
          searchKey:data.searchKey
        },
        tag:data.tag
      }
    },
    xdrOption: function(){
      return {method:'POST'}
    },
    search: function(tag){
      this.data.current = 1;
      var list = e._$getByClassName('searchbox','j-flag');
      this.data['searchType'] = list[0].value;
      this.data['searchKey'] = list[1].value;
      this.data['tag'] = tag;
      this.$emit('updatelist');
    },
    reject: function(item){
      if(!!this.rejectWin){
        this.rejectWin._$recycle();
      }
      this.rejectWin = RejectWin._$allocate({parent:document.body,onok:this.onRejectOK._$bind(this,item)})._$show();
    },
    onRejectOK: function(item){
      var form = _t._$$WebForm._$allocate({
        form:'rejectForm'
      });
      if(form._$checkValidity()){
        var data = form._$data();
        data['userId'] = item.userId;
        data['retId'] = item.retId;
        Request('/order/return/reject',{
          data: data,
          method:'POST',
          onload: this.rejectSuccess._$bind(this),
          onerror: function(){
            notify.showError('拒绝错误');
          }
        });
      };
    },
    onKeyUp: function(event){
    	if(event.which == 13){
    		e._$get('search1').click();
    	}
    },
    rejectSuccess: function(json){
      if(json.code == 200){
        notify.show('拒绝成功');
        this.$emit('updatelist');
        this.rejectWin._$hide();
      }else{
        notify.show({
          type:'success',
          message:'拒绝失败！'
        })
        this.rejectWin._$hide();
      }
    },
    pass: function(item){
      if(!!this.PassWin){
          this.PassWin._$recycle();
      }
      this.PassWin = PassWin._$allocate({title:'通过',parent:document.body,onok:this.onPassOK._$bind(this,item)})._$show();
    },
    onPassOK: function(item){
      var form = _t._$$WebForm._$allocate({
        form:'passForm'
      });
      if(form._$checkValidity()){
        var data = form._$data();
        data['userId'] = item.userId;
        data['retId'] = item.retId;
        Request('/order/return/pass',{
          data:data,
          method:'POST',
          onload:this.passSucess._$bind(this),
          onerror:function(){
            notify.show({
              type:'error',
              message:'退款失败！'
            })
          }
        })
      }
    },
    passSucess: function(json){
      if(json.code == 200){
        notify.show('退款成功');
        this.$emit('updatelist');
        this.PassWin._$hide();
      }else{
        notify.show({
          type:'error',
          message:'退款失败！'
        });
        this.PassWin._$hide();
      }
    },
    goback: function(item){
      if(!!this.gobackWin){
          this.gobackWin._$recycle();
      }
      this.gobackWin = GobackWin._$allocate({title:'撤销',parent:document.body,onok:this.onGobackOK._$bind(this,item)})._$show();
    },
    onGobackOK: function(item){
      var data = {},
          self = this;
      data['retId'] = item.retId;
      data['userId'] = item.userId;
      Request('/order/return/goback',{
        data:data,
        method:'POST',
        onload:function(json){
          if(json.code == 200){
            notify.show('撤销成功');
            self.$emit('updatelist');
            self.gobackWin._$hide();
          }else{
            notify.show({
              type:'error',
              message:'撤销失败'
            });
            self.gobackWin._$hide();
          }
        },
        onerror:function(e){
          notify.showError('撤销失败');
        }
      })
    },
    cancelRequest: function(item){
      if(!!this.cancelReq){
          this.cancelReq._$recycle();
      }
      this.cancelReq = CancReqWin._$allocate({parent:document.body,onok:this.onCancelOK._$bind(this,item)})._$show();
    },
    onCancelOK: function(item){
      var data = {},
          self = this;
      data['retId'] = item.retId;
      data['userId'] = item.userId;
      Request('/order/return/deprecate',{
        data:data,
        onload:function(json){
          if(json.code == 200){
            notify.show('取消申请成功');
            self.$emit('updatelist');
            self.cancelReq._$hide();
          }else{
            notify.show({
              type:'error',
              message:'取消申请失败'
            });
            self.cancelReq._$hide();
          }
        },
        onerror:function(e){
          notify.showError('取消申请失败');
        }
      })
    }
  });
  return ReturnList;

})