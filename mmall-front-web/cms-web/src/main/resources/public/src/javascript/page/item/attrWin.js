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
		 ,'{lib}util/template/jst.js'
		 ,'{lib}util/template/tpl.js'
		 ,'{pro}widget/layer/window.js'
		 ,'{pro}extend/request.js'
		 ,'{lib}util/form/form.js'
		 ,'{pro}components/notify/notify.js'
		 ,'{lib}util/chain/NodeList.js'
		 ,'text!./model/attrwin.html'
		 ,'text!./model/attrwin.css'],
		function(u, v, _e, t, e2,_p, Window, Request, _t, notify, $,html,css,p ) {
			var pro;
			
			var _seed_css = _e._$pushCSSText(css);
			
			p._$$AddAttrGroupWin = NEJ.C();
			pro = p._$$AddAttrGroupWin._$extend(Window);
			/**
			 * 控件重置
			 * @protected
			 * @method {__reset}
			 * @param  {Object} options 可选配置参数
			 *                           clazz
			 *                           draggable
			 *                           mask
			 */
			pro.__reset = function(options) {
				var formData = options.item;
				if(!formData){
					options.title = '添加扩展属性';
					formData = {};
				}else{
					options.title = '修改扩展属性';
					this.index=options.indexs;
				}
				this.modelId=$("#modelId")._$val()||"";
				var _seed_html=e2._$add(html);
				e2._$render(this.__body,_seed_html,{item:formData,modelId:this.modelId});
				this.__initForm();
				this.__super(options);
				this.__addEvent();
				
			};
			pro.__addEvent = function(){
				var $addattr=$('#addAttrOption'),that=this;
				
				if(this.index!="undefined" && u._$isNumber(this.index)){
					this.__paramlist=__attrlist[this.index].optionList || [];
				}
				
				if(this.__paramlist.length<0){
					$('.addattr-box')._$siblings("p")._$style("display","block");
				 }else{
					 $('.addattr-box')._$siblings("p")._$style("display","none");
				 }
				
				$('#confrimattr')._$on("click",function(e){
					that.__onOKClick();
				})
				$('#attrsubmit')._$on("click",function(e){
					that.__onsubClick();
				})
				var attr_box=$(".addattr-box"),_span=attr_box._$children("span");
				u._$forIn(_span,function(_item,_index,_this){
					_this[0].indexs=_index;
					that.__bindClose(_this._$children(".close"));
				});
				
				$addattr._$on("click",function(e){
					var attrv=$addattr._$prev("input")._$val();
					that.__addOptions($addattr,attrv);
				})
				
				var $subAttrOption=$("#subAttrOption")
				$subAttrOption._$on("click",function(e){
					var attrv=$(this)._$prev("input")._$val();
					var _span=$("<span class='attrwin-error'>添加的属性选项不能为空</span>");
					if(u._$length(attrv)<1){
						$(this)._$insert(_span,'after')
						return false;
					}
					var data={
						"itemModelId":$(this)._$attr("data-model"),
						"modelParameterId":$(this)._$attr("data-parame"),
						"paramOption":attrv
					};
					that.__subAttrOption(data);
				});
			};
			pro.__initForm = function(){
				this.__attritem=null;
				this.__paramlist=[];
				this.__Form = _t._$$WebForm._$allocate({
					form:'attrForm'
				});
			};
			pro.__onsubClick = function(){
				var that=this;
				if(that.__Form._$checkValidity()){
					
					if(that.__paramlist.length<1){
						$('.addattr-box')._$siblings("p")._$style({"display":"block","color":"red"});
						return false;
					}
					var data = that.__packData();
					
					if(typeof(that.index)=="undefined" || typeof(that.index)!="number"){
						data['itemModelId']=that.modelId;
						data['optionList']=that.__paramlist;
						Request('model/paramAdd',{
							data:data,
							method:'POST',
							type:'JSON',
							onload:function(dt){
								if(dt.code=="200"){
//									that.__attritem=data;
//									$("#attrbox")._$prev("thead")._$style("visibility","visible");
//									__attrlist.push(that.__attritem);
//									that.__createTr(that.__attritem);
//									that._$hide();
//									that.__clearDate();
									notify.show({
										'type':'success',
										'message':dt.message
									});
									window.location.reload();
								}
							},
							onerror:function(data){
								notify.show({
									'type':'error',
									'message':data.message
								});
							}
						})
						
					}else{
						Request('model/updateParam',{
							data:data,
							method:'POST',
							type:'JSON',
							onload:function(dt){
								if(dt.code=="200"){
									notify.show({
										'type':'success',
										'message':dt.message
									});
									that.__attritem=data;
									that.__attritem['optionList']=that.__paramlist;
									
									that.__mdTr(that.__attritem);
									that._$hide();
									that.__clearDate();
								}else{
									notify.show({
										'type':'error',
										'message':dt.message
									});
								}
							},
							onerror:function(dt){
								notify.show({
									'type':'error',
									'message':dt.message
								});
							}
						})
					}
				}
			}
			pro.__onOKClick = function(){
				if(this.__Form._$checkValidity()){
					
					if(this.__paramlist.length<1){
						$('.addattr-box')._$siblings("p")._$style({"display":"block","color":"red"});
						return false;
					}
					
					var data = this.__packData();
					
					
					if(typeof(this.index)=="undefined" || typeof(this.index)!="number"){
						this.__attritem=data;
						this.__attritem['optionList']=this.__paramlist;
						
						$("#attrbox")._$prev("thead")._$style("visibility","visible");
						
						__attrlist.push(this.__attritem);
						this.__createTr(this.__attritem);
						
					}else{
						this.__attritem=data;
						this.__attritem['optionList']=this.__paramlist;
						this.__mdTr(this.__attritem);
						
					}
					
					this._$hide();
					this.__clearDate();
				}
			};
			pro.__createTr=function(data){
				var attrbox=$("#attrbox"),that=this;
				var _node=e2._$get('attr-box',{item:data});
				var _tr=_e._$html2node(_node),$tr=$(_tr);
				attrbox._$insert(_tr,'append');
				
				that.__setIndex(attrbox);
				var _list=$tr._$children("td:last-child");
				var _a=_list._$children(".p-flag");
				
				_a._$on("click",function(_event){
					var _type=$(this)._$attr("data-type");
					if(_type=="del"){
						that.__delTr(this.parentNode.parentNode.indexs);
					}
					if(_type=="subdel"){
						that.__subdelTr(this.parentNode.parentNode.indexs);
					}
					if(_type=="md"){
						that.__mdWin(this.parentNode.parentNode.indexs);
					}
				});
			}
			pro.__subAttrOption=function(data){
				var that=this;
				Request('model/paramOptionAdd',{
					data:data,
					method:'POST',
					type:'JSON',
					onload:function(data){
						if(data.code=="200"){
							notify.show({
								'type':'success',
								'message':data.message
							});
							that._$hide();
							window.location.reload();
						}
					},
					onerror:function(data){
						notify.show({
							'type':'error',
							'message':data.message
						});
					}
				})

			}
			pro.__setIndex=function(){
				var attrbox=$("#attrbox"),len=attrbox._$children("tr").length;
				 if(len<1){
					 attrbox._$prev("thead")._$style("visibility","hidden");
					 return false;
				 }
				 else{
					 attrbox._$prev("thead")._$style("visibility","visible");
					 var $p1=$(".perror1");
					 	 $p1._$style("display","none");
				 }
				 for (var i = 0; i < len; i++) {
					 attrbox._$children("tr")[i].indexs = i;
				 }
			}
			pro.__mdWin=function(index){
				var config={
								item:__attrlist[index],
								indexs:index
				           };
				this.__reset(config);
				this._$show();
			}
			pro.__subdelTr=function(index){
				var isgo=confirm("删除是不可恢复的，你确定删除吗？");
				var attrbox=$("#attrbox");
				var $tr=attrbox._$children("tr")[index];
				var that=this;
				if(isgo){
					var data={
						"itemModelId":__attrlist[index].itemModelId,
						"parameterId":__attrlist[index].parameterId
					};
					Request('model/delParam',{
						data:data,
						method:'GET',
						type:'JSON',
						onload:function(dt){
							if(dt.code=="200"){
								notify.show({
									'type':'success',
									'message':dt.message
								});
								_e._$remove($tr,false);
								__attrlist.splice(index, 1);
								that.__setIndex();
							}
						},
						onerror:function(dt){
							notify.show({
								'type':'error',
								'message':dt.message
							});
						}
					})
				}
			}
			pro.__delTr=function(index){
				var attrbox=$("#attrbox");
				var $tr=attrbox._$children("tr")[index];
				_e._$remove($tr,false);
				__attrlist.splice(index, 1);
				this.__setIndex();
			}
			pro.__mdTr2=function(index,data){
				var attrbox=$("#attrbox");
				var $tr=attrbox._$children("tr")[index];
				_e._$remove($tr,false);
				__attrlist[index]=data;
				this.__setIndex();
			}
			pro.__mdTr=function(data){
				var attrbox=$("#attrbox"),that=this;
				var _node=e2._$get('attr-box',{item:data});
				var _tr=_e._$html2node(_node),$tr=$(_tr);
				
				$(attrbox._$children("tr")[that.index])._$insert(_tr,'after');
				that.__mdTr2(that.index,data);
				
				var _list=$tr._$children("td:last-child");
				var _a=_list._$children(".p-flag");
				_a._$on("click",function(_event){
					var _type=$(this)._$attr("data-type");
					if(_type=="del"){
						that.__delTr(this.parentNode.parentNode.indexs);
					}
					if(_type=="subdel"){
						that.__subdelTr(this.parentNode.parentNode.indexs);
					}
					if(_type=="md"){
						that.__mdWin(this.parentNode.parentNode.indexs);
					}
				});

				
			}
			pro.__packData = function(){
				var data = this.__Form._$data();
				return data;
			};
			
			pro.__addOptions=function(obj,value){
				var value=value.encodeTag();
				if(value.trim().length<1) return false;
				
				obj._$prev("input")._$val("");
				
				var $attrbox=$('.addattr-box'),$sclose=$('<span class="close">×</span>'),$span=$('<span><label>'+value+'</label></span>'),that=this;
				$attrbox._$siblings("p")._$style("display","none");
				
				var param={
					paramOption:value
				};
				this.__paramlist.push(param);
				$span._$insert($sclose,'append');
				$attrbox._$insert($span,'append');
				
				this.__setSpan();
				this.__bindClose($sclose);
			}
			pro.__setSpan=function(){
				var attr_box=$(".addattr-box"),len=attr_box._$children("span").length;
				 if(len<1){
					 attr_box._$siblings("p")._$style("display","block");
					 return false;
				 }
				 
				 for (var i = 0; i < len; i++) {
					 attr_box._$children("span")[i].indexs = i;
				 }
			}
			pro.__bindClose=function(obj){
				var that=this;
				obj._$off("click");
				obj._$on("click",function(e){
					var index=this.parentNode.indexs,modal=this;
					var id=obj._$attr("data-option");
					if(id!=null){
						var paramid=obj._$attr("data-parame");
						var data={
							"parameterId":paramid,
							"paramOptionId":id,
						}
						Request('model/delParamOption',{
							data:data,
							method:'GET',
							type:'JSON',
							onload:function(dt){
								if(dt.code=="200"){
									notify.show({
										'type':'success',
										'message':dt.message
									});
									that.__paramlist.splice(index, 1);
									 _e._$remove(modal.parentNode,false);
									that.__setSpan();
								}else{
									notify.show({
										'type':'error',
										'message':dt.message
									});
								}
							},
							onerror:function(dt){
								notify.show({
									'type':'error',
									'message':dt.message
								});
							}
						})
					}else{
						that.__paramlist.splice(index, 1);
						 _e._$remove(this.parentNode,false);
						that.__setSpan();
					}
					
				});
			}
			pro.__clearDate=function(){
				this.__attritem=null;
				this.__paramlist=[];
				this.index="";
			}
			pro.__initXGui = function() {
				this.__seed_css  = _seed_css;
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
			};
			
			return p._$$AddAttrGroupWin;
		})