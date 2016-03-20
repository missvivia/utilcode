/*
 * @author   hzwuyuedong(hzwuyuedong@corp.netease.com)
 */
NEJ.define([
  'base/util',
  'text!./address.html',
  'pro/components/notify/notify',
  'pro/widget/BaseComponent',
  'pro/extend/util',
  'pro/widget/ui/address/address',
  'pro/components/address/address',
  '{pro}components/notify/notify.js'
], function (_ut, _html, notify, BaseComponent, _, AddressWin, AddressList, Notify, _p, _o, _f, _r) {

  // change事件，组建内容发生变化的时候emit
  // updatelist 重新获取全部地址
  return BaseComponent.extend({
    template: _html,
    config: function (data) {
      this.supr(data);
      _.extend(data, {
        address: {}
      })
    },
    showSelectAddress:function(){
    	this.$emit('select');
    }
  });
});