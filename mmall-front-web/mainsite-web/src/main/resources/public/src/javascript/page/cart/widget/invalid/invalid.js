/**
 * Created by hzwuyuedong on 2014/9/22.
 */

define([
  '{pro}extend/util.js',
  '{pro}widget/BaseComponent.js',
  '{lib}util/animation/easeinout.js',
  'text!./invalid.html'
], function (_, BaseComponent, t, tpl) {

  var productInvalidModule = BaseComponent.extend({
    name: 'wgt-product-invalid',
    template: tpl,
    config: function (data) {
      _.extend(data, {
        statusMap: {
          'overpo': '档期过期',
          'soldout': '已售罄'
        },
        left: 0,
        current: 1,
        total: Math.ceil(data.invalidList.length / 4)
      });
    },
    prev: function () {
      var data = this.data,
        current = data.current;
      if (current > 1) {
        this.doAnimation(current, current - 1);
        data.current = current - 1;
      }
    },
    next: function () {
      var data = this.data,
        current = data.current;
      if (current < data.total) {
        this.doAnimation(current, current + 1);
        data.current = current + 1;
      }
    },

    //数值变换动画
    doAnimation: function (start, end) {
      var data = this.data,
        width = 1000;
      this.easeinout = t._$$AnimEaseInOut._$allocate({
        from: {
          offset: (start - 1) * width
        },
        to: {
          offset: (end - 1) * width
        },
        duration: 250,
        onupdate: function (event) {
          this.$update('left', -event.offset);
        }._$bind(this),
        onstop: function () {
          //数据模型真正改变之前remove slide DOM
          this.easeinout._$recycle();
        }._$bind(this)
      });
      this.easeinout._$play();
    }
  });
  return productInvalidModule;
});