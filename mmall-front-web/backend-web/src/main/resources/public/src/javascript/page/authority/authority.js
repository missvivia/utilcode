/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/authority/userGroupList.js?v=1.0.0.0',
    '{pro}extend/request.js'
    ],
    function(_ut,_v,_e,Module,UserGroupList,Request,p) {
        var pro;
        
        p._$$AuthorityModule = NEJ.C();
        pro = p._$$AuthorityModule._$extend(Module);
        
        pro.__init = function(_options) {
            _options.tpl = 'jstTemplate';
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
            this.__getGroupList();
        };
        pro.__getGroupList = function(){
            Request('/authority/userGroup/getAccessList',{
                // data:,
                onload:this.__cbGetData._$bind(this),
                onerror:this.__onGetError._$bind(this)
            })
        }
        pro.__cbGetData = function(json){
            window.__accessList__ = json.result;
        }
        pro.__onGetError = function(e){
            console.log(e);
        }
        pro.__getNodes = function(){
        }
        pro.__addEvent = function(){
        }
        p._$$AuthorityModule._$allocate();
    });