/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    '{lib}base/util.js?v=1.0.0.0',
    'text!./list.html?v=1.0.0.3',
    '{pro}components/MmallListComponent.js?v=1.0.0.0',
    '{pro}components/notify/notify.js?v=1.0.0.0',
    '{pro}widget/layer/spu/create.spu.js?v=1.0.0.1',
    '{lib}util/ajax/xdr.js?v=1.0.0.0',
    '{lib}util/file/select.js?v=1.0.0.0',
    '{pro}components/modal/modal.js?v=1.0.0.0',
    "{pro}widget/layer/window.js",
    '{lib}util/chain/NodeList.js',
    '../synchronize/synchronize.window.js?v=1.0.0.0'
],function(_ut,_html,MmallListComponent,notify,createSpuWin,j,_e,Modal,Dialog,$,SynWindow,_o,_f,_r)
{
    return MmallListComponent.extend(
    {
        url:'/item/spu/list',
        template:_html,
        
        getExtraParam:function()
        {
            return this.data.condition;
        },
        
        refresh:function(_data)
        {
            if (!!_data.url)
            {
                this.url = _data.url;
                delete _data.url;
            } 
            this.data.condition = _data;
            if(this.data.currentPage == 1){
            	this.$emit('updatelist');
            }else{
            	this.data.currentPage = 1;
            	this.$update();
            }
            
        },
        
        updateSpu:function(spuId,spuBarCode,spuName,brandId,categoryNormalId,categoryNormalName,event){
        	createSpuWin._$allocate(
        	{
        		type:1,
        		spuId:spuId,
        		spuName:spuName,
        		spuBarCode:spuBarCode,
        		brandId:brandId,
        		categoryNormalId:categoryNormalId,
        		categoryNormalName : categoryNormalName,
        		brandList:window.brandList,
        		categoryList:window.categoryList,
        		onok:function(data)
        		{
        			this.$emit('updatelist');
        		}._$bind(this)
        	})._$show();
        	if($(event.target)._$next()._$next()[0].innerText == "已同步"){
            	$("#syncBtn")._$attr("class","btn j-flag btn-disabled");
            }
        },
        
        createSpu:function()
        {
        	createSpuWin._$allocate({brandList:window.brandList,onok:function(data)
        	{
        		this.$emit('updatelist');
        	}._$bind(this)})._$show();
        },
        
        filtrate:function()
        {
        	this.data.condition.searchValue = document.getElementsByName("searchValue")[0].value;
        	this.data.condition.brandId = document.getElementById("brands").value;
        	var itemAttr = $('#categories .itemCategory')._$attr("value");
        	this.data.condition.categoryNormalId = itemAttr ? itemAttr : -1;
        	this.$emit('updatelist');
        },
        
        deleteSpu:function(spuId)
        {
        	this.$request('/item/spu/del',
        	{
        		data:{"spuIds":spuId},
        		method:'GET',
        		onload:function(json)
        		{
        			if(json.code==200)
        			{
        				notify.show('删除成功');
//        				var parentNode = document.getElementById("listBody");
//        				var childNode = document.getElementById(spuId);
//        				parentNode.removeChild(childNode);
        				this.$emit('updatelist');
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
        },
        synchronizeSpu : function(spuId){
            if(event.target.innerText == "已同步"){
            	return;
            }
        	this.__SynWin = SynWindow._$allocate({
	           	"spuId" : spuId
           })._$show();
        },
        importSpu : function(){
		     var _id = _e._$bind('importSpu',{
		         name : "file",
		         accept : "xls,xlsx",
		    	 onchange:this.onLogoUpload._$bind(this) 
		     });
        },
		onLogoUpload : function(event){
           this.__loadingWin = Dialog._$allocate({
	           	clazz : "loading m-window",
	           	parent:document.body,
           })._$show();
           $(".loading .zcnt")[0].innerText = "导入中,请稍候...";				
			var form = event.form;
			form.action =  '/item/spu/import';
			j._$upload(form,{
				type:"text",
				onload:function(result){
					this.__loadingWin._$hide();
					var list = result.split("\n");
					var html = list.join("</div><div>");
					html = "<div>" + html + "</div>";
					var modal = new Modal({
			        	data:{
			          	'title':'提示',
			          	'content':html,
			          	'width':500,
			        	}
			      	});
					this.$emit('updatelist');
				}._$bind(this),
				onerror:function(result){
					notify.show(result);
				}
			})
		}
    });
});