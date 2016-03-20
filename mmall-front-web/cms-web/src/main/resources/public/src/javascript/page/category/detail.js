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
	'{pro}extend/request.js',
	'{pro}components/notify/notify.js',
	'{lib}util/chain/NodeList.js',
    './tree/tree.js?v=1.0.0.0',
    '{pro}components/progress/progress.js',
    '{pro}page/category/categorywin/assignDistrictWin.js?v=1.0.0.0',
    '{pro}/widget/district/addressSelector.js?v=1.0.0.0',
    '{pro}page/category/categorywin/editCategoryWin.js?v=1.0.0.0'
  ],
  function(ut,v,e,Module,f,Request,notify,$,Tree,progress,DistrictWin,Address,CategoryWin,p) {
    var _pro;

    p._$$ContentModule = NEJ.C();
    pro = p._$$ContentModule._$extend(Module);

    pro.__init = function(_options) {
      var that=this;
      this.__super(_options);
      this.__initForm();
	  this.__getDetail();
      
      this.__addEvent();
    };
    pro.__addEvent = function(){
    	var that  = this;
    	v._$addEvent('searchValue','keyup',function(_event){
    		that.__onKeyUp(_event);
    	});
    	v._$addEvent('btn-search','click',function(){
      	  that.__onSearch();
        });
        v._$addEvent('openall','click',function(){
            e._$delClassName('openclose','btn-clicked');
            e._$addClassName('openall','btn-clicked');
      	    that.__Tree._$expandAll(true);
        });
        v._$addEvent('openclose','click',function(){
        	e._$delClassName('openall','btn-clicked');
            e._$addClassName('openclose','btn-clicked');
      	    that.__Tree._$expandAll(false);
        });
        $("#assignDistrict")._$on("click",function(){
        	that.__assignDistrict();
        });
        $(".editCategoryName")._$on("click",function(){
        	that.__editCategoryName();
        });
    };
    pro.__editCategoryName = function(){
    	var that = this;
    	this.cateWin = null;
    	if(!!this.cateWin){
    		cateWin._$recycle();
        }
    	this.cateWin = CategoryWin._$allocate({
        	parent:document.body,
            onok:function(){
            	that.__updateCategoryName();
            }
        })._$show();
    	$("#name")._$val($("#detailHeader")._$text());
    };
    pro.__initForm = function(){
		this.form = f._$$WebForm._$allocate({
			form:"search-form"	
		});
    }
    pro.__addAnchorP = function(){
    	var anchorP=window.location.hash ||"";
    	if(anchorP!="")
    		$(anchorP)[0].scrollIntoView();
    }
    pro.__onKeyUp = function(_event){
    	if(_event.keyCode == 13){
    		this.__onSearch();
    	}
    }
    pro.__onSearch = function(){
    	var data = this.form._$data(),that=this;
    	data["rootId"] = window.__rootId;
    	Request('/category/content/search',{
			method:'GET',
			data:data,
			type:'JSON',
			progress : true,
			onload:function(dt){
				if(dt.code=="200"){
					console.log(dt.result.list);
					that.__initTree(dt.result.list);
				}else{
					notify.show({
						'type':'error',
						'message':dt.message
					});
				}
			},
			onerror:function(dt){
				notify.show({
					'type':'error',
					'message':dt.message
				});
			}
    	});
    };
    
    pro.__getDetail = function(){
    	var that = this;
    	var itemId = window.__rootId;
    	Request("/category/content/listCategoryContentByRootId",{
    		methold:'GET',
    		type:'json',
    		progress : true,
    		data:{"rootId": itemId},
    		onload : function(dt){
    			if(!dt.result){
    				that.setBtnDisabled(true);
    				return;
    			}else if(dt.result.length == 0){
    				that.setBtnDisabled(true);
    				return;
    			}
				pro.__initTree(dt.result);
				pro.areaList = dt.result.sendDistrictDTOs;
				that.setBtnDisabled(false);
			},
			onerror:function(dt){
				notify.show({
					'type':'error',
					'message':dt.message
				});
				that.setBtnDisabled(true);
			}
		});
    };
    pro.setBtnDisabled = function(flag){
    	document.getElementById("openall").disabled = flag;
    	document.getElementById("openclose").disabled = flag;
    	document.getElementById("assignDistrict").disabled = flag;
    };
    pro.__initTree=function(data){
    	var that=this;
    	if(!!this.__Tree){
            this.__Tree._$recycle();
          }
        this.__Tree = Tree._$allocate({
	      	parent:document.body,
	      	setting:{
	      		tree:data,
		      	Template:"seedTree",
		      	wrap:"categrid",
		      	callback:{
		      		onInitAfter:function(obj){
		      			obj._$district();
		      			$("body")._$on("click",function(_event){
		      				var _list = e._$getByClassName(obj.treeObj,'d-ul');
		      				for(var i=0,len=_list.length;i<len;i++){
 		 					   if(_list[i].style.display=="block"){
 		 						  _list[i].style.display="none";
 		 						}
 		 					 }
		      			});

		      		},
		      		onAdd:function(id,node){
		      			var contentCategoryId=node.id;
		      			window.location.href="/category/content/createPage?id="+contentCategoryId + "&rootId=" + window.__rootId;
		      		},
		      		onEdit:function(id,node){
		      			var contentCategoryId=node.id;
		      			window.location.href="/category/content/edit?contentCategoryId="+contentCategoryId + "&rootId=" + window.__rootId;
		      		},
		      		onDel:function(id,node){
		      			var contentCategoryId=node.id;
		      			that.__delNode(contentCategoryId,node);
		      		},
		      		onDown:function(id,node){
		            	 //<span class="make col-sm-1" treenode="down"><span class="glyphicon glyphicon-arrow-down"></span>下移</span>
		      		},
		      		onUp:function(id,node){
		      			//<span class="make col-sm-1" treenode="up"><span class="glyphicon glyphicon-arrow-up"></span>上移</span>
		      		}
		      	}
	      	}
	     });
    };
    pro.__updateCategoryName = function(){
		var that = this;
    	var data = {
    		"name" : $("#name")._$val(),
    		"id" : window.__rootId
    	};
		Request('/category/content/update',{
			data:data,
    		method:'POST',
            onload:function(json){
            	if(json.code=="200"){
            		notify.show({
						'type':'success',
						'message':'修改成功'
					});
            		$("#detailHeader")._$text($("#name")._$val());
            		that.cateWin._$hide();
            		
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
    };
    pro.__delNode=function(id,node){
    	if(id=="") return false;
    	Request('/category/content/delete',{
			method:'GET',
			data:{
				categoryId:id
			},
			type:'JSON',
			onload:function(dt){
				if(dt.code=="200"){
					notify.show({
						'type':'success',
						'message':"删除成功"
					});
					e._$remove(node,false);
				}else{
					notify.show({
						'type':'error',
						'message':dt.message
					});
				}
			},
			onerror:function(dt){
				notify.show({
					'type':'error',
					'message':dt.message
				});
			}
    	});
    };
    pro.__assignDistrict = function(){
    	this.DisWin = null;
    	var that = this;
    	if(!!this.DisWin){
        	obj._$recycle();
        	document.getElementById("districtForm").innerHTML = "";
        }
    	
    	this.DisWin = DistrictWin._$allocate({
        	parent:document.body,
        	onok:function(){
            	that.__submitDistrict();
            }
        })._$show();
    	
    	var districtStr = $("#sendDistrictDTOs")._$text();
		this.districtForm =$('#districtForm');
    	var item = [];
    	this.areaData = [];
    	if(districtStr.indexOf("#") > -1){
    		var districtList = districtStr.split("#");
    		for(var i = 0 ; i < districtList.length -1;i++){
    			var areaStr = districtList[i];
    			var areaList = areaStr.split("-");
    			var data = {
    				"isEdit" : areaList[0],
    				"provinceId" : areaList[1],
	    			"cityId" : areaList[2],
	    			"districtId" : areaList[3],
	    			"provinceName" : areaList[4],
	    			"cityName" : areaList[5],
	    			"districtName" : areaList[6]
	    		};
    			
//    			if(areaList[0] == "false"){
//        			this.areaData.push(data.districtId == 0 ? (data.cityId == 0 ? data.provinceId : data.cityId) : data.districtId);
//        			$("#districtForm")._$insert('<div class="j-address col-sm-12" areaId="">'+ areaList[4] + areaList[5] + areaList[6] +'</div>','append');
//        		}else if(areaList[0] == "true"){
        			this.__addDistrict(data);
//        		}
    		}
    	}


		var $districtAdd=$("#districtAdd");
		$districtAdd._$on("click",function(){
    		that.__addDistrict();
    	});
    };
    pro.__submitDistrict = function(){
    	var that = this;
    	var data = this.__getDistrict();
    	//若存在不可编辑的地区
//    	if(this.areaData.length > 0){
//    		data["districtIds"] += this.areaData.join();
//    	}
//    	if($("#districtForm .j-address")[0])
    	data["id"] = window.__rootId;
    	data["name"] = $("#detailHeader")._$text();
		Request('/category/content/update',{
			data:data,
    		method:'POST',
    		progress:true,
            onload:function(json){
            	if(json.code=="200"){
            		notify.show({
						'type':'success',
						'message':'修改成功'
					});
            		that.DisWin._$hide();
            		location.href= "/category/content/editContentTree?rootId=" + window.__rootId;
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
    };
    pro.__addDistrict = function(item){
    	var div = e._$create('div','form-group'),that=this;
        div.innerHTML = '<div class="j-address col-sm-12"></div>';
        this.districtForm[0].appendChild(div);
        var vlist = e._$getByClassName(div,'j-address');
        if(item){
        	Address._$$AddressBox._$allocate({
            	parentNode:vlist[0],
                data:item,
                remove:function(node){this.__removeDistrict(node)}._$bind(this)
            });    		
        }else{
        	Address._$$AddressBox._$allocate({
                parentNode:vlist[0],
                remove:function(node){this.__removeDistrict(node)}._$bind(this)
            });
        }
    };
    pro.__removeDistrict = function(node){
		var list = e._$getByClassName(this.districtForm[0],'j-address');
		//若存在不可编辑的地区，允许删除最后一个区域组件
    	if(this.areaData){
    		e._$remove(node.parentNode);
    	}else{
	    	// 最后一个不允许删除
			if(list.length ==1){
				notify.show('请至少填写一个区域');
				return;
			}			
			e._$remove(node.parentNode);
    	}
	};
	
	pro.__getDistrict = function(){
        var data = {};
		var list = e._$getByClassName(this.districtForm[0],'j-address');
        var districtNames = [];
        var districtIds = [];
        for (var i=0,l=list.length; i<l; i++) {
			var vlist = e._$getByClassName(list[i],'form-control');
            if(vlist.length == 0){
            	districtNames.push($(list[i])._$attr('areaName'));
            	districtIds.push($(list[i])._$attr('areaId'));
            	continue;
            }
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
    p._$$ContentModule._$allocate();
  });