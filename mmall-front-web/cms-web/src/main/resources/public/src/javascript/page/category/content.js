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
    './tree/tree.js',
    '{pro}page/category/categorywin/editCategoryWin.js',
    '{pro}page/category/categorywin/assignDistrictWin.js',
	'{pro}/widget/district/address.js',
	'{pro}components/progress/progress.js',
	'{pro}page/access/account/delete.win.js'
  ],
  function(ut,v,e,Module,f,Request,notify,$,Tree,CategoryWin,DistrictWin,Address,progress,DeleteWin,p) {
    var _pro;

    p._$$ContentModule = NEJ.C();
    pro = p._$$ContentModule._$extend(Module);

    pro.__init = function(_options) {
      var that=this;
      this.__super(_options);
//      this.__initPage();
      this.__initList();
      this.__addEvent();
    };
    pro.__addEvent = function(){
    	var that  = this;
        $("#createTree")._$on("click",function(){
        	that.__createCategoryTree();
        });
        $("#areaSelector")._$on("change",function(){
        	that.__initList();
        });
    };
    pro.__addAnchorP = function(){
    	var anchorP=window.location.hash ||"";
    	if(anchorP!="")
    		$(anchorP)[0].scrollIntoView();
    }

    pro.__initList = function(){
    	var param = {};
    	var areaId = $("#areaSelector")._$val();
		param["areaId"] = areaId;
    	Request('content/listCategoryContent',{
    		methold:'GET',
    		type:'json',
    		data:param,
    		onload : function(dt){
//    			alert(1);
    			if(dt.code == 200){
    				pro.__initTreeList(dt.result);
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
    pro.__initTreeList = function(data){
    	var that = this;
    	if(!!this.__TreeList){
            this.__TreeList._$recycle();
          }
        this.__TreeList = Tree._$allocate({
	      	parent:document.body,
	      	setting:{
	      		tree:data,
		      	Template:"cateTreeList",
		      	wrap:"categoryList",
		      	isopen:false,
		      	callback:{
		      		onInitAfter : function(obj){
//		      			obj._$expandAll(false);
		                $(".detail")._$on("click",function(){
			            	var itemId = $(this)._$parent(".panel")._$attr("itemId");
			            	that.rootId = itemId;
			            	
			            	pro.__getDetail(itemId);
			            });
			            $(".remove")._$on("click",function(){
			            	var itemId = $(this)._$parent(".panel")._$attr("itemId");
			            	pro.__removeCategoryTree(itemId);
			            });
			            $(".multiArea")._$on("click",function(){
			            	var message = $(this)._$next()._$text();
			            	notify.show({
			    				'type':'info',
			    				'message':message,
			    				duration:"10000"
			    			});
			            });
		      		}
		      	}
	      	}
        });
    };
    pro.__createCategoryTree = function(){
    	var data = {
    		"level" : 0,
    		"name" : "未命名",
    		"rootId" : 0
    	};
		Request('/category/content/add',{
			data:data,
    		method:'POST',
            onload:function(json){
            	if(json.code=="200"){
            		notify.show({
						'type':'success',
						'message':'添加成功'
					});
            		pro.__initList();
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
    		
    };
   
    pro.__removeCategoryTree = function(itemId){
        
    	if(!!this.__deleteWin){
          this.__deleteWin._$recycle();
        }
        this.__deleteWin = DeleteWin._$allocate({
          parent:document.body,
          onok:function(){
        	  Request("content/deleteTree",{
        		 method : "get",
        		 data : {"rootId":itemId},
        		 onload : function(json){
        			 if(json.code == 200){
        				 pro.__initList();
        				 pro.__deleteWin._$hide();
        			 }
        		 },
        		 onerror : function(json){
        			 notify.showError('删除失败'); 
        		 }
        	  });
          }
        })._$show();
    };
    p._$$ContentModule._$allocate();
  });