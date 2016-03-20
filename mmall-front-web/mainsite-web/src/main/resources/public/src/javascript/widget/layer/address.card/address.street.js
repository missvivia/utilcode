/*
 * --------------------------------------------
 * xx卡片控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(['base/klass'
        ,'base/util'
        ,'base/event'
        , 'base/element'
        , 'util/event'
        , 'ui/layer/card.wrapper'
        ,'util/template/tpl'
        ,'text!./address.html'
        ,'./address.city.js'
        ,'pro/extend/request'
        , 'util/template/jst'], 
        function(k,ut, _v, _e, t,i,e1,html,AddressCity,request,e2,p, o, f, r) {
	var pro, sup,
	_seed_ui = e1._$parseUITemplate(html),
    _seed_tab = _seed_ui['seedTab'],
    _seed_data = _seed_ui['seedData'],
    _seed_tab1 = _seed_ui['seedTab1'],
    seed_html = e1._$addNodeTemplate(e1._$getTextTemplate(_seed_tab)),
	_provinceListCache;
	
	/**
	 * 所有分类卡片
	 *
	 * @class   {nm.l._$$AddressStreetCard}
	 * @extends {nm.l._$$CardWrapper}
	 *
	 *
	 */
	p._$$AddressStreetCard = k._$klass();
	pro = p._$$AddressStreetCard._$extend(AddressCity);
	sup = p._$$AddressStreetCard._$supro;
	
	
	
	/**
	 * 控件重置
	 * @protected
	 * @method {__reset}
	 * @param  {Object} options 可选配置参数
	 * 							data {
	 * 								province:province,provinceId:,
	 * 								city:city,cityId:cityId,
	 * 								section:section,sectionId:sectionId,
	 * 								street:street,streetId:streetId
	 */
	pro.__reset = function(options) {
		this.__data = options.data||{};
		this.__super(options);
	};
	
	/**
	 * 初使化UI
	 */
	pro.__initXGui = function() {
		//this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
		this.__seed_html = seed_html;
		//这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
	};
	
	pro.__resetTab = function(index){
		var children = _e._$getChildren(this.__tab);
		var preTab= _e._$getByClassName(this.__tab,'crt');
		if(preTab&&preTab.length){
			_e._$delClassName(preTab[0],'crt');
		}
		_e._$addClassName(children[index],'crt');
		e2._$render(this.__box,_seed_data,{type:4})
		if(index==0){
			this.__cbRenderProvince();
		} else if(index==1){
			// /src/javascript/widget/layer/address.card/mock/city.json
			// /src/javascript/widget/layer/address.card/mock/city.json
			_e._$delClassName(children[1],'dn');
			request('/location/city',{
				data:ut._$object2query({code:this.__data.provinceId}),
				onload:this.__cbGetCity._$bind(this),
				onerror:this.__cbGetCity._$bind(this),
				type:'json'
			})
		} else if(index==2){
			//  /src/javascript/widget/layer/address.card/mock/section.json
			request('/location/district',{
				data:ut._$object2query({code:this.__data.cityId}),
				onload:this.__cbGetSection._$bind(this),
				onerror:this.__cbGetSection._$bind(this),
				type:'json'
			})
		} else if(index==3){
			// /src/javascript/widget/layer/address.card/mock/street.json
			request('/location/street',{
				data:ut._$object2query({code:this.__data.sectionId}),
				onload:this.__cbGetStreet._$bind(this),
				onerror:this.__cbGetStreet._$bind(this),
				type:'json'
			})
		} 
		
	};
	
	pro.__onBodyClick = function(event) {
		var elm = _v._$getElement(event);
		_v._$stop(event);
		if(_e._$hasClassName(elm,'j-pro')){
			this.__provinceId = parseInt(_e._$dataset(elm,'id'));
			this.__province = elm.innerText;
			
			this.__data.province = this.__province
			this.__data.provinceId = this.__provinceId
			delete this.__data.city;
			delete this.__data.cityId;
			delete this.__data.section;
			delete this.__data.sectionId;
			delete this.__data.street;
			delete this.__data.streetId;
			

			this.__currentTab = 1;
			var _isMunicipality = parseInt(_e._$dataset(elm,'municipality'));
			this.__resetTab(1);
			this._$dispatchEvent('onchange',this.__data);
		} else if(_e._$hasClassName(elm,'j-city')){
			this.__cityId = parseInt(_e._$dataset(elm,'id'));
			this.__city = elm.innerText;
			this.__data.city ={name:this.__city,cityId:this.__cityId};
			this.__data.city = this.__city;
			this.__data.cityId = this.__cityId;
			delete this.__data.section;
			delete this.__data.sectionId;
			delete this.__data.street;
			delete this.__data.streetId;
			this.__currentTab = 2;
			this.__resetTab(2);
			this._$dispatchEvent('onchange',this.__data);
		} else if(_e._$hasClassName(elm,'j-section')){
			this.__sectionId = parseInt(_e._$dataset(elm,'id'));
			this.__section = elm.innerText;
			this.__data.section = this.__section;
			this.__data.sectionId = this.__sectionId;
			delete this.__data.street;
			delete this.__data.streetId;
			
			this.__currentTab = 3;
			this.__resetTab(3);
			this._$dispatchEvent('onchange',this.__data);
		} else if(_e._$hasClassName(elm,'j-street')){
			var id = parseInt(_e._$dataset(elm,'id'));
			if(id){
				this.__streetId = parseInt(_e._$dataset(elm,'id'));
				this.__street = elm.innerText;
				this.__data.street = this.__street;
				this.__data.streetId = this.__streetId;
			}
			this.__currentTab = 4;
			this._$dispatchEvent('onchange',this.__data,true);
			this._$hide();
		} else if(_e._$hasClassName(elm,'j-tab')){
			 var index= _e._$dataset(elm,'index');
			 index = parseInt(index);
			 switch(index){
			 case 1:
				 if(this.__data.province){
					 this.__currentTab = 1;
					 this.__resetTab(1);
				 }
				 break;
			 case 2:
				 if(this.__data.city){
					 this.__currentTab = 2;
					 this.__resetTab(2);
				 } 
				 break;
			 case 3:
				 if(this.__data.section){
					 this.__currentTab = 3;
					 this.__resetTab(3);
				 }
				 break; 
			 default:
				 if(index<=this.__currentTab){
					 this.__resetTab(index);
				 }
			 }
			 
		}
		
	}
	pro.__checkValidCity = function(_list,_key){
		var valid = true;
		for(var i=0,l=_list.length;i<l;i++){
			if(_list[i].id<0){
				valid = false;
				this.__data[_key+'Id'] = _list[i].id;
				this.__data[_key] = _list[i].name;
			}
		}
		return valid;
	}
	pro.__cbGetCity = function(result){
		if(result.code==200){
			if(this.__checkValidCity(result.result.list,'city')){
				e2._$render(this.__box,_seed_data,{type:1,cityList:result.result.list,data:this.__data})
			} else{
				this.__resetTab(2);
			}
		}
	}
	pro.__cbGetSection = function(result){
		if(result.code==200){
			if(result.result.list.length==0){
				this._$dispatchEvent('onchange',this.__data,true);
				this._$hide();
			} else{
				if(this.__checkValidCity(result.result.list,'section')){
					e2._$render(this.__box,_seed_data,{type:2,sectionList:result.result.list,data:this.__data})
				} else{
					this.__resetTab(3);
				}
			}
		}
	}
	pro.__cbGetStreet = function(result){
		if(result.code==200){
			e2._$render(this.__box,_seed_data,{type:3,streetList:result.result.list,data:this.__data})
		}
	}
	
	return p._$$AddressStreetCard;
})
