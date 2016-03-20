/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/authority/accountList.js?v=1.0.0.0',
    '{pro}extend/request.js'],
    function(_ut,_v,_e,Module,AccountList,Request,p) {
        var pro;

        p._$$AccountModule = NEJ.C();
        pro = p._$$AccountModule._$extend(Module);
        
        pro.__init = function(_options) {
            _options.tpl = 'jstTemplate';
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
            this.__getGroupList();
        };
        pro.__getGroupList = function(){
            Request('/authority/user/getGroupList',{
                onload:this.__cbGetList._$bind(this),
                onerror:function(e){
                    console.log(e);
                }
            })
        }
        pro.__cbGetList = function(json){
            window.__userGroupList__ = json.result.list;
        }
        pro.__getNodes = function(){
        }
        pro.__addEvent = function(){

        }

        p._$$AccountModule._$allocate();
    });