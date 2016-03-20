/*
 * ------------------------------------------
 * 档期商品资料审核列表
 * @version  1.0
 * @author   yuqijun(yuqijun@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/util',
    'text!./address.html',
    'pro/components/ListComponent',
    'pro/extend/util',
    '{pro}widget/layer/address/address.js?20150906',
    'pro/components/notify/notify',
    'pro/widget/layer/sure.window/sure.window'
],function(_ut,_html,ListComponent,_,AddressWin,notify,SureWindow,_p,_o,_f,_r){
    return ListComponent.extend({
//        url:'/profile/address/list',
    	  url:'/profile/address/list',
        api:'/user/address/',
        template:_html,
        xdrOption:function(){
        	return {method:'POST'}
        },
        name: 'wgt-address',
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
        changeItem:function(item,index){
        	AddressWin._$allocate({type:1,address:item,onok:function(data){
        		// /user/addess/update
        	  data.id = item.id;
  			  this.$request('/profile/address/update',{
  	  			  data:data,
  	  			  method:'POST',
  	  			  type:'json',
  	  			  onload:function(_result){
  	  				  if(_result.code==200){
//	  	  				  if(_result.result.isDefault){
//	  	  				  	this.resetDefault();
//	  	  				  	//this.data.list.unshift(_result.result)
//	  	  				  	this.data.list[index] = _result.result;
//	  	  				  	var item = this.data.list.splice(index,1);
//	  	  				  	this.data.list.unshift(item[0]);
//	  	  				  } else{
//	  	  				  	this.data.list[index] = _result.result;
//	  	  				  }
	  	  				  
  	  					  if(_result.result.isDefault){
		  				  	this.resetDefault();
		  				  }
  	  					  this.data.list[index] = _result.result;
		  				  this.$emit('updatelist');
  	  				  } else{
  	  					//notify.show('地址更新失败');
  	  					  $.message.alert('地址更新失败', "fail");
  	  				  }
  	  			  },
  	  			  onerror:_f
  	  		  })
  		  }._$bind(this)})._$show();
        },
        removeItem:function(_item,index){
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
        					  //notify.show('地址删除失败');
        					  $.message.alert('地址删除失败', "fail");
        				  }
        			  },
      	  			  onerror:_f
        		  })
        	}._$bind(this),clazz:'w-win w-win-1 w-win-1-3'})._$show();
        },
        _sendReq:function(_url,schedule){
            
        },
        setDefault:function(_item,_index){
        	// /user/addess/setdefault
	  		  this.$request('/profile/address/setdefault',{
	  			  query:_ut._$object2query({id:_item.id}),
	  			  norest:true,
	  			  type:'json',
	  			  onload:function(_result){
	  				  if(_result.code==200){
	  					  this.resetDefault();
	  					  this.data.list[_index].isDefault = true;
//	  					  var item = this.data.list.splice(_index,1);
//	  				  	  this.data.list.unshift(item[0]);
	  					  this.$emit('updatelist');
	  				  }
	  			  }._$bind(this),
		  		  onerror:_f
	  		  })
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
		  				  this.$emit('updatelist');
	  				  } else{
	  					  //notify.show('地址添加失败')
	  					  $.message.alert('地址添加失败', "fail");
	  				  }
	  			  },
  	  			  onerror:function(){
  	  				//notify.show('服务器错误')
  	  				$.message.alert('服务器错误', "fail");
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

