/**
 * 订单列表筛选
 * author hzwujingfei(hzwujingfei@corp.netease.com)
 */

define([
  "text!./accountlist.html?v=1.0.0.1",
  "{pro}components/ListComponent.js",
  '{pro}page/access/account/add.account.win.js?v=1.0.0.1',
  '{pro}page/access/account/delete.win.js',
  '{pro}components/notify/notify.js',
  '{pro}extend/request.js'
  ], function(tpl, ListComponent, AccountWin, DeleteWin, notify, Request){
  var AccountList = ListComponent.extend({
    url: "/access/account/getlist",
    name: "m-accountlist",
    notMerge:true,
    template: tpl,
    watchedAttr: ['current','siteId', 'roleId','status'],
    // @子类修改
    edit: function(item){
      if(!item){ //add
        this.showWin();
      }else{ //edit
//        var data = {};
//        data['id'] = item.id;
//        Request('account/edit',{
//          data:data,
//          progress:true,
//          onload:this.getItem._$bind(this),
//          onerror: function(e){
//            console.log(e);
//          }
//        });
    	  this.showWin(item);
      }
      
    },
    getItem: function(json){
      if(json.code == 200){
        var item = json.result;
        this.showWin(item);
      }
    },
    showWin: function(item){
      if(!!this.__accountWin){
        this.__accountWin._$recycle();
      }

      this.__accountWin = AccountWin._$allocate({
        parent:document.body,
        item:item,
        onok:this.onAddOK._$bind(this)
      })._$show();
    },
    remove: function(id){
      if(!!this.__deleteWin){
        this.__deleteWin._$recycle();
      }
      this.__deleteWin = DeleteWin._$allocate({
        parent:document.body,
        onok:this._sendReq._$bind(this,'account/delete',{"id":id})
      })._$show();
    },
    batchDelete : function(){
    	var checkbox = document.getElementsByName("check");
    	var list = [];
    	for(var i = 0; i < checkbox.length;i++){
    		if(checkbox[i].checked){
    			list.push(checkbox[i].value); 
    		}
    	}
    	var ids = list.join(",");
    	var _data = {"ids":ids};
    	if(!!this.__deleteWin){
            this.__deleteWin._$recycle();
        }
        this.__deleteWin = DeleteWin._$allocate({
            parent:document.body,
            onok:this._sendReq._$bind(this,'/access/account/delBulk',_data)
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
    _sendReq: function(url,_data){
      var that = this;
      var data = {};
      var method = "get";
      if(url.indexOf("delBulk") > -1){
    	  method = "post";
      }
      Request(url,{
        data:_data,
        method : method,
        onload:function(json){
            if(json.code == 200){
            	that.$emit('updatelist');
              };
              that.__deleteWin._$hide();
        },
        onerror: function(e){
          notify.showError('删除失败');
        }
      });
    },
    lock: function(item){
      var data = {};
      data['id'] = item.id;
      Request('account/lock',{
        data:data,
        onload:this.updateList._$bind(this),
        onerror: function(e){
          console.log(e);
        }
      })
    },
    unlock: function(item){
      var data = {};
      data['id'] = item.id;
      Request('account/unlock',{
        data:data,
        onload:this.updateList._$bind(this),
        onerror: function(e){
          console.log(e);
        }
      })
    },
    updateList: function(json){
      if(json.code == 200){
        this.$emit('updatelist');
      }
    },
    onAddOK: function(){
      this.__accountWin._$hide(); 
      this.data.current = 1;
      this.$emit('updatelist');
    },
    getExtraParam: function(data){
    	var param = {};
    	if(data.searchValue){
    		param["searchValue"] = data["searchValue"];
    	}   	
    	if(data.siteId){
    		param["siteId"] = data["siteId"];
    	}
    	if(data.roleId){
    		param["roleId"] = data["roleId"];
    	}
    	if(data.status){
    		param["status"] = data["status"];
    	}
    	return param;
    },
    onKeyUp: function(e){
    	if(e.which == 13){
    		this.onSearch(e);
    	}
    },
    onSearch : function(e){
    	this.data.current = 1;
    	var _node = e.target;
    	this.data.searchValue = document.getElementById("key").value;
    	this.__getList();
    },
    onSiteChange: function(e){
    	this.data.current = 1;
    	var _node = e.target;
    	this.data.siteId = _node.value;
    },
    onRoleChange: function(e){
    	this.data.current = 1;
    	var _node = e.target;
    	this.data.roleId = _node.value;
    },
    onStatusChange: function(e){
    	this.data.current = 1;
    	var _node = e.target;
    	this.data.status = _node.value;
    },
    __getList: function(_option){
        _option = _option || {};
        var data = this.data;
        var self = this;
        var onload = _option.onload||function(){return !0};
        var option = {
          progress: true,
          sync : true,
          data: this.getListParam(),
          onload: function(json){
            if(json.code == 200){
              var result = json.result;
                list = result.list||[];
              if(!self.notMerge){
                _.mergeList(list, data.list,data.key||'id')
              }
              data.total = result.total;
              data.list = list||[];
              data.lastId = result.lastId;
              onload(result);
            }
          },
          // test
          onerror: function(json){
  			if(json.code == 403){
  				notify.show({
  					'type':'error',
  					'message':json.message
  				});
  			}
            // @TODO: remove
          }
        };
        //继承类提供xdrOption方法，用来表明请求类型
        /**
         * function(){
         *  return {method:'POST',norest:true}
         & }
        **/
        if(this.xdrOption){
          var xdrOpt = this.xdrOption();
          if(xdrOpt.norest){
            option.data = _ut._$object2query(this.getListParam());
            option.norest = true;
          }

          option.method = xdrOpt.method||'GET';

        }
        this.$request(this.url,option)
    }
  });
  return AccountList;

})