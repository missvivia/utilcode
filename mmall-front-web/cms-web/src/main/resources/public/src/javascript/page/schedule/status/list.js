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
    'pro/extend/util'
],function(_ut,_html,ListComponent,_,_p,_o,_f,_r){
     var StatusList = ListComponent.extend({
        url:'/schedule/status/list',
        api:'/schedule/status/',
        template:_html,
        search:function(_status){
        	this._sendReq(this.api,{status:_status});
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
        searchSchedule:function(_status){
        	this.data.condition.flag = _status;
        	this.data.current =1;
        	var data = this.data;
        	this.$request(this.url,{
        		data: this.getListParam(),
        		method:'POST',
        		onload:function(json){
        			var result = json.result,
                    list = result.list;
                    _.mergeList(list, data.list,data.key||'id');

                    data.total = result.total;
                    data.type=1;
                    data.list = list;
                    data.lastId = result.lastId;
                    this.$update();
        		}._$bind(this)
        	});
        },
        
        online:function(po){
        	this.$request(this.api+'online',{
        		data:{id:po.id},
        		onload:function(_result){
        			if(_result.code==200){
        				po.poStatus = 3;
        				this.$update();
        			}
        		}._$bind(this)
        	});
        
        },
        offline:function(po){
        	
        	this.$request(this.api+'offline',{
        		data:{id:po.id},
        		onload:function(_result){
        			if(_result.code==200){
        				po.poStatus = 2;
        				this.$update();
        			}
        		}._$bind(this)
        	});
            
        },
        remind:function(po){
        	this.$request(this.api+'reminde',{
        		data:{id:po.id},
        		onload:function(_result){
        			//po.status = 4
        		}
        	});
            
        },
        _sendReq:function(_url,schedule){
            this.$request(_url,{
                method:'POST',
                data:{id:schedule.id},
                onload:function(_json){
                    
                },
                onerror:function(_error){
                	
                }
            });
        },
        xdrOption:function(){
        	return {method:'POST'};
        },
     // update loading
        __getList: function(){
          var data = this.data;
          var option = {
            progress: true,
            method:'POST',
            data: this.getListParam(),
            onload: function(json){
              var result = json.result,
              list = result.list;
              _.mergeList(list, data.list,data.key||'id');

              data.total = result.total;
              data.list = list;
              data.lastId = result.lastId;
              data.cntUncheckedBanner = json.result.cntUncheckedBanner;
              data.cntUncheckedPage = json.result.cntUncheckedPage;
              data.cntUncheckedPrdInfo = json.result.cntUncheckedPrdInfo;
              data.cntUncheckedPrdList = json.result.cntUncheckedPrdList;
              data.cntReadyOnline = json.result.cntReadyOnline;
              
            },
            // test
            onerror: function(json){
              // @TODO: remove
            }
          };
          this.$request(this.url,option);
        }
    });
     StatusList.filter('statusMap',function(_status){
    	 var map = {"1":"未完备",
    		 "2":"待上线",
    		 "3":"待开场",
    		 "4":"结束",
    		 "5":"售卖中",
    		 "6":"失效"}
     return map[_status];
    });
     
    return StatusList;
});