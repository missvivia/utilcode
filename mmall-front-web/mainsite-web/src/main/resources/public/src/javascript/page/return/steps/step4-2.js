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
    'pro/extend/config',
    'pro/extend/util',
    'util/chain/chainable',
    'pro/page/return/steps/returnList/list',
    'text!pro/page/return/steps/template/step4.html',
    'util/ajax/xdr',
    'base/util',
    'base/element',
    'util/template/tpl',
    'util/template/jst',
    'base/event',
    'util/event'
],function(_k,_w,_config,_,$,ListComponent,_html,_j,_u,_e,_t,_t2,_v,_u,_p,_o,_f,_r){
    var _pro;

    _p._$$StepFourFail = _k._$klass();
    _pro = _p._$$StepFourFail._$extend(_u._$$EventTarget);
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
        this.__initReturnStatus(_parentId,_data.statusInfo);
        this.__initList(_parentId,_list);
//        $("#service")._$attr("href",_config.IM_DOMAIN_URL + _.getFullUserName());
        _v._$addEvent($("#service")[0], 'click', function(_event){
        	_v._$stop(_event);
       	 _._$openKefuWin();
       });
    };

    _pro.__initReturnStatus=function(_parent,_data){
        var _html = _t2._$get('step4-jst-returnstatusfail',_data);
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


    return _p._$$StepFourFail;
});