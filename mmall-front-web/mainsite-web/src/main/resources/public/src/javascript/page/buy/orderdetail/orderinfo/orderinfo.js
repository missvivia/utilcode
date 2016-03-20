/**
 * Created by wuyuedong on 2014/10/10.
 */

NEJ.define([
  '{lib}base/util.js',
  '{pro}extend/util.js',
  '{pro}widget/BaseComponent.js',
  'text!./orderinfo.html'
],function(u, _, BaseComponent, tpl, _p, _o, _f, _r) {
  var OrderInfo = BaseComponent.extend({
    template: tpl,
    name: 'wgt-orderinfo',
    config: function (data) {
      _.extend(data, {
        'yhMap': ['免邮']
      });
    },
    init: function () {
      this.$on('updatewgt', this.refresh._$bind(this));
    },

    refresh: function(){
      this.$update();
    }

  });

  return OrderInfo;
})