/**
 * 活动列表筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!./list.html?v=1.0.0.2",
  "{pro}components/ListComponent.js?v=1.0.0.0",
  '{pro}components/notify/notify.js?v=1.0.0.0',
  "{pro}extend/util.js?v=1.0.0.0",
  '{pro}page/access/account/delete.win.js?v=1.0.0.0'
  ], function(tpl, ListComponent,notify,_,DeleteWin){
  var CouponList = ListComponent.extend({
    url: "/coupon/listData",
    name: "m-couponlist",
    api:"/coupon/",
    config: function(data){
        _.extend(data, {
          total: 1,
          current: 1,
          limit: 10,
          list: [],
          auditState:data.audit == 0?-1:1,
          qvalue:''
        });
        this.$watch(this.watchedAttr, function(){
          if(this.shouldUpdateList()) this.__getList();
        })
      },
    template: tpl,

    // @子类修改
    watchedAttr: ['current', 'auditState'],
    getExtraParam: function(data){
    	var param = {state: data.auditState,apply:(data.audit == 0?1:0),qvalue: data.qvalue};
    	return param;
    },
    onKeyUp: function(e){
    	if(e.which == 13){
    		var search = document.getElementById("search");
    		search.click();
    	}
    },
    onChange: function(e){
    	var _node = e.target;
    	this.data.current = 1;
    	this.data.auditState = _node.value;
    },
    onSearch : function(e){
    	var _node = e.target;
    	
    	this.data.qvalue = document.getElementById("key").value;
    	if(this.data.current == 1){
    		this.$emit("updatelist");
    	}else{
    		this.data.current = 1;
    		this.$update();
    	}
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
	  var that = this;
	  var _url = this.api + _opt,
	      _data = {id:_id,auditValue:_auditValue};
	  if(_opt == "delete" && _auditValue == 4){
	      if(!!this.__deleteWin){
	          this.__deleteWin._$recycle();
	        }
	        this.__deleteWin = DeleteWin._$allocate({
	          parent:document.body,
	          onok:function(){
	        	  that._sendReq(_url,_data);
	        	  that.__deleteWin._$hide();
	          }
	        })._$show();
	  }else{	
		  this._sendReq(_url,_data);
	  }
    },
	__getList: function(){
      var data = this.data;
      console.log(this.getListParam());
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
  return CouponList;

})