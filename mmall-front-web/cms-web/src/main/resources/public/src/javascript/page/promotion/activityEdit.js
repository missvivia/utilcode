/**
 * xx平台活动编辑——商品添加页
 * author hzzhenghaibo(hzzhenghaibo@corp.netease.com)
 */

// 总流程  activityEdit -> 
//      -activityItem(ListComponent) - >
//          - poModal  -> 
//              - poSearchList -> 
//                  - provinceSelect 
//                  - datepicker
//              
//  

define([
    '{lib}base/element.js',
    '{lib}base/event.js', 
    '{lib}base/util.js',   
    '{pro}extend/util.js', 
    '{pro}extend/request.js', 
    "{lib}util/form/form.js",
    '{pro}widget/module.js', 
    '{lib}util/query/query.js',
    '{lib}util/file/select.js',
    '{pro}components/promotion/activityItem.js',
    '{pro}components/datepicker/datepicker.js',
    'pro/components/notify/notify',
    'pro/components/po/poSelect',
    'pro/widget/util/datanotify',
    
    ],
    function(e1, v, u, _, request, ut, Module, e, c, ActivityItem, DatePicker, notify, PoSelect, t) {
        var pro, 
            SAVE_URL = "/promotion/save",
            $$CustomModule = NEJ.C(),
            dom = Regular.dom,
            pro = $$CustomModule._$extend(Module);


              // 静态变量
        var consts ={
            ID: _.getSearch().id
        }; 

        
        pro.__init = function(_options) {
            this.__supInit(_options);
            this.data = window.__data__;
            this.__type = this.data.favorType||0;
            this.__initPoSelect();
            this.__initActivityItem();
            this.__initDateTime();
            this.__initWebForm();
            
            if(nes.one(".j-submit")){
            	dom.on(nes.one(".j-submit"), 'click', this.__submit._$bind(this));
            }
            this.__radios = nes.all(".favorTypeRadio");
            this.__selectAll = nes.one(".selectedAllPro");
            this.__items = nes.all(".j-items");
            //绑定切换按钮的背景切换效果
        		for(var i=0; i < this.__radios.length; i++){
        			v._$addEvent(this.__radios[i], 'click', this.__onRadioClick._$bind(this,this.__radios[i]));
        		}
            v._$addEvent(this.__selectAll, 'click', this.__onSelectedAllClick._$bind(this,this.__selectAll));
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

        /**
         * 初始化条件效果项组件
         */
        pro.__initActivityItem = function(){
            var data = this.data;
            // 选择项组件
            this.__activityItem0 = new ActivityItem({
               data: {
                items: this.__type? [] : data.itemList || [],
                type: 0
               }
            }).$inject(".j-items-0");
            
            this.__activityItem1 = new ActivityItem({
                data: {
                 items: this.__type? data.itemList || [] : [],
                 type: 1
                }
             }).$inject(".j-items-1");
        };
        
        /**
         * 初始化po选择控件
         */
        pro.__initPoSelect = function(){
            var data = this.data,
            	item = {};
            if(data.itemList && data.itemList.length >0){
            	item = data.itemList[0];
            }
            // 选择项组件
            this.__poSelect = new PoSelect({
               data: {
                item: item,
                startTime: data.startTime,
                endTime: data.endTime
               }
            }).$inject("#activitySite", "after");
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
               },
               events: {
                select: function(date){
                  t._$pushDateTime({datetype:0,time:date});
                }
              }
            }).$inject("#form-startTime");

            this.__datePicker2 = new DatePicker({
               data: {
                select: data.endTime,
                name:"endTime"
               },
               events: {
                select: function(date){
                  t._$pushDateTime({datetype:1,time:date});
                }
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
              
            if(!baseData.provinceIds){
                notify.notify({
                  type: "error",
                  message: "至少选择一个站点"
                });
                _.smoothTo('.j-baseform');
                return;
            }
            //处理data
            this.__propareBase(baseData);


            var itemData = this.__type?this.__activityItem1.checkAndGetData() : this.__activityItem0.checkAndGetData();

            if(!itemData){
                _.smoothTo('.j-items-'+this.__type?1:0);
                return
            }
            
            var schedules = this.__poSelect.checkAndGetData();

            if(!schedules){
                _.smoothTo('.j-baseform');
                return
            }
            
            for(var i =0;i<itemData.length;i++){
            	itemData[i].schedules = schedules;
            }

            baseData.itemList = itemData;

            return baseData;

        };

        /**
         * propareBase
         * @return {[type]} [description]
         */
        pro.__propareBase = function(baseData){
//        	baseData.selectedProvince = baseData.selectedProvince.join(',');
            baseData.labelList = [];
            [1,2,4].forEach(function(num){
                baseData.labelList.push({
                    select: (baseData['type-' + num ]== "on") ? true : false,
                    type: num,
                    desc: baseData["desc-" + num],
                    name: baseData["name-" + num]
                });
                delete baseData['type-' + num ];
                delete baseData['desc-' + num ];
                delete baseData['name-' + num ];
            });
            //baseData.labelList = baseData.labels;
           // baseData.provinceIds = baseData.selectedProvince;
           var _ds = new Date(),
               _de = new Date();
            _ds.setTime(baseData.startTime);
            _de.setTime(baseData.endTime);
            baseData.startTime = +(_.setDateTOStart(_ds));
            baseData.endTime = +(_.setDateTOEnd(_de));
            if(!u._$isArray(baseData.provinceIds)){
            	baseData.provinceIds = [baseData.provinceIds];
            }
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
       * 点击radio重新渲染条件和效果
       * @return {[type]} [description]
       */
        pro.__onSelectedAllClick = function(_node){
          var _list = this.__baseform._$get("provinceIds");
          for(var i =0;i<_list.length;i++){
             if(_node.checked){
                 _list[i].checked=true;    

              }else{
                  _list[i].checked=false;
              }
          }
         
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
            var onload = this.__onAfterSave._$bind(this);
            request(SAVE_URL, {
                method: consts.ID? "PUT": "POST",
                data: data,
                onload: onload,
                onerror: onload
            });
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
                setTimeout(function(){window.location= "/promotion/activity";},3000);
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