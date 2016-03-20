/**
 * Created by hzwuyuedong on 2014/9/22.
 */

define([
    '{lib}base/element.js',
    '{pro}extend/util.js',
    '{pro}widget/BaseComponent.js',
    'pro/components/notify/notify',
    'text!./kefu.html',
    'pro/widget/util/datanotify',
    'pro/components/remind/remind',
    '{pro}/components/countdown/countdown.js'
  ],
  function(_e, _, BaseComponent, notify, tpl, _t, p, o, f, r, pro) {
    var kefuModule = BaseComponent.extend({
      url: '/ex/hasNewMessage',
      template: tpl,
      config: function (data) {
        data.hasNewMsg = false;
      },
      init: function (data) {
        this.$on('updatewgt', this.refresh);
        this.$emit('updatewgt');
      },
      refresh: function(){
        var data = this.data;
        this.$request(this.url, {
          onload: function(json){
            data.hasNewMsg = json.result
          }._$bind(this)
        });
      },
      openKefuWin: function(event){
        if(event == 2) this.data.hasNewMsg = false;
        _._$openKefuWin(event);
      }
    });
    return kefuModule;
  });