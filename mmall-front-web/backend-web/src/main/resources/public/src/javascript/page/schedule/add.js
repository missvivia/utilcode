/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/po/addtable1.js',
    '{pro}components/po/addtable2.js',
    'util/tab/tab',
    'pro/widget/form',
    '{lib}util/file/select.js',
    'pro/extend/request',
    'pro/components/notify/notify',
    'util/ajax/xdr',
    '{pro}page/product/modal.batch/modal.batch.result.js'
    ],
    function(ut,v,e,Module,addtable1,addtable2,_ut0,_ut1,s,request,notify,j,BatchResultModal,p) {
        var pro;

        p._$$SizeModule = NEJ.C();
        pro = p._$$SizeModule._$extend(Module);
        
        pro.__init = function(_options) {
            _options.tpl = 'jstTemplate';
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
			this.__form  = _ut1._$$WebForm._$allocate({
							form:e._$get('search-form'),
							onsubmit:this.__onSearch._$bind(this)
						})
			
        };
        
        pro.__getNodes = function(){
            var _list = e._$getByClassName(document,'j-flag');
            this.allinput = e._$get('batchimport');
            this.outproduct = _list[0];
            this.__tab = _list[1];
            this.__addNum = _list[2];
            this.__productNode = _list[3];
            this.__addedNode = _list[4];
            this.__statusSelect = e._$get('status');
        };
        pro.__addEvent = function(){
        	if( this.allinput){
	        	s._$bind(this.allinput, {
	    			parent:this.allinput.parentNode,
	                name: 'myfile',
	                multiple: false,
	                param:{scheduleId:scheduleId},
	                onchange: this.__onFileUpload._$bind(this)
	            });
        	}
            v._$addEvent('submit','click',this.__onSubmitClick._$bind(this));
        }
        pro.__onSubmitClick = function(_event){
        	var _elm = v._$getElement(_event);
        	var _id = e._$dataset(_elm,'id');
        	request('/schedule/productlist/submit',{
                method:'POST',
                data:{id:_id},
                onload:function(_json){
                  if(_json.code==400){
                    notify.show('请添加售卖商品后提交审核');
                    
                  } else{
                    notify.show('提交审核成功');
                    if(this.__list1){
                    	this.__list1.$updateStatus(2);
                    }
                    if(this.__list2){
                    	this.__list2.$updateStatus(2);
                    }
                    _elm.disabled = true;
                  }
                }._$bind(this),
                onerror:function(_error){
                    notify.showError('提交审核失败');
                }
            });
        }
        pro.__onFileUpload = function(_event){
        	var form = _event.form;
					form.action =  '/rest/schedule/batchUploadProductInfo';
					j._$upload(form,{
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
        }
        pro.__onSearch = function(_data){
        	if(!this.__taber){
        		this.__taber = _ut0._$$Tab._$allocate({
				   				 list:e._$getChildren(this.__tab),
				   				 selected:'addclick',
				   				 onchange:this.__onTabChange._$bind(this,_data)
				   			});
                // this.__taber._$go(0,true);
        	} else{
        		this.__taber._$go(_index,true);
        	}
        };
        pro.__doCategoryChange = function(_data){
       		var _lowCategoryId = parseInt(_data.lowCategoryId)||parseInt(_data.category2)||parseInt(_data.category1);
       		delete _data.category2;
       		delete _data.category1;
       		_data.lowCategoryId = _lowCategoryId;
        };
        
        pro.__onTabChange = function(_data,_event){
        	_index = _event.index;

        	if(_index==0){
        		if(this.__form){
        			_data = this.__form._$data();
        		}
        		this.__doCategoryChange(_data);
                // if(this.__list1.data.lastId == undefine){
                //     _data['lastId'] = 0;
                // }else{
                //     _data['lastId'] = this.__list1.data.lastId;
                // }
                _data['poId'] = parseInt(e._$get('poId').innerText);
                _data['lastId'] = 0;
                _data['page'] = _index;
        		if(!this.__list1){
                    
            		this.__list1 = new addtable1({
            			data:{condition:_data,poId:_data['poId'],status:status,page:_index}
            		});
            		this.__list1.$inject('#productlist');
            	} else{
            		this.__list1.refresh(ut._$merge(_data,{page:_index}));
            	}
        		this.__list1.$watch('skuTotal',function(_total){
        			this.__addNum.innerText =( _total!=-1?'('+_total+')':'');
        		}._$bind(this))
        		e._$delClassName(this.__productNode,'f-dn');
        		e._$addClassName(this.__addedNode,'f-dn');
        		this.__statusSelect.selectedIndex = 0;
        		e._$addClassName(this.__statusSelect.parentNode.parentNode.parentNode,'f-dn');
        	} else if(_index==1){
                 
        		e._$addClassName(this.__productNode,'f-dn');
        		e._$delClassName(this.__addedNode,'f-dn');
        		e._$delClassName(this.__statusSelect.parentNode.parentNode.parentNode,'f-dn');
                if(this.__form){
                    _data = this.__form._$data();
                }

        		this.__doCategoryChange(_data);
        		// _data.status =1;
                 _data['poId'] = parseInt(e._$get('poId').innerText);
                // this.__list2.data.list[this._list2.data.list.length-1]
                _data['lastId'] = 0;
                _data['page'] = _index;
        		if(!this.__list2){
                    this.__list2 = new addtable2({
                        data:{condition:_data,poId:_data['poId'],status:status,page:_index}
                    });
                    this.__list2.$inject('#addedproductlist');
               
                } else{
                    this.__list2.refresh(ut._$merge(_data,{page:_index}))

                }
        		this.__list2.$watch('skuTotal',function(_total){
        			this.__addNum.innerText =( _total!=-1?'('+_total+')':'');
        		}._$bind(this))
                
        	}
        };
       
        
        p._$$SizeModule._$allocate();
    });