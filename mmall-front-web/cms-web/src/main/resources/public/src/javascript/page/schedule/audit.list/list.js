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
    'pro/components/notify/notify',
    '{pro}widget/layer/po/change.po.win.js',
    '{pro}page/audit/widget/reject/reject.simple.js'
],function(_ut,_html,ListComponent,notify,POChangeWin,RejectWin,_p,_o,_f,_r){
    return ListComponent.extend({
        url:'/schedule/audit/list',
        api:'/schedule/audit/',
        template:_html,
        map:{1:'待提交',2:'待审核',3:'审核通过',4:'审核拒绝'},
        submit:function(schedule){
        	//this._sendReq(this.api,schedule);
        	this.$request(this.api,{
                method:'POST',
                data:{id:schedule.id},
                onload:function(_json){
                    notify.show('审核操作成功');
                    schedule.status = 2;
                    //this.$emit('updatelist');
                },
                onerror:function(_error){
                    notify.showError('审核操作失败');
                }
            });
        },
        pass:function(schedule,isPass){
        	var path;
        	if(isPass){
        		path = 'pass';
        	} else{
        		path = 'reject';
        	}
        	if(!isPass){
        		var _win = new RejectWin();
                _win.$on('reject',function(_event){
                	 this.$request(this.api+'reject',{
                         method:'POST',
                         norest:true,
                         data:_ut._$object2query({id:schedule.id,desc:_event.reason}),
                         onload:function(_json){
                             notify.show('操作成功');
                             schedule.status = 4;
                             //this.$emit('updatelist');
                         },
                         onerror:function(_error){
                             notify.showError('审核操作失败');
                         }
                     });
                    _win.close();
                }._$bind(this));
        	} else{
	        	this.$request(this.api+'pass',{
	                method:'POST',
	                norest:true,
	                data:_ut._$object2query({id:schedule.id,pass:isPass}),
	                onload:function(_json){
	                    notify.show('操作成功');
	                    schedule.status = 3;
	                    //this.$emit('updatelist');
	                },
	                onerror:function(_error){
	                    notify.showError('审核操作失败');
	                }
	            });
        	}
        },
        change:function(schedule){
        	POChangeWin._$allocate({type:1,schedule:schedule,onok:function(data){
        		this.$request(this.api+'change',{
                    method:'POST',
                    data:{id:schedule.id,date:data.date,desc:'',poFollowerUserName:data.poFollowerUserName},
                    onload:function(_json){
                    	if(_json.code==200){
	                        notify.show('调整档期成功');
	                        //schedule.status = 3;
	                        schedule.startTime = data.date;
	                        schedule.poFollowerUserName = data.poFollowerUserName;
	                        this.$update();
                    	} else{
                    		notify.show(_json.msg);
                    	}
                    }._$bind(this),
                    onerror:function(_error){
                        notify.showError('调整档期时间失败');
                    }
                });
        	}._$bind(this)
        })._$show();
        },
        format:function(timestamp){
        	return _ut._$format(new Date(timestamp),'yyyy-MM-dd');
        },
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
        _sendReq:function(_url,schedule){
            this.$request(_url,{
                method:'POST',
                data:{id:schedule.id},
                onload:function(_json){
                    notify.show('审核操作成功');
                    schedule.status = 3;
                    //this.$emit('updatelist');
                },
                onerror:function(_error){
                    notify.showError('审核操作失败');
                }
            });
        },
    });
});