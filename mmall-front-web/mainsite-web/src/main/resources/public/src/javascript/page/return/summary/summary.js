/*
 * ------------------------------------------
 * 退回申请说明页面
 * @version  1.0
 * ------------------------------------------
 */
NEJ.define([
	'text!./summary.css',
    'text!./summary.html',
    'base/klass',
    'ui/base',
    'util/chain/chainable',
    'base/element',
    'util/template/tpl',
    'base/config'
],function(_css,_html,_k,_i,$,_e,_tpl,_c,_p,_o,_f,_r){
    var _pro,
    _seed_css = _e._$pushCSSText(_css),
    _seed_html= _tpl._$addNodeTemplate(_html);

    _p._$$Summary = _k._$klass();
    _pro = _p._$$Summary._$extend(_i._$$Abstract);;


    _pro.__init = function(_options){
        this.__super(_options);
      
        // TODO
    };

    _pro.__initXGui = function() {
		this.__seed_css  = _seed_css;
		this.__seed_html = _seed_html;
	};

    _pro.__getNodes=function(_options){
        this.__agreeBtn = $(".j-agree")[0];
    };
    // 控件重复使用重置过程
    _pro.__reset = function(_options){
         this.__super(_options);
         this.__getNodes(_options);
         this.__doInitDomEvent([
          [this.__agreeBtn, 'click', this.__onAgree._$bind(this)]
        ]);
    };

    _pro.__onAgree=function(_event){
        this._$dispatchEvent(
            'onagree',{
                x:'xxxxx',
                y:'yyyyyyy'
            }
        );
    };
    // 控件回收销毁过程
    _pro.__destroy = function(){
        this.__super();
        // TODO
    };


     return _p;
});