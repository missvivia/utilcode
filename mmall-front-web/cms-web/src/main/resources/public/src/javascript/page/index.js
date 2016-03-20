/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
        '{lib}base/event.js',
        '{lib}base/element.js',
        '{pro}widget/module.js',
        'pro/extend/util'],
    function(ut,v,e,Module,_,p,o,f,r) {
        var pro;

        p._$$DecorateModule = NEJ.C();
        pro = p._$$DecorateModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
			e._$get('username').innerHTML = _._$getFullUserName();
        };
        
        pro.__getNodes = function(){
            
        };
        
        pro.__addEvent = function(){
           
        };

        p._$$DecorateModule._$allocate();
    });