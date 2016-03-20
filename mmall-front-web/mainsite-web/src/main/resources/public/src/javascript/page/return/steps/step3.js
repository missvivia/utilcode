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
    'text!pro/page/return/steps/template/step3.html',
    'util/ajax/xdr',
    'base/util',
    'base/element',
    'pro/page/return/steps/payMethod/payMethod',
    'pro/page/return/steps/returnMoney/returnMoney',
    'util/template/tpl',
    'util/template/jst',
    'base/event',
    'util/event'
],function(_k,_w,$,ListComponent,_html,_j,_u,_e,PayMethod,ReturnMoney,_t,_t2,_v,_u,_p,_o,_f,_r){
    var _pro;

    _p._$$StepThree = _k._$klass();
    _pro = _p._$$StepThree._$extend(_u._$$EventTarget);
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
        this.__initReturnMark(_parentId);
        this.__initReturnExpress(_parentId,_data.returnExpress);
        this.__initList(_parentId,_list);
        this.__initShowReturnTimeNotify(_parentId);
        this.__initReturnMoney(_parentId,_data.returnForm);
    };

    _pro.__initReturnMark=function(_parent){
        var _node = _t._$getNodeTemplate("step3-ntp-returnmark");
        $(_parent)[0].appendChild(_node)
    };

    _pro.__initReturnExpress=function(_parent,_data){
        var _html = _t2._$get('step3-jst-returninfo',_data);
        var _node =_e._$html2node(_html);
        $(_parent)[0].appendChild(_node);
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

    _pro.__initPayMethod=function(_payType,_parent){
        this.__payMethod=PayMethod._$allocate({payType:_payType,parentSelector:_parent});
        this.__components.push(this.__payMethod);
    };

    _pro.__initShowReturnTimeNotify=function(_parent){
      var _node = _t._$getNodeTemplate("step3-ntp-returntime");
         $(_parent)[0].appendChild(_node);
    };

    _pro.__initReturnMoney=function(_parent,_data){
        this.__returnMoney = new ReturnMoney({data:_data});
        this.__returnMoney.$inject(_parent,"bottom");
    };


    return _p._$$StepThree;
});