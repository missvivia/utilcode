/*
 * --------------------------------------------
 * UTIL控件实现
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
		[ 'base/klass', 
		  'base/util', 
		  'base/event', 
		  'base/element', 
		  'util/event',
		  'pro/extend/request',
		  'pro/extend/config'
		  ],
		function(k, ut, v, e, t,request,config,p, o, f, r) {
			var pro, sup;

			/**
			 * 全局状态控件
			 * @class   {p._$$UtilCtl}
			 * @extends {nej.ui._$$Abstract}
			 */
			p._$$UtilCtl = k._$klass();
			pro = p._$$UtilCtl._$extend(t._$$EventTarget);
			sup = p._$$UtilCtl._$supro;

			/**
			 * 重置控件
			 * @param  {[type]} options [description]
			 *
			 */
			pro.__reset = function(options) {
				this.__super(options);
				this.__province = options.province;
				this.__city = options.city;
				this.__section = options.section;
				this.__street = options.street;
				this.__doInitDomEvent([[this.__province,'change',this.__onProvinceChange._$bind(this)],
				                       [this.__city,'change',this.__onCityChange._$bind(this)],
				                       [this.__section,'change',this.__onSectionChange._$bind(this)]])
				this.__initProvince();
				                       
			};
			pro.__initProvince = function(){
				request(config.DOMAIN_URL+'/address/provinceList',{
					onload:this.__cbGetProvince._$bind(this),
					onerror:this.__cbGetProvince._$bind(this),
					type:'json'
				})
				
			};
			pro.__cbGetProvince = function(_json){
				if(_json.code==200){
					this.__initSelect(this.__province,_json.provinceList,'locationName','code');
					this.__onProvinceChange();
				}
				
			};
			pro.__onProvinceChange = function(){
				var provinceId = this.__province.value;
				if(provinceId==-1){return;}
				request('/location/city',{
					data:ut._$object2query({code:provinceId}),
					onload:this.__cbGetCity._$bind(this),
					onerror:this.__cbGetCity._$bind(this),
					type:'json'
				})
			};
			pro.__cbGetCity = function(_json){
				if(_json.code==200){
					this.__initSelect(this.__city,_json.result.list);
					this.__onCityChange();
				}
			};
			pro.__initSelect = function(_select,_list,_name,_id){
				_select.options.length=0;
				var defaultCode = e._$dataset(_select,'value'),
					selectIndex =-1
					_name = _name||'name',
					_id = _id||'id';
				for(var i=0,l=_list.length;i<l;i++){
					var option = new Option(_list[i][_name],_list[i][_id]);
					if(defaultCode==_list[i][_name]){
						selectIndex = i;
					}
					_select.options.add(option);
				}
				if(selectIndex!=-1){
					_select.selectedIndex = selectIndex;
				}
			};
			pro.__onCityChange = function(){
				var cityId = this.__city.value;
				if(cityId==-1){return;}
				request('/location/district',{
					data:ut._$object2query({code:cityId}),
					onload:this.__cbGetSection._$bind(this),
					onerror:this.__cbGetSection._$bind(this),
					type:'json'
				})
			};
			pro.__cbGetSection = function(_json){
				if(_json.code==200){
					this.__initSelect(this.__section,_json.result.list);
					this.__onSectionChange();
				}
			};
			pro.__onSectionChange = function(){
				var sectionId = this.__section.value;
				if(sectionId==-1){return;}
				request('/location/street',{
					data:ut._$object2query({code:sectionId}),
					onload:this.__cbGetStreet._$bind(this),
					onerror:this.__cbGetStreet._$bind(this),
					type:'json'
				})
			};
			pro.__cbGetStreet = function(_json){
				if(_json.code==200){
					_json.result.list.push({name:'我不知道',id:0})
					this.__initSelect(this.__street,_json.result.list);
				}
			};
			pro._$data = function(_json){
				var _data ={};
				_data.province = this.__province.options[this.__province.selectedIndex].text;
				_data.provinceId = this.__province.options[this.__province.selectedIndex].value;
				_data.city = this.__city.options[this.__city.selectedIndex].text;
				_data.cityId = this.__city.options[this.__city.selectedIndex].value;
				_data.section =  this.__section.options[this.__section.selectedIndex].text;
				_data.sectionId = this.__section.options[this.__section.selectedIndex].value;
				var streetId = this.__street.options[this.__street.selectedIndex].value;
				if(streetId!=-1){
					_data.street = this.__street.options[this.__street.selectedIndex].text;
					_data.streetId = streetId;
				}
				return _data;
			};
			/**
			 * 控件销毁
			 *
			 */
			pro.__destroy = function() {
				this.__super();
			};

			return p._$$UtilCtl;
		})