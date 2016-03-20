/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{lib}util/template/jst.js',
    '{pro}widget/module.js',
    '{pro}page/access/role/rolelist.js',
    '{pro}page/access/role/add.role.win.js',
    '{pro}extend/request.js'
    ],
    function(_ut,_v,_e,e2,Module,RoleList,RoleWin,Request,p) {
        var pro;
        
        p._$$RoleModule = NEJ.C();
        pro = p._$$RoleModule._$extend(Module);
        
        pro.__init = function(_options) {
            _options.tpl = 'jstTemplate';
            this.__initList();
            this.__supInit(_options);
            this.__getNodes();
            this.__addEvent();
        };
        pro.__initList = function(){
            if(!this.__roleList){
                this.__roleList = new RoleList({})
                    .$inject('.j-list', "after")
            }
           
        };
        pro.__getNodes = function(){
            
        }
        pro.__addEvent = function(){
           
        }
        p._$$RoleModule._$allocate();
    });