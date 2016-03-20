NEJ.define([
    'pro/extend/util',
    'base/element',
    'base/util',
    'lib/util/ajax/rest',
    'text!./redpacketDetail.html',
    'pro/widget/BaseComponent'
],function(_,_e,_u,_j,_html,Component){
    var format = function(_time){
        if (!_time) return '';
        _time = parseInt(_time);
        return _u._$format(new Date(_time),'yyyy.MM.dd');
    };
    return Component.extend({
        template:_html
    }).filter('format',format);
});
