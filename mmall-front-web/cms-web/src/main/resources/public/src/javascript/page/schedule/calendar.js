/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{pro}widget/module.js'
        ,'{pro}widget/ui/calendar/calendar.js'
        ],
    function(ut,v,e,Module,Calendar,p,o,f,r) {
        var pro;
        
        p._$$CalendarModule = NEJ.C();
        pro = p._$$CalendarModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
            this.__initCalender();
           
        };
        
        pro.__getNodes = function(){
          var node = e._$get('audit');
          var list = e._$getByClassName(node,'j-flag');
          this.province = list[0];
          this.title = list[1];
          this.calendarBox = list[2];
        };
        pro.__initCalender = function(){
            this.calendar = Calendar._$allocate({parent:this.calendarBox,province:this.province.options[this.province.selectedIndex].value});
        };
        pro.__addEvent = function(){
           v._$addEvent(this.province,'change',this.__onProvinceChange._$bind(this));
        };
        
        pro.__onProvinceChange = function(){
        	//ajax 通过
        	this.calendar._$setProvince(this.province.options[this.province.selectedIndex].value);
        };
        
        p._$$CalendarModule._$allocate();
    });