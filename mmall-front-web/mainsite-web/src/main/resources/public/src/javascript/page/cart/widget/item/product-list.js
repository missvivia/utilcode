/**
 * Created by hzwuyuedong on 2014/9/22.
 */

define([
  '{pro}extend/util.js',
  '{pro}widget/BaseComponent.js',
  '{pro}components/amountpick/amountpick.js',
  '{pro}/components/notify/notify.js',
  'text!./product-list.html',
  'text!./product.html'
], function (_, BaseComponent, AmountPick, notify, tpl, tpl0) {

  var productListItem = BaseComponent.extend({
    urlMap: {
      'DELETE': '/cart/delete',
      'AMOUNT-CHANGE': '/cart/updateamount',
      'RECOVER': '/cart/recover',
      'SELECT': '/cart/select',
      'REBUY': '/cart/rebuy'
    },
    codeMap: {
      '401': '<div class="f-fs20">抱歉，商品已售罄！</div><div class="f-fs1">真可惜，下次赶早哦</div>',
      '402': '<div class="f-fs20">抱歉，该活动已过期！</div><div class="f-fs1">真可惜，你来迟了，下次赶早哦</div>',
      '403': '<div class="f-fs20">抱歉，购物袋已满10款商品！</div><div class="f-fs1">请先提交订单或清空再购买新的商品</div>',
      '404': '抱歉，每款商品同尺码限购2件！'
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
      this.$request(this.urlMap['DELETE'], {
        data: {
          'ids': [product.id],
          'selectedIds': _._$grepArray(data.cartInfoVO.selectedItems, [product.id])
        },
        method: 'POST',
        onload: function (json) {
          this.$emit('change', json.result);
        }._$bind(this),
        onerror: function (json) {
          this.$emit('error', '刪除商品失败，请稍后重试！');
        }._$bind(this)
      });
    },

    /**
     * 修改数量
     * @param pick
     * @param sku
     */
    onAmountChange: function (pick, sku) {
      var data = this.data;
      sku.count = pick.newValue;
      //sku.cartPrice = pick.newValue * sku.retailPrice;
      this.$request(this.urlMap['AMOUNT-CHANGE'], {
        data: {
          'skuId': sku.id,
          'diff': pick.newValue - pick.oldValue,
          'selectedIds': data.cartInfoVO.selectedItems
        },
        method: 'POST',
        onload: function (json) {
          this.$emit('change', json.result);
        }._$bind(this),
        onerror: function (json) {
          var msg = this.codeMap[json && json.code] || '修改商品数量失败，请稍后重试！';
          //先emit event error，emit event change之后，组建会重新实例化
          this.$emit('error', msg);
          this.$emit('change', json.result);
        }._$bind(this)
      });
    },

    /**
     * 选择整个po或activity
     * @param item
     */
    onSelect: function (item) {
      var data = this.data, tmp, sku,
        ids = [];
      //获取当前节点下的所有ids
      if (item.type == 'active') {
        for (var i = 0, l = item.poList.length; i < l; i++) {
          tmp = item.poList[i];
          for (var m = 0, n = tmp.skulist.length; m < n; m++) {
            sku = tmp.skulist[m];
            !sku.overTime && !sku.deleted&& ids.push(sku.id);
          }
        }
      } else if (item.type == 'po') {
        for (var i = 0, l = item.skulist.length; i < l; i++) {
          sku = item.skulist[i];
          !sku.overTime && !sku.deleted && ids.push(sku.id);
        }
      }
      this.$request(this.urlMap['SELECT'], {
        data: {
          'selectedIds': !!item.selected ? _._$grepArray(data.cartInfoVO.selectedItems, ids)
                                       : data.cartInfoVO.selectedItems.concat(ids)
        },
        method: 'POST',
        onload: function (json) {
          this.$emit('change', json.result);
        }._$bind(this),
        onerror: function (json) {
          this.$emit('error', '选中商品失败，请稍后重试！');
        }._$bind(this)
      });
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
          var msg = !!(json && json.code == 401) ? '<div class="f-fs20">抱歉，商品库存不足！</div><div class="f-fs1">真可惜，下次赶早哦</div>'
            :this.codeMap[json && json.code] || '恢复商品失败，请稍后重试！';
          this.$emit('error', msg);
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
          var msg = this.codeMap[json && json.code] || '重新购买商品失败，请稍后重试！';
          this.$emit('error', msg);
          this.$emit('change', json.result);
        }._$bind(this)
      });
    },

    /**
     * 数量不符合提示
     * @param data
     * @param sku
     */
    showMsg: function (data, sku) {
      var time = function () {
        sku.msg = null;
        this.$update();
      };
      sku.msg = data.msg;
      window.setTimeout(time._$bind(this), 5000);
    }

  });
  return productListItem;
});