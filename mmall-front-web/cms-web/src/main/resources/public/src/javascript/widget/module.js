/**
 * 基础模块
 * author yuqijun(yuqijun@corp.netease.com)
 */
define(['{lib}util/event.js'
       , '{lib}util/template/tpl.js'
       , '{lib}util/query/query.js'
       , 'base/element'
       , '{pro}widget/BaseComponent.js'
       , '{pro}extend/util.js'
       , '{pro}widget/util/side.nav.js'
       ],
    function(ut,e, e2,e3, BaseComponent,util,Nav) {
        var _ = NEJ.P,
            pro, pn;

        var $$Module = NEJ.C();
        pro = $$Module._$extend(ut._$$EventTarget);

        pro.__init = function(_options) {
            this.__supInit(_options);
            if ( !! _options.tpl)
                e._$parseTemplate(_options.tpl);
            e._$parseTemplate('wgt-tpl');
            this.__initShowUserName();
            this.__initComponent(_options);
            Nav._$allocate();
        };

        // 启动所有页面上的Regularjs Component
        pro.__initComponent = function(_options){
            // @TODO remove
            if(_options.data) 
                this.data = _options.data;
            if(!_options.noboot){
                this.__hub = BaseComponent.boot(_options);
            }
        }

        //显示用户名
        pro.__initShowUserName = function(){
            var _ct = e3._$get('username-ct');
            if(util._$isLogin){
            	_ct.innerHTML = util._$getFullUserName();
            }
        }
        
        return $$Module;
    });