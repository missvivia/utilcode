/**
 * 限购管理
 * author liuqing
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}extend/request.js',
    '{pro}components/notify/notify.js',
    './limit.list/limitList.js?v=1.0.0.1'
    ],
    function(ut,v,e,Module,Request,notify,list,p) 
    {
        var pro;

        p._$$limitModule = NEJ.C();
        pro = p._$$limitModule._$extend(Module);
        
        pro.__init = function(_options) 
        {
            this.__supInit(_options);
            this.__addEvent();
        };
        
        pro.__addEvent = function(){
        	v._$addEvent('searchBtn','click',this.__searchList._$bind(this));
        };
        
        pro.__searchList = function(){
            e._$get('limitList').innerHTML = '';        	
        	var userName = e._$get('userName').value,
        	    skuId = e._$get('skuId').value,
        	    data = {};
        	
        	if(userName == ""){
        		notify.show('请填写用户名');
        		return;
        	}else if(skuId == ""){
        		notify.show('请填写商品skuId');
        		return;
        	}
        	
        	Request("/item/limit/userExist?userName="+userName,{
                method:'GET',
                onload:function(_json){
                  if(_json.code == 200){
                	  data.userId = _json.result;
                  	  data.skuId = skuId;
                      pro.__limitList = new list({data: {condition:data,userName:userName}}).$inject('#limitList');
                  }
                },
                onerror:function(_json){
               	   notify.show(_json.message);
                }
            });
        };
        
        p._$$limitModule._$allocate();
    });