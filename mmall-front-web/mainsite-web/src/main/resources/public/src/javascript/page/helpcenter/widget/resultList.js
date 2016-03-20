NEJ.define([
    'pro/extend/util',
    'base/element',
    'base/util',
    'text!./resultList.html',
    'pro/components/ListComponent',
    'pro/components/pager/ucenterpager'
],function(_,_e,_u,_html,ListComponent,ucenterpager){
    return ListComponent.extend({
        watchedAttr: ['current'],
        url:'/help/search',
        template:_html,
        toggle:function(item){
            item.flag = !item.flag;
        },
        __getList: function(){
            var data = this.data;
            this.$request(this.url, {
                method:'get',
                data: this.getListParam(),
                onload: function(json){
                    var result = json.result||{},
                        list = result.list||[],
                        datanum = list.length;
                    //searcha
                    var regs = data.condition.keywords.replace(/\s+/g ,'+').split("+");
                    for(var i=0;i<list.length;i++){
                        list[i].flag = false;
                        list[i].content = _._$unescape(list[i].content);
                        for(var j = 0,rl = regs.length; j < rl; j++){
                            var reg = new RegExp("("+regs[j]+")","g");
                            list[i].content = (list[i].content).replace(reg,"<span class='s-fc5'>$1</span>");
                        }
                    }
                    data.total = result.total;
                    data.list = list;
                    data.datanum = datanum;
                },
                // test
                onerror: function(json){
                    // @TODO: remove
                }
            });
        }
    });
});