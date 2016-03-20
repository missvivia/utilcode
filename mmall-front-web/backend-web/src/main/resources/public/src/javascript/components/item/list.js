/**
 * 活动列表筛选
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  "{pro}extend/util.js?v=1.0.0.0",
  '{lib}base/element.js?v=1.0.0.0',
  '{pro}widget/util/preview.js?v=1.0.0.0',
  '{pro}extend/config.js?v=1.0.0.0',
  '{pro}components/notify/notify.js?v=1.0.0.0',
  'text!./list.html?v=1.0.0.4',
  '{pro}components/ListComponent.js?v=1.0.0.0',
  '{pro}components/modal/modal.js?v=1.0.0.0',
  '{pro}components/item/delete/productList.deleteModal.js?v=1.0.0.0',
  '{lib}base/util.js'
  ], function(_u,_e,preview, config, notify, tpl, ListComponent,Modal,DeleteModal,util){

  var SizeList = ListComponent.extend({
    url: '/item/product/searchProduct',
    template: tpl,
    data:{nowTime:new Date().getTime()},
    config: function(data){
    	this.supr(data);
    	data.limit = 20;
    	if(window.flag != 1){
    		data.current = window.offset/data.limit + 1;
    	}
        data.type = window.listType;
      },

    // @子类修改
    watchedAttr: ['current'],
    getExtraParam: function(data){
    	return data.condition;
    },
    getListParam: function(){
        var data = this.data;
        return _u.extend({
        	limit: data.limit,
        	offset: data.limit * (data.current-1),
        	isPage : 1,
          }, this.getExtraParam(data));
    },
    _getList:function(filter){
          return this.data.list.filter(function(_item){
              return !!_item[filter];
          });
    },
    computed:{
        allChecked:{
            get: function(_data){
                return _data.list.length===(this._getList('checked')||_r).length;
            },
            set: function(_sign,_data){
                util._$forEach(
                    _data.list,function(_it){
                        _it.checked = _sign;
                    }
                );
            }
        }
    },
    
    init:function(){
      this.supr();
    },
	onChange : function(e){
    	var _node = e.target;
		this.data.condition.status = _node.value;

		if(this.data.current == 1){
			this.$emit('updatelist');
		}else{
			this.data.current = 1;
			this.$update();
		}
	},
	refresh : function(_data) {
		this.data.condition = _data;
		if(this.data.current == 1){
			this.$emit('updatelist');
		}else{
			this.data.current = 1;
			this.$update();
		}
	},
    modifyStockClick:function(_id){
//      console.log('id='+_id);
      
      var div = _e._$get(_id);
      var text = div.children[0];
      var input = div.children[1];
      
      _e._$replaceClassName(input,'hide','show');
      _e._$replaceClassName(text,'show','hide');
      
      	var elm = input.children[0];
    	elm.focus();
    	this.skuNum = elm.value;
    },

    // input blur时触发 修改库存
    updateStock:function(_id){
      	var div = _e._$get(_id);
      	var text = div.children[0];
      	var input = div.children[1];

      	var elm = input.children[0];
      	var count = elm.value;
      	//console.log('stock count='+count);
      	var hideInput = function(){
      		_e._$replaceClassName(text,'hide','show');
      		_e._$replaceClassName(input,'show','hide');
      	};
      	var reg = /^\d+$/;
      	if(this.skuNum == count){
      		hideInput();
      		return;
      	}
      	if(!count || !reg.test(count)){
      		notify.show("请输入库存数量(至多10位数字)");
      		elm.value = this.skuNum;
        	hideInput();
      		return;
      	}
      	// /item/product/updateStock?productSKUId=1003729&skuCount=23
    	this.$request("/item/product/updateStock?productSKUId="+_id+"&skuCount="+count,{
          method:'GET',
          type:'json',
          onload:function(_json){
          	hideInput();
            if(_json.code==200){
              	location.reload();
            }
          },
          onerror:function(_error){
          	elm.value = this.skuNum;
        	hideInput();
          	notify.show("修改库存失败");
          }
        })
    },
    
    // 批量上架商品
    batchOnShelve:function(){
    	var _arr = this.$getCheckIds();
    	if (!_arr||_arr.length == 0) {
        	notify.show('请先选择商品');
        	return;
    	}
		//console.log('onshelve'+_arr.join(','));
    	this.$request("/item/product/batchAction?action=shelve&productSKUIds="+_arr.join(','),{
        method:'GET',
        onload:function(_json){
          if(_json.code==200){
            notify.show("上架成功！");
            //this.$emit('updatelist');
            // 直接刷新页面更新上架与未上架数量
            //location.reload();
            location.href='/item/product/list?type='+window.listType;
          }
        },
        onerror:function(_error){
        	notify.show("上架失败！");
        }
      	})
    },
    
    // 批量下架商品
    batchUnShelve:function(){
    	var _arr = this.$getCheckIds();
      	if (!_arr||_arr.length == 0) {
          	notify.show('请先选择商品');
          	return;
      	}
		
		//console.log('onshelve'+_arr.join(','));
      	var modal = new Modal({
        	data:{
          	'title':'商品下架',
          	'content':'下架后，该商品将不能出售！<br/>再次上架后可以再次出售',
          	'width':500}
      	});

     	modal.$on('confirm',function(){
        
        this.$request("/item/product/batchAction?action=unshelve&productSKUIds="+_arr.join(','),{
          	method:'GET',
          	onload:function(_json){
              if(_json.code==200){
                notify.show("下架成功！");
                //this.$emit('updatelist');
                // 直接刷新页面更新上架与未上架数量
            	//location.reload();
                location.href='/item/product/list?type='+window.listType;
              }
            },
            onerror:function(){
              notify.show('下架商品失败');
            }
        	})
        	modal.destroy();
      	}.bind(this));
      
      modal.$on('close',function(){modal.destroy();}.bind(this));
    },
    
    // 上架商品
    onShelve:function(tpl){
      this.$request("/item/product/action?action=shelve&productSKUId="+tpl.skuId,{
        method:'GET',
        //type:'json',
        onload:function(_json){
          if(_json.code==200){
            notify.show("上架成功！");
            //this.$emit('updatelist');
            // 直接刷新页面更新上架与未上架数量
           // location.reload();
            location.href='/item/product/list?type='+window.listType;
          }
        },
        onerror:function(_error){
        	notify.show("上架失败！");
        }
      })
    },
    
    // 下架商品
    unShelve:function(tpl){
      var modal = new Modal({
        data:{
          'title':'商品下架',
          'content':'下架后，该商品将不能出售！<br/>再次上架后可以再次出售',
          'width':500}
      });

      modal.$on('confirm',function(){
        this.$request("/item/product/action?action=unshelve&productSKUId="+tpl.skuId,{
          method:'GET',
          //type:'json',
          onload:function(_json){
            if(_json.code==200){
              notify.show("下架成功！");
              //this.$emit('updatelist');
              // 直接刷新页面更新上架与未上架数量
              //location.reload();
              location.href='/item/product/list?type='+window.listType;
            }
          },
          onerror:function(){
            notify.show('下架商品失败');
          }
        })
        modal.destroy();
      }.bind(this));
      
      modal.$on('close',function(){modal.destroy();}.bind(this));
    },
    
    // 预览商品
    preview: function(tpl){
      preview.preview(tpl.skuId)
    },

    // 删除商品
    remove: function(prod, index){
      var data = this.data;
      this.$request("/item/product/delete/" + prod.id, {
        onload: function(){
          notify.notify({
            type: 'success',
            message: '删除商品成功'
          });
          //data.list.splice(index,1);
          // 直接刷新页面更新上架与未上架数量
          location.reload();
        },
        onerror: function(){
          notify.notify({
            type: 'error',
            message: '删除商品失败'
          })
        }
      })
      
    },
	
	$getCheckIds:function(){
    	var items = this._getList('checked'),idList=[];
    	for(var i=0,l=items.length;i<l;i++){
    		idList.push(items[i].skuId);
    	}
    	return idList;
    },
    
    
    // 获取选中商品
    getCheckItems:function(){
    	var items = this._getList('checked'),idList=[];
    	for(var i=0,l=items.length;i<l;i++){
    		idList.push(items[i].skuId);
    	}
    	return idList;
    },
    
    // 获取上架商品
    getOnShelveItems:function(){
    	var items = this._getList('checked'),onList=[];
    	for(var i=0,l=items.length;i<l;i++){
    		if(items[i].prodStatus == 4){
    			onList.push(items[i].skuId);
    		}
    	}
    	return onList;
    },
    
    // 获取未上架商品
    getUnShelveItems:function(){
    	var items = this._getList('checked'),unList=[];
    	for(var i=0,l=items.length;i<l;i++){
    		if(items[i].prodStatus != 4){
    			unList.push(items[i].skuId);
    		}
    	}
    	return unList;
    },
    
    // 批量删除商品
    batchRemove:function(){
      var _arr = this.$getCheckIds();
      if (!_arr||_arr.length == 0) {
        notify.show('请先选择商品');
        return;
      }
      
      var unItems = this.getUnShelveItems();
      var onItems = this.getOnShelveItems();
      var type = 0;
      if(unItems.length&&onItems.length){
      	type = 1;
      }
      // 只删除未上架商品
      else if(unItems.length&&(onItems.length==0)){
      	type = 2;
      }
      // 只删除上架商品
      else if(onItems.length&&(unItems.length==0)){
      	type =3;
      }
      var delModal = new DeleteModal({
        data:{'title':'确认',
        	'width':500,
        	'type':type,
        	'unShelveItems':unItems,
        	'onShelveItems':onItems}
      });

      delModal.$on('confirm',function()
      {
      	var _arr = [];
      	delModal.$getDeleteItemIds();
      	if(delModal.data.onShelveChecked&&delModal.data.unShelveChecked){
      		_arr = this.getCheckItems();
      	}
      	else if (delModal.data.unShelveChecked&&(!delModal.data.onShelveChecked)){
      		_arr = this.getUnShelveItems();
      	}
      	else if(delModal.data.onShelveChecked&&(!delModal.data.unShelveChecked)){
      		_arr = this.getOnShelveItems();
      	}
      	
      	if(_arr.length ==0){
      		delModal.destroy();
      		return;
      	}
      	
      	this.$request("/item/product/remove",
        {
          data:_arr,
          method:'POST',
          type:'json',
          onload:function(_json)
          {
            if(_json.code==200){
              notify.show('删除成功');
              //this.$emit('updatelist');
              // 直接刷新页面更新上架与未上架数量
              location.reload();
            }
          },
          onerror:function(_error){
          	if(_error.message){
          		notify.show(_error.message)
          	}
          	else{
          		notify.show('删除失败');	
          	}
          }
        })
        
        delModal.destroy();
      }.bind(this));
      
      delModal.$on('close',function(){
      	delModal.destroy();}.bind(this)
      );
    }

  });

  var statusMap = {
          "1":"未审核",
          "2":"审核中",
          "3":"审核未通过",
          "4":"已上架",
          "5":"已下架"
      }
  SizeList.filter("statusName", function(code){
    return statusMap[code]||code;
  })

  return SizeList;

})
