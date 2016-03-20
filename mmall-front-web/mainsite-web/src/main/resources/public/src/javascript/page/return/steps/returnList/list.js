/**
 * 基于NEJ和bootstrap的日期选择器
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
    "pro/extend/util",
    'text!./list2.html',
    "pro/widget/BaseComponent",
    'pro/components/amountpick/amountpick',
    'base/util',
    'util/chain/chainable',
    'util/es/array',
    'base/event'
], function (_, _html, BaseComponent, AmountPick, _u, $, _v) {

    var ListComponent = BaseComponent.extend({
        template: _html,
        computed: {
            "checkAll": {
                set: function (value, data) {
                    data.list.forEach(function (item) {
                        return item.checked = value;
                    })
                },
                get: function (data) {
                    var self = this;
                    if (!data.list) return false
                    return data.list.filter(function (item) {
                        return item.checked == true && self.checkStatus(item);
                    }).length === data.list.filter(this.checkStatus).length;
                }
            },
            checkedList: function (data) {
                var self = this;
                return data.list.filter(function (item) {
                    return item.checked == true && self.checkStatus(item);
                })
            }
        },
        init: function () {
            var self = this;
            this.supr();
            this.$watch("checkedList", function (arr, splice) {
                setTimeout(function () {
                    self.$emit("sendQequest");
                }, 0);
            })
        },
        setData: function (_list) {
            this.data.list = _list || [];
            this.$update();
        },
        checkStatus: function (item) {
            return item.status.canReturn;
        },
        increase: function (item) {
            if (item.quantity < item.maxQuantity)
                item.quantity++;
            this.calSum(item);
        },
        onAmountChange: function ($event,item) {
            this.calSum(item);
        },
        decrease: function (item) {
            if (item.quantity > 1)
                item.quantity--;
            this.calSum(item);

        },
        calSum: function (item) {
            item.sum = (item.quantity * item.salePrice).toFixed(2);
            this.$emit("sendQequest");
        },
        checkValidity: function () {
            var self = this;
            var checkedList = this.data.list.filter(function (item) {
                return item.checked && self.checkStatus(item)
            });
            var result = checkedList.every(function (item) {
                if (!item.comment) {
                    item.showNoCommentError = true;
                }
                return item.comment;
            });
            this.$update();
            return checkedList.length && result ? true : false;
        },
        selectChange: function (item) {
            if (item.comment)
                item.showNoCommentError = false;
            else
                item.showNoCommentError = true;
            this.$emit("sendQequest");
            this.$update();
        },
        getRequestParam: function () {
            var self = this;
            self.requestParams = [];
            this.data.list.filter(function (item) {
                return item.checked && self.checkStatus(item)
            }).forEach(function (item) {
                    self.requestParams.push({"productid": item.id, "quantity": item.quantity, "comment": item.comment})
                });

            return self.requestParams;
        }
    }).directive('r-init', function (elem, value) {
            this.$get(value);
        });


    return ListComponent;

})