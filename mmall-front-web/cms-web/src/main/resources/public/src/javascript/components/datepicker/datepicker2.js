/**
 * 日期和具体时间的选择器
 */
define([
  "{pro}extend/util.js?v=1.0.0.0",
  "{pro}widget/BaseComponent.js?v=1.0.0.0",
  "{lib}base/util.js?v=1.0.0.0",
  "{lib}ui/datepick/datepick.js?v=1.0.0.0",
  '{pro}widget/date/datepick.js?v=1.0.0.0'
  ], function(_, BaseComponent, u, ut,_dp){
  var du  = NEJ.P('du.ui');
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
    name: "datepicker2",
    computed: {
      dateStr: function(data){
          return u._$format(data.select, data.format);
      }
    },
    template:
      '<div class="input-group" on-click={{this.show($event)}}>\
        <input type="text" class="form-control" name={{name}} value={{dateStr}} data-pattern="^(((20[0-3][0-9]-(0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|(20[0-3][0-9]-(0[2469]|11)-(0[1-9]|[12][0-9]|30))) (20|21|22|23|[0-1][0-9]):[0-5][0-9]:[0-5][0-9])$" data-time={{time}} readOnly>\
        <span class="input-group-btn">\
          <button class="btn btn-default" type="button">\
            <span class="glyphicon glyphicon-calendar"></span>\
          </button>\
        </span>\
      </div>',
    config: function(data){
      var now = new Date;
      now = +new Date(now.getFullYear(), now.getMonth(), now.getDate(),now.getHours(),now.getMinutes(),now.getSeconds());
      _.extend(data, {
        select: now,
        format: "yyyy-MM-dd HH:mm:ss"
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
      if(data.disable){return;}
      if(this.datepicker){
        this.datepicker._$recycle();
      }
      this.datepicker = du._$$DatePick._$allocate({
        parent: this.group.children[0].node(),
        clazz: 'dropdown-menu m-datepicker m-datepicker-tr',
        date: data.select,
        // 设置日期的可选范围
        onchange:function(data){
          var time=data.date+" "+data.time;
          self.$update("select", time)
          self.$emit("select",  time)
        }
      });
    }
  })
  return DatePicker;
})