/**
 * Created by hzwuyuedong on 2014/9/22.
 */

define([
    '{pro}extend/util.js',
    '{lib}base/element.js',
    '{pro}widget/BaseComponent.js',
    '{pro}page/cart/widget/item/product-list.js',
    '{pro}page/cart/widget/invalid/invalid.js',
    '{pro}widget/layer/alert/alert.js',
    '{pro}widget/util/scrollspy.js',
    '{pro}widget/util/datanotify.js',
    '{pro}/components/notify/notify.js',
    'text!./cart.html',
    '{pro}/components/countdown/countdown.js'
  ],
  function (_, e, BaseComponent, item, module, Alert, Scrollspy, t, notify, tpl, p, o, f, r, pro) {
    var cartModule = BaseComponent.extend({
      urlMap: {
        'LOAD': '/cart/list',
        'SELECT': '/cart/select',
        'DELETE': '/cart/delete',
        'SUBMIT': '/cart/resettime'
      },
      template: tpl,
      config: function (data) {
        _.extend(data, {
          "firstLoad": true,
          "form": {},
          "format": '<span class="s-fc5 f-fl f-fw1">{{mm}}分{{ss}}秒</span>',
          "cartInfoVO": {
            "leftTime": 0,
            "totalPrice": 0.00,
            "payAble": false,
            "selectALl": false
          }
        });
      },
      init: function (data) {
        this.$on('updatewgt', this.fetch._$bind(this));
        //this.$emit('updatewgt');
      },

      //全选
      onSelectALL: function () {
        var data = this.data,
          flag = data.cartInfoVO.selectALl;
        this.$request(this.urlMap['SELECT'], {
          data: {'selectedIds': !!flag ? [] : data.cartInfoVO.totalItems},
          method: 'POST',
          onload: function (json) {
            this.refresh(json.result);
          }._$bind(this),
          onerror: function (json) {
            this.error('全选商品失败！');
          }._$bind(this)
        });
      },

      //批量删除
      onDelete: function () {
        var data = this.data;
        this.$request(this.urlMap['DELETE'], {
          data: {'ids': data.cartInfoVO.selectedItems, 'selectedIds': []},
          method: 'POST',
          onload: function (json) {
            this.refresh(json.result);
          }._$bind(this),
          onerror: function (json) {
            this.error('删除商品失败！');
          }._$bind(this)
        });
      },

      //更具内部componet返回的数据刷新cart
      refresh: function (result) {
        var box = e._$get('cart-body'),
          data = this.data;
        _.transition(box, 'height', 'auto');
        _.extend(data, result, true);
        if(data.cartInfoVO.leftTime < 0){
          data.cartInfoVO.leftTime = 0;
        }
        this.check();
        this.$update();
        this.position();
        _.transition(box, 'height', 'auto');
      },

      //判断结算栏位置
      position: function(){
        var _target = e._$get('accountbar');
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
      //check数据
      check: function () {
        var data = this.data,
          select_flag = true,
          deleteFlag = false,
          totalSelectCount = 0,
          total = [],
          seleted = [],
          deleted = [];

        var makeData = function (list, isParent) {
          for (var i = list.length - 1, l = 0; i >= l; i--) {
            var tmp = list[i];
            if (isParent) {
              if (!tmp.selected) {
                select_flag = false;
              }
              //删除多个活动的时候，在最后一个位置提示
              if (tmp.deleted && deleteFlag == false) {
                deleteFlag = !deleteFlag;
                tmp.deleteFlag = deleteFlag;
              }
            }
            for (var m = 0, n = tmp.skulist.length; m < n; m++) {
              //不包含已过期和删除的sku， 传给下单页的时，只需要有效的skuid
              var sku = tmp.skulist[m];
              if(sku.selected && !sku.overTime && !sku.deleted){
                seleted.push(sku.id);
                totalSelectCount += sku.count;
              }
              sku.deleted && deleted.push(sku.id);
              !sku.deleted && !sku.overTime && total.push(sku.id);
            }
          }
        };

        //遍历poList
        if(!!data.poList){
          makeData(data.poList, true);
        }

        //遍历activations
        if(!!data.activations){
          for (var i = data.activations.length - 1, l = 0; i >= l; i--) {
            var tmp = data.activations[i];
            if (!tmp.selected) {
              select_flag = false;
            }
            //删除多个活动的时候，在最后一个位置提示
            if (tmp.deleted && deleteFlag == false) {
              deleteFlag = !deleteFlag;
              tmp.deleteFlag = deleteFlag;
            }
            makeData(tmp.poList, false);
          }
        }
        data.cartInfoVO.payAble = seleted.length>0 ? true : false;
        data.cartInfoVO.selectALl = select_flag;
        data.cartInfoVO.totalItems = total;
        data.cartInfoVO.selectedItems = seleted;
        data.cartInfoVO.deletedItems = deleted;
        //用datanotify通知
        t._$refreshCart({'cartCount': totalSelectCount});
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
          }._$bind(this),
          onerror: function (json) {
            this.error('获取购物车数据失败，请稍后重试！');
          }._$bind(this)
        });
      },

      //倒计时
      onCountDown: function (time) {
        var data = this.data;
        if (!!time.isdown && data.cartInfoVO.leftTime != 0) {
          data.cartInfoVO.leftTime = 0;
          this.$emit('updatewgt');
        }
      },

      //去支付
      pay: function () {
        var data = this.data,
          form = e._$get('postForm');
        if (data.cartInfoVO.payAble) {
          this.$request(this.urlMap['SUBMIT'], {
            data: {t: +new Date()},
            onload: function (json) {
              this.$update('form', {
                'cartIds': data.cartInfoVO.selectedItems.join(',')||'',
                'cartEndTime': json.result
              });
              form.submit();
            }._$bind(this),
            onerror: function (json) {
              this.error('提交购物车数据失败，请稍后重试！');
            }._$bind(this)
          });
        }
      },

      error: function (msg) {
        Alert._$$AlertWindow._$allocate({
          text: msg
        })._$show();
      }
    });
    return cartModule;
  });