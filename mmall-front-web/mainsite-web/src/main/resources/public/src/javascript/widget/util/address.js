/**
 * 地真模块联动
 * author yuqijun(yuqijun@corp.netease.com)
 */
define(
[ 'base/klass', 
  'base/util', 
  'base/event', 
  'base/element', 
  'util/event',
  'pro/widget/layer/address.card/address.street'],
function(k, ut, v, e, t, AddressCard,p, o, f, r) {
	var pro,
		_zhiXiaCities = ['北京市','上海市','天津市','重庆市'];

	p._$$Module = k._$klass();
	pro = p._$$Module._$extend(t._$$EventTarget);

	pro.__reset = function(options) {
		this.__super(options);
		this.__addressIpt = options.address;
		this.__data = options.data;
		this.__data&&this.__onChange(this.__data,true);
		var arrow = e._$getByClassName(this.__addressIpt.parentNode,'arrow'),arrowNode;
		if(arrow.length){
			arrowNode = arrow[0];
		}
		this.__doInitDomEvent([[this.__addressIpt,'click',this.__onAddressClick._$bind(this)],
		                       [arrowNode,'click',this.__onAddressClick._$bind(this)]])
	};
	pro.__onAddressClick = function(event){
		v._$stop(event);
		if(this.__card){
			this.__card._$recycle();
		}
		this.__card = AddressCard._$allocate({parent:this.__addressIpt.parentNode,
								data:this.__data,onchange:this.__onChange._$bind(this)})
		
	};
	pro.__onChange = function(_data,_isEnd){
		var value = '';
		if(_data.province){
			value += _data.province
		}
		if(_data.city&&ut._$indexOf(_zhiXiaCities,_data.province)==-1){
			value += ' / ' +_data.city
		}
		if(_data.section&&_data.sectionId>0){
			value += ' / ' +_data.section
		}
		if(_data.street){
			value += ' / ' +_data.street
		}
		this.__data = _data;
		this.__addressIpt.value = value;
		this._$dispatchEvent('onchange',_data,_isEnd);
	};
	/**
	 * 销毁控件
	 */
	pro.__destory = function() {
		this.__super();
	};

	return p._$$Module;
});