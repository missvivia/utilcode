NEJ.define([
    'text!./search.html?20150915',
    '{pro}extend/util.js',
    '{pro}widget/BaseComponent.js'
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
        onKeyUp: function($event){
        	if($event.which == 13) {
                this.dosearch();
            }
        },
        dosearch: function(){
          this.$emit('search',this.data.searchValue);
        }
    });
});