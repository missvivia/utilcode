/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{pro}widget/module.js'
        ,'{pro}widget/ui/calendar/calendar.js'
        ,'{lib}util/template/jst.js'],
    function(ut,v,e,Module,Calendar,e1,p,o,f,r) {
        var pro;
        var polist=[
{"id":"1","brandId":"123","name":"秋水","date":"2014-9-1","action":"修改档期申请","state":0,"desc":"档期修改为从2014.09.05开始","order":"1"},
{"id":"1","brandId":"123","name":"秋水","date":"2014-9-2","action":"修改档期申请","state":1,"desc":"档期修改为从2014.09.05开始","order":"1"},
{"id":"1","brandId":"123","name":"秋水","date":"2014-9-3","action":"修改档期申请","state":2,"desc":"档期修改为从2014.09.05开始"},
{"id":"1","brandId":"123","name":"秋水","date":"2014-9-5","action":"修改档期申请","state":0,"desc":"档期修改为从2014.09.05开始"},
{"id":"1","brandId":"123","name":"秋水","date":"2014-9-1","action":"修改档期申请","state":3,"desc":"档期修改为从2014.09.05开始"},
{"id":"1","brandId":"123","name":"秋水","date":"2014-9-4","action":"修改档期申请","state":4,"desc":"档期修改为从2014.09.05开始"},
{"id":"1","brandId":"123","name":"秋水","date":"2014-9-1","action":"修改档期申请","state":0,"desc":"档期修改为从2014.09.05开始"}];
        p._$$ExhibitionModule = NEJ.C();
        pro = p._$$ExhibitionModule._$extend(Module);
        
        pro.__init = function(_options) {
        	_options.tpl ='jst-template';
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
			this.__getData()
        };
        
        pro.__getNodes = function(){
          var node = e._$get('exhibition');
          this.table = e._$get('table');
          var list = e._$getByClassName(node,'j-flag');
          this.province = list[0];
          this.cagtegory = list[1];
          this.key = list[2];
          this.searchBtn = list[3];
          this.saveOrderBtn = list[4];
        };
        
        pro.__addEvent = function(){
           v._$addEvent(this.province,'change',this.__onProvinceChange._$bind(this));
           v._$addEvent(this.cagtegory,'change',this.__onCategoryChange._$bind(this));
           v._$addEvent(this.searchBtn,'click',this.__onSearchBtnClick._$bind(this));
           v._$addEvent('table','click',this.__onTableClick._$bind(this));
           v._$addEvent(this.saveOrderBtn,'click',this.__onSaveOrderBtnClick._$bind(this))
        };
        pro.__onProvinceChange = function(){
        	this.__onSearchBtnClick();
        };
        pro.__onCategoryChange = function(){
        	this.__onSearchBtnClick();
        };
        pro.__onSearchBtnClick = function(){
        	var pid = this.province.options[this.province.selectedIndex].value;
        	var cid = this.cagtegory.options[this.cagtegory.selectedIndex].value;
        	var key = this.key.value;
        	//location.href ='/po/exhibition?key='+encodeComponentURI(key)+'&provinceid='+pid+'&=categoryid='+cid;
        	this.__getData();
        };
        
        pro.__getData = function(){
        	this.polist = polist;
        	this.__renderItems();
        };
        pro.__renderItems = function(){
        	e1._$render(this.table,'jst-po-items',{polist:this.polist});
        }
        pro.__onTableClick = function(event){
        	var findParent = function(node,tag){
        		var parent = node.parentNode;
        		while(parent.tagName.toLowerCase()!=tag){
        			parent = parent.parentNode;
        		}
        		return parent;
        	}
            var elm = v._$getElement(event);
            if(e._$hasClassName(elm,'j-up')){
            	var index=  parseInt(e._$dataset(elm,'index'));
            	if(index==0){
            		return;
            	}
            	var current = this.polist[index];
            	var pre = this.polist[index-1];
            	if(pre.order!=1){
            		this.polist[index-1] = current;
            		this.polist[index] = pre;
            	}
            	this.__renderItems();
            } else if(e._$hasClassName(elm,'j-down')){
            	var index=  parseInt(e._$dataset(elm,'index'));
            	if(index==this.polist.length-1){
            		return;
            	}
            	var current = this.polist[index];
            	var next = this.polist[index+1];
        		this.polist[index+1] = current;
        		this.polist[index] = next;
            	this.__renderItems()
            } else if(e._$hasClassName(elm,'j-top')){
            	var index=  parseInt(e._$dataset(elm,'index'));
        		var item = this.polist.splice(index,1);
        		item[0].order= 1;
        		r.unshift.apply(this.polist,item);
            	this.__renderItems()
            } else if(e._$hasClassName(elm,'j-clear')){
            	var index=  parseInt(e._$dataset(elm,'index'));
        		var item = this.polist.splice(index,1),index=0;
        		for(var i=0,l=this.polist.length;i<l;i++){
        			if(this.polist[i].order!=1){
        				index = i;
        				break;
        			}
        		}
        		item[0].order = null;
        		this.polist.splice(index,0,item[0]);
            	this.__renderItems()
            } 
        };
        pro.__onSaveOrderBtnClick = function(){
        	
        }
        p._$$ExhibitionModule._$allocate();
    });