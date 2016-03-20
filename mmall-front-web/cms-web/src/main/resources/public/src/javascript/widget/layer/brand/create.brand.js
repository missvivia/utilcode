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
		 , 'text!./create.brand.css'
	     , 'text!./create.brand.html'
	     , '{lib}util/template/tpl.js'
	     ,'{lib}util/file/select.js'
	     , '{pro}widget/ui/warning/warning.js'
	     , 'util/form/form'
	     , 'pro/extend/request'
	     , 'pro/components/notify/notify'
	     , 'pro/extend/config'
	     , 'util/ajax/xdr'
		 ],
		function(u, v, e, t, Window,css,html, e1,s,Warning,ut,request,notify,c,j,p, o, f, r) {
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
			pro.__reset = function(options) {
				options.title = '创建品牌';
				this.__super(options);
				if(options.type==1){
					this.form.brandNameEn.value = options.data.brandNameEn;
					this.form.brandNameZh.value = options.data.brandNameZh;
					this.form.brandId.value = options.data.brandId;
					
					this.form.logo.value = options.data.logo;
					this.form.brandVisualImgWeb.value = options.data.brandVisualImgWeb;
					this.form.brandVisualImgApp.value = options.data.brandVisualImgApp;
//					for(var i=0,l=this.imgNodes.length;i<l;i++){
//						this.imgNodes[i].src = this.logos[i];
//					}
					this.img['logo'].src = options.data.logo;
					this.img['brandVisualImgWeb'].src = options.data.brandVisualImgWeb;
					this.img['brandVisualImgApp'].src = options.data.brandVisualImgApp;
					//this.form.brandProbability.selectedIndex= 0;
				} else{
					this.logos = [];
					this.logos.length = 1;
				}
				this.__form = ut._$$WebForm._$allocate({
							form:this.form
						})
			};
			
			/**
			 * 初使化UI
			 */
			pro.__initXGui = function() {
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
			pro.__destroy = function() {
				this.__super();
			};
			/**
			 * 初使化节点 
			 */
			pro.__initNode = function() {
				this.__super();
				var list = e._$getByClassName(this.__body, 'j-flag');
				this.form = list[0];
				this.logosNode = list[1];
				//this.uploadErrorNode = list[2];
				this.__initLogoUpload();
				v._$addEvent(list[2], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[3], 'click', this.__onCCClick._$bind(this));
			};
			pro.__initLogoUpload = function(){
				var _labelList = e._$getByClassName(this.logosNode,'j-img');
				this.img ={};
				for(var i=0,l=_labelList.length;i<l;i++){
					var _name = e._$dataset(_labelList[i],'name');
					this.img[_name] = _labelList[i].parentNode.getElementsByTagName('img')[0];
					s._$bind(_labelList[i], {
		                name: 'img',
		                multiple: false,
		                accept:'image/*',
		                onchange: this.__onLogoUpload1._$bind(this,_labelList[i])
		            });
				}
			} 
			
			pro.__onLogoUpload1 = function(_label,_event){
				var form = _event.form;
				form.action =  c.UPLOAD_URL;
				j._$upload(form,{
					onload:function(result){
						var _name = e._$dataset(_label,'name');
						this.form[_name].value = result.result[0].imgUrl;
						var img = _label.parentNode.getElementsByTagName('img')[0];
						img.src = result.result[0].imgUrl;
						var errorNode = e._$getByClassName(_label.parentNode,'js-error');
		    			if(errorNode.length){
		    				e._$remove(errorNode[0].parentNode);
		    			}
					}._$bind(this),
					onerror:function(){
						notify.show('上传图片可能超过2M');
					}
				})
			}
			pro.__onLogoUpload = function(imageNode,index,event){
				var form = event.form;
				form.action =  c.UPLOAD_URL;
				j._$upload(form,{
					onload:function(result){
						this.logos[index] = result.result[0].imgUrl;
						this.imgNodes[index].src = result.result[0].imgUrl;
						e._$removeByEC(this.uploadError);
					}._$bind(this),
					onerror:function(){
						
					}
				})
			};
			pro.__onCCClick = function(event) {
				this._$hide();
			};
			pro.__showError = function(_text){
				if(this.warning){
					this.warning = this.warning._$recycle();
				}
				this.warning = Warning._$allocate({text:_text})
			}
			pro.__validImage = function(){
				var list = e._$getByClassName(this.logosNode,'j-img');
				var valid = true;
				for(var i=0,l=list.length;i<l;i++){
	        		var ipt = list[i].parentNode.getElementsByTagName('input')[0];
	        		if(ipt.value==''){
	        			var errorNode = e._$getByClassName(ipt.parentNode,'js-error');
	        			if(!errorNode.length){
	        				var div = e._$create('div');
	            			div.innerHTML ='<span class="js-error">'+ e._$dataset(ipt,'message')+'</span>';
	            			list[i].parentNode.appendChild(div);		
	        			}
	        			e._$addClassName(ipt.parentNode,'js-invalid');
	        			valid = false;
	        		} else{
	        			e._$delClassName(ipt.parentNode,'js-invalid');
	        		}
	        	}
				return valid;
			}
			pro.__onOKClick = function(event) {
				var pass = this.__form._$checkValidity();
				var formData = this.__form._$data();
				if(formData.brandNameEn || formData.brandNameZh){
					pass = true;
				}else{
					pass = false;
					notify.show({
						type:'error',
						message:'中文名和英文名必须填写一项'
					})
				};
				var pass1 = this.__validImage();
				if (pass&&pass1){
					request('/item/brand/update',{data:formData,method:'PUT',onload:this.__onRequestLoad._$bind(this),onerror:this.__onErrorLoad._$bind(this)})
					
				}
				
			}
			
			pro.__onRequestLoad = function(_result){
				if(_result.code==200){
					this._$dispatchEvent('onok',_result.result);
					this._$hide();
				} else if(_result.code){
					notify.showError(_result.message);
					
				}
			}
			
			pro.__onErrorLoad = function(_result){
				if(_result.code){
					notify.showError(_result.message);
					this._$hide();
				}
			}
			return p._$$ChangePOWin;
		})