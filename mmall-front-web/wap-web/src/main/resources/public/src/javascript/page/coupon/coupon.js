NEJ.define([
    'text!./coupon.html',
    'base/element',
    'util/cache/storage',
    'pro/widget/BaseComponent',
    'pro/page/coupon/widget/tab/tab',
    'pro/page/coupon/widget/select/select',
    'pro/page/coupon/coupon/couponList',
    'pro/page/coupon/redpacket/redpacketList',
    'pro/page/coupon/coupon/couponDetail',
    'pro/page/coupon/redpacket/redpacketDetail'
],function(_html,_e,_j,Component,tab,Select,CouponList,RedpacketList,CouponDetail,RedpacketDetail,_p,_o,_f,_r){
    var Coupon= Component.extend({
        template:_html,
        data:{
            currentType:0,
            //页面类型与url映射关系（0：优惠券列表，1：红包列表，2：优惠券详情，3：红包详情）
            typeUrlMapping: {
                0:"/coupon",
                1:"/coupon/redpacketlist",
                2:"/coupon/coupondetail",
                3:"/coupon/redpacketdetail"
            }
        },
        /**
         * 初始化
         */
        init:function(){
            this.$inject('#wrap');
            this.resetPage();
            window.addEventListener('popstate', function(event){this.resetPage();}._$bind(this));
        },
        /**
         * 重置页面
         */
        resetPage:function(){
            var typeUrlMapping=this.data.typeUrlMapping;
            //解析url获取当前页面类型
            var type= 0,url=typeUrlMapping[0];
            for(var i in typeUrlMapping){
                if(i!=0 && window.location.href.indexOf(typeUrlMapping[i])>=0){
                    type=i;
                    url=typeUrlMapping[i];
                    break;
                }
            }
            //处理详情页面
            if(type==2){//优惠券详情页
                var couponDetail=_j._$getDataInStorage('couponDetail');
                if(!couponDetail){//如果无法恢复数据，则转到优惠券列表页面
                    type=0;
                    window.history.replaceState({},0,typeUrlMapping[type]);
                }else{
                    this.showCouponDetail(couponDetail,true);
                }
            }
            else if(type==3){//红包详情页
                var redpacketDetail=_j._$getDataInStorage('redpacketDetail');
                if(!redpacketDetail){//如果无法恢复数据，则转到红包列表页面
                    type=1;
                    window.history.replaceState({},0,typeUrlMapping[type]);
                }else{
                    this.showRedpacketDetail(redpacketDetail,true);
                }
            }

            //如果为列表页，初始化tab控件
            if(type==0||type==1){
                if(!this.__tab){
                    this.__tab = new tab({data:{selectedIndex:type}});
                    this.__tab.$inject('#tab');
                    this.__tab.$on('change',this.onTabChange._$bind(this));
                }else if(type!=this.__tab.data.selectedIndex){
                    this.__tab.$update('selectedIndex',type);//恢复选中状态
                }
            }
            //处理列表页面
            if(type==0){//优惠券列表
                if(!this.__couponList){
                    this.__couponList = new CouponList();
                    this.__couponList.$inject('#couponList');
                    this.__couponList.$on('tap',this.showCouponDetail._$bind(this));
                }
            }
            else if(type==1){//红包列表
                if(!this.__redpacketList){
                    this.__redpacketList = new RedpacketList();
                    this.__redpacketList.$inject('#redPacketList');
                    this.__redpacketList.$on('tap',this.showRedpacketDetail._$bind(this));
                }
            }
            if(type!=this.data.currentType) this.$update("currentType",type);//更新当前的页面类型
            this.resetTitle();//重置顶栏标题
        },
        resetTitle:function(){
            var node=_e._$getByClassName(document.body,"tt")[0];
            var type=this.data.currentType;
            if(type==2){
                node.innerHTML="优惠券详情";
            }else if(type==3){
                node.innerHTML="红包详情";
            }else{
                node.innerHTML="优惠券/红包"
            }
        },
        /**
         * 顶栏tab切换
         * @param _index
         */
        onTabChange:function(_index){
            window.history.pushState({},0,this.data.typeUrlMapping[_index]);
            this.resetPage();
        },
        /**
         * 显示优惠券详情
         * @param couponDetail 详情数据
         * @param isHistory 是否是浏览器历史状态（刷新，后退，前进等动作触发）
         */
        showCouponDetail:function(couponDetail,isHistory){
            var type=2;
            //更新当前页面类型
            this.$update("currentType",type);
            //处理新生成页面
            if(!isHistory){
                _j._$setDataInStorage('couponDetail',couponDetail);
                window.history.pushState({},0,this.data.typeUrlMapping[type]);
                this.resetTitle();
            }
            //显示优惠券详情
            if(!this.__couponDetail){
                this.__couponDetail = new CouponDetail({data:{couponDetail:couponDetail}});
                this.__couponDetail.$inject('#couponDetail');
            }else{
                this.__couponDetail.$update("couponDetail",couponDetail);
            }
        },
        /**
         * 显示红包详情
         * @param redpacketDetail 红包详情
         * @param isHistory 是否是浏览器历史状态（刷新，后退，前进等动作触发）
         */
        showRedpacketDetail:function(redpacketDetail,isHistory){
            var type=3;
            //更新当前页面类型
            this.$update("currentType",type);
            //处理新生成页面
            if(!isHistory){
                _j._$setDataInStorage('redpacketDetail',redpacketDetail);
                window.history.pushState({},0,this.data.typeUrlMapping[type]);
                this.resetTitle();
            }
            //显示红包详情
            if(!this.__redpacketDetail){
                this.__redpacketDetail = new RedpacketDetail({data:{redpacketDetail:redpacketDetail}});
                this.__redpacketDetail.$inject('#redpacketDetail');
            }else{
                this.__redpacketDetail.$update("redpacketDetail",redpacketDetail);
            }
        }
    });
    new Coupon();
});

