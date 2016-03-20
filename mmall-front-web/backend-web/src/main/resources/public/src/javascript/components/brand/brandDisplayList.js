define([
    "{pro}extend/util.js",
    "text!./brandDisplayList.html",
    "{pro}components/ListComponent.js",
    'pro/extend/config',
    'util/ajax/xdr',
    "pro/components/notify/notify"
], function (_, tpl, ListComponent, config, _j, notify) {


    var SizeList = ListComponent.extend({
        url: "/brand/list",
        // url:"/src/javascript/components/brand/brandDisplayList.json",
        name: "m-sizelist",
        template: tpl,
        data: {
            limit: 15
        },
        preview: function (tpl) {
            var that = this;
            var _obj = "id=" + tpl.id;
            _j._$request("/brand/preview", {
                headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                method: 'get',
                data:{t:+new Date},
                query: _obj,
                sync: true,
                onload: function (_data) {
                    if (_data) {
                        that.post2Main(_data);
                    }
                },
                onerror: function (_error) {
                    notify.notify({type: "error", message: _error.message});
                }

            });
        },
        post2Main: function (_data) {
            var pform = this.$refs.pform;
            pform.action = config.MAINSITE + "/mainbrand/backend2";
            pform.data.value = _data;
            pform.submit();
        },
        online: function (tpl) {
            var that = this;
            var _obj = "id=" + tpl.id;
            _j._$request("/brand/online", {
                headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                method: 'post',
                data: _obj,
                timeout: 5000,
                onload: function (_data) {
                    if (_data) {
                        var _obj = JSON.parse(_data)
                        _obj.id == -1 ? notify.notify({type: "error", message: "已经有介绍页上线，请先下线"}) : _.extend(tpl, _obj, true);
                        that.$update();
                    }
                },
                onerror: function (_error) {
                    notify.notify({type: "error", message: _error.message});
                }

            });
        },
        offline: function (tpl) {
            var that = this;
            var _obj = "id=" + tpl.id;
            _j._$request("/brand/offline", {
                headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                method: 'post',
                data: _obj,
                timeout: 5000,
                onload: function (_data) {
                    if (_data) {
                        var _obj = JSON.parse(_data)
                        _.extend(tpl, _obj, true);
                        that.$update();
                    }
                },
                onerror: function (_error) {
                    notify.notify({type: "error", message: _error.message});
                }

            });

        },
        audit: function (tpl) {
            var that = this;
            var _obj = "id=" + tpl.id;
            _j._$request("/brand/audit", {
                headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                method: 'post',
                data: _obj,
                norest: true,
                onload: function (_data) {
                    if (_data) {
                        var _obj = JSON.parse(_data)
                        _.extend(tpl, _obj, true);
                        that.$update();
                    }
                },
                onerror: function (_error) {
                    notify.notify({type: "error", message: _error.message});
                }

            });
        },
        copy: function (tpl) {
            var that = this;
            var _obj = "id=" + tpl.id;
            _j._$request("/brand/copy", {
                headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                method: 'post',
                data: _obj,
                norest: true,
                timeout: 5000,
                onload: function (_data) {
                    if (_data) {
                        var _obj = JSON.parse(_data)
                        that.data.list.push(_obj);
                        that.data.total++;
                        that.$update();
                    }

                },
                onerror: function (_error) {
                    notify.notify({type: "error", message: _error.message});
                }

            });

        },
        del: function (tpl) {
            var that = this;
            var _obj = "id=" + tpl.id;
            _j._$request("/brand/del", {
                headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
                method: 'post',
                data: _obj,
                onload: function (_data) {
                    if (_data) {
                        var i = that.data.list.indexOf(tpl);
                        if (i != -1) {
                            that.data.list.splice(i, 1);
                            that.$update();
                        }
                    }

                },
                onerror: function (_error) {
                    notify.notify({type: "error", message: _error.message});
                }

            });
        },
        __getList: function () {
            this.supr();
            window.scrollTo(0, 0);
        }

    });

    return SizeList;


})


// NULL(-1, "NULL"),
//   /**
//    * 新建状态
//    */
//   BRAND_NEW(0, "品牌新建状态"),
//   /**
//    * 审核中
//    */
//   BRAND_AUDITING(1, "审核中"),

//   BRAND_AUDITPASSED_UNUSED(2, "核审通过-暂未使用"),

//   BRAND_AUDITPASSED_USING(3, "审核通过-使用中"),

//   BRAND_AUDITREFUSED(4, "审核拒绝"),

//   BRAND_CANCELED(5, "已作废"),

//@TODO: remove

// [{
//   id:1,
//   status:{
//     type:1,
//     desc:"通过审核"
//   },
//   imageUrl:"/res/images/cut.jpg",
//   updateTime:"//这里用date格式",
//   editor:"oushutian",
//   intro:"与时尚紧密接轨的年轻女装品牌，在淘宝立足不久...."
// }]


// var timestar=new Date;
// var timeend=new Date;
// var lists = [];
// for(var i = 0; i< 105 ;i++){
//   lists.push({
//     id: i,
//     status:{type:i,desc:"通过审核"},
//     imageUrl:"/res/images/cut.jpg",
//     updateTime:timestar,
//     editor:"oushutian",
//     intro:"与时尚紧密接轨的年轻女装品牌，在淘宝立足不久即登上了电子商务大舞台。妖精的口袋专注于打造独立、自信、优雅、小资以及小暧昧的品牌形象，"
//   })
// }
