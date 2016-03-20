/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'text!./list.html',
    'pro/components/ListComponent',
    'pro/components/notify/notify'
],function(_u,_html,ListComponent,notify,_p,_o,_f,_r){
    return ListComponent.extend({
        url:'/schedule/list',
        api:'/schedule/audit/submit',
        template:_html,
        getExtraParam:function(){
            return this.data.condition;
        },
        refresh:function(_data){
            if (!!_data.url){
                this.url = _data.url;
                delete _data.url;
            }
        	this.data.current = 1;
            this.data.condition = _data;
            this.$emit('updatelist');
        },
        map:{1:'待提交',2:'待审核',3:'审核通过',4:'审核拒绝','202':'已上线','-1':'失效'},
        submit:function(_schedule){
        	this._sendReq(this.api,_schedule);
        },
        _sendReq:function(_url,_schedule){
            this.$request(_url,{
                method:'GET',
                data:{id:_schedule.id},
                onload:function(_json){
                    notify.show('审核操作成功');
                    _schedule.status = 2;
                    //this.$emit('updatelist');
                },
                onerror:function(_error){
                    notify.showError('审核操作失败');
                }
            });
        },
    });
});