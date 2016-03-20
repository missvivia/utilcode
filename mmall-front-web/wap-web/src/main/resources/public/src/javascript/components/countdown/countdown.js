/**
 *
 * 倒计时组件
 * author cheng-lin(cheng-lin@corp.netease.com)
 *
 */

NEJ.define([
  'base/util',
  'pro/extend/util',
  'pro/widget/BaseComponent',
  'pro/widget/util/countdown',
  'text!./countdown.html'
], function (_u,_, BaseComponent, t, tpl) {

  var countdown = BaseComponent.extend({
    name: 'm-countdown',
    template: tpl,
    config: function (data) {
      _.extend(data, {
        dd:'--',
        HH:'--',
        mm:'--',
        ss:'--',
        updatetime:1000,
        content:'<p><span>{{dd}}天</span><span>{{HH}}小时</span><span>{{mm}}分钟</span><span>{{ss}}秒</span></p>'
      });
    },
    init: function (data) {
      this.$watch('time', this.dorefresh._$bind(this));
      this.$update();
    },

    dorefresh: function(newValue, oldValue){
      var data = this.data;
      if(!!this.__key){
        t._$clearCountdown(this.__key);
      }
      this.__key = t._$countdown(0,data.time||0,{'format':data.content,'updatetime':data.updatetime,'onchange':this.dochange._$bind(this)});
    },

    dochange: function(_options){
        _u._$merge(this.data,_options.meta);
        this.$update();
        if (this.data.onchange){
          this.data.onchange.apply(null,arguments);
        }
        this.$emit('onchange',_options);

    },
    destroy:function(){
      t._$clearCountdown(this.__key);
      this.supr();
    }
  });
  return countdown;
});

