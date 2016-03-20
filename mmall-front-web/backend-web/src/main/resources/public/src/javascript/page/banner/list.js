/**
 * banner管理
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/banner/list.js'
    ],
    function(_ut,_v,_e,Module,BannerList,p) {
        var pro;

        p._$$BannerListModule = NEJ.C();
        pro = p._$$BannerListModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);

            this.__skuListInPick = new BannerList({
                url: "/schedule/banner/getlist.json",
                api: "/schedule/banner/"
            }).$inject(".j-bannerlist", 'after');
        };
        

        p._$$BannerListModule._$allocate();
    });