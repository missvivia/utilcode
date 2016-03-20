/*
 * --------------------------------------------
 * 店面弹出框基本页面
 * @version  1.0
 * @author   author(author@corp.netease.com)
 * --------------------------------------------
 */
define(
    [ '{lib}base/util.js'
        , '{lib}base/event.js'
        , '{lib}base/element.js'
        , '{lib}util/event.js'
        , '{lib}util/form/form.js'
        , '{pro}widget/layer/window.js'
        , 'text!./shopwindow.html'
        , 'util/chain/chainable'
        , 'util/ajax/xdr'
        , 'base/config'
        , 'util/template/jst'
        , '{lib}util/template/tpl.js'
        ,"pro/components/notify/notify"
        ,"pro/widget/layer/address.card/address.street"
    ],
    function (u, v, e, t, _f, Window, html, $, _ajax, c, _p2, l, notify,AddressCard,p, o, f, r) {
        var pro,_zhiXiaCities = ['北京市','上海市','天津市','重庆市'],
            _seed_html = l._$addNodeTemplate(html);

        /**
         * 所有分类卡片
         *
         * @class   {nm.l._$$ShopWin}
         * @extends {nm.l._$$CardWrapper}
         *
         *
         */
        p._$$ShopWin = NEJ.C();
        pro = p._$$ShopWin._$extend(Window);

        pro.__reset = function (options) {
        	this.__initDefaultData(options);
        	this.__getElements(options);
        	this.__initData(options);
            this.__super(options);
            this.__initWidgets(options);
            this.__initEventsHandlers(options);
        };
        
        pro.__initDefaultData=function(options){
        	options.mask=true;
        	options.title=options.title||"添加门店";
        };
        
        pro.__getElements=function(options){
        	/*获取form的*/
        	this.__shopFormNode=$("#shopForm")[0];
        	var inputs = $(".form-control", this.__body);
        	this.__shopNameNode=inputs[0];
        	this.__shopZoneNode=inputs[1];
        	this.__shopTelNode=inputs[2];
        	this.__shopContactNode=inputs[3];
        	this.__shopAddressCardNode=inputs[4];
        	this.__shopAddressNode=inputs[5];
        	var _arrow=$(".arrow",this.__body);
        	this.__arrowNode=_arrow[0];
        	var coords=$(".j-coordinate", this.__body);
        	this.__lngNode=coords[0];
        	this.__latNode=coords[1];
        	/*获取定位安按钮*/
        	this.__orientationNode=$(".j-orientation",this.__body)[0];
        	/*获取动作按钮*/
        	var btns=$(".j-action",this.__body);
        	if(btns&&btns.length){
        		this.__okBtnNode=btns[0];
            	this.__cancelBtnNode=btns[1];
        	}
        	/*获取隐藏的节点*/
        	var hidens=$(".j-hidden",this.__body);
        	this.__brandShopIdNode= hidens[2];
        	
        	
        };
        
        pro.__initData=function(options){
        	this.__initBasicData(options);
        };
        
        
        
        pro.__initBasicData=function(options){
        	if(options.data){
        		this.__opData=options.data;
        		this.__shopNameNode.value=this.__opData.shopName;
        		this.__shopZoneNode.value=this.__opData.shopZone;
        		this.__shopTelNode.value=this.__opData.shopTel;
        		this.__shopContactNode.value=this.__opData.shopContact;
        		this.__shopAddressCardNode.value=this.__resetCardAddressValue(this.__opData);
        		this.__shopAddressNode.value=this.__opData.shopAddr;
        		this.__lngNode.value=this.__opData.longitude;
        		this.__latNode.value=this.__opData.latitude;
        		this.__brandShopIdNode.value=this.__opData.brandShopId;
        		/*级联数据*/
//        		this.__shopProvinceNode.value=_data.provice;
//        		this.__shopCityNode.value=_data.city;
//        		this.__districtNode.value=_data.district;
        	}
        	else
        		this.__opData={};
        };
        
        pro.__resetCardAddressValue=function(_data){
        	if(_data.cardAddress)
        		return _data.cardAddress;
        	var value = '';
        	if(_data.province){
    			value += _data.province.name;
    		}
    		if(_data.city){
    			if(u._$indexOf(_zhiXiaCities,_data.province)==-1){
    				value += _data.city.name;
    			}
    		}
    		if(_data.district&&_data.district.id>0){
    			value +=_data.district.name;
    		}
    		if(_data.street&&_data.street.id>0){
    			value +=_data.street.name;
    		}
    		return value;
        };
        

        
        pro.__initWidgets=function(options){
        	this.__initFormWidget(options);
        	this.__initMap(options);
        };
        
        pro.__initFormWidget=function(options){
        	var that=this;
        	this.__formWidget = _f._$$WebForm._$allocate({
                form: "shopForm",
                oninvalid: function (_event) {
                    $(_event.target)._$parent(".form-group")._$children('.control-label')._$addClassName("js-error");
                },
                onvalid: function (_event) {
                    $(_event.target)._$parent(".form-group")._$children('.control-label')._$delClassName("js-error");
                    $(".js-errorMsg")._$html("");
                }
            });
        	
        };
        
        pro.__initMap=function(options){
        	var that=this;
        	var _lng=this.__lngNode.value||120.192642,_lat=this.__latNode.value||30.187820;
        	this.__mapObj = new AMap.Map("allmap", {
                view: new AMap.View2D({
                    center:new AMap.LngLat(_lng,_lat),//地图中心点
                    zoom:13//地图显示的缩放级别
                }),
                keyboardEnable:false
            });
        	
        	
        	this.__mapObj.plugin(["AMap.Geocoder"], function() {        
            	that.__MGeocoder= new AMap.Geocoder({ 
                });
                //返回地理编码结果 
            	AMap.event.removeListener(that.__MGeocoder, "complete", that.__resetCenterMap._$bind(that));
                AMap.event.addListener(that.__MGeocoder, "complete", that.__resetCenterMap._$bind(that)); 
            });
        	
        };
        
        
        pro.__initEventsHandlers=function(options){
        	var that = this;
        	
        	/*箭头*/
        	v._$addEvent(this.__arrowNode, "click", this.__onAddressClick._$bind(this));
        	
        	v._$addEvent(this.__orientationNode, "click", this.__onShowMap._$bind(this));
        	
            v._$addEvent(this.__okBtnNode, "click", this.__onOkBtnClick._$bind(this));

            v._$addEvent(this.__cancelBtnNode, "click", function (_event) {
            	v._$stop(_event);
            	that._$hide();
            });
            
        };
        
    	pro.__onAddressClick = function(event){
    		v._$stop(event);
    		this.__opData={};
    		AddressCard._$allocate({parent:"addrCard",data:{shopProvince:true},onchange:this.__onCardChange._$bind(this)})
    	};
    	
    	pro.__onCardChange=function(_data){
    		var value = '';
    		this.__opData=this.__opData||{};
    		if(_data.province){
    			value += _data.province;
    			this.__opData.province={id:_data.provinceId,name:_data.province};
    		}
    		if(_data.city){
    			this.__opData.city={id:_data.cityId,name:_data.city};
    			if(u._$indexOf(_zhiXiaCities,_data.province)==-1){
    				value += _data.city;
    			}
    		}
    		if(_data.section&&_data.sectionId>0){
    			value +=_data.section;
    			this.__opData.district={id:_data.sectionId,name:_data.section};
    		}
    		if(_data.street){
    			value +=_data.street;
    			this.__opData.street={id:_data.streetId,name:_data.street};
    		}
    		this.__opData.street=this.__opData.street||{id:"-111",name:"无名小路"};
    		this.__shopAddressCardNode.value = value;
    		this._$dispatchEvent('onchange',_data);
    	};
        
    	
    	pro.__getCityCode=function(){
    		if(this.__opData.city.id<0)
    			return this.__opData.province.name;
    		else
    			return this.__opData.city.name;
    	};
        
        pro.__onShowMap=function(event){
        	v._$stop(event);
        	var that =this;
        	if (this.__formWidget._$checkValidity()){
        		that.__MGeocoder.setCity(that.__getCityCode());
        		that.__MGeocoder.getLocation(that.__calAddress())
        	}
        };
        
        pro.__showMap=function(lng,lat){
        	var _lng=lng||120.192642,_lat=lat||30.187820,that=this;
        	this.__mapObj.setZoomAndCenter(14,new AMap.LngLat(_lng,_lat));
        	if(this.__marker){
        		this.__marker.setPosition(new AMap.LngLat(_lng,_lat));
        	}else
        	  this.__marker = new AMap.Marker({ //创建自定义点标注                 
      		  map:this.__mapObj,                 
      		  position: new AMap.LngLat(_lng,_lat),                 
      		  icon: "http://webapi.amap.com/images/0.png"                 
        	}); 
        };
        
        
        pro.__resetCenterMap=function(data){
        	this.__setCoordination(data);
        	this.__showMap(this.__lngNode.value,this.__latNode.value);
        	if(this.__okFlag){
        		this.__disptchOkData();
        	}
        };
        
        pro.__setCoordination=function(data){
    	    var geocode = new Array();
    	    geocode = data.geocodes;
    	    for (var i = 0,len=geocode.length; i < len; i++) {
    	    	this.__lngNode.value=geocode[i].location.getLng();
    	    	this.__latNode.value=geocode[i].location.getLat();
    	    }
    	   
        };
        
        
        pro.__calAddress = function () {
            return this.__shopAddressCardNode.value+this.__shopAddressNode.value;
        };
        
        pro.__onOkBtnClick = function (_event) {
        	v._$stop(_event);
            if (this.__formWidget._$checkValidity()) {
            	this.__okFlag=true;
            	this.__MGeocoder.getLocation(this.__calAddress());
            }
        };
        
        pro.__disptchOkData=function(){
        	var _data = this.__formWidget._$data();
        	delete _data.cardAddress;
        	if(!this.__opData.district){
        		$(".js-errorMsg")._$html('请至少选到区一级');
        		$("[name=cardAddress]")._$addClassName("js-invalid");
        		return;
        	}
    		u._$merge(this.__opData,_data);
    		this._$dispatchEvent("onOkCallback",this.__opData);
    		this._$hide();
        };
        
        
        
        

        // 控件重复使用重置过程
        // 控件回收销毁过程
        pro.__destroy = function () {
            this.__super();
        };



        pro.__initXGui = function () {
            this.__seed_html = _seed_html;
        };














        return p._$$ShopWin;
    })