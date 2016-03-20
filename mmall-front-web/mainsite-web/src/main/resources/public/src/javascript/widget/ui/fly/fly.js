/*
 * ------------------------------------------
 * 购物车飞入效果 copy taobao
 * @version  1.0
 * @author   hzwuyuedong@corp.netease.com
 * ------------------------------------------
 */

NEJ.define([
  'base/klass',
  'base/event',
  'base/util',
  'base/element',
  'ui/base',
  'util/template/tpl',
  'util/chain/NodeList',
  'text!./fly.html',
  'pro/widget/util/requestAnimationFrame'
],function(_k,_v,_u,_e,_t, _t1,$,_tpl,_p,_o,_f,_r){
  var _pro;
  var _seed_html = _t1._$addNodeTemplate(_tpl);

  /*_t._$$Flyer._$allocate({
    startNode: $('#cart')[0],
    destNode: $('#sidebar-carticon')[0],
    endSize: {x: 10, y: 10},
    parent: document.body
  })._$show();*/

  //<span data-placement="top" r-tooltip></span>
  //option.startNode    起始元素(String||Node)
  //option.destNode     终点元素(String||Node)
  //option.oncomplete   结束回调
  //option.imgUrl       放入购物车sku的img
  //option.endSize      运动元素结束的的高宽

  _p._$$Flyer = _k._$klass();
  _pro = _p._$$Flyer._$extend(_t._$$Abstract);

  /**
   * 控件重置
   *
   * @protected
   */
  _pro.__reset = function(_options){
    _options.parent = _options.parent || document.body;
    this.__super(_options);
    this.__initData(_options);
  };

  /**
   * 控件回收
   *
   */
  _pro.__destroy = function(){
    this.__super();
    $(this.__body)._$style({width: '40px', height: '40px'});
    this.__img.src = '#';
  };

  /**
   * 初始化外观
   * @private
   */
  _pro.__initXGui = function(){
    this.__seed_html = _seed_html;
  };

  /**
   * 初始化node
   * @private
   */
  _pro.__initNode = function(){
    this.__super();
    $(this.__body)._$style({position: 'fixed', marginTop: '0px', marginLeft: '0px', zIndex: '9999', width: '40px', height: '40px'});
    this.__img = _e._$getByClassName(this.__body, 'j-flag')[0];
  };

  /**
   * 计算曲率等数据
   * @param _options
   * @private
   */
  _pro.__initData = function(_options){
    var _box = _e._$getPageBox(),
      _offset_start = _e._$offset(_e._$get(_options.startNode)),
      _offset_dest = _e._$offset(_e._$get(_options.destNode));
    this.__start = {
      top: _offset_start.y - _box.scrollTop,
      left: _offset_start.x - _box.scrollLeft
    }
    this.__dest = {
      top: _offset_dest.y,
      left: _offset_dest.x
    };

    _e._$attr(this.__img, 'src', _options.imgUrl || 'http://localhost:8000/res/images/temp/demo.png');

    this.__speed = _options.speed||1.2;
    this.__endSize = _options.endSize;
    this.__count = -1;

    this.__topRefer = Math.min(this.__start.top, this.__dest.top) - Math.abs(this.__start.left - this.__dest.left) * (1 / 3);
    if (this.__topRefer < 0) {
      this.__topRefer = Math.min(0, Math.min(this.__start.top, this.__dest.top));
    }
    this.__distance = Math.sqrt(Math.pow(this.__start.top - this.__dest.top, 2) + Math.pow(this.__start.left - this.__dest.left, 2));
    //speed越大，steps越少
    this.__steps = Math.ceil(Math.min(Math.max(Math.log(this.__distance) / .05 - 75, 30), 100) / this.__speed);
    this.__O = this.__start.top == this.__topRefer ? 0 : -Math.sqrt((this.__dest.top - this.__topRefer) / (this.__start.top - this.__topRefer));
    this.__L = (this.__O * this.__start.left - this.__dest.left) / (this.__O - 1);
    this.__curvature = this.__dest.left == this.__L ? 0 : (this.__dest.top - this.__topRefer) / Math.pow(this.__dest.left - this.__L, 2);

    if (this.__endSize) {
      this.__size = {x: this.__body.clientWidth, y: this.__body.clientHeight};
    };
  };

  /**
   * 按step move
   * @private
   */
  _pro.__move = function(){
    ++this.__count;
    var left = this.__start.left + (this.__dest.left - this.__start.left) * this.__count / this.__steps;
    var top = this.__curvature == 0 ? this.__start.top + (this.__dest.top - this.__start.top) * this.__count / this.__steps : this.__curvature * Math.pow(left - this.__L, 2) + this.__topRefer;
    var offset = {left: left,top: top};
    var tmpOffset = {left: 0,top: 0};
    if (this.__endSize) {
      var i = this.__steps / 2;
      var width = this.__endSize.x - (this.__endSize.x - this.__size.x) * Math.cos(this.__count < i ? 0 : (this.__count - i) / (this.__steps - i) * Math.PI / 2),
        height = this.__endSize.y - (this.__endSize.y - this.__size.y) * Math.cos(this.__count < i ? 0 : (this.__count - i) / (this.__steps - i) * Math.PI / 2);
      $(this.__body)._$style({width: width + "px",height: height + "px","font-size": Math.min(width, height) + "px"});
      tmpOffset.left = -width / 2;
      tmpOffset.top = -height / 2
    }
    $(this.__body)._$style({left: offset.left + tmpOffset.left + "px",top: offset.top + tmpOffset.top + "px"});

    var timer = window.requestAnimationFrame(this.__move._$bind(this));
    if (this.__count == this.__steps) {
      window.cancelAnimationFrame(timer);
      //callback
      this._$dispatchEvent('oncomplete');
      this._$recycle();
    }
  };

  /**
   * 开始动画
   *
   */
  _pro._$show = function(){
    this.__super();
    this.__move();
  };

  return _p;
});
