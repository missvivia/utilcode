/*
 * ------------------------------------------
 * 订单列表
 * @version  1.0
 * @author   chenlei(chenlei@xyl.com)
 * ------------------------------------------
 */
NEJ.define([
	'{pro}extend/util.js',
    '{lib}base/util.js',
    '{pro}/widget/BaseComponent.js',
    'text!./account.html'
],function(_,ut,BaseComponent,_html0){

    var order = BaseComponent.extend({
        url:'/src/javascript/page/user/account/account.json',
        name: 'm-user-info-account',
        template: _html0,
        config: function(data){
            _.extend(data, {
              list:[]
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
          //console.log(data);
          this.$request(this.url, {
            data: {userId:this.data.userid},
            onload: function(json){
            	data.list = json.result.list;
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