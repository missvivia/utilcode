/**
 * xx平台商品编辑——商品添加页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
    '{lib}util/selector/cascade.js',
    '{pro}extend/request.js',
    '{pro}extend/util.js',
    "{lib}util/form/form.js",
    '{pro}components/notify/notify.js',
    '{pro}components/product/prodParam.js',
    '{pro}components/product/imageView.js',
    '{pro}components/product/sizeTemplate.js',
    '{pro}widget/editor/editor.js',
    '{pro}widget/module.js', 
    '{lib}util/query/query.js',
    '{lib}util/file/select.js'
    ],
    function(
      ut1,
      request, 
      _ , 
      ut,
      notify, 
      ProdParam, 
      ImageView, 
      SizeTemplate, 
      Editor, 
      Module, 
      e, 
      s) {

      var using = NEJ.P;
      var merge = NEJ.X;
      var dw = using('dd.widget');

      // 请求参数
      var url = {
        //@TODO
        "SAVE": "/rest/schedule/product",
        "GET_SIZE": "/rest/product/getSizeTemplate",
        "GET_PARAM": "/rest/product/getProductParams"
      }
      // 静态变量
      var consts ={
        ID: _.getSearch().id
      } 

      var pro, dom= Regular.dom,
        $$CustomModule = NEJ.C(),
        pro = $$CustomModule._$extend(Module);

      
      pro.__init = function(_options) {
          this.data = window.__data__;
          this.__supInit(_options);
          this.__initDom();
          this.__initEvent();

      };

      /**
       * 初始化编辑器
       * @return {[type]} [description]
       */
      pro.__initEditor = function(){
        this.__editor = dw._$$Editor._$allocate({
            parent: nes.one(".m-editor"),
            clazz: 'editor',
            focus: false,
            content: nes.one('.j-editor').value
        });
      }

      pro.__initDom = function(){
        var data =this.data;
        // 级联类目
        this.__selector = ut1._$$CascadeSelector._$allocate({
            select: nes.all(".j-select select"),  
            data: data.categoryList,
            onchange: this.__onChangeSelect._$bind(this)
        });


        this.__initEditor();

        /**
         * 初始化sizeTable
         */
        this.__sizeTemplate = new SizeTemplate({

          data: {
            template: data.template || (data.template= {}),
            sizeType: data.sizeType,
            skuList2: data.skuList2,
            skuList: data.skuList,
            categoryId: data.categories && data.categories[2] || "NONE",
            sizeTemplateId: data.sizeTemplateId
          }

        }).$inject(".j-sizeTemplate");

        /**
         * 初始化表单生成
         */
        this.__prodparam = new ProdParam({
          data: { productParamList: data.productParamList },
          events: {
            "oninvalid": function(ev){
              notify.notify({
                message: "必填商品参数缺失",
                type: "error",
                duration: 3000
              })
            }
          } 
        }).$inject(".j-fieldgen");



        this.__baseform = ut._$$WebForm._$allocate({
          form: nes.one(".j-baseform"),
          oninvalid: function(ev){
            var msg = dom.attr(ev.target, "data-msg") || ev.code;
            if(!msg) return;
            notify.notify({
              message: msg,
              type: "error",
              duration: 3000
            })
          },
          oncheck: function(ev){}
        })


        this.__initImageView();

      }

      pro.__initEvent = function(){
        var self = this;
        //@TODO remove
        var submit = this.$("a.j-submit");

        dom.on(submit, "click", this.__onSubmit._$bind(this,1))

      }




      // Category发生改变时
      pro.__onChangeSelect = function(ev){

        var data = this.data;
        var self = this, type;

        console.log(ev.category1, ev.category3)
        if(!data.categories) data.categories = [];
        
        var category1 = parseInt(ev.category1);
        var category2 = parseInt(ev.category2);
        var category3 = parseInt(ev.category3);

        var preCateGory1 = data.categories[0];
        var preCateGory3 = data.categories[2];

        if(category3 !== preCateGory3 && ev.category3 && preCateGory3) this.__doRefreshSizeTemplate(category3);
        if(category1 !== preCateGory1 && ev.category1 && preCateGory1) this.__doRefreshProdParams(category1);

        data.categories[0]= category1;
        data.categories[1]= category2;
        data.categories[2]= category3;
      }

      /**
       * 重置商品参数
       */
      pro.__doRefreshProdParams = function(category1){
        var self =this;
        request(url.GET_PARAM,  {
          data: {categoryId: category1 },
          onload: function(json){
              notify.notify("商品参数表单更新成功");
              self.__prodparam.refresh(json.result);
          },
          onerror: function(json){
              notify.notify({
                message: json && json.message || "商品参数更新失败",
                type: "error"
              })
          }
        })
      }
      /**
       * 重置尺寸模版
       */
      var i = 1;
      pro.__doRefreshSizeTemplate = function(category3){
        var self = this;
        // @REMOVE
        request(url.GET_SIZE,  {
          data: {lowCategoryId: category3, productId: consts.ID || 0 },
          onload: function(json){
            notify.notify("模板数据更新成功");
            self.__sizeTemplate.refresh(json.result, category3);
          },
          onerror: function(json){
            notify.notify({
              message: json && json.message || "模板数据更新失败",
              type: "error"
            })
          }
        })
      }

      /**
       * 初始化图片上传区域
       */
      pro.__initImageView = function(){
        var images = this.__imageViews = {};
        var data = this.data;
        var map = {
          "product": {
            name: "prodShowPicList",
            limit: 3
          },
          "list": {
            name: "listShowPicList",
            limit: 2
          },
          "detail": {
            name: "detailShowPicList",
            limit: 10
          }
        }
        var keys = Object.keys(map);
        keys.forEach(function(name){
          var selector = ".j-image-"+name;
          var config = map[name];
          var imageview = new ImageView({
            data: {
              limit: config.limit,
              imgs: data[config.name] || (data[config.name] = [])
            }
          }).$inject(selector);

          images[name] = imageview;
        })
      }

      /**
       * 从零散的位置收集数据 Data
       */
      pro.__gatherData = function(){
        var data = this.data;
        //  基础表单

        if(!this.__baseform._$checkValidity()){
          _.smoothTo('.j-baseform');
          return;
        }
        var baseData = this.__baseform._$data();        
        baseData.lowCategoryId = baseData.category3;
        // 图片们
        var prodShowPicList = data.prodShowPicList;
        var listShowPicList = data.listShowPicList;
        var detailShowPicList = data.detailShowPicList;
        // 自定义html
        var customEditHTML = this.__editor._$getContent();
        // 参数列表

        if(!this.__prodparam._$checkValidity()){
          return;
        }
        var prodParamObj = this.__prodparam._$data();

        var productParamList = [];

        for(var i in prodParamObj){
          if(prodParamObj.hasOwnProperty(i)){
            productParamList.push({
              id: i,
              value: prodParamObj[i]
            })
          }
        }

        var template = data.template;

        var formdata =  _.extend(baseData, {
          prodShowPicList: prodShowPicList,
          listShowPicList: listShowPicList,
          detailShowPicList: detailShowPicList,
          customEditHTML: customEditHTML,
          productParamList: productParamList
        })
        _.extend(formdata, this.__sizeTemplate.getData(),true)

        formdata.lowCategoryId = formdata.category3;

        delete formdata.category1;
        delete formdata.category2;
        delete formdata.category3;

        // 前端
        _.extend(formdata, {
          sizeTemplateId: 0,
          sizeHeader: [],
          skuList: [],
          SKUList: [],
          skuList2: []
        })
        return formdata;
      }
      /**
       * 点击发送回调
       * @return 
       */
      pro.__onSubmit = function(){

        var data = this.__gatherData();

        if(!data) return;

        var onLoad = this.__onAfterSaveProduct._$bind(this);

        var rurl = consts.ID? (url.SAVE+"/" +consts.ID) : url.SAVE;
        var method = consts.ID? "PUT": "POST";
        request(rurl, {
          method: method,
          progress: true,
          data: data,
          onload: onLoad,
          onerror: onLoad
        });
      }
      /**
       * [ description]
       * @return {[type]} [description]
       */
      pro.__onAfterSaveProduct = function(json){
        if(json.code === 200){

          notify.notify({
            type: "success",
            message: "保存商品成功，2s后返回商品列表"
          })

          setTimeout(function(){
            debugger
            location.href = "/product"
          }, 2000);
        }else{

          notify.notify({
            type: "error",
            message: json.message || "保存产品失败"
          })
        }
      }




      $$CustomModule._$allocate();

      return $$CustomModule;
});