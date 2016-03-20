/**
 * 用户列表
 * author chenlei(chenlei@xyl.com)
 *
 */

NEJ.define([
	'{lib}base/util.js',
	'{lib}base/event.js',
	'{lib}base/element.js',
	'{pro}widget/module.js',
	'{pro}/widget/util/search.select.js',
    './userlist/userlist.js',
    './user/infowin.js?v=1.0.0.4',
    '{pro}extend/request.js',
	'{pro}components/notify/notify.js',
	 'pro/widget/layer/address/address',
	 '{lib}util/chain/NodeList.js'
  ],
  function(ut,v,e,Module,f,List,InfoWin,Request,notify,AddressWin,$,p) {
    var _pro;

    p._$$UserModule = NEJ.C();
    _pro = p._$$UserModule._$extend(Module);

    _pro.__init = function(_options) {
      this.__super(_options);
      
      var _form = f._$allocate({
          form:'search-form',
          onsearch:function(_data){
          	  if(!_data.searchValue){
          		  delete _data["searchValue"];
          	  }
        	  this.data = _data;
        	  this.searchList();
          }._$bind(this)
      });
      v._$addEvent("btn-create","click",this.__onAddUser._$bind(this));
      v._$addEvent('searchValue','keyup',this.__onKeyUp._$bind(this));
    };
    _pro.searchList = function(){
  	    var _data = this.data;
    	if(!this.__list){
    		this.__list = new List({
    			data:{
  				condition:_data
  				}
    		});
    		this.__list.$inject('#userlist');
    	}else{
    		this.__list.refresh(_data);
    	}    	
    };
    _pro.__onAddUser=function(){
    	var that = this;
		if(!!this.__infoWin){
			this.__infoWin._$recycle();
		}
		this.__infoWin = InfoWin._$allocate({
		  	parent:document.body,
		  	item:{},
		  	onok : function(data){
		  		that.__createUser(data);
		  	}
		})._$show();
    };
    _pro.__onKeyUp = function(_event){
    	if(_event.keyCode == 13){
    		e._$get('searchBtn').click();
    	}
    };
	//新建买家
	_pro.__createUser = function(data){
		var that = this;
		data.isModifyPass = 1;
		Request('/userInfo/addUser',{
			data:data,
			method:'post',
			type:'JSON',
			onload:function(dt){
				if(dt.code=="200"){
					notify.show({
						'type':'success',
						'message':dt.message
					});
					that.__infoWin._$hide();
					that.searchList();
					that.__createAddress(dt.result);
				}else{
					notify.show({
						'type':'error',
						'message':dt.message 
					});
				}
			},
			onerror:function(data){
				notify.show({
					'type':'error',
					'message':data.message
				});
			}
		})
	};
    _pro.__createAddress=function(userId){
        var that = this;

    	var url = "/userInfo/getArea/";
    	this.addressWin = AddressWin._$allocate({
        	type:0,
        	url : url,
        	title : "买家新建成功！请继续填写收货信息",
        	onok:function(data){
        		data.userId = userId;
        		this.__submitAddress(data);
        	}._$bind(this)
        });
    	
    	var cancelBtn = $(".m-form-addr .btn")[1];
    	$(cancelBtn)._$text("以后再填")._$attr("class","btn j-flag");
    	this.addressWin._$show();		
    };
    _pro.__submitAddress  = function(address){
	      if(address.provinceId == ""){
	    	  notify.notify({
  	            type: "error",
  	            message: '请选择地区'
  	          });
	    	  return;
	      }
	      Request('/userInfo/consigneeAddress/update',{
	        data: address,
	        method:'POST',
	        onload:function(result){
	      	  var result = {"code" : 200};
	          if(result.code == 200){
	        	  this.addressWin._$hide();
	          }else{
	        	  notify.notify({
	  	            type: "error",
	  	            message: json && json.message || '收货地址添加失败！'
	  	          }); 
	          }
	        }._$bind(this),
	        onerror:function(json){
	          notify.notify({
	            type: "error",
	            message: json && json.message || '收货地址添加失败！'
	          });
	        }
	      });
    };	
    p._$$UserModule._$allocate();
  });