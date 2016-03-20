/*
 * ------------------------------------------
 * 红包列表
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'pro/extend/util',
    'base/util',
    'pro/widget/BaseComponent',
    'text!./order.html'
],function(_,_u,BaseComponent,_html0){

    var order = BaseComponent.extend({
        url:'/user/order',
        name: 'm-user-info-order',
        template: _html0,
        config: function(data){
          _.extend(data, {
            list:{}
          });
        },
        init: function(){
          // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
          //this.$on('updatelist', this.getList.bind(this));
          this.getList();
        },

        groupList: function(){
          var group = {};
              group.active = [];
              group.over = [];
              group.past = [];
          _u._$forEach(this.data.result.list,function(o){
            if (o.status == 21 || o.status == 20){
              group.past.push(o);
            }else if(o.status == 0){
              group.active.push(o);
            }else{
              group.over.push(o);
            }
          }._$bind(this));
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
              data.list = this.groupList();
            }._$bind(this),
            // test
            onerror: function(json){
              // @TODO: remove
            }
          });
        }
    })

    return order;
});