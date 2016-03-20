/**
 * 活动列表筛选
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  'base/util',
  'pro/widget/util/preview',
  'pro/extend/config',
  'pro/components/notify/notify',
  'text!./list.html',
  '{pro}components/ListComponent.js'
  ], function(_u, preview, config, notify, tpl, ListComponent){

  var REST_URL = '/rest/product/'
  var SizeList = ListComponent.extend({
    url: '/rest/product/searchProduct',
    api:'/rest/product/remove',
    template: tpl,
    data: {
      config: config,
      limit: 50
    },
    config: function(data){
      this.supr(data)
    },
    xdrOption:function(){
      return { method:'POST' }
    },
    // 预览商品
    preview: function(tpl){
      preview.preview(tpl.id)
    },
    // @子类修改
    remove: function(prod, index){
      var data = this.data;
      this.$request(REST_URL + prod.id, {
        method:'delete',
        onload: function(){
          notify.notify({
            type: 'success',
            message: '删除商品成功'
          });
          data.list.splice(index,1);
        },
        onerror: function(){
          notify.notify({
            type: 'error',
            message: '删除商品失败'
          })
        }
      })
      
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
                _u._$forEach(
                    _data.list,function(_it){
                        _it.checked = _sign;
                    }
                );
            }
        }
    },
    $getCheckIds:function(){
    	var items = this._getList('checked'),idList=[];
    	for(var i=0,l=items.length;i<l;i++){
    		idList.push(items[i].id);
    	}
    	return idList;
    },
    batchRemove:function(){
    	var _list = this._getList('checked')||_r;
        if (!_list||!_list.length){
            notify.showError('请先选择要操作的项');
            return;
        }
        var _arr = [];
        _u._$forEach(
            _list,function(_it){
                _arr.push(_it.id);
            }
        );
        this.$request(this.api,{
        	data:_arr,
        	method:'POST',
        	type:'json',
        	onload:function(_json){
        		if(_json.code==200){
        			notify.show('删除成功');
        			this.$emit('updatelist');
        		}
        	},
        	onerror:function(){}
        })
    }
  });
  return SizeList;

})
