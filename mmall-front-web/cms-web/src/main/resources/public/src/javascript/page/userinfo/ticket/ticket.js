/*
 * ------------------------------------------
 * 优惠券列表
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'pro/extend/util',
    'base/util',
    'pro/widget/BaseComponent',
    'text!./ticket.html'
],function(_,_u,BaseComponent,_html0){

    var format = function(_time){
      if (!_time) return '';
      _time = parseInt(_time);
      return _u._$format(new Date(_time),'yyyy.MM.dd');
  };
    var ticket = BaseComponent.extend({
        url:'/user/coupon',
        name: 'm-user-info-ticket',
        template: _html0,
        config: function(data){
          _.extend(data, {
            currentState:'active',
            result: {}
          });
        },
        init: function(){
          // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
          //this.$on('updatelist', this.getList.bind(this));
          this.getList();
        },

        setCurrentState : function(state){
          this.data.currentState = state;
          this.data.list = this.getListByState();
          // this.getList(state);
        },

        getListByState: function(state){
          var list = this.data.result.list,
              group = [],
              state = this.data.currentState;
          if (state == 'active'){
            _u._$forEach(list,function(t){
              if (t.couponState==0){
                group.push(t);
              }
            }._$bind(this))
          }else if(state == 'over'){
            _u._$forEach(list,function(t){
              if (t.couponState==3){
                group.push(t);
              }
            }._$bind(this))
          }else if(state == 'past'){
            _u._$forEach(list,function(t){
              if (t.couponState==2){
                group.push(t);
              }
            }._$bind(this))
          }else if(state == 'lose'){
            _u._$forEach(list,function(t){
              if (t.couponState==5){
                group.push(t);
              }
            }._$bind(this))
          }else if(state == 'inactive'){
            _u._$forEach(list,function(t){
              if (t.couponState==8){
                group.push(t);
              }
            }._$bind(this))
          }
          return group;
        },

        //获取数据
        getList: function(){
          var data = this.data;
          console.log(data);
          this.$request(this.url, {
            data: {userId:this.data.userid},
            onload: function(json){
              data.result = json.result;
              data.list = this.getListByState();
            }._$bind(this),
            // test
            onerror: function(json){
              // @TODO: remove
            }
          });
        }
    }).filter('format',format);

    return ticket;
});