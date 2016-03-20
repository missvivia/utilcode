/*
 * --------------------------------------------
 * 商品列表里的尺码助手
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * --------------------------------------------
 */
define([
  "regular!./product.helper.html",
  "{pro}widget/BaseComponent.js",
  "{pro}components/notify/notify.js",
  "{pro}components/form/btnSelect.js",
  "{pro}components/product/helper.view.js",
  '{pro}extend/util.js',
  '{pro}extend/config.js'
  ], function( tpl, BaseComponent, notify ,BtnSelect, HelperView , _, config){

  var ProductHelper = BaseComponent.extend({
    // 默认属性
    // 计算属性
    template: tpl,
    config: function(data){
      _.extend(data, {
        useHelper: !!data.helperId
      })
      if(data.helperId ==0) data.helperId = undefined;
      // 请求远程的尺寸助手
      this.$request(config.resource.HELP, {
        data: {
          limit: 10,
          offset: 0,
          lastId: undefined
        },
        onload: function( json ){
          if(json.code === 200){
            data.helpers = json.result.list || [];
            this.$watch("helperId", function(id){
              var index = _.findInList(id, data.helpers);
              if(~index) data.selected = data.helpers[index];
              else data.selected = data.helpers[0];
              if(!data.selected.body) data.selected.body = []
            })
          }
        },
        onerror: function( json ){
          notify.notify({
            "type": "error",
            "message": "同步尺寸助手信息失败"
          })
        }
      })
    },
    init: function(){},
    pick: function(helper){
      var data = this.data;
      if(typeof helper.body === "string"){
        helper.body = JSON.parse(helper.body)
      }
      if(!helper.body) helper.body = []
      data.selected = helper;
    }
  }).component("helper-view", HelperView)



  return ProductHelper;

})