/**
 * 档期详情列表
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  'base/util',
  'text!./addtable2.html',
  'pro/widget/util/preview',
  'pro/components/ListComponent',
  'pro/widget/layer/schedule.add2',
  'pro/extend/request',
  'pro/extend/util',
  "pro/widget/layer/sure.window/sure.window"
  ], function(_u,tpl, preview, ListComponent,ExpressWin,Request,_,SureWindow){
  var ActList = ListComponent.extend({
    url: '/rest/schedule/search',
    name: 'm-tablelist',
    template: tpl,
    // @子类修改
    xdrOption:function(){
      return {method:'POST'}
    },
    config: function(data){
        _.extend(data, {
          skuTotal: 0,
          supplyMode:0,
          limit: 50
        });
        this.supr(data);
      },
    $updateStatus:function(_status){
    	this.data.status = _status;
    	this.$update();
    },
    showWin: function(num){
      if(!!this.addexpressWin){
          this.addexpressWin._$recycle();
      }
      this.addexpressWin = ExpressWin._$allocate({parent:document.body,data:data,onok:this.onAddOK._$bind(this)})._$show();
    },
    __getList: function(){
        var data = this.data;
        var option = {
          progress: true,
          data: this.getListParam(),
          onload: function(json){
            var result = json.result,
              list = result.list;
            _.mergeList(list, data.list,data.key||'id')

            data.total = result.total;
            data.skuTotal = result.skuTotal;
            data.supplyMode = result.scheduleType;
            data.list = list;
          },
          onerror: function(json){
            // @TODO: remove
          }
        };
        //继承类提供xdrOption方法，用来表明请求类型
        /**
         * function(){
         *  return {method:'POST',norest:true}
         & }
        **/
        if(this.xdrOption){
          var xdrOpt = this.xdrOption();
          if(xdrOpt.norest){
            option.data = _ut._$object2query(this.getListParam());
            option.norest = true;
          } 

          option.method = xdrOpt.method||'GET';
        
        } 
        this.$request(this.url,option)
      },
    preview: function(tpl){
      preview.preview(tpl.id,{
        url: "/schedule/product/preview",
        data: {scheduleId: this.data.condition.poId}
      })
    },
    onAddOK: function(){
      console.log('add');
    },
    remove: function(index){
      this.data.list.splice(index,1)
      this.$emit('updatelist');
    },
    canceladd: function(item,_index){
      var listItem = {};

      var data = [],
          dataItem = {},
          skuList = [];

      skuList.push(item.skuList[_index].skuId);
      dataItem['productId'] = item.id;
      dataItem['skuList'] = skuList;
      data.push(dataItem);

      listItem['poId'] = this.data.poId;
      listItem['data'] = data;
      
      Request('/rest/schedule/deleteProduct',{
        data: listItem,
        method: 'POST',
        type: 'JSON',
        onload:this.canceladdSeccess._$bind(this,item,_index),
        onerror:function(e){
          console.log(e);
        }
      })
    },
    canceladdSeccess:function(item,_index,json){
      item.skuList.splice(_index,1);
      this.data.skuTotal -= 1;
      this.$emit('updatelist');
    },
    batchCancel:function(_list,_poId){
		var listItem ,_prdList =[];
	    _u._$forEach(
	    		_list,function(_it){
	        	var _skuData=[],_skuList=[];
	            for(var i=0,l=_it.skuList.length;i<l;i++){
	            	_skuList.push(_it.skuList[i].skuId)
	            }
	            if(_skuList.length){
	            	_prdList.push({productId:_it.id,skuList:_skuList});
	            }
	        }
	    );
	    if(_prdList.length){
	        SureWindow._$allocate({text:'确认取消',title:'取消','onok':function(){
	        	listItem = {poId:_poId,data:_prdList};
		        
		        //send batch request
		        this.$request('/rest/schedule/deleteProduct',{
		            data:listItem,
		            method:'POST',
		            type:'JSON',
		            onload:function(_result){
		            	if(_result.code==200){
		            		_u._$forEach(
		                    		_list,function(_it){
		                            for(var i=_it.skuList.length-1;i>=0;i--){
		                            	_it.skuList.splice(i,1);
		                            }
		                        }
		                    );
		                this.$emit('updatelist');
		            	}
		            }._$bind(this),
		            onerror:function(e){
		              console.log(e);
		            }
		          })
	        }._$bind(this)})._$show()
	    }
    }
  });
  ActList.filter('statusMap',function(_status){
	  var _map ={'1':'待提交','2':'审核中','3':'审核通过','4':'审核未通过'}
	  return _map['status']||('未知状态'+_status)
  })
  return ActList;

})