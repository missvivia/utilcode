/*
 * ------------------------------------------
 * 订单列表实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */

NEJ.define([
    'pro/extend/util',
    'base/element',
    'base/util',
    'text!./userlist.html',
    'pro/components/ListComponent',
    'util/template/jst',
    'pro/extend/request',
],function(_,_e,_u,_html,ListComponent,_u0,request,_p,_o,_f,_r){

    var userList =  ListComponent.extend({
        config: function(data){
            _.extend(data, {
                total: 1,
                current: 1,
                limit: 10,
                list: [],
                condition:{'type':'0','value':''}
            });
            this.$watch(this.watchedAttr, function(){
                if(this.shouldUpdateList()) this.__getList();
            })
        },
        watchedAttr: ['current'],
        url:'/user/userlist',
        template:_html,
        init: function(){
            if(!this.url) throw "ListModule未指定url";
            // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
            this.$on("updatelist", this.__getList.bind(this));
        },
        __getList:function(){
            var data = this.data;
            var option = {
                progress: true,
                data: this.getListParam(),
                onload: function(json){
                  var result = json.result,
                    list = result.list||[];
                  _.mergeList(list, data.list,data.key||'id')

                  data.total = result.total;
                  data.list = list;
                  data.lastId = result.lastId;
                },
                // test
                onerror: function(json){
                  // @TODO: remove
                }
            };
            if(this.xdrOption){
                var xdrOpt = this.xdrOption();
                if(xdrOpt.norest){
                  option.data = _ut._$object2query(this.getListParam());
                  option.norest = true;
                }

                option.method = xdrOpt.method||'GET';

              }
            this.$request(this.url,option)
        },
        setCondition:function(_data){
            this.data.current = 1;
            this.data.condition.search = _data.search;
            this.data.condition.type = _data.type;
            this.$emit('updatelist');
        }
    });

    return userList;
});