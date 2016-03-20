NEJ.define([
    'text!./tab.html',
    'pro/widget/BaseComponent'
],function(_html,Component,_p,_o,_f,_r){
    return Component.extend({
        template:_html,
        data:{
            selectedIndex:0
        },
        /**
         * tab选项点击事件
         * @param _index
         */
        go:function(_index){
            this.$update("selectedIndex",_index);
            this.$emit('change',_index);
        }
    });
});