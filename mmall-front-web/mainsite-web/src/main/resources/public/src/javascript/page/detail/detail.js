/*
 * ------------------------------------------
 * 详情页面模块实现文件
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'pro/widget/util/fav',
    'pro/extend/config',
    'pro/extend/util',
    'pro/widget/layer/login/login',
    'pro/extend/request',
    'base/element',
    'pro/widget/ui/fly/fly',
    'pro/widget/tooltip/tooltip.wrap',
    'pro/components/product/helper.view',
    'pro/components/product/slider',
    'pro/components/notify/notify',
    'pro/components/scrollspy/scrollspy',
    'pro/components/window/base',
    'pro/components/magnifier/magnifier',
    'pro/page/detail/detailcount',
    'pro/page/detail/numcount',
    'pro/page/detail/sizepicker',
    'util/chain/chainable',
    'base/klass',
    'pro/widget/module'
],function(fav, config, _, lt ,request, e, t1 ,t , HelperView, Slider ,notify, ScrollSpy, Modal , Magifier, DetailCount, NumCount, SizePicker, $, _k,_m,_p,_o,_f,_r,_pro){

    var url = {
        // 添加到购物车
        "CART": "/cart/addToCart",
        "LIKE": "/brand/follow",
        "UN_LIKE": "/brand/unfollow",
        "CHECK_SKU_CART": "/cart/checkSkuNum"
    }

    var statusMap = {
        1: "放入购物袋",
        2: "抢购已过期",
        3: "活动未开始",
        4: "已抢光",
        5: "放入购物袋"
    }

    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 控件重置
     * @param  {Object} 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){

        this.data = window.__data__ || {};
        this.__super(_options);
        this.__initDom();
        this.__initComponent();
        this.__initEvent();
    };


    _pro.__initDom = function(){

        this.$thumb = $('.j-thumb');
        this.$sthumbList = $('.j-sthumbs li');
        this.$followBtn = $(".j-follow");
        this.__doInitState();

    }
    // 当用户登录时
    _pro.__doInitState = function(){
        var data = this.data;
        var isLogin = this.data.isLogin = _.isLogin();
        this.$follow = $(".j-follow");
        var product = this.data.product;
        var self = this;
        if(isLogin && product.brand){
            request("/brand/state", {
                data: {id: product.brand.id},
                onload: function(json){
                    if(json.code===200){
                        self.$follow ._$style("display", "")
                        self.__toggleFollow(json.result)

                    }

                }
            })
        }else{
            self.$follow ._$style("display", "")
        }

        //如果登录了 就显示客服联系方式
        var userName = _.getFullUserName();
        var dom = Regular.dom;
        // if(isLogin && userName){
        //     var account = nes.one(".j-account");
        //     if(account){
        //         dom.on(account, 'click', _._$openKefuWin);
        //     }
        //     account.style.display = "";
        // }

        // this.$follow = $(".j-follow")._$style("display", "");
    }
    // 初始化一些分立的组件
    _pro.__initComponent = function(){
        var data = this.data;
        var product = data.product;
        var self = this;

        // 初始化slider
        this.__slider = new Slider({
            parent: nes.one(".m-detail")
        })
        // 初始化放大镜
        this.__magnifier = new Magifier({
            thumb: this.$thumb[0],
            data: {
                direct: 'right',
                ratio: 80 / 46
            }
        })

        // scroll spy
        // 只有非ie6启动 scrollspy 。因为ie6不支持fixed
        if(!Regular.dom.msie || Regular.dom.msie > 6){
            // 滚动切换的tab
            this.__scrollspy = new ScrollSpy({
                data:{
                    elem: nes.one(".j-spy")
                },
                events:{
                    "fix": function(flag){

                        if(flag) nes.one(".p-dbody").style.paddingTop="52px";
                        else nes.one(".p-dbody").style.paddingTop="0px";
                    }
                }
            })
        }


        if(data.isSnapshot){
           return;
        }
        // 倒计时插件
        this.__countdown = null;
        if(data.product.status == 1){
            this.__countdown = new DetailCount({
                data: {
                    scheduleTime: product.schedule && product.schedule.poCountDownTime ||0,
                    startTime: product.schedule && product.schedule.startTime ||0,
                    status: product.status || 1
                },
                events: {
                    "end": this.__onTimeEnd._$bind(this)
                }
            }).$inject(".j-count", "after");
        }

        // 数量统计插件
        var numcount = this.__numcount = new NumCount({
            data: {},
            events: {
                // 抛出的需要剩余值
                "need_remain": function(){
                    size.toggleError("请选择尺寸！")
                }
            }
        }).$inject(".j-numcount")


        // 尺码部分
        var replaceSize = nes.one(".j-sizes");
        // 尺码选择
        var size = this.__size = new SizePicker({
            data: {
                id: data.product.productId,
                skuId: data.product.skuId,
                sizes: data.product.sizeSpecList || [],
                scheduleId: data.product.schedule && data.product.schedule.id,
                disable: (this.data.product.preview || this.data.product.status!==1)? 1:0
            },
            events: {
                // 默认选择
                "selected": function(size){
                    if(size) numcount.setRemain(size.num);
                },
                //没货有两种情况
                //一. 是有机会的没货
                //二. 是完全没货
                "empty": function(type){
                    type = type || 4;
                    if(product.status === 1) self._$disable(type)
                }
            }
        }).$inject(replaceSize, 'after');
        size.$watch('selected && selected.num', function(num){
            numcount.setRemain(num);
        })

        Regular.dom.remove(replaceSize);

        // 添加标签的hover效果
        nes.all(".j-tags .tag").forEach(function(tag){
            t._$$ToolTipWrap._$allocate({
                element: tag
            })
        })
        // 初始化尺码助手控件
        if(data.product.helper && nes.one(".j-helper")){
            data.product.helper.preview = 1;
            new HelperView({
                data: data.product.helper
                //     || {
                //     "preview": true,
                //     "id":7331,
                //     "supplierId":0,
                //     "name":"大白兔模板",
                //     "haxis":{
                //         "name":"体重kg",
                //         "list":[45.0,47.5,50.0,52.5,55.0,57.5,60.0,62.5,65.0,67.5,70.0,72.5,75.0,77.5,80.0,82.5,85.0,87.5,90.0]
                //     },
                //     "vaxis":{
                //         "name":"身高cm",
                //         "list":[155.0,160.0,165.0,170.0,175.0,180.0,185.0,190.0]
                //     },
                //     "body":[
                //     ["m","m","m","m","m","m","m","m","m","m","m","l","k","k","m","","","",""],
                //     ["","l","l","l","l","l","l","l","l","l","l","l","k","k","m","","","",""],
                //     ["","k","k","k","k","k","k","k","k","k","k","k","k","k","m","","","",""],
                //     ["n","n","n","n","n","n","n","n","n","n","n","n","n","n","n","","","",""],
                //     ["","","","","","","","","","","","","","","","","","",""],
                //     ["","","","","","","","","","","","","","","","","","",""],
                //     ["","","","","","","","","","","","","","","","","","",""],
                //     ["","","","","","","","","","","","","","","","","","",""]]
                // }
            }).$inject(".j-helper");
        }

        // 如果状态不是1， 当然要禁止所有操作
        if(data.product.status!==1){
            this._$disable(data.product.status)
        }

    }


    /**
     * 初始化事件
     * @return {[type]} [description]
     */
    _pro.__initEvent = function(){

        var $sthumbList = this.$sthumbList;
        var $thumb = this.$thumb;
        // 初始化图片点击切换代理事件
        this.$sthumbList._$on({
            "mouseover": function(){
                $sthumbList
                    ._$delClassName("z-active");

                $(this)
                    ._$addClassName("z-active");

                var url = nes.one('img', this).src;
                url = url.split("?")[0] + "?imageView&quality=90&thumbnail="+ 460  +"x"+ 460
                $thumb._$attr("src", url);
            }
        })



        this.$follow
            ._$on("click", this.__onFollow._$bind(this));
        $(".j-cart")
            ._$on("click", this.__onCart._$bind(this));
    }

    /**
     * 档期结束
     * @return {[type]} [description]
     */
    _pro.__onTimeEnd = function(){

        this._$disable(2,"抢购已过期")

    }

    _pro.__toggleFollow = function(sign){
        this.data.isLiked = sign;

        if(sign){
            this.$follow
                ._$addClassName("active")
                ._$children('span')._$text("取消关注");
            this.$follow._$children("i")._$html("&#xe606;")
        }else{
            this.$follow
                ._$delClassName("active")
                ._$children('span')._$text("关注品牌");
            this.$follow._$children("i")._$html("&#xe607;")
        }
    }

    /**
     * follow回调
     * @param  {[type]} ev [description]
     * @return {[type]}    [description]
     */
    _pro.__onFollow = function(ev){
        if(!this.data.isLogin){
            return this.__doLogin();
        }
        ev.preventDefault();

        var self = this;
        var data = this.data;
        var brand = data.product.brand;
        var $follow = this.$follow;
        request(data.isLiked? url.UN_LIKE: url.LIKE, {
            method: "post",
            data: {
                brandId: brand.id
            },
            onload: function(){
                self.__toggleFollow(!data.isLiked)

                if(data.isLiked){
                    // 加入fav效果
                    // new Modal({
                    //     content: "<h2><span class='u-success'></span>关注成功</h2>"+
                    //         "<p>将为您推送相关品牌折扣信息</p>",
                    //     data: {
                    //         clazz: "u-win-follow",
                    //         confirmText: "确 定",
                    //         autofix:false
                    //     }
                    // })

                }else{
                }

            },
            onerror: function(){
                notify.notify({
                    type: "error",
                    message: data.isLiked? "取消关注失败": "关注失败"
                })
            }
        })

    }

    _pro.__doLogin = function(){
        var _win = lt._$$LoginWindow._$allocate({
             parent:document.body,
             redirectURL:window.location.href
        })._$show();
        return;
    }

    /**
     * 放入购物车回调
     * @param  {[type]} ev [description]
     * @return {[type]}    [description]
     */
    _pro.__onCart = function(ev){
        ev.preventDefault();
        if(e._$hasClassName(ev.target, 'u-btn-dis')) return;

        if(!this.data.isLogin){
            return this.__doLogin();
        }
        var count = this.__numcount.data.count;
        var selected = this.__size.data.selected;

        if(!selected) {
            return this.__size.toggleError("请选择尺寸！")
        }
        var self = this;
        var sizepicker = this.__size;


        this.__size.checkSize(function(err, data){
            if(err) return notify.notify({
                type: "error",
                message: "检查库存失败，请稍后"
            });
            var selected = data.selected;
            if(selected && count>=1){
                // 检查购物车
                self.__checkCart(selected.skuId, function(num){
                    //当超过2件
                    if(count + num > config.MAX_PICK){
                        return notify.notify("每款商品同尺码限购2件")
                    }
                    //当超过库存
                    if(num > selected.num){
                        return notify.notify("很抱歉，商品库存不足");
                    }
                    // 最后才是成功
                    self.__doAddToCart(selected, count, ev.target);
                })
            }
            if(!selected){
                // 我们需要更新selected状态
            }
        })
    }

    // 放入购物车操作
    _pro.__doAddToCart = function(selected, count, node){
        var self = this;
        var product = self.data.product;
        request(url.CART, {
            method: "post",
            data: {
                diff: count,
                skuId: selected.skuId
            },
            onload: function(json){
                self._$addToCart(node, nes.one('.u-sel-color-selected img'));
                // 统计
                try{
                    window['_smq'].push(['custom','加入购物车', product.productName,selected.skuId,product.salePrice * count]);
                }catch(ex){}
                // 完了我们再同步一次库存
                self.__size.checkSize(function(err,data){

                });


            },
            onerror: function(json){
                var error = "添加购物车失败";
                switch(json.code){
                    case 403:
                        error = "购物袋最多只能放10款商品";
                        break;
                    case 402: 
                        error = "很抱歉，抢购已过期";
                        break;
                    case 401:
                        error = "很抱歉，商品库存不足";
                        break;
                }
                notify.notify({
                    type: "error",
                    message: error
                })
            }
        })
    }
    /**
     * 添加到指定截点
     * @param  {[type]} node [description]
     * @return {[type]}      [description]
     */
    _pro._$addToCart = function(node, img){
        t1._$$Flyer._$allocate({
          startNode: node,
          destNode: $('#sidebar-carticon')[0],
          endSize: {x: 10, y: 10},
          parent: document.body,
          imgUrl: img && img.src,
          oncomplete: function(){
            if(!!this.__sidebar)
              this.__sidebar._$showLayer('cart');
          }._$bind(this)
        })._$show();
    }

    /**
     * 检查购物车数量
     * @param  {[type]}   skuId    skuId
     * @param  {Function} callback 回调函数
     * @return {[type]}            [description]
     */
    _pro.__checkCart = function(skuId, callback){
        request(url.CHECK_SKU_CART, {
            data: {skuId: skuId},
            onload: function(json){
                callback(json.result || 0);
            },
            onerror: function(){
                callback(0);
            }
        })
    }

    /**
     * disable所有的组件，即表单项需要实现一个disable方法
     * @return {[type]} [description]
     */
    _pro._$disable = function(status){
        //将两个购物车按钮修改为不可用
        $(".j-cart")
            ._$addClassName("u-btn-dis")
            ._$children("span")
            ._$text(statusMap[status]);

        // countdown 移除
        if(status < 4){
            if(this.__countdown){
                this.__countdown.destroy();
            }
            // size 禁止
            this.__size.disable(status);
        }
        // size 计数器禁止
        this.__numcount.disable(status);
    }

    // init page
    _p._$$Module._$allocate();
});


