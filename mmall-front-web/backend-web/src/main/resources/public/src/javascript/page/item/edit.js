define([
    '{lib}base/util.js',
	'{lib}base/element.js',
    '{lib}base/event.js',
    '{pro}components/notify/notify.js',
	'{pro}widget/module.js',
    '{pro}extend/request.js',
    '{pro}components/modal/modal.js',
    '{pro}components/item/imageView.js',
    '{lib}util/form/form.js',
    '{pro}components/datepicker/datepicker.js',
    '{pro}widget/editor/editor.js',
],function(ut,e,v,notify,Module,Request,Modal,ImageView,ut1,DatePicker,Editor){
	
    var dw = NEJ.P('dd.widget');
	var selectRadioList = {};

	$$EditModule = NEJ.C();
    pro = $$EditModule._$extend(Module);
    
    pro.__init = function(_options) {
        this.__supInit(_options);
        this.__getNodes();
        this.__addEvent();
        this.__initImageView();
        this.__initDate();
        this.__initEditor();
    };
    
    pro.__getNodes = function(){
        this.form = e._$get('dataform');

        this.webForm = ut1._$$WebForm._$allocate({
            form:this.form,
            oncheck:function(event){}._$bind(this)
            });

        // 记录单选
        /** 暂时注释
        var list = e._$getByClassName('productModelList','j-flag');
        ut._$forEach(list,function(_parent,_index){
            if((_parent.type=="radio") && (_parent.checked)){
                var paramId = e._$attr(_parent,'name');
                var paramOptionId = e._$attr(_parent,'value');
                selectRadioList[paramId] = paramOptionId;
            }
        });*/
    };

    pro.__initImageView = function(){
        var selector = '.j-image-product';
        var imageview = new ImageView({
            data: {
                target: selector,
                clazz: 'j-image-product',
                limit: 5,
                imgs:window.picList,
                edit:true
            }
        }).$inject(selector);

        this.__productImageView = imageview;
  	};
    
    pro.__initDate = function(){
    	var stime = e._$get("stime");
    	if(e._$dataset(stime,'value') == ""){
    		e._$dataset(stime,'value',(new Date()).getTime());
    	}
    	
    	ut._$forEach(
            e._$getByClassName(this.form,'j-datepick'),
            function(_parent,_index){
                var _name = e._$dataset(_parent,'name'),
                    _value = parseInt(e._$dataset(_parent,'value')),
                    _time = e._$dataset(_parent,'time'),
	                _picker = new DatePicker({
                        data:{name:_name,select:_value,time:_time}
                    });
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

    pro.__addEvent = function(){
    	v._$addEvent('onSave','click',this.__onSave._$bind(this,1));
        v._$addEvent('onShelve','click',this.__onSave._$bind(this,2));
        v._$addEvent('unShelve','click',this.__onSave._$bind(this,3));
		v._$addEvent('onBack','click',this.__onBack._$bind(this));
		v._$addEvent('onDelete','click',this.__onDelete._$bind(this));
		
		//已下架商品才可编辑限购部分
		if(window.prodStatus == 5){
			//是否限购
	        var limit = e._$getByClassName('limitList','prodLimit');
	       
	        ut._$forEach(limit, function(_parent,_index){
	        	v._$addEvent(_parent,'click',this.__modifyLimit._$bind(this,_parent,_index));
	        }._$bind(this));
	        
	        if(window.isSKULimited == 1){
	             limit[1].checked = "checked";
	        	 this.__modifyLimit(limit[1],1);
	        }
		}
		
		// 暂时屏蔽模型
		/**
        var list = e._$getByClassName('productModelList','j-flag');
        ut._$forEach(list,function(_parent,_index){
            v._$addEvent(_parent,'click',this.__modifyParam._$bind(this,_parent));
        }._$bind(this));*/
    };
    
    pro.__modifyLimit = function(elm, index){
    	var limitArea = e._$get('limitArea');
    	if(elm.checked){
    		if(index == 0){
    			e._$addClassName(limitArea,'hidden');
    		}else if(index == 1){
    			e._$delClassName(limitArea,'hidden');
    		}
    	}
    };

    pro.__modifyParam = function(elm){
        var sku = e._$get('skuId');
        var skuId = sku.value;

        var paramId = e._$attr(elm,'name');
        var paramOptionId = e._$attr(elm,'value');
		var isCheck = elm.checked?1:2;
		
        var optionList = [];
        var isRadio = false;

		// 判断是否为单选
		if(elm.type == 'radio'){
            // 修改老的选择
            var oldOptionId = selectRadioList[paramId];
            if(!oldOptionId){
            	var option = new Object();
                option.paramOptionId = paramOptionId;
                option.isCheck = 1;
                optionList.push(option);

                isRadio = true;
            }
            else if(oldOptionId != paramOptionId){
                var oldOption = new Object();
                oldOption.paramOptionId = selectRadioList[paramId];
                oldOption.isCheck = 2;
                optionList.push(oldOption);

                var option = new Object();
                option.paramOptionId = paramOptionId;
                option.isCheck = 1;
                optionList.push(option);

                isRadio = true;
            }
            else {
                return;
            }
        }
	    else {
            var option = new Object();
            option.paramOptionId = paramOptionId;
            option.isCheck = isCheck;
            optionList.push(option);

            isRadio = false;
        }
        

        var data = {};
        data.skuId = skuId;
        data.parameterId = paramId;
        data.optionList = optionList;

		
        Request("/item/product/updateParam",{
            method:'POST',
            data:data,
            onload:function(_json){
              if(_json.code==200){
                notify.show("修改商品模型成功！");
                if (isRadio) {
                	selectRadioList[paramId]=paramOptionId;
                };
              }
            },
            onerror:function(_error){
                notify.show("修改商品模型失败！");
            }
        })
    };

    // 上架商品
    pro.__onShelve = function(_event){
    	var elm = v._$getElement(_event);
    	var skuId = e._$dataset(elm,'skuid');
    	
        Request("/item/product/action?action=shelve&productSKUId="+skuId,{
            method:'GET',
            onload:function(_json){
              if(_json.code==200){
                notify.show("上架已成功！");
                //location.reload(true);
                location.href='/item/product/list?type='+window.listType;
              }
            },
            onerror:function(_error){
                notify.show("上架提交失败！");
            }
        })
    };
    
    // 下架商品
    pro.__unShelve = function(_event){
    	var elm = v._$getElement(_event);
    	var skuId = e._$dataset(elm,'skuid');

        var modal = new Modal({
            data:{
              'title':'商品下架',
              'content':'下架后，该商品将不能出售！<br/>再次上架后可以再次出售',
              'width':500}
        });

        modal.$on('confirm',function(){
            Request("/item/product/action?action=unshelve&productSKUId="+skuId,{
                method:'GET',
                onload:function(_json){
                    if(_json.code==200){
                      notify.show("下架成功！");
                      location.reload(true);
                    }
                },
                onerror:function(){
                    notify.show('下架商品失败');
                }
            })
            modal.destroy();
        }.bind(this));
      
        modal.$on('close',function(){modal.destroy();}.bind(this));
    };

    // 返回
    pro.__onBack = function(){
    	location.href='/item/product/list?type='+window.listType;
    };
    
    // 删除
    pro.__onDelete = function(_event){
    	var elm = v._$getElement(_event);
    	var skuId = e._$dataset(elm,'skuid');
    	
    	var modal = new Modal({
            data:{
              'title':'删除商品',
              'content':'确定删除商品？',
              'width':500}
        });

        modal.$on('confirm',function(){
            Request("/item/product/delete/"+skuId,{
                method:'GET',
                onload:function(_json){
                    if(_json.code==200){
                      notify.show("删除成功！");
                      location.href='/item/product/list?type='+window.listType;
                    }
                },
                onerror:function(){
                    notify.show('删除商品失败');
                }
            })
            modal.destroy();
        }.bind(this));
      
        modal.$on('close',function(){modal.destroy();}.bind(this));
    };
    
    
    pro.__getData = function(data,status){
    	
        // 商品图片只有添加和删除
        var images = this.__productImageView.data.imgs;
        var productPicList = [];
        for(var i=0;i<images.length;i++){
        	if(!images[i].prodPicId)
        	{
        		productPicList.push(images[i]);
        	}
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
        
        //已下架商品
		if(window.prodStatus == 5){
	        //删除限购radio的name值
	        delete data['prodLimit'];
	        //限购参数
	        if(data['isSKULimited'] == 1){
	        	var end = parseInt(data['limitEndTime'])+ (24*3600*1000 - 1);
	        	if(data['limitComment']==""){
	            	data['skuLimitConfigVO']={
	                	limitStartTime:data['limitStartTime'],
	                	limitEndTime:end,
	                	limitPeriod:data['limitPeriod'],
	                	skuLimitNum:data['skuLimitNum']
	                };
	            }else{
	            	data['skuLimitConfigVO']={
	                	limitComment:data['limitComment'],
	                	limitStartTime:data['limitStartTime'],
	                	limitEndTime:end,
	                	limitPeriod:data['limitPeriod'],
	                	skuLimitNum:data['skuLimitNum']
	                };
	            }
	        	delete data['limitComment'];
	            delete data['limitStartTime'];
	            delete data['limitEndTime'];
	            delete data['limitPeriod'];
	            delete data['skuLimitNum'];
	        }
		}else if(window.prodStatus == 4){
			if(window.isSKULimited == 0){
				data['isSKULimited'] = 0;
			}else if(window.isSKULimited == 1){
				data['isSKULimited'] = 1;
				var limitComment = e._$get("limitComment").innerHTML;
				if(limitComment == ""){
	            	data['skuLimitConfigVO']={
	                	limitStartTime:window.limitStartTime,
	                	limitEndTime:window.limitEndTime,
	                	limitPeriod:window.limitPeriod,
	                	skuLimitNum:window.skuLimitNum
	                };
	            }else{
	            	data['skuLimitConfigVO']={
	                	limitComment:limitComment,
	                	limitStartTime:window.limitStartTime,
	                	limitEndTime:window.limitEndTime,
	                	limitPeriod:window.limitPeriod,
	                	skuLimitNum:window.skuLimitNum
	                };
	            }
			}
		}
        
		// 删除 form 表单模型 暂时屏蔽
		/**
		var modelDiv = e._$get('productModelList');
        var modelList = e._$getByClassName(modelDiv,'form-group');
            
        for (var i = 0; i < modelList.length; i++) {
        	var parameterId = e._$dataset(modelList[i],'parameterid');
        	delete data[parameterId];
        }*/
                
        // 自定义html
        var customEditHTML = this.__editor._$getContent();
        var detail = new Object();
        detail.customEditHTML = customEditHTML;
        detail.prodParamJson = '';
        data['prodDetail'] = detail;
        
    };

	// 添加商品设置为不同的状态
	pro.__onSave = function(_event,flag){
		var data = this.webForm._$data();
        var pass = this.webForm._$checkValidity();
        var reg = /^[1-9]\d*$/;
        
        //已下架商品
		if(window.prodStatus == 5){
	       //限购
	        var limit = e._$getByClassName('limitList','prodLimit');
	        if(limit[1].checked){
	        	data['isSKULimited'] = 1;
	        	if(!data.limitComment){
	        		notify.show('请填写限购说明');
	        		return;
	        	}
	        	if(!data.limitEndTime){
	        		notify.show('请选择限购时间');
	        		return;
	        	}
	        	if(!reg.test(data.limitPeriod)){
	        		notify.show('请填写正确的限购周期（大于0的整数）');
	        		return;
	        	}
	        	if(!reg.test(data.skuLimitNum)){
	        		notify.show('请填写正确的限购数量（大于0的整数）');
	        		return;
	        	}
	        }else{
	        	data['isSKULimited'] = 0;
	        	delete data['limitStartTime'];
	        	delete data['limitEndTime'];
	        	delete data['limitPeriod'];
	        	delete data['skuLimitNum'];
	        	delete data['limitComment'];
	        }
		}

        if(pass){
        	this.__getData(data);
        	
            Request('/item/product/update',{
                data:data,
                method:'POST',
                type:'JSON',
                onload:this.__updateSuccessed._$bind(this,_event,flag),
                onerror:this.__updateFaild._$bind(this)
            })
        }
	};

    pro.__updateSuccessed = function(flag,_event){
        if(flag == 1){   //点击提交保存信息并跳转到列表页面
        	notify.show('更新商品成功');
        	location.href='/item/product/list?type='+window.listType;
        }else if(flag == 2){ //点击上架保存信息并上架商品
        	pro.__onShelve(_event);
        }else if(flag == 3){ //点击下架保存信息并下架商品
        	pro.__unShelve(_event);
        }
    };

    pro.__updateFaild = function(_error){
        notify.show(_error.message);
        console.log('更新失败'+_error);
    };

    $$EditModule._$allocate();
    //return $$EditModule;
})