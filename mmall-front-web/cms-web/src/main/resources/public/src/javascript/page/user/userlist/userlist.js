NEJ.define([
        	'{lib}base/util.js',
        	'{lib}base/event.js',
        	'{lib}base/element.js',
            'text!./userlist.html?v=1.0.0.2',
            '{pro}components/MmallListComponent.js',
            '{pro}components/notify/notify.js',
            '{pro}widget/layer/sure.window/sure.window.js'
],function(ut,v,e,_html,ListComponent,notify,Suerwindow){

    var userList = ListComponent.extend({
        url:'/userInfo/queryUser',
        template:_html,
        data :{
        	platMap : {
        		"0" : "运营",
        		"1" : "商家",
        		"2" : "主站",
        		"3" : "手机",
        		"4" :  "wap",
        		"5" :  "ERP",
        		"6" : "地推"
        	}
        } ,
	    refresh:function(_data){
	    	if (!!_data.url){
	            this.url = _data.url;
	            delete _data.url;
	        }
	        this.data.condition = _data;
	        this.$emit('updatelist');
	    },
	    lock:function(item){
	    	this.__lockWin = Suerwindow._$allocate({
                title:'请确认是否冻结该账户',
                text:'用户ID：'+item.uid+'，用户名：'+item.account,
                onok:this.doLock._$bind(this,item)
            });
            this.__lockWin._$show();
	    },
	    doLock: function(item){
	    	this.$request('/userInfo/lockUser',{
	    		method:'POST',
	    		data:{uid:item.uid},
            	onload:function(json){
            		this.__lockWin._$hide();
            		if(json.code==200){
            			notify.show("该账户已冻结");
            			this.$emit('updatelist');
            		} else{
            		    notify.show(json.message);
            		}
            	}._$bind(this),
            	onerror:function(json){
            		this.__lockWin._$hide();
            		notify.show(json.message);
            	}._$bind(this)
            });
	    },
	    unlock: function(item){
	    	this.$request('/userInfo/unlockUser',{
	    		method:'POST',
	    		data:{uid:item.uid},
            	onload:function(json){
            		if(json.code==200){
            			notify.show("该账户已解冻");
            			this.$emit('updatelist');
            		} else{
            		    notify.show(json.message);
            		}
            	}._$bind(this),
            	onerror:function(json){
            		notify.show(json.message);
            	}
            });
	    }
    });

    return userList;
});