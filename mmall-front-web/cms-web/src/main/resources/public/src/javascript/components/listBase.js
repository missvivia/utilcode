/**
 * 基于NEJ和bootstrap的日期选择器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "{pro}extend/util.js",
  "{pro}widget/BaseComponent.js"
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
    },
    init: function(){
      if(!this.url) throw "ListModule未指定url";
      this.$watch(this.watchedAttr, function(){
        if(this.shouldUpdateList()) this.getList();
      })
      // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
      this.$on("updatelist", this.getList.bind(this));

    },
    // @子类修改
    shouldUpdateList: function(data){
      return true;
    },
    // @子类修改
    getExtraParam: function(data){
      return {}
    },
    getListParam: function(){
      var data = this.data;
      return _.extend({
        limit: data.limit,
        offset: data.limit * (data.current-1)
      }, this.getExtraParam(data));
    },
    // update loading
    getList: function(){
      var data = this.data;
      this.$request(this.url, {
        progress: true,
        data: this.getListParam(),
        onload: function(json){
          var result = json.result,
            list = result.list;
          _.mergeList(list, data.list)

          data.total = result.total;
          data.list = list;
        },
        // test
        onerror: function(json){
          // @TODO: remove
        }
      })
    }
  })


  return ListComponent;

})