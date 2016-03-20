NEJ.define([
    'base/util',
    'text!./list.html',
    '{pro}components/MmallListComponent.js',
    '{pro}components/notify/notify.js'
],function(_ut,_html,ListComponent,notify,form,datepicker){
    return ListComponent.extend({
        url:'/item/model/list',
        template:_html,
        getExtraParam:function()
        {
            return this.data.condition;
        },
        refresh:function(_data){
        	if (!!_data.url){
                this.url = _data.url;
                delete _data.url;
            }
        	
        	this.data.current = 1;
            this.data.condition = _data;
            this.$emit('updatelist');
        },
        removeModel:function(model,index){
        	this.$request('/item/model/del',{
        		data:{modelId:model.modelId},
        		method:'GET',
        		onload:function(json){
        			if(json.code==200){
        				notify.show({
							'type':'success',
							'message':json.message
						});
        				this.data.list.splice(index,1);
        			} else{
        				notify.show({
							'type':'error',
							'message':json.message
						});
        			}
        		}._$bind(this),
        		onerror:function(json){
        			notify.show({
						'type':'error',
						'message':json.message
					});
        		}
        	})
        }
        
    });
});