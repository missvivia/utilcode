/**
 *
 * 气泡提示实现文件
 * @author cheng-lin(cheng-lin@corp.netease.com)
 *
 */
NEJ.define([
    'text!./bubble.html',
    'pro/extend/util',
    'pro/extend/config',
    'base/element',
    'base/util'
],function(_html,_,config,_e,_u,_p,_o,_f,_r){
    var Remind = Regular.extend({
        template: _html,
        config: function(data){
          _.extend(data, {
            content: '',
            clazz:''
          })
        }
    });

    return Remind;
});