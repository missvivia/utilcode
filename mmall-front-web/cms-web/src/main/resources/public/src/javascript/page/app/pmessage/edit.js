/*
 * ------------------------------------------
 * 消息管理 编辑
 * @version  1.0
 * @author   xwb(xiangwenbin@corp.netease.com)
 * ------------------------------------------
 */

NEJ.define([
  'base/util',
  'pro/extend/util',
  'pro/components/window/base',
  'pro/components/datepicker/datepicker2',
  'pro/components/notify/notify',
  'util/form/form'
], function(_u,_, BaseModal, dp,_notify,_webForm, p,o,f,r){

  var contentModal = BaseModal.extend({
        url: '/app/pmessage/',
        baseurl:'/app/pmessage/',
        config: function(data){
          _.extend(data, {
            
          });
          // form需要检查的字段
//          for (var i= 0, l=this.watchedAttr.length;i<l; i++){
//            var name = this.watchedAttr[i];
//            data.form[name] = {};
//          }
        },

        init: function(){
        	
          // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
        },
        
        $bindForm:function(){
        	if(!this.__form)
	        	this.__form = _webForm._$$WebForm._$allocate({
	                form:'message-form'
	        });
        },

        //重写confirm
        confirm: function(event){
          event.preventDefault();
          var _data=this.checkForm();
          if (_data){
              // 直接提交spread
              this.$emit('confirm', _data);
              this.destroy();
          }
        },
        checkForm: function(){
        	if(this.__form._$checkValidity()){
        		var _data=this.__form._$data();
	        	if(!_data.areaId){
	           		 _notify.showError("请选中一个站点");
	           		 return null;
	           	}
	           	if(!_data.os){
	   	       		 _notify.showError("请选中一个推广平台");
	   	       		 return null;
	           	}
	           	_data.id=this.data.message.id;
	           	if(_data.areaId instanceof Array)
	           		_data.areaId=_data.areaId.join(",");
	        	if(_data.os instanceof Array)
	        		_data.os=_data.os.join(",");
                if(_data.pushTime)
                    _data.pushTime=new Date(_data.pushTime).getTime();
	           	console.log(_data);
	           	return _data;
        	}else
        		return false;
        	
        }
    });

    return contentModal;
});