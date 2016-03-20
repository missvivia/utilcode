/**
 * 仓库统计
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define([
  "text!./list.html",
  "text!./template/export-1.html",
  "text!./template/export-2.html",
  "text!./template/export-3.html",
  "text!./template/export-4.html",
  "text!./template/export-5.html",
  "text!./template/export-6.html",
  "text!./template/export-7.html",
  'base/util',
  "{pro}components/ListComponent.js",
  'pro/components/notify/notify',
  "{pro}extend/util.js"
  ], function(tpl,tt1,tt2,tt3,tt4,tt5,tt6,tt7,_u,ListComponent,notify,_){
	
  var tpList = [tt1,tt2,tt3,tt4,tt5,tt6,tt7];
  var format = function(_time, format){
        format = format || "yyyy-MM-dd";
        if (!_time) return '';
        _time = parseInt(_time);
        return _u._$format(new Date(_time),format);
    };
  var toFixed = function(_num){
        return (_num*100).toFixed(2) +'%';
    };
  var ActList = ListComponent.extend({
    url: "/storage/warehouse/list",
    template: tpl,
    config: function(data){
        _.extend(data, {
          total: 1,
          current: 1,
          limit: 10,
          list: []
        });
        this.content = tpList[parseInt(data.condition.type)-1];
        this.$watch(this.watchedAttr, function(){
          if(this.shouldUpdateList()) this.__getList();
        });
	  },
	refresh:function(_data){
        this.data.current = 1;
        if(_data.lastId){
    	  _data.lastId = null;
        }
        this.data.condition = _data;
        this.content =tpList[parseInt(this.data.condition.type)-1];
        this.$emit('updatelist');
    },
  }).filter('format',format).filter('toFixed',toFixed);
  return ActList;

});