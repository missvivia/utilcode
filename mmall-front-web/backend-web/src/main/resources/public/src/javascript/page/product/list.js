/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/product/list.js',
    '{pro}extend/request.js',
    '{pro}widget/form.js',
    '{lib}util/file/select.js',
    'util/ajax/xdr',
    '{pro}components/notify/notify.js',
    '{pro}page/product/modal.batch.js',
    '{pro}page/product/modal.batch/modal.batch.result.js'
    ],
    function(ut,v,e,Module,SizeList,Request,ut0,s,j,notify, BatchModal,BatchResultModal,p) {
        var pro, url = {
            "batch": "/rest/product/changeProductName"
        };

        p._$$SizeModule = NEJ.C();
        pro = p._$$SizeModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
            
            this.__form = ut0._$$WebForm._$allocate({form:e._$get('search-form'),
			            	onsubmit:function(data){
				            	if(!this.__sizeList){
				            		this.__doCategoryChange(data);
					                this.__sizeList = new SizeList({
					                    data: {condition:data}
					                }).$inject("#m-productlist");
					            } else{
					            	this.__doCategoryChange(data);
					            	this.__sizeList.refresh(data)
					            }
			            	}._$bind(this)
			            })
            this.__initBatch();
            //this.__initExport();
        };
        pro.__doCategoryChange = function(_data){
       		var _lowCategoryId = parseInt(_data.lowCategoryId)||parseInt(_data.category2)||parseInt(_data.category1);
       		delete _data.category2;
       		delete _data.category1;
       		_data.lowCategoryId = _lowCategoryId;
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
        
        pro.__getNodes = function(){
//            var _list = e._$getByClassName(document,'j-flag')
//            this.allinput = _list[0];
//            this.allinPicture = _list[1];
//            this.allinSize = _list[2];
//            this.allinHtml = _list[3];
//            this.editName = _list[4];
            this.card = e._$get('actionCard');
            
        };

        pro.__addEvent = function(){
            v._$addEvent('cardbtn','click',this.__showOrHideCard._$bind(this,'actionCard'));
            v._$addEvent('cardbtn1','click',this.__showOrHideCard._$bind(this,'actionCard1'));
            v._$addEvent(document,'click',this.__hideCard._$bind(this));

            Regular.dom.on(nes.one(".j-batch-name"), 'click', this.__doBatchEdit.bind(this));
            Regular.dom.on(nes.one(".j-export"), 'click', this.__doExportProducts.bind(this));
            
            v._$addEvent('exportall','click',this.__doExportAllProducts._$bind(this));
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
        	if(e._$hasClassName(_elm,'j-export')){
        		var _data = this.__getFormData();
        		_elm.href = '/product/export?'+ ut._$object2query(_data);
        	}
        	e._$delClassName(_id,'f-dn');
        };
        pro.__hideCard = function(){
        	e._$addClassName('actionCard','f-dn');
        	e._$addClassName('actionCard1','f-dn');
        };
        pro.__getFormData = function(){
        	var _data = this.__form._$data();
    		var _lowCategoryId = parseInt(_data.lowCategoryId)||parseInt(_data.category2)||parseInt(_data.category1);
           	delete _data.category2;
           	delete _data.category1;
           	_data.lowCategoryId = _lowCategoryId;
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