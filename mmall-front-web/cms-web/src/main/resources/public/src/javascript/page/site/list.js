/**
 * 活动列表筛选
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "{pro}components/ListComponent.js",
  'pro/components/notify/notify',
  "{pro}extend/util.js",
  '{pro}page/access/account/delete.win.js',
  ], function(ListComponent,notify,_,DeleteWin){
  var SiteList = ListComponent.extend({
    url: "/site/list",
    name: "m-sitelist",
    api:"/site/",
    config: function(data){
        _.extend(data, {
          total: 1,
          current: 1,
          limit: 10,
          list: []
        });
        this.$watch(this.watchedAttr, function(){
          if(this.shouldUpdateList()) this.__getList();
        })
      },
    template: "#siteListTpl",

    // @子类修改
    watchedAttr: ['current', 'auditState','qvalue'],
    getExtraParam: function(data){
    	var param = {"isPage" : 1};
    	if(data.searchValue){
    		param["searchValue"] = data.searchValue;
    	}else{
    		delete param["searchValue"];
    	}
    	return param;
    },
    getListParam: function(){
        var data = this.data;
        return _.extend({
        	pageSize: data.limit,
        	currentPage: data.limit * (data.current-1),
          }, this.getExtraParam(data));
      },
    onKeyUp: function(e){
    	if(e.which == 13){
    		document.getElementById('search').click();
    	}
    },
    onSearch : function(e){
    	var _node = e.target;
    	this.data.searchValue = document.getElementById("key").value;
    	this.__getList();
    },
	_sendReq:function(_data){
        this.__deleteWin._$hide();
		this.$request("/site/delete",{
          method:'post',
          data:_data,
          norest : false,
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
	del: function(id){
      if(!!this.__deleteWin){
          this.__deleteWin._$recycle();
      }
      var _data = {"siteIds":id};      
      this.__deleteWin = DeleteWin._$allocate({
          parent:document.body,
          onok:this._sendReq._$bind(this,_data)
      })._$show();
    },

    batchDelete : function(){
    	var checkbox = document.getElementsByName("check");
    	var siteList = [];
    	for(var i = 0; i < checkbox.length;i++){
    		if(checkbox[i].checked){
    			siteList.push(checkbox[i].value); 
    		}
    	}
    	var siteIds = siteList.join(",");
    	var _data = {"siteIds":siteIds};
    	if(!!this.__deleteWin){
            this.__deleteWin._$recycle();
        }
        this.__deleteWin = DeleteWin._$allocate({
            parent:document.body,
            onok:this._sendReq._$bind(this,_data)
        })._$show();
    },
	__setItemCheck : function(e){
		var isChecked = e.target.checked;
		if(!isChecked){
			$("#all")[0].checked = false;
		}else{
			var trs = document.getElementsByName("check");
        	var count = 0;
			for(var i = 0;  i < trs.length;i++){
        		if(trs[i].checked){
        			count += 1;
        		}
        	}
			if(trs.length == count){
				$("#all")[0].checked = true;
			}
		}
	},
	__setAllCheck : function(e){
		var isChecked = e.target.checked;
		var trs = document.getElementsByName("check");
		for(var i = 0;  i < trs.length;i++){
			trs[i].checked = isChecked;
		}
	},
	__getList: function(){
	  var data = this.data;
      this.$request(this.url, {
        progress: true,
        data: this.getListParam(),
        onload: function(json){
          data.total = json.result.total;
          data.list = json.result.list || [];
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
  return SiteList;

})