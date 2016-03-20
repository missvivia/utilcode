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
    'util/flash/flash',
    'pro/extend/util',
    'pro/widget/layer/login/login',
    'pro/widget/module',
    'pro/widget/util/fade',
    'util/effect/api',
    'util/placeholder/placeholder',
    'pro/widget/util/util',
    'pro/widget/util/fav',
    'pro/widget/util/shoot',
    'pro/components/countdown/countdown',
    'pro/components/bubble/bubble',
    'pro/components/remind/remind',
    'pro/extend/request',
    'util/cache/cookie',
    '../activity0117/explosion/explosion.js',
    'pro/page/activity/components/getcoupon/getcoupon'
],function(_k,_e,_v,_u,_s,_u0,_l,_w,_t0,_t1,_t2,_t3,_t4,_t5,_countdown,_bubble,_remind,request,_j,Explosion,_gc,_p,_o,_f,_r){
    var _pro,
        _ctime = 1000,
        _barw0 = 300,
        _barw1 = 60,
        _loginuri = '/login';

    _p._$$Index = _k._$klass();
    _pro = _p._$$Index._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
    	// 活动绑定优惠券
    	if(_u0.isLogin()) _gc._$getcoupon();
    	_e._$getByClassName(document,'j-getcoupon')[0].onclick = function(){
    		_gc._$getcoupon(true); 
    	}
    	// 活动绑定优惠券结束
    	
        var _nodes = _e._$getByClassName(document,'j-node')
        // var _opt = {
        //     slideBox:_nodes[0],
        //     iconBox:_nodes[3],
        //     prev:_nodes[1],
        //     next:_nodes[2],
        //     selected:'z-active',
        //     width:1920,
        //     duration:300,
        //     stop:3000,
        //     initleft:1090/2
        // }
        var _fadeopt = {
            indexBox:_nodes[3],
            contentBox:_nodes[0],
            prev:_nodes[1],
            next:_nodes[2],
            stop:5100
        }
        if(!_j._$cookie('isshowbuble')){
	        Explosion._$allocate({onanimationend:function(){
	        	_j._$cookie('isshowbuble',{value:1})
	        }})
        }
        if (!_e._$hasClassName(_fadeopt.next,'f-dn')){
            this.__mainfade = _t0._$$Fade._$allocate(_fadeopt);
        }
        this.__firstImg = _e._$getByClassName(document,'j-first')[0];
        this.__word = _e._$getByClassName(document,'j-word')[0];
        this.__closeq = _e._$getByClassName(document,'j-quan')[0];
        setTimeout(function(){
            _e._$addClassName(this.__firstImg,'do-transform');
            _e._$addClassName(this.__word,'do-tram');
        }._$bind(this),0);
        this.__flashBox = _e._$getByClassName(document,'j-slogan')[0];
        this.__brandBox = _nodes[6];
        this.__newBrand = _nodes[5];
        this.__collect = _nodes[4];
        // this.__love = _nodes[7];
        // this.__lovei = _nodes[8];
        this.__nbox = _e._$getByClassName(document,'j-newbrand-box')[0];
        this.__nbox2 = _e._$getByClassName(document,'j-newbrand-box-tody')[0];
        this.__bcds = _e._$getByClassName(document,'j-bcd');
        this.__newBrandList = _e._$getChildren(this.__nbox);
        this.__blis = _e._$getByClassName(document,'j-li');
        _u._$forEach(this.__blis,function(_li){
            _v._$addEvent(_li,'mouseenter',this.__clearLIerror._$bind(this));
        }._$bind(this));
        this.__newBrandIndex = 0;
        this.__nblis = _e._$getChildren(this.__newBrand);
        // this.__lis = _e._$getChildren(this.__brandBox);
        this.__pdmes = _e._$getByClassName(document,'j-pdme');
        this.__super();
        this.__tolove = _e._$get('sidebar-followicon');
        // this.__getMyLove();
        // 最新品牌倒计时
        _u._$forEach(this.__bcds,function(_node){
            var _time = _e._$dataset(_node,'countdown');
            var _ct = new _countdown({
                data:{
                    content:'<b>{{dd}}</b>天<b>{{HH}}</b>时<b>{{mm}}</b>分<b>{{ss}}</b>秒',
                    time:_time,
                    onchange:function(_opt){
                        if (_opt.isdown){
                            var _pd = _node.parentNode.parentNode.parentNode.parentNode;
                            _e._$addClassName(_pd,'f-dn');
                        }
                    }._$bind(_node)
                }
            });
            _ct.$inject(_node);
        }._$bind(this));
         // 错误提示
        _u._$forEach(this.__pdmes,function(_node){
            _t2._$placeholder(_node);
            this.__doInitDomEvent([
                [_node,'focus',this.__onClearError._$bind(this)]
            ]);
        }._$bind(this));
        // 错误提示
        /**_u._$forEach(this.__lis,function(_node){
            _node = _e._$getByClassName(_node,'j-phd')[0];
            _t2._$placeholder(_node);
            this.__doInitDomEvent([
                [_node,'focus',this.__onClearError._$bind(this,0)]
            ]);
        }._$bind(this));*/

        // 品牌动画效果
        _t5._$$Shoot._$allocate({
            ulbox:this.__brandBox,
            lazyRefresh:this.__onLazyRefresh._$bind(this)
        });
        // this.__setFlash();

        //统计信息
        this.__linkswrap=_e._$getByClassName(document,'cnt')[0];
        var _schedules=_e._$getByClassName(document,'schedule');
        this.__schedule1=_schedules[0];
        this.__schedule2=_schedules[1];
    };

    _pro.__clearLIerror = function(_event){
        var _node = _v._$getElement(_event),
            _ipt = _e._$getByClassName(_node,'j-pdme')[0];
        if (this.__bubble){
            this.__bubble.destroy();
        }
    };

    /**
     * 重置方法
     * @param  {Object} _options - 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__doInitDomEvent([
            [this.__closeq,'click',this.__onCloseQuan._$bind(this)],
            [this.__linkswrap,'click',this.__statistic._$bind(this)],
            [this.__schedule1,'click',this.__statistic1._$bind(this)],
            [this.__schedule2,'click',this.__statistic2._$bind(this)],
            [this.__nbox,'click',this.__onCallMe._$bind(this)],
            [this.__nbox2,'click',this.__onCallMe._$bind(this)],
            [this.__newBrand,'mouseover',this.__onNewBrandOver._$bind(this)]

        ]);
        this.__super(_options);
    };

    _pro.__onCloseQuan = function(_event){
        var _target = _v._$getElement(_event);
        if (_target && !!_target.parentNode){
            _e._$addClassName(_target.parentNode,'f-dn');
        }
    };

    /**
     * 加载shoot的图片
     * @return {[type]} [description]
     */
    _pro.__onLazyRefresh = function(){
        this.__doLazyRefresh();
    };

    _pro.__doFindLi = function(_node){
        if (!_node){
            return null;
        }else if (_node.tagName == 'LI'){
            return _node;
        }else{
            return this.__doFindLi(_node.parentNode);
        }
    }

    /**
     * 获取我的关注
     * @return {[type]} [description]

    _pro.__getMyLove = function(){
        if (!!_j._$cookie('notremind')){
            return;
        }
        if (this.__checkLogin()){
            // 取8个
            request('/schedule/favlist?limit=8&offset=0',{
                onload:function(_result){
                    if (_result.code == 200 && !!_result.result && _result.result.list && _result.result.list.length > 0){
                        this.__doRemind();
                    }
                }._$bind(this),
                onerror:function (argument) {
                }
            })
        }
    };*/

    /**
     * 提醒我一次
     * @return {[type]} [description]

    _pro.__doRemind = function(){
        var _offset = _e._$offset(this.__tolove);
        this.__myremind = new _remind({
            data:{
                content:"关注的品牌有活动中",
                start:[_offset.y,_offset.x+100],
                end:[_offset.y,_offset.x-200]
            }
        });
        _j._$cookie('notremind','1',{expires:1});
    };*/

    /**
     * 清除所有错误提示
     * @param  {[type]} _flag  [description]
     * @param  {[type]} _event [description]
     * @return {[type]}        [description]
     */
    _pro.__onClearError = function(_event){
        if (!!this.__bubble){
            this.__bubble.destroy();
        }
        // var _target = _v._$getElement(_event),
        // _node = _e._$getByClassName(_target.parentNode.parentNode.parentNode.parentNode,'error')[0];
        // _e._$addClassName(_node,'f-dn');
    };

    /**
     * 品牌的提醒我
     * @param  {[type]} _event [description]
     * @return {[type]}        [description]

    _pro.__onBDCallMe = function(_event){
        var _target = _v._$getElement(_event),
            _id = _e._$dataset(_target,'id');
        if (!!_id){
            var _prenode = _e._$getByClassName(_e._$getSibling(_target,{backward:true}),'j-phd')[0],
                _phone = _prenode.value.trim(),
                _type = _phone.indexOf('@') > -1 ? 1 : 0;
            if (_t3._$checkEmailAndPhone(_phone)){
                // 提醒我接口
                this.__doCallMe(_id,'brand',_type,_phone);
            }else{
                var _error = _e._$getSibling(_target.parentNode,'error');
                _error.innerHTML = '输入格式错误';
            }
            return _id;
        }
    };*/

    /**
     * 预告通知我或收藏
     * @return {[type]} [description]
     */
    _pro.__onCallMe = function(_event){
        var _target = _v._$getElement(_event),
            _timestr = _e._$dataset(_target,'action'),
            _id = _e._$dataset(_target,'id');
        if (!!_timestr){
            // 活动提醒我接口
            var _parent = _target.parentNode.parentNode;
            var _prenode = _e._$getByClassName(_parent,'j-pdme')[0],
                _phone = _prenode.value.trim(),
                _type = _phone.indexOf('@') > -1 ? 1 : 0,
                _errorNode = _e._$getByClassName(_parent,'error')[0];
            if (_t3._$checkEmailAndPhone(_phone)){
                var _sid = _e._$dataset(_target,'sid');
                this.__doCallMe(_sid,1,_type,_phone,_timestr,_errorNode);
            }else{
                if (!!this.__bubble){
                    this.__bubble.destroy();
                }
                this.__bubble = new _bubble({
                    data:{
                        content:'输入正确的邮箱或手机号',
                        clazz:'m-notecb-red'
                    }
                });
                this.__bubble.$inject(_errorNode);
            }
            return this.__newBrandIndex;
        }
        // if (!!_id){
        //     // 关注品牌
        //     if (!this.__checkLogin()){
        //         this.__showLogin();
        //     }else{
        //         this.__doFav(_id,_target);
        //     }
        // }
    };

    _pro.__showLogin = function(){
        var _window = _l._$$LoginWindow._$allocate({
            parent:document.body
        });
        _window._$show();
    };

    /**
     * 提醒我请求
     * @return {[type]} [description]
     */
    _pro.__doCallMe = function(_id,_activeType,_type,_value,_timeStr,_errorNode){
        var _uri = '/user/active/tellme';
        request(_uri,{
            data:{'activeId':_id,'activeType':_activeType,'type':_type,'value':_value,timeStr:_timeStr||''},
            onload:function(_result){
                if (_result.code == 200){
                    // alert('提醒成功')
                    if (!!this.__bubble){
                        this.__bubble.destroy();
                    }
                    this.__bubble = new _bubble({
                        data:{
                            content:'添加提醒成功',
                            clazz:'m-notecb'
                        }
                    })
                    this.__bubble.$inject(_errorNode);
                }
            }._$bind(this),
            onerror:function(){

            }
        })
    };

    /**
     * 加入关注
     * @param  {Event}  event - 事件对象
     * @return {Void}

    _pro.__onCollect = function(_event){
        var _target = _v._$getElement(_event),
            _action = _e._$dataset(_target,'action');
        if (!!_action){
            _action = parseInt(_action);
            if (!this.__checkLogin()){
                this.__showLogin();
            }else{
                this.__doFav(_action,_target);
            }
        }
    };*/

    /**
     * 检查登录状态
     * @return {Boolean} 是否登录
     */
    _pro.__checkLogin = function(){
        return _u0.isLogin();
    };

    /**
     * 关注操作
     * @param  {[type]} _id     [description]
     * @param  {[type]} _target [description]
     * @return {[type]}         [description]

    _pro.__doFav = function(_id,_target){
        var _hasFav = _e._$dataset(_target,'hasFav');
        if (!!_hasFav && _hasFav == 'yes'){
            this.__favurl = '/schedule/unfollow';
        }else{
            this.__favurl = '/schedule/follow';
        }
        request(this.__favurl,{
            data:{'scheduleId':_id},
            method:'POST',
            onload:function(_result){
                if (_result.code == 200){
                    var _hasFav = _e._$dataset(_target,'hasFav');
                    //,_hasFav,this.__tolove,this.__love,this.__lovei
                    _t4._$doFav(_target);
                }
            }._$bind(this),
            onerror:function(){

            }
        })
    };*/

    /**
     * 预告品牌tab鼠标移入事件
     * @param  {Node} node - 事件起始节点
     * @return {Node|null} LI节点或null
     */
    _pro.__onNewBrandOver = function(_event){
        var _target = _v._$getElement(_event),
            _li = this.__findStop(_target);
        if (!!_li){
            var _index = _e._$dataset(_li,'index');
            _u._$forEach(this.__nblis,function(_item){
                _e._$delClassName(_item,'z-active');
            });
            _e._$addClassName(_li,'z-active');
            this.__showNewBrand(_index);
        }
    };

    _pro.__statistic=function(_event){
    	 var target = _event.target || _event.srcElement || _event.originalTarget,ttt="";
    	 if(_e._$hasClassName(target,"rule1")){
    		 ttt="xianliangling";
    	 }else if(_e._$hasClassName(target,"rule2")){
    		 ttt="zhengdianqiang"
    	 }else if(_e._$hasClassName(target,"rule3")){
    		 ttt="manesong"
    	 }else if(_e._$hasClassName(target,"rule4")){
    		 ttt="xiadanchou"
    	 };
    	 var userName = _u0.getFullUserName();
    	 _gaq.push(['_trackEvent', 'xinchonghui', ttt,'mainpage###'+userName]);
    }

    _pro.__statistic1=function(_event){
   	 var userName = _u0.getFullUserName();
   	 _gaq.push(['_trackEvent', 'xinchonghui', "banner1",'mainpage###'+userName]);
   }

    _pro.__statistic2=function(_event){
    	 var userName = _u0.getFullUserName();
       	 _gaq.push(['_trackEvent', 'xinchonghui', "banner2",'mainpage###'+userName]);
   }

    /**
     * 显示新品牌
     * @param  {Number} index - 品牌序号
     * @return {Void}
     */
    _pro.__showNewBrand = function(_index){
        _e._$addClassName(this.__newBrandList[this.__newBrandIndex],'f-dn');
        this.__newBrandIndex = _index;
        _e._$delClassName(this.__newBrandList[this.__newBrandIndex],'f-dn');
    };

    /**
     * 找鼠标移动事件的父节点一直到LI
     * @param  {Node} node - 事件起始节点
     * @return {Node|null} LI节点或null
     */
    _pro.__findStop = function(_node){
        var _tagName = _node.tagName;
        if (_tagName == 'UL'){
            return null;
        }
        if (_tagName == 'LI'){
            return _node;
        }else{
            return this.__findStop(_node.parentNode);
        }
    };

    _pro.__setFlash = (function(){
        var checkFlash = function(){
            var version = 0;
            if (document.all){
                try{
                    var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
                }catch(e){
                    version = 0;
                }
                if (swf){
                    version = 1;
                }
            }else{
              if(navigator.plugins && navigator.plugins.length > 0) {
                var swf = navigator.plugins["Shockwave Flash"];
                if (swf){
                  version = 1;
                }
              }
            }
            return version;
        }
        return function(){
            if (!checkFlash()){
                this.__flashBox.innerHTML = '<p class="u-slogan"></p>';
            }else{
                _s._$flash({
                    src:'../../res/welcome.swf',
                    hidden:false,
                    parent:this.__flashBox,
                    width:150,
                    height:100,
                    params:{
                        flashvars:'',
                        wmode:'transparent',
                        allowscriptaccess:'always'
                    }
                });
            }
        }
    })();

    _p._$$Index._$allocate({});

    return _p;
});