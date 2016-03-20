/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    './userwin/infowin.js'
    ],
    function(ut,v,e,Module,InfoWin,p) 
    {
        var pro;

        p._$$AccountSuccessModule = NEJ.C();
        pro = p._$$AccountSuccessModule._$extend(Module);
        
        pro.__init = function(_options) 
        {
            this.__supInit(_options);
            
            v._$addEvent('manageUser','click',this.__manageUsers._$bind(this));
        };
        
        pro.__manageUsers = function(_event){
    		if(!!this.__infoWin){
    			this.__infoWin._$recycle();
    		}
    		
    		this.__infoWin = InfoWin._$allocate({
    		  	parent:document.body,
    		  	item:{'businessId': window.businessId}
    		})._$show();
        };
        
        p._$$AccountSuccessModule._$allocate();
    });