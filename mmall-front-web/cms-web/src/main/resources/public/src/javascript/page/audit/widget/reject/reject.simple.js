/*
 * ------------------------------------------
 * 拒绝理由控件封装
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
	'base/event',
    'text!./reject.simple.html',
    'pro/components/modal/modal'
],function(_v,_html,Modal,_p,_o,_f,_r){
    var Modal =  Modal.extend({
			        data:{
			            title:'拒绝理由',
			            reason:'',
			            message:''
			        },
			        onKeyDown:function(event){
			        	if(this.data.reason.length>=60){
			        		_v._$stop(event);
			        	}
			        },
			        content:_html,
			        confirm:function(){
			            var _value = (this.data.reason||'').trim();
			            if (!_value){
			                this.data.message = '请输入拒绝理由';
			                return;
			            }
			            this.$emit('reject',{
			                reason:this.data.reason
			            });
			        },
			        close:function(){
			            this.destroy();
			        }
			    });
   
    return Modal;
});
