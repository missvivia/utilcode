/**
 *
 * 收藏动画实现文件
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
/* pro/widget/util/fav */
NEJ.define([
    'base/element',
    'base/event',
    'base/util',
    'base/platform',
    'util/effect/api'
],function(_e,_v,_u,_m,_t,_p,_o,_f,_r){
    var _lovest,
        _lovehtml = '<div id="fav-love-box" class="f-dn love0">\
                      <i id="fav-lovei-box" class="u-iconlove"> &#xe606; </i>\
                    </div>';
    /**
     * 收藏动画
     *  if (!!_hasFav && _hasFav == 'yes'){
            _e._$dataset(_target,'hasFav','no');
            _target.title = '添加关注';
            _target.innerHTML = ' &#xe607; ';
        }else{
     * @param  {node} _target - 开始节点
     * @param  {node} _hasFav - 是添加关注还是取消关注
     * @param  {node} _tolove - 目标节点
     * @param  {node} _love   - 用来做动画的div的外层结构
     * @param  {node} _lovei  - 用来做动画的div的内层结构
     * @param  {[type]} _key [description]
     * @return {[type]}      [description]
     */
    _p._$doFav = function(_target,_options){
        var _love = _e._$get('fav-love-box'),
            _tolove = _e._$get('sidebar-followicon'),
            _target = _e._$get(_target),
            _options = _options || {},
            _lovei;
        if (!_love){
            var _node = _e._$html2node(_lovehtml);
            document.body.appendChild(_node);
            _love = _node
        }
        _lovei = _e._$get('fav-lovei-box');
        if (!_lovei || !_love){
            return;
        }
        if (_options.clazz){
            _e._$addClassName(_lovei,_options.clazz);
        }
        // _e._$dataset(_target,'hasFav','yes');
        _target.title = '已关注';
        // 调用收藏成功后，设置_target的状态
        if (!!_lovest){
            _lovest = clearTimeout(_lovest);
        }
        if (!!this.__phst){
            this.__phst = clearTimeout(this.__phst);
        }
        var _offset = _e._$offset(_target);
        _e._$setStyle(_love,'left',_offset.x + 'px');
        _e._$setStyle(_love,'top',_offset.y + 'px');
        _e._$delClassName(_love,'f-dn');
        // _e._$addClassName(_target.parentNode,'fav-ok');
        setTimeout(function(){
            _e._$addClassName(_lovei,'u-iconlove-showing');
        }._$bind(this),0);
        // 变大的时间,替换心的状态
        // this.__phst = setTimeout(function(){
        //     _target.innerHTML = ' &#xe606; ';
        // },200);

        if (!!this.__doAnim){
            // 点太快，动画来不及
            return;
        }
        var _tolove = _e._$get(_tolove);
        if (!_tolove){
            return;
        }
        this.__doAnim = true;
        var _to = _e._$offset(_tolove),
            _x = _to.x,
            _y = _to.y +
            (_m._$KERNEL.browser == 'ie' ?
            document.documentElement.scrollTop : document.body.scrollTop);
        // 避免x相同的情况
        if (_offset.x == _x){
            _x++;
        }
        // 清除节点上的动画
        _t._$stopEffect(_love);
        // 开始移动
        _t._$moveTo(_love,{left:_x,top:_y},{
            timing:'ease-in-out',
            delay:_options.delay||0.3,
            duration:[_options.duration||0.3,_options.duration||0.3],
            onstop:function(){
                this.__doAnim = false;
                _e._$delClassName(_lovei,'u-iconlove-showing');
                _lovest = setTimeout(function(){
                    // _e._$delClassName(_target.parentNode,'fav-ok');
                    _e._$addClassName(_love,'f-dn');
                }._$bind(this),200)

            }._$bind(this)
        });
    };


    return _p;
});
