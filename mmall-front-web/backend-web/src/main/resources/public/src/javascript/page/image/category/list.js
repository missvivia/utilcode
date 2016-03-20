/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'text!./list.html',
    'pro/components/ListComponent',
    'pro/components/notify/notify',
    './add/add.js?v=1.0.0.1'
],function(_u,_html,ListComponent,notify,AddWin,_p,_o,_f,_r){
    return ListComponent.extend({
        url:'/image/category/list',
        api:'/image/category/',
        template:_html,
       
        _sendReq:function(_url,_data){
            this.$request(_url,{
                method:'POST',
                data:_data,
                onload:function(_json){
                    notify.show('操作成功');
                    _schedule.status = 3;
                    //this.$emit('updatelist');
                },
                onerror:function(_error){
                    notify.showError('操作失败');
                }
            });
        },
        update:function(_item){
        	var _win = new AddWin({data:{title:'修改分类',name:_item.name}});
        	_win.$on('onok',function(_event){
        		
        		this.$request(this.api+'update',{
                    method:'POST',
                    data:{id:_item.id,name:_event.name},
                    onload:function(_json){
                   	 if(_json.code==200){
                   		 notify.show('分类更新成功');
//                   		_item.name = _event.name
                   		 this.$emit('updatelist');
                   	 }
                        
                    },
                    onerror:function(_error){
                        notify.showError('分类更新失败');
                    }
                });
        		_win.close();
        	}._$bind(this))
        },
        remove:function(_item,_index){
        	 this.$request(this.api+'remove',{
                 method:'POST',
                 data:{id:_item.id},
                 onload:function(_json){
                	 if(_json.code==200){
                		 notify.show('操作成功');
                		 this.data.list.splice(_index,1);
                	 }
                     //this.$emit('updatelist');
                 }._$bind(this),
                 onerror:function(_error){
                     notify.showError('操作失败');
                 }
             });
        },
        newCategory:function(_item,_index){
        	var _win = new AddWin({data:{title:'添加分类'}});
        	_win.$on('onok',function(_event){
        		this.$request(this.api+'add',{
                    method:'POST',
                    data:{name:_event.name},
                    onload:function(_json){
                    	if(_json.code==200){
                    		notify.show('添加成功');
                    		this.data.list.push({name:_json.result.dirName,id:_json.result.id});
                    	}
                        //this.$emit('updatelist');
                    }._$bind(this),
                    onerror:function(_error){
                        notify.showError('操作失败');
                    }
                });
        		_win.close();
        	}._$bind(this))
        },
    });
});