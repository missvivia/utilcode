NEJ.define([
    'base/klass',
    'pro/widget/module',
    'base/element',
    '{lib}base/event.js',
    'pro/page/feedback/feedback',
    'pro/page/feedback/success'
],function(_k,_w,_e,_v,FeedBack,Success,_p,_o,_f,_r){
    var _pro;
    _p._$$Feedback = _k._$klass();
    _pro = _p._$$Feedback._$extend(_w._$$Module);
    _pro.__init = function(){
        this.__super();
    };
    _pro.__reset = function(_options){
        this.__feedback = new FeedBack();
        this.__feedback.$inject('#form');
        this.__feedback.$on("succeeded",this.__onSucceeded._$bind(this));
        _e._$addClassName(_e._$get("success"),"f-dn");
        _e._$delClassName(_e._$get("form"),"f-dn");
    };
    _pro.__onSucceeded = function(){
        if(!!this.__success) {
            delete this.__success;
            _e._$clearChildren('success');
        }
        this.__success = new Success();
        this.__success.$inject('#success');
        _e._$delClassName(_e._$get("success"),"f-dn");
        _e._$addClassName(_e._$get("form"),"f-dn");
    };
    _p._$$Feedback._$allocate({});
    return _p;
});
