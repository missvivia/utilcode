/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js'
       ,'{lib}base/event.js'
       ,'{lib}base/element.js'
       ,'{pro}widget/module.js'
       ,'{lib}util/list/module.pager.js'
       ,'{pro}widget/cache/image.cache.list.js'
       ,'{pro}widget/item/image.item/image.item.js?v=1.0.0.1'
       ,'{pro}widget/layer/move.images/move.images.js'
       ,'{pro}widget/ui/warning/warning.js'
       ,'{pro}widget/layer/sure.window/sure.window.js'
       , '{lib}ui/datepick/datepick.js'
       ,'util/form/form'
       ],
    function(u,v,e,Module,t1,ImageCacheList,ImageItem,MoveImagesWin,Warning,SureWin,ut,t0,p,o,f,r) {
        var pro;

        p._$$ManageModule = NEJ.C();
        pro = p._$$ManageModule._$extend(Module);
        
        pro.__init = function(_options) {
        	_options.tpl = 'jst-template';
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
    		this.__form = t0._$$WebForm._$allocate({
	            form:this.form
	        });
    		this.cache = ImageCacheList._$allocate({'moveimages':this.__cbMoveImages._$bind(this),
    											removeimages:this.__cbreMoveImages._$bind(this)});
    		this.__onCategoryChange();
        };
        pro.__onCategoryChange = function(event){
        	if(this.mdl){
        		this.mdl = this.mdl._$recycle();
        	}
        	this.cache._$clearListInCache('user-list');
        	var data = this.__form._$data();
        	u._$merge(data);
        	//请添加相关的cache脚本文件和item文件,
			//添加{lib}util/list/module.pager.js
			//添加{pro}widget/item/item.js

			this.mdl = t1._$$ListModulePG._$allocate({
				limit : 18,
				parent :this.box, //列表容器节点
				item : {
					klass : ImageItem,
					prefix : 'g-item'
				}, // klass 可以是item对象，也可以是jst，如果是jst传入模板id就可，如果是item对象需要实现item对象
				cache : {
					key : 'id', // 此key必须是唯一的，对应了item中的值,也是删除选项的data-id
					lkey : 'user-list', // 此key必须是唯一的，对应了item中的值,也是删除选项的data-id
					data : data, //  列表加载时携带数据信息，此数据也可在cache层补全 {pro}widget/cache/cust.cache.list.js
					klass : ImageCacheList
				},
				ondelete : function(data) {
					alert('删除成功');
					this.mdl._$delete(data);
				}._$bind(this),
				onupdate : function(data) {
					alert('更新成功');
					this.mdl._$update(data);
				}._$bind(this),
				pager : {
					parent : this.pager
				}
			});
			this.allBtn.checked = false;
        };
        pro.__getNodes = function(){
            this.categorySelect = e._$get('category');
            this.act = e._$get('act');
            this.imagebox = e._$get('imagebox');
            this.form = e._$get('searchFrm');
            var list = e._$getByClassName(this.act,'j-flag');
            //搜索图片名称
            //上传开始时间
            //上始结束时间
            //搜索
            //all
            //移动
            //删除
//            this.name = list[0];
//            this.start= list[1];
//            this.end=list[2];
//            this.searchBtn = list[3];
            this.allBtn = list[0];
            this.moveBtn = list[1];
            this.delBtn = list[2];
            
            list = e._$getByClassName(this.imagebox,'j-flag');
            this.box = list[0];
            this.pager = list[1];
        };
        
        pro.__addEvent = function(){
           v._$addEvent(this.categorySelect,'change',this.__onCategoryChange._$bind(this));
           v._$addEvent(this.form.name,'keyup',this.__onKeyUp._$bind(this));
           v._$addEvent(this.form.searchbtn,'click',this.__onSearchBtnClick._$bind(this));
           v._$addEvent(this.form.startTime,'click',this.__onDatePickClick._$bind(this,this.form.startTime));
           v._$addEvent(this.form.startTime1,'click',this.__onDatePickClick._$bind(this,this.form.startTime));
           v._$addEvent(this.form.endTime,'click',this.__onDatePickClick._$bind(this,this.form.endTime));
           v._$addEvent(this.form.endTime1,'click',this.__onDatePickClick._$bind(this,this.form.endTime));
           v._$addEvent(this.allBtn,'click',this.__onAllBtnClick._$bind(this));
           v._$addEvent(this.moveBtn,'click',this.__onMoveBtnClick._$bind(this));
           v._$addEvent(this.delBtn,'click',this.__onDelBtnClick._$bind(this));
        };
        
        pro.__onKeyUp = function(_event){
            if(_event.keyCode == 13) {
                this.form.searchbtn.click();
            }
        };
        
        pro.__onDatePickClick = function(elm,event){
        	v._$stop(event);
        	if(this.datePick){
        		this.datePick._$recycle();
        	}
        	this.datePick = ut._$$DatePick._$allocate({parent:elm.parentNode,
				clazz:'m-datepick',
				onchange:this.__onDateChange._$bind(this,elm)})
        };
        pro.__onDateChange = function(elm,date){
        	elm.value = u._$format(date,'yyyy-MM-dd');
        };
        pro.__onSearchBtnClick = function(event){
        	this.__onCategoryChange();
        };
        pro.__onAllBtnClick = function(event){
        	var items = this.mdl._$items();
        	for(var i=0,l=items.length;i<l;i++){
        		items[i]._$checked(this.allBtn.checked);
        	}
        };
        pro.__getCheckedImages = function(){
        	var items = this.mdl._$items();
        	var checkList = [];
        	for(var i=0,l=items.length;i<l;i++){
        		if(items[i]._$isChecked()){
        			checkList.push(items[i]._$getData().imgId);
        		}
        	}
        	return checkList;
        }
        pro.__onMoveBtnClick = function(event){
        	var items = this.__getCheckedImages();
        	if(!items.length){
        		if(this.warn){
        			this.warn._$recycle();
        		}
        		this.warn = Warning._$allocate({text:'请选择图片'});
        	} else{
        		this.categories=[];
            	for(var i=0,l=this.categorySelect.options.length;i<l;i++){
            		if(this.categorySelect.selectedIndex!=i){
            			this.categories.push({name:this.categorySelect.options[i].text,id:this.categorySelect.options[i].value})
            		}
            	}
            	
        		MoveImagesWin._$allocate({parent:document.body,categories:this.categories,onok:this.__onOKCategory._$bind(this,items)})._$show();
        	}
        };
        
        pro.__onOKCategory = function(ids,category){
        	this.cache._$moveImages(ids,parseInt(category));
        }
        pro.__cbMoveImages = function(){
        	this.__onCategoryChange();
        };
        pro.__onDelBtnClick = function(event){
        	var items = this.__getCheckedImages();
        	if(!items.length){
        		if(this.warn){
        			this.warn._$recycle();
        		}
        		this.warn = Waring._$allocate({text:'请选择图片'});
        	} else{
        		SureWin._$allocate({parent:document.body,text:'确定删除选中的图片',onok:this.__onSureOK._$bind(this,items)})._$show();
        	}
        };
        pro.__onSureOK = function(ids){
        	this.cache._$removeImages(ids);
        }
        pro.__cbreMoveImages = function(){
        	this.__onCategoryChange();
        };
        p._$$ManageModule._$allocate();
    });