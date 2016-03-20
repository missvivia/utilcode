/**
 * 站点覆盖区域选择
 *
**/

define([
	'base/global',
	'base/klass',
	'base/element',
	'base/util',
	'base/event',
	'util/event',
	'util/ajax/xdr',
	'{lib}util/chain/NodeList.js',
	'{pro}extend/request.js'
	],function(NEJ,_k,_e,_u,_t,_t1,_q,$,Request,_p,_o,_f,_r){
	var _pro;
	window._provinceCache=[];
	window._cityCache={};

	_p._$$SiteSelector = _k._$klass();
	_pro = _p._$$SiteSelector._$extend(_t1._$$EventTarget);

	// 控件重置
	_pro.__reset = function(_options){
		this.__parent = _options.parentNode;
		this.__url = _options.url;
		
		
		this.__getProvince();
		$("body")._$on("click",function(e){
			_pro.__onBodyClick(e);
		});
	};

	_pro.__destroy = function(){
        this.__super();
    };
	


	_pro.__setDefaultData = function(_type){
		if(!this.__data){
			return;
		}
		
		var node,code;
		switch(_type){
			case 'province':
				node = this.__province;
				code = this.__data.provinceId;
			break;
			case 'city':
				node = this.__city;
				code = this.__data.cityId;
			break;
		}

		this.__setSelectOption(node,code);
	};

	_pro.__getProvince = function(){
		// 判断是否有缓存
		if (_provinceCache.length>0) {
			//console.log('xxxxx province cache');
			this.__renderList('province',_provinceCache);
//			this.__getCity();
		} else {
			_q._$request(this.__url,{
				type:'json',
				method:'GET',
				headers : {'Content-Type' : 'text/plain;charset=utf-8'},
//				timeout:1000,
				onload:this.__loadProvince._$bind(this),
	            onerror:this.__loadProvince._$bind(this)
			});
		}
	};
	
	_pro.__loadProvince = function(_data){
		if (_data.code==200) {
			_provinceCache = _data.result;
			this.__renderList('province',_provinceCache);

//			this.__getCity();
		}
	};

	_pro.__getCity = function(checked){
		var index = this.__province.selectedIndex;
		var provinceId = this.__province.selectedArea;
		var list = _cityCache[provinceId];
		if (list) {
			//console.log('xxxxx city cache');
			for(var i = 0; i < list.length;i++){
				if(checked != "load"){
					list[i]["isChecked"] = (checked ? 1 : 0);
				}
			}
			this.__renderList('city',list);
		}
		else {
			_q._$request(this.__url +'&areaId='+provinceId,{
				type:'json',
				method:'GET',
				timeout:1000,
				onload:this.__loadCity._$bind(this,provinceId,checked),
	            onerror:this.__loadCity._$bind(this,provinceId,checked)
			});
		}
	};

	_pro.__loadCity = function(_provinceId,checked,_data){
		if (_data.code==200) {
			var list = _data.result;
			_cityCache[_provinceId] = list;
			for(var i = 0; i < _cityCache[_provinceId].length;i++){
				if(checked != "load"){
					_cityCache[_provinceId][i]["isChecked"] = (checked ? 1 : 0);
				}
			}
			this.__renderList('city',list);

		}
	};

	_pro.__renderList = function(_type,_list)
	{
		var div = _e._$create('div','col-sm-6');
		var select = _e._$create('div','list-group');
		$(select)._$attr("id",_type);
		div.appendChild(select);
		
		for (var i =0;i<_list.length;i++) {
			var option = _e._$create("a","list-group-item");
			var span = _e._$create("span","item");
			$(span)._$text(_list[i].areaName);
			
			var checkbox = _e._$create("input","check");
			$(checkbox)._$attr("type","checkbox")._$val(_list[i].areaId);
			checkbox.className="";
			if(_list[i].isChecked == 0){
				checkbox.checked = false;
			}else if(_list[i].isChecked == 1){
				checkbox.checked = true;
			}else{
				checkbox.checked = false;
				checkbox.indeterminate = true;
			}
  			
			$(option)._$attr("areaId",_list[i].areaId)._$attr("index",i)._$insert(checkbox,'append')._$insert(span,'append');
			$(select)._$insert(option,'append');
		}
		
		switch(_type){
			case 'province':
				$(this.__parent)._$insert(div,'append');
				
				this.__province = select;
				break;
			case 'city':
				$(this.__province.parentNode)._$insert(div,'after');
				this.__city = select;
				break;
		}
		var _self = this;
		$('#province a')._$off()._$on("click",function(e){

			var checked = $(this)._$children("input")[0].checked;	
			$("#province a")._$attr("class","list-group-item");
			$(this)._$addClassName("active");
			_self.__removeSelectNode("city");
			_self.__province.selectedIndex = $(this)._$attr("index");
			var provinceId = $(this)._$attr("areaId")
			_self.__province.selectedArea = provinceId;
			
			if(e.target.type !="checkbox"){
				_self.__getCity("load");
			}else{
				_self.__getCity(checked);
			}
		});
		$("#city a")._$on("click",function(e){
			var checked = $(this)._$children("input")[0].checked;
			var index = $(this)._$attr("index");
			if(e.target.type != "checkbox"){
				$(this)._$children("input")[0].checked = !checked;
				_cityCache[_self.__province.selectedArea][index]["isChecked"] = (!checked ? 1 : 0);
			}else{
				_cityCache[_self.__province.selectedArea][index]["isChecked"] = (checked ? 1 : 0);
			}
			
			
			var count = 0;
			for(var i = 0 ; i < _cityCache[_self.__province.selectedArea].length;i++){
				if(_cityCache[_self.__province.selectedArea][i].isChecked){
					count += 1;
				}
			}
			$("#province .active")._$children("input")[0].indeterminate = false;
			if(count == 0){
				_provinceCache[_self.__province.selectedIndex]["isChecked"] = 0;
				$("#province .active")._$children("input")[0].checked = false;
			}else if(count < _cityCache[_self.__province.selectedArea].length){
				_provinceCache[_self.__province.selectedIndex]["isChecked"] = 2;
				$("#province .active")._$children("input")[0].indeterminate = true;
			}else{
				_provinceCache[_self.__province.selectedIndex]["isChecked"] = 1;
				$("#province .active")._$children("input")[0].checked = true;
			}
		});
		this.__setDefaultData(_type);
	};
	_pro.__removeSelectNode = function(_type){
		var node;
		switch(_type){
			case 'province':
				node = this.__province;
			break;
			case 'city':
				node = this.__city;
			break;
		}
		
		if(node){			 
			_e._$remove(node.parentNode,false);
		}
	};
	_pro.__onBodyClick = function(event) {
//		var elm = _t._$getElement(event);
//		_t._$stop(event);
//		if(!_e._$hasClassName(elm,'list-group')){	
//			this.__removeSelectNode("city");
//		}
	};
	_pro.__getAreaList = function(){
		var areaList = [];

		for(var j in _cityCache){
			for(var k = 0 ; k < _cityCache[j].length;k++){
				var area = {};
				area["areaId"] = _cityCache[j][k].areaId;
				area["isChecked"] = _cityCache[j][k].isChecked;
				areaList.push(area);
			}
		}
		return areaList;
	};
	return _p._$$SiteSelector;
});