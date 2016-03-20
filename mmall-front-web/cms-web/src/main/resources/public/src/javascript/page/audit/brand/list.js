/*
 * ------------------------------------------
 * 档期BANNER审核列表
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'text!./list.html',
    '../widget/list.js',
    'pro/extend/config',
    '../widget/reject/reject.simple.js',
    'pro/components/notify/notify'
],function(_html,AuditList,config,SimpleRejectWin,notify,_p,_o,_f,_r){
    return AuditList.extend({
        url:'/audit/brand/search',
        api:'/rest/audit/brand/',
        data:{config:config},
        template:_html,
        preview:function(brand){
        	var pform = this.$refs.pform;
            if(pform){
              this.$request("/item/brand/preview",{
                sync: true,
                data: {id: brand.id},
                method:'GET',
                onload: function(json){
                    pform.data.value = JSON.stringify(json);
                    pform.submit();
                },
                onerror: function(){
                  notify.notify({
                    "type": "error",
                    "message": "尝试预览失败，请稍后再试"
                  })
                }
              } )
            }else{
              notify.notify({
                "type": "error",
                "message": "尝试预览失败，请稍后再试"
              })
            }
        },
        areaNames:function(list){
        	var sites = '';
        	for(var i=0,l=list.length;i<l;i++){
        		sites += list[i]+' ';
        	}
        	return sites;
        },
        _doAudit:function(_item,_passed){
            var _name = !_passed?'reject':'pass',
                _url = this.api+_name;
            
            if (!_passed){
        		var _win = new SimpleRejectWin();
                _win.$on('reject',function(_event){
                	var _rejectData ={ids:[_item[0].id]};
                	_rejectData.reason = _event.reason;
                    //this._sendReq(_url,_rejectData);
                	this.$request(_url,{
                        method:'POST',
                        data:_rejectData,
                        onload:function(_json){
                            notify.show('审核操作成功');
                            _item[0].status.type = 4;
                            _item[0].status.desc = '审核未通过';//_event.reason;
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
                    data:[_item[0].id],
                    onload:function(_json){
                        notify.show('审核操作成功');
                        _item[0].status.type = 2;
                        _item[0].status.desc = '核审通过-暂未使用';
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