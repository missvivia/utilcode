/*
 * ------------------------------------------
 * 详情页面模块实现文件
 * @version  1.0
 * @author   hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'pro/widget/gesture/gesture',
    'util/chain/chainable',
    'util/query/query',
    'pro/components/imgbox/imgbox',
    'pro/components/notify/notify',
    'pro/extend/config',
    'pro/extend/util',
    'pro/extend/request',
    'base/element',
    'pro/components/product/helper.view',
    'pro/page/detail/detailcount',
    'pro/page/detail/numcount',
    'pro/page/detail/sizepicker',
    'base/klass',
    'pro/widget/module',
    'pro/widget/slide/slide'
],function(
    gesturify,
    $,
    e1,
    ImgBox,  // 图片放大Gallery组件
    notify,
    config, 
    _,
    request, 
    e, 
    HelperView, 
    DetailCount, 
    NumCount, 
    SizePicker, 
    _k,
    _m,
    _slide, _p,_o,_f,_r,_pro){


    var url = {
        // 添加到购物车
        "CART": "/cart/addToCart",
        "LIKE": "/brand/follow",
        "UN_LIKE": "/brand/unfollow",
        "CHECK_SKU_CART": "/cart/checkSkuNum"
    }

    var statusMap = {
        1: "放入购物袋",
        2: "抢购已结束",
        3: "敬请期待",
        4: "已售罄", 
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
        this.data.disable = 
            this.data.notAllowed === true || // 未被允许地区
            this.data.isSnapshot === true ;
        this.__super(_options);
        this.__doInitState();
        this.__initDom();
        this.__initComponent();
        this.__initEvent();
    };


    _pro.__initDom = function(){
        var data = this.data;
        var list = e1._$one('.j-slide .j-node');
        this.$msgbox = $(".m-msgbox");

        _slide._$$Slide._$allocate({
         slideBox: list,
         iconBox: e1._$one('.m-pointer .j-node'),
         selected:'z-active',
         width:document.body.scrollWidth,
         duration:500,
         stop:5000,
         manualSlide:true
        });
        this.$list = $(list);
        var imgs = this.$list._$children("img", true)._$map(function(node){
            return node.src;
        })

        gesturify(nes.one(".m-tparam")).addEventListener("tap", function(){
        })
        if(!data.notAllowed){
            this.$list._$on({
                "click img": function  (ev) {
                    ev.preventDefault();
                    var current = this.getAttribute("src");
                    var index = imgs.indexOf(current)
                    if(index === -1) return;
                    var imgbox = new ImgBox({
                        data: {
                            index: index,
                            slides: imgs
                        },
                        events: {
                            "$inject": function(){
                                nes.one(".g-box").style.display = "none"
                            },
                            "$destroy": function(){
                                nes.one(".g-box").style.display = ""
                            }
                        }
                    });
                }
            })
        }


    }
    // 当用户登录时
    _pro.__doInitState = function(){
        var data = this.data;
        var isLogin = this.data.isLogin = _.isLogin();
    }
    // 初始化一些分立的组件
    _pro.__initComponent = function(){
        var data = this.data;
        var product = data.product;

        if(data.disable){
           return; 
        }
        // // 倒计时插件
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
            }).$inject(".j-count");
        }

        // // 数量统计插件
        var numcount = this.__numcount = new NumCount({
            data: {},
            events: {
                // 抛出的需要剩余值
                "need_remain": function(){
                    notify.notify("必须选择尺码！", "error")
                }
            }
        }).$inject(".j-numcount")



        // // 尺码部分
        var replaceSize = nes.one(".j-size");
        // // 尺码选择
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
        }).$inject(replaceSize);

        size.$watch('selected && selected.num', function(num){
            numcount.setRemain(num);
        })



        // 初始化尺码助手控件
        if(data.product.helper && e1._$one(".j-helper")){
            data.product.helper.preview = 1;
            new HelperView({
                data: data.product.helper 
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

        $(".j-cart")
            ._$on("click", this.__onCart._$bind(this));


    }

    /**
     * 档期结束
     * @return {[type]} [description]
     */
    _pro.__onTimeEnd = function(){
        this._$disable(2)

    }

    /**
     * 放入购物车回调
     * @param  {[type]} ev [description]
     * @return {[type]}    [description]
     */
    _pro.__onCart = function(ev){
        ev.preventDefault();
        if(e._$hasClassName(ev.target, 'z-disabled')) return;

        if(!this.data.isLogin){
            notify.notify("请先登录", "warning")
            return setTimeout(function(){
                location.href = "/login?redirect=" + encodeURI(location.href);
            },1500)
             
        }
        var count = this.__numcount.data.count;
        var selected = this.__size.data.selected
        if(!selected) {
            return notify.notify("请选择尺码!", "fail")
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
                // 完了我们再同步一次库存
                self._$addToCart();
                // 统计
                try{
                    window['_smq'].push(['custom','加入购物车', product.productName,selected.skuId,product.salePrice * count]);
                }catch(ex){}
                notify.notify("已加入购物袋", "success")
                self.__size.checkSize(function(err,data){});
            },


            onerror: function(json){
                var error = "加入购物车失败";
                switch(json.code){
                    case 403: 
                        error = "很抱歉，购物袋最多放10款商品";
                        break;
                    case 402: 
                        error = "很抱歉，抢购已过期";
                        break;
                    case 401: 
                        error = "很抱歉，该商品库存不足";
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
        // TODO
        _.tpopup.show(
            "<div class='p-pop'>" +
            "<h3>为您保留商品20分钟</h3>"+
            "<a class='u-cbtn u-cbtn-2' href='/cart'>去购物袋</a>" +
            "</div>"
            , {duration: 6000})


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
            ._$addClassName("z-disabled")
            ._$text(statusMap[status]);

        if(status == 4){
            $(".j-flag")._$style("display", "");
        }

        // countdown 移除
        if(status < 4){
            if(this.__countdown){
                this.__countdown.destroy();
            }
            // size 禁止
            this.__size.disable(status);
        }

        if(status === 2){
            this._$showmsg("对不起，该抢购活动已结束");
        }
        // size 计数器禁止
        this.__numcount.disable(status);
    }

    _pro._$showmsg = function(msg){
       if(typeof msg === "number") msg = statusMap[msg]; 
            this.$msgbox
                ._$html(msg)
                ._$addClassName("z-active")
    }

    // init page
    _p._$$Module._$allocate();
});


