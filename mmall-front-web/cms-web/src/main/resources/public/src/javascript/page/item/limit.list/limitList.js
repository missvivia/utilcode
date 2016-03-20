/**
 * 限购信息
 * author liuqing
 */

define([
  "{pro}extend/util.js",
  '{lib}base/event.js',
  '{lib}base/element.js',
  'util/template/jst',
  '{pro}components/ListComponent.js',
  '{pro}components/notify/notify.js',
  './orderList.js'
  ], function(_,v,e,_t, ListComponent,notify,list){

var limitList = ListComponent.extend({
    url: '/item/limit/getInfo',
    template: "#limitListTpl",
    format:function(timestamp){
    	var date = new Date(timestamp);
    	return _ut._$format(date,'yyyy-MM-dd');
    },
    data: {
    	total: 1,
        current: 1,
        limit: 10,
        list: []
    },
    config: function(data){
      this.supr(data);
      data.userName = this.data.userName;
    },
    getListParam: function(){
        var data = this.data;
        
        return _.extend({
            /*limit: data.limit,
            offset: data.limit * (data.current-1),*/
          }, this.getExtraParam(data));
    },
    getExtraParam:function(){
          var obj = {};
          var condition = this.data.condition;
         
	  	  for(var i in condition){
	      	  var item = condition[i];
	      	  if(item){
	      		  obj[i] = item;
	      	  }
          }
  	  
  	      return obj;
    },
    __getList:function(){
        var data = this.data;
        var option = {
            progress: true,
            data: this.getListParam(),
            onload: function(json){
              var result = json.result,
                list = [result]||{};
              _.mergeList(list, data.list,data.key||'id')
             
              data.total = 1;
              data.list = list;
              data.lastId = result.lastId;
            },
            // test
            onerror: function(json){
            	notify.show({
					'type':'error',
					'message':json.message
				});
            }
        };
        if(this.xdrOption){
            var xdrOpt = this.xdrOption();
            if(xdrOpt.norest){
              option.data = _ut._$object2query(this.getListParam());
              option.norest = true;
            }

            option.method = xdrOpt.method||'GET';

          }
        this.$request(this.url,option)
    },
    updateCache: function(event,item){
    	var elem = event.target,
    	    skuLimitNum = item.skuLimitConfig.skuLimitNum,
    	    buyNum = item.skuLimitRecord.buyNum,
    	    userId = this.data.condition.userId,
    	    skuId = this.data.condition.skuId,
    	    _data = {
    			userId: userId,
    			skuId: skuId
    	    };
    	elem.value = "正在更新...";
    	e._$addClassName(elem,'disabled');
    	e._$addClassName('modifyNumBtn','disabled');
    	e._$addClassName('searchOrder','disabled');
    	this.$request("/item/limit/syncCache",{
            data:_data,
            method:'POST',
            onload:function(_result){
                if (_result.code == 200){
                	notify.show(_result.message);
                	elem.value = "更新";
                	e._$delClassName(elem,"disabled");
                	e._$delClassName('modifyNumBtn',"disabled");
                	e._$delClassName('searchOrder','disabled');
                	e._$get('cacheNum').innerHTML = skuLimitNum - _result.result;
                }else{
                	notify.show(_result.message);
                	elem.value = "更新";
                	e._$delClassName(elem,"disabled");
                	e._$delClassName('modifyNumBtn',"disabled");
                	e._$delClassName('searchOrder','disabled');
                }
            }._$bind(this),
            onerror:function(_result){
            	notify.show(_result.message);
            	elem.value = "更新";
            	e._$delClassName(elem,"disabled");
            	e._$delClassName('modifyNumBtn',"disabled");
                e._$delClassName('searchOrder','disabled');
            }
        });
    },
    hidePrimary: function(){
    	e._$addClassName('setLimitPrimary','hide');
    	e._$delClassName('setLimitModify','hide');
    },
    modifyNum: function(event){
    	var elem = event.target,
		    userId = this.data.condition.userId,
		    skuId = this.data.condition.skuId,
		    leftNum = parseInt(e._$get('leftNum').value),
		    reg = /^\d+$/,
		    data = {};
    	if(!reg.test(leftNum)){
    		notify.show('请输入大于等于0的可购数量');
    		return;
    	}
    	data = {
    		skuId:skuId,
    		userId:userId,
    		buyNum:leftNum
    	};
    	this.$request("/item/limit/update",{
            data:data,
            method:'POST',
            onload:function(_result){
                if (_result.code == 200){
                	notify.show(_result.message);
                	this.$emit('updatelist');
                }else{
                	notify.show(_result.message);
                }
            }._$bind(this),
            onerror:function(_result){
            	notify.show(_result.message);
            }
        });
    },
    cancelModify: function(){
    	e._$delClassName('setLimitPrimary','hide');
    	e._$addClassName('setLimitModify','hide');
    },
    searchOrder: function(item){
    	var userId = this.data.condition.userId,
	        skuId = this.data.condition.skuId,
    	    data = {
    			userId:userId,
    			skuId:skuId
    	    };
    	if(!this.__orderList){
            this.__orderList = new list({data: {condition:data}}).$inject('#orderList');
        }else{
        	this.__orderList.refresh(data);
        }
    }
  });
  return limitList;

})
