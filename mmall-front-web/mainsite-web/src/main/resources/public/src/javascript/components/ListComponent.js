/**
 * 基于NEJ和bootstrap的日期选择器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "pro/extend/util",
  "pro/widget/BaseComponent",
  "pro/components/pager/pager.js?20150828"
  ], function(_, BaseComponent){

  // ###data
  //  - pager
  //    * total: 列表总数
  //    * list : 列表数组
  // ###example
  // <div>
  //  {{#list list as item}}
  //
  //  {{/item}}
  // </div>

  var ListComponent = BaseComponent.extend({
    // 配置链接
    // @子类必须提供
    // url: 'xx.json',
  // 任意一个监听列表发生改变时，判断更新列表
  // @子类修改
    watchedAttr: ['current', 'status'],
    config: function(data){
      _.extend(data, {
        total: 1,
        current: 1,
        limit: 10,
        list: []
      });
      this.$watch(this.watchedAttr, function(){
        if(this.shouldUpdateList()) this.__getList();
      })
    },
    init: function(){
      if(!this.url) throw "ListModule未指定url";

      // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
      this.$on("updatelist", this.__getList.bind(this));

    },
    // @子类修改
    shouldUpdateList: function(data){
      return true;
    },
    getExtraParam:function(){
      return this.data.condition;
    },
    refresh:function(_data){
      this.data.current = 1;
      this.data.condition = _data;
      this.$emit('updatelist');
    },
    getListParam: function(){
      var data = this.data;
      return _.extend({
          limit: data.limit,
          offset: data.limit * (data.current-1)
        }, this.getExtraParam(data));
    },
    // update loading
    __getList: function(){
      var data = this.data;
      var option = {
        data: this.getListParam(),
        onload: function(json){
          var result = json.result,
            list = result.list||result||[];
          _.mergeList(list, data.list,data.key||'id')

          data.total = result.total;
          data.list = list;
        },
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
    }
  })


  return ListComponent;

})