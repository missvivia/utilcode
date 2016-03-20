/**
 * 资料下载
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js'
    ],
    function(_ut,_v,_e,Module,p) {
        var pro;

        p._$$BannerListModule = NEJ.C();
        pro = p._$$BannerListModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
        };
        

        p._$$BannerListModule._$allocate();
    });