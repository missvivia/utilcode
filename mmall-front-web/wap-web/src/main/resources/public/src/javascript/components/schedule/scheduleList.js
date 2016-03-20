/**
 *
 * 活动列表实现文件
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
/* pro/components/schedule/scheduleList */
NEJ.define([
    'base/klass',
    'base/element',
    'base/util',
    'base/event',
    'ui/base',
    'pro/components/schedule/scheduleCache',
    'util/list/waterfall',
    'util/template/tpl',
    'util/cache/cookie',
    'pro/components/countdown/countdown',
    'pro/extend/request',
    'text!./scheduleList.css',
    'text!./scheduleList.html'
],function(_k,_e,_u,_v,_t,_t0,_t1,_t2,_j,_countdwon,request,_css,_html,_p,_o,_f,_r){

    var _pro,
        _seed_css  = _e._$pushCSSText(_css),
        _seed_ui   = _t2._$parseUITemplate(_html),
        _seed_box  = _seed_ui['seedBox'],
        _seed_list = _seed_ui['seedList'],
        _seed_loading = _seed_ui['seedLoading'],
        _limit = 10;

    /**
     * [_$$ScheduleList description]
     * @type {[type]}
     */
    _p._$$ScheduleList = _k._$klass();
    _pro = _p._$$ScheduleList._$extend(_t._$$Abstract);

    /**
     * 初始化
     * @param  {[type]} _options [description]
     * @return {[type]}          [description]
     */
    _pro.__init = function(_options){
        this.__super(_options);
    };

    /**
     * 直接加到
     * @param  {[type]} _options [description]
     * @return {[type]}          [description]
     */
    _pro.__reset = function(_options){
        var _parent = _e._$get('list-box');
        _options.parent = _e._$get(_options.parent)||_parent;
        this.__super(_options);
        // 构建列表模块，使用JST模版
        var _limit = _options.limit||_limit;
        this.__listmodule = _t1._$$ListModuleWF._$allocate({
            limit:_limit,
            sbody:_options.sbody||window,
            parent:this.__body,
            delta:_options.delta||100,
            item:_seed_list, // 这里也可以传自己实现的item类型
            cache:{
                ext:{url:_options.url||''},
                lkey:_options.lkey||'scedule-list-data',// 此key必须是唯一的，对应了item中的值,也是删除选项的data-id
                data:{list:_options.list}, // <--- 列表加载时携带数据信息，此数据也可在cache层补全
                klass:_t0._$$ScheduleCache
            },
            onbeforelistload:function(_event){
                _event.stopped = !0;
                this.__ntip = _t2._$getNodeTemplate(_seed_loading);
                this.__body.insertAdjacentElement('beforeEnd',this.__ntip);
            }._$bind(this),
            onafterlistrender:function(_options){
                _e._$removeByEC(this.__ntip);
                _v._$dispatchEvent(window,'scroll');
                var _parent = _options.parent;
                var _list = _e._$getByClassName(_parent,'j-li');
                _list = _list.slice(_options.offset,_options.offset + _limit);
                this.__addCountdown(_list);
            }._$bind(this),
            onemptylist:function(_event){
                if (!!this.__ntip){
                    _e._$removeByEC(this.__ntip);
                }
            }._$bind(this),
            pager:{parent:'pager-box'}
        });
    };

    /**
     * 初始化结构
     * @return {[type]} [description]
     */
    _pro.__initXGui = function(){
        this.__seed_css  = _seed_css;
        this.__seed_html = _seed_box;
    };

    /**
     * [__checkLogin description]
     * @return {[type]} [description]
     */
    _pro.__checkLogin = function(){
        var _info = _j._$cookie('P_INFO');
        return !!_info;
    };

    /**
     * 显示倒计时
     * @param  {[type]} _list [description]
     * @return {[type]}       [description]
     */
    _pro.__addCountdown = function(_list){
        _u._$forEach(_list,function(_item){
            var _node = _e._$getByClassName(_item,'j-bcd')[0],
                _time = _e._$dataset(_node,'countdown');
            if (!!_time){
                var _ct =  new _countdwon({
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
                                _node.innerHTML = _opt.meta.ss + '秒';
                            }else if (_opt.meta.dd == '00' && _opt.meta.HH == '00'){
                                _node.innerHTML = _opt.meta.mm +　'分钟';
                            }else if(_opt.meta.dd == '00'){
                                _node.innerHTML = _opt.meta.HH +　'小时';
                            }else{
                                _node.innerHTML = _opt.meta.dd +　'天';
                            }
                        }
                    }._$bind(_node)
                }
                })
                _ct.$inject(_node);
            }
        }._$bind(this));
    };
});
