/* 
 * ------------------------------------------
 * 帮助中心-新手指南
 * @version  1.0
 * @author   zzj(hzzhangzhoujie@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/element',
    'pro/widget/module',
    'util/tab/tab',
    'pro/extend/util',
    'pro/components/scrollspy/scrollspy',
    'util/chain/chainable'
],function(_k,_e,_m,_t,_,ScrollSpy,$,_p,_o,_f,_r,_pro){
    /**
     * 页面模块
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 模块初始化
     * @param {Object} _options
     */
    _pro.__init = function(){
        this.__super();
        this.__type = window.__data__.type || 0;
        this.__tab = _t._$$Tab._$allocate({
            list:_e._$getChildren('tab-box'),
            index:this.__type,
            selected:'sel',
            event:'mouseover',
            onchange:function(_event){
            	var _node = _e._$get('icon-nav');
                if(_event.last !=undefined){
                	_e._$delClassName(_node,'icon-nav-'+_event.last);
                }
                _e._$addClassName(_node,'icon-nav-'+_event.index);
                if(_event.last ==undefined){
                	_.smoothTo('.part-'+(_event.index+1),600);
                }
                
            }._$bind(this)
        });
        
        this.__scrollspy = new ScrollSpy({
            data:{
                elem: nes.one(".j-spy")
            }
            
        })
    };
   
    
    _p._$$Module._$allocate();
});
