NEJ.define([
    'text!./express.html',
    'pro/extend/util',
    'pro/widget/BaseComponent'
],function(_html,_,BaseComponent){
    return BaseComponent.extend({
        url:'/myorder/package/detail',
        template: _html,
        config: function(data){
          _.extend(data, {
            result: []
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
          this.$request(this.url, {
            data: {packageID:this.data.pid},
            onload: function(json){
              if (json.code == 200){
                data.result = json.result.trackVOList;
              }
            },
            // test
            onerror: function(json){
              // @TODO: remove
            }
          });
        }
    });
});