/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/schedule/returndetail.js'
    ],
    function(_ut,_v,_e,Module,p) {
        var pro;

        p._$$SizeModule = NEJ.C();
        pro = p._$$SizeModule._$extend(Module);
        
        pro.__init = function(_options) {
            _options.tpl = 'jstTemplate';
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
        };
        
        pro.__getNodes = function(){
            var _list = _e._$getByClassName(document,'j-flag');
            this.__returnList = _list[0];
        };
        
        pro.__addEvent = function(){
            _v._$addEvent(this.__returnList,'click',this.__onReturnBtnClick._$bind(this)); 
        };
        pro. __onReturnBtnClick = function(event){
            
        }

        p._$$SizeModule._$allocate();
    });