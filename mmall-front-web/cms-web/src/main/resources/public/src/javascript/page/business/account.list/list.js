/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
	'base/event',
    'base/element',
    'base/util',
    'text!./list.html',
    'pro/components/ListComponent',
    'pro/components/notify/notify',
    '{pro}components/modal/modal.js',
],function(v,e,_ut,_html,ListComponent,notify,Modal,_p,_o,_f,_r){
    return ListComponent.extend({
        url:'/business/account/list',
        template:_html,
        format:function(timestamp){
        	var date = new Date(timestamp);
        	return _ut._$format(date,'yyyy-MM-dd');
        },
        getExtraParam:function(){
            return this.data.condition;
        },
        shouldUpdateList: function(data){
            return true;
        },
        /** init方法覆盖了父类方法，document添加click，点击展开按钮也会收到事件
        init:function(){
        	this.$on("updatelist", this.__getList.bind(this));
        	v._$addEvent(document,'click',this.hideCard._$bind(this));
        },*/
        lock:function(item){
            this.$request('/business/lock/'+item.id,{
            	onload:function(json){
            		if(json.code==200){
            			item.isActive = 1;
            		} else{
            			if(json.result){
            				notify.show(json.result.msg);
            			} else{
            				notify.show('冻结失败');
            			}
            		}
            	}
            })
        },
        unlock:function(item){
        	this.$request('/business/unlock/'+item.id,{
            	onload:function(json){
            		if(json.code==200){
            			item.isActive = 0;
            		} else{
            			if(json.result){
            				notify.show(json.result.msg);
            			} else{
            				notify.show('解结失败');
            			}
            		}
            	}
            })
        },
        del:function(item,index){
        	var modal = new Modal({
            	data:{
              	'title':'删除商家',
              	'content':'确定删除商家？',
              	'width':400}
          	});

     	    modal.$on('confirm',function(){
                this.$request('/business/delete/'+item.id,{
                    onload:function(json){
                        if(json.code==200){
                            if(json.result){
                                notify.show(json.result.msg);
                            } else{
                                notify.show('删除成功');                            
                            }

                            this.data.list.splice(index, 1);
                        } else{
                            if(json.result){
                                notify.show(json.result.msg);
                            } else{
                                notify.show('删除失败');
                            }
                        }
                    },
                    onerror:function(){notify.show('删除失败');}
                })

                modal.destroy();
            }.bind(this));

            modal.$on('close',function(){modal.destroy();}.bind(this));
        },

        refresh:function(_data){
        	//console.log('zdy___refresh');
            if (!!_data.url){
                this.url = _data.url;
                delete _data.url;
            } 
        	this.data.current = 1;
            this.data.condition = _data;
            this.$emit('updatelist');
        },
        getList:function(filter){
            return this.data.list.filter(function(_item){
                return !!_item[filter];
            });
        },
        computed:{
            allChecked:{
                get: function(_data){
                    return _data.list.length===(this.getList('checked')||_r).length;
                },
                set: function(_sign,_data){
                    _ut._$forEach(
                        this.data.list,function(_it){
                            _it.checked = _sign;
                        }
                    );
                }
            }
        },
        $getCheckIds:function(){
        	var items = this.getList('checked'),idList=[];
        	for(var i=0,l=items.length;i<l;i++){
        		idList.push(items[i].id);
        	}
        	return idList;
        },
        batchRemove:function(){
        	var _list = this.getList('checked')||_r;
            if (!_list||!_list.length){
                notify.showError('请先选择要操作的项');
                return;
            }
            var _arr = [];
            _ut._$forEach(
                _list,function(_it){
                    _arr.push(_it.id);
                }
            );
            var ids = _arr.join(',');
            
            var modal = new Modal({
                data:{
              	'title':'删除商家',
              	'content':'确定删除商家？',
              	'width':400}
            });

            modal.$on('confirm',function(){
                this.$request('/business/batchdelete',{
                    data:ids,
                    method:'POST',
                    onload:function(_json){
                        if(_json.code==200){
                            notify.show('删除成功');
                            this.$emit('updatelist');
                        }
                        else{
                            notify.show('删除失败');
                        }
                    },
                    onerror:function(_json){
                        notify.show('删除失败');
                    }
                })

                modal.destroy();
            }.bind(this));

            modal.$on('close',function(){modal.destroy();}.bind(this));
        },
        
        distcitClick:function(_nodeId){
        	var elm = e._$get(_nodeId);
        	
        	if(e._$hasClassName(elm,'open')){
        		e._$delClassName(elm,'open');
        	}
        	else {
        		e._$addClassName(elm,'open');
        	}
        },
        hideCard:function(_event){
        	console.log('zd----'+_event);
        	var list = e._$getByClassName(this,'btn-group');
        	for(var i=0;i<list.length;i++){	
        		var elm = list[i];
        	
        		if(e._$hasClassName(elm,'open')){
        			e._$delClassName(elm,'open');
        		}
        	}
        }
    });
});