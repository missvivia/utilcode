/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    "{pro}extend/util.js",
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{lib}util/form/form.js',
    '{lib}util/file/select.js',
    '{pro}widget/module.js',
    'pro/extend/config',
    '{pro}components/brand/brandBasic.js',
    '{pro}components/brand/brandEditTable.js',
    '{lib}util/template/tpl.js',
    '{lib}base/platform.js',
    '{lib}util/template/jst.js',
    'util/chain/chainable',
    'util/ajax/xdr',
    "pro/components/notify/notify"
],
    function (_ut,_, _v, _e, _f, _s, Module, config, BrandBasic, Table, el, pt, e1, $, _j, notify, p) {
        var _pro, maxSize3 = 3, maxSize6 = 6;
        p._$$BrandEditModule = NEJ.C();
        _pro = p._$$BrandEditModule._$extend(Module);

        _pro.__init = function (_options) {
            _options.tpl = 'jst-template';
            this.__brandObj = window["g_brand"] || [];
            this.__supInit(_options);
            this.__initData();
            this.__bindEvents(_options);

        };

//        _pro.__initProvince = function (opotions) {
//            var that = this;
//            _j._$request("/brand/shop/province", {
//                    type: 'json',
//                    method: 'get',
//                    onload: function (_data) {
//                        if (_data) {
//                            that.__province = _data;
//                            that.__brandObj.shops.forEach(function (item) {
//                                item.province = _data;
//                            });
//                            that.__table = new Table({
//                                data: {
//                                    list: that.__brandObj.shops,
//                                    supplierBrandId: that.__brandObj.basic.id,
//                                    province: that.__province
//                                }
//                            }).$inject('.j-table', 'after');
//
//
//                        }
//
//                    },
//                    onerror: function (_error) {
//                        notify.notify({type: "error", message: _error.message})
//                    }
//                }
//            );
//        };

        _pro.__initData = function () {
            var that = this;
            this.__brandBasic = new BrandBasic({data: {
                basic: that.__brandObj.basic,
                fixop: {
                    eternal: true
                }
            }}).$inject("#basicinfo", 'top');
            that.__table = new Table({
              data: {
                  list: that.__brandObj.shops,
                  supplierBrandId: that.__brandObj.basic.id,
              }
          }).$inject('.j-table', 'after');

        };


        _pro.__bindEvents = function (_options) {
            var _node = _e._$get("addShop");
            var actions = $('.j-action');
            this.__preBtn = $("#preview2main")[0];
            this.__saveBtn = actions[0];
            this.__submitBtn = actions[1];
            _v._$addEvent(_node, 'click', this._showAddShopDialog._$bind(this), false);
            _v._$addEvent(this.__saveBtn, 'click', this.__onSaveBtn._$bind(this));
            _v._$addEvent(this.__submitBtn, 'click', this.__onSubmitBtn._$bind(this));
            _v._$addEvent(this.__preBtn, 'click', this.__onPreviewBtn._$bind(this));
        };

        _pro._showAddShopDialog = function (_event) {
            if(this.__table)
            this.__table.showDialog(this.__province);
        };

        _pro.__getBasicReq = function () {
            return this.__brandBasic.getReq();
        };

        _pro.__getReqParam = function () {
            var basic = this.__getBasicReq();
            if (basic)
                basic.id = this.__brandObj.basic.id;
            return basic;
        };

        _pro.__onSaveBtn = function (_event) {
            var _obj = this.__getReqParam();
            if (_obj) {
                _j._$request("/brand/save", {
                    headers: {"Content-Type": "application/json;charset=UTF-8"},
                    method: 'put',
                    type: "json",
                    data: JSON.stringify(_obj),
                    onload: function (_data) {
                        if (_data) {
                            notify.notify({
                                type: "success",
                                message: "保存成功"
                            });
                        }

                    },
                    onerror: function (_error) {
                        console.log(_error);
                    }

                });
            }

        };

        _pro.__onSubmitBtn = function (_event) {
            var _obj = this.__getReqParam();
            if (_obj) {
                _j._$request("/brand/submit", {
                    headers: {"Content-Type": "application/json;charset=UTF-8"},
                    method: 'put',
                    type: "json",
                    data: JSON.stringify(_obj),
                    onload: function (_data) {
                        if (_data) {
                            notify.notify({
                                type: "success",
                                message: "提交成功"
                            });
                            setTimeout(function () {
                                window.location = "/brand/display";
                            }, 1000);
                        }

                    },
                    onerror: function (_error) {
                        notify.notify({
                            type: "error",
                            message: _error.message
                        });
                    }

                });
            }

        };

       _pro.__onPreviewBtn = function (_event) {
           var _obj = this.__brandObj.basic.id, that = this;
           _j._$request("/brand/preview", {
               headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
               method: 'get',
               data:{t:+new Date},
               query: "id=" + _obj,
               sync: true,
               onload: function (_data) {
                   if (_data) {
                       var _obj=JSON.parse(_data);
                       var basic=that.__getReqParam();
                       _.extend(_obj.basic,that.__getReqParam(),true);
                       _.extend(_obj.shops,that.__table.getPreDataList(),true)
                       var pform = $(".j-2form")[0];
                       pform.action = config.MAINSITE + "/mainbrand/backend2";
                       pform.data.value = JSON.stringify(_obj);
                       pform.submit();
                   }
               },
               onerror: function (_error) {
                   notify.notify({type: "error", message: _error.message});
               }

           });
       };

        _pro.__destroy = function () {
            this.__super();
            _f._$$WebForm._$recycle(this.__form);
            delete this.__form;
        };

        p._$$BrandEditModule._$allocate();
    });