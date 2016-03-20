/**
 * xx平台商品编辑——商品添加页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
		'{lib}base/util.js',
        '{lib}base/event.js',
        '{lib}base/element.js',
        'util/ajax/xdr',
        '{lib}util/selector/cascade.js',
        '{lib}util/placeholder/placeholder.js',
        '{pro}extend/config.js',
        '{pro}extend/request.js',
        '{pro}extend/util.js',
        "{lib}util/form/form.js",
        '{pro}components/notify/notify.js',
        "{pro}widget/layer/window.js",
        '{pro}components/item/prodParam.js',
        '{pro}components/item/imageView.js',
        '{pro}components/item/sizeTemplate.js',
        '{pro}components/item/product.helper.js',
        '{pro}widget/editor/editor.js',
        '{pro}components/datepicker/datepicker.js',
        '{pro}widget/module.js',
        '{lib}util/query/query.js',
        '{lib}util/file/select.js',
        '{pro}page/item/modal.batch/modal.batch.result.js',
        '{pro}components/item/create/createProductModal.js',
        '{pro}components/modal/modal.js?v=1.0.0.1',
        '{lib}util/chain/NodeList.js?v=1.0.0.0',
        '{pro}widget/layer/sure.window/sure.window.js'
    ],
    function(ut,v,e,j,ut1,ep,config,Request,_,ut2,notify,Dialog,ProdParam,ImageView,SizeTemplate,ProductHelper,Editor,DatePicker,Module,Query,f,BatchResultModal,ProductModal,Modal,$,SureWindow) {
        
        var using = NEJ.P;
      	var merge = NEJ.X;
      	var dw = using('dd.widget');
      
        $$CreateModule = NEJ.C(),
        pro = $$CreateModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__getNodes();
            this.__addEvent();
            this.__initImageView();
            this.__initWebForm();
            this.__initEditor();
            this.__initDate();          
        };
        
        pro.__getNodes = function(){
        	this.form = e._$get('dataform');
        };
        
        pro.__addEvent = function(){
            v._$addEvent('searchBarCodeBtn','click',this.searchWithBarCode._$bind(this));
            v._$addEvent('submit','click',this.__submit._$bind(this));
            v._$addEvent('submitAndAdd','click',this.__submitAndAdd._$bind(this));
            v._$addEvent('applySale','click',this.__applySale._$bind(this));
            v._$addEvent('importItemBtn','click',this.__importItem._$bind(this));
            
            // 上传图片
            f._$bind('addProductPic', {
                name: 'img',
                multiple: false,
                accept:'image/*',
                parent:document.body,
                onchange: this.__onProductPicUpload._$bind(this)
            });
        };
        
		pro.__onProductPicUpload = function(event){        	
        	var form = event.form;
        	form.action = config.UPLOAD_URL;
        	j._$upload(form,{onload:function(result){
        		var a = e._$create('a');
        		a.href=result.result[0].imgUrl;
        		e._$dataset(a,'lightbox',name);
        		var img = e._$create('img');
        		img.src= result.result[0].imgUrl;
        		a.appendChild(img);
        		
        		var list = e._$get('productPicList');
        		list.insertAdjacentElement('afterBegin',a);
        		
        		//this.form[name].value =  result.result[0].imgUrl;
        		//this.__frontImage = result.result[0].imgUrl;
        	}._$bind(this),
        	onerror:function(e){
        		notify.show('上传图片可能超过2M');
        	}});
        }
        
        pro.__initWebForm = function(){
        	this.webForm = ut2._$$WebForm._$allocate({
                form:this.form,
                oncheck:function(event){
            	  
                }._$bind(this)
            });
        };
        
        pro.__initImageView = function(){
            
            var selector = '.j-image-product';
            var imageview = new ImageView({
                data: {
                    target: selector,
                    clazz: 'j-image-product',
                    limit: 5,
                    imgs: []
                }
            }).$inject(selector);

            this.__productImageView = imageview;
      	};
      
        pro.__initDate = function(){
            this.__picker = [];
        	ut._$forEach(
                e._$getByClassName(this.form,'j-datepick'),
                function(_parent,_index){
    	                var _name = e._$dataset(_parent,'name'),
    	                    _value = parseInt(e._$dataset(_parent,'value')),
    	                    _time = e._$dataset(_parent,'time'),
       	                    _picker = new DatePicker({
    	                        data:{name:_name,select:_value,time:_time}
    	                    });
                        this.__picker.push(_picker);
    	                _picker.$inject(_parent);
    	                this.form[_name].defaultValue = _value;
                }._$bind(this)
            );
        };

        pro.__initEditor = function(){
        	this.__editor = dw._$$Editor._$allocate({
            	parent: nes.one(".m-editor"),
            	clazz: 'editor',
            	focus: false,
            	content: nes.one('.j-editor').value
        	});
        };
        
        // 根据条形码搜索单品
        pro.searchWithBarCode = function(){
            var elm = e._$get('searchBarCodeField');
            var barCode = elm.value;
            if (barCode.length==0) {return};

            Request("/item/product/searchSPU?spuBarCode="+barCode,{
                method:'GET',
                type:'json',
                onload:function(_json){
                    var data = _json.result
                    this.__showSearchSPU(data);
                }._$bind(this),
                onerror:function(_error){
                    this.__showSearchSPU();
                }._$bind(this)
            })
        };

        pro.__showSearchSPU =function(data){
        	if(!data){
        		e._$addClassName(this.form,'hidden');
            	e._$addClassName('buttonTool','hidden');
            	notify.show('搜索不到条形码对应的单品！');
        	}
        	
            this.sup = data;
            
            var brandName = e._$get('itemSPUId');
            brandName.value = this.sup.spuId;
            
            var spuName = e._$get('spuName');
            spuName.value = this.sup.spuName;
            
            var categoryNormalName = e._$get('categoryNormalName');
            categoryNormalName.innerHTML = this.sup.categoryNormalName;

            var brandName = e._$get('brandName');
            brandName.innerHTML = this.sup.brandName;

            /**
            // 暂时去掉商品模型
            if (this.sup) {
                this.__searchProductModelWithCategoryId(this.sup.categoryNormalId);
            }*/
            
            // 显示form 表单和提交按钮
            e._$delClassName(this.form,'hidden');
            e._$delClassName('buttonTool','hidden');
        };

        // 根据categoryNormalId 分类ID 获取商品模型
        pro.__searchProductModelWithCategoryId = function(_categoryId){
        	console.log('search categoryid='+_categoryId);
        	
            if(!this.__productModel){
                this.__productModel = new ProductModal({
                       data:{categoryId:_categoryId}
                }).$inject('#productModelList');
            }
            else {
                
            }
        };
        
        pro.__getData = function(data,status){
        	// 没有搜索到单品,或商品模型加载失败 暂时去掉
        	/**
        	if(!this.sup || !this.__productModel){
        		notify.show('单品模型加载失败');
        	} 
        	*/
        	
        	// 商品状态 不用上传
        	//data['prodStatus'] = status;
        	
            // 获取图片
            var images = this.__productImageView.data.imgs;
            var productPicList = [];
            for(var i=0;i<images.length;i++){
                productPicList.push(images[i]);
            }
            data['picList'] = productPicList;

            // 获取批发价格区间，第一期不做价格区间
            var priceList = [];
            var price = new Object();
            price.prodMinNumber = data['batchNum'];
            price.prodPrice = data['price'];
            priceList.push(price);
            data['priceList'] = priceList;
            delete data['price'];
            

            // 获取多规格库存
            var stockList = [];
            var stock = new Object();
            stock.speciIdMap = {};    // 规格-选项id对应map，单一库存填空
            stock.productNum = data['productCount'];
            stock.attentionNum = data['productMinCount'];
            stock.prodInnerCode = data['productInnerCode'];
            stockList.push(stock);
            data['speciList'] = stockList;
            delete data['productCount'];
            delete data['productMinCount'];
            delete data['productInnerCode'];

			// 获取模型 暂时屏蔽
			/**
            var productModelList = [];
            var modelDiv = e._$get('productModelList');
            var modelList = e._$getByClassName(modelDiv,'form-group');
            
            for (var i = 0; i < modelList.length; i++) {
                var parameterId = e._$dataset(modelList[i],'parameterid');

                var prodParam = new Object();
                prodParam.parameterId = parameterId;

                var values = data[parameterId];
                if(ut._$isArray(values)){

                    var optionList = [];
                    for (var i = 0; i < values.length; i++) {
                        var option = new Object();
                        option.isCheck=1;
                        option.paramOptionId = values[i];
                        optionList.push(option);           
                    }
                    
                    prodParam.optionList = optionList;
                }
                else if(ut._$isString(values)){
                    var option = new Object();
                    option.isCheck=1;
                    option.paramOptionId = values;

                    prodParam.optionList = [option];
                }
    			
    			if(prodParam.optionList && prodParam.optionList.length>0){
    				productModelList.push(prodParam);	
    			}
    			
                delete data[parameterId];
            }
            
            if(productModelList.length>0){
            	data['paramList'] = productModelList;
            }
    		*/

            
            // 自定义html
            var customEditHTML = this.__editor._$getContent();
            var detail = {};
            detail.customEditHTML = customEditHTML;
            detail.prodParamJson = '';
            data['prodDetail'] = detail;
            data["productName"] = document.getElementById("spuName").value;
        };

		// 添加商品设置为不同的状态
		pro.__submitDataWithStatus = function(_status){
			var data = this.webForm._$data();
            var pass = this.webForm._$checkValidity();

            if(pass){
            	this.__getData(data,_status);
            	
                Request('/item/product/add',{
                    data:data,
                    method:'POST',
                    type:'JSON',
                    onload:function(json){
                    	if(json.code==200){
                    		notify.show({
                    			'type':'success',
	    						'message':json.message
                    		});
                    		location.href='/item/product/list?type=1';
                    	}else{
                    		notify.show({
                    			'type':'info',
	    						'message':json.message
                    		});
                    	}
                    },
                    onerror:function(json){
                    	notify.show({
                			'type':'error',
    						'message':json.message
                		});
                    }
                })
            }
		};
		
        pro.__createSuccessed = function(_data){
        	notify.show('添加商品成功');
        };
        
        pro.__createFaild = function(_error){
        	notify.show('添加商品失败');
        	console.log('创建失败'+_error);
        };

        pro.__submit = function(){
            this.__submitDataWithStatus(1);
        };

        pro.__submitAndAdd = function(){
			this.__submitDataWithStatus(1);
        };

        pro.__applySale = function(){
			this.__submitDataWithStatus(1);
        };
        pro.__importItem = function(){
        	var that = this;
            
        	var _id = f._$bind('importItemBtn',{
		         name : "file",
		         accept : "xlsx",
		    	 onchange:function(e){
		    		 that.__onitemUpload(e);
		    	 }
		     });
       };
       pro.__onitemUpload = function(event){
           this.__loadingWin = Dialog._$allocate({
	           	clazz : "loading m-window",
	           	parent:document.body,
           })._$show();
           $(".loading .zcnt")[0].innerText = "导入中，请稍候...";	
    	   var form = event.form;
			form.action =  '/item/product/import';
			j._$upload(form,{
				type:"text",
				progress :true,
				onload:function(result){
					this.__loadingWin._$hide();
					if(result == "导入结束！"){
				           this.__sureWin = SureWindow._$allocate({
					           	title : "提示",
					           	text : result,
					           	okBtnText : "前往商品列表",
					           	cancelBtnText : "继续导入",
					           	onok : function(){
					           		location.href = "/item/product/list?type=1";
					           	}, 
					           	oncancel : function(){
					           		$("#importItemBtn")._$trigger("click");
					           	}
				           })._$show();
						return;
					}
					var list = result.split("\n");
					var html = list.join("</div><div>");
					html = "<div>" + html + "</div>";
					var modal = new Modal({
			        	data:{
			          	'title':'提示',
			          	'content':html,
			          	'width':500
			        	}
			      	});
				}._$bind(this),
				onerror:function(result){
					notify.show(result);
				}
			})
		}
        $$CreateModule._$allocate();
      	return $$CreateModule;
    });
