/**
 * Created by maggiehe on 14-12-14.
 */
NEJ.define([
    '{lib}base/event.js',
    'text!./select.html',
    'pro/widget/BaseComponent'
],function(_v,_html,Component,_p,_o,_f,_r){
    return Component.extend({
        template:_html,
        data:{
            list:[
                {name:"全部",value:-1},
                {name:"可用",value:0},
                {name:"已过期",value:2},
                {name:"已使用",value:3},
                {name:"已失效",value:5},
                {name:"未生效",value:8}
            ],
            currentIndex:0,
            showList:false
        },
        /**
         * 筛选事件
         * @param _index
         */
        select:function(_index){
            this.data.currentIndex=_index;
            this.$emit('selectChanged',this.data.list[_index].value);
            this.data.showList=false;
        }
    });
});

