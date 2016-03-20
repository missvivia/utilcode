/**
 * Created by wuyuedong on 2014/12/5.
 */

define([
    '{pro}extend/util.js',
    '{pro}extend/config.js',
    '{lib}base/element.js',
    '{pro}widget/BaseComponent.js',
    '{pro}components/notify/notify.js',
    '{pro}widget/util/scrollspy.js',
    'text!./cart.html?v=1.0.0.2',
    '{pro}page/cart/item/product-list.js?v=1.0.0.1',
    'util/chain/NodeList'
  ],
  function (_, config, e, BaseComponent, Notify, Scrollspy, tpl, o,$, f, r, pro) {
    var cartModule = BaseComponent.extend({
      urlMap: {
        'LOAD': '/cart/listmini',
        'SUBMIT': '/purchase/index'  
      },
      template: tpl,
      config: function (data) {
        _.extend(data, {
          "firstLoad": true,
          "form": {},
          "cartInfoVO": {
            "totalPrice": 0.00,
            "payAble": true,
            "selectAll": false,
            "selectedTotal" : 0,
            "selectedItems" : [],
            "now" : new Date().getTime()
          },
          "empty" : false,
        });
      },
      init: function (data) {
        this.$on('updatewgt', this.fetch._$bind(this));
      },

      //根据内部componet返回的数据刷新cart
      refresh: function (result) {
        var data = this.data,
          totalCount = 0,
          selected = [],
          selectedPrice = [],
          selectedTotal = 0,
          selectedStoreCount = 0,
          _self = this;
          data.cartInfoVO.payAble = true;

        var makeData = function (list) {
          for (var i = list.length - 1, l = 0; i >= l; i--) {
            var tmp = list[i],
            	storeSkuCount = 0;
            tmp["totalPrice"] = 0;
            for (var m = 0, n = tmp.skulist.length; m < n; m++) {
              // 不包含已过期和删除的sku， 传给下单页的时，只需要有效的skuid
              var sku = tmp.skulist[m];
              if(sku.selected && !sku.deleted){
            	totalCount += sku.count;
                storeSkuCount += 1;
                selected.push(sku.id);
                selectedPrice.push(sku.id + "|" + sku.cartPrice);
                selectedTotal += sku.count * sku.cartPrice;
                tmp["totalPrice"] += sku.count * sku.cartPrice;
              }
              if(result.productMap){
            	  sku.min = result.productMap[sku.id][0]["minNumber"];
              }
              if(sku.limitConfigVO){
            	  sku.max = sku.limitConfigVO.allowBuyNum;
              }else{
            	  sku.max = sku.inventroyCount;
              }
            }
            if(storeSkuCount == 0){
            	tmp["selected"] = false;
            }else if(storeSkuCount == tmp.skulist.length){
            	tmp["selected"] = true;
            	selectedStoreCount += 1;
            }else{
            	tmp["selected"] = "indeterminate";//店铺商品全部选中或部分选中时才可以判断是否满足起批金额
            }
            //起批金额不足时结算按钮disable
            if(tmp.selected){
	            if(data.cartInfoVO.payAble){
	            	data.cartInfoVO.payAble = (tmp.storeBatchCash > tmp.totalPrice) ? false : true;
	            }
            }
          }
        };

        // 更新数据
        if(result.empty){
        	data.cartStoreList = null;
        }else{
	        if(result.cartStoreList){
	        	data.cartStoreList = result.cartStoreList;
	        }
        }
        if(data.cartStoreList){
        	makeData(data.cartStoreList);
        }
        if(result.cartInfoVO){
	        for(var i in data.cartInfoVO){
	        	if(result.cartInfoVO[i]){
	        		data.cartInfoVO[i] = result.cartInfoVO[i];
	        	}
	        }
        }


        this.skusPrice = selectedPrice;
        data.cartInfoVO["selectedItems"] = selected || '';
        data.cartInfoVO["selectedTotal"] = selectedTotal + (data.diffPrice || 0);
        if(data.cartInfoVO["selectedItems"] == ""){
        	data.cartInfoVO.payAble = false;
        }
        this.$update();
        this.position();
      },
      inArray : function(item,list){
    	  if(list.length == 0){
    		  return false;
    	  }
    	  var str = list.join(",");
    	  if(str.indexOf(item) > -1){
    		  return true;
    	  }else{
    		  return false;
    	  }
      },
      //获取数据
      fetch: function () {
        var data = this.data;
        this.$request(this.urlMap['LOAD'], {
          data: {t: +new Date()},
          onload: function (json) {
            //判断是否是第一次fetch数据
            data.firstLoad = false;           
            this.refresh(json.result);
            this.onAllCheck();
          }._$bind(this),
          onerror: function (json) {
            Notify.notify(json && json.message || '获取购物车数据失败，请稍后重试！', 'fail');
          }._$bind(this)
        });
      },


      //去支付
      pay: function () {
        var data = this.data,
        	form = e._$get('postForm');
        if(!this.data.cartInfoVO.payAble){
        	return;
        }

    	this.$update('form', {
            'cartIds': this.data.cartInfoVO["selectedItems"].join(','),
            'skusPrice': this.skusPrice.join(',')
        });
        form.submit();
      },

      //判断结算栏位置
      position: function(){
        var _target = e._$get('float-bar');
        if(!_target){
          return false;
        }
        if(!this.__position){
          this.__position = Scrollspy._$$ScrollSpy._$allocate({
            target: _target
          });
        }else{
          this.__position._$updatewgt();
        }
      },
      onAllCheck : function(e){
    	  var list = this.data.cartStoreList;
    	  for (var i = list.length - 1, l = 0; i >= l; i--) {
              var item = list[i];
              for (var m = 0, n = item.skulist.length; m < n; m++) {
                var sku = item.skulist[m];
    			if(sku.max < sku.count || sku.offline || sku.count < sku.min){//库存不足或限购或商品下架或未达到起批数量
    				sku.selected = false;
    			}else{
    				sku.selected = !this.data.cartInfoVO["selectAll"];
    			}
              }
            }  
    	  this.data["cartInfoVO"]["selectAll"] = !this.data.cartInfoVO["selectAll"];
    	  this.refresh(this.data);
      },
      onStoreCheck : function(e,item){
    	for(var i = 0; i < item.skulist.length;i++){
    		var sku = item.skulist[i];
			if(sku.max < sku.count || sku.offline || sku.count < sku.min){//库存不足或限购或商品下架或未达到起批数量
				sku.selected = false;
			}else{
				sku.selected = !item.selected;
			}
    	}
    	this.refresh(this.data); 
      },
      onConditionClick : function(e){
    	  var mAct = "";
    	  if(e.target.className == ""){
    		  mAct = $(e.target)._$parent();
    	  }else{
    		  mAct = $(e.target);
    	  }
    	  if(mAct[0].className.indexOf("show") > -1){
    		  mAct._$next()._$style("display","none");
    		  mAct[0].className = "m-act";
    	  }else{
    		  mAct._$next()._$style("display","block");
    		  mAct[0].className = "m-act show"; 
    	  }
      }
    });
    return cartModule;
  });