/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./topaylist.html?v=1.0.0.1",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  "{pro}components/form/form.js?v=1.0.0.0",
  '{pro}components/order/win/topay.pass.win.js?v=1.0.0.0',
  '{pro}components/order/win/reject.win.js?v=1.0.0.0',
  '{lib}base/element.js?v=1.0.0.0',
  '{pro}extend/request.js?v=1.0.0.0',
  '{pro}components/notify/notify.js?v=1.0.0.0',
  '{lib}util/form/form.js?v=1.0.0.0'
  ], function(tpl, ListComponent, form, TopPassWin, RejectWin, e, Request, notify, _t){

  var TopayList = ListComponent.extend({
    url: "/order/topay/getlist",
    name: "m-topayform",
    template: tpl,
    computed: {
      start: function(data){
        var value = data.startHour || "";
        var tmp = value.split(":");
        var hh = parseInt(tmp[0] || "0");
        var mm = parseInt(tmp[1] || "0");
        return hh * 3600 * 1000 + 60 * 1000 * mm + data.startTime;
      },
      end: function(data){
        var value = data.endHour || "";
        var tmp = value.split(":");
        var hh = parseInt(tmp[0] || "0");
        var mm = parseInt(tmp[1] || "0");
        return hh * 3600 * 1000 + 60 * 1000 * mm + data.endTime;
      }
    },
    data: {
      status:0,
      isOK:true,
      typeList:__typeList__,
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
          startTime: this.$get('start'),
          endTime: this.$get('end')
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
      if(this.$get('start') > this.$get('end')){
        notify.show({
          'type':'error',
          'message':'请选择正确的时间范围！'
        })
      }else if(this.data.isOK){
        this.$emit('updatelist');
      }else{
        notify.show({
          'type':'error',
          'message':'时间格式不正确！'
        })
      }
    },
    //事件方式验证时间输入正确性
    checkTime: function(event){
      var time = event.target.value;
      if(/^([0-1]?[0-9]|2[0-4]):[0-5]?[0-9]$/.test(time)){
        e._$delClassName(event.target,'j-invalid');
        var list = time.split(':');
        var hour = parseInt(list[0]),
            min = parseInt(list[1]);
        this.data.isOK = true;
      }else if(!time){
        this.data.isOK = true;
      }else{
        e._$addClassName(event.target,'j-invalid');
        this.data.isOK = false;
      }
    },
    onInput: function(event){
      e._$delClassName(event.target,'j-invalid');
    },
    onKeyUpTime: function(event){
    	if(event.which == 13){
    		e._$get("searchTime").click();
    	}
    },
    onKeyUpID: function(event){
    	if(event.which == 13){
    		e._$get("searchId").click();
    	}
    },
    pass: function(_item,_index){
      var _url = '/order/topay/pass';
      if(!!this.topPassWin){
          this.topPassWin._$recycle();
      }
      this.topPassWin = TopPassWin._$allocate({title:'通过',parent:document.body,onok:this.onOKClick._$bind(this,_item,_url,_index)})._$show();
    },
    reject: function(_item,_index){
      var _url = '/order/topay/reject';
      if(!!this.rejectWin){
          this.rejectWin._$recycle();
      }
      this.rejectWin = RejectWin._$allocate({parent:document.body,onok:this.onDeleteOK._$bind(this,_item,_url,_index)})._$show();
    },
    goback: function(_item,_index){
      var _url = '/order/topay/goback';
      if(!!this.goBackWin){
          this.goBackWin._$recycle();
      }
      this.goBackWin = TopPassWin._$allocate({title:'撤销',parent:document.body,onok:this.onOKClick._$bind(this,_item,_url,_index)})._$show();
    },
    addressblist: function(_item,_index){
      var _url = '/order/topay/addblackaddress';
      if(!!this.addressWin){
          this.addressWin._$recycle();
      }
      this.addressWin = TopPassWin._$allocate({title:'添加地址黑名单',parent:document.body,onok:this.onOKClick._$bind(this,_item,_url,_index)})._$show();
    },
    adduserblist: function(_item,_index){
      var _url = '/order/topay/addblackuser';
      if(!!this.userWin){
          this.userWin._$recycle();
      }
      this.userWin = TopPassWin._$allocate({title:'添加用户黑名单',parent:document.body,onok:this.onOKClick._$bind(this,_item,_url,_index)})._$show();
    },
    onDeleteOK: function(item,url,_index){
      var form = _t._$$WebForm._$allocate({
        form:'rejectForm'
      });
      if(form._$checkValidity()){
    	var list = this.data.list;
        var data = form._$data();
        var self = this;
        data['logId'] = item.id,
        data['orderId'] = item.orderId,
        data['userId'] = item.userId;
        Request(url,{
          data:data,
          method:'POST',
          onload:function(json){
            if(json.code == 200){
              notify.show('成功');
              self.rejectWin._$hide();
              self.$emit("updatelist");
            } else if(json.code == 201){
            	list.splice(_index,1);
            	notify.show('到付审核记录已失效');
            }
          },
          onerror:function(e){
            notify.show({
              'type':'error',
              'message':'删除失败'
            })
          }
        })
      }
    },
    onOKClick: function(item,url,_index){
      var data = {};
      var self = this;
      var list = this.data.list;
      data['logId'] = item.id,
      data['orderId'] = item.orderId,
      data['userId'] = item.userId,
      data['extInfo'] = item.account;
      Request(url,{
        data:data,
        method:'POST',
        onload:function(json){
          if(json.code == 200){
            notify.show('成功');
            self.$emit("updatelist");
          } else if(json.code == 201){
        	  list.splice(_index,1)
          	notify.show('到付审核记录已失效');
          }
        },
        onerror:function(e){
          console.log(e);
        }
      })
    }
  }).use("$form");
  return TopayList;

})


