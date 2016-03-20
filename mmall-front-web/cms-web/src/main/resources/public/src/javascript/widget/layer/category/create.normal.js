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
		 , 'text!./create.normal.css'
	     , 'text!./create.normal.html'
	     , '{lib}util/template/tpl.js'
	     ,'{lib}util/file/select.js'
	     , '{pro}widget/ui/warning/warning.js'
	     , 'util/form/form'
	     , 'pro/extend/request'
	     , 'pro/components/notify/notify'
	     , 'pro/extend/config'
	     , 'util/ajax/xdr'
		 ],
		function(u, v, e, t, Window,css,html, e1,s,Warning,ut,request,notify,c,j,p, o, f, r) 
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
					options.title = "修改分类名称";
					this.form.type.value = "1";
					this.form.categoryId.value = options.categoryId;
					this.form.categoryName.value = options.categoryName;
					this.form.parentId.value = options.parentId;
					this.form.parentName.value = options.parentName;
				}
				else
				{
					if(options.parentId != null)
					{
						options.title = '创建子分类';
						this.form.parentId.value = options.parentId;
						this.form.parentName.value = options.parentName;
					}
					else
					{
						options.title = '创建分类';
					}
				}
				
				
				
				this.__super(options);

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
				v._$addEvent(list[1], 'click', this.__onOKClick._$bind(this));
				v._$addEvent(list[2], 'click', this.__onCCClick._$bind(this));
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
				if(formData.categoryName)
				{
					pass = true;
				}
				else
				{
					pass = false;
					notify.show(
					{
						type:'error',
						message:'请填写分类的名称'
					})
				};
				
				if (pass)
				{
					if(formData.type == 0)
					{
						var postData = 
						{
								"categoryName" : formData.categoryName,
								"parentId" : formData.parentId
						};
						request('/category/normal/create',{data:postData,method:'POST',onload:this.__onRequestLoad._$bind(this),onerror:f})
					}
					else
					{
						var postData = 
						{
								"categoryName" : formData.categoryName,
								"categoryId" : formData.categoryId
						};
						
						if(formData.parentId != "")
						{
							postData.parentId = formData.parentId;
						}
							
						request('/category/normal/update',{data:postData,method:'POST',onload:this.__onRequestLoad._$bind(this),onerror:f})
					}
					
					
				}
			}
			
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
			return p._$$ChangePOWin;
		})