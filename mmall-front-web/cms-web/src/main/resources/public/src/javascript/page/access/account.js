/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{lib}util/template/jst.js',
    '{pro}widget/module.js',
    '{pro}page/access/account/accountlist.js?v=1.0.0.3',
    '{pro}extend/request.js',
    'util/chain/NodeList'
    ],
    function(_ut,_v,_e,e2,Module,AccountList,Request,$,p) {
        var pro;
        
        p._$$AccountModule = NEJ.C();
        pro = p._$$AccountModule._$extend(Module);
        
        pro.__init = function(_options) {
            _options.tpl = 'jstTemplate';
            window.__SiteList__ = [];
            window.__AdminList__ = [];
            window.__RoleList__ = [];
            this.__initList();
            this.__initAdminList();
            
            this.__supInit(_options);
            this.__getNodes();
            this.__addEvent();
        };
        pro.__initList = function(){
            if(!this.__accountList){
                this.__accountList = new AccountList({})
                    .$inject('.j-list', "after")
            }
        };
        pro.__initAreaList = function(){
            Request('account/getSiteList',{
            	progress:true,
            	timeout:3000,
                onload:function(json){
                    window.__SiteList__ = json.result.list;
                    pro.__renderSiteControl();
                },
                onerror:function(e){
                    console.log(e);
                }
            })
        };
        pro.__initAdminList = function(){
            Request('account/getAdminList',{
            	progress:true,
            	timeout:3000,
                onload:this.__getAdminList._$bind(this),
                onerror:function(e){
                    console.log(e);
                }
            });
        };
        pro.__getAdminList = function(json){
            window.__AdminList__ = json.result.list;
//            console.log(window.__AdminList__);
            this.__getRoleList();
        };
        pro.__renderSiteControl = function(){
        	var _html = "<option value='-1'>全部站点</option>";
        	if(window.__SiteList__.length > 0){
        		for(var i = 0; i < window.__SiteList__.length;i++){
        			var item = window.__SiteList__[i];
        			_html += "<option value='"+ item.siteId +"'>"+ item.siteName +"</option>";
        		}
        		document.getElementById("siteControl").innerHTML = _html;
        	}
        };
		pro.__getRoleList = function(){
			var data = {},that=this;
			var list = window.__AdminList__;
			data['id'] = list[0].id;
			Request('account/getRoleList',{
				data:data,
				progress:true,
            	timeout:3000,
				onload:function(json){
					window.__RoleList__ = json.result.list;
					pro.__renderRoleControl();
					pro.__initAreaList();
				},
				onerror:function(e){
					console.log(e);
				}
			});
		};
        pro.__renderRoleControl = function(){
        	var _html = "<option value='-1'>全部角色</option>";
        	if(window.__RoleList__.length > 0){
        		for(var i = 0; i < window.__RoleList__.length;i++){
        			var item = window.__RoleList__[i];
        			_html += "<option value='"+ item.id +"'>"+ item.name +"</option>"; 
        		}
        		document.getElementById("roleControl").innerHTML = _html;
        	}
        };
        pro.__getNodes = function(){

        }
        pro.__addEvent = function(){
            
        }
        p._$$AccountModule._$allocate();
    });