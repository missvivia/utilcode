/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
        '{lib}base/event.js',
        '{lib}base/element.js',
        'util/chain/chainable',
        '{pro}widget/module.js',
        '{pro}components/brand/brandDisplayList.js'
    ],
    function(_ut, _v, _e, $,Module,SizeList,p,o,f,r) {
        var _pro;

        p._$$BrandDisplayModule = NEJ.C();
        _pro = p._$$BrandDisplayModule._$extend(Module);

        _pro.__init = function(_options) {
            this.__super(_options);
            this.__list = new SizeList({data:{limit:9}});
            this.__list.$inject("#list");
        };

        // 控件重复使用重置过程
        _pro.__reset = function(_options) {
            this.__super(_options);
        };
        // 控件回收销毁过程
        _pro.__destroy = function() {
            this.__super();
        };

        p._$$BrandDisplayModule._$allocate();
    });
