/**
 * 品购装修页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js',
        '{lib}base/event.js',
        '{lib}base/element.js',
        '{lib}util/file/select.js',
        '{pro}widget/ui/color/color.js',
        '{lib}util/tab/tab.js',
        '{pro}widget/cut.js',
        '{pro}widget/module.js',
        '{pro}widget/list/product.list.js'
        ,'{pro}widget/ui/warning/warning.js'],
    function(ut,v,e,s,Color,t,Cut,Module,ListModule,Warning,p,o,f,r) {
        var pro;
        var test ={"width":1000,"rows":[{"height":551,"y":0,"cols":[{"x":0,"width":998}]},{"height":401,"y":551,"cols":[{"x":0,"width":998}]},{"height":738,"y":952,"cols":[{"x":0,"width":231,"id":"wzfbg3SoOv","data":{"name":"2Ay0KpGqLx","left":61,"top":79,"width":149,"height":578}},{"x":231,"width":230,"id":"wzfbg3SoOv","data":{"name":"2Ay0KpGqLx","left":45,"top":173,"width":168,"height":541}},{"x":461,"width":229,"id":"wzfbg3SoOv","data":{"name":"2Ay0KpGqLx","left":80,"top":45,"width":145,"height":589}},{"x":690,"width":308,"id":"wzfbg3SoOv","data":{"name":"2Ay0KpGqLx","left":129,"top":261,"width":160,"height":261}}]},{"height":639,"y":1690,"cols":[{"x":0,"width":220,"id":"wzfbg3SoOv","data":{"name":"2Ay0KpGqLx","left":24,"top":3,"width":149,"height":560}},{"x":220,"width":211,"id":"8RDfGo0KDT","data":{"name":"iqdinTpVRI","left":20,"top":72,"width":182,"height":529}},{"x":431,"width":367,"id":"8RDfGo0KDT","data":{"name":"iqdinTpVRI","left":67,"top":204,"width":280,"height":397}},{"x":798,"width":200,"id":"8RDfGo0KDT","data":{"name":"iqdinTpVRI","left":29,"top":15,"width":151,"height":612}}]},{"height":768,"y":2329,"cols":[{"x":0,"width":269,"id":"wzfbg3SoOv","data":{"name":"2Ay0KpGqLx","left":56,"top":285,"width":190,"height":407}},{"x":269,"width":277,"id":"8RDfGo0KDT","data":{"name":"iqdinTpVRI","left":55,"top":51,"width":193,"height":679}},{"x":546,"width":219,"id":"8RDfGo0KDT","data":{"name":"iqdinTpVRI","left":36,"top":114,"width":139,"height":641}},{"x":765,"width":233,"id":"8RDfGo0KDT","data":{"name":"iqdinTpVRI","left":43,"top":204,"width":163,"height":515}}]},{"height":830,"y":3097,"cols":[{"x":0,"width":261,"id":"wzfbg3SoOv","data":{"name":"2Ay0KpGqLx","left":53,"top":78,"width":159,"height":660}},{"x":261,"width":252,"id":"8RDfGo0KDT","data":{"name":"iqdinTpVRI","left":38,"top":196,"width":201,"height":621}},{"x":513,"width":247,"id":"8RDfGo0KDT","data":{"name":"iqdinTpVRI","left":52,"top":150,"width":163,"height":640}},{"x":760,"width":238,"id":"xtDkSxcnDn","data":{"name":"pOHCxURnc8","left":50,"top":102,"width":169,"height":640}}]},{"height":454,"y":3927,"cols":[{"x":0,"width":998}]}],"image":{"url":"http://localhost:8080/res/images/cut.jpg","width":"1440","height":"4381"}};
        	
    	p._$$CustomModule = NEJ.C();
        pro = p._$$CustomModule._$extend(Module);
        
        pro.__init = function(_options) {
        	_options.tpl = 'jst-template';
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
			this.__onFileChange();
			t._$$Tab._$allocate({selected:'active',list:this.tab.children,onchange:this.__onTabChange._$bind(this)})
			this.__cbGetData(test);
			//this.__setCustomBackGround('',1440,4381);
        };
        pro.__cbGetData = function(result){
        	//this.colorpick._$setColor(test.backgroundColor);
        	this.colorpick = Color._$allocate({parent:this.colorp,clazz:'m-cp',onchange:this.__onColorChange._$bind(this),defaultColor:result.background})
        	this.cut = Cut._$allocate({node:this.custNode,data:result});
        };
        pro.__onTabChange = function(event){
        	var index = event.index;
        	if(index==0){
        		e._$delClassName(this.imgCnt,'f-dn');
        		e._$addClassName(this.goodsCnt,'f-dn');
        	} else{
        		e._$addClassName(this.imgCnt,'f-dn');
        		e._$delClassName(this.goodsCnt,'f-dn');
        		
        		if(!this.mdl){
        			this.mdl = ListModule._$allocate({node:this.goodsContainer,pager:this.pager})
        		}
        	}
        }
        pro.__onColorSelect = function(event){
        	this.colorp.value =event.color;
        	this.colorpick = this.colorpick._$recycle();
        };
        pro.__getNodes = function(){
            var list = e._$getByClassName('body','j-flag');
            this.side = list[0];
            this.act = list[1];
            this.tab = list[2];
            this.imgCnt = list[3]
            this.label = list[4];
            this.folderSlt = list[5];
            this.size = list[6];
            this.imgCase = list[7];
            this.goodsCnt = list[8];
            this.custNode = list[9];
            list = e._$getByClassName('saveact','z-flag');
            this.colorp = list[1];
            this.save = list[0];
            list = e._$getByClassName(this.goodsCnt,'d-flag');
            this.goodsContainer = list[0];
            this.pager = list[1];
            this.addCountDownBtn = e._$get('countdown');
            //this.rulerv = e._$get('ruler_v');
        };
        
        pro.__addEvent = function(){
           s._$bind(this.__label, {
                name: 'img',
                multiple: false,
                accept:'image/*',
                onchange: this.__onFileChange._$bind(this)
            });
          v._$addEvent(this.imgCase,'click',this.__onImageCaseClick._$bind(this));
          v._$addEvent(this.act,'click',this.__onActClick._$bind(this));
          v._$addEvent(this.save,'click',this.__onSaveClick._$bind(this));
          //v._$addEvent(this.colorp,'click',this.__onColorPickClick._$bind(this));
          v._$addEvent(this.addCountDownBtn,'click',this.__onAddCountDownClick._$bind(this))
        };
        pro.__onAddCountDownClick = function(){
        	if(!this.cut._$hasCountDownMdl()){
        		this.cut._$addCountDownModule(this.__countDownNode);
        	} else{
        		if(this.warn){
        			this.warn =  this.warn._$recycle();
        		}
        		this.warn = Warning._$allocate({text:'已添加倒计加模块'});
        	}
        	
        	
        };
        pro.__onColorPickClick = function(event){
        	if(!this.colorpick){
        		
        	}
    		
        };
        pro.__onColorChange = function(color){
        	this.colorp.value = color;
        }
        pro.__onActClick = function(){
        	if(e._$hasClassName(this.side,'j-min')){
        		e._$delClassName(this.side,'j-min');
        		this.act.innerText ='隐藏';
        	} else{
        		e._$addClassName(this.side,'j-min');
        		this.act.innerText ='显示';
        	}
        };
        pro.__onImageCaseClick = function(event){
        	var elm = v._$getElement(event);
        	if(elm.tagName =='IMG'){
        		this.__setCustomBackGround(elm.src,e._$dataset(elm,'width'),e._$dataset(elm,'height'))
        	}
        };
        
        pro.__setCustomBackGround = function(url,width,height){
        	//this.custNode.style.cssText = css.join('');
        	this.cut._$setCustomBackground( url,width,height );
        	
        };
        pro.__onSaveClick = function(){
        	var data = this.cut._$getCutData();
        	data.background = this.colorpick._$getColor()||'#ffffff';
        	if(data.rows.length==0){
        		alert('请从标尺拖参考线么效果图')
        	}
        }
        pro.__onFileChange = function(_data){
        	
        };
        p._$$CustomModule._$allocate();
    });