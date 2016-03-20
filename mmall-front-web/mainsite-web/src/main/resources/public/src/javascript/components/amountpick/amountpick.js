/**
 * Created by hzwuyuedong on 2014/9/22.
 */

define([
  '{pro}extend/util.js',
  '{pro}widget/BaseComponent.js',
  '{lib}util/animation/easeinout.js',
  'text!./amountpick.html'
], function (_, BaseComponent, t, tpl) {
  var amount = BaseComponent.extend({
    name: 'amountpick',
    template: tpl,
    config: function (data) {
      _.extend(data, {
        amount: data.amount!=undefined ? parseInt(data.amount, 10): 1,
        oldValue: data.amount!=undefined ? parseInt(data.amount, 10): 1,
        maxamount: data.maxamount!=undefined ? parseInt(data.maxamount, 10) : 2,
        offsetTop: -30,
        animationEnd: true
      }, true);
    },
    init: function (data) {
      // 证明不是内嵌组件
      //if(this.$root == this) this.$inject(document.body);
      this._check();
    },

    onMinus: function (event) {
      var data = this.data;
      if (!data.noMinus) {
        this._doAnimation('-');
      }
    },

    onPlus: function (event) {
      var data = this.data;
      if (!data.noPlus) {
        this._doAnimation('+');
      }else{
        this._checkMsg();
      }
    },

    //keyup事件绑定
    onAmountChange: function (event) {
      var data = this.data,
        value = parseInt(data.amount, 10);
      value = !!value && (value > 0) ? value : 1;
      if(value > data.maxamount) {
        this._checkMsg();
        value = data.maxamount;
      };
      this._doAmountChange(value);
    },

    //判断-+按钮是否可点
    _check: function () {
      var data = this.data;
      data.noMinus = !!(data.amount <= 1)? true: false;
      data.noPlus = !!(data.amount >= data.maxamount)? true: false;
    },

    _checkMsg: function(){
      var data = this.data,
        msg = !!(data.maxamount == 1)? '仅剩1件': '每款商品同尺码限购'+ data.maxamount +'件';
      this.$emit('over', {
        'msg': msg
      })
    },

    //修改model的值，并且触发回调
    _doAmountChange: function (value) {
      var data = this.data;
      var maxamount=this.data.maxamount;
      this.$update('amount', value);
      this.$update("maxamount",maxamount);
      data.newValue = value;
      this._check();
      this.$emit('change', {
        'oldValue': data.oldValue,
        'newValue': data.newValue
      });
      data.oldValue = value;
    },

    //数值变换动画
    _doAnimation: function (type) {
      var data = this.data,
        start = -30, end, amount;
      data.animationEnd = false;
      switch (type) {
        case '-':
          end = 0;
          amount = data.amount - 1;
          break;
        case '+':
          end = -60;
          amount = data.amount + 1;
          break;
      }
      this.easeinout = t._$$AnimEaseInOut._$allocate({
        from: {
          offset: start
        },
        to: {
          offset: end
        },
        duration: 300,
        onupdate: function (event) {
          this.$update('offsetTop', event.offset);
        }._$bind(this),
        onstop: function () {
          //数据模型真正改变之前remove slide DOM
          this.$update('animationEnd', true);
          this._doAmountChange(amount);
          this.easeinout._$recycle();
        }._$bind(this)
      });
      this.easeinout._$play();
    }
  });
  return amount;
});