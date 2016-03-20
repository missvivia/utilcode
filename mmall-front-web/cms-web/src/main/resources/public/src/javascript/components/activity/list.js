/**
 * 活动列表筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!./list.html?v=1.0.0.0",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  '{pro}components/notify/notify.js?v=1.0.0.0',
  "{pro}extend/util.js?v=1.0.0.0"
  ], function(tpl, ListComponent,notify,_){
  var ActList = ListComponent.extend({
    url: "/promotion/listData.json",
    name: "m-actlist",
    api:"/promotion/",
    template: tpl,
    config: function(data){
        _.extend(data, {
          total: 1,
          current: 1,
          limit: 10,
          list: [],
          auditState:data.audit == 0?-1:1,
          city:0
        });

        this.$watch(this.watchedAttr, function(){
          if(this.shouldUpdateList()) this.__getList();
        })
      },
    // @子类修改
    watchedAttr: ['current', 'auditState', 'city'],
    getExtraParam: function(data){
      return {state: data.auditState, province:data.city,apply:(data.audit == 0?1:0)};
    },
    onChangeAudit: function(e){
    	var _node = e.target;
    	this.data.current = 1;
    	this.data.auditState = _node.value;
    },
    onChangeCity: function(e){
    	var _node = e.target;
    	this.data.current = 1;
    	this.data.city = _node.value;
    },
	_sendReq:function(_url,_data){
      this.$request(_url,{
          method:'post',
          query:_data,
          onload:function(_json){
        	  notify.notify({
                  type: "success",
                  message: _json.message
                });
              this.$emit('updatelist');
          },
          onerror:function(_error){
        	  notify.notify({
                  type: "error",
                  message: _error.message
                });
          }
      });
	},
	operate: function(_id,_opt,_auditValue){
	  var _url = this.api + _opt,
	      _data = {id:_id,auditValue:_auditValue};
	  	  	
      this._sendReq(_url,_data);
    },
	__getList: function(){
      var data = this.data;
      this.$request(this.url, {
        progress: true,
        data: this.getListParam(),
        onload: function(json){
          data.total = json.total;
          data.list = json.list;
        },
        onerror: function(json){
        	notify.notify({
                type: "error",
                message: "网络异常，稍后再试！"
              });
        }
      })
    }
  });
  return ActList;

})