/*
 * ------------------------------------------
 * 主站分类页实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/platform',
    'util/template/tpl',
    'base/klass',
    'base/element',
    'base/event',
    'base/util',
    'pro/widget/module',
    'pro/widget/util/fade',
    'util/effect/api',
    'pro/components/countdown/countdown',
    'util/placeholder/placeholder',
    'pro/page/minicart/minicart',
    'pro/page/minibrand/minibrand',
    'pro/extend/request',
    'util/cache/cookie',
    'pro/components/remind/remind'
],function(_m,_t3,_k,_e,_v,_u,_w,_t0,_t1,_t2,_t5,_cartModule,_brandModule,request,_j,_remind,_p,_o,_f,_r){
    var _pro,
        _ctime = 1000,
        _barw0 = 300,
        _barw1 = 60;

    _p._$$SubPage = _k._$klass();
    _pro = _p._$$SubPage._$extend(_w._$$Module);

    /**
     * 初始化方法
     * @return {Void}
     */
    _pro.__init = function(){
        var _nodes = _e._$getByClassName(document,'j-node')
        var _fadeopt = {
            indexBox:_nodes[3],
            contentBox:_nodes[0],
            prev:_nodes[1],
            next:_nodes[2],
            stop:10000
        }
         if (!_e._$hasClassName(_fadeopt.next,'f-dn')){
            this.__mainfade = _t0._$$Fade._$allocate(_fadeopt);
        }
        this.__firstImg = _e._$getByClassName(document,'j-first')[0];
        setTimeout(function(){
            _e._$addClassName(this.__firstImg,'do-transform');
        }._$bind(this),10);

        this.__collect = _nodes[4];
        this.__love = _nodes[5];
        this.__lovei = _nodes[6];
        this.__bcds = _e._$getByClassName(document,'j-bcd');
        this.__super();
        _u._$forEach(this.__bcds,function(_node){
            var _time = _e._$dataset(_node,'countdown');
            var _ct = new _t2({
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
    };

    /**
     * 重置方法
     * @param  {Object} _options - 配置参数
     * @return {Void}
     */
    _pro.__reset = function(_options){
        // this.__doInitDomEvent([
        //     [this.__collect,'click',this.__onCollect._$bind(this)]
        // ]);
        this.__isFirst = true;
        this.__super(_options);
    };

    /**
    _pro.__onClearError = function(_flag,_event){
        var _target = _v._$getElement(_event),
            _node = _e._$getSibling(_target.parentNode.parentNode,'error');
        if (_flag){
            _e._$addClassName(_node,'f-dn');
        }else{
            _node.innerHTML = '';
        }
    };*/

    /**
     * 加入关注
     * @param  {Event}  event - 事件对象
     * @return {Void}

    _pro.__onCollect = function(_event){
        var _target = _v._$getElement(_event),
            _action = _e._$dataset(_target,'action');
        if (!!_action){
            _action = parseInt(_action);
            this.__doCollect(_action,_target);
        }
    };*/

    /**
     * 检查登录状态
     * @return {Boolean} 是否登录

    _pro.__checkLogin = function(){
        var _info = _j._$cookie('P_INFO');
        return !!_info;
    };*/

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
                    this.__doFavAmin(_target);
                }
            }._$bind(this),
            onerror:function(){

            }
        })
    };*/

    /**
     * 关注活动
     * @param  {Number} brandId - 品牌ID
     * @param  {Node}   操作节点
     * @return {Void}

    _pro.__doCollect = function(_brandId,_target){
        if (!this.__checkLogin()){
            location.href = _loginuri;
        }else{
            this.__doFav(_brandId,_target);
        }
    };*/

    /**
     * 关注的动画
     * @param  {[type]} _target [description]
     * @return {[type]}         [description]

    _pro.__doFavAmin = function(_target){
        var _hasFav = _e._$dataset(_target,'hasFav');
        if (!!_hasFav && _hasFav == 'yes'){
            _e._$dataset(_target,'hasFav','no');
            _target.title = '添加关注';
            _target.innerHTML = ' &#xe607; ';
        }else{
            _e._$dataset(_target,'hasFav','yes');
            _target.title = '已关注';
            // 调用收藏成功后，设置_target的状态
            if (!!this.__lovest){
                this.__lovest = clearTimeout(this.__lovest);
            }
            if (!!this.__phst){
                this.__phst = clearTimeout(this.__phst);
            }
            var _offset = _e._$offset(_target);
            _e._$setStyle(this.__love,'left',_offset.x + 'px');
            _e._$setStyle(this.__love,'top',_offset.y + 'px');
            _e._$delClassName(this.__love,'f-dn');
            _e._$addClassName(_target.parentNode,'fav-ok');
            setTimeout(function(){
                _e._$addClassName(this.__lovei,'u-iconlove-showing');
            }._$bind(this),0);
            // 变大的时间,替换心的状态
            this.__phst = setTimeout(function(){
                _target.innerHTML = ' &#xe606; ';
            },200);


            if (!!this.__doAnim){
                // 点太快，动画来不及
                return;
            }
            this.__doAnim = true;
            var _to = _e._$offset(_e._$get('sidebar-followicon')),
                _x = _to.x,
                _y = _to.y +
                (_m._$KERNEL.browser == 'ie' ?
                document.documentElement.scrollTop : document.body.scrollTop);
            // 避免x相同的情况
            if (_offset.x == _x){
                _x++;
            }
            _t1._$moveTo(this.__love,{left:_x,top:_y},{
                timing:'ease-in-out',
                delay:0.3,
                duration:[0.5,0.5],
                onstop:function(){
                    this.__doAnim = false;
                    _e._$delClassName(this.__lovei,'u-iconlove-showing');
                    this.__lovest = setTimeout(function(){
                        _e._$delClassName(_target.parentNode,'fav-ok');
                        _e._$addClassName(this.__love,'f-dn');
                    }._$bind(this),200)

                }._$bind(this)
            });
        }
    };*/

    _p._$$SubPage._$allocate({});

    return _p;
});