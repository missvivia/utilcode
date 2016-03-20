/*
 * ------------------------------------------
 * 我的退货
 * @version  1.0
 * @author   xxx(xxx@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'pro/widget/module',
    'base/element',
    'pro/page/return/summary/summary',
    'util/chain/chainable',
    'pro/page/return/steps/steps',
    'util/template/tpl'
],function(_k,_w,_e,_t,$,Steps,_tpl,_p,_o,_f,_r){
    var _pro;

    _p._$$Index = _k._$klass();
    _pro = _p._$$Index._$extend(_w._$$Module);


    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
        this.__super();
        this.__initWay();
        
    };

    _pro.__initWay=function(){
        var _data=window["g_return"];
        var _returnState=_data.returnState;
        if(_returnState&&_returnState.intValue==0){
           this.__initSummary();
        }
        else{
            this.__initSteps();
        }
    };

    _pro.__initSummary=function(){
        var that = this;
        var _parent =$('#summary')[0];
        this.__summary = _t._$$Summary._$allocate({
            parent:_parent,
            onagree:that.__onAgree._$bind(that)
        });

    };

    _pro.__onAgree=function(_event){
        this.__summary._$recycle();
        $('#summary')._$remove();
        this.__initSteps();
    };

    _pro.__initSteps=function(_options){
        var _stepsBox = $("#steps-box");
        var _stepHTML=_tpl._$getTextTemplate('txt-template-steps');
        _stepsBox._$html(_stepHTML);
        var _parent =$('#steps',_stepsBox)[0];
        var _tabBox = $("#tab-box",_stepsBox)[0];
        this.__steps = Steps._$allocate({
            parent:_parent,
            tabBox:_tabBox
        });
    };

    _p._$$Index._$allocate({});

    return _p;
});