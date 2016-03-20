NEJ.define([
    'pro/extend/util',
    'base/element',
    'base/util',
    'lib/util/ajax/rest',
    'text!./success.html',
    'pro/widget/BaseComponent'
],function(_,_e,_u,_j,_html,Component){
    return Component.extend({
        template:_html
    });
});