/**
 * 基于bootstrap的dropdown样式的select，与datepicker一样会有一个input用于webform的取值
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "text!./btnSelect.html",
  "{pro}extend/util.js",
  "{pro}widget/BaseComponent.js",
  "{lib}base/util.js"
  ], function(tpl, _, BaseComponent, u){

  /**
   * btn-select  就是bootstrap中的按钮下拉
   * 1. declarative: 
   *   <btn-select
   *         options=.. 默认空
   *         value=.. 默认空
   *         name=..  默认空
   *         direct= up [down:默认] left right
   *         on-select={{}}  select事件触发
   *   ></btn>
   * 2. new BtnSelect({data: {direct: 'xx', ...} })
   */
  var BtnSelect = BaseComponent.extend({
    name: "btn-select",
    template: tpl,
    computed: {
      "selectName": function(data){
        if(data.options && data.value){
          for(var i = 0, len = data.options.length; i < len; i++){
            if(data.options[i].value == data.value) return data.options[i].name
          }
        }
      }
    },
    config: function(data){
      _.extend(data, {options: [] });
    },
    select: function(option){
      this.data.value = option.value;
      this.$emit("select", option);
    }
  })

  return BtnSelect;

})