/*
 * ------------------------------------------
 * 延时加载图片控件实现文件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'util/lazy/image'
],function(_k,_e,_t,_p,_o,_f,_r,_pro){
    /**
     * 延时加载图片
     *
     * @class   _$$LazyImage
     * @extends _$$LazyImage
     *
     * @param    {Object} config - 配置信息
     *
     */
    _p._$$LazyImage = _k._$klass();
    _pro = _p._$$LazyImage._$extend(_t._$$LazyImage);
    /**
     * 控件重置
     *
     * @protected
     * @method module:util/lazy/image._$$LazyImage#__reset
     * @param  {Object} arg0 - 配置信息
     * @return {Void}
     */
    _pro.__reset = function(_options){
        _options.attr = 'src,axis';
        this.__super(_options);
    };
    /**
     * 验证资源是否需要做处理
     *
     * @protected
     * @param  {Node}   arg0 - 资源节点
     * @param  {Object} arg1 - 滚动容器节点
     * @return {Number}        操作标识，-1 - 移除，0 - 不做处理， 1 - 追加到页面
     */
    _pro.__doCheckResource = function(_target,_parent){
        var _ch = _parent.clientHeight,
            _rup = -_ch,
            _rdn = 2*_ch,
            _top = _e._$offset(_target,_parent).y-_parent.scrollTop,
            _bottom = _top+_target.offsetHeight,
            _config = this.__getSettingInfo(_target),
            // not src
            // src is blank image
            // src not equal to data-src
            _holded = !_target.src||
                       _target.src.indexOf(this.__holder)>=0||
                       _target.src.indexOf(_config.src)<0;
        // check resource append
        if (_holded&&_rup<=_bottom&&_top<=_rdn){
            return 1;
        }
        // check resource remove
        if (!_holded&&(_rup>_bottom||_top>_rdn)){
            return -1;
        }
        // do nothing
        return 0;
    };
    /**
     * 添加资源
     *
     * @protected
     * @method module:util/lazy/image._$$LazyImage#__doAppendResource
     * @param  {Node}   arg0 - 资源节点
     * @param  {Object} arg1 - 配置信息
     * @return {Void}
     */
    _pro.__doAppendResource = function(_node,_conf){
        var _src = this.__holder;
        if (!!_conf.src){
            _src = _conf.src;
            if (_src.indexOf('nos.netease.com')>=0&&
                _src.indexOf('?')<0){
                _src += '?imageView&quality=95&thumbnail='+_node.offsetWidth*2+'y'+_node.offsetHeight*2+'&axis='+(_conf.axis||10);
            }
        }
        _node.src = _src;
    };
});
