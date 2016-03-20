/*
 * ------------------------------------------
 * 地址列表
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'pro/extend/util',
    'pro/widget/BaseComponent',
    'text!./address.html'
],function(_,BaseComponent,_html0){

    var address = BaseComponent.extend({
        url:'/user/consigneeAddress',
        name: 'm-user-info-address',
        template: _html0,
        config: function(data){
          _.extend(data, {
            list: []
          });
        },
        init: function(){
          // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
          //this.$on('updatelist', this.getList.bind(this));
          this.getList();
        },

        //获取数据
        getList: function(){
          var data = this.data;
          // console.log(data);
          this.$request(this.url, {
            data: {userId:this.data.userid},
            onload: function(json){
              if (json && json.code == 200){
                data.list = json.result.list;
              }
            },
            // test
            onerror: function(json){
              // @TODO: remove
            }
          });
        }
    })

    return address;
});