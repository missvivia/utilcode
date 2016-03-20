/*
 * ------------------------------------------
 * 分类管理中商品分类列表
 * @version  1.0
 * @author   durianskh(shaokehua@xinyunlian.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'text!./list.html?v=1.0.0.1',
    'pro/components/ListComponent',
    'pro/components/notify/notify',
    '{pro}widget/layer/category/create.normal.js',
    '{lib}util/chain/NodeList.js'
],function(_ut,_html,ListComponent,notify,createCategoryWin,$,_p,_o,_f,_r)
{
    return ListComponent.extend(
    {
        url:'/category/normal/list',
        api:'/category/normal/update',
        api_create:'/category/normal/create',
        template:_html,
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
        getListParam: function(){
            var data = this.data;
            return {
                isPage : -1
              };
        },         
        packUpCategory:function($event,categoryId, level)
        {
        	var eles = $($event.target.parentNode.parentNode)._$siblings();
        	var i = 0;
        	if(level == 1){
        		for(var i = 0; i < eles.length;i++){
          			if($(eles[i])._$attr("name") == (""+categoryId+"2")){
		        		if(eles[i].style.display == "none"){
		        			eles[i].style.display = "";
		        		}else{
	            			eles[i].style.display = "none";
		        		}
          			}else if($(eles[i])._$attr("name") == (""+categoryId+"3")){
          				eles[i].style.display = "none";		        			
          			}
	        	}
        	}else if(level == 2){
        		for(var i = 0; i < eles.length;i++){
        			if($(eles[i])._$attr("name") == (""+categoryId+"3")){
        				if(eles[i].style.display == "none"){
		        			eles[i].style.display = "";
		        		}else{
	            			eles[i].style.display = "none";
		        		}
        			}
        		}
        	}
        },
        
        packUpAllCategory:function()
        {
        	var eles = this.listTable._$children()._$children()._$children("tr");
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
        

        openContent : function($event){
        	this.itemCategory = $($event.target);
        	this.listTable = $($event.target.parentNode)._$next();
        	this.packUpAllCategory();
        	if(this.listTable._$style("display") == "none"){
        		this.listTable._$style("display","block");
        	}else{
        		this.listTable._$style("display","none");
        	}
        },
        selectCategory : function(categoryId,categoryName){
        	this.itemCategory._$attr("value",categoryId);
        	this.itemCategory._$text(categoryName);
        	this.listTable._$style("display","none");
        	
        }
    });
});