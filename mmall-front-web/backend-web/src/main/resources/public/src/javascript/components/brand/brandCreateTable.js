/**
 * 表单字段生成组件
 */

define([
    "{pro}extend/util.js",
    "text!./brandCreateTable.html",
    "{pro}widget/BaseComponent.js",
    "{pro}widget/dialog/shopwindow/shopwindow.js",
    "{pro}widget/dialog/shopwindow/shopviewwindow.js",
    "{pro}widget/dialog/shopwindow/shopeditwindow.js"
], function (_, tpl, BaseComponent, ShopWindow,ShopViewWindow,ShopEditWindow) {

    var SizeList = BaseComponent.extend({
        template: tpl,
        add: function (data) {
            if (!this.data.list) {
                this.data.list = [];
            }
            data.status = {
                intValue: 1,
                desc: "新建"
            };
            this.data.list.push(data);
            this.$update();
        },
        active: function (tpl) {
            tpl.status = {
                intValue: 2,
                desc: "已启用"
            };
            this.$update();
        },
        deactive: function (tpl) {
            tpl.status = {
                intValue: 0,
                desc: "已停用"
            };
            this.$update();
        },
        edit: function (tpl) {
            var that = this;
            if(this.__editDialog)
            	ShopEditWindow._$recycle();
            this.__editDialog = ShopEditWindow._$allocate({data: tpl,onOkCallback:this.onEditCallback._$bind(this,tpl)})._$show();
        },
        onEditCallback:function(originData,_data){
        	_.extend(originData, _data, true);
        	this.$update();
        },
        view: function (tpl) {
            if(this.__viewdialog)
            	ShopViewWindow._$recycle();
            this.__viewdialog = ShopViewWindow._$allocate({data: tpl})._$show();
        },
        showDialog: function () {
        	var that = this;
            if(this.__dialog)
            	ShopWindow._$recycle();
            this.__dialog = ShopWindow._$allocate({onOkCallback:function(_data){
            	that.add(_data);
            }})._$show();
        },
        getDataList: function () {
            var list = this.data.list || [];
            return list;
        },
        del: function (tpl) {
            var index = this.data.list.indexOf(tpl);
            if (index != -1) {
                this.data.list.splice(index, 1);
                this.$update();
            }
        }
    });

    return SizeList;


})


