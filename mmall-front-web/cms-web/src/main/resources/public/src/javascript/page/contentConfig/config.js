/**
 * 首页配置
 * author liuqing
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{lib}util/file/select.js',
    '{lib}util/ajax/xdr.js',
    '{pro}extend/request.js',
    '{pro}widget/layer/sure.window/sure.window.js',
    './configwin/configwin.js?v=1.0.0.0',
    './recoverwin/recoverwin.js?v=1.0.0.0',
    '{lib}util/chain/NodeList.js',
    '{pro}components/notify/notify.js'
    ],
    function(ut,v,e,Module,f,j,Request,SureWin,configwin,recoverwin,$,notify,p) 
    {
        var pro;

        p._$$ContentConfigModule = NEJ.C();
        pro = p._$$ContentConfigModule._$extend(Module);
        
        pro.__init = function(_options) 
        {
            this.__supInit(_options);
            var that = this;
            
            v._$addEvent('importIndexWeb','click',this.__importDataFile._$bind(this,1,'importIndexWeb'));
            v._$addEvent('importIndexWap','click',this.__importDataFile._$bind(this,2,'importIndexWap'));
            v._$addEvent('importIndexIOS','click',this.__importDataFile._$bind(this,3,'importIndexIOS'));
            v._$addEvent('importCateWeb','click',this.__importDataFile._$bind(this,4,'importCateWeb'));
            v._$addEvent('importCateWap','click',this.__importDataFile._$bind(this,5,'importCateWap'));
            v._$addEvent('importCateIOS','click',this.__importDataFile._$bind(this,6,'importCateIOS'));
            
            $(".recover")._$forEach(function(_node, _index){
            	v._$addEvent(_node,'click',that.__recoverFile._$bind(that,e._$dataset(_node,'type'))); 
            })
        };
        
        pro.__importDataFile = function(type,node){
        	var _id = f._$bind(node,{
		         name : "file",
		         accept : "xls,xlsx",
		    	 onchange:this.__onLogoUpload._$bind(this,type) 
		    });
        };
        
        pro.__onLogoUpload = function(type,event){
        	var that = this;
    		if(!!that.__configWin){
    			that.__configWin._$recycle();
    		}
    		that.__configWin = configwin._$allocate({
    		  	parent:document.body,
    		  	item:{type:type},
    		  	onok : function(data){
    		  	}
    		})._$show();
 			var form = event.form;
 			form.action = '/content/uploadIndexData';
 			j._$upload(form,{
 				type:"text",
 				query:{fileType:type},
 				onload:function(result){
 					if(result == 200){
 						that.__configWin.__showContent(true,type,'');
 					}else{
 						that.__configWin.__showContent(false,type,result);
 					}
 				}._$bind(this),
 				onerror:function(result){
 					notify.show(result.message);
 					that.__configWin._$hide();
 				}
 			})
 		};
 		
 		pro.__recoverFile = function(type){
 			var that = this;
 			SureWin._$allocate({
        		parent:document.body,
        		title:'提示',
        		text:'恢复操作将直接替换正在使用的首页，是否确认恢复？',
        		onok:function(){
        			this._$hide();
        			if(!!that.__recoverWin){
            			that.__recoverWin._$recycle();
            		}
            		that.__recoverWin = recoverwin._$allocate({
            		  	parent:document.body,
            		  	item:{type:type},
            		  	onok : function(data){
            		  		that.__recoverFile(data);
            		  	}
            		})._$show();
	        		Request('/content/restore',{
	                    method:'GET',
	                    type:'JSON',
	                    data:{fileType:type},
	                    onload:function(result){
	                    	if(result.code == 200){
	     						that.__recoverWin.__showContent(true,type,'');
	     					}else{
	     						that.__recoverWin.__showContent(false,type,result);
	     					}
	                    }._$bind(this),
	                    onerror:function(result){
	                    	notify.show(result.message);
	     					that.__recoverWin._$hide();
	                    }._$bind(this)
	                });
        	    }
        	})._$show();
 		};
        
        p._$$ContentConfigModule._$allocate();
    });