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
        ,'pro/extend/request'
        ,'pro/extend/config'
        , 'util/template/jst'], 
        function(k,ut, _v, _e, t,i,e1,html,request,config,e2,p, o, f, r) {
	var pro, sup,
	_seed_ui = e1._$parseUITemplate(html),
    _seed_tab = _seed_ui['seedTab'],
    _seed_data = _seed_ui['seedData'],
    _seed_tab1 = _seed_ui['seedTab1'],
    seed_html = e1._$addNodeTemplate(e1._$getTextTemplate(_seed_tab1)),
	_provinceListCache;
	
	/**
	 * 所有分类卡片
	 *
	 * @class   {nm.l._$$AddressCityCard}
	 * @extends {nm.l._$$CardWrapper}
	 *
	 *
	 */
	p._$$AddressCityCard = k._$klass();
	pro = p._$$AddressCityCard._$extend(i._$$CardWrapper);
	sup = p._$$AddressCityCard._$supro;
	
	
	
	/**
	 * 控件重置
	 * @protected
	 * @method {__reset}
	 * @param  {Object} options 可选配置参数
	 * 							data {province:province,provinceId:provinceId,
	 * 								  city:city,cityId:cityId}
	 */
	pro.__reset = function(options) {
		this.__super(options);
		this.__data = options.data||{};
		this.__getProvince();
		if(!this.__data.city&&this.__data.section){
			var children = _e._$getChildren(this.__tab);
			_e._$addClassName(children[1],'dn');
		}
	};
	
	pro.__getProvince = function(){
		if(!_provinceListCache){
			//  /src/javascript/widget/layer/address.card/mock/province.json
			request(config.DOMAIN_URL+'/shop/province',{
				onload:this.__cbGetProvince._$bind(this),
				onerror:this.__cbGetProvince._$bind(this),
				type:'json'
			})
		} else{
			this.__cbRenderProvince();
		}
	};
	pro.__cbGetProvince = function(result){
		if(result.code==200){
			_provinceListCache = result.result.list
			this.__cbRenderProvince();
		}
	};
	
	pro.__cbRenderProvince = function(){
		e2._$render(this.__box,_seed_data,{type:0,provinceList:_provinceListCache,data:this.__data})
	};
	/**
	 * 初使化UI
	 */
	pro.__initXGui = function() {
		//this.__seed_html如果不设id上去，ui的父类会做一次this.__initNodeTemplate()操作，在样例中把this.__seed_html不设值了
		this.__seed_html = seed_html;
		//这里的ntp模板可以放在html的模板里，模板一定要parseTemplate才能取到这个id
	};
	
	/**
	 * 销毁控件 
	 */
	pro.__destroy = function() {
		this.__super();
		
	};
	/**
	 * 初使化节点 
	 */
	pro.__initNode = function() {
		this.__super();
		var list = _e._$getByClassName(this.__body,'j-flag');
		this.__tab = list[0];
		this.__box = list[1];
		_v._$addEvent(this.__body, 'click', this.__onBodyClick._$bind(this));
	};
	pro.__resetTab = function(index,_isMunicipality){
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
			if(_isMunicipality){
				_e._$replaceClassName(preTab[1],'crt','f-dn');
				_e._$addClassName(preTab[2],'crt');
				request('/location/district',{
					data:ut._$object2query({code:this.__data.cityId}),
					onload:this.__cbGetSection._$bind(this),
					onerror:this.__cbGetSection._$bind(this),
					type:'json'
					})
			} else{
				_e._$delClassName(preTab[1],'f-dn');
				request('/location/city',{
					data:ut._$object2query({code:this.__data.provinceId}),
					onload:this.__cbGetCity._$bind(this),
					onerror:this.__cbGetCity._$bind(this),
					type:'json'
					})
			} 
		}
	};
	pro.__checkValidCity = function(_list,_key){
		var valid = true;
		for(var i=0,l=_list.length;i<l;i++){
			if(_list[i].id<0){
				valid = false;
				this.__data[_key+'Id'] = _list[i].id;
				this.__data[_key+'Name'] = _list[i].name;
			}
		}
		return valid;
	}
	pro.__cbGetCity = function(result){
		if(result.code==200){
			if(this.__checkValidCity(result.result.list,'city')){
				e2._$render(this.__box,_seed_data,{type:1,cityList:result.result.list,data:this.__data})
			} else{
				this.__resetTab(1,true);
			}
		}
	};
	pro.__cbGetSection = function(result){
		if(result.code==200){
			e2._$render(this.__box,_seed_data,{type:2,sectionList:result.result.list,data:this.__data})
		}
	}
	pro.__onBodyClick = function(event) {
		var elm = _v._$getElement(event);
		_v._$stop(event);
		if(_e._$hasClassName(elm,'j-pro')){
			this.__provinceId = parseInt(_e._$dataset(elm,'id'));
			var _isMunicipality = parseInt(_e._$dataset(elm,'municipality'));
			
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
			this.__resetTab(1);
			this._$dispatchEvent('onchange',this.__data);
		} else if(_e._$hasClassName(elm,'j-city')){
			this.__cityId = parseInt(_e._$dataset(elm,'id'));
			this.__city = elm.innerText;
			this.__data.city = this.__city;
			this.__data.cityId = this.__cityId;
			delete this.__data.section;
			delete this.__data.sectionId;
			delete this.__data.street;
			delete this.__data.streetId;
			this.__currentTab = 2;
			this._$dispatchEvent('onchange',this.__data,true);
			this._$hide();
		} else if(_e._$hasClassName(elm,'j-section')){
			this.__sectionId = parseInt(_e._$dataset(elm,'id'));
			this.__section = elm.innerText;
			this.__data.section = this.__section;
			this.__data.sectionId = this.__sectionId;
			delete this.__data.street;
			delete this.__data.streetId;
			
			this.__currentTab = 3;
			this.__resetTab(3);
			this._$dispatchEvent('onchange',this.__data,true);
			this._$hide();
		} else if(_e._$hasClassName(elm,'j-tab')){
			 var index= _e._$dataset(elm,'index');
			 index = parseInt(index);
			 if(index<=this.__currentTab){
				 this.__resetTab(index);
			 }
		}
		
	}
	
	return p._$$AddressCityCard;
})
