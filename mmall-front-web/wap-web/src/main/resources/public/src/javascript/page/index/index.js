/*
 * ------------------------------------------
 * 主站首页实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'pro/widget/module',
    'pro/widget/slide/slide',
    'pro/components/countdown/countdown',
    'pro/components/schedule/scheduleList'
],function(_k,_e,_v,_u,_w,_slide,_countdown,_pl,_p,_o,_f,_r){
    var _pro;

    _p._$$Index = _k._$klass();
    _pro = _p._$$Index._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
        this.__super();
        // 0:slide的父容器，做translate3d用
        // 1:图片切换后的小图标展示，没实际作用
        this.__nodes = _e._$getByClassName(document,'j-node');
        this.__bcds = _e._$getByClassName(document,'j-bcd');
        _slide._$$Slide._$allocate({
          slideBox:this.__nodes[0],
          iconBox:this.__nodes[1],
          selected:'z-active',
          width:document.body.scrollWidth,
          duration:300,
          stop:5000
        });
        // window["g_return"] = {}
        // window["g_return"].polist = [
        //     {"banner":{"scheduleId":"1","homeBannerImgUrl":"http://paopao.nos.netease.com/416c377b-8cd5-4170-9a88-7c4e817bf4c8"},
        //     "prdDetail":{"productTotalCnt":10,"minDiscount":1.5},
        //     "brandId":"1","brandLogo":"http://paopao.nos.netease.com/83706237-28e3-41a8-b12e-771ce75d8791","title":"title","prdDetail":{"minDiscount":9000},"startTime":190000,"endTime":1419268094417
        //     },
        //     {"banner":{"scheduleId":"2","homeBannerImgUrl":"http://paopao.nos.netease.com/416c377b-8cd5-4170-9a88-7c4e817bf4c8"},
        //     "prdDetail":{"productTotalCnt":10,"minDiscount":1.5},
        //     "brandId":"2","brandLogo":"http://paopao.nos.netease.com/83706237-28e3-41a8-b12e-771ce75d8791","title":"title","prdDetail":{"minDiscount":9000},"startTime":190000,"endTime":1419268094417
        //     },
        //     {"banner":{"scheduleId":"3","homeBannerImgUrl":"http://paopao.nos.netease.com/416c377b-8cd5-4170-9a88-7c4e817bf4c8"},
        //     "prdDetail":{"productTotalCnt":10,"minDiscount":1.5},
        //     "brandId":"3","brandLogo":"http://paopao.nos.netease.com/83706237-28e3-41a8-b12e-771ce75d8791","title":"title","prdDetail":{"minDiscount":9000},"startTime":190000,"endTime":1419268094417
        //     },
        //     {"banner":{"scheduleId":"4","homeBannerImgUrl":"http://paopao.nos.netease.com/416c377b-8cd5-4170-9a88-7c4e817bf4c8"},
        //     "prdDetail":{"productTotalCnt":10,"minDiscount":1.5},
        //     "brandId":"4","brandLogo":"http://paopao.nos.netease.com/83706237-28e3-41a8-b12e-771ce75d8791","title":"title","prdDetail":{"minDiscount":9000},"startTime":190000,"endTime":1419268094417
        //     }
        // ]
        // this.__poList=_pl._$$ScheduleList._$allocate({
        //     parent:'po-list-box',
        //     limit:1,
        //     delta:1,
        //     list:window["g_return"].polist,
        //     lkey:"scedule-list-data"
        // })
        // 最新品牌倒计时
        _u._$forEach(this.__bcds,function(_node){
            var _time = _e._$dataset(_node,'countdown');
            var _ct = new _countdown({
                data:{
                    content:'',
                    time:_time,
                    updatetime:60000,
                    onchange:function(_opt){
                        if (_opt.isdown){
                            var _pd = _node.parentNode.parentNode.parentNode.parentNode;
                            _e._$addClassName(_pd,'f-dn');
                        }else{
                            if (_opt.meta.dd == '00' && _opt.meta.HH == '00' && _opt.meta.mm == '00'){
                                var _s = _opt.meta.ss,
                                    _ss = parseInt(_s,10);
                                _node.innerHTML = (_ss < 10 ? (_ss + ' '):_ss) + '秒';
                            }else if (_opt.meta.dd == '00' && _opt.meta.HH == '00'){
                                var _m = _opt.meta.mm,
                                    _mm = parseInt(_m,10);
                                _node.innerHTML = (_mm < 10 ? (_mm + ' '):_mm) + '分钟';
                            }else if(_opt.meta.dd == '00'){
                                var _h = _opt.meta.HH,
                                    _hh = parseInt(_h,10);
                                _node.innerHTML = (_hh < 10 ? (_hh + ' '):_hh) + '小时';
                            }else{
                                var _d = _opt.meta.dd,
                                    _dd = parseInt(_d,10);
                                _node.innerHTML = (_dd < 10 ? (_dd + ' '):_dd) +'天';
                            }
                        }
                    }._$bind(_node)
                }
            });
            _ct.$inject(_node);
        }._$bind(this));
    };

    /**
     * 重置方法
     * @param  {Object} _options - 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
    };

    _p._$$Index._$allocate({});

    return _p;
});