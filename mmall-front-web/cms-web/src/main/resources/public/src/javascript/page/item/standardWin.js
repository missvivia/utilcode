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
		 ,'text!./model/standardwin.html'
		 ,'text!./model/attrwin.css'],
		function(u, v, _e, t, e2,_p, Window, Request, _t, notify, $,html,css,p ) {
			var pro;
			var trim=function(){
				return this.replace(/(^\s*)|(\s*$)/g, ""); 
			}
			
			_seed_css = _e._$pushCSSText(css)
			
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
					options.title = '添加规格';
					formData = {};
				}else{
					options.title = '修改规格';
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
				var $addStand=$('#addStand'),that=this;
				
				if(this.index!="undefined" && u._$isNumber(this.index)){
					this.__paramlist=__standardlist[this.index].speciOptionList||[];
					this.count=this.__paramlist.length;
				}
				
				if(this.__paramlist.length<0){
					$('.addStand-box')._$siblings("p")._$style("display","block");
				 }else{
					 $('.addStand-box')._$siblings("p")._$style("display","none");
				 }
				
				$('#confrim')._$on("click",function(e){
					that.__onOKClick();
				})
				$('#standsubmit')._$on("click",function(e){
					that.__onsubClick();
				})
				var attr_box=$(".addStand-box"),_span=attr_box._$children("span");
				u._$forIn(_span,function(_item,_index,_this){
					_this[0].indexs=_index;
					that.__bindClose(_this._$children(".close"));
				});
				$addStand._$on("click",function(e){
					var standv=$addStand._$prev("input")._$val();
					that.__addStandOption($addStand,standv);
				})
				
				var $subStandOption=$("#subStandOption")
				$subStandOption._$on("click",function(e){
					var attrv=$(this)._$prev("input")._$val();
					var _span=$("<span class='attrwin-error'>添加的规格选项不能为空</span>");
					if(u._$length(attrv)<1){
						$(this)._$insert(_span,'after')
						return false;
					}
					var data={
						"itemModelId":$(this)._$attr("data-model"),
						"specificationId":$(this)._$attr("data-parame"),
						"speciOptionType":"1",
						"speciOption":attrv
					};
					that.__subStandOption(data);
				});
			};
			pro.__initForm = function(){
				this.__standitem=null;
				this.__paramlist=[];
				this.count=0;
				this.__Form = _t._$$WebForm._$allocate({
					form:'standForm'
				});
			};
			pro.__onsubClick=function(){
				var that=this;
				
				if(that.__Form._$checkValidity()){
					
					if(that.__paramlist.length<1){
						$('.addStand-box')._$siblings("p")._$style({"display":"block","color":"red"});
						return false;
					}
					
					var data = that.__packData();
					
					if(typeof(that.index)=="undefined" || typeof(that.index)!="number"){
						
						data['itemModelId']=that.modelId;
						data['speciOptionList']=that.__paramlist;
						Request('model/speciAdd',{
							data:data,
							method:'POST',
							type:'JSON',
							onload:function(dt){
								if(dt.code=="200"){
//									that.__standitem=data;
//									__standardlist.push(that.__standitem);
//									that.__createTr(that.__standitem);
//									that._$hide();
//									that.__clearDate();
									notify.show({
										'type':'success',
										'message':dt.message
									});
									window.location.reload();
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
						Request('model/updateSpeci',{
							data:data,
							method:'POST',
							type:'JSON',
							onload:function(dt){
								if(dt.code=="200"){
									notify.show({
										'type':'success',
										'message':dt.message
									});
									that.__standitem=data;
									that.__standitem['speciOptionList']=that.__paramlist;
									that.__mdTr(that.__standitem);
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
						$('.addStand-box')._$siblings("p")._$style({"display":"block","color":"red"});
						return false;
					}
					var data = this.__packData();
					
					if(typeof(this.index)=="undefined" || typeof(this.index)!="number"){
						this.__standitem=data;
						this.__standitem['speciOptionList']=this.__paramlist;
						
						$("#standbox")._$prev("thead")._$style("visibility","visible");
						
						__standardlist.push(this.__standitem);
						this.__createTr(this.__standitem);
						
						
					}else{
						this.__standitem=data;
						this.__standitem['speciOptionList']=this.__paramlist;
						this.__mdTr(this.__standitem);
						
					}
					
					this._$hide();
					this.__clearDate();
				}
			};
			pro.__createTr=function(data){
				var standbox=$("#standbox"),that=this;
				var _node=e2._$get('stand-box',{item:data});
				var _tr=_e._$html2node(_node),$tr=$(_tr);
				standbox._$insert(_tr,'append');
				
				that.__setIndex(standbox);
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
			pro.__subdelTr=function(index){
				var isgo=confirm("删除是不可恢复的，你确定删除吗？");
				var standbox=$("#standbox");
				var $tr=standbox._$children("tr")[index];
				var that=this;
				if(isgo){
					var data={
						"itemModelId":__standardlist[index].itemModelId,
						"specificationId":__standardlist[index].specificationId
					};
					Request('model/delSpeci',{
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
			pro.__setSpan=function(){
				var stand_box=$(".addStand-box"),len=stand_box._$children("span").length;
				 if(len<1){
					 stand_box._$siblings("p")._$style("display","block");
					 return false;
				 }
				 for (var i = 0; i < len; i++) {
					 stand_box._$children("span")[i].indexs = i;
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
							"specificationId":paramid,
							"speciOptionId":id,
						}
						Request('model/delSpeciOption',{
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
			pro.__setIndex=function(){
				var standbox=$("#standbox"),len=standbox._$children("tr").length;
				 if(len<1){
					 standbox._$prev("thead")._$style("visibility","hidden");
					 return false;
				 }else{
					 standbox._$prev("thead")._$style("visibility","visible");
					 var $p1=$(".perror2");
					 $p1._$style("display","none");
				 }
				 
				 for (var i = 0; i < len; i++) {
					 standbox._$children("tr")[i].indexs = i;
				 }
			}
			pro.__mdWin=function(index){
				var config={
								item:__standardlist[index],
								indexs:index
				           };
				this.__reset(config);
				this._$show();
			}
			pro.__delTr=function(index){
				var standbox=$("#standbox");
				var $tr=standbox._$children("tr")[index];
				_e._$remove($tr,false);
				__standardlist.splice(index, 1);
				this.count--;
				this.__setIndex();
			}
			pro.__mdTr2=function(index,data){
				var standbox=$("#standbox");
				var $tr=standbox._$children("tr")[index];
				_e._$remove($tr,false);
				__standardlist[index]=data;
				this.count--;
				this.__setIndex();
			}
			pro.__mdTr=function(data){
				var standbox=$("#standbox"),that=this;
				var _node=e2._$get('stand-box',{item:data});
				var _tr=_e._$html2node(_node),$tr=$(_tr);
				
				$(standbox._$children("tr")[that.index])._$insert(_tr,'after');
				that.__mdTr2(that.index,data);
				
				that.__setIndex(standbox);
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
			pro.__addStandOption=function(obj,value){
				var value=value.encodeTag();
				if(value.trim().length<1) return false;
				this.count++;
				obj._$prev("input")._$val("");
				
				var $standbox=$('.addStand-box'),$sclose=$('<span class="close">×</span>'),$span=$('<span><label>'+value+'</label></span>'),that=this;
				
				$standbox._$siblings("p")._$style("display","none");
				
				var param={
					speciOption:value,
					speciOptionType:"1",
					index:this.count
				};

				this.__paramlist.push(param);
				$span._$insert($sclose,'append');
				$standbox._$insert($span,'append');
				
				this.__setSpan();
				this.__bindClose($sclose);
				
			}
			pro.__subStandOption=function(data){
				var that=this;
				Request('model/speciOptionAdd',{
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
						}else{
							notify.show({
								'type':'error',
								'message':dt.message
							});
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
			pro.__clearDate=function(){
				this.__standitem=null;
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