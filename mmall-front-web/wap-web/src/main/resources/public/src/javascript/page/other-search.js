/*
 * ------------------------------------------
 * 页面模块实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/widget/module',
    '{lib}util/chain/NodeList.js?v=1.0.0.0',
    '{pro}extend/request.js',
    'pro/extend/config'
],function(_k,_m,$,Request,config,_p,_o,_f,_r,_pro){
    /**
     * 页面模块基类
     *
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);

    
    _pro.__init = function(){
		$("#key")._$val("");
    	this.bindEvent();
    };
    _pro.renderLocalList = function(){
    	var list = [];
		var _html = "<li class='resultTitle'>历史搜索</li>";
		if(localStorage.searchHistory){
			var searchHistory = JSON.parse(localStorage.searchHistory);
			for(var i = 0; i < searchHistory.list.length;i++){
				var temp = searchHistory.list[i];
				var url = config.DOMAIN_URL + "/search?keyword=" + encodeURIComponent(temp);
				_html += '<li><a href="'+ url +'">'+ temp +'</a></li>';
			}
			document.getElementById("searchList").innerHTML = _html;
			$("#result")._$style("display","block");
		}else{
			$("#result")._$style("display","none");
		}
	};
	_pro.bindEvent = function(){
		var _self = this;
		$('.search')._$on("click",function(){
			$(".searchMask")._$style("display","block");
			$(".searchMode")._$style("display","block");
			_self.renderLocalList();
			$("#wrap")._$style({"height":document.body.clientHeight+"px","overflow":"hidden"});
			$("#key")._$trigger("click");
			window.scroll(0,0);
		});
		$(".search")._$on("blur",function(){
			_self.searchList();
		});
		$("body")._$on("keyup",function(event){
			if(event.keyCode == 13){
				_self.searchList();
			}
		});
		$("#clearHistory")._$on("click",function(){	   
			localStorage.clear();
			document.getElementById("result").innerHTML = "";
			$("#result")._$style("display","block");
			
		});
		$(".cancel")._$on("click",function(){
			$(".searchMode")._$style("display","none");
			$(".searchMask")._$style("display","none");
			$("#wrap")._$style({"height":"auto"});
		});
		$(".searchIcon")._$on("click",function(){
			_self.searchList();
		});
	};
	_pro.searchList = function(){
		var searchValue = $("#key")._$val();
		if(searchValue == ""){
			return;
		}
		location.href = config.DOMAIN_URL + "/search?keyword=" + encodeURIComponent(searchValue);
		if(localStorage.searchHistory){
			var searchHistory = JSON.parse(localStorage.searchHistory);
			for(var i= 0;i < searchHistory.list.length;i++){
				var temp = searchHistory.list[i];
				if(temp == searchValue){
					searchHistory.list.splice(i,1);
					break;
				}
			}		
		}else{
			searchHistory = {};
			searchHistory["list"] = [];
		}
		searchHistory.list.unshift(searchValue);
		localStorage["searchHistory"] = JSON.stringify(searchHistory);
	};
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);

        // TODO
    };

    // init page
    _p._$$Module._$allocate();
    return _p;
});