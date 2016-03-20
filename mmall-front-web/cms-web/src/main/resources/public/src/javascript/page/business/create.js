/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{lib}util/form/form.js',
    '{lib}util/file/select.js',
    '{pro}extend/request.js',
    '{pro}components/notify/notify.js',
    '{lib}util/ajax/xdr.js',
    '{pro}extend/config.js',
    '{lib}util/data/region/zh.js',
    '{lib}util/region/zh.js',
    '{pro}lib/jquery/dist/jquery.min.js',
    '{pro}lib/jquery/dist/lightbox.min.js',
    '{pro}components/datepicker/datepicker.js',
    '{pro}widget/district/address.js',
    './userwin/infowin.js?v=1.0.0.1'
    ],
    function(ut,v,e,Module,ut1,s,Request,notify,j,c,t0,t1,jq,jql,DatePicker,Address,InfoWin,p) {
        var pro,
		districtDict = {},
		editSelectedUserList =[],
		selectedUserIdList =[],
		deleteDistrictIdList = [];
        p._$$CreateModule = NEJ.C();
        pro = p._$$CreateModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
			this.__initImage();
			this.__initDate();
            this.__initDistrict();
			this.__checkFranchising();
			this.__initManageType();
			
            //营业执照有效期  长期有效截至日期不能选
            if(this.chkbox.checked) this.__onChkboxClick();
            /**
			t1._$$RegionSelector._$allocate({
				cache:t0._$$CacheRegionZH._$allocate(),
				province:this.form['returnProvince'],
				city:this.form['returnCity'],
				area:this.form['returnCountry'],
				data:{province:e._$dataset(this.form['returnProvince'],'default'),city:e._$dataset(this.form['returnCity'],'default'),area:e._$dataset(this.form['returnCountry'],'default')},
				onchange:function(){}
			})*/
			
			t1._$$RegionSelector._$allocate({
				cache:t0._$$CacheRegionZH._$allocate(),
				province:this.form['contactProvince'],
				city:this.form['contactCity'],
				area:this.form['contactCountry'],
				data:{province:e._$dataset(this.form['contactProvince'],'default'),city:e._$dataset(this.form['contactCity'],'default'),area:e._$dataset(this.form['contactCountry'],'default')},
				onchange:function(){}
			})
        };
        
        pro.__getNodes = function(){
        	this.form = e._$get('dataform');
        	//var list = e._$getByClassName(this.form,'j-flag');
            this.chkbox = this.form['registrationNumberAvaliable'];
            this.districtForm = e._$get('districtForm');
            this.franchisingGroup = e._$get('groupFranchising');
            this.searchUserInput = e._$get('searchUserInput');
            this.searchUserList = e._$get('searchUserList');
            
            this.webform = ut1._$$WebForm._$allocate({
            	form:this.form,
            	oncheck:function(event){}._$bind(this)
            	});
        };
        
        pro.__addEvent = function(){
            v._$addEvent(this.chkbox,'click',this.__onChkboxClick._$bind(this));
        	v._$addEvent('submit','click',this.__onFormSubmit._$bind(this));
            v._$addEvent('districtAdd','click',this.__addDistrict._$bind(this));
           // v._$addEvent(this.searchUserInput,'keyup',this.__searchSpecificUsers._$bind(this));
           // v._$addEvent(this.searchUserInput,'click',this.__searchSpecificUsers._$bind(this));
            v._$addEvent('manageUser','click',this.__manageUsers._$bind(this));
            //v._$addEvent(this.searchUserInput.parentNode,'mouseleave',this.__showSearchUserList._$bind(this,false));
            v._$addEvent('dropdownMenuButton','click',this.__onClickManageTypeDropdown._$bind(this));
          	v._$addEvent(document.body,'click',this.__onHideDropdown._$bind(this));
          	v._$addEvent('dropdownMenuList','click',this.__onClickManageTypeDropdownList._$bind(this));
          	
            var div = e._$get('businessTypeCheck');
            var checkList = div.getElementsByTagName('input');
            ut._$forEach(checkList,function(_parent,_index){
            	v._$addEvent(_parent,'click',this.__businessTypeCheckClick._$bind(this,_parent,_index));
            }._$bind(this));
        };
        
        pro.__manageUsers = function(_event){
    		if(!!this.__infoWin){
    			this.__infoWin._$recycle();
    		}
    		
    		this.__infoWin = InfoWin._$allocate({
    		  	parent:document.body,
    		  	item:{'businessId': window.businessId}
    		})._$show();
        };
        
        pro.__onClickManageTypeDropdownList = function(_event){
        	this.dropdownlistisClick = true;
        	this.__updateManageTypeTitle();
        };
        
        pro.__onHideDropdown = function(_event){
        	// 隐藏经营类别
        	if(this.dropdownlistisClick == false){
        		var elm = e._$get('manageTypeDropdowMenu');
        		e._$delClassName(elm,'open');
        	}
        	this.dropdownlistisClick = false;
        	
        	// 隐藏指定用户
        	//this.__showSearchUserList(false);
        };
        
        pro.__onClickManageTypeDropdown = function(_event){
        	v._$stop(_event);
        	var elm = v._$getElement(_event);
        	if((elm.type == 'button') || (elm.tagName == 'SPAN')){
        		var div = e._$get('manageTypeDropdowMenu');
        		if(e._$hasClassName(div,'open')){
        			e._$delClassName(div,'open');
        		}
        		else {
        			e._$addClassName(div,'open');
        		}
        	}
        };
        
        pro.__updateManageTypeTitle = function(){
        	var li = e._$get('dropdownMenuList');
        	var list = e._$getByClassName(li,'j-flag');
        	var button = e._$get('dropdownMenuButton');
        	
        	var title = '';
        	var array = [];
        	
        	for(var i=0;i<list.length;i++){
        		var elm = list[i];
        		if(elm.checked){ 
        			array.push(elm.parentNode.innerText);
        			elm.checked = true;
        		}
        	}
        	title = array.join(',') + '<span class="caret">';
        	button.innerHTML = title;
        	
        	if(array.length >0){
        		// 删除提示经营类别提示
        		var elm = e._$get('manageTypeDropdowMenu');
        		var errorNode = e._$getByClassName(elm,'js-error');
    			if(errorNode.length){
    				/*e._$remove(errorNode[0].parentNode);*/
    				errorNode[0].innerHTML = "";
    			}
        	}
        };
        
        pro.__initManageType = function(){
        	var div = e._$get('manageTypeDropdowMenu');
        	var typeString = e._$dataset(div,'type');
        	var s = typeString.split(',');
        	
        	var li = e._$get('dropdownMenuList');
        	var list = e._$getByClassName(li,'j-flag');
        	var button = e._$get('dropdownMenuButton');
        	
        	var title = '';
        	var array = [];
        	
        	for(var i=0;i<s.length;i++){
        		var n = s[i];
        		if(n.length && (n<list.length)){
        			var elm = list[n];
        			if(elm.value == n){
        				elm.checked=true;
        				array.push(elm.parentNode.innerText);
        			}
        		}
        	}
        	
        	title = array.join(',') + '<span class="caret">';
        	button.innerHTML = title;
        };
        
        pro.__initDistrict = function() {
            var list = e._$getByClassName(this.districtForm,'j-address');
            for (var i=0,l=list.length; i<l; i++) {
				var provinceId = e._$dataset(list[i],'provinceid');
				var cityId = e._$dataset(list[i],'cityid');
				var districtId = e._$dataset(list[i],'districtid');
				
                Address._$$AddressSelector._$allocate({
                	parentNode:list[i],
                    data:{provinceId:provinceId,cityId:cityId,districtId:districtId},
                    remove:function(node){this.__removeDistrict(node)}._$bind(this)
                });
            }
        };

        pro.__initDate = function(){
            this.__picker = [];
            this.__datepicker = e._$getByClassName(this.form,'j-datepick');
        	ut._$forEach(
                    this.__datepicker,
                    function(_parent,_index){
        	                var _name = e._$dataset(_parent,'name'),
        	                    _value = parseInt(e._$dataset(_parent,'value')),
        	                    _time = e._$dataset(_parent,'time'),
           	                    _picker = new DatePicker({
        	                        data:{name:_name,select:_value,time:_time}
        	                    });
                            this.__picker.push(_picker);
        	                _picker.$inject(_parent);
        	                this.form[_name].defaultValue = _value;
                    }._$bind(this)
                );
        };
        
        pro.__initImage = function(){
        	var list = e._$getByClassName(this.form,'j-img');
        	for(var i=0,l=list.length;i<l;i++){
        		s._$bind(list[i], {
                    name: 'img',
                    multiple: false,
                    accept:'image/*',
                    parent:list[i].parentNode,
                    onchange: this.__onImageUpload._$bind(this,list[i])
                });
        	}
        	
        };
        pro.__onImageUpload = function(_label,event){
        	var name = e._$dataset(_label,'name');
        	var form = event.form;
        	form.action = c.UPLOAD_URL;
        	j._$upload(form,{onload:function(result){
        		//this.frontImg.src = result.result[0].imgUrl;
        		var list =  _label.parentNode.getElementsByTagName('img');
        		var errorNode = e._$getByClassName(_label.parentNode,'js-error');
    			if(errorNode.length){
    				e._$remove(errorNode[0].parentNode);
    			}
        		if(list.length){
        			list[0].src =  result.result[0].imgUrl;
        			list[0].parentNode.href= result.result[0].imgUrl;
        		} else{
        			var a = e._$create('a');
        			a.href=result.result[0].imgUrl;
        			e._$dataset(a,'lightbox',name);
        			var img = e._$create('img');
        			img.src= result.result[0].imgUrl;
        			a.appendChild(img);
        			_label.parentNode.insertAdjacentElement('afterBegin',a);
        		}
        		this.form[name].value =  result.result[0].imgUrl;
        		//this.__frontImage = result.result[0].imgUrl;
        	}._$bind(this),
        	onerror:function(e){
        		notify.show('上传图片可能超过2M');
        	}})
        	//e._$delClassName(this.lblParent.parentNode,'js-invalid');
        }
        
        // 编辑商家页面，type==3，freemark文件中会显示指定用户选择面板
        pro.__checkFranchising = function() {
        	var check = e._$get('franchisingCheck')
        	if(check.checked){
        		this.__showFranchising(true);
        		
            	// 从编辑页面上获取已经服务器下发的已经选择的指定用户列表
            	/*var ul = e._$get('selectedUserList');
            	var list = e._$getByClassName(ul,'j-flag');
            	for(var i=0;i<list.length;i++){
            		var a = list[i];
            		var user = new Object();
            		user.id = a.getAttribute('data-id');
            		user.userId = a.getAttribute('data-userId');
            		user.userName = a.getAttribute('data-userName');
            		editSelectedUserList.push(user); 
            		selectedUserIdList.push(user.userId);
            		v._$addEvent(a,'click',this.__deleteUser._$bind(this,a,user.userId));
            	}*/
            	
            	this.oldIsFranchising = true;
        	}
        	else {
        		this.oldIsFranchising = false;
        	}
        };
        
        
        pro.__businessTypeCheckClick = function(_parent,_index){
            //console.log('parent='+_parent+'index='+_index);
            this.__showFranchising((_index==3));
        };

        // 点击特许经营展开
        pro.__showFranchising = function(_show){
            // 修改 groupFranchising 类 hidden或show
            var franchising = e._$get('groupFranchising');
            if(_show){
            	e._$replaceClassName(franchising,'hidden','show');
            	this.isFranchising=true;
            }
            else {
            	e._$replaceClassName(franchising,'show','hidden');
            	this.isFranchising=false;
            }
        };
	
		/*pro.__searchSpecificUsers = function(){
			var text = this.searchUserInput.value;
			//console.log('搜索内容'+text);
			
			if(text.length==0){
				//this.__showSearchUserList(false);
			}
			else {
				// TODO:分页实现
				Request('/business/user/list?userName='+text+'&limit=100&offset=0',{
                    method:'GET',
                    type:'JSON',
                    onload:this.__loadSearchUsers._$bind(this),
                    onerror:this.__loadSearchUsers._$bind(this)
                })
			}           
		};
		
		pro.__loadSearchUsers = function(_result){
			if(_result.code==200){
			//	this.__showSearchUserList(true);
				
				var list = _result.result.list;
                var dom='';
				for(var i=0;i<list.length;i++){
					var user = list[i];
					dom += '<dd><label>'+user.userName+'<label><a class="btn btn-link">点击添加</a></dd>';
				}
                this.searchUserList.innerHTML=dom;
                
                var elements = this.searchUserList.getElementsByTagName('a');
                for(var i=0;i<elements.length;i++){
                	var user = list[i];
                	v._$addEvent(elements[i],'click',this.__addUser._$bind(this,user.userId,user.userName));
                }
			}
		};
		
		pro.__showSearchUserList = function(_show){
           // this.searchUserList.innerHTML="";
                
			if(_show) {
				e._$replaceClassName(this.searchUserList,'userList-normal','userList-active');
			}
			else {
				e._$replaceClassName(this.searchUserList,'userList-active','userList-normal');
			}
		};*/
		
		/*pro.__addUser = function(_userId,_userName){
			// 去重
			for(var i=0;i<selectedUserIdList.length;i++){
				if(selectedUserIdList[i] == _userId){
					return;
				}
			}
			
			//console.log('添加用户='+_userId+_userName);
			var ul = e._$get('selectedUserList');
			
			var dom ='<li><label>'+_userName+'</label><a class="btn btn-link j-flag" data-id="" data-userId="'+_userId+'" data-userName="'+_userName+'" >删除</a></li>';
			
			var li = e._$create('dd');
			li.innerHTML = dom;
			ul.appendChild(li); 
			var a = li.getElementsByTagName('a');
			v._$addEvent(a[0],'click',this.__deleteUser._$bind(this,a[0],_userId));
			
			// 将添加的用户userId加入数组
			selectedUserIdList.push(_userId);
			//this.__showSearchUserList(false);
		};*/
		
		/*pro.__deleteUser = function(_node,_userId){
			// 先判断删除的是否是提交过的
			var id = _node.getAttribute('data-id');
			if(!id){
				e._$remove(_node.parentNode,true);
			}
			else {
				Request('/business/deleteBusiUserRelation/'+id,{
                    onload:function(_result){
                    	if(_result.code==200){
                    		e._$remove(_node.parentNode,true);
                    	}
                    },
                    onerror:function(_error){
                    	//console.log('删除指定用户失败');
                    	notify.show('删除指定用户失败');
                    }
                })
			}
		};*/
		
		/*pro.__getSelectedUser = function(_data){
			if(this.isFranchising){
				var ul = e._$get('selectedUserList');
            	var list = e._$getByClassName(ul,'j-flag');
            	var selectedUserList=[];
            	for(var i=0;i<list.length;i++){
            		var a = list[i];
            		var user = new Object();
            		user.id = a.getAttribute('data-id');
            		user.userId = a.getAttribute('data-userId');
            		user.userName = a.getAttribute('data-userName');
            		selectedUserList.push(user); 
            	}
            	_data['busiUserRelations'] = selectedUserList;
			}
			
			// 有特许经营变成非特许经营，
			if(this.oldIsFranchising && (!this.isFranchising)){
				_data['typeIsChange'] = 'Y';
			}
			else {
				_data['typeIsChange'] = 'N';
			}
			
			return _data;
		};*/
		
        pro.__addDistrict = function(){
            var div = e._$create('div','form-group');
            div.innerHTML = '<div class="j-address col-sm-12"></div>';
            this.districtForm.appendChild(div);

            var vlist = e._$getByClassName(div,'j-address');
            Address._$$AddressSelector._$allocate({
                parentNode:vlist[0],
                remove:function(node){this.__removeDistrict(node)}._$bind(this)
            });
        };
		
		pro.__removeDistrict = function(node){
			// 最后一个不允许删除
			var list = e._$getByClassName(this.districtForm,'form-group');
			if(list.length ==1){
				notify.show('请至少填写一个配送地址');
				return;
			}
			var id = e._$dataset(node,'id');
			if(id && id.length>0){
				deleteDistrictIdList.push(id);
			}			
			e._$remove(node.parentNode);
		};
		
		pro.__getDistrict = function(data){
            var list = e._$getByClassName(this.districtForm,'j-address');
            var districts = [];
            for (var i=0,l=list.length; i<l; i++) {
				var vlist = e._$getByClassName(list[i],'form-control');
                
                var id = e._$dataset(list[i],'id');
                
                var index = vlist[0].selectedIndex;
                var option = vlist[0].options[index];
                var provinceId = option.value;
                var provinceTitle = option.text;
                
                var cityId = '00';
                var cityTitle = '';
				if(vlist.length>1){
					var index1 = vlist[1].selectedIndex;
                	var option1 = vlist[1].options[index1];
                	cityId = option1.value;
                	
                	// 直辖市不用加市名
                	if(option1.text != provinceTitle){
                		cityTitle = ' ' + option1.text;	
                	}
				}
                
                var districtId = '00';
                var districtTitle = '';
                if(vlist.length>2){
                	var index2 = vlist[2].selectedIndex;
                	var option2 = vlist[2].options[index2];
                	districtId = option2.value;	
                	districtTitle = ' ' + option2.text;
                }
                
                var district = new Object();
                district.id = id?id:'';
                district.provinceId = provinceId;
                district.cityId = cityId;
                district.districtId = districtId;   
                district.districtName = provinceTitle+cityTitle+districtTitle;
                districts.push(district);
            }
            
            // 从j-district 中取,j-district 中包含的是不可编辑的配送地址
            var jlist = e._$getByClassName(this.districtForm,'j-district');
            for(var i=0;i<jlist.length;i++){
            	var di = jlist[i]
            	
            	var id = e._$dataset(di,'id')
            	var provinceId = e._$dataset(di,'provinceId')
            	var cityId = e._$dataset(di,'cityId')
            	var districtId = e._$dataset(di,'districtId')
            	var districtName = e._$dataset(di,'districtName')
            	
            	var district = new Object();
                district.id = id?id:'';
                district.provinceId = provinceId;
                district.cityId = cityId;
                district.districtId = districtId;   
                district.districtName = districtName;
                districts.push(district);
            }
            
            data['sendDistrictDTOs'] = districts;
            
            // 删除
            if(deleteDistrictIdList.length >0)
            	{data['areaIds'] = deleteDistrictIdList;}
            	
            return data;
		};
		
        pro.__onChkboxClick = function(){
            if(this.chkbox.checked){
                this.__picker[1].data.select = 0;
                this.__picker[1].$update('disable',true);
                //this.__datepicker[1].style.display='none';
            }else{
            	//this.__datepicker[1].style.display='block';
                this.__picker[1].data.select = new Date().valueOf();
                this.__picker[1].$update('disable',false);
            }
        };
        pro.__onTypeChange = function(_type,_event){
        	if(_type==1){
                e._$delClassName('allsites','f-dn');
                e._$addClassName('sites','f-dn');
        		e._$delClassName('brandImg','f-dn');
        		e._$addClassName('brandAuthImg','f-dn');
        	} else{
                e._$delClassName('sites','f-dn');
                e._$addClassName('allsites','f-dn');
        		e._$delClassName('brandAuthImg','f-dn');
        		e._$addClassName('brandImg','f-dn');
        	}
        };
        pro.__checkDate = function(_data){
        	var pass = true;
        	if(!_data.registrationNumberStart){
        		pass = false;
        		e._$addClassName(this.form.registrationNumberStart,'js-invalid');
        	} else{
        		e._$delClassName(this.form.registrationNumberStart,'js-invalid');
        	}
        	if(_data.registrationNumberAvaliable!='on'&&!_data.registrationNumberEnd){
        		pass = false;
        		e._$addClassName(this.form.registrationNumberEnd,'js-invalid');
        	} else{
        		e._$delClassName(this.form.registrationNumberEnd,'js-invalid');
        	}
        	
        	// 经营类别判断
        	var manageType = _data['manageType'];
        	var elm = e._$get('manageTypeDropdowMenu');
        	var err = e._$get('manageTypeError');
        	if(manageType == null){
        		pass = false;
        		err.innerHTML = e._$dataset(elm,'message');
        	}else{
        		err.innerHTML = "";
        	}
        	
        	return pass;
        };

        pro.__getData = function(_data){
            var password = _data['password'];
            if(password == '******'){
                delete _data['password'];
                _data['passwordIsChange'] = 'N';
            }
            else {
                _data['passwordIsChange'] = 'Y';   
            }
            
            // 经营分类
            var typeArray = _data['manageType'];
            if(ut._$isArray(typeArray))
            {
            	var typeString = typeArray.join(',');
            	_data['manageType'] = typeString;
            }
            else if(ut._$isString(typeArray)){
            	_data['manageType'] = typeArray;
            }
            else {
            	console.log("获取经营分类错误");
            }
        };

        pro.__onFormSubmit = function(){
        	var data = this.webform._$data();
        	var pass = this.webform._$checkValidity(),
        	    pass1 = this.__validImage(),
                pass2 = this.__checkTimeRange(),
                datecheck = this.__checkDate(data);
                //console.log("pass=%d,pass1=%d,pass2=%d,datecheck=%d",pass,pass1,pass2,datecheck)
        	if(pass&&pass1&&pass2&&datecheck){
                // data = this.__areaidToInt(data);
                data = this.__registrationNumberAvaliable(data);
                data = this.__getDistrict(data);
                //data = this.__getSelectedUser(data);
                this.__getData(data);
                Request('/business/create/account',{
        			data:data,
        			method:'POST',
                    type:'JSON',
        			onload:this.__cbCreateAccount._$bind(this),
        			onerror:this.__onError._$bind(this)
        		})
        	}
        };
        
        pro.__registrationNumberAvaliable = function(data) {
        	if(data.registrationNumberAvaliable=='on'){
                data.registrationNumberAvaliable = 1;
            } else{
                data.registrationNumberAvaliable = 0;
            }
            return data;  
        }
        
        pro.__areaidToInt = function(data){
           // if(this.form['type'][0].checked){
                var list = e._$getByClassName('sitebox','j-site'),
                    siteList = [];
                for(var i=0,len=list.length;i<len;i++){
                    if(list[i].checked){
                        siteList.push(parseInt(list[i].id));
                    }
                }
                if(!siteList.length){
                    notify.show({
                        type:'error',
                        message:'请选择站点！'
                    })
                }else{
                    data['areaIds'] = siteList;
                }
          //  }else if(this.form['type'][1].checked){
              //  data['areaIds'] = [0];
          //  }
            return data;
        };
        pro.__checkTimeRange = function(){
            if((this.chkbox.checked == false) && (this.__picker[1].data.select < this.__picker[0].data.select)){
                notify.show({
                    type:'error',
                    message:'请选择正确的时间范围'
                });
                return false;
            }else{
                return true;
            }
        };
        pro.__validImage = function(){
        	var list = e._$getByClassName(this.form,'j-img');
        	var valid = true;
        	for(var i=0,l=list.length;i<l;i++){
        		var ipt = list[i].parentNode.parentNode.getElementsByTagName('input')[1];
        		
        		if(ipt.value==''){
//        			if(ipt.name=='brandImg'&&this.form['type'][0].checked){
//        				e._$delClassName(ipt.parentNode,'js-invalid');
//            			continue;
//            		}
//        			if(ipt.name=='brandAuthImg'&&this.form['type'][1].checked){
//        				e._$delClassName(ipt.parentNode,'js-invalid');
//            			continue;
//            		}
        			var errorNode = e._$getByClassName(ipt.parentNode,'js-error');
        			if(!errorNode.length){
        				var div = e._$create('div');
            			div.innerHTML ='<span class="js-error">'+ e._$dataset(ipt,'message')+'</span>';
            			list[i].parentNode.appendChild(div);		
        			}
        			e._$addClassName(ipt.parentNode,'js-invalid');
        			valid = false;
        		} else{
        			e._$delClassName(ipt.parentNode,'js-invalid');
        		}
        	}
        	return valid;
        };
        pro.__cbCreateAccount = function(result){
        	if(result.code==200){
        		if(this.isFranchising&&result.result){
        			location.href = '/business/creaseAccountSuccess?businessId='+result.result;
        		}else{
        			location.href='/business/account';
        		}
        	} else{
        		if(result.message){
        			notify.showError(result.message);
        		}
        		else {  
        			notify.showError("error");	
        		}
        	}
        }
        pro.__onError = function(result){
        	if(result.message){
    			notify.showError(result.message);
    		}
    		else {  
    			notify.showError('系统错误');
    		}
        	
        };
        
        pro.__getBrandList = function(){
            // 
            Request('/item/brand/list',{
                    method:'GET',
                    type:'JSON',
                    onload:function(_result){
                        if (_result.code==200) {

                        }
                    },
                    onerror:this.__onError._$bind(this)
                })
        };

        p._$$CreateModule._$allocate();
    });