/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define([
    '{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}extend/request.js',
    '{lib}util/chain/NodeList.js',
    '{lib}util/template/jst.js',
	'{lib}util/form/form.js',
	'{pro}components/notify/notify.js',
    './attrWin.js',
    './standardWin.js'
    ],
    function(ut,v,_e,Module,Request,$,_p, _t, notify,AttrWin,StandardWin,p) {
        var pro;
        
        String.prototype.trim=function(){
			return this.replace(/(^\s*)|(\s*$)/g, ""); 
		}
		String.prototype.encodeTag = function(){
    		return this.replace(/</g, "&lt;").replace(/>/g, "&gt;");
    	};
        
        p._$$addModule = NEJ.C();
        pro = p._$$addModule._$extend(Module);
                
        pro.__init = function(_options) {
        	_options.tpl = 'jstTemplate';
        	window.modelId="";
        	window.__attrlist=[];
        	window.__standardlist=[];
        	this.__initCategoryList();
        	this.__addEvent();
            this.__supInit(_options);
        	
        }
        pro.__initCategoryList=function(){
        	var that=this;
        	Request('/category/normal/list',{
        		method:'GET',
				sync:false,
                onload:function(json){
                	if(json.code=="200"){
                		that.__createCategoryList(json.result.list);
                		that.__initForm();
                	}else{
                		notify.show({
    						'type':'info',
    						'message':json.message
    					});
                	}
                },
                onerror:function(data){
                	notify.show({
						'type':'info',
						'message':data.message
					});
                }
            })
        }
        pro.__initForm = function(){
        	var modelId=$("#modelId")._$val(),that=this;
        	
			window.__Form = _t._$$WebForm._$allocate({
				form:'addModel',
				oncheck:function(_event){
					if (_event.target.name=='categoryNormalId' && (_event.target.value=="0" || _event.target.value=="")){
							_event.value = "必选项";
					}
				},
				oninvalid:function(_event){
				}
			});
			
			if(modelId!=""){
				Request('model/edit',{
					data:{modelId:modelId},
					method:'GET',
					sync:false,
					onload:function(data){
						if(data.code==200){
							that.__initPage(modelId,data.result);
						}
					},
					onerror:function(data){
						notify.show({
							'type':'info',
							'message':data.message
						});
					}
				})
				
			}
		};
		pro.__initPage = function(modelId,data){
			modelId=modelId;
			var modelName=$("#modelName"),category=$("#category");
			this.categoryNormalId=data.categoryNormalId;
			
			modelName._$val(data.modelName);
			category._$val(data.categoryNormalId);
			
			
			__attrlist=data.parameterList||[];
			__standardlist=data.specificationList||[];
			var attrWin=new AttrWin(),standardWin=new StandardWin();
			if(__attrlist.length>0){
				$("#attrbox")._$prev("thead")._$style("visibility","visible");
				ut._$forIn(__attrlist,function(_item,_index,_this){
					attrWin.__createTr(__attrlist[_index]);
				});
			}
			if(__standardlist.length>0){
				$("#standbox")._$prev("thead")._$style("visibility","visible");
				ut._$forIn(__standardlist,function(_item,_index,_this){
					standardWin.__createTr(__standardlist[_index]);
				});
			}
			
		}
        pro.__addEvent = function(){
        	var that=this;
        	$("#addextend")._$on("click",function(_event){
        		_event.preventDefault();
        	    that.__showAttrWin();
        	})
        	$("#addstand")._$on("click",function(_event){
        		_event.preventDefault();
        		that.__showStandWin();
        	})
        	$("#submitBtn")._$on("click",function(_event){
        		that.__onSubmitClick();
        	})
        	$("#updateBtn")._$on("click",function(_event){
        		that.__onUpdateClick();
        	})
        }
        pro.__onUpdateClick=function(){
        	var category=$("#category"),$p1=$(".perror1"),$p2=$(".perror2");
        	if(__Form._$checkValidity()){
        		
        		var level=category._$children("option:selected")._$attr("level");
        		if(level<3){
        			__Form._$showMsgError(category[0],"必须是分类的三级才能创建模型");
        			return false;
        		}
        		var id=category._$val(),isM=true;
        		if(level==3 && this.categoryNormalId != id){
        			Request('model/checkModel',{
    					data:{categoryId:id},
    					method:'GET',
    					type:'JSON',
    					sync:false,
    					onload:function(dt){
    						if(dt.code=="417"){
    							isM=false;
    						}else{
    							isM=true;
    						}
    					},
    					onerror:function(dt){
    						notify.show({
    							'type':'info',
    							'message':dt.message
    						});
    					}
    				})
        		}
        		if(!isM){
					__Form._$showMsgError(category[0],dt.message);
					return false;
				}
        		if(__attrlist.length<1){
        			$p1._$style({"color":"red","display":"block"});
        			return false;
        		}
        		if(__standardlist.length<1){
        			$p2._$style({"color":"red","display":"block"});
        			return false;
        		}
        		$p1._$style({"display":"none"});
        		$p2._$style({"display":"none"});
  		
        		var data=__Form._$data();

        		
        		Request('model/update',{
					data:data,
					method:'POST',
					type:'JSON',
					onload:function(dt){
						if(dt.code=="200"){
							notify.show({
								'type':'success',
								'message':dt.message
							});
							window.location.href="/item/model";
						}else{
							notify.show({
								'type':'info',
								'message':dt.message
							});
						}
					},
					onerror:function(dt){
						notify.show({
							'type':'info',
							'message':dt.message
						});
					}
				})
        	}
        }
        pro.__onSubmitClick=function(){
        	var category=$("#category"),$p1=$(".perror1"),$p2=$(".perror2");
        	if(__Form._$checkValidity()){
        		
        		var level=category._$children("option:selected")._$attr("level");
        		if(level<3){
        			__Form._$showMsgError(category[0],"必须是分类的三级才能创建模型");
        			return false;
        		}
        		var id=category._$val();
        		if(level==3){
        			Request('model/checkModel',{
    					data:{categoryId:id},
    					method:'GET',
    					type:'JSON',
    					onload:function(dt){
    						if(dt.code=="417"){
    							__Form._$showMsgError(category[0],dt.message);
    							return false;
    						}
    					},
    					onerror:function(dt){
    						notify.show({
    							'type':'info',
    							'message':dt.message
    						});
    					}
    				})
        		}
        		
        		if(__attrlist.length<1){
        			$p1._$style({"color":"red","display":"block"});
        			return false;
        		}
        		if(__standardlist.length<1){
        			$p2._$style({"color":"red","display":"block"});
        			return false;
        		}
        		$p1._$style({"color":"red","display":"none"});
        		$p2._$style({"color":"red","display":"none"});
        		var data=__Form._$data();
        		data['parameterList']=__attrlist;
        		data['specificationList']=__standardlist;
        		
        		Request('model/create',{
					data:data,
					method:'POST',
					type:'JSON',
					onload:function(dt){
						if(dt.code=="200"){
							notify.show({
								'type':'success',
								'message':dt.message
							});
							window.location.href="/item/model";
						}else{
							notify.show({
								'type':'info',
								'message':dt.message
							});
						}
					},
					onerror:function(dt){
						notify.show({
							'type':'info',
							'message':dt.message
						});
					}
				})
        	}
        }
        pro.__showAttrWin=function(){
            if(!!this.__attrWin){
              this.__attrWin._$recycle();
            }

            this.__attrWin = AttrWin._$allocate({
            	parent:document.body,
            	modelId:modelId
            })._$show();
          }
        pro.__showStandWin=function(){
            if(!!this.__standWin){
              this.__standWin._$recycle();
            }

            this.__standWin = StandardWin._$allocate({
            	parent:document.body,
            	modelId:modelId
            })._$show();
          }
        pro.__createCategoryList=function(data){
        	var that=this;
        	ut._$forEach(data,function(_item,_index,_this){
        	    that.__renderOption(_item.categoryId,_item.categoryName,_item.categoryDepth);
        	    if(_item.subCategoryList){
        	    	that.__createCategoryList(_item.subCategoryList);
        	    }
        	});
        }
        
        pro.__renderOption=function(id,name,categoryDepth){
        	var $category=$("#category");
        	var name;
        	if(categoryDepth==2){
        		name="&nbsp;&nbsp;&nbsp;&nbsp;"+name;
        	}
        	if(categoryDepth==3){
        		name="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+name;
        	}
        	
        	var _coption=$('<option level='+categoryDepth+' value='+id+'>'+name+'</option>');
        	$category._$insert(_coption,'append');
        	
//        	var _coption =  _p._$add('<option class=level${depth} value=${id}>${name}</option>');
//        	console.log(_p._$get(_coption));
//        	_p._$render('category',_coption,[{depth:categoryDepth,id:id,name:name},{depth:categoryDepth,id:id,name:name}]);
        }
        p._$$addModule._$allocate();
    });