NEJ.define([
    'pro/extend/util',
    'base/element',
    'base/util',
    'lib/util/ajax/rest',
    'pro/components/notify/notify',
    'text!./feedback.html',
    'pro/widget/BaseComponent'
],function(_,_e,_u,_j,notify,_html,Component){
    return Component.extend({
        template:_html,
        data:{
            wordLimit:300,//suggestion字数限制
            suggestion:"",
            tel:"",
            email:"",
            submitting:false//正在提交标识，防止重复提交
        },
        computed:{
            remain:function(data){
              if(data.suggestion.length>=300)notify.notify("反馈字数超出！", "error"); 
              return data.wordLimit-data.suggestion.length;
            },
            valid:function(data){
                    return data.suggestion.length>0 && !data.submitting;
            },
            telClearVisible:function(data){//手机号清除icon是否可见
                return data.telFocus && data.tel.length>0;
            },
            emailClearVisible:function(data){//邮件地址清除icon是否可见
            	if(data.email.length>=50)notify.notify("邮箱地址字数超出！", "error"); 
                return data.emailFocus && data.email.length>0;
            }
        },
        url:'/feedback/submit',
        submit:function(){
            this.$update("submitting",true);
            var data=this.data;
            var regx=/^[1][3-8][0-9]{9}/;
            //手机号码验证失败
            if(data.tel.length>0 && !regx.exec(data.tel)){
                notify.notify({
                    type: "error",
                    message:"手机号码填写错误 "
                });
            }else{
                var that=this;
                _j._$request(this.url,{
                    data: {message:data.suggestion,email:data.email,phone:data.tel},
                    method:'post',
                    onload: function(_data){
                        if(_data&&_data.code==0){
                            that.$emit("succeeded");
                        }else{
                            notify.notify({
                                type: "error",
                                message:"提交失败,请稍后再试"
                            });
                        }
                        this.$update("submitting",false);
                    },
                    onerror: function(_data){
                        notify.notify({
                            type: "error",
                            message:"提交失败,请稍后再试"
                        });
                        this.$update("submitting",false);
                    }
                });
            }
        }
    });
});
