/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{lib}util/form/form.js',
    '{lib}util/file/select.js',
    '{pro}lib/jquery/dist/jquery.min.js',
    '{pro}lib/jquery/dist/lightbox.min.js'
    ],
    function(ut,v,e,Module,ut1,s,p) {
        var pro;

        p._$$CreateModule = NEJ.C();
        pro = p._$$CreateModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
        };
        
        pro.__getNodes = function(){
        	
        };
        
        pro.__addEvent = function(){
        	v._$addEvent('reset','click',this.__onResetPassword._$bind(this));
        };
        pro.__onResetPassword = function(){
        	e._$('password').innerText ='345'
        };
        p._$$CreateModule._$allocate();
    });