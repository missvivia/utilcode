/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
	'base/util',
    'pro/widget/util/preview',
    'text!./list.html',
    '../widget/list.js',
    '../widget/reject/reject.js',
    '../widget/reject/reject.simple.js',
    'pro/components/notify/notify',
    'pro/extend/config'
],function(_u, preview,_html,AuditList,RejectWin,SimpleRejectWin,notify,config,_p,_o,_f,_r){
    return AuditList.extend({
        url:'/rest/audit/product/search',
        api:'/rest/audit/product/',
        rejectType:1,
        template:_html,
        data:{config:config,limit:50},
        config:function(data){
        	this.supr(data);
        	data.condition["poId"]=window["g_poId"]||0;
        },
        xdrOption:function(){
        	return {method:'POST'};
        },
        audit:function(_item,_passed){
            this._doAudit({poList:[_item.poId],list:[_item.id]},_passed);
        },
        getExtraParam:function(){
        	this.data.condition.poId=window["g_poId"]||0;
            return this.data.condition;
        },
        computed:{
            allChecked:{
                get: function(_data){
                    return _data.list.length===(this._getList('checked')||_r).length;
                },
                set: function(_sign,_data){
                    _u._$forEach(
                        _data.list,function(_it){
                        	if(_sign){
                        		if((_it.productStatus==5||_it.productStatus==4)&&window["g_disabled"]==1){
                        			_it.checked = false;
                        		} else{
                        			_it.checked = _sign;
                        		}
                        	} else{
                        		_it.checked = _sign;
                        	}
                        }
                    );
                }
            }
        },
        batchAudit:function(_passed){
            var _list = this._getList('checked')||_r;
            if (!_list||!_list.length){
                notify.showError('请先选择要操作的项');
                return;
            }
            var _arr = [], _poarr=[];
            _u._$forEach(
                _list,function(_it){
                    _arr.push(_it.id);
                    _poarr.push(_it.poId);
                }
            );
            this._doAudit({poList:_poarr,list:_arr},_passed);
        },
        statusData:function(_item){
        	if(_item.productStatus==2){
        		if(window["g_disabled"]==1){
        			return "待审核（失效）";
        		}else{
        			return "待审核";
        		}
        	} else if(_item.productStatus==3){
        		if(window["g_disabled"]==1){
        			return "审核通过（失效）";
        		}else{
        			return "审核通过";
        		}
        	} else if(_item.productStatus==4){
        		if(window["g_disabled"]==1){
        			return "审核不通过（失效）";
        		}else{
        			return "审核不通过";
        		}
        	} else if(_item.productStatus==5){
        		return "失效";
        	}
        },
        preview: function(_item){
        	preview.preview({id:_item.id,scheduleId:_item.poId});
            return false;
        },
        isDisabled: function(){
	    	if(window["g_disabled"]==1)
	    		return true;
	    	else
	    		return false;
	    },
	    isReady: function(){
	    	if(window["g_isReady"]==1)
	    		return false;
	    	else
	    		return true;
	    },
        _doAudit:function(_data,_passed){
            var _name = !_passed?'reject':'pass',
                _url = this.api+_name;
            var _setStatusName = function(_ids,_status,_reason){
            	for(var i=0,l=_ids.length;i<l;i++){
            		for(var j=0,jl=this.data.list.length;j<jl;j++){
            			if(this.data.list[j].id==_ids[i]){
            				this.data.list[j].productStatus = _status;
            				this.data.list[j].reason = _reason||'';
            			}
            		}
            	}
            }._$bind(this);
            if (!_passed){
            	var _win = new RejectWin();
                _win.$on('reject',function(_event){
                	var _rejectData ={ids:_data.list,poList:_data.poList};
                	if(this.rejectType==1){
                		_rejectData.reason = _event.reason;
                		_rejectData.descp = _event.descp;
                	} else{
                		_rejectData.reason = _event.reason;
                	}
                    //this._sendReq(_url,_rejectData);
                    this.$request(_url,{
                        method:'POST',
                        data:_rejectData,
                        onload:function(_json){
                            notify.show('审核操作成功');
                            _setStatusName(_data.list,4,_rejectData.reason);
                            this.$update('allChecked',false);
                            this.$emit('updatelist');
                        },
                        onerror:function(_error){
                            notify.showError('审核操作失败');
                        }
                    });
                    _win.close();
                }._$bind(this));
            }else{
                //this._sendReq(_url,_data);
                this.$request(_url,{
                    method:'POST',
                    data:_data,
                    onload:function(_json){
                        notify.show('审核操作成功');
                        _setStatusName(_data.list,3);
                        this.$update('allChecked',false);
                        this.$emit('updatelist');
                    },
                    onerror:function(_error){
                        notify.showError('审核操作失败');
                    }
                });
            }
        }
    });
});