/**
 * xx平台商务管理——档期列表页
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}extend/request.js',
    '{pro}widget/layer/sure.window/sure.window.js',
    '{pro}components/notify/notify.js'
    ],
    function(_ut,_v,_e,Module,request,SureWin,notify,p) {
        var pro;

        p._$$ReturnModule = NEJ.C();
        pro = p._$$ReturnModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
        };
        
        pro.__getNodes = function(){
            var _list = _e._$getByClassName(document,'j-flag');
            this.__searchtmp = _list[0];
        };
        
        pro.__addEvent = function(){
        	var _list = _e._$getByClassName('action','j-btn');
        	for(var i=0,l=_list.length;i<l;i++){
        		_v._$addEvent(_list[i],'click',this.__onBtnClick._$bind(this,_list[i]));
        	}
        };
        pro.__onBtnClick = function(_node,_event){
            var _url = _e._$dataset(_node,'url');
            var _message = _e._$dataset(_node,'message');
            if(_message){
            	SureWin._$allocate({
            		text:_message,
            		title:'确认',
            		onok:this.__sendRequest._$bind(this,_url)
            	})._$show();
            } else{
            	this.__sendRequest(_url)
            }
            
        }
        pro.__sendRequest = function(_url){
        	request(_url,{
        		norest:true,
        		type:'json',
            	onload:function(_json){
            		if(_json.code==200){
            			notify.show('操作成功')
            		}
            	},
            	onerror:function(){
            		notify.show('操作失败')
            	}
            })
        }
        p._$$ReturnModule._$allocate();
    });