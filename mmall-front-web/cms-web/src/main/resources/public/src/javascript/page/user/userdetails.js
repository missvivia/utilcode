/**
 * 用户信息详情
 * author chenlei(chenlei@xyl.com)
 *
 */

NEJ.define([
	'{lib}base/util.js',
	'{lib}base/event.js',
	'{lib}base/element.js',
	'{pro}widget/module.js',
	'{lib}util/form/form.js',
	'./tab/tab.js',
    './order/order.js?v=1.0.0.1',
    './coupon/coupon.js',
    './user/infowin.js?v=1.0.0.3',
    '{pro}widget/layer/address/address.js?v=1.0.0.0',
    '{pro}/extend/request.js',
    '{pro}components/notify/notify.js',
    '{pro}widget/layer/sure.window/sure.window.js'
  ],
  function(ut,v,e,Module,f,tab,List,CouponList,InfoWin,AddressWin,Request,notify,Suerwindow,p) {
	
    var _pro;

    p._$$InfoModule = NEJ.C();
    _pro = p._$$InfoModule._$extend(Module);

    _pro.__init = function(_options) {
    	this.__super(_options);
    	this.__initPage();
    };
    _pro.__initPage=function(){
    	this.baseInfo = e._$get('baseInfo');
    	this.addressInfo = e._$get('addressInfo');
    	this.couponInfo = e._$get('couponInfo');
    	this.orderlist = e._$get('orderlist');
    	
    	this.__Form = f._$$WebForm._$allocate({
			form:'userinfo'
		});
    	this.data = this.__Form._$data();
    	
    	this.__AddrForm = f._$$WebForm._$allocate({
			form:'addrinfo'
		});
    	this.addrData = this.__AddrForm._$data();
    	
    	this.__tab = new tab();
        this.__tab.$inject('#tab-box');
        this.__tab.$on('change',this.__onTabChange._$bind(this));
        this.__tab.go(0);
        this.__tab.$update();
    	
    	v._$addEvent("updateinfo","click",this.__updateInfo._$bind(this));
    	v._$addEvent("updateAddress","click",this.__updateAddress._$bind(this));
    	v._$addEvent("lockUser","click",this.__lockUser._$bind(this));
    	v._$addEvent("unlockUser","click",this.__unlockUser._$bind(this));
    };
    _pro.__onTabChange = function(index){
    	if(index == 0){
    		e._$replaceClassName(this.baseInfo,"f-dn","f-db");
    		e._$replaceClassName(this.addressInfo,"f-dn","f-db");
    		e._$replaceClassName(this.couponInfo,"f-db","f-dn");
    		e._$replaceClassName(this.orderlist,"f-db","f-dn");
    	}else if(index == 2){
    		e._$replaceClassName(this.couponInfo,"f-dn","f-db");
    		e._$replaceClassName(this.baseInfo,"f-db","f-dn");
    		e._$replaceClassName(this.addressInfo,"f-db","f-dn");
    		e._$replaceClassName(this.orderlist,"f-db","f-dn");
    		if(!this.__couponlist){
        		this.__couponlist = new CouponList({
        			data:{
        				condition:{
        					userId:this.data.uid
        				}
        			}
        		});
        		this.__couponlist.$inject('#couponInfo');
        	}else{
        		this.__couponlist.refresh({userId:this.data.uid});
        	}
    	}else if(index == 3){
    		e._$replaceClassName(this.orderlist,"f-dn","f-db");
    		e._$replaceClassName(this.couponInfo,"f-db","f-dn");
    		e._$replaceClassName(this.baseInfo,"f-db","f-dn");
    		e._$replaceClassName(this.addressInfo,"f-db","f-dn");
    		if(!this.__list){
        		this.__list = new List({
        			data:{
        				condition:{
        					userId:this.data.uid
        				}
        			}
        		});
        		this.__list.$inject('#orderlist');
        	}else{
        		this.__list.refresh({userId:this.data.uid});
        	}
    	}
    };
    _pro.__updateInfo=function(){
		var that = this;
    	if(!!this.__infoWin){
			this.__infoWin._$recycle();
		}
		this.__infoWin = InfoWin._$allocate({
		  	parent:document.body,
		  	item:this.data,
		  	onok : function(data){
		  		that.__updateUser(data);
		  	}
		})._$show();
    };
	//修改用户信息
	_pro.__updateUser = function(data){
		var that = this;
		if(data.password=="******"){
			data['isModifyPass']=0;
		}else{
			data['isModifyPass']=1;    
		}
		Request('/userInfo/userUpdate',{
			data:data,
			method:'POST',
			type:'JSON',
			progress:true,
			onload:function(dt){
				if(dt.code=="200"){
					notify.show({
						'type':'success',
						'message':dt.message
					});
					window.location.reload();
					that.__infoWin._$hide();
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
    _pro.__updateAddress=function(){
        var that = this;
    	var url = "/userInfo/getArea/";
    	this.addressWin = AddressWin._$allocate({
        	type:this.addrData .address != "" ? 1 : 0,
        	address:this.addrData,
        	url : url,
        	onok:function(data){
        		this.__submitAddress(data);
        	}._$bind(this)
        })._$show();		
    };
    _pro.__submitAddress  = function(address){
    	var that = this;
    	address.userId = this.data.uid;
    	address.id = this.addrData.id;
	      if(address.provinceId == ""){
	    	  notify.notify({
  	            type: "error",
  	            message: '请选择地区'
  	          });
	      }else{
		      Request('/userInfo/consigneeAddress/update',{
		        data: address,
		        method:'POST',
		        progress:true,
		        onload:function(result){
		          if(result.code == 200){
		        	  location.href="/userInfo/userDetail/" + this.data.uid
		        	  that.addressWin._$hide();
		          }else{
		        	  notify.notify({
		  	            type: "error",
		  	            message: json && json.message || '编辑地址失败！'
		  	          }); 
		          }
		        }._$bind(this),
		        onerror:function(json){
		          notify.notify({
		            type: "error",
		            message: json && json.message || '编辑地址失败！'
		          });
		        }
		      });
	      }
    };
    _pro.__lockUser = function(){
    	this.__lockWin = Suerwindow._$allocate({
            title:'请确认是否冻结该账户',
            text:'用户ID：'+this.data.uid+'，用户名：'+this.data.account,
            onok:this.__doLock._$bind(this)
        });
        this.__lockWin._$show();
    };
    _pro.__doLock = function(){
    	var that = this;
    	Request('/userInfo/lockUser',{
    		method:'POST',
    		data:{uid:this.data.uid},
        	onload:function(json){
        		that.__lockWin._$hide();
        		if(json.code==200){
        			notify.show("该账户已冻结");
        			window.location.reload();
        		} else{
        		    notify.show(json.message);
        		}
        	}._$bind(this),
        	onerror:function(json){
        		that.__lockWin._$hide();
        		notify.show(json.message);
        	}._$bind(this)
        });
    };
    _pro.__unlockUser = function(){
    	Request('/userInfo/unlockUser',{
    		method:'POST',
    		data:{uid:this.data.uid},
        	onload:function(json){
        		if(json.code==200){
        			notify.show("该账户已解冻");
        			window.location.reload();
        		} else{
        		    notify.show(json.message);
        		}
        	}._$bind(this),
        	onerror:function(json){
        		notify.show(json.message);
        	}
        });
    };
    
    p._$$InfoModule._$allocate();
  });