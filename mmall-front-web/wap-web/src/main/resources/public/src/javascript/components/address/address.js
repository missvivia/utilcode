/*
 * ------------------------------------------
 * 用户地址列表列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'base/event',
    'text!./address.html',
    'pro/components/ListComponent',
    'pro/extend/util',
    'pro/widget/layer/sure.window/sure.window',
    'pro/components/notify/notify'
],function(_ut,_v,_html,ListComponent,_,SureWindow,notify,_p,_o,_f,_r){
	/**
	 * 传入 id表示当前选中项的地址id,如 new AddressList({data:{id:1004005}}) ,
	 * 选中一项，监听onchange事件
	 * $on('change',function(item){
	 * 
	 * })
	 */
    return ListComponent.extend({
//        url:'/profile/address/list',
    	  url:'/profile/address/list',
        api:'/user/address/',
        template:_html,
        name: 'wgt-address',
        xdrOption:function(){
        	return {method:'POST'}
        },
        getExtraParam:function(){
            return this.data.condition;
        },
        onselect:function(_item){
        	this.$emit('change',_item)
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
        getDefault:function(filter){
        	return this.data.list.filter(function(_item){
        			return _item[filter]
        		})
        },
        resetDefault:function(){
        	var list = this.getDefault('isDefault');
        	if(list.length!=0)
				list[0].isDefault = false;
        },
        removeItem:function(_event,_item,index){
        	_v._$stop(_event);
        	SureWindow._$allocate({onok:function(){
        		// /user/addess/delete
        		this.$request('/profile/address/delete',{
        			  query:_ut._$object2query({id:_item.id}),
        			  norest:true,
        			  type:'json',
        			  onload:function(result){
        				  if(result.code==200){
        					  this.data.list.splice(index,1);
        				  }else{
        					  notify.show('地址删除失败');
        				  }
        			  },
      	  			  onerror:_f
        		  })
        	}._$bind(this),clazz:'w-win w-win-1 w-win-1-3'})._$show();
        },
        _sendReq:function(_url,schedule){
            
        },
        addAddress:function(_event){
        	_v._$stop(_event);
        	this.$emit('editaddress');
        },
        editAddress:function(_event,_item){
        	_v._$stop(_event);
        	this.$emit('editaddress',_item);
        },
        setDefault:function(_item){
        	// /user/addess/setdefault
  		  this.$request('/profile/address/setdefault',{
  			  query:_ut._$object2query({id:_item.id}),
  			  norest:true,
  			  type:'json',
  			  onload:function(_result){
  				  if(_result.code==200){
  					  this.resetDefault();
  					  _item.isDefault = true;
  				  }
  			  },
	  		  onerror:_f
  		  })
  	  },
  	 __getList: function(){
  	      var data = this.data;
  	      var option = {
  	        data: this.getListParam(),
  	        onload: function(json){
  	          var result = json.result,
  	            list = result.list||result||[];
  	          _.mergeList(list, data.list,data.key||'id')

  	          data.total = result.total;
  	          data.list = list;
  	          if(!data.id){
  	        	  var address = this.getDefault();
		          var selectAddress;
		          if(address.length==0&&data.list.length!=0){
		        	selectAddress = data.list[0];
		          }
		          this.$emit('change',selectAddress);
  	          }
  	          
  	        }._$bind(this),
  	        onerror: function(json){
  	          // @TODO: remove
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
  	   
	  addItem:function(){
		  AddressWin._$allocate({type:0,onok:function(data){
			  // /user/addess/add
			  this.$request('/profile/address/add',{
	  			  data:data,
	  			  method:'POST',
	  			  onload:function(_result){
	  				  if(_result.code==200){
		  				  if(_result.result.isDefault){
		  					this.resetDefault();
		  				  }
		  				  this.data.list.push(_result.result)
	  				  } else{
	  					  notify.show('地址添加失败')
	  				  }
	  			  },
  	  			  onerror:function(){
  	  				notify.show('服务器错误')
  	  			  }
	  		  })
		  }._$bind(this)})._$show();
	  },
	  isZhixiashi:function(_province){
		  var _zhiXiaCities = ['北京市','上海市','天津市','重庆市'];
		  var _index = _ut._$indexOf(_zhiXiaCities,_province);
		  return _index!=-1;
		  
	  }
    });
});