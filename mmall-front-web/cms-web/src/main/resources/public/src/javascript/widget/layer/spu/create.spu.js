/*
 * --------------------------------------------
 * xx窗体控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ '{lib}base/util.js'
		 ,'{lib}base/event.js'
		 ,'{lib}base/element.js'
		 ,'{lib}util/event.js'
		 ,'{pro}widget/layer/window.js'
		 , 'text!./create.spu.css'
	     , 'text!./create.spu.html?v=1.0.0.1'
	     , '{lib}util/template/tpl.js'
	     ,'{lib}util/file/select.js'
	     , '{pro}widget/ui/warning/warning.js'
	     , 'util/form/form'
	     , 'pro/extend/request'
	     , 'pro/components/notify/notify'
	     , 'pro/extend/config'
	     , 'util/ajax/xdr'
	     ,'{pro}page/item/normal.list/list.js?v=1.0.0.1'
	     ,'{lib}util/chain/NodeList.js'
		 ,'{pro}page/item/synchronize/synchronize.window.js?v=1.0.0.0'
		 ],
		function(u, v, e, t, Window,css,html, e1,s,Warning,ut,request,notify,c,j,categoryList,$,SynWindow,p, o, f, r) 
		{
			var pro,
			_seed_css = e._$pushCSSText(css),
	        _seed_html= e1._$addNodeTemplate(html);


			/**
			 * 所有分类卡片
			 *
			 * @class   {nm.l._$$ChangePOWin}
			 * @extends {nm.l._$$CardWrapper}
			 *
			 *
			 */
			p._$$ChangePOWin = NEJ.C();
			pro = p._$$ChangePOWin._$extend(Window);
			/**
			 * 控件重置
			 * @protected
			 * @method {__reset}
			 * @param  {Object} options 可选配置参数
			 *                           type 1		修改
			 *                           type 0		无时间输入
			 */
			pro.__reset = function(options) 
			{
				if(options.type == 1)
				{
					options.title = "修改单品";
					this.form.type.value = "1";
					this.form.spuId.value = options.spuId;
					this.form.spuName.value = options.spuName;
					this.form.spuBarCode.value = options.spuBarCode;
					this.__okBtn.style.display = "inline-block";
    				this.__ccBtn.style.display = "none";
    				this.__syncBtn.style.display = "inline-block";
				}
				else
				{
					options.title = '创建单品';
					this.__okBtn.style.display = "inline-block";
    				this.__ccBtn.style.display = "inline-block";
    				this.__syncBtn.style.display = "none";
				}
				
				this.__super(options);
				
				var brandSelect = document.getElementById("brandId");
				var brandOptions = "";
				options.brandList.sort(pro.by("brandNameEn"));
				for(var i = 0; i < options.brandList.length; i++)
				{
					if(options.brandList[i].brandId == options.brandId)
					{
						brandOptions += "<option name='" + options.brandList[i].brandId 
						  + "' id='" + options.brandList[i].brandId
						  + "' value='" + options.brandList[i].brandId + "' selected='selected'>" 
						  + options.brandList[i].brandNameZh + "</option>";
					}
					else
					{
						brandOptions += "<option name='" + options.brandList[i].brandId 
												  + "' id='" + options.brandList[i].brandId
												  + "' value='" + options.brandList[i].brandId + "'>" 
												  + options.brandList[i].brandNameZh + "</option>";
					}
				}
				brandSelect.innerHTML = brandOptions;
				
				this.__clist =  new categoryList().$inject('#categoryNormalId');
	        	$("#categoryNormalId .itemCategory")._$attr("value",options.categoryNormalId);
	        	$("#categoryNormalId .itemCategory")._$text(options.categoryNormalName);
				
				this.__form = ut._$$WebForm._$allocate(
				{
							form:this.form
				})
			};
			
			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() 
			{
				// 在正常开发中不太会把样式写在js中，_seed_css写在样式文件中，
				// this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
				// 这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
				// this.__seed_html = 'wgt-ui-xxx';
				this.__seed_css  = _seed_css;
		        this.__seed_html = _seed_html;
			};
			
			/**
			 * 销毁控件
			 */
			pro.__destroy = function() 
			{
				this.__super();
			};
			
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() 
			{
				this.__super();
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.form = list[0];
				this.__okBtn = list[1];
				this.__ccBtn = list[2];
				this.__syncBtn = list[3];
				v._$addEvent(list[1], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[2], 'click', this.__onCCClick._$bind(this));
				v._$addEvent(list[3], 'click', this.__onSync._$bind(this));
			};
			
			pro.__onCCClick = function(event) 
			{
				this._$hide();
			};
			
			pro.__showError = function(_text)
			{
				if(this.warning)
				{
					this.warning = this.warning._$recycle();
				}
				this.warning = Warning._$allocate({text:_text})
			}
			
			pro.__onOKClick = function(event) 
			{
				var pass = this.__form._$checkValidity();
				var formData = this.__form._$data();
				var itemAttr = $('#categoryNormalId .itemCategory')._$attr("value");
				if(formData.spuBarCode && formData.spuName)
				{
					pass = true;
				}
				else
				{
					pass = false;
					notify.show(
					{
						type:'error',
						message:'请输入单品条码和填写单品名称'
					})
				};
				
				if (pass)
				{
					if(formData.type == 0)
					{
						var postData = 
						{
								"spuBarCode" : formData.spuBarCode,
								"spuName" : formData.spuName,
								"categoryNormalId" : (itemAttr ? itemAttr : -1),
								"brandId" : formData.brandId
						};
						request('/item/spu/create',{data:postData,method:'POST',onload:this.__onRequestLoad._$bind(this),onerror:this.__onErrorLoad._$bind(this)})
					}
					else
					{
						var postData = 
						{
								"spuId" : formData.spuId,
								"spuBarCode" : formData.spuBarCode,
								"spuName" : formData.spuName,
								"categoryNormalId" : (itemAttr ? itemAttr : -1),
								"brandId" : formData.brandId
						};
							
						request('/item/spu/update',{data:postData,method:'POST',onload:this.__onRequestLoad._$bind(this),onerror:this.__onErrorLoad._$bind(this)})
					}
					
					
				}
			}
			pro.__onSync = function(){
				var spuId = this.form.spuId.value;
				if(this.__syncBtn.className.indexOf("btn-disabled") > -1){
	            	return;
	            }
	        	this.__SynWin = SynWindow._$allocate({
		           	"spuId" : spuId,
	           })._$show();
	        	$(".m-window")[1].style.zIndex = 1001;
	        	$(".m-mask")[1].style.zIndex = 1000;
			};
			pro.__onRequestLoad = function(_result)
			{
				if(_result.code==200)
				{
					this._$dispatchEvent('onok',_result.result);
					this._$hide();
				} 
				else if(_result.code)
				{
					notify.showError(_result.message);
				}
			}
			
			pro.__onErrorLoad = function(_result){
				if(_result.code){
					notify.showError(_result.message);
					this._$hide();
				}
			};
	        pro.by = function(name){
	    		return function(o,p){
	    			var a,b;
	    			if(typeof o == "object" &&  typeof p =="object" && o && p){
	    				a = o[name];
	    				b = p[name];
	    				if(a == b){
	    					return 0;
	    				}
	    				if(typeof a == typeof b){
	    					 return a < b ? -1 : 1;
	    				}
	    				return typeof a < typeof b ? -1 : 1;
	    			}else{
	    				throw("error");
	    			}
	    		}
	    	}
			return p._$$ChangePOWin;
		})