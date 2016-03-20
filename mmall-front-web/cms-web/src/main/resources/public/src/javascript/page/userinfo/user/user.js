/**
 * 用户信息
 * author cheng-lin@corp.netease.com
 *
 */

NEJ.define([
  'pro/extend/util',
  'pro/widget/BaseComponent',
  'pro/components/notify/notify',
  'text!./user.html',
  'base/util'
], function(_, BaseComponent,notify, _html0,_u){
  var format = function(_time){
      if (!_time) return '';
      _time = parseInt(_time);
      return _u._$format(new Date(_time),'yyyy-MM-dd HH:mm:ss');
  };
  var regMap = {
    'mobile':/^(13|15|18)\d{9}$/i,
    'email':/^[\w-\.]+@(?:[\w-]+\.)+[a-z]{2,6}$/i
  }
  var user = BaseComponent.extend({
    url:'/user/detail',
    url2:'/user/update',
    name: 'm-user-info-user',
    template: _html0,
    config: function(data){
      _.extend(data, {
        result: {},
        showb:false,
        showmobilemodify:false,
        showemailmodify:false
      });
    },
    init: function(){
      // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
      //this.$on('updatelist', this.getList.bind(this));
      this.getList();
    },

    showBad: function(show){
        this.data.showb = show;
    },

    //获取数据
    getList: function(){
      var data = this.data;
      // console.log(data);
      this.$request(this.url, {
        data: {userId:this.data.userid},
        onload: function(json){
          data.result = json.result;
        },
        // test
        onerror: function(json){
          // @TODO: remove
        }
      });
    },

    checkvalue:function(o){
      return regMap[o.type].test(o.value);
    },

    // 更新手机号或邮箱
    update: function(type,flag){
      if (flag){
        var o = {};
            o.type = type;
            o.value= this.data.result[type];
            o.userId = this.data.userid;
        if (!this.checkvalue(o)){
          notify.showError('格式不正确');
          return;
        }
        // doupdate
        this.$request(this.url2, {
          data: o,
          onload: function(json){
            if (json.code == 200){
              this.data['show' + type + 'modify'] = false;
            } else{
              notify.showError('修改失败');
            }
          },
          // test
          onerror: function(json){
            // @TODO: remove
            notify.showError('修改失败');
          }
        });
      }else{
        // show modify
        this.data['show' + type + 'modify'] = true;
      }
    }

  }).filter('format',format);


  return user;

});