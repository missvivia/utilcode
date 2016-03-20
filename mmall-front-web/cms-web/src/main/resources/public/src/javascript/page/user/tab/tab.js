/*
 * ------------------------------------------
 * TAB控件封装
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'pro/extend/util',
    'text!./tab.html',
    'pro/widget/BaseComponent'
],function(_,_html,Component,_p,_o,_f,_r){
    return Component.extend({
        template:_html,
        config:function(data){
            _.extend(data,{
                selectedIndex:0
            })
        },
        go:function(_index){
            this.data.selectedIndex = _index;
            this.$emit('change',_index);
        }
    });
});