/**
 * 档期详情列表
 * author hzxuyingshan(hzxu_yingshan@corp.netease.com)
 */

define([
  'pro/extend/config',
  'pro/extend/util',
  'base/util',
  'pro/widget/util/preview',
  'text!./addtable1.html',
  'base/element',
  'pro/components/ListComponent',
  'pro/widget/layer/po.add',
  'pro/extend/request',
  'pro/components/notify/notify'
  ], function(config, u, _u, preview, tpl,e,ListComponent,AddWin,Request,notify){
  var ActList = ListComponent.extend({
    url: '/rest/schedule/search',
    template: tpl,
    data: {
      limit: 50
    },
    xdrOption:function(){
      return {method:'POST'}
    },
    config: function(data){
        u.extend(data, {
          skuTotal: 0,
          supplyMode:1
        });
        this.supr(data);
      },
    $updateStatus:function(_status){
    	this.data.status = _status;
    	this.$update();
    },
    preview: function(tpl){
      preview.preview(tpl.id,{
        data: {scheduleId: this.data.condition.poId}
      })
    },
    __getList: function(){
        var data = this.data;
        var option = {
          progress: true,
          data: this.getListParam(),
          onload: function(json){
            var result = json.result,
              list = result.list;
            u.mergeList(list, data.list,data.key||'id')

            data.total = result.total;
            data.supplyMode = result.scheduleType;
            data.skuTotal = result.skuTotal;
            data.list = list;
          },
          onerror: function(json){
            // @TODO: remove
          }
        };
        //继承类提供xdrOption方法，用来表明请求类型
        /**
         * function(){
         *  return {method:'POST',norest:true}
         & }
        **/
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
    remove: function(index){
      this.data.list.splice(index,1)
      this.$emit('updatelist');
    },
    add: function(item,act){
      var num = parseInt(act.addNum);
      var supplyAddNum = parseInt(act.supplyAddNum);
      if(num>0||supplyAddNum>0){
        var listItem = {};
        
        var data = [],
            dataItem = {},
            skuList = [],
            skuItem={};

        skuItem['skuId'] = act.skuId;
        skuItem['addNum'] = parseInt(act.addNum)||0;//parseInt(e._$get(act.skuId).value);
        skuItem['supplyAddNum'] = parseInt(act.supplyAddNum)||0;//parseInt(e._$get(act.skuId).value);
        skuList.push(skuItem);
        dataItem['productId'] = item.id; 
        dataItem['skuList'] = skuList;
        data.push(dataItem);

        listItem['poId'] =  this.data.poId;
        listItem['data'] = data;
        this.$request('/rest/schedule/addProduct',{
          data:listItem,
          method:'POST',
          type:'JSON',
          onload:this.addSeccess._$bind(this,act,skuItem['addNum'],skuItem['supplyAddNum']),
          onerror:function(e){
            console.log(e);
          }
        })
      }else{
        notify.show({
          "type":"error",
          "message":"请输入有效数量"
        })
      }
    },
    addAll: function(_list,_poId){
    	var listItem ,_prdList =[],_count =0;
        _u._$forEach(
        		_list,function(_it){
            	var _skuData=[],_skuList=[];
                for(var i=0,l=_it.skuList.length;i<l;i++){
                	if(_it.skuList[i].addStatus==0){
                		if((_it.skuList[i].addNum&&_it.skuList[i].addNum!='')||_it.skuList[i].supplyAddNum&&_it.skuList[i].supplyAddNum!=''){
                        _count++;
                    		_skuList.push({
                    			    	skuId:_it.skuList[i].skuId,
                    			    	addNum:parseInt(_it.skuList[i].addNum),
                    			    	supplyAddNum:parseInt(_it.skuList[i].supplyAddNum),
                    		})
                		}
                	}
                }
                if(_skuList.length){
                	_prdList.push({productId:_it.id,skuList:_skuList});
                }
            }
        );
        var data = this.data;
        listItem = {poId:_poId,data:_prdList};
        //send batch request
        this.$request('/rest/schedule/addProduct',{
            data:listItem,
            method:'POST',
            type:'JSON',
            onload:function(_result){
            	if(_result.code==200){
            		_u._$forEach(
                    		_list,function(_it){
                            for(var i=0,l=_it.skuList.length;i<l;i++){
                            	if(_it.skuList[i].addStatus==0){
                            		if(_it.skuList[i].addNum&&_it.skuList[i].addNum!=''){
                            			_it.skuList[i].addStatus=1;
                            			_it.skuList[i].num= parseInt(_it.skuList[i].addNum);
                            			_it.skuList[i].addNum = '';
                            		}
                            		if(_it.skuList[i].supplyAddNum&&_it.skuList[i].supplyAddNum!=''){
                            			_it.skuList[i].addStatus=1;
                            			_it.skuList[i].supplyNum= parseInt(_it.skuList[i].supplyAddNum);
                            			_it.skuList[i].supplyAddNum = '';
                            		}
                            	}
                            }
                        }
                    );
            		data.skuTotal += _count;
            	}
            },
            onerror:function(e){
              console.log(e);
            }
          })
    },
    addSeccess: function(data,count,supplyCount,json){
    	if(json.code==200){
	      data['addStatus'] = 1;
	      data['num'] = count;
	      data['supplyNum'] = supplyCount;
	      data['addNum'] ='';
	      data['supplyAddNum'] = '';
	      this.data.skuTotal += 1;
    	}
    }

  });
  return ActList;

})