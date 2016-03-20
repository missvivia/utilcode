/**
 * 表单字段生成组件
 */

define([
    "{pro}extend/util.js",
    "text!./brandEditTable.html",
    "{pro}components/brand/brandCreateTable.js",
    '{lib}util/ajax/xdr.js',
    "pro/components/notify/notify"
], function(_, tpl, BaseComponent,_j,notify) {

    var SizeList = BaseComponent.extend({
        name: "m-sizelist",
        template: tpl,
        config: function(data) {
            _.extend(data, {
                total: 1,
                current: 1,
                limit: 10,
                list: [],
                supplierBrandId: 1
            });
        },
        active: function(tpl) {
            var that = this;
            var _obj = "id="+tpl.brandShopId;
            _j._$request("/brand/shop/active", {
                headers:{"Content-Type":"application/x-www-form-urlencoded;charset=UTF-8"},
                method:'post',
                data:_obj,
                onload:function(_data) {
                    if(!!_data)
                    {
                        var obj = JSON.parse(_data);
                        _.extend(tpl,obj,true);
                        that.$update();
                    }
                    
                },
                onerror:function(_error) {
                	notify.notify({type:"error",message:_error.message});
                }
            });

        },
        add:function(data) {
            var that = this;
            var _obj = data;
            var supermethod = that.supr;
            _obj['supplierBrandId'] = this.data.supplierBrandId;
            _j._$request("/brand/shop/add", {
                headers: {
                    "Content-Type": "application/json;charset=UTF-8"
                },
                method:'put',
                type:"json",
                data:JSON.stringify(_obj),
                onload:function(_data) {
                    if(!!_data)
                    supermethod.call(that,_data);
                },
                onerror:function(_error) {
                	notify.notify({type:"error",message:_error.message});
                }
            });

        },
        onEditCallback:function(originData,_omdata){
            var that= this;
            _omdata['supplierBrandId'] = this.data.supplierBrandId;
            _j._$request("/brand/shop/edit", {
                headers: {
                    "Content-Type": "application/json;charset=UTF-8"
                },
                method:'post',
                data:JSON.stringify(_omdata),
                type:"json",
                onload:function(_data) {
                    if(!!_data)
                    {
                        _.extend(originData,_data,true);
                        that.$update();
                    }
                },
                onerror:function(_error) {
                	notify.notify({type:"error",message:_error.message});
                }
            });
        },
        getPreDataList: function () {
            var prelist = this.data.list || [];
            if (prelist&&prelist.length) {
                 prelist=prelist.filter(function(item){
                    return item.status.intValue==2;
                });
                prelist.forEach(function(entity){
                        delete entity.cardAddress;
                });
            }
            return prelist;
        },
        stop: function(tpl) {
            var that = this;
            var _obj = "id="+tpl.brandShopId;
            _j._$request("/brand/shop/stop", {
                headers:{"Content-Type":"application/x-www-form-urlencoded;charset=UTF-8"},
                method:'post',
                data:_obj,
                onload:function(_data) {
                    if(!!_data)
                    {
                        var obj = JSON.parse(_data);
                        _.extend(tpl,obj,true);
                        that.$update();
                    }
                    
                },
                onerror:function(_error) {
                	notify.notify({type:"error",message:_error.message});
                }
            });

        },
        del: function(tpl) {
            var that = this;
            var supermethod = that.supr;
             var _obj = "id="+tpl.brandShopId;
            _j._$request("/brand/shop/del", {
                headers:{"Content-Type":"application/x-www-form-urlencoded;charset=UTF-8"},
                method:'post',
                data:_obj,
                onload:function(_data) {
                    if(!!_data)
                    {
                        supermethod.call(that,tpl);
                    }
                    
                },
                onerror:function(_error) {
                    console.log(_error);
                }
            });
        },
        view: function(tpl) {
            this.supr(tpl);
        }
    });

    return SizeList;



})
