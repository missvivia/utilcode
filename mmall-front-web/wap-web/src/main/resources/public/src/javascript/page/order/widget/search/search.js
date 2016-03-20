NEJ.define([
    'text!./search.html',
    'pro/extend/util',
    'pro/widget/BaseComponent'
],function(_html,_,BaseComponent){
    return BaseComponent.extend({
        template: _html,
        config: function(data){
          _.extend(data, {
            searchValue:''
          });
        },
        init: function(){
          // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
          //this.$on('updatelist', this.getList.bind(this));
        },
        dosearch: function(){
          this.$emit('search',this.data.searchValue);
        }
    });
});