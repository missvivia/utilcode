/**
 * xx平台活动编辑——商品添加页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
    '{pro}widget/module.js', 
    '{pro}widget/district/siteSelector.js',
    '{lib}util/chain/NodeList.js',
	'{pro}extend/request.js',
	'{pro}components/notify/notify.js',
    ],
    function(Module,SiteSelector,$,Request,notify, e, c) {
        var pro, 
          $$CustomModule = NEJ.C(),
          pro = $$CustomModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__addEvent();
        };
        pro.__addEvent = function(){
        	var name = $('.j-submit')._$attr("name");
        	var siteId = name == "edit" ? $("#siteId")._$val() : 0;
        	pro.site = SiteSelector._$allocate({
        		parentNode:$("#area"),
        		url : '/site/areaList?siteId=' + siteId
        	});
        	
        	$(".j-submit")._$on("click",pro.submit);
        };
        pro.submit = function(){
    		var param = {};
    		param["siteName"] = $("#name")._$val();
    		param["areaList"] = pro.site.__getAreaList();
    		
    		var name = $('.j-submit')._$attr("name");
    		if(name == "edit"){
    			var url = "/site/update";
    			param["siteId"] = $("#siteId")._$val();
    		}else if(name="create"){
    			var url = "/site/add";
    		}
    		Request(url,{
                data:param,
                method:'POST',
                type:'JSON',
                onload: function(json){
                    notify.show(json.message);
                    location.href="/site/site";
                },
                onerror: function(json){
                	notify.show(json.message);
                }
            });
    	};

        $$CustomModule._$allocate({
          data: {}
        });

        return $$CustomModule;
    });