/*
 * ------------------------------------------
 * 审核列表基类
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'pro/components/ListComponent',
    'pro/components/notify/notify',
    './reject/reject.js',
    './reject/reject.simple.js'
],function(_u,ListComponent,notify,RejectWin,SimpleRejectWin,_p,_o,_f,_r){
    return ListComponent.extend({
        _sendReq:function(_url,_data){
            this.$request(_url,{
                method:'POST',
                data:_data,
                onload:function(_json){
                    notify.show('审核操作成功');
                    this.$update('allChecked',false);
                    this.$emit('updatelist');
                },
                onerror:function(_error){
                    notify.showError('审核操作失败');
                }
            });
        },
        _getList:function(filter){
            return this.data.list.filter(function(_item){
                return !!_item[filter];
            });
        },
        _doAudit:function(_ids,_passed){
            var _name = !_passed?'reject':'pass',
                _url = this.api+_name,
                _data = _ids;
            if (!_passed){
            	if(this.rejectType==1){
            		var _win = new RejectWin();
            	} else{
            		var _win = new SimpleRejectWin();
            	}
                _win.$on('reject',function(_event){
                	var _rejectData ={ids:_ids};
                	if(this.rejectType==1){
                		_rejectData.reason = _event.reason;
                		_rejectData.descp = _event.descp;
                	} else{
                		_rejectData.reason = _event.reason;
                	}
                    this._sendReq(_url,_rejectData);
                    _win.close();
                }._$bind(this));
            }else{
                this._sendReq(_url,_data);
            }
        },
        computed:{
            allChecked:{
                get: function(_data){
                    return _data.list.length===(this._getList('checked')||_r).length;
                },
                set: function(_sign,_data){
                    _u._$forEach(
                        _data.list,function(_it){
                            _it.checked = _sign;
                        }
                    );
                }
            }
        },
        shouldUpdateList:function(){
            return !!this.data.condition;
        },
        getExtraParam:function(){
            return this.data.condition;
        },
        refresh:function(_data){
            if (!!_data.url){
                this.url = _data.url;
                delete _data.url;
            }
            console.log(this.data);
            if(this.data.lastId){
            	this.data.lastId = null;
            }
        	this.data.current = 1;
            this.data.condition = _data;
            this.$emit('updatelist');
        },
        audit:function(_id,_passed){
            this._doAudit([_id],_passed);
        },
        batchAudit:function(_passed){
            var _list = this._getList('checked')||_r;
            if (!_list||!_list.length){
                notify.showError('请先选择要操作的项');
                return;
            }
            var _arr = [];
            _u._$forEach(
                _list,function(_it){
                    _arr.push(_it.id);
                }
            );
            this._doAudit(_arr,_passed);
        }
    });
});