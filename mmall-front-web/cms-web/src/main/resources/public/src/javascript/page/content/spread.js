/**
 * xx平台活动编辑——商品添加页
 * author (hzwuyuedong@corp.netease.com)
 */

define([
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/content/list.js'
  ],
  function(_v, _e, Module, c) {
    var _pro,

      $$ContentModule = NEJ.C(),
      _pro = $$ContentModule._$extend(Module);

    _pro.__init = function(_options) {
      this.__supInit(_options);
      /*this.__form = _e._$get('searchform');
      var _list = _e._$getByClassName(this.__form, 'j-flag'),
        i = 0;
      this.__area = _list[i++];
      this.__date = _list[i++];
      this.__sbtn = _list[i++];*/
    };

    $$ContentModule._$allocate({});

    return $$ContentModule;
  });