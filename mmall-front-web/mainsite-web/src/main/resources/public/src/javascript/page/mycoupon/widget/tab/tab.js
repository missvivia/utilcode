/*
 * ------------------------------------------
 * TAB控件封装
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'text!./tab.html',
    'pro/widget/BaseComponent'
],function(_html,Component,_p,_o,_f,_r){
    return Component.extend({
        template:_html,
        data:{
            selectedIndex:0
        },
        go:function(_index){
            this.data.selectedIndex = _index;
            this.$emit('change',_index);
        }
    });
});