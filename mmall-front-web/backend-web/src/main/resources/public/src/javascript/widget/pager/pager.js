/*
 * ------------------------------------------
 * 分页器控件封装实现文件
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/global',
    'base/klass',
    'base/element',
    'ui/pager/pager',
    'util/template/jst',
    'text!./pager.html',
    'text!./pager.css'
],function(NEJ,_k,_e,_i0,_t0,_html,_css,_p,_o,_f,_r){
    // variable declaration
    var _pro,
        _seed_css = _e._$pushCSSText(_css),
        _seed_page = _t0._$add(_html);
    /**
     * 分页器控件封装
     *
     * 页面结构举例
     * ```html
     *   <div id="pagerCnt">page</div>
     *   <div id="pagerCnt2">page</div>
     * ```
     *
     * 脚本举例
     * ```javascript
     *   NEJ.define([
     *       '{pro}widget/pager/pager'
     *   ],function(_u,_p,_o,_f,_r){
     *           // 默认第一页
     *       var _setIndex = 1;
     *       // 页面更改的回调方法
     *       var _onchangeHandle = function(_obj){
     *           var _index = _obj.index;
     *       };
     *       // 实例化一个pager对象，总共10页
     *       var _pager = _u._$$Pager._$allocate({
     *           parent:'pagerCnt',
     *           onchange: _onchangeHandle,
     *           total: 10,
     *           index:_setIndex
     *       });
     *       // 从第2页翻到第10页
     *       for(var i = 2 ; i < 11 ; i++){
     *           _setIndex = i;
     *           _pager._$setIndex(_setIndex);
     *       }
     *       // 绑定一个翻页器,视觉上翻页器会联动，
     *       但最后触发一次翻页器的回调,避免重复触发
     *       _pager._$bind('pagerCnt2');
     *   })
     * ```
     *
     * @class     module:{pro}widget/pager/pager._$$Pager
     * @extends   module:ui/base._$$Abstract
     * @param     {Object}  arg0 - 可选配置参数
     * @property  {Number}  index - 当前页码
     * @property  {Number}  total - 总页码数
     * @property  {Boolean} noend - 无尾页显示
     */
    /**
     * 页码切换事件，输入{last:3,index:1,total:12}
     *
     * @event  module:ui/pager/pager._$$Pager#onchange
     * @param  {Object}   arg0  - 页码状态对象
     * @property {Number} last  - 上一次的页码
     * @property {Number} index - 当前要切换的页面
     * @property {Number} total - 总页面数
     *
     */
    _p._$$Pager = _k._$klass();
    _pro = _p._$$Pager._$extend(_i0._$$Pager);


    /**
     * 初始化外观信息
     *
     * @protected
     * @method module:ui/pager/base._$$AbstractPager#__initXGui
     * @return {Void}
     */
    _pro.__initXGui = function(){
        this.__seed_css  = _seed_css;
    };
    /**
     * 重置页码数
     *
     * @protected
     * @method module:ui/pager/base._$$AbstractPager#__doResetNumber
     * @return {Void}
     */
    _pro.__doResetNumber = function(_data){
        _t0._$render(
            this.__body,_seed_page,_data
        );
        var _seed = _t0._$seed();
        this.__popt.list = _e._$getByClassName(
                this.__body,
                'js-i-'+_seed
        );
        this.__popt.pbtn = (
            _e._$getByClassName(
                this.__body,
                'js-p-'+_seed
            )||_r
        )[0];
        this.__popt.nbtn = (
            _e._$getByClassName(
                this.__body,
                'js-n-'+_seed
            )||_r
        )[0];
    };

    return _p;
});