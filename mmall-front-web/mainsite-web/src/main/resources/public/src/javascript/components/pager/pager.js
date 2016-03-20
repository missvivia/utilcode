/**
 *
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
NEJ.define([
  "text!./pager.html",
  "pro/widget/BaseComponent",
  'base/element'
  ], function(tpl, BaseComponent, _e){

  // <pager total=3 current=1></pager>
  var Pager = BaseComponent.extend({
    name: "pager",
    template: tpl,
    // is called before compile. 一般用来处理数据
    config: function(data){
      var count =  5;
      var show = data.show = Math.floor( count/2 );
      data.current = parseInt(data.current || 1);
      data.total = parseInt(data.total || 1);

      this.$watch(['current', 'total'], function( current, total ){
        data.begin = current - show;
        data.end = current + show;
        if(data.begin < 2) data.begin = 2;
        if(data.end > data.total-1) data.end = data.total-1;
        if(current-data.begin <= 1) data.end = data.end + show + data.begin- current;
        if(data.end - current <= 1) data.begin = data.begin-show-current+ data.end;
      });
    },
    nav: function(page){
      var data = this.data,
          _prevBtn = _e._$get("prevBtn"),
          _nextBtn = _e._$get("nextBtn");
      if(page < 1) return;
      if(page > data.total) return;
      if(page === data.current) return;
      if(page === 1){
    	  _e._$addClassName(_prevBtn, 'disable');
      }else{
    	  _e._$delClassName(_prevBtn, 'disable');
      }
      if(page === data.total){
    	  _e._$addClassName(_nextBtn, 'disable');
      }else{
    	  _e._$delClassName(_nextBtn, "disable");
      }
      data.current = page;
      this.$emit('nav', page);
    }
  });

  return Pager;

})

