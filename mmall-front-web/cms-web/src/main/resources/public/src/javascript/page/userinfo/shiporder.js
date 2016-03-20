/**
 * 用户信息详情
 * author cheng-lin(cheng-lin@corp.netease.com)
 *
 */

define(['{lib}base/util.js',
        '{lib}base/klass.js',
        '{lib}base/event.js',
        '{lib}base/element.js',
        '{pro}widget/module.js',
        '{pro}widget/form.js',
        '{pro}components/notify/notify.js'
    ],
  function(_ut,_k,_v,_e,Module,_ut0,notify,p) {
    var _pro;

    p._$$Module = NEJ.C();
    pro = p._$$Module._$extend(Module);

    pro.__init = function(_options) {
      this.__super(_options);
      var _form = _e._$get('search-form');
      _ut0._$$WebForm._$allocate({
      	form:_form,
      	onsubmit:function(data){
      		if(isNaN(data.startTime)){
      			data.startTime =0;
      		}
      		if(isNaN(data.endTime)){
      			data.endTime =2524579200000;
      		}
      		if(data.startTime > data.endTime){
      			 notify.notify({
  	                type: "error",
  	                message: "结束时间不能小于开始时间"
      	         });
      			 return;
      		}
          	
      	}._$bind(this)
      });
    };


    p._$$Module._$allocate();

    
  });