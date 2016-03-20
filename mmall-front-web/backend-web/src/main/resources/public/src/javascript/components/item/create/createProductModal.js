define([
	'text!./createProductModal.html',
	'{pro}widget/BaseComponent.js',
	'{pro}extend/util.js',
	'{pro}components/notify/notify.js'
	],function(tpl,BaseComponent,_,notify){

	return BaseComponent.extend({
		template:tpl,
		config:function(data){
			_.extend(data,{
				model:{}
			})
		},
		init:function(){
			this.loadProductModelList();
		},

		loadProductModelList:function(){
			var categoryId = this.data.categoryId;
			this.$request("/item/product/getModel?categoryId="+categoryId,{
	          	method:'GET',
	          	//type:'json',
	          	onload:function(_json){
		            if(_json.code==200){
		              	this.data.model = _json.result;
		              	console.log('categoryId='+this.data.model);
		            }
	          	},
	          	onerror:function(_error){
		          	console.log('_error');
	          	}
	        })
		},

		$getSelectProductModel:function(){},
	});
})