/*
 * ------------------------------------------
 * 取消订单实现文件
 * @version  1.0
 * @author   cheng-lin(cheng-lin@corp.netease.com)
 * ------------------------------------------
 */
/* 需要layer.css */
NEJ.define([
    'base/klass',
    'base/element',
    'base/event',
    'pro/widget/layer/window',
    'util/template/tpl',
    'util/form/form',
    'text!./mylayer.html',
    'text!./mylayer.css'
],function(_k,_e,_v,_$$LWindow,_t1,_t2,_html,_css,_p,_o,_f,_r){
    var _pro,
        _seed_html = _t1._$addNodeTemplate(_html),
        _seed_css = _e._$pushCSSText(_css);

    _p._$$Mylayer = _k._$klass();
    _pro = _p._$$Mylayer._$extend(_$$LWindow);

    /**
     * 初始化方法
     * @param  {Object} _options - 配置信息
     * @return {Void}
     */
    _pro.__init = function(_options){
        this.__simple = _options.simple||true;
        this.__super(_options);
    };

    /**
     * 生成结构
     * @return {Void}
     */
    _pro.__initXGui = function(){
        this.__seed_html = _seed_html;
        this.__seed_css = _seed_css;
    };

    /**
     * 初始化节点
     * @param  {Object} _options - 配置信息
     * @return {Void}
     */
    _pro.__initNode = function(_options){
        this.__super(_options);
        var _cnts = _e._$getByClassName(this.__body,'j-cnt');
        this.__cc = _cnts[4];
        this.__tit= _cnts[0];
        this.__ntp= _cnts[1];
        this.__ok = _cnts[3];
        this.__co = _cnts[4];
        this.__doInitDomEvent([
            [this.__cc,'click',this.__onCC._$bind(this)],
            [this.__co,'click',this.__onCC._$bind(this)],
            [this.__ok,'click',this.__onOK._$bind(this)]
        ]);
    };

    /**
     * 重置控件
     * @param  {Object} _options - 配置信息
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__tit.innerText = _options.title||'确认框';
        if (!!_options.cnt){
            this.__ntp.innerHTML = _options.cnt;
        }
        this.__super(_options);
        this.__webform = _e._$getByClassName(this.__body,'j-form')[0];
        if (!!this.__webform){
            this.__form = _t2._$$WebForm._$allocate({
                form:this.__webform,
                oncheck:this.__checkForm._$bind(this),
                oninvalid:this.__onInvalid._$bind(this)
            });
        }
    };

    /**
     * 没有通过检查的处理
     * @param  {Event} event - 事件对象
     * @return {Void}
     */
    _pro.__onInvalid = function(_event){
        this._$dispatchEvent('check',_event);
    };

    /**
     * 额外的自定义检查
     * @param  {Event} event - 事件对象
     * @return {Void}
     */
    _pro.__checkForm = function(_event){
        this._$dispatchEvent('check',_event);
    };

    /**
     * 取消
     * @return {Void}
     */
    _pro.__onCC = function(){
        this._$hide();
    };

    /**
     * 确定
     * @return {Void}
     */
    _pro.__onOK = function(){
        var _data = {'reason':'wap'};
        if (!!this.__webform){
            this.__form._$checkValidity();
            _data = this.__form._$data();
        }
        this._$dispatchEvent('onok',_data);
    };

});