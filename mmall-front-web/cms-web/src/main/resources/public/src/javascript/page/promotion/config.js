/**
 * xx平台活动编辑——商品添加页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
    'base/event',
    '{lib}util/chain/NodeList.js',
    '{pro}widget/layer/sure.window/sure.window.js',
    '{pro}extend/request.js',
    '{pro}widget/module.js',
    '{pro}components/promotion/codePreview.js?v=1.0.0.0',
	 '{pro}extend/util.js',
	 '{pro}components/notify/notify.js',
    ],
    function(v,$,SureWindow,Request,Module,PreviewWin,_,Notify, e, c) {
        var pro, 
          $$CustomModule = NEJ.C(),
          pro = $$CustomModule._$extend(Module);
        
        pro.__init = function(_options) {
            window.urlMap = {
                	'PREVIEW' : '/promotion/couponConfig/preview',
            		'SUBMIT' : '/promotion/couponConfig/saveConfig',
            		'GET_CONFIG' : '/promotion/couponConfig/getConfigByType'
                };
        	this.__supInit(_options);
            this.__addEvent();
            this.__initAreaList();
        };
        
        pro.__addEvent = function(){
        	var _self = this;
			$(".couponBtn")._$on("click",function(){
				_self.onChangePanel();
			});
			$("#code")._$on("input",function(){
				_self.onInputCode();
			});
			$("#isRelativeTime")._$on("change",function(){
				_self.onInputCode();
			});
			$("#submit")._$on("click",function(){
				_self.onSubmit();
			}); 
			$(".preview")._$on("click",function(){
				_self.preview();
			});
			$("#siteControl")._$on("change",function(){
				_self.onChangeSite();
			});
        };
        pro.__initAreaList = function(){
            var _self = this;
        	Request('/access/account/getSiteList',{
            	progress:true,
            	timeout:3000,
                onload:function(json){
                    window.__SiteList__ = json.result.list;
                    pro.__renderSiteControl();
                    _self.__getConfig(__SiteList__[0].siteId);
                },
                onerror:function(e){
                    console.log(e);
                }
            });
        };
        pro.__renderSiteControl = function(){
        	var _html = "";
        	if(window.__SiteList__.length > 0){
        		for(var i = 0; i < window.__SiteList__.length;i++){
        			var item = window.__SiteList__[i];
        			_html += "<option value='"+ item.siteId +"'>"+ item.siteName +"</option>";
        		}
        		document.getElementById("siteControl").innerHTML = _html;
        	}
        };
        pro.__getConfig = function(sid){
        	var _self = this;
        	Request(urlMap['GET_CONFIG'],{
        		data : {"siteId":sid,"couponConfigType":1},
        		progress : true,
        		timeout : 3000,
        		onload : function(json){
        			window.__couponConfig__ = json.result;
        			_self.setPanel();
        		},
        		onerror : function(json){
        			alert(json.message);
        		}
        	});
        };
        pro.setPanel = function(){
			if(__couponConfig__.isValid == 1){
				$(".couponBtn")._$attr("class","couponBtn active");
				$(".configBody")._$style("display","block");
			}else{
				//关闭状态清空优惠券信息
				$(".couponBtn")._$attr("class","couponBtn");
				$(".configBody")._$style("display","none");
				__couponConfig__.couponCodes = "";
				__couponConfig__.isRelativeTime = 0;
			}
        	$("#code")._$val(__couponConfig__.couponCodes);
        	var isRelativeTimeMap = {"0":false,"1" : true};
			document.getElementById("isRelativeTime").checked = isRelativeTimeMap[__couponConfig__.isRelativeTime];
			$(".error")._$attr("class","error hide");
        	$("#submit")._$attr("class","btn center-block btn-disabled")._$text("保存");
	        $(".preview")._$attr("class","btn btn-disabled preview");
        };
        pro.onChangePanel = function(){
        	var _self = this;
        	var className = $(".couponBtn")._$attr("class");
			if(className.indexOf("active") > -1){
				//关闭配置
				__couponConfig__.isValid = 0;
				Request(urlMap['SUBMIT'], {
		  	          data: __couponConfig__,
		  	          method: 'POST',
		  	          progress : true,
		  	          onload: function (json) {
		  	        	Notify.notify("优惠券配置关闭成功");
		  	        	_self.setPanel();
		  	          }._$bind(this),
		  	          onerror: function (json) {
		  	        	if(json.code == 414){
			    	        	var list = [];
			  					for(var i in json.result){
			  						list.push(json.result[i]);
			  					}
			  					json["list"] = list;
			  					$(".error")._$html(list.join("<br/>")); 
		  	        	}else if(json.code == 417){
		  	        		$(".error")._$text(json.message);
		  	        	}
		  	          }._$bind(this)
		  	        });
			}else{
				//从关闭状态打开配置
				__couponConfig__.isValid = 1;
				this.setPanel();
			} 
        };
        pro.onInputCode = function(){
        	if($("#code")._$val() == ""){
        		$("#submit")._$attr("class","btn center-block btn-disabled")._$text("保存");
            	$(".preview")._$attr("class","btn btn-disabled preview");
        		return;
        	}
        	$(".error")._$attr("class","error show")._$text("设置内容有变动，请保存");
        	$("#submit")._$attr("class","btn center-block btn-primary")._$text("保存");
        	$(".preview")._$attr("class","btn btn-primary preview");
        };
        pro.onChangeSite = function(){
        	this.__getConfig($("#siteControl")._$val());
        };
        pro.checkValid = function(){
        	var couponCodeReg = /^[0-9a-zA-Z]{1,20}(,[0-9a-zA-Z]{1,20})*$/;
        	var code = $("#code")._$val();
        	var list = code.split(",");
        	if(list.length > 10){
        		$(".error")._$text("最多输入10张优惠券");
        		return false;
        	}else if(couponCodeReg.test(code)){
        		return true;
        	}else{
        		$(".error")._$text("优惠券代码为1-20位字母,数字或二者组合");
        		return false;
        	}
        	
        };
        pro.onSubmit = function(event){
        	v._$stop(event);
        	if($("#submit")._$attr("class").indexOf("disabled") > -1) return;
	    	if(!this.checkValid()) return;
        	__couponConfig__.siteId = $("#siteControl")._$val();
        	__couponConfig__.couponCodes = $("#code")._$val();
        	__couponConfig__.couponConfigType =  1;
        	__couponConfig__.isRelativeTime = document.getElementById("isRelativeTime").checked ? 1 : 0;
        	__couponConfig__.isValid = 1;
	    	SureWindow._$allocate({
	    	  title:"提示",
	    	  text : '该操作将覆盖当前设置并立即生效，是否继续？',
	    	  okText : '继续保存',
	    	  onok:function(){
	    	  	
					Request(urlMap['SUBMIT'], {
	    	          data: __couponConfig__,
	    	          method: 'POST',
	    	          onload: function (json) {
	    	        	  Notify.notify(json && json.message);
	    	        	  $(".error")._$attr("class","error hide");
	    	        	  $("#submit")._$attr("class","btn center-block btn-disabled")._$text("已保存");
//	    	        	  $(".preview")._$attr("class","btn btn-disabled preview");
	    	          }._$bind(this),
	    	          onerror: function (json) {
	    	        	if(json.code == 414){
		    	        	var list = [];
		  					for(var i in json.result){
		  						list.push(json.result[i]);
		  					}
		  					json["list"] = list;
		  					$(".error")._$html(list.join("<br/>")); 
	    	        	}else if(json.code == 417){
	    	        		$(".error")._$text(json.message);
	    	        	}
	    	          }._$bind(this)
	    	        });
	    	  }._$bind(this),
	    	  
	  	  })._$show();
        };
        pro.preview = function(){
        	if($(".preview")._$attr("class").indexOf("disabled") > -1) return;
        	if(!this.checkValid()) return;
        	var code = $("#code")._$val();
    		var type = 1;
    		var _self = this;
			Request("/promotion/couponConfig/preview",{
				data:{"couponCodes":code,"couponConfigType":type},
				method: "get",
				progress : true,
				onload : function(result){
					for(var i = 0; i < result.result.length;i++){
						var coupon = result.result[i];
						var items = JSON.parse(coupon.items);
						coupon["items"] = [];
						coupon["items"][0] = items[0].result[0].value;
						coupon["items"][1] = items[0].condition.value.replace("-","");
						if(document.getElementById("isRelativeTime").checked){
							coupon["validTime"] = Math.floor((parseInt(coupon.endTime) - parseInt(coupon.startTime))/1000/3600/24) + "天";
						}else{
							coupon["validTime"] = _.formatDate(new Date(coupon.startTime),'yyyy.MM.dd') + " - " + _.formatDate(new Date(coupon.endTime),'yyyy.MM.dd');
						}
					}
					_self.showPreviewWin(result);
				},
				onerror : function(result){
					var list = [];
					for(var i in result.result){
						list.push(result.result[i]);
					}
					result["list"] = list;
					$(".error")._$html(list.join("<br/>"))._$attr("class","error show"); 
					
				}
			});        	
        };
        pro.showPreviewWin = function(data){
    		if(!!this.__PreviewWin){
    			this.__PreviewWin._$recycle();
    		}
    		
    		this.__PreviewWin = PreviewWin._$allocate({
			    title : '优惠券预览',
    			parent:document.body,
    		  	item:data
    		})._$show();         	
        };
        $$CustomModule._$allocate({
          data: {}
        });

        return $$CustomModule;
    });