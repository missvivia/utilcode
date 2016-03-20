/**
 * 省份选择列表
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */


define([
  "{pro}extend/util.js?v=1.0.0.0",
  "{pro}components/form/btnSelect.js?v=1.0.0.0",
  "{pro}components/notify/notify.js?v=1.0.0.0",
  "{pro}extend/config.js?v=1.0.0.0"
  ], function(_, BtnSelect, notify, config){

  /**
   * @extends BtnSelect
   * @return 
   */

  // 缓存城市列表即当前页面只加载一次.
  var ProvinceSelect  = BtnSelect.extend({
    name: "province-select",
    url: "/promotion/getProvinceList",
    config: function(data){
      this.supr(data);
      _.extend(data,{
        placeholder: "加载站点ing..."
      })
      if(config.CITY_LIST){
        onLoadList({
          code: 200,
          result: config.CITY_LIST
        })
      }else{
        this.$request(this.url, {
          onload: this.onLoadList._$bind(this),
          onerror: this.onLoadList._$bind(this)
        })  
      }

    },
    init: function(){

      this.supr();

    },
    convertCity: function(cities){
      for(var i = 0 ,len = cities.length ; i < len; i++){
        cities[i].value = cities[i].id;
      }

      return cities;
    },
    onLoadList: function(json){
      console.log("haha")
      var data =this.data;
      if(json && json.code === 200){
        data.options = this.convertCity(json.result);
        data.placeholder = "请选择城市";
      }else{
        notify.notify({
          type: "error",
          message: "获取城市列表失败"
        })
        data.placeholder = "获取城市失败!";
        data.theme = "danger";
      }
    }
  })

  return ProvinceSelect;

})