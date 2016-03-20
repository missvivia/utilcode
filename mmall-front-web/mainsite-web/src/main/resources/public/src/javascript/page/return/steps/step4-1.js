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
    'util/chain/chainable',
    'pro/page/return/steps/returnList/list',
    'text!pro/page/return/steps/template/step4.html',
    'util/ajax/xdr',
    'base/util',
    'base/element',
    'pro/page/return/steps/payMethod/payMethod',
    'pro/page/return/steps/returnMoney/returnMoney',
    'text!pro/page/return/steps/returnMoney/returnMoney2.html',
    'util/template/tpl',
    'util/template/jst',
    'base/event',
    'util/event'
],function(_k,_w,$,ListComponent,_html,_j,_u,_e,PayMethod,ReturnMoney,_r2html,_t,_t2,_v,_u,_p,_o,_f,_r){
    var _pro;

    _p._$$StepFourPass = _k._$klass();
    _pro = _p._$$StepFourPass._$extend(_u._$$EventTarget);
    var _seed = _t._$parseUITemplate(_html);


    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(_options){
        var _parentId="#"+_options.parent;
        this.__parent=_options.parent;
        this.__components=[];
        this.__super();
        this.__initStep(_parentId,_options.data);
        
       
    };

    _pro.__initStep=function(_parentId,_data){
        this.__data=_data;
        var _list = _data.returns.list;
        this.__initReturnStatus(_parentId);
        this.__initReturnMoney(_parentId,_data.price);
        this.__initList(_parentId,_list);
    };

    _pro.__initReturnStatus=function(_parent){
        var _node = _t._$getNodeTemplate("step4-ntp-returnstatuspass");
        $(_parent)[0].appendChild(_node)
    };


    _pro.__initList = function(_parentId,list){
        var that = this;
        this.__list=new ListComponent();
        this.__list.$inject(_parentId);
        this.__list.setData(list);
        this.__components.push(this.__list);
        this.__list.$on("sendQequest",function($event){
          that.__sendRuquest(that.__returnMoney.setData,that.__returnMoney,false);
        })
    };

    _pro.__initReturnMoney=function(_parent,_data){
        this.__returnMoney = new ReturnMoney({data:_data,template:_r2html});
        this.__returnMoney.$inject(_parent,"bottom");
    };


    return _p._$$StepFourPass;
});