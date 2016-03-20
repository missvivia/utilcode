/**
 * Created by wuyuedong on 2014/10/11.
 */

define(
  [ 'base/klass',
    'base/util',
    'base/event',
    'base/element',
    'util/event',
    'pro/widget/tooltip/tooltip' ],
  function(k, ut, v, e, t, t1, p, o, f, r) {
    var pro;

    p._$$ToolTipWrap = k._$klass();
    pro = p._$$ToolTipWrap._$extend(t._$$EventTarget);
    pro.__reset = function(options) {
      this.__super(options);
      this.__bindTooltip(options);
    };

    //绑定事件
    pro.__bindTooltip = function(options){
      var elem = options.element;
      var toolTip;
      v._$addEvent(elem, 'mouseover', function(){
        toolTip = t1._$$ToolTip._$allocate({target: elem, parent: document.body});
      });
      v._$addEvent(elem, 'mouseout', function(){
        toolTip._$recycle();
      });
    }

    /**
     * 销毁控件
     */
    pro.__destory = function() {
      this.__super();
    };
    return p;
  });