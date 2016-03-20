/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{pro}widget/module.js'
        ,'{pro}widget/ui/calendar/calendar.js'
        ,'pro/widget/form'
        ,'{lib}util/form/form.js'
        ,'{pro}extend/request.js'
        , '{lib}ui/datepick/datepick.js'
        , '{pro}components/notify/notify.js'
        ,'pro/extend/util'],
    function(ut,v,e,Module,Calendar,ut1,form2,request,ut2,notify,_,p,o,f,r) {
        var pro;
        
        p._$$CreateModule = NEJ.C();
        pro = p._$$CreateModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.__schedule=window["g_schedule"]||null;
			this.__getNodes();
			this.__addEvent();
//			this.form.reset();
            //this.__initCalender();

//            this.__onProvinceChange();
        };
        
        /**
         * 只能输入数字keydown事件函数
         */
        pro.__keydown = function(_float,_event){
    		if ((_event.keyCode >= 48&&_event.keyCode <= 57)||(_event.keyCode >= 96&&_event.keyCode <= 105)||_event.keyCode == 8||_event.keyCode == 9) {
    			return true;
    		}else{
    			if(_float&&_event.keyCode ==110){ //。
    				return true;
    			} else{
    				v._$stop(_event);
    				return false;
    			}
    		}
    	};
    	
    	
        pro.__getNodes = function(){
          var node = e._$get('pocreate');
          var list = e._$getByClassName(node,'j-flag');
          this.province = list[0];
          this.form = e._$get('dataform');
          this.webform = form2._$$WebForm._$allocate({
        	  form:this.form
          });
        };
       
        pro.__addEvent = function(){
        	v._$addEvent('supplierAcct-btn','click',this.__onSupplierAcct._$bind(this));
        	v._$addEvent('poFollowerUserName-btn','click',this.__onPoFollowerUserName._$bind(this));
        	v._$addEvent('submit','click',this.__onSubmitData._$bind(this,true));
          v._$addEvent('save','click',this.__onSubmitData._$bind(this,false));
//           v._$addEvent(this.form.startTime,'click',this.__onDatePickClick._$bind(this));
          v._$addEvent("supplyMode-1",'click',this.__onSupplyMode._$bind(this,1));
          v._$addEvent("supplyMode-2",'click',this.__onSupplyMode._$bind(this,2));
          v._$addEvent("endTime",'click',this.__onEndTimeClick._$bind(this));
          
          var _inputs=e._$getByClassName(this.form,"mark-num");
          for(var i=0;i<_inputs.length;i++){
          	 var float = e._$dataset(_inputs[i],'float');
          	 v._$addEvent(_inputs[i], "keydown", this.__keydown._$bind(this,float));
          	 if(float){
          		v._$addEvent(_inputs[i], "blur", (function(elm){
          				return function(){
          					elm.value=parseFloat(elm.value)||'';
      					}})(_inputs[i]));
          	 }
          }
           //编辑初始化数据
           if(this.__schedule){
        	   this.__onSupplierAcct();
//        	   this.__onSupplyMode(this.__schedule.supplyMode);
        	   var _elm=e._$get("supplyMode-"+this.__schedule.supplyMode);
        	   if (_elm.click) {
	   				_elm.click();
	   			} else {
	   				var _event = document.createEvent("MouseEvents");
	   				_event.initEvent("click", true, true);
	   				_elm.dispatchEvent(_event);
	   			}
        	   var _supplyMode=null;
        	   if(this.__schedule.supplierStoreId){
        		   _supplyMode=e._$get("supplierStoreId");
        		   for(var i=0;i<_supplyMode.options.length;i++)
        			   if(this.__schedule.supplierStoreId==_supplyMode.options[i].value){
        				   _supplyMode.options[i].selected="selected";
        				   break;
        			   }
        	   }
        	   if(this.__schedule.brandSupplierId){
        		   _supplyMode=e._$get("brandSupplierId");
        		   for(var i=0;i<_supplyMode.options.length;i++)
        			   if(this.__schedule.brandSupplierId==_supplyMode.options[i].value){
        				   _supplyMode.options[i].selected="selected";
        				   break;
        			   }
        	   }
           }
        };
        pro.__onEndTimeClick = function(_event){
        	if(this.__datepick){
        		this.__datepick._$recycle();
        	}
        	v._$stop(_event);
        	this.__datepick = ut2._$$DatePick._$allocate({
        						parent:'endTime',
        						date:this.form.endTime.value,
        						clazz:'dropdown-menu m-datepicker m-datepicker-tr',
				        		range:[+(_.setDate(this.form.startTime.value))-3600*1000*24],
				        		onchange:function(_date){
				        			this.form.endTime.value = ut._$format(_date,'yyyy-MM-dd');
				        		}._$bind(this)
				        	})
        };
        /**
         * 供货方式切换
         */
        pro.__onSupplyMode = function(_type,_event){
//        	var _target=v._$getElement(_event);
        	if(this.supplierinfo||this.__schedule){
        		if(_type==1){
        			if((this.supplierinfo&&this.supplierinfo.supplierType==1)||(this.__schedule&&this.__schedule.supplierType==1)){
        				//代理商
        				e._$get("supplierStoreId").disabled="";
        				e._$get("brandStoreId").disabled=true;
        			}else{
        				e._$get("supplierStoreId").disabled=true;
        				e._$get("brandStoreId").disabled="";
        			}
	        	}else{
	        		e._$get("supplierStoreId").disabled="";
					e._$get("brandStoreId").disabled="";
	        	}
        	}else{
    			v._$stop(_event);
    			alert("请先输入商家帐号并获取相关信息");
    		}
        };
        
        /**
         * 检查帐号是否有相应站点"PO商品清单审核/PO商品资料审核/PO装修审核/PO BANNER 审核"四项权限；
         */
        pro.__onPoFollowerUserName = function(){
        	if(!this.form.supplierAcct.value){
        		notify.showError('验证需先填商家帐号');
        		return false;
        	}
        	if(this.form.poFollowerUserName.value){
        		request('/schedule/checksupplieracctvalid',{
            		data:{supplierAcct:this.form.poFollowerUserName.value,poSupplierAcct:this.form.supplierAcct.value},
            		method:'GET',
            		onload:function(_data){
            			console.log(_data);
            			if(_data&&_data.code==200){
            				if(_data.result)
            					notify.show("It's Ok");
            				else
            					notify.showError('该帐号无相关审核权限，建议更换跟进人');
            			}else{
            				notify.showError('请求失败');
            			}
            			
            		}._$bind(this),onerror:f});
        	}
        };
        /**根据商家帐号获取商家信息**/
        pro.__onSupplierAcct = function(_event){
        	if(this.form.supplierAcct.value){
        		request('/schedule/getsupplierinfo',{
            		data:{supplierAcct:this.form.supplierAcct.value},
            		method:'GET',
            		onload:function(_data){
            			console.log(_data);
            			if(_data&&_data.code==200){
            				this.supplierinfo=_data.msg;
            				e._$get("brandNname").innerHTML=this.supplierinfo.brandName;
            				if(this.supplierinfo.supplierType==1&&this.supplierinfo.brandAgentExist){
            					//代理商
            					e._$get("supplyMode-2").disabled="";
            				}else{
            					e._$get("supplyMode-2").disabled="true";
            				}
            				//非事件触发
            				if(_event){
            					e._$get("supplyMode-1").checked="";
								e._$get("supplyMode-2").checked="";
                				e._$get("supplierStoreId").disabled="disabled";
                				e._$get("brandStoreId").disabled="disabled";
            				}
							if(this.__schedule)
								this.__schedule.supplierType=this.supplierinfo.supplierType;
            				this.__initCheckBoxs(this.supplierinfo.saleSiteList,e._$get("saleSiteIds"));
            			}else{
            				notify.showError('信息获取失败');
            			}
            			
            		}._$bind(this),onerror:f});
        	}
        };
        
        pro.__initCheckBoxs = function(list,_container){
        	_container.innerHTML="",_checkbox="";
        	for(var i=0,l=list.length;i<l;i++){
        		var _checked='';
        		if(this.__schedule){ //编辑时选中销售站点
	        		var _index = ut._$indexOf(this.__schedule.saleSiteList,function(_item){
	        			return _item.id==list[i].id;
	        		});
	        		if(_index!=-1){
	        			_checked=' checked="checked" ';
	        		}
        		}
        		_checkbox+='<label><input type="checkbox" name="saleSiteIds" value="'+list[i].id+'" '+_checked+'/>  '+list[i].name+'  </label>&nbsp;&nbsp;';
        	}
        	_container.innerHTML=_checkbox;
        };
//        pro.__initSelect = function(list,select){
//        	select.options.length = 0;
//        	for(var i=0,l=list.length;i<l;i++){
//        		var option = new Option(list[i].name,list[i].id);
//        		select.options.add(option);
//        		if(this.__schedule){ //编辑时选中销售站点
//	        		var _index = ut._$indexOf(this.__schedule.saleSiteList,function(_item){
//	        			return _item.id==list[i].id;
//	        		});
//	        		if(_index!=-1){
//	        			option.selected ='selected';
//	        		}
//        		}
//        	}
//        };
        pro.__onSubmitData = function(_isSubmit){
        	
	        if(this.webform._$checkValidity()){
	            var _data = this.webform._$data();
	            if(!_data.saleSiteIds){
	            	notify.showError('销售站点不能为空');
	            	return false;
	            }
	            if(_data.startTime<window["g_currTime"]+86400000*4){
	            	notify.showError('档期开始时间-当天 必须大于4天');
	            	return false;
	            }
	            if(!_data.supplyMode){
	            	notify.showError('请选择一种供货方式');
	            	return false;
	            }
	            if(_data.platformSrvFeeRate>100){
	            	notify.showError('服务费率必须小于100%');
	            	return false;
	            }
	            if(!(_data.adPosition instanceof Array))
	            	_data.adPosition=_data.adPosition.split("###");
	            if(!(_data.saleSiteIds instanceof Array))
	            	_data.saleSiteIds=_data.saleSiteIds.split("###");
	            console.log(_data);
	            if(this.__schedule){
	            	_data.id=this.__schedule.id;
	            }
	            var _temp_elm=e._$get("supplierStoreId");
	            if(_temp_elm.disabled){
	            	_data.supplierStoreId="";
	            }
	            _temp_elm=e._$get("brandStoreId");
	            if(_temp_elm.disabled){
	            	_data.brandStoreId="";
	            }
	            if(_data.startTime>_data.endTime){
	            	notify.showError('请输入正确的档期时间');
	            	return;
	            }
	            if(_isSubmit){
	            	if(this.supplierinfo.supplierType==2){ //删除品牌商的代理商入库id
	            		delete _data.supplierStoreId;
	            	}
	            	request('/schedule/create/add',{
	                	data:_data,
	                	method:'POST',
	                	onload:function(_json){
	                		if(_json.code==200){
	                			notify.show('添加成功');
	                			location.href='/schedule/schedulelist';
	                		} else{
	                			notify.showError(_json.msg);
	                		}
	                	},
	                	onerror:this.__onError._$bind(this)});
	            } else{
	            	if(this.supplierinfo.supplierType==2){ //删除品牌商的代理商入库id
	            		delete _data.supplierStoreId;
	            	}
	            	request('/schedule/create/save',{
	                	data:_data,
	                	method:'POST',
	                	onload:function(_json){
	                		if(_json.code==200){
	                			notify.show('保存成功');
	                			location.href='/schedule/schedulelist';
	                		} else{
	                			notify.showError(_json.msg);
	                		}
	                	},
	                	onerror:function(){
	                		notify.showError('保存失败');
	                	}});
	            }
	        }
        };
        pro.__onError = function(){
        	notify.showError('添加失败');
        };
        p._$$CreateModule._$allocate();
    });