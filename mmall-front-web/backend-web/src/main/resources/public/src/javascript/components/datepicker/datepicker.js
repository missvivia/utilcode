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
   * 2. new DatePicker({data: {start: 'xx', ...} })
   */
  var DatePicker = BaseComponent.extend({
    name: "datepicker",
    computed: {
      dateStr: function(data){
          return u._$format(data.select, data.format);
      }
    },
    template: 
      '<div class="input-group" on-click={{this.show($event)}}>\
        <input type="text" class="form-control" name={{name}} value={{dateStr}} data-type="date" data-time={{time}} readOnly>\
        <span class="input-group-btn">\
          <button class="btn btn-default" type="button">\
            <span class="glyphicon glyphicon-calendar"></span>\
          </button>\
        </span>\
      </div>',
    config: function(data){
      var now = new Date;
      now = +new Date(now.getFullYear(), now.getMonth(), now.getDate());
      _.extend(data, {
        select: now,
        format: "yyyy-MM-dd"
      });
    },
    init: function(){
      this.datepicker;
    },
    show: function(ev){
      // @TODO: NEJ,bug;
      ev.stopPropgation();
      var self = this;
      var data = this.data;
      if(this.datepicker){
        this.datepicker._$recycle();
      }
      this.datepicker = ut._$$DatePick._$allocate({
        parent: this.group.children[0].node(),
        clazz: 'dropdown-menu m-datepicker m-datepicker-tr',
        date: data.select,
        // 设置日期的可选范围
        onchange:function(date){
          self.$update("select", +date) 
          self.$emit("select", +date) 
        }
      });
    }
  })


  return DatePicker;

})