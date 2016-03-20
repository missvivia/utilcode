/**
 * 首页商品推荐
 * author liuqing
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}/extend/util.js',
    '{pro}widget/module.js',
    '{lib}util/chain/NodeList.js',
    '{pro}extend/request.js',
    '{pro}components/notify/notify.js'
    ],
    function(ut,v,e,_,Module,$,Request,notify,p) {
        var pro;

        p._$$recommendationEditModule = NEJ.C();
        pro = p._$$recommendationEditModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            
            this.__addEvent();
        }
        
        pro.__addEvent = function(){
            var that = this;
            
            $(".proInput")._$forEach(function(_node, _index){
            	v._$addEvent(_node,'blur',that.__onBlur._$bind(that));
            });
            
            v._$addEvent('onSubmit','click',that.__onSubmit._$bind(that));
            v._$addEvent('onCancel','click',function(){
            	window.location.href = "/item/product/recommendation";
            }._$bind(that));
        }
        
        pro.__onBlur = function(event){
        	var proSkuId = event.target.value,
        	    reg =  /^[0-9]*$/ ,
        	    errtip = $(event.target)._$parent()._$siblings(".err-tip");
        	if(proSkuId == ""){
        		e._$remove(errtip[0],true);
        	}else{
        		if(!reg.test(proSkuId)){
                	if(errtip.length > 0){
                		errtip[0].innerHTML = "请输入数字";
                	}else{
                		var errDiv =  e._$create("div","err-tip");
                		errDiv.innerHTML = "请输入数字";
                		$(event.target)._$parent()._$insert(errDiv, 'after');
                	}
        			return;
        		}
        		Request("/item/product/checkStatus?productId="+proSkuId,{
                    method:"GET",
                    sync:true,
                    onload: function(data){
                    	if(data.code == 200){
                    		e._$remove(errtip[0],true);
                    	}
                    },
                    onerror: function(data){
                    	if(errtip.length > 0){
                    		errtip[0].innerHTML = data.message;
                    	}else{
                    		var errDiv =  e._$create("div","err-tip");
                    		errDiv.innerHTML = data.message;
                    		$(event.target)._$parent()._$insert(errDiv, 'after');
                    	}
                    	
                    }
                });
        	}
        }
        
        pro.__onSubmit = function(){
        	var count = 0,
        	    skuRecoDTOs = [],
        	    errCount = 0;
        	
        	$(".proInput")._$forEach(function(_node, _index){
            	var value = _node.value,
            	    productId = e._$dataset(_node,'id'),
            	    productSKUId = e._$dataset(_node,'SKUId'),
            	    index = e._$dataset(_node,'index'),
            	    tip = $(_node)._$parent()._$siblings(".err-tip");
            	
            	if(tip.length > 0 && tip[0].innerHTML != ""){
            		errCount++;
            	}
            	if(value != ""){
            		var skuRecoDTO = {};
            		count++;
            		if(!!productId){
            			skuRecoDTO["id"] = productId;
            			skuRecoDTO["productSKUId"] = value;
            			skuRecoDTO["showIndex"] = index;
            		}else{
            			skuRecoDTO["productSKUId"] = value;
            			skuRecoDTO["showIndex"] = index;
            		}
            	}else{
            		if(!!productId){
            			var skuRecoDTO = {};
            			skuRecoDTO["id"] = productId;
            			skuRecoDTO["productSKUId"] = 0;
            			skuRecoDTO["showIndex"] = index;
            		}
            	}
            	
            	if(skuRecoDTO){
            		skuRecoDTOs.push(skuRecoDTO);
            	}
            	
            });
        	
        	if(count == 0){
        		notify.show("请至少配置1个推荐商品");
        		return;
        	}
        	
        	if(errCount > 0){
        		notify.show("请填写正确的商品货号");
        		return;
        	}
        	
        	Request("/item/product/addOrUpdateRecommendation",{
                method:"POST",
                data:{skuRecommendationDTOs:skuRecoDTOs},
                onload: function(data){
                	if(data.code == 200){
                		notify.show("配置成功");
                		window.location.href = "/item/product/recommendation";
                	}
                },
                onerror: function(data){
            		notify.show(data.message);
                }
            });
        	
        }
        
        p._$$recommendationEditModule._$allocate();
    });