/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{lib}util/file/select.js',
    '{pro}widget/module.js',
    '{pro}components/brand/brandBasic.js',
    '{pro}components/brand/brandCreateTable.js',
    '{lib}util/template/tpl.js',
    '{lib}base/platform.js',
    '{lib}util/template/jst.js',
    'util/chain/chainable',
    'util/ajax/xdr',
    ],
    function(_ut,_v,_e,_s,Module,BrandBasic,Table,el,pt,e1,$,_j,p) {
        var _pro,maxSize3=3,maxSize6=6;
        p._$$BrandEditModule = NEJ.C();
        _pro = p._$$BrandEditModule._$extend(Module);
        
        _pro.__init = function(_options) {
            _options.tpl = 'jst-template';
            this.__supInit(_options);
            this.__initData();
            this.__getNodes(_options);
            this.__bindEvents(_options);
//            this.__initProvince(_options);

        };
        _pro.__initData=function(){
            var that = this;
            this.__brandBasic = new BrandBasic({
            	data:{
            		basic:{logoUrl:window["g_logo"]}
            	}
            }).$inject("#basicinfo",'top');
            this.__table = new Table({}).$inject('.j-table', 'after');
        };
        
        _pro.__getNodes=function(_options){
        	var _actionbox= $("#actions",this.__body);
        	var actions= $(".j-action",_actionbox);
        	this.__saveBtn = actions[0];
        	this.__cancelBtn = actions[1];
        	
        };

        _pro.__bindEvents=function(_options){
            var that =this;
            var _node = _e._$get("addShop");
            _v._$addEvent(_node,'click',this._showAddShopDialog._$bind(this),false);
            _v._$addEvent(this.__saveBtn,'click',this.__onSaveBtn._$bind(this));
            _v._$addEvent(this.__cancelBtn,'click',this.__onCancelBtn._$bind(this));
        };


//        _pro.__initProvince = function (opotions) {
//            var that = this;
//            _j._$request("/brand/shop/province", {
//                    type: 'json',
//                    method: 'get',
//                    onload: function (_data) {
//                        if (_data) {
//                            that.__province=_data;
//                            that.__table = new Table({data:{
//                                province:that.__province
//                            }}).$inject('.j-table', 'after');
//                        }
//
//                    },
//                    onerror: function (_error) {
//                        notify.notify({type:"error",message:_error.message})
//                    }
//                }
//            );
//        };



        _pro._showAddShopDialog = function(_event){
            if(this.__table)
            this.__table.showDialog();
        };
        
        _pro.__getBasicReq=function(){
        	return this.__brandBasic.getReq();
        };
        
        _pro.__getTableReq = function(){
        	var req= this.__table?this.__table.getDataList():[];
        	return req;
        };
        _pro.__getReqParam=function(){
        	var basic = this.__getBasicReq();
        	var table = this.__getTableReq();
            if(basic){
                var _obj = {"basic":basic,shops:table};
            }
        	return _obj;
        };
        _pro.__onSaveBtn=function(_event){
        	var _obj = this.__getReqParam();
            if(_obj){
                _j._$request("/brand/add",{
                headers:{"Content-Type":"application/json;charset=UTF-8"},
                 method:'put',
                 type:"json",
                 data:JSON.stringify(_obj),
                 onload:function(_data){
                     if(_data&&_data.basic.id){
                         window.location.href="/brand/display";
                     }
                     
                 },
                 onerror:function(_error){
                  console.log(_error);
                 }

               });

            }
        	
        };
        
        _pro.__onCancelBtn = function(_event){
        	window.location.reload();
        	
        };
        
        _pro.__destroy = function(){
            this.__super();
             _f._$$WebForm._$recycle(this.__form);
            delete this.__form;
        };

        p._$$BrandEditModule._$allocate();
 });