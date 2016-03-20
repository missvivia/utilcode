/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}extend/request.js',
    '{pro}widget/layer/sure.window/sure.window.js'
    ],
    function(ut,v,e,Module,Request,SureWin,p) 
    {
        var pro;

        p._$$UploadSuccessModule = NEJ.C();
        pro = p._$$UploadSuccessModule._$extend(Module);
        
        pro.__init = function(_options){ 
            this.__supInit(_options);
            this.__getNodes();
            
            v._$addEvent(this.issueBtn,'click',this.__issueFile._$bind(this));
            v._$addEvent(this.issueAgainBtn,'click',function(){
            	this.issueBtn.click();
            }._$bind(this));
        };
        
        pro.__getNodes = function(){
        	this.issueBtn = e._$get('issue');
        	this.issueAgainBtn = e._$get('issueAgain');
        	this.uploadsuccessGroup = e._$get('uploadsuccess');
        	this.publishfailGroup = e._$get('publishfail');
        	this.progressGroup = e._$get('progress');
        };
        
        pro.__issueFile = function(){
        	var uploadsuccessGroup = this.uploadsuccessGroup,
        	    publishfailGroup = this.publishfailGroup,
        	    progressGroup = this.progressGroup;
        	
        	SureWin._$allocate({
        		parent:document.body,
        		title:'提示',
        		text:'发布当前首页配置文件将直接替换正在使用的首页，是否确认发布？',
        		onok:function(){
        			e._$style(uploadsuccessGroup,{display:'none'});
        			e._$style(publishfailGroup,{display:'none'});
        		    e._$style(progressGroup,{display:'block'});
	        		Request('/content/publishIndex',{
	                    method:'GET',
	                    type:'JSON',
	                    onload:function(result){
	                    	if(result.code==200){
	                    		location.href="/content/publishSuccess";
	                    	}else{
	                    		e._$style(progressGroup,{display:'none'});
	     						e._$style(publishfailGroup,{display:'block'});
	                    		e._$get('msg').innerHTML = result.message;
	                    	}
	                    }._$bind(this),
	                    onerror:function(){}._$bind(this)
	                });
        	    }
        	})._$show();
        };
        
        p._$$UploadSuccessModule._$allocate();
    });