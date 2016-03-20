/**
 * 基于NEJ和bootstrap的日期选择器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
  "{pro}extend/util.js",
  "{pro}widget/BaseComponent.js",
  "{lib}base/util.js",
  "{lib}ui/datepick/datepick.js"
  ], function(_, BaseComponent, u, ut){

  /**
   * Data: 
   *   -format: 日期格式化 
   * Event: 
   *   -select: 
   *     - $event: {value: }
   * Example: 
   * 1. declarative: <datepicker select: start='xx' limit='xx' format='YY' />
   * 2. new LoadBtn({data: {start: 'xx', ...} })
   */
  var LoadBtn = BaseComponent.extend({
    name: "load-btn",
    template: 
      '<a class="btn btn-{{type||"primary"}} {{clazz}}" on-click={{this.click($event)}}><r-content /></a>',
    config: function(data){
      _.extend(data, {
        select: +new Date,
        format: "yyyy-MM-dd"
      });
    },
    init: function(){
      this.datepicker;
    },
    click: function(ev){
      ev.cancel();
    }
  })


  return LoadBtn;

})