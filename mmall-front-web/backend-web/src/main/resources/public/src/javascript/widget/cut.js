/**
 * 品购装修页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{lib}util/event.js'
        ,'{lib}util/file/select.js'
        ,'{lib}util/template/tpl.js'
        ,'{pro}extend/util.js'
        ,'{pro}widget/layer/countdown/countdown.setting.js'],
    function(ut,v,e,t,s,e2,ut0,CountDownSetting,DragMask,p,o,f,r) {
        var pro,width = 1000,topHeight = 73,offsetLeft=(document.body.clientWidth-width)/2;
     // ui css text
    	var _seed_css = e._$pushCSSText('\
    			.m-rulerh{position:absolute;top:0;left:0;width:100%;height:18px;background:url(/res/images/decorate/ruler_h.png) repeat-x;cursor:default;z-index:888}\
    			.m-rulerv{position:absolute;top:0;left:0;bottom:0;height:100%;width:18px;background:url(/res/images/decorate/ruler_v.png) repeat-y;cursor:default;z-index:888;}\
    			.m-rulerv span,.m-rulerh span{position:absolute;}\
    			.m-rulerv span{display:block;width:8px;word-wrap: break-word;margin-left:3px}\
    			.cutcnt{width:1000px;margin:0 auto;height:100%;border:1px solid #4affff;border-width:0 1px;position:relative}\
    			.m-hline{width:100%;border-top:1px solid #4affff;height:0;position:absolute;cursor:url(/res/images/decorate/cur_move_h.cur),move}\
    			.m-vline{width:0;border-left:1px solid #4affff;height:100%;position:absolute;top:0;cursor:url(/res/images/decorate/cur_move_v.cur),move;}\
    			.m-vbox{position:absolute;top:0;left:0;width:100%;}\
    			.m-vbox .cntbox{position:absolute;bottom:0;width:100%;left:0}\
    			.m-hbox{position:absolute;top:0;left:0;height:100%;}\
    			.m-hbox .cnthbox{position:absolute;right:0;top:0;height:100%}\
    			.m-vbox .m-countdown{z-index:999}\
    			.m-hbox .cnthbox .name,.m-vbox .cntbox .name{background:#ccc;color:#666;display:block;position:relative;}\
    			.m-hbox .hotbox{position:absolute;border:dashed 1px red}\
    			.m-hbox .cnthbox .col{background-color:#000;opacity:0.5;position:absolute;text-align:center}\
    			.m-hbox .hotbox .act{position:absolute;bottom:-26px;right:0;width:53px;height:25px;z-index:999;color:red}\
    			.m-hbox .hotbox .act input,.m-hbox .hotbox .act label,.m-hbox .hotbox .act span{float:left}\
    			.m-hbox .hotbox .del{width:26px;display:inline-block;height:25px;background:url(/res/images/cut_opt.png) no-repeat -27px 0;}\
    			.m-hbox .hotbox .ok{width:26px;height:25px;display:inline-block;background:url(/res/images/cut_opt.png) no-repeat 0 0;}\
    			.m-hbox .hotbox .hotcnt{position:absolute;top:0;bottom:0;left:0;right:0;}\
    			.m-hbox .hotboxok .hotcnt{border:1px dashed red;text-align:center;}\
    			.m-hbox .hotboxok .act{width:26px;top:0;bottom:auto;}\
    			.m-hbox .hotboxok .act .ok{display:none}\
    			.m-hbox .hotboxok .act .del{display:none}\
    			.m-hbox .hotboxok:hover .act .del{display:inline-block;}\
    			.m-vbox .hline{position:absolute;bottom:1px;border-top:1px solid #4affff;width:100%;cursor:url(/res/images/decorate/cur_move_h.cur),move;z-index:1000}\
    			.m-hbox .vline{position:absolute;right:0px;border-left:1px solid #4affff;height:100%;cursor:url(/res/images/decorate/cur_move_v.cur),move;z-index:1}\
        ');
    	e._$dumpCSSText()
        var $$Cut = NEJ.C();
        pro = $$Cut._$extend(t._$$EventTarget);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
        };
        pro.__reset = function(_options) {
            this.__supReset(_options);
            this.custNode = _options.node;
            console.log(this.custNode);
            this.rulerh = e._$create('div','m-rulerh');
            this.rulerv = e._$create('div','m-rulerv');
            this.defautlCnt = e._$create('div','cutcnt');
            this.custNode.appendChild(this.rulerh);
            this.custNode.appendChild(this.rulerv);
            this.custNode.appendChild(this.defautlCnt);
            v._$addEvent(this.defautlCnt,'drop',this.__onDragDrop._$bind(this));
            v._$addEvent(this.defautlCnt, 'dragover',function(event){v._$stop(event)});
            v._$addEvent(this.defautlCnt, 'dragleave', function(event){v._$stop(event)});
			this.__initRuler();
			this.__initEvent();
			this.__initData(_options.data);
        };
        pro.__initEvent = function(){
        	v._$addEvent(this.rulerh,'mousedown',this.__onHMouseDown._$bind(this));
        	v._$addEvent(this.custNode,'mousemove',this.__onMouseMove._$bind(this));
        	v._$addEvent(this.custNode,'mouseUp',this.__onMouseUp._$bind(this));
        	v._$addEvent(this.rulerv,'mousedown',this.__onVMouseDown._$bind(this));
        	v._$addEvent(this.custNode,'mousedown',this.__onMousedownBoxChange._$bind(this));
        };
        pro.__initData = function(data){
        	this._$setCustomBackground(data.image.url,data.image.width,data.image.height);
        	this.__initRowCols(data.rows);
        };
        
        pro.__onDragMaskHotBoxOK = function(){
        	//this.dragMask = this.dragMask._$recycle();
        	this.dragMask = null;
        };
        
        pro.__onDragMaskHotBoxDelete = function(){
        	if(this.dragMask){
        		this.dragMask = this.dragMask._$recycle();
        	}
        };
        pro.__initRowCols = function(rows){
        	e._$clearChildren(this.defautlCnt);
        	for(var i=0,l=rows.length;i<l;i++){
        		var vbox = e._$create('div','m-vbox');
        		vbox.style.height = rows[i].y+rows[i].height +'px';
        		vbox.innerHTML = '<div class="hline"></div>';
        		var cntBox = e._$create('div','cntbox');
        		vbox.appendChild(cntBox);
        		for(var j=0,jl=rows[i].cols.length;j<jl;j++){
        			var hbox = e._$create('div','m-hbox');
        			hbox.innerHTML = '<div class="vline"></div>';
        			hbox.style.width = rows[i].cols[j].width + rows[i].cols[j].x + 'px';
        			var cntHBox = e._$create('div','cnthbox');
        			if(rows[i].cols[j].id){
        				DragMask._$allocate({parent:cntHBox
        					,data:rows[i].cols[j].data
        					,clazz:'hotbox'
							,type:0
							});
        				//cntHBox.innerHTML = '<span class="name" data-id='+rows[i].cols[j].id+'>'+(rows[i].cols[j].name||rows[i].cols[j].id)+'</span>'
        			}
        			hbox.appendChild(cntHBox);
        			cntBox.appendChild(hbox);
        		}
        		this.defautlCnt.appendChild(vbox);
        		this.__reorderHBox(vbox);
        	}
        	this.__reorderVBox(this.defautlCnt);
        };
        /**
         * 拖动商器到大容器
         */
        pro.__onDragDrop = function(event){
        	var elm = v._$getElement(event);
        	v._$stop(event);
        	
        	var data = JSON.parse(event.dataTransfer.getData('text/html'));
        	if(e._$hasClassName(elm,'hotcnt')&&this.dragMask){
        		this.dragMask._$setData(0,data);
        	}
        	
        };
        /**
         * 已经拖好的参考线拖动
         */
        pro.__onMousedownBoxChange = function(event){
        	if(event.which !=1){
        		return;
        	}
        	var elm = v._$getElement(event);
        	if(e._$hasClassName(elm,'hline')){ //水平线拖动
        		this.dragVBox = elm.parentNode;
        	} else if(e._$hasClassName(elm,'vline')){ //竖线拖动
        		this.dragHBox = elm.parentNode;
        	} else{
        		v._$stop(event);
            	if(elm.tagName!='DIV'){
            		return;
            	}
            	var vbox = this.__getTargetVBox(event.clientY+document.body.scrollTop-topHeight);
            	var target = this.__getTargetHBox(vbox,event.clientX-offsetLeft);
            	if(!target){
            		return;
            	}
            	if(this.dragMask){
            		this.dragMask = this.dragMask._$recycle();
            	}
            	this.dragMask = DragMask._$allocate({parent:target,event:event,clazz:'hotbox'
            							,onok:this.__onDragMaskHotBoxOK._$bind(this)
            							,type:1
            							,ondelete:this.__onDragMaskHotBoxDelete._$bind(this)});
        	}
        };
        /**
         * 水平参考线拖动
         */
        pro.__onHMouseDown = function(event){
        	this.targetHLine = e._$create('div','m-hline');
        	this.custNode.appendChild(this.targetHLine);
        	v._$stop(event);
        };
        /**
         * 纵向参考线拖动
         */
        pro.__onVMouseDown = function(){
        	this.targetVLine = e._$create('div','m-vline');
        	this.custNode.appendChild(this.targetVLine);
        	v._$stop(event);
        };
        /**
         * 鼠标移动事件响应
         */
        pro.__onMouseMove = function(event){
        	//console.log('event.clientX:'+event.clientX);
        	//console.log('event.offsetY',event.offsetY,'event.clientY',event.clientY);
        	//console.log('event.offsetX',event.offsetX,'event.clientX',event.clientX);
        	if(this.targetHLine){
        		//console.log('event.offsetX:'+event.offsetY);
        		this.targetHLine.style.top =((event.clientY+document.body.scrollTop)-topHeight)+'px';
        	}
        	if(this.targetVLine){
        		//console.log('event.clientX:'+event.clientX);
        		this.targetVLine.style.left = event.clientX+'px';
        	}
        	if(this.dragVBox){
        		var top = (event.clientY+document.body.scrollTop - topHeight);
        		if(top <= 18){
        			e._$remove(this.dragVBox);
        		} else{
        			this.dragVBox.style.height = top+'px';
        		}
        	}
        	if(this.dragHBox){
        		if(event.clientX-offsetLeft<5){
        			e._$remove(this.dragHBox);
        		} else {
        			this.dragHBox.style.width = (event.clientX-this.defautlCnt.offsetLeft)+'px';
        		}
        	}
        	if(this.dragMask){
        		this.dragMask._$moveTarget(event);
        	}
        };
        /**
         * 重新排序box的z-index
         * @param	{Node}	节点
         * @param	{Bolean}	是否是纵向box排序zindex
         */
        pro.__reorderHBox = function(box){
        	var getCntBox = function(box){
        		return e._$getByClassName(box,'cnthbox')[0];
        	}
        	var list = e._$getByClassName(box,'m-hbox');
        	list.sort(function(item1,item2){
        			return item1.clientWidth - item2.clientWidth;
        	});
        	for(var i=0,l=list.length;i<l;i++){
        		list[i].style.zIndex = 200-i;
        		cntbox = getCntBox(list[i]);
        		if(i==0){
        			cntbox.style.width = list[i].clientWidth+'px';
        		} else{
        			cntbox.style.width = (list[i].clientWidth-list[i-1].clientWidth)+'px';
        		}
        	}
        	
        };
        /**
         * 重新计算box里的cntBox的高度
         */
        pro.__reorderVBox = function(box){
        	var list = e._$getByClassName(box,'m-vbox');
        	list.sort(function(item1,item2){
    			return item1.clientHeight - item2.clientHeight;
        	});
        	var getCntBox = function(box){
        		return e._$getByClassName(box,'cntbox')[0];
        	}
        	
        	for(var i=0,l=list.length;i<l;i++){
        		list[i].style.zIndex = 100-i;
        		console.log('list[i].clientHeight',list[i].clientHeight);
        		cntbox = getCntBox(list[i]);
        		if(i==0){
        			cntbox.style.height = list[i].clientHeight+'px';
        		} else{
        			cntbox.style.height = (list[i].clientHeight-list[i-1].clientHeight)+'px';
        		}
        	}
        }
        /**
         * 拖动纵向参考线时计划落在哪个纵向的box
         */
        pro.__getTargetVBox = function(_y){
        	var list = e._$getByClassName(this.defautlCnt,'m-vbox');
        	if(!list.length){
        		return
        	}
        	list.sort(function(item1,item2){
        		return item1.clientHeight - item2.clientHeight;
        	});
        	var box;
        	for(var i=0,l=list.length;i<l;i++){
        		console.log(list[i].clientHeight);
        		if(list[i].clientHeight>_y){
        			box = list[i];
        			break;
        		}
        	}
        	if(box){
        		return e._$getByClassName(box,'cntbox')[0]
        	} else{
        		return;
        	}
        };
        /**
         * 拖动纵向参考线时计划落在哪个纵向的box
         */
        pro.__getTargetHBox = function(box,x){
        	if(x<=0){
        		return;
        	}
        	var list = e._$getByClassName(box,'m-hbox');
        	if(!list.length){
        		return box;
        	}
        	list.sort(function(item1,item2){ //升序
        		return item1.clientWidth - item2.clientWidth;
        	});
        	var hbox;
        	for(var i=0,l=list.length;i<l;i++){
        		console.log(list[i].clientWidth);
        		if(list[i].clientWidth > x){
        			hbox = list[i];
        			break;
        		}
        	}
        	if(hbox){
        		return e._$getByClassName(hbox,'cnthbox')[0]
        	} else{
        		return ;
        	}
        };
        /**
         * 计算纵向的box的高度
         */
        pro.__getCntBoxHeight = function(box){
        	var list = e._$getByClassName(this.defautlCnt,'m-vbox');
        	if(!list.length){
        		return
        	}
        	list.sort(function(item1,item2){
        		return item1.clientHeight - item2.clientHeight;  //升序
        	});
        	var height,index = ut._$indexOf(list,function(item){return box==item});
        	if(index!=-1){
        		if(index!=0){
        			height = list[index].clientHeight - list[index-1].clientHeight;
        		}else{
        			height =  list[0].clientHeight;
        		}
        		return height;
        	}
        }
        pro.__getCntHBoxWidth = function(vbox,box){
        	var list = e._$getByClassName(vbox,'m-hbox');
        	if(!list.length){
        		return
        	}
        	list.sort(function(item1,item2){
        		return item1.clientWidth - item2.clientWidth;  //升序
        	});
        	var width,index = ut._$indexOf(list,function(item){return box==item});
        	
        	if(index!=-1){
        		if(index!=0){
        			width = list[index].clientWidth - list[index-1].clientWidth;
        		}else{
        			width =  list[0].clientWidth;
        		}
        		return width;
        	}
        };
        
        pro._createVBox = function(height){
    		var box = e._$create('div','m-vbox');
    		box.innerHTML = '<div class="hline"></div>';
    		box.style.height = height + 'px';
    		var cntBox = e._$create('div','cntbox');
    		box.appendChild(cntBox);
    		this.defautlCnt.appendChild(box);
    		var height = this.__getCntBoxHeight(box);
    		cntBox.style.height = height+'px';
    		return cntBox;
    	};
    	pro._createHBox = function(vbox,width){
    		var box = e._$create('div','m-hbox');
    		box.innerHTML = '<div class="vline"></div>';
    		box.style.width = width + 'px';
    		var cntBox = e._$create('div','cnthbox');
    		box.appendChild(cntBox);
    		vbox.appendChild(box);
    		var width = this.__getCntHBoxWidth(vbox,box);
    		cntBox.style.width = width +'px';
    		return cntBox;
    	};
        /**
         * 鼠标up响应
         */
        pro.__onMouseUp = function(event){
        	if(this.targetHLine){ //添加水平参考线，添加完后计划每个box里的cntHeight
        		var height = parseInt(this.targetHLine.style.top)||0;
        		console.log('height:'+height);
        		if(height<18){
        			console.log('remove');
        			e._$remove(this.targetHLine);
            		this.targetHLine = null;
        			return;
        		}
        		
        		var vBox = this.__getTargetVBox(event.clientY+document.body.scrollTop-topHeight);
        		var list = e._$getByClassName(vBox,'hotbox');
    			if(list&&list.length!=0){
    				ut0.showError('拖动的参考线已存在热区，不允许添加添加线！');
    				e._$remove(this.targetHLine);
            		this.targetHLine = null;
    				return;
    			}
    			var cntBox = this._createVBox(height);
        		this._createHBox(cntBox,this.defautlCnt.clientWidth);
        		
//        		var box = e._$create('div','m-hbox');
//        		box.style.width = cntBox.clientWidth+'px';
//        		cntBox.appendChild(box); //有水平参考线拖动时生成一个横向的节点
        		
        		
        		this.__reorderVBox(this.defautlCnt);
        		e._$remove(this.targetHLine);
        		this.targetHLine = null;
        	}
        	if(this.targetVLine){//添加纵向参考线
        		var left = parseInt(this.targetVLine.style.left);
        		//offsetLeft
        		var offsetLeft = this.defautlCnt.offsetLeft;
        		if(!left||left-offsetLeft<0||left>=width+offsetLeft){
        			e._$remove(this.targetVLine);
            		this.targetVLine = null;
        			return;
        		}
        		
    			
//        		var box = e._$create('div','m-hbox');
//        		box.innerHTML = '<div class="vline"></div><div class="hcntbox"></div>';
//        		box.style.width = (left-offsetLeft) + 'px';
        		var targetBox = this.__getTargetVBox(event.clientY+document.body.scrollTop-topHeight);
        		if(!targetBox){
        			targetBox = this._createVBox(this.defautlCnt.clientHeight);
        		}

        		var list = e._$getByClassName(targetBox,'hotbox');
    			if(list&&list.length!=0){
    				ut0.showError('拖动的参考线已存在热区，不允许添加添加线！');
    				e._$remove(this.targetVLine);
            		this.targetVLine = null;
    				return;
    			}
        		this._createHBox(targetBox,left-offsetLeft);
        		this.__reorderHBox(targetBox);
        		e._$remove(this.targetVLine);
        		this.targetVLine = null;
        	}
        	if(this.dragVBox){ //移动水平参考线
        		this.__reorderVBox(this.defautlCnt);
        		this.dragVBox = null;
        	}
        	if(this.dragHBox){ //移动纵向考线
        		this.__reorderHBox(this.dragHBox);
        		this.dragHBox = null;
        	}
        	if(this.dragMask){
        		this.dragMask._$mouseUpTarget(event);
        		//this.dragMask = this.dragMask._$recycle();
        	}
        };
        
        /**
         * 标尺初始化
         */
        pro.__initRuler = function(){
        	this.__setVRuler();
        	this.__setHRuler();
        };
        /**
         * 纵向标尺
         */
        pro.__setVRuler = function(){
        	var height = this.custNode.clientHeight;
        	e._$clearChildren(this.rulerv);
        	var count = Math.floor(height/50);
        	for(var i=0;i<count;i++){
        		var span = document.createElement('span');
        		span.style.top = (i*50+2)+'px';
        		span.innerText = i*50;
        		this.rulerv.appendChild(span);
        	}
        };
        /**
         * 水平标尺
         */
        pro.__setHRuler = function(){
        	var width = this.custNode.clientWidth;
        	var count = Math.floor(width/50);
        	for(var i=0;i<count;i++){
        		var span = document.createElement('span');
        		span.style.left = (i*50+2)+'px';
        		span.innerText = i*50;
        		this.rulerh.appendChild(span);
        	}
        };
        /**
         * 设置背景
         */
        pro._$setCustomBackground = function(url,width,height){
        	var css = [];
        	url = url||'/res/images/cut.jpg';
        	css.push('background:#373c41 url('+url+') no-repeat center 0;');
        	css.push('height:'+height +'px;');
        	this.image ={
        			url:url,
        			width:width,
        			height:height
        	}
        	this.custNode.style.cssText = css.join('');
        	e._$clearChildren(this.defautlCnt);
        	var cntBox = this._createVBox(this.custNode.clientHeight);
        	this._createHBox(cntBox,this.defautlCnt.clientWidth);
        	this.__setVRuler();
        };
        
        pro._$getCutData = function(){
        	var rows = e._$getByClassName(this.defautlCnt,'m-vbox');
        	var data = {width:1000,rows:[]};
        	rows.sort(function(item1,item2){
        		return item1.clientHeight - item2.clientHeight;
        	})
        	var getCountDown = function(box){
        		var list = e._$getByClassName(box,'m-countdown');
        		if(list.length){
        			return list[0]
        		}
        	}
        	for(var i=0,l=rows.length;i<l;i++){
        		var cntBox = e._$getByClassName(rows[i],'cntbox')[0];
        		var row ={};
        		row.height = cntBox.clientHeight;
        		row.y = cntBox.offsetTop;
        		row.cols=[];
        		var cols = e._$getByClassName(rows[i],'m-hbox');
        		cols.sort(function(item1,item2){ //升序
        			return item1.clientWidth - item2.clientWidth;
        		})
        		for(var j=0,jl=cols.length;j<jl;j++){
        			var col ={};
        			if(j==0){
        				col.x = 0;
        				col.width =	cols[j].clientWidth
        			} else{
        				col.x = cols[j-1].clientWidth
        				col.width =	cols[j].clientWidth -cols[j-1].clientWidth;
        			}
        			var namelist = e._$getByClassName(cols[j],'name');
        			if(namelist.length){
        				col.id = e._$dataset(namelist[0],'id');
        				col.data ={};
        				col.data.name = namelist[0].innerText;
        				var hotBox = namelist[0].parentNode.parentNode;
        				col.data.left = parseInt(hotBox.style.left);
        				col.data.top = parseInt(hotBox.style.top);
        				col.data.width = parseInt(hotBox.style.width);
        				col.data.height = parseInt(hotBox.style.height);
        			}
					row.cols.push(col);
        		}
        		var cd = getCountDown(rows[i]);
        		if(cd){
        			row.countDown = {
        					top:parseInt(this.countDownNode.style.top),
        					left:parseInt(this.countDownNode.style.left),
        					cssText:this.cssText,
        			}
        		}
        		data.rows.push(row);
        		
        	}
        	for(var i=0,l=data.rows.length;i<l;i++){
        		console.log('y:'+data.rows[i].y+',height:'+data.rows[i].height);
        		for(var j=0,ll=data.rows[i].cols.length;j<ll;j++)
        		{
        			console.log('x:'+data.rows[i].cols[j].x+',width:'+data.rows[i].cols[j].width);
        		}
        	}
        	data.image = this.image;
        	return data;
        };
        pro._$getBannerMdl = function(){
        	var rows = e._$getByClassName(this.defautlCnt,'m-vbox');
        	if(!rows.length){
        		ut0.showError('请拉出一个水平参考线，用于放置倒计时模块');
        	} else{
        		rows.sort(function(item1,item2){
        			return item1.clientHeight- item2.clientHeight;
        		});
        		return rows[0];
        	}
        };
        pro._$addCountDownModule = function(node){
        	this.hasCountDownMdl = true;
        	this.countDownNode = e._$create('div','m-countdown');
        	this.countDownNode.innerHTML = e2._$getTextTemplate('txt-countdown');
        	this.countDownNode.style.position ='absolute';
        	this.countDownNode.style.top = '18px';
        	v._$addEvent(this.countDownNode,'mousedown',this.__onCountDownMouseDown._$bind(this));
        	v._$addEvent(this.countDownNode,'mousemove',this.__oCountDownnMouseMove._$bind(this));
        	v._$addEvent(this.countDownNode,'mouseup',this.__oCountDownnMouseUp._$bind(this));
        	v._$addEvent(this.countDownNode,'click',this.__oCountDownnClick._$bind(this));
        	this.coundown = e._$getByClassName(this.countDownNode,'coundown')[0];
        	
        	var vmodule = this._$getBannerMdl();
        	if(vmodule){
        		this.countDownNode.style.left =( vmodule.clientWidth - 260 )/2 +'px' ;
        		vmodule.appendChild(this.countDownNode);
        	}
        };
        pro.__oCountDownnClick = function(event){
        	var elm = v._$getElement(event);
        	if(e._$hasClassName(elm,'j-editcountdown')){
        		CountDownSetting._$allocate({onok:this.__onCountDownSettingOK._$bind(this),cssText:this.cssText||''})._$show();
        	} else if(e._$hasClassName(elm,'j-drag')){
        		
        	}
        }
        pro.__onCountDownSettingOK = function(style){
        	this.cssText =[];
        	this.cssText.push('font:'+style.font)
        	this.cssText.push('color:'+style.color)
        	this.cssText.push('border:1px solid '+style.borderColor)
        	this.cssText.push('background-color:'+style.bgColor)
        	this.cssText.push('opacity:'+style.bgOpcity||1);
        	this.cssText = this.cssText.join(';');
        	this.coundown.style.cssText = this.cssText;
        	
        };
        pro.__onCountDownMouseDown = function(event){
        	v._$stop(event);
        	this.countDown = true;
        	var elm = v._$getElement(event);
        	if(elm.tagName=='INPUT'){
        		this.offsetY = event.offsetY||event.layerY;
        		this.offsetX = event.layerX||event.offsetX;
        	} else{
	        	this.offsetY = event.offsetY||event.layerY;
	        	this.offsetX = event.layerX||event.offsetX;
        	}
        };
        pro.__oCountDownnMouseMove = function(event){
        	v._$stop(event);
        	if(this.countDown){
        		this.countDownNode.style.left = ((event.pageX-offsetLeft) - this.offsetX) + 'px';
        		this.countDownNode.style.top = ((event.pageY-topHeight) - this.offsetY) + 'px';
        	}
        };
        /**
         * 拖动纵向参考线时计划落在哪个纵向的box
         */
        pro.__getTargetVBoxCase = function(_y){
        	var list = e._$getByClassName(this.defautlCnt,'m-vbox');
        	if(!list.length){
        		return
        	}
        	list.sort(function(item1,item2){
        		return item1.clientHeight - item2.clientHeight;
        	});
        	var box;
        	for(var i=0,l=list.length;i<l;i++){
        		console.log(list[i].clientHeight);
        		if(list[i].clientHeight>_y){
        			box = list[i];
        			break;
        		}
        	}
        	return box;
        };
        pro.__oCountDownnMouseUp = function(event){
        	v._$stop(event);
        	this.countDown = false;
        	var boxCase = this.__getTargetVBoxCase(event.clientY+document.body.scrollTop-topHeight);
        	//this.countDownNode.style.left = ((event.pageX-offsetLeft) - this.offsetX) + 'px';
    		this.countDownNode.style.top = ((event.pageY-topHeight-boxCase.offsetTop) - this.offsetY) + 'px';
    		boxCase.appendChild(this.countDownNode);
        };
        pro._$hasCountDownMdl = function(node){
        	return this.hasCountDownMdl;
        };
        return $$Cut;
        
    });