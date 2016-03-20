/**
 * 动态字段生成, 用于商品属性
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!./prodParam.html",
  "{pro}widget/BaseComponent.js",
  "{pro}components/notify/notify.js",
  "{pro}extend/util.js",
  "{pro}components/form/webForm.js"
  ], function(tpl, BaseComponent, notify,  _, formModule){

  return BaseComponent.extend({
    template: tpl,
    config: function(data){
      // @TODO: remove. 使用假数据
      _.extend(data,{
        productParamList: [],
        messages: {}
      })

      this.configParam(data.productParamList);
    },
    configParam: function(list){
      list.sort(function(a, b){
        return a.type-b.type
      });
      list.forEach(function(param){
        if(param.type === 4){
          if(!param.value) param.value = [];
          else if(param.value && typeof param.value==="string"){
            param.value = JSON.parse(param.value)
          }
          if(param.value.length){
            var value = param.value;
            param.list.forEach(function(item){
              for(var i = 0, len = value.length; i < len ; i++){
                if(value[i] == item.optId) item.checked = true;
              }
            })
          }
        }
      })
    },
    // 刷新我们的productParams
    refresh: function(productParamList){
      this.configParam(productParamList || []);
      this.$update("productParamList", productParamList || [])
    }
  }).use(formModule);
})