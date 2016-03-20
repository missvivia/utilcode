/*
 * ------------------------------------------
 * 分类管理中商品分类列表
 * @version  1.0
 * @author   durianskh(shaokehua@xinyunlian.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'text!./list.html',
    'pro/components/ListComponent',
    'pro/components/notify/notify',
    '{pro}widget/layer/category/create.normal.js',
    "{pro}extend/util.js"
],function(_ut,_html,ListComponent,notify,createCategoryWin,_,_p,_o,_f,_r)
{
    return ListComponent.extend(
    {
        url:'/category/normal/list',
        api:'/category/normal/update',
        api_create:'/category/normal/create',
        template:_html,
        
        updateCategory:function(
        		rootId,
        		secondCategoryId,
        		categoryId,
        		categoryName,
        		parentId,
        		parentName,
        		level,
        		callback)
        {
        	var _url = this.api;
        	createCategoryWin._$allocate(
        	{
        		type:1,
        		categoryId:categoryId,
        		categoryName:categoryName,
        		parentId:parentId,
        		parentName:parentName,
        		onok:function(data)
        		{
        			this.$emit('updatelist');
        			setTimeout(function()
        	        {
        				if(level == 1)
        				{
        					if(secondCategoryId != "")
        					{
        						callback(rootId, secondCategoryId, level);
        					}
        					else
        					{
        						callback(rootId, categoryId, level);
        					}
        	            }
        	            else
        	            {
        	            	if(secondCategoryId != "")
        					{
        						callback(rootId, secondCategoryId, 1);
            	                callback(rootId, secondCategoryId, level);
        					}
        					else
        					{
        						callback(rootId, categoryId, 1);
            	                callback(rootId, categoryId, level);
        					}
        	            }
        	        }, 1500);
        		}._$bind(this)})._$show();
        },
        getListParam: function(){
            var data = this.data;
            return {
                isPage : -1
              };
          },        
        refresh:function(_data)
        {
            if (!!_data.url)
            {
                this.url = _data.url;
                delete _data.url;
            }
        	this.data.current = 1;
            this.data.condition = _data;
            this.$emit('updatelist');
        },
        
        createCategory:function()
        {
        	var _url = this.api_create;
        	createCategoryWin._$allocate({onok:function(data)
        	{
        		this.$emit('updatelist');
        	}._$bind(this)})._$show();
        },
        
        createSubCategory:function(rootId,parentId,parentName,level, callback)
        {
        	var _url = this.api_create;
        	createCategoryWin._$allocate({parentId:parentId,parentName:parentName,onok:function(data)
        	{
        		this.$emit('updatelist');
            	setTimeout(function()
            	{
            		if(level == 1)
            		{
            			callback(rootId, parentId, level);
            		}
            		else
            		{
            			callback(rootId, parentId, 1);
                		callback(rootId, parentId, level);
            		}
            	}, 1500);
        	}._$bind(this)})._$show();
        },
        
        packUpCategory:function(categoryId, subCategoryId, level)
        {
        	if(level == 1)
        	{
        		var eles = document.getElementsByName(categoryId + "2");
            	for(var i = 0; i < eles.length; i++)
            	{
            		if(eles[i].style.display == "none")
            		{
            			eles[i].style.display = "";
            		}
            		else
            		{
            			eles[i].style.display = "none";
            			var subEles = document.getElementsByName(categoryId + "3");
            			for(var j = 0; j < subEles.length; j++)
            			{
            				subEles[j].style.display = "none";
            			}
            		}
            	}
        	}
        	else if(level == 2)
        	{
        		for(var i = 0; i < 1000; i++)
        		{
        			var ele = document.getElementById("" +subCategoryId + i);
        			if(ele != null)
        			{
        				if(ele.style.display == "none")
                		{
                			ele.style.display = "";
                		}
                		else
                		{
                			ele.style.display = "none";
                		}
        			}
        			else
        			{
        				break;
        			}
        		}
        	}
        },
        
        packUpAllCategory:function()
        {
        	var eles = document.getElementsByTagName("tr");
        	for(var i = 0; i < eles.length; i++)
        	{
        		if(eles[i].attributes["name"].value == "firstclasscategory")
        		{
        			eles[i].style.display = "";
        		}
        		else
        		{
        			eles[i].style.display = "none";
        		}
        	}
        },
        
        unPackUpAllCategory:function()
        {
        	var eles = document.getElementsByTagName("tr");
        	for(var i = 0; i < eles.length; i++)
        	{
        		eles[i].style.display = "";
        	}
        },
        
        remove:function(categoryId, childNodeId)
        {
        	this.$request('/category/normal/delete',
        	{
        		data:{"categoryId":categoryId},
        		method:'GET',
        		onload:function(json)
        		{
        			if(json.code==200)
        			{
        				notify.show('删除成功');
        				var parentNode = document.getElementById("listBody");
        				var childNode = document.getElementById(childNodeId);
        				parentNode.removeChild(childNode);
        			} 
        			else
        			{
        				notify.show(json.message||'删除失败');
        			}
        		}._$bind(this),
        		onerror:function(json)
        		{
        			notify.show(json.message||'删除失败');
        		}
        	})
        }
    });
});