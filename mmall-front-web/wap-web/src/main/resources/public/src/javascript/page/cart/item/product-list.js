/**
 * Created by hzwuyuedong on 2014/9/22.
 */

define([
  '{pro}extend/util.js',
  '{pro}widget/BaseComponent.js',
  '{pro}components/notify/notify.js',
  'text!./product-list.html',
  'text!./product.html?v=1.0.0.1',
  '{pro}widget/ui/activities/activities.js',
  '{pro}page/cart/numcount/numcount.js?v=1.0.0.0',
  '{pro}widget/layer/sure.window/sure.window.js'
], function (_, BaseComponent, Notify, tpl, tpl0, Act,NumCount,SureWindow) {

  var i=0;
  var productListItem = BaseComponent.extend({
    urlMap: {
      'DELETE': '/cart/deleteProduct',
      'AMOUNT-CHANGE': '/cart/updateCartAmount',
      'RECOVER': '/cart/recover',
      'REBUY': '/cart/rebuy',
      'LOAD' : '/cart/listmini'
    },
    codeMap: {
      '401': '该商品已被抢光！',
      '402': '该抢购已结束！',
      '403': '购物袋最多放10款商品！',
      '404': '每款商品同尺码限购2件！'
    },
    name: 'wgt-item-product-list',
    template: tpl,
    config: function (data) {
      _.extend(data, {
        tpl: tpl0
      });
    },

    /**
     * 单个删除sku
     * @param product
     */
    onDelete: function (product) {
        var data = this.data;
    	SureWindow._$allocate({
    	  clazz:'w-win w-win-1 w-win-1-3',
    	  txt : '确定要删除该商品吗？',
    	  onok:function(){
    	      this.$request(this.urlMap['DELETE'], {
    	          data: {
    	            'ids': [product.id],
    	          },
    	          method: 'POST',
    	          onload: function (json) {
    	            this.getCartList();
    	        	  
    	          }._$bind(this),
    	          onerror: function (json) {
    	            Notify.notify(json && json.message || '刪除商品失败！', 'fail');
    	          }._$bind(this)
    	        });    		  
    	  }._$bind(this),
    	  
  	  })._$show();


    },
    getCartList : function(){
        this.$request(this.urlMap['LOAD'], {
            data: {t: +new Date()},
            onload: function (json) {
            	if(!json.result.cartStoreList){
            		json.result["empty"] = true;
            	}
            	this.$emit('change', json.result);
            }._$bind(this),
            onerror: function (json) {
              Notify.notify(json && json.message || '获取购物车数据失败，请稍后重试！', 'fail');
            }._$bind(this)
      });    	
    },
    /**
     * 修改数量
     * @param pick
     * @param sku
     */
    onNumChange: function (pick, sku) {
    	var data = this.data;
      var arr = new Array();
      arr[0] = {
      	'skuid': sku.id,
        'count': pick	  
      };
	  	//库存不足或限购
		if(pick > sku.max){
			data["diffPrice"] = (pick - sku.count) * sku.cartPrice;
	    	sku.selected = false;
			sku.count = pick;
			this.$emit('change', data);
			return;
		}
      var loadingEle = document.getElementsByClassName("cart-loading")[0];
      var left = (window.screen.width - 52)/2;
      var top = (window.screen.height - 52)/2;
      loadingEle.style.left = left + "px";
      loadingEle.style.top = top + "px";
      loadingEle.style.display = "block";
      this.$request(this.urlMap['AMOUNT-CHANGE'], {
        data: {
          'cartItemDTOs' : arr
        },
        method: 'POST',
        onload: function (json) { 
        	loadingEle.style.display = "none";
        	data["diffPrice"] = (pick - sku.count) * sku.cartPrice;
        	sku.selected = true;
        	this.$emit('change', data);
        }._$bind(this),
        onerror: function (json) {
          loadingEle.style.display = "none";
          var msg = '修改商品数量失败！';
//          Notify.notify(msg, 'fail');
          this.$emit('change', json.result);
        }._$bind(this)
      });
      sku.count = pick;
    },
    onCheck : function(e,sku){
    	//库存不足
    	if(sku.max < sku.count) return;
    	//未达到起批数量
    	if(sku.min > sku.count) return;
    	if(sku.limitConfigVO){
	    	//限购未开始
	    	if(sku.limitConfigVO.startTime > this.data.cartInfoVO.now) return;
	    	//限购已经结束
	    	if(this.data.cartInfoVO.now > sku.limitConfigVO.endTime) return;
    	}
    	sku.selected = !sku.selected;
    	this.data["cartInfoVO"]["selectAll"] = false;
    	this.$emit('change', this.data);
    },

    /**
     * 恢复删除
     * @param arg
     */
    onRecover: function (arg) {
      this.$request(this.urlMap['RECOVER'], {
        data: {'selectedIds': this.data.cartInfoVO.selectedItems},
        method: 'POST',
        onload: function (json) {
          this.$emit('change', json.result);
          //notify.notify("成功恢复你删除的商品");
        }._$bind(this),
        onerror: function (json) {
          var msg = !!(json && json.code == 401) ? '商品库存不足！'
            :this.codeMap[json && json.code] || '恢复商品失败！';
          Notify.notify(json && json.message || msg, 'fail');
          this.$emit('change', json.result);
        }._$bind(this)
      });
    },


    /**
     * 重新购买
     * @param product
     */
    onReBuy: function (product) {
      var data = this.data;
      this.$request(this.urlMap['REBUY'], {
        data: {
          'skuId': product.id,
          'selectedIds': data.cartInfoVO.selectedItems
        },
        method: 'POST',
        onload: function (json) {
          this.$emit('change', json.result);
        }._$bind(this),
        onerror: function (json) {
          var msg = this.codeMap[json && json.code] || '重新购买商品失败！';
          Notify.notify(json && json.message || msg, 'fail');
          this.$emit('change', json.result);
        }._$bind(this)
      });
    }
  });
  return productListItem;
});