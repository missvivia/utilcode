/**
 * 用户列表
 * author chenlei(chenlei@xyl.com)
 *
 */

NEJ.define([
	'{lib}base/util.js',
	'{lib}base/event.js',
	'{lib}base/element.js',
	'{pro}widget/module.js',
	'{lib}util/form/form.js',
    '{lib}util/data/region/zh.js',
    '{lib}util/region/zh.js',
    '{pro}/widget/district/address.js',
	'{pro}extend/request.js',
	'{pro}components/notify/notify.js',
	'{lib}util/chain/NodeList.js',
	'./categorywin/categorywin.js'
  ],
  function(ut,v,e,Module,f,t0,t1,Address,Request,notify,$,CateWin,p) {
    var _pro,deleteDistrictIdList = [];

    p._$$CreateModule = NEJ.C();
    pro = p._$$CreateModule._$extend(Module);

    pro.__init = function(_options) {
      this.__super(_options);
      
      this.__initNodes();
//      this.__initDistrict();
      this.webform = f._$$WebForm._$allocate({
			form:this.form[0],
			oncheck:function(_event){

			},
		});
      this.__addEvents();
      this.__initCategoryList();
      
      window.categrodList=[];
      
      window.selectedList=[];
      this.__initSelectedList();
    };
    pro.__initNodes = function(){
    	 this.form =$('#createCategory');
         this.districtForm =$('#districtForm');
         this.relevanceForm=$('#relevanceForm');
         this.districtBlock=$("#districtBlock");
    }
    pro.__addEvents = function(){
    	var $category=$("#category"),$submitBtn=$("#submitBtn"),$updateBtn=$("#updateBtn"),$districtAdd=$("#districtAdd"),$addRelevance=$("#addRelevance"),that=this;
    	$category._$on("change",function(){
    		var level=$(this)._$children("option:selected")._$attr("level");
    		switch (level){
    		case "0":{
//    			that.districtBlock._$style("display","block");
//    			that.districtBlock._$next()._$style("display","block");
    			that.relevanceForm._$style("display","none");
    			that.webform._$showMsgPass($category[0],"ok");
    			break;
    		}
    		case "1":{
    			that.districtBlock._$style("display","none");
    			that.districtBlock._$next()._$style("display","none");
    			that.relevanceForm._$style("display","none");
    			that.webform._$showMsgPass($category[0],"ok");
    			break;
    		}
    		case "2":{
    			that.districtBlock._$style("display","none");
    			that.districtBlock._$next()._$style("display","none");
    			that.webform._$showMsgPass($category[0],"ok");
    			that.relevanceForm._$style("display","block");
    			break;
    		}
    		case "3":{
    			that.districtBlock._$style("display","none");
    			that.districtBlock._$next()._$style("display","none");
    			that.relevanceForm._$style("display","none");
    			that.webform._$showMsgError($category[0],"第三级分类不能作为上级分类");
    			break;
    		}
    		}
    		
    	});
    	$districtAdd._$on("click",function(){
    		that.__addDistrict(this);
    	});
    	$submitBtn._$on("click",function(){
    		that.__submitBtn("add");
    	});
    	$updateBtn._$on("click",function(){
    		that.__submitBtn("update");
    	});
    	$addRelevance._$on("click",function(){
    		if(!!this.__categoryWin){
    			this.__categoryWin._$recycle();
    		}
    		this.__categoryWin = CateWin._$allocate({
    		  	parent:document.body
    		})._$show();
    	});
    	$("#goToDetail")._$on("click",function(){
    		location.href="/category/content/editContentTree?rootId=" + window.__rootId;
    	});
    }
    pro.__submitBtn = function(type){
    	var $category=$("#category"),level=$category._$children("option:selected")._$attr("level");
    	
    	if(this.webform._$checkValidity()){
    		var data = this.webform._$data();
			data=this.__formatTreeDate(data);
			data["level"] = parseInt(level) + 1;
			data["rootId"] = window.__rootId;
    		Request('/category/content/'+type,{
    			data:data,
        		method:'POST',
        		progress:true,
                onload:function(json){
                	if(json.code=="200"){
                		notify.show({
    						'type':'success',
    						'message':'添加成功'
    					});
                		window.location.href = "/category/content/editContentTree?rootId=" + window.__rootId;
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
            });
    		
    	}
    }
    pro.__formatTreeDate=function(data){
    	var tmp=[];
    	for(var i=0;i<selectedList.length;i++){
    		tmp.push(selectedList[i].id);
    	}
    	data["categoryNormalIds"]=tmp.join();
    	return data;
    }
    pro.__initSelectedList=function(){
    	selectedList=[];
    	var _li=e._$getChildren(e._$get("categrod-wrap"));
    	ut._$forIn(_li,function(_item,_index,_this){
    		var id=e._$dataset(_li[_index],'id');
    		var name=e._$dataset(_li[_index],'name');
    		var obj=new Object();
    		obj.id=id;
    		obj.name=name;
    		selectedList.push(obj);
    	});
    	var closes=e._$getByClassName(e._$get("categrod-wrap"),'close');
    	$(closes)._$off("click");
    	$(closes)._$on("click",function(){
    		var $parent=$(this)._$parent(),
    			id=e._$dataset($parent[0],'id');
    		
    		for(var i=0;i<selectedList.length;i++){
    			if(selectedList[i].id==id){
    				selectedList.splice(i,1);
    			}
    		}
    		e._$remove($parent[0],false);
    	});
    }
    pro.__initDistrict = function() {
    	var list = e._$getByClassName(this.districtForm[0],'j-address');
        for (var i=0,l=list.length; i<l; i++) {
            
			var provinceId = e._$dataset(list[i],'provinceid');
			var cityId = e._$dataset(list[i],'cityid');
			var districtId = e._$dataset(list[i],'districtid');
			
            Address._$$AddressSelector._$allocate({
            	parentNode:list[i],
                data:{provinceId:provinceId,cityId:cityId,districtId:districtId},
                remove:function(node){this.__removeDistrict(node)}._$bind(this)
            });
        }
    };
    pro.__addDistrict = function(obj){
    	var div = e._$create('div','form-group'),that=this;
        div.innerHTML = '<div class="j-address col-sm-12"></div>';
        this.districtForm[0].appendChild(div);

        var vlist = e._$getByClassName(div,'j-address');
        Address._$$AddressSelector._$allocate({
            parentNode:vlist[0],
            remove:function(node){this.__removeDistrict(node)}._$bind(this)
        });
    };
    pro.__removeDistrict = function(node){
		// 最后一个不允许删除
		var list = e._$getByClassName(this.districtForm[0],'j-address');
		if(list.length ==1){
			notify.show('请至少填写一个区域');
			return;
		}			
		e._$remove(node.parentNode);
	};
	
	pro.__getDistrict = function(data){
        var list = e._$getByClassName(this.districtForm[0],'j-address');
        var districtNames = [];
        var districtIds = [];
        for (var i=0,l=list.length; i<l; i++) {
			var vlist = e._$getByClassName(list[i],'form-control');
            
            var id = e._$dataset(list[i],'id');
            
            var index = vlist[0].selectedIndex;
            var option = vlist[0].options[index];
            var provinceId = option.value;
            var provinceTitle = option.text;
            
            var cityId = '00';
            var cityTitle="";
			if(vlist.length>1){
				var index1 = vlist[1].selectedIndex;
            	var option1 = vlist[1].options[index1];
            	cityId = option1.value;
            	
            	// 直辖市不用加市名
            	if(option1.text != provinceTitle){
            		cityTitle =option1.text;	
            	}
			}
            
            var districtId = '00';
            var districtTitle = '';
            if(vlist.length>2){
            	var index2 = vlist[2].selectedIndex;
            	var option2 = vlist[2].options[index2];
            	districtId = option2.value;	
            	districtTitle =option2.text;
            }
            
            var district = new Object();
            district.provinceId = provinceId;
            district.cityId = cityId;
            district.districtId = districtId;   
            var id=districtId!="00" ? districtId : (cityId !="00" && parseInt(cityId)>0) ? cityId+"00": provinceId !="00" ? provinceId+"0000" :"000000";
            
            var name = provinceTitle+" "+cityTitle+" "+districtTitle;
            districtNames.push(name);
            districtIds.push(id);
        }
        data["districtIds"]=districtIds.join();
        data["districtNames"]=districtNames.join();
        
        return data;
	};
    pro.__initCategoryList=function(){
    	var that=this;
		var rootId = window.__rootId;
    	Request('/category/content/listCategoryContentByRootId',{
    		method:'GET',
    		data : {"rootId" : rootId},
			progress:true,
    		sync:false,
            onload:function(json){
            	if(json.code=="200"){
            		that.__createCategoryList(json.result);
            		var $category=$("#category");
            		$category._$val(e._$dataset($category[0],"value"));
            		$category._$trigger("change");
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

    pro.__createCategoryList=function(data){
    	var that=this;
    	ut._$forEach(data,function(_item,_index,_this){
    	    that.__renderOption(_item.id,_item.name,_item.level);
    	    if(_item.subCategoryContentDTOs){
    	    	that.__createCategoryList(_item.subCategoryContentDTOs);
    	    }
    	});
    }
    
    pro.__renderOption=function(id,name,level){
    	var $category=$("#category");
    	var name;
    	if(level==2){
    		name="&nbsp;&nbsp;&nbsp;&nbsp;"+name;
    	}
    	if(level==3){
    		name="&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+name;
    	}
    	
    	var _coption=$('<option level='+level+' value='+id+'>'+name+'</option>');
    	$category._$insert(_coption,'append');
    }

    p._$$CreateModule._$allocate();
  });