/**
 * 图片列表 + 上传组件
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */
define([
  'pro/extend/config',
  'pro/extend/util',
  'pro/components/notify/notify',
  'text!./hview.html',
  "pro/widget/BaseComponent"
  ], function(config, _, notify, tpl, BaseComponent){

    var REST_URL = "/rest/helpers"

  var HelperView = BaseComponent.extend({
    template: tpl,
    data: {
      // 默认颜色配置
      colorMap: config.helpColors
   },
    config: function(data){
      _.extend(data, {
          vaxis: {name:'身高cm', list: []},
          haxis: {name:'体重kg', list: []},
          body: [],
          colors: []
      })
      var vlen = data.vaxis.list.length,
        hlen = data.haxis.list.length,
        body = data.body;

      for(var i = 0; i < vlen; i++){
        if( !body[i] ) body[i] = [];
        for(var j = 0; j < hlen; j++){
          if( body[i][j] == null ) body[i][j] = ""
        }
      }
      // 重置color样式
      this.resetColor();
    },
    init: function(){
      this.supr();
    },
    // 这个主要是color的计算
    resetColor: function(){
      var data = this.data;
      var body = data.body;
      var vlen = body.length;
      var colors = {}, row, val;

      for(var i = 0; i < vlen; i++){
        row = body[i];
        for(var j = 0, hlen = row.length; j < hlen; j++){
          val = row[j].trim();
          if( val){
            // 如果没有就
            if(!colors[val] ){
              colors[val] = {weight: j+i}
            }else{ //如果有就按第一个取到的值定
              if(j+i < colors[val].weight) colors[val].weight = j + i;
            }
            
          }
        }
      }
      data.colorHash = {};
      data.colors = Object.keys(colors).sort(function(a, b){
        return colors[a].weight - colors[b].weight;
      })
      data.colors.forEach(function(color, index){
        data.colorHash[color] = data.colorMap[index];
      })
    },
    change: function(row, col, ev){
      var value = ev.target.value;
      this.data.body[row][col] = value;
      this.resetColor();
    },
    select: function(value){
      this.data.selected = value;
    }
  });

  return HelperView;

})