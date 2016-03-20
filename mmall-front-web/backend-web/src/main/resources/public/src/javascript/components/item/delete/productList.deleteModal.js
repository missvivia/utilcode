
define([
	'{pro}extend/util.js',
  "{pro}components/modal/modal.js",
  "{pro}components/item/delete/productList.deleteView.js",
  '{lib}base/element.js'
  ], function(ut,Modal, DeleteView , e){


  /**
   * 弹窗组件
   * 直接调用, this.destroy() 来关闭弹窗和回收
   * Event:
   *   -close (data): 关闭事件, 事件对象即data
   *   -confirm (data): 确认时间，时间对象即data
   * 配置: 
   *   content: 即弹窗body处的内容
   */


  var ProductListDeleteModal = Modal.extend({
    // 默认属性
    // 计算属性
    content: '<productListDelete-view type={{type}} allCount={{unShelveItems.length+onShelveItems.length}} unShelveCount={{unShelveItems.length}} onShelveCount={{onShelveItems.length}}></productListDelete-view>',
	config:function(data){
		ut.extend(data,{
			onShelveChecked:false,
			unShelveChecked:false
		});
	},  
    init: function(){
    	this.data.onShelveChecked = false;
    	this.data.unShelveChecked = false;
      	this.supr();
    },
    
    $getDeleteItemIds:function(){
    	var items = [];
    	var delType = 0;
    	if(this.data.type==1){
    		var elm = e._$get('onShelveCheck')//this.$refs.onShelveCheck;
    		if(elm.checked){
    			this.data.onShelveChecked = true;
    			//ut.mergeList(items,this.data.onShelveItems,this.data.onShelveItems.skuId||'id');
    			//items += this.data.onShelveItems;
    		}
    		
    		var unElm = e._$get('unShelveCheck');
    		if(unElm.checked){
    			this.data.unShelveChecked = true;
    			//ut.mergeList(items,this.data.unShelveItems,this.data.unShelveItems.skuId||'id');
    			//items += this.data.unShelveItems;
    		}
    	}
    	else if(this.data.type==2){
    		this.data.unShelveChecked = true;
    		//ut.mergeList(items,this.data.onShelveItems,this.data.onShelveItems.skuId||'id');
    		//ut.mergeList(items,this.data.unShelveItems,this.data.unShelveItems.skuId||'id');
    		//items += this.data.onShelveItems;
    		//items += this.data.unShelveItems
    	}
    	else if(this.data.type==3){
    		this.data.onShelveChecked = true;
    	}
    	//return items;
    }
  }).component("productListDelete-view", DeleteView)



  return ProductListDeleteModal;
})