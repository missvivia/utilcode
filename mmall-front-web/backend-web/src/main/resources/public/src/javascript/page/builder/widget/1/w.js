/*
 * ------------------------------------------
 * 自定义组件 - 文字自定义分类条
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/event',
    'base/element',
    'util/template/tpl',
    'util/toggle/toggle',
    '../base/widget.js',
    '../../widget.js',
    '../../util.js',
    '../../ui/setting/font.js',
    'text!./w.html'
],function(_k,_u,_v,_e,_l,_g,_i,_w,_z,_x,_html,_p,_o,_f,_r,_pro){
    /**
     * 自定义组件 - 文字自定义分类条
     * 
     * @class   _$$Widget
     * @extends _$$Widget
     */
    _p._$$Widget = _k._$klass();
    _pro = _p._$$Widget._$extend(_i._$$Widget);
    /**
     * 控件初始化
     * @return {Void}
     */
    _pro.__init = function(){
        this.__topt = {
            clazz:'j-show',
            ontoggle:this.__onInput._$bind(this)
        };
        this.__sopt = {
            parent:document.body,
            onsubmit:this.__doUpdateFontSetting._$bind(this)
        };
        this.__tbop = {
            title:'文字栏',
            acts:['edit','sort','remove'],
            onedit:this.__onFontSetting._$bind(this)
        };
        this.__super();
    };
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        this.__doInitDomEvent([[
            this.__nipt,'enter',
            this.__onTextSetting._$bind(this)
        ],[
            this.__nccl,'click',
            this.__onTextCancel._$bind(this)
        ],[
            this.__nbtn,'click',
            this.__onTextSetting._$bind(this)
        ]]);
        this.__doUpdateSetting(_options);
    };
    /**
     * 初始化外观
     * @return {Void}
     */
    _pro.__initXGui = (function(){
        var _seed_html = _u._$uniqueID();
        _l._$addTextTemplate(_seed_html,_html);
        return function(){
            this.__super();
            this.__seed_html = _seed_html;
            this.__seed_css = 'j-widget m-bd-widget-ud m-bd-widget-1';
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - background show
        // 1 - font setting
        // 2 - tip word
        // 3 - text input
        // 4 - cancel button
        // 5 - ok button
        var _list = _e._$getByClassName(
            this.__wrap,'j-flag'
        );
        this.__nshw = _list[0];
        this.__ntip = _list[1];
        this.__ntxt = _list[2];
        this.__nipt = _list[3];
        this.__nccl = _list[4];
        this.__nbtn = _list[5];
        _g._$toggle(this.__ntxt,this.__topt);
    };
    /**
     * 输入内容同步
     * @return {Void}
     */
    _pro.__onInput = function(_event){
        if (!_event.toggled){
            _v._$stop(_event.event);
            this.__nipt.value = this.__ntxt.innerText;
            this.__nipt.select();
        }
    };
    /**
     * 文字栏设置
     * @return {Void}
     */
    _pro.__onFontSetting = function(){
        _x._$$Font._$allocate(this.__sopt);
    };
    /**
     * 更新文字
     * @return {Void}
     */
    _pro.__onTextSetting = function(_event){
        _v._$stop(_event);
        _e._$addClassName(this.__ntip,this.__topt.clazz);
        var _value = this.__nipt.value.trim()||'文字自定义分类条';
        this.__ntxt.innerText = _value;
    };
    /**
     * 取消文字编辑
     * @return {Void}
     */
    _pro.__onTextCancel = function(){
        _e._$addClassName(this.__ntip,this.__topt.clazz);
    };
    /**
     * 更新设置
     * @param {Object} _data
     */
    _pro.__doUpdateSetting = function(_data){
        // sync text content
        this.__ntxt.innerText = _data.textContent||'文字自定义分类条';
        // sync height
        if (_data.height!=null){
            var _value = _data.height+'px';
            _e._$style(
                this.__wrap,{
                    height:_value,
                    lineHeight:_value
                }
            );
        }
        // sync font and background
        this.__doUpdateFontSetting(_data);
    };
    /**
     * 更新文字设置
     * @return {Void}
     */
    _pro.__doUpdateFontSetting = function(_options){
        _u._$merge(
            this.__sopt,_u._$fetch({
                fontFamily:'Arial',
                fontWeight:'normal',
                fontSize:20,
                fontColor:'#000000',
                textAlign:'center',
                bgColor:'#E5E5E5',
                opacity:100,
                borderColor:'#aaaaaa',
                borderStyle:'none',
                borderWidth:0
            },_options)
        );
        // update background
        _e._$style(
            this.__nshw,{
                opacity:this.__sopt.opacity/100,
                backgroundColor:this.__sopt.bgColor
            }
        );
        // update font
        _e._$style(
            this.__ntip,{
                fontFamily:this.__sopt.fontFamily,
                fontWeight:this.__sopt.fontWeight,
                fontSize:this.__sopt.fontSize+'px',
                textAlign:this.__sopt.textAlign,
                color:this.__sopt.fontColor
            }
        );
        // update border
        _e._$style(
            this.__wrap,{
                borderWidth:this.__sopt.borderWidth+'px',
                borderColor:this.__sopt.borderColor,
                borderStyle:this.__sopt.borderStyle
            }
        );
    };
    /**
     * 取布局信息
     * @return {Object} 布局信息
     */
    _pro._$getLayout = function(){
        return {
            height:this.__wrap.offsetHeight,
            textContent:this.__ntxt.innerText,
            spaceTop:_z._$getNumber(this.__body,'paddingTop'),
            textAlign:_e._$getStyle(this.__ntip,'textAlign'),
            fontFamily:_e._$getStyle(this.__ntip,'fontFamily'),
            fontWeight:_e._$getStyle(this.__ntip,'fontWeight'),
            fontSize:_z._$getNumber(this.__ntip,'fontSize',20),
            fontColor:_z._$getColor(this.__ntip,'color'),
            bgColor:_z._$getColor(this.__nshw,'backgroundColor'),
            opacity:_z._$getOpacity(this.__nshw),
            borderWidth:_z._$getNumber(this.__wrap,'borderWidth'),
            borderStyle:_e._$getStyle(this.__wrap,'borderStyle'),
            borderColor:_z._$getColor(this.__wrap,'borderColor')
        };
    };
    
    // regist widget impl
    _w._$regist('1',_p._$$Widget);
    
    return _p;
});
