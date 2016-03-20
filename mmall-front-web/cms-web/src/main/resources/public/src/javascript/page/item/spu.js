/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js?v=1.0.0.0',
    '{lib}base/event.js?v=1.0.0.0',
    '{lib}base/element.js?v=1.0.0.0',
    './spu.list/list.js?v=1.0.0.3',
    '{pro}widget/module.js?v=1.0.0.0',
    '{pro}/widget/util/search.select.js?v=1.0.0.0',
    './normal.list/list.js?v=1.0.0.2',
    '{pro}/extend/request.js?v=1.0.0.0',
    '{lib}util/chain/NodeList.js'
    ],
    function(ut,v,e,List,Module,searchForm,categoryList,Request,$,p) 
    {
        var pro;

        p._$$AccountModule = NEJ.C();
        pro = p._$$AccountModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            
            v._$addEvent('searchValue','keyup',function(_event){
            	if(_event.keyCode == 13){
            		e._$get('searchBtn').click();
            	}
            });
            var _form = searchForm._$allocate(
            {
		        form:'search-form',
		        onsearch:function(_data)
		        {
		        	if(!this.__list)
		        	{
		        		this.__list = new List({data:{condition:_data}});
		        		this.__list.$inject('#spulist');
		        	} 
		        	else
		        	{
		        		this.__list.refresh(_data);
		        	}
		        }._$bind(this)
		    });
            
        	var brandList;
        	Request("/item/brand/list?index=0&limit=0&offset=0",{
        		method : "get",
        		onload : function(json){
        			if(json.code == 200){
        	        	brandList = json.result.list;
        				window.brandList = brandList;
        	        	brandList.sort(pro.by("brandNameEn"));
        	        	var brandSelect = document.getElementById("brands");
        				var brandOptions = "";
        				for(var i = 0; i < brandList.length; i++){
        					brandOptions += "<option name='" + brandList[i].brandId 
        																+ "' value='" + brandList[i].brandId + "'>" 
        																+ brandList[i].brandNameZh + "</option>";
        				}
        				brandSelect.innerHTML += brandOptions;
        			}else{
        				alert('品牌列表获取失败，请稍后创建单品，或联系管理员')
        	        }
        		},
        		onerror : function(json){
        			alert('品牌列表获取失败，请稍后创建单品，或联系管理员')
        		}
        	});        	
        	//get category normal list
        	this.__clist =  new categoryList().$inject('#categories');
        	
        	$("body")._$on("click",function(event){
//        		v._$stop(event);
        		var className = event.target.className;
        		if(className.indexOf("listTable") > -1) return;
        		if(className.indexOf("itemCategory") > -1) return;
        		if(className.indexOf("j-update") > -1) return;
        		if(className.indexOf("j-content") > -1) return;
        		$(".listTable")._$style("display","none");
        	});
        };
        pro.by = function(name){
    		return function(o,p){
    			var a,b;
    			if(typeof o == "object" &&  typeof p =="object" && o && p){
    				a = o[name];
    				b = p[name];
    				if(a == b){
    					return 0;
    				}
    				if(typeof a == typeof b){
    					 return a < b ? -1 : 1;
    				}
    				return typeof a < typeof b ? -1 : 1;
    			}else{
    				throw("error");
    			}
    		}
    	};
        
        p._$$AccountModule._$allocate();
    });