/**
 * xx平台活动编辑——商品添加页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

define([
    '{lib}base/element.js',
    '{lib}base/event.js',    
    '{pro}extend/util.js', 
    '{pro}extend/request.js', 
    "{lib}util/form/form.js",
    '{pro}widget/module.js', 
    '{lib}util/query/query.js',
    '{lib}util/file/select.js',
    '{pro}components/promotion/couponItem.js?v=1.0.0.0',
    '{pro}components/promotion/codeTypeSelect.js?v=1.0.0.2',
    '{pro}components/datepicker/datepicker.js',
    '{pro}components/notify/notify.js',
    './address.js',
    '{lib}util/chain/NodeList.js'
    ],
    function(e1, v, _, request, ut, Module, e, c, CouponItem, CodeTypeSelect, DatePicker, notify,Address,$) {
        var pro, 
            SAVE_URL = "/coupon/save",
            $$CustomModule = NEJ.C(),
            dom = Regular.dom,
            pro = $$CustomModule._$extend(Module);
		var _findParent = function(_elm,_className){
			while(_elm&&!e1._$hasClassName(_elm,_className)){
				_elm = _elm.parentNode;
			}
			return _elm;
		};

              // 静态变量
        var consts ={
            ID: _.getSearch().id
        }; 

        
        pro.__init = function(_options) {
            if(window.__editable == 0){
                $("#st")._$text(_.formatDate(new Date(window.__data__.startTime),'yyyy-MM-dd'));
                $("#et")._$text(_.formatDate(new Date(window.__data__.endTime),'yyyy-MM-dd'));
            	var conditionList = [];
                for(var i = 0 ; i < window.__data__.itemList.length;i++){
            		var item = window.__data__.itemList[i];
            		var condition = item.condition;
            		var tmpValues = (condition.value||"").split("-");
            		var result;
            	      switch(condition.type){
            	        case 1:
            	          condition.minPrice = tmpValues[0] ||"";
            	          result = item.result.length == 0 ? "" : item.result[0].value;
            	          conditionList[i] = "满" + condition.minPrice + "元，减" + result + "元";
            	          break;
            	        case 2:
            	          condition.minNum = tmpValues[0] ||"";
            	          result = item.result.length == 0 ? "" : item.result[1].value;
            	          if(result != ""){
            	        	  conditionList[i] = "满" + condition.minNum + "元，打" + (10 - result/10) + "折";
            	          }
            	      }
            	}
                $("#condition")._$text(conditionList.join("；")); 
            }
        	this.__supInit(_options);
        	if(window.__editable == 0){
        		return;
        	}
            this.data = window.__data__;
            this.__type = this.data.favorType||0;
            this.__initCodeTypeSelect();
            this.__initCouponItem();
            this.__initDateTime();
            this.__initWebForm();
//            this.__initAddress();
            if(nes.one(".j-submit")){
            	dom.on(nes.one(".j-submit"), 'click', this.__submit._$bind(this));
            }
            
            
            this.__radios = nes.all(".favorTypeRadio");
            this.__items = nes.all(".j-items");
            
          //绑定切换按钮的背景切换效果
    		for(var i=0; i < this.__radios.length; i++){
    			v._$addEvent(this.__radios[i], 'click', this.__onRadioClick._$bind(this,this.__radios[i]));
    		}
    		if(!!this.__addrui){
				this.__addrui._$recycle();
			}
        };
        

        /**
         * 初始化webform
         */
        pro.__initWebForm = function(){

            function checkTime(ev){

            }

            this.__baseform = ut._$$WebForm._$allocate({
              form: nes.one(".j-baseform"),
              oninvalid: function(ev){
                var msg = dom.attr(ev.target, "data-msg") || ev.code;
                if(!msg) return;
                notify.notify({
                  message: msg,
                  type: "error",
                  duration: 3000
                });
              },
              oncheck: function(ev){
//                var target = ev.target;
//                if(target.name === "startTime") {
//                  if(!target.value) ev.value = "开始时间不能为空";
//                }
//                if(target.name === "endTime") {
//                  if(!target.value) ev.value = "结束时间不能为空";
//                }
              }
            });
        };
        pro.__initAddress = function(){
        	this.address = $('.j-flag')[0];
        	this.__addr =  Address._$allocate({address:this.address});
        };
        /**
         * 初始化条件效果项组件
         */
        pro.__initCouponItem = function(){
            var data = this.data;
            var list0 = [{
                schedules: [],
                condition:{type: 1},
                result: []            	
            }];
            var list1 = [{
                schedules: [],
                condition:{type: 2},
                result: []            	
            }];
            // 选择项组件
            this.__couponItem0 = new CouponItem({
                data: {
                 items: this.__type? list0 : data.itemList || list0,
                 type: 0
                }
             }).$inject(".j-items-0");
             
             this.__couponItem1 = new CouponItem({
                 data: {
                  items: this.__type? data.itemList || list1 : list1,
                  type: 1
                 }
              }).$inject(".j-items-1");
        };
        
        /**
         * 初始化条件效果项组件
         */
        pro.__initCodeTypeSelect = function(){
            var data = this.data;
            // 选择项组件
            this.__codeTypeSelect = new CodeTypeSelect({
            	data: {
                    couponVO: data||{}
                  }
            }).$inject("#invalidTime","after");
        };
        
        /**
         * 初始化datePicker选择控件
         */
        pro.__initDateTime = function(){
          var data = this.data;
            // 选择项组件
            this.__datePicker1 = new DatePicker({
               data: {
                select: data.startTime,
                name:"startTime"
               }
            }).$inject("#form-startTime");

            this.__datePicker2 = new DatePicker({
               data: {
                select: data.endTime,
                name:"endTime"
               }
            }).$inject("#form-endTime");
        };

        /**
         * [ description]
         * @return {[type]} [description]
         */
        pro.__gatherData = function(){
            // 收集items
            // 收集webform 
            if(!this.__baseform._$checkValidity()){
                _.smoothTo('.j-baseform');
                return;
            }

            var baseData = this.__baseform._$data();  
            
            if(isNaN(baseData.startTime)){
                notify.notify({
                  type: "error",
                  message: "开始时间不能为空"
                });
                _.smoothTo('.j-baseform');
                return;
              }
              
            if(isNaN(baseData.endTime)){
                notify.notify({
                  type: "error",
                  message: "结束时间不能为空"
                });
                _.smoothTo('.j-baseform');
                return;
              }
            baseData.endTime += (23*3600 + 59*60 + 59)*1000;
            if(baseData.endTime < baseData.startTime){
              notify.notify({
                type: "error",
                message: "结束时间不能小于开始时间"
              });
              _.smoothTo('.j-baseform');
              return;
            }
            if(baseData.endTime < (new Date).getTime()){
                notify.notify({
                  type: "error",
                  message: "结束时间不能小于当前时间"
                });
                _.smoothTo('.j-baseform');
                return;
              }
            //处理data
            this.__propareBase(baseData);
            
            var codeTypeData = this.__codeTypeSelect.checkAndGetData();

            if(!codeTypeData){
                _.smoothTo('.j-baseform');
                return
            }
            _.extend(baseData, codeTypeData, 1);

            var itemData = this.__type?this.__couponItem1.checkAndGetData() : this.__couponItem0.checkAndGetData();

            if(!itemData){
                _.smoothTo('.j-items-'+(this.__type?1:0));
                return
            }

            baseData.itemList = itemData;

            return baseData;

        };

        /**
         * propareBase
         * @return {[type]} [description]
         */
        pro.__propareBase = function(baseData){
            var _ds = new Date(),
               _de = new Date();
            _ds.setTime(baseData.startTime);
            _de.setTime(baseData.endTime);
            baseData.startTime = +(_.setDateTOStart(_ds));
            baseData.endTime = +(_.setDateTOEnd(_de));
        };

        /**
         * 提交表单 
         * @return {[type]} [description]
         */
        pro.__submit = function(ev){
          ev.preventDefault();
            var data = this.__gatherData();
            if(!data) return;
            if(consts.ID){
            	data.id=consts.ID;
            }
            data.areaIds;
            var onload = this.__onAfterSave._$bind(this);
            request(SAVE_URL, {
//                method: consts.ID? "PUT": "POST",
            	method : "post",
                data: data,
                onload: onload,
                onerror: onload
            });
        };
        
        /**
    	 * 点击radio重新渲染条件和效果
    	 * @return {[type]} [description]
    	 */
        pro.__onRadioClick = function(_node){
        	var _data = this.__baseform._$data(),
        		_type = parseInt(_data.favorType);
        	if(this.__type ==_type)return;
        	e1._$addClassName(this.__items[this.__type], 'f-dn');
        	e1._$delClassName(this.__items[_type], 'f-dn');
        	this.__type = _type;
    	};


        /**
         * 请求保存回调
         * @param  {[type]} json [description]
         * @return {[type]}      [description]
         */
        pro.__onAfterSave = function(json){
            if(json && json.code === 200){
                notify.notify({
                    type: "success",
                    message: "保存活动成功, 三秒后跳转到活动列表"
                });
                setTimeout(function(){window.location= "/promotion/coupon";},3000);
            }else{
                notify.notify({
                    type: "error",
                    message: json && json.message || "保存活动失败"
                });

            }
        };

        $$CustomModule._$allocate({});

        return $$CustomModule;
    });