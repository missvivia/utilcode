/**
 * 拣货单的列表组件
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define([
  "{pro}extend/util.js",
  "text!./pickList.html",
  "{pro}widget/BaseComponent.js",
  "{pro}widget/layer/sure.window/sure.window.js",
	"{pro}components/notify/notify.js",
	"{pro}components/jit/pick/invoiceModal.js",
  "{pro}components/pager/pager.js"
  ], function(_,tpl, BaseComponent,SureWin,notify,InvoiceModal){
	
	var PickList = BaseComponent.extend({
		url: "/jit/getPKList.json",
	    template: tpl,
	    watchedAttr: ['current'],
	    config: function(data){
	      _.extend(data, {
	        total: 1,
	        current: 1,
	        limit: 5,
	        list: [],
	        currentlist:[]
	      });
	      this.$watch(this.watchedAttr, function(){
	        if(this.shouldUpdateList()) this.data.currentlist = this.getCurrents();
	      })
	    },
	    init: function(){
	      if(!this.url) throw "ListModule未指定url";

	      // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
	      this.__getList();
	      this.$update();

	    },
	    onExport:function(item,index1,index2){
	    	this.$request('/jit/addexportone.json', {
	    		method:'get',
	            data:{id:item.pickOrderId} ,
	            onload: function(json){
	            	 var _index = index1+(this.data.current-1)*this.data.limit;
	            	 (this.data.list[_index]).pickList[index2].exportTimes+=1;
	            	 (this.data.list[_index]).pickList[index2].firstExportTime = json.data.firstExportTime;
	            	 this.data.currentlist = this.getCurrents();
	            	 this.$update();
	            }._$bind(this),
	            // test
	            onerror: function(json){
	              // @TODO: remove
	            }
	        });
	    },
		confirmPick:function(pkId){
			SureWin._$allocate({text:'确定仓库已发货？',title:'确认',onok:function(){
				this.$request('/jit/pkUpdate/'+pkId,{
					type:'json',
					onload:function(_json){
						if(_json.code==200){
							notify.show('操作成功');
							this.__getList();
						}else{
							notify.show('操作失败');
						}
					},onerror:function(){
						notify.show('操作失败');
					}
				})
			}._$bind(this)})._$show();
		},
	    checkAndGetResult:function(item){
	    	var _data = [],
	    		_totalNum = 0,
	    		_pickOrderIds =[];
	    	for(var i=0;i<item.pickList.length;i++){
	    		if(!!item.pickList[i].selected){
	    			_data.push(item.pickList[i]);
	    		}
	    	}
	    	if(_data.length<1){
	    		return false;
	    	}else{
	    		for(var i=0;i<_data.length;i++){
	    			_totalNum+=_data[i].pickTotalQuantity;
	    			_pickOrderIds.push(_data[i].pickOrderId);
		    	}
	    		return {
	    			totalNum:_totalNum,
	    			pickOrderIdStr:_pickOrderIds.join(','),
	    			poId:_data[0].poOrderId
	    		};
	    	}
	    	
	    },
	    onConfirm:function(item){
	    	var _data = this.checkAndGetResult(item);
	    	if(!_data){return;}
	    	var modal = new InvoiceModal({
	    		data:_data
	    	});
	    	modal.$on('confirm', function (_data) {
	    		this.refresh();
	    	}._$bind(this));
	    },
	    shouldUpdateList: function(){
	      return (this.data.list.length<1?false:true);
	    },
	    getExtraParam:function(){
	      return this.data.condition;
	    },
	    refresh:function(_data){
	      this.data.condition = _data||this.data.condition;
	      this.__getList();
	      this.data.current = 1;
	    },
	    getCurrents: function(){
	      var lists = this.data.list;
	      return lists.slice((this.data.current-1) * this.data.limit, this.data.current * this.data.limit); 
	    },
	    // update loading
	    __getList: function(){
	      this.$request(this.url, {
	    	 method:'post',
	        data: this.getExtraParam(),
	        onload: function(json){
	          var result = json.result,
	            list = result.list||result;

	          this.data.total = result.total;
	          this.data.list = list;
	          this.data.currentlist = this.getCurrents();

	        }._$bind(this),
	        // test
	        onerror: function(json){
	          // @TODO: remove
	        }
	      })
	    }
	});
    return PickList;

});