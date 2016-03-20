/*! tooltip - v - 2014-09-26
 * hzwuyuedong@corp.netease.com
 * Copyright (c) 2014 ; Licensed MIT */
define([
  '{pro}extend/util.js',
  '{lib}base/klass.js',
  '{lib}base/element.js',
  '{lib}base/util.js',
  '{lib}ui/base.js',
  '{lib}util/template/tpl.js',
  'text!./tooltip.html'
], function(_, k, e, u, t, t1, tpl,p,o,f,r) {
  var pro,
  seed_html = t1._$addNodeTemplate(tpl);
  p._$$ToolTip = k._$klass();
  pro = p._$$ToolTip._$extend(t._$$Abstract);

  //<span data-placement="top" r-tooltip></span>
  //option.target     hover的元素
  //data-placement    tooltip相对target的位置
  //data-content      tooltip的内容
  //data-clazz        tooltip自定义class
  //data-offset       tooltip偏移量

  pro.__init = function(options){
    this.__defaults = {
      arrow_orientation_map:{
        'left': 'right',
        'right': 'left',
        'top': 'bottom',
        'bottom': 'top'
      }
    };
    this.__super(options);
  };

  pro.__reset = function(options){
    this.__super(options);
    this.__target = e._$get(options.target);
    this.__settings = _.extend(this.__defaults, {
      "content": e._$dataset(this.__target, 'content') || '',
      "placement": e._$dataset(this.__target, 'placement') || 'left',
      "clazz":  e._$dataset(this.__target, 'clazz') || '',
      "offset": parseInt(e._$dataset(this.__target, 'offset'), 10) || 10
    }, true);
    if(this.__settings.content != ''){
      this.__show();
    }
  };

  /**
   * 初始化节点
   *
   */
  pro.__initNode = function (options) {
    this.__super();
    var list = e._$getByClassName(this.__body, 'j-flag');
    this.__arrow = list[0];
    this.__cbox = list[1];
  };

  /**
   * 清除tooltip添加class和内容
   *
   */
  pro.__destroy = function(){
    var settings = this.__settings,
      arrow_orientation = settings.arrow_orientation_map[settings.placement];
    this.__super();
    this.__cbox.innerHTML = '';
    e._$delClassName(this.__body, settings.clazz);
    e._$delClassName(this.__arrow, 'arrow-'+arrow_orientation);
  };

  pro.__initXGui = function(){
    this.__seed_html  = seed_html;
  };


  pro.__show = function(){
    var settings = this.__settings,
      offset = e._$offset(this.__target),
    //hover元素的高宽
      boxWidth =  this.__target.offsetWidth,
      boxHeight = this.__target.offsetHeight,
      arrow = this.__arrow,
      arrow_orientation, left, top, width,height;

    arrow_orientation = settings.arrow_orientation_map[settings.placement];
    e._$addClassName(this.__body, settings.clazz);
    e._$addClassName(arrow, 'arrow-'+arrow_orientation);
    this.__cbox.innerHTML = settings.content;
    //tooltip的高宽
    width = this.__body.offsetWidth;
    height = this.__body.offsetHeight;

    switch(settings.placement){
      case 'left':
        left = Math.floor(offset.x - width - settings.offset);
        top = Math.floor(offset.y + boxHeight/2 - height/2);
        e._$setStyle(arrow, 'right', '-7px');
        e._$setStyle(arrow, 'top', Math.floor(height/2 - 7) + 'px');
        break;
      case 'right':
        left = Math.floor(offset.x + boxWidth + settings.offset);
        top = Math.floor(offset.y + boxHeight/2 - height/2);
        e._$setStyle(arrow, 'left', '-7px');
        e._$setStyle(arrow, 'top', Math.floor(height/2 - 7) + 'px');
        break;
      case 'top':
        left = Math.floor(offset.x + boxWidth/2 - width/2);
        top = Math.floor(offset.y - height - settings.offset);
        e._$setStyle(arrow, 'bottom', '-7px');
        e._$setStyle(arrow, 'left', Math.floor(width/2 - 7) + 'px');
        break;
      case 'bottom':
        left = Math.floor(offset.x + boxWidth/2 - width/2);
        top = Math.floor(offset.y + boxHeight + settings.offset);
        e._$setStyle(arrow, 'top', '-7px');
        e._$setStyle(arrow, 'left', Math.floor(width/2 - 7) + 'px');
        break;
    }
    e._$setStyle(this.__body, 'top', top + 'px');
    e._$setStyle(this.__body, 'left', left + 'px');
  };

  return p;
});