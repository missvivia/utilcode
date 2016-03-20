/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js?v=1.0.0.0',
    '{lib}base/event.js?v=1.0.0.0',
    '{lib}base/element.js?v=1.0.0.0',
    '{pro}/extend/util.js?v=1.0.0.0',
    '{pro}widget/module.js?v=1.0.0.0',
    '{pro}components/item/list.js?v=1.0.0.5',
    '{pro}extend/request.js?v=1.0.0.0',
    '{pro}widget/form1.js?v=1.0.0.2',
    '{lib}util/file/select.js?v=1.0.0.0',
    '{pro}components/notify/notify.js?v=1.0.0.0',
    '{pro}page/product/modal.batch.js?v=1.0.0.0',
    '{pro}page/product/modal.batch/modal.batch.result.js?v=1.0.0.0',
    '{lib}util/chain/NodeList.js'
    ],
    function(ut,v,e,_,Module,SizeList,Request,ut0,s,notify, BatchModal,BatchResultModal,$,p) {
        var pro, url = {
            "batch": "/rest/product/changeProductName"
        };

        p._$$SizeModule = NEJ.C();
        pro = p._$$SizeModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
            this.__initBatch();
            this.__initNav();
            
            this.__form = ut0._$$WebForm._$allocate({form:e._$get('search-form'),
			            	onsubmit:function(data){
			            		if(!this.__sizeList){
				            		this.__doCategoryChange(data);
					                this.__sizeList = new SizeList({
					                    data: {condition:data,type:window.listType}
					                }).$inject("#m-productlist");
					            } else{
					            	this.__doCategoryChange(data);
					            	this.__sizeList.refresh(data)
					            }
			            	}._$bind(this)
			            })
            
            //this.__initExport();
        };
        pro.__initNav = function(){
        	var index = parseInt(location.search.replace("?type=",""),10);
        	var nav = $(".m-nav li")[index-1];
        	$(nav)._$attr("class","current"); 
        };
        pro.__getNodes = function(){
//            var _list = e._$getByClassName(document,'j-flag')
//            this.allinput = _list[0];
//            this.allinPicture = _list[1];
//            this.allinSize = _list[2];
//            this.allinHtml = _list[3];
//            this.editName = _list[4];
            //this.card = e._$get('actionCard');
        };

        pro.__addEvent = function(){
        	var input = e._$get('search-text');
        	v._$addEvent(input,'keyup',this.__onKeyUp._$bind(this));
            v._$addEvent('cardbtn','click',this.__showOrHideCard._$bind(this,'cardbtn'));
            v._$addEvent('cardbtn1','click',this.__showOrHideCard._$bind(this,'cardbtn1'));
            v._$addEvent(document,'click',this.__hideCard._$bind(this));
			v._$addEvent('search-select','change',this.__searchSelectChange._$bind(this));
			v._$dispatchEvent('search-select','change');
			
            //Regular.dom.on(nes.one(".j-batch-name"), 'click', this.__doBatchEdit.bind(this));
            //Regular.dom.on(nes.one(".j-export"), 'click', this.__doExportProducts.bind(this));
            
            //v._$addEvent('exportall','click',this.__doExportAllProducts._$bind(this));
        };
        
        pro.__onKeyUp = function(_event){
        	if(_event.keyCode == 13) {
        		var btn = e._$get("submitBtn");
        		btn.click();
            }
        };
        
        pro.__searchSelectChange = function(_event){
        	var _elm = v._$getElement(_event);
            var input = e._$get('search-text');
            var index = _elm.value;
            
            if (index == 0) {
                input.name = 'productName';
            } else if (index == 1) {
                input.name = 'goodsNo';
            } else if (index == 2) {
                input.name = 'barCode';
            }
        }
        
        pro.__doCategoryChange = function(_data){
       		var _lowCategoryId = parseInt(_data.category3)||parseInt(_data.category2)||parseInt(_data.category1);
    		var _level = 1;
    		if(_lowCategoryId == _data.category1){
    			_level =1;
    		}
    		else if(_lowCategoryId == _data.category2){
    			_level =2;
    		}
    		else if(_lowCategoryId == _data.category3){
    			_level =3;
    		}
           	
           	_data.lowCategoryId = _lowCategoryId;
           	_data.firstCategoryId = _data.category1;
           	_data.secondCategoryId = _data.category2;
           	_data.level = _level;
           	_data.etime = parseInt(_data.etime) + (24*3600*1000 - 1);
           	delete _data.category1;
           	delete _data.category2;
           	delete _data.category3;
       		
       		
       		//商品状态 : 1未审核，2审核中，3审核未通过，4已上架，5已下架
       		//type 二级分类列表 1全部 2库存不足 3已上架 4未上架 5待审核 6审核未通过
       		//1 库存不足 ，2库存足
       		
       		var type = window.listType;
       		_data.stockStatus = 0;
       		_data.status = 0;
       		
       		if(type ==1){
       			_data.status = 0;
       		}
       		else if(type==2){
       			_data.status = 0;
       			_data.stockStatus = 1;
       		}
       		else if(type == 3){
       			_data.status = 4;
       		} 
       		else if(type == 4){
       			_data.status = 5;
       		}
       		else if(type == 5){
       			_data.status = 2;
       		}
       		else if(type==6){
       			_data.status = 3;
       		}
       		
       		if(_data.status == 0){
       			delete _data.status;
       		}
       		if(_data.stockStatus == 0){
       			delete _data.stockStatus;
       		}
       		if(!_data.stime){
       			delete _data.stime;
       		}
       		if(!_data.etime){
       			delete _data.etime;
       		}
       		if(_data.lowCategoryId == null){
       			delete _data.lowCategoryId;
       		}
       		if(!_data.productName){
       			delete _data.productName;
       		}
       		if(!_data.goodsNo){
       			delete _data.goodsNo;
       		}
       		
       		_data.orderColumn = 'UpdateTime';
        };
        
        // 批量导入
        pro.__initBatch = function(){
            // 有个label是弹框
            var card = e._$get('cardbtn');
        	var _labelList = nes.all('label[data-url]',card);
        	for(var i=0,l=_labelList.length;i<l;i++){
        		s._$bind(_labelList[i], {
        			parent:document.body,
	                name: 'myfile',
	                multiple: false,
	                onchange: this.__onFileUpload._$bind(this,_labelList[i])
	            });
        	}
        }
        pro.__onFileUpload = function(_label,_event){
        	var form = _event.form;
			form.action =  e._$dataset(_label,'url');
			j._$upload(form,{
				type:'json',
				onload:function(_json){
					if(_json.code==200){
						form.reset();
						var modal = new BatchResultModal({data: {title: "批量导入结果",message:_json.result.join('\r\n')}});
						modal.$on('confirm',function(){
							location.reload();
						})
					} else{
						form.reset();
						//new BatchResultModal({data: {title: "批量导入结果",type:0,result:result.msg.join('\r\n')}});
						notify.show("导入格式错误，请确认导入格式");
					}
				}._$bind(this),
				onerror:function(){
					notify.show('请求错误!');
				}
			})
        };
        
        
        // 批量操作
        pro.__doBatchEdit = function(){
            var list = this.__sizeList
            function loadCallback(json){
                if(json.code == 200){
                    list.$emit("updatelist")
                }else{
                    notify.notify({
                        type: "error",
                        message: "批量修改失败"
                    })
                }
            }
            var pid = list.data.list.filter(function(item){
                return item.checked == true;
            }).map(function(item){
                return item.id
            });
            if(!pid.length) return notify.notify({
                type: "error",
                message: "请先勾选要批量修改的商品"
            })
            new BatchModal({data: {title: "批量修改"}}).$on("batch", function(data){
                Request(url.batch,{
                    method:"POST",
                    data: {
                        pid: pid,
                        header: data.header||"",
                        tailer: data.tailer||"",
                        replace: data.replace||"",
                        replacement: data.replacement|| ""
                    },
                    onload: loadCallback,
                    onerror: loadCallback
                })
            })

        }
        
        pro.__doExportProducts = function(_event){
        	var _elm = v._$getElement(_event);
        	
        	var idList = this.__sizeList.$getCheckIds();
        	if(idList.length!=0){
        		_elm.href = '/product/export?pid=['+idList.join(',') +']';
        	} else{
        		v._$stop(_event);
        		notify.notify({
                    type: "error",
                    message: "请选择要导出商品"
                })
        	}
        };
        pro.__doExportAllProducts = function(_event){
        	var _elm = v._$getElement(_event);
        	var _data = this.__getFormData();
        	_elm.href = '/product/exportAll?'+ ut._$object2query(_data);
        };
        
        pro.__showOrHideCard = function(_id,_event){
        	var _elm = v._$getElement(_event);
        	if(!(_elm.tagName=='A'||_elm.tagName=='LABEL')){
        		v._$stop(_event);
        	}
        	
        	if(e._$hasClassName(_id,'open')){
        		e._$delClassName(_id,'open');
        	}
        	else {
        		e._$addClassName(_id,'open');
        	}
        	
        	if(e._$hasClassName(_elm,'j-export')){
        		var _data = this.__getFormData();
        		_elm.href = '/product/export?'+ ut._$object2query(_data);
        	}
        };
        
        pro.__hideCard = function(){
        	e._$delClassName('cardbtn','open');
        	e._$delClassName('cardbtn1','open');
        };
        
        pro.__getFormData = function(){
        	var _data = this.__form._$data();
    		var _lowCategoryId = parseInt(_data.category3)||parseInt(_data.category2)||parseInt(_data.category1);
    		var _level = 1;
    		if(_lowCategoryId == _data.category1){
    			_level =1;
    		}
    		else if(_lowCategoryId == _data.category2){
    			_level =2;
    		}
    		else if(_lowCategoryId == _data.category3){
    			_level =3;
    		}
           	
           	_data.lowCategoryId = _lowCategoryId;
           	_data.level = _level;
           	
           	delete _data.category1;
           	delete _data.category2;
           	delete _data.category3;
           	
           	return _data;
        };
        pro.__initExport = function(){
        	v._$addEvent('export','click',function(event){
        		var _elm = v._$getElement(event);
        		var _data = this.__getFormData();
	           	_elm.href = '/product/export?'+ ut._$object2query(_data);
        	}._$bind(this));
        };
        p._$$SizeModule._$allocate();
    });