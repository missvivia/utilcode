/**
 * xx平台商品编辑——商品添加页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
    '{lib}util/placeholder/placeholder.js',
    'pro/extend/config',
    '{lib}base/event.js',
    '{lib}base/element.js',
    'util/ajax/xdr',
    '{lib}util/selector/cascade.js',
    '{pro}extend/request.js',
    '{pro}extend/util.js',
    "{lib}util/form/form.js",
    '{pro}components/notify/notify.js',
    '{pro}components/product/prodParam.js',
    '{pro}components/product/imageView.js',
    '{pro}components/product/sizeTemplate.js',
    '{pro}components/product/product.helper.js',
    '{pro}widget/editor/editor.js',
    '{pro}widget/module.js', 
    '{lib}util/query/query.js',
    '{lib}util/file/select.js',
    '{pro}page/product/modal.batch/modal.batch.result.js'
    ],
    function(
      ep,
      config,
      v,
      _e,
      j,
      ut1,
      request, 
      _ , 
      ut,
      notify, 
      ProdParam, 
      ImageView, 
      SizeTemplate, 
      ProductHelper, 
      Editor, 
      Module, 
      e, 
      s,
      BatchResultModal) {

      var using = NEJ.P;
      var merge = NEJ.X;
      var dw = using('dd.widget');

      // 请求参数
      var url = {
        //@TODO
        "SAVE": "/rest/product",
        "GET_SIZE": "/rest/product/getSizeTemplate",
        "GET_PARAM": "/rest/product/getProductParams",
        "REDIRECT": "/product/list"
      };
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
          var schedule = this.data.schedule;
          if(schedule){
            url.SAVE = "/rest/schedule/product";
            // 修改跳转按钮的链接和保存后跳转的地址
            nes.one('a.j-redirect').href = 
              (url.REDIRECT = "/schedule/add?id=" + schedule.id);
          }
          this.__initBatch();
      };
      
      pro.__initBatch = function(){
          // 有个label是弹框
      	var _labelList = nes.all('label[data-url]',this.card);
      	for(var i=0,l=_labelList.length;i<l;i++){
      		s._$bind(_labelList[i], {
      			parent:this.card.parentNode,
	                name: 'myfile',
	                multiple: false,
	                onchange: this.__onFileUpload._$bind(this,_labelList[i])
	            });
      	}
      };
      pro.__onFileUpload = function(_label,_event){
      	var form = _event.form;
			form.action =  _e._$dataset(_label,'url');
			j._$upload(form,{
				onload:function(_json){
					if(_json.code==200){
						form.reset();
						//notify.show('操作成功');
						var modal = new BatchResultModal({data: {title: "批量导入结果",message:_json.result.join('\r\n')}});
						modal.$on('confirm',function(){
							location.reload();
						})
					} else{
						form.reset();
						//new BatchResultModal({data: {title: "批量导入结果",type:0,result:result.msg.join('\r\n')}});
						notify.show("导入格式错误，请确认导入模板");
					}
				}._$bind(this),
				onerror:function(){
					notify.show("导入格式错误，请确认导入格式");
				}
			});
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
      };

      pro.__initDom = function(){
    	  this.card = _e._$get('actionCard');
        var data =this.data;

        // // 全局placeholder
        nes.all("[placeholder]").forEach(function(node){
          ep._$placeholder(node);
        });

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
            schedule: data.schedule,
            categoryId: data.categories && data.categories[2] || "NONE",
            sizeTemplateId: data.sizeTemplateId
          }

        }).$inject(".j-sizeTemplate");

        // 初始化尺码助手
        this.__prodhelper = new ProductHelper({
          data: {
            helperId: data.helperId,
            isShowSizePic: data.isShowSizePic
          }
        }).$inject(".j-helperSize");

        /**
         * 初始化表单生成
         */
        this.__prodparam = new ProdParam({
          data: { productParamList: data.productParamList },
          events: {
            "oninvalid": function(ev){
              _.smoothTo(".j-fieldgen");
              notify.notify({
                message: "必填商品参数缺失",
                type: "error",
                duration: 3000
              });
            }
          } 
        }).$inject(".j-fieldgen");

        // 基础form表单, 即头部的所有
        this.__baseform = ut._$$WebForm._$allocate({
          form: nes.one(".j-baseform"),
          oninvalid: function(ev){
            var msg = typeof ev.code === "number"? config.FORM_CODE[ev.code]: ev.code;
            if(!msg){
               msg = dom.attr(ev.target, 'data-msg');
            }else{
               msg = "【" +(dom.attr(ev.target, 'data-name') || "") + "】" + msg;
            }

            if(!msg) return;
            notify.notify({
              message: msg,
              type: "error",
              duration: 3000
            });
          },
          oncheck: function(ev){
            var form = ev.form;
            var marketPrice, salePrice;
            switch(ev.target.name){
              case 'marketPrice':
              case 'salePrice':
                marketPrice = form.marketPrice && form.marketPrice.value;
                salePrice = form.salePrice && form.salePrice.value;
                if(marketPrice && salePrice){
                  if(parseFloat(marketPrice) < parseFloat(salePrice)){
                    ev.value = "销售价格不能大于正品价格";
                  }
                }
                if(ev.target.name === "marketPrice" && parseFloat(marketPrice, 10) <=0){
                  ev.value="正品价格必须大于0";
                }
                if(ev.target.name === "salePrice" && parseFloat(salePrice, 10) <=0){
                  ev.value="销售价格必须大于0";
                }
            }


          }
        });


        this.__initImageView();

      };

      pro.__initEvent = function(){
        var self = this;
        //@TODO remove
        var submit = this.$("a.j-submit");
        var preview = this.preview = this.$("form.j-preview");

        dom.attr(preview, "action", config.MAINSITE + "/preview");

        dom.on(submit, "click", this.__onSubmit._$bind(this));
        dom.on(preview, "submit", this.__onPreview._$bind(this));

        v._$addEvent('cardbtn','click',this.__showOrHideCard._$bind(this,'actionCard'));
        v._$addEvent('cardbtn1','click',this.__showOrHideCard._$bind(this,'actionCard1'));
        v._$addEvent(document,'click',this.__hideCard._$bind(this));
      };
      pro.__showOrHideCard = function(_id,_event){
      	var _elm = v._$getElement(_event);
      	if(!(_elm.tagName=='A'||_elm.tagName=='LABEL')){
      		v._$stop(_event);
      	}
      	_e._$delClassName(_id,'f-dn');
      };
      pro.__hideCard = function(){
      	_e._$addClassName('actionCard','f-dn');
      	_e._$addClassName('actionCard1','f-dn');
      };

      // Category发生改变时
      pro.__onChangeSelect = function(ev){

        var data = this.data;
        var self = this, type;

        if(!data.categories) data.categories = [];
        
        var category1 = ev.category1;
        var category2 = ev.category2;
        var category3 = ev.category3;

        var preCateGory1 = data.categories[0];
        var preCateGory3 = data.categories[2];

        if(category3 != preCateGory3 && ev.category3 && preCateGory3) {
          this.__doRefreshSizeTemplate(category3);
          this.__doRefreshProdParams(category3);
        }

        data.categories[0]= category1;
        data.categories[1]= category2;
        data.categories[2]= category3;
      };

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
              });
          }
        });
      };
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
            if(json && json.code == 200){
              notify.notify("模板数据更新成功");
              self.__sizeTemplate.refresh(json.result, category3);
            }else{
              notify.notify({
                message: json && json.message || "模板数据更新失败",
                type: "error"
              });
            }
          },
          onerror: function(json){
            notify.notify({
              message: json && json.message || "模板数据更新失败",
              type: "error"
            });
          }
        });
      };

      /**
       * 初始化图片上传区域
       */
      pro.__initImageView = function(){
        var images = this.__imageViews = {};
        var data = this.data;
        var map = {
          "product": {
            name: "prodShowPicList",
            limit: 16
          },
          "list": {
            name: "listShowPicList",
            limit: 2
          }
        };
        var keys = Object.keys(map);
        keys.forEach(function(name){
          var selector = ".j-image-"+name;
          var config = map[name];
          var imageview = new ImageView({
            data: {
              target: selector,
              clazz: 'm-imgview-'+name,
              limit: config.limit,
              imgs: data[config.name] || (data[config.name] = [])
            }
          }).$inject(selector);

          images[name] = imageview;
        });
      };

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
        console.log(this.__prodparam);
        baseData.lowCategoryId = baseData.category3;
        // 图片们
        var prodShowPicList = data.prodShowPicList.map(function(img){
          return img.src;
        });
        var listShowPicList = data.listShowPicList.map(function(img){
          return img.src;
        });

        if(listShowPicList.length<2){
          _.smoothTo(".j-image-list");
          notify.notify({
            type: "error",
            message: "必须上传两张商品列表图"
          });
          return null;
        }
        // 自定义html
        var customEditHTML = this.__editor._$getContent();
        // 参数列表

        if(!this.__prodparam._$checkValidity()){
          return;
        }
        var prodParamObj = this.__prodparam._$data();

        var productParamList = this.__prodparam.data.productParamList;

        for(var i = 0, plen = productParamList.length; i < plen ; i++){
          var prodParam = productParamList[i];
          if(prodParam.type === 4 && prodParam.isRequired && !prodParamObj[prodParam.id]){
            _.smoothTo(".j-fieldgen");
            notify.notify({
              type: "error",
              message: prodParam.name + "不能为空"
            });
            return;
          }
        }

        var productParamList = [];

        for(var i in prodParamObj){
          if(prodParamObj.hasOwnProperty(i)){
            var type = _.typeOf(prodParamObj[i]);
            productParamList.push({
              id: i,
              value: type === 'array'? JSON.stringify(prodParamObj[i]): prodParamObj[i]
            });
          }
        }
        var template = data.template;

        var formdata =  _.extend(baseData, {
          prodShowPicList: prodShowPicList,
          listShowPicList: listShowPicList,
          customEditHTML: customEditHTML,
          productParamList: productParamList
        });
        var sizeData = this.__sizeTemplate.getData();
        if(sizeData.code == 400){
          notify.notify({
            type: "error",
            message: sizeData.message
          });
          _.smoothTo('.j-sizeTemplate');
          return
        }else{
          _.extend(formdata, sizeData ,true);
        }

        formdata.lowCategoryId = formdata.category3;

        delete formdata.category1;
        delete formdata.category2;
        delete formdata.category3;

        //是否显示那张尺寸图
        formdata.isShowSizePic = nes.one("#isShowSizePic").checked? 1: 0;

        // 前端
        _.extend(formdata, {
          sizeTemplateId: 0,
          sizeHeader: [],
          skuList: [],
          skuList2: []
        });

        if(this.__prodhelper.data.useHelper){
          var selected = this.__prodhelper.data.selected;
          if( !selected ){
            notify.notify({
              "type": "error",
              "message": "您勾选了使用尺码助手但是还未选择"
            });
            return;
          }else{
            formdata.helperId = selected.id;
          }
        }
        return formdata;
      };
      // 预览，我们post这个商品数据提供其预览
      pro.__onPreview = function(ev){
        ev.preventDefault();
        var product = this.data.product;
        var data = this.__gatherData();
        var preview = this.preview;

        if(!data) return;
        if(consts.ID) data.id = consts.ID;
        if(this.data.schedule) data.scheduleId = this.data.schedule.id;

        request("/previewUnSavePage",{
          method: "POST",
          data: data,
          sync:true,
          progress: true,
          onload: function(json){
            if(json.code == 200){
              preview["product"].value = JSON.stringify(json.result);
              preview.submit();
            }
          }
        });

        

      };
      /**
       * 点击发送回调
       * @return 
       */
      pro.__onSubmit = function(ev){
        if(dom.hasClass(ev.target, 'disabled')){
          return
        }


        var data = this.__gatherData();

        if(!data) return;

        dom.addClass(ev.target, 'disabled');
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
      };
      /**
       * 保存后回调函数
       * @return {[type]} [description]
       */
      pro.__onAfterSaveProduct = function(json){
        var error; 
        if(json.code === 200){

          notify.notify({
            type: "success",
            message: "保存商品成功，2s后返回商品列表"
          });

          setTimeout(function(){
            if(url.REDIRECT) location.href = url.REDIRECT;
          }, 2000);
        }else{
          // 505: 条形码重复
          if(json.code == 505){
              error = "您编辑的条形码与其他商品重复";
              _.smoothTo('.j-sizeTemplate');
          }

          dom.delClass(nes.one("a.j-submit"), 'disabled');
          notify.notify({
            type: "error",
            message: error || json.message || "保存产品失败"
          });
        }
      };

      $$CustomModule._$allocate();

      return $$CustomModule;
});