/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{pro}widget/module.js'
        ,'{pro}widget/ui/calendar/calendar.js'
        ,'{pro}widget/layer/place.schedule/place.schedule.js'
        ,'{lib}util/template/jst.js'
        ,'{pro}extend/util.js'],
    function(ut,v,e,Module,Calendar,PlaceScheduleWin,e1,ut1,p,o,f,r) {
        var pro;
        
        p._$$PlacePModule = NEJ.C();
        pro = p._$$PlacePModule._$extend(Module);
        
        pro.__init = function(_options) {
        	_options.tpl ='jst-template';
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
			this.podate = e._$dataset(this.date,'date');
			this.startDate = ut1.setDate(ut._$format(new Date(parseInt(this.podate)),'yyyy-MM-dd'));
			this.endDate = new Date(this.startDate);
			this.endDate.setDate(this.endDate.getDate()+5);
			
			this.now = +new Date();
			if(+this.endDate-this.now<0){
				this.expired = true;
			} else{
				this.expired = false;
			}
			this.startDateStr = ut._$format(this.startDate,'yyyy年MM月dd日');
			this.endDateStr = ut._$format(this.endDate,'yyyy年MM月dd日');
			this.__initDate();
        };
        pro.__initDate = function(){
        	this.__renderDate();
        	setInterval(this.__renderDate._$bind(this),1000);
        };
        pro.__getNodes = function(){
          var node = e._$get('exhibition');
          this.table = e._$get('table');
          var list = e._$getByClassName(node,'j-flag');
          this.province = list[0];
          this.cagtegory = list[1];
          this.poIDIpt = list[2];
          this.searchBtn = list[3];
          this.date = list[4];
          this.topBtn = list[5];
        };
        
        pro.__addEvent = function(){
           v._$addEvent(this.province,'change',this.__onProvinceChange._$bind(this));
           v._$addEvent(this.cagtegory,'change',this.__onCategoryChange._$bind(this));
           v._$addEvent(this.searchBtn,'click',this.__onSearchBtnClick._$bind(this));
           v._$addEvent(this.topBtn,'click',this.__onTopBtnClick._$bind(this));
        };
        pro.__onProvinceChange = function(){
        	this.__onSearchBtnClick();
        };
        pro.__onCategoryChange = function(){
        	this.__onSearchBtnClick();
        };
        pro.__onSearchBtnClick = function(){
        	
        };
        pro.__onTopBtnClick = function(){
        	var scheculeId = e._$dataset(this.topBtn,'id');
        	PlaceScheduleWin._$allocate({id:scheculeId})._$show();
        };
        
        pro.__renderDate = function(){
        	var now = +new Date();
        	this.remain = ut1.getRemainTime(+this.endDate - now);
        	e1._$render(this.date,'jst-po-date',{epxired:this.expired,remain:this.remain,start:this.startDateStr,end:this.endDateStr});
        }
        p._$$PlacePModule._$allocate();
    });