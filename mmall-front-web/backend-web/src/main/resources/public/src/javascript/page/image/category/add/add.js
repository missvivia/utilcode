/*
 * ------------------------------------------
 * 拒绝理由控件封装
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    '{lib}base/element.js',
    'text!./add.html?v=1.0.0.0',
    'pro/components/modal/modal'
],function(e,_html,Modal,_p,_o,_f,_r){
    return Modal.extend({
        data:{
            title:'添加分类',
            name:''
        },
        content:_html,
        confirm:function(){
            var _value = (this.data.name||'').trim();
            var reg = /^[a-zA-Z0-9_\u4e00-\u9fa5]+$/; 
            if (!_value){
            	e._$get('err').innerHTML = "请输入分类名称";
               // this.data.name = '请输入分类名称';
                return;
            }else if(!reg.test(_value)){
            	e._$get('err').innerHTML = "请不要输入 ＂, % ' \ / ；| &.* 等特殊字符";
            	return;
            }
            this.$emit('onok',{
            	name:this.data.name
            });
        },
        close:function(){
            this.destroy();
        }
    });
});
