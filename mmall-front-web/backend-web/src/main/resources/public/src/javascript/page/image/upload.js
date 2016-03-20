/**
\ * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['base/util'
        ,'base/event'
        ,'base/element'
        ,'pro/widget/module'
        ,'util/tab/tab'
        ,'util/file/select'
        ,'pro/widget/item/preview.image/preview.image'
        ,'util/template/tpl'
        ,'base/platform'
        ,'util/ajax/xdr'
        ,'pro/components/notify/notify'
        ,'pro/extend/config'
        ,'pro/extend/util'
        ,'pro/components/progress/progress'],
    function(u,v,e,Module,t,s,PreviewItem,e1,pt,_j,notify,c,_,progress,p,o,f,r) {
        var pro;

        p._$$UploadModule = NEJ.C();
        pro = p._$$UploadModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__initTab();
			this.__addEvent();
			s._$bind(this.initUploadBtn, {
                name: 'img',
                multiple: true,
                accept:'image/*',
                onchange: this.__onFileChange._$bind(this)
            });
			this.files = [];
			this.__initWaterPrint();
        };
        pro.__onFileChange = function(event){
        	if(pt._$KERNEL.browser=='ie'&&(pt._$KERNEL.release=='4.0'||pt._$KERNEL.release=='5.0'||pt._$KERNEL.release=='3.0')){
        		_j._$upload(form,{onload:function(_result){
        			if(_result&&_result.code==200){
                		_._$uploadImage2Category(_result.result,
                				parseInt(this.categoryNode.options[this.categoryNode.selectedIndex].value),
                				this.__cbUploadImageCategory._$bind(this));
                		notify.show('上传成功');
                	}
            		
            	}._$bind(this),
            	onerror:function(e){
            		notify.show('上传图片可能超过2M');
            	}})
        	} else{
	        	
	        	var form = event.form;
	    		var list = form.getElementsByTagName('input');
	    		var input;
	            for (var i = 0, l = list.length; i < l; i++) {
	                if (list[i].type == 'file'&&list[i].files.length!=0) {
	                	input = list[i];
	                }
	            }
	            if(this.files.length+input.files.length>10){
	         		notify.show('最大一次上传限制10张图片');
	         		return;
	         	}
	            e._$addClassName(this.initUpload,'f-dn');
	            this.__addFiles(input.files);
//	            
//	            for(var i=0,l=input.files.length;i<l;i++){
//	            	if(this.files.length>10){
//	            		notify.show('最大一次上传限制10张图片');
//	            		break;
//	            	} else{
//	            		if(input.files[i].size/1024/1024>1){
//	            			notify.show('上传的图片超过1M,请转成jgp格式上传');
//	            			return;
//	            		}
//	            		this.files.push(input.files[i]);
//	            	}
//	            }
//	            this.__renderImageFiles(this.files);
        	}
        }
        
        pro.__renderImageFiles = function(files){
        	if(this.items){
        		this.items = PreviewItem._$recycle(this.items);
        	}
        	this.items = e1._$getItemTemplate(files,PreviewItem,{parent:this.uploadBox,ondelete:this.__onItemDelete._$bind(this)});
        	if(!this.uploadNode){
        		this.uploadNode = this.__createUploadNode();
        		this.uploadBox.appendChild(this.uploadNode);
        	} else{
        		this.uploadBox.appendChild(this.uploadNode);
        	}
        	if(files.length >= 10){
        		e._$addClassName(this.uploadNode,'f-dn');
        	}
        };
        pro.__createUploadNode = function(){
        	var item = e._$create('div','item');
        	item.innerHTML = '<a class="fileuploadbtn f-ib"><input type="file" multiple class="fileipt"/><img src="/res/images/addmore.png" width="148" height="148"/></a>';
        	this.__fileInput = item.getElementsByTagName('input')[0];
        	v._$addEvent(this.__fileInput,'change',this.__onFileInputChange._$bind(this));
        	return item;
        };
        pro.__onFileInputChange = function(event){
        	if(this.files.length+event.target.files.length>10){
        		notify.show('最大一次上传限制10张图片');
        		return;
        	}
        	this.__addFiles(event.target.files);
//        	
//        	for(var i=0,l=event.target.files.length;i<l;i++){
//            	if(this.files.length>10){
//            		notify.show('最大一次上传限制10张图片');
//            		break;
//            	} else{
//            		if(event.target.files[i].size/1024/1024>1){
//            			notify.show('上传的图片超过1M,请转成jgp格式上传');
//            			return;
//            		}
//            		this.files.push(event.target.files[i]);
//            	}
//            }
//        	
//        	this.__renderImageFiles(this.files);
        };
        pro.__onItemDelete = function(item){
        	var index = u._$indexOf(this.files,item);
        	this.items[index]._$recycle();
        	this.items.splice(index,1)
        	this.files.splice(index,1);
        	if(this.files.length < 10){
        		e._$delClassName(this.uploadNode,'f-dn');
        	}
        };
        pro.__appendImage = function(canvas,file){
        	var item = e._$create('div','item');
            item.appendChild(canvas)
            this.uploadBox.appendChild(item);
        }
        
        pro.__initTab = function(){
        	//引入{lib}util/tab/tab.js
        	var tb = t._$$Tab._$allocate({
				list : e._$getChildren(this.tab),
				index : 0,
				selected:'active',
				onchange : this.__onTabChange._$bind(this)
			});
        }
        pro.__getNodes = function(){
            var node = e._$get('upload');
            var list = e._$getByClassName(node,'j-box');
            this.tab = list[0];
            this.upload = list[1];
            this.markbox = list[2];
            list =  e._$getByClassName(this.upload,'j-flag');
            this.uploadBox = list[0];
            this.initUpload = list[1];
            this.initUploadBtn = list[2];
            this.uploadBtn = list[3];
            this.clearBtn = list[4];
            this.waterform = e._$get('waterpring');
            this.waterpringimg = e._$get('waterpringimg');
            
            this.categoryNode = e._$get('category');
        };
        pro.__onTabChange = function(event){
        	var index=  event.index;
        	if(index==0){
        		e._$delClassName(this.upload,'f-dn');
        		e._$addClassName(this.markbox,'f-dn');
        	} else if(index=1){
        		e._$addClassName(this.upload,'f-dn');
        		e._$delClassName(this.markbox,'f-dn');
        	}
        }
        pro.__addFiles = function(files){
        	for(var i=0,l=files.length;i<l;i++){
            	if(this.files.length>10){
            		notify.show('最大一次上传限制10张图片');
            		break;
            	} else{
            		if(files[i].size/1024/1024>5){
            			notify.show('上传的图片超过5M,请转成jgp格式上传');
            			return;
            		}
            		this.files.push(files[i]);
            	}
            }
            this.__renderImageFiles(this.files);
        }
        pro.__onDragDrop = function(event){
        	v._$stop(event);
        	 var files = event.dataTransfer.files;
        	 
        	 if(this.files.length+files.length>10){
         		notify.show('最大一次上传限制10张图片');
         		return;
         	 }
        	 e._$addClassName(this.initUpload,'f-dn');
        	 this.__addFiles(files);
        	 
//            for(var i=0,l=files.length;i<l;i++){
//            	if(this.files.length>10){
//            		notify.show('最大一次上传限制10张图片');
//            		break;
//            	} else{
//            		if(files[i].size/1024/1024>1){
//            			notify.show('上传的图片超过1M,请转成jgp格式上传');
//            			return;
//            		}
//            		this.files.push(files[i]);
//            	}
//            }
//            this.__renderImageFiles(this.files);
        };
        
        pro.__addEvent = function(){
           v._$addEvent(this.uploadBox,'drop',this.__onDragDrop._$bind(this));
           v._$addEvent(this.uploadBox, 'dragover',function(event){v._$stop(event)});
           v._$addEvent(this.uploadBox, 'dragleave', function(event){v._$stop(event)});
           v._$addEvent(this.uploadBtn, 'click', this.__onStartUploadClick._$bind(this));
           v._$addEvent(this.clearBtn, 'click', this.__onClearUploadClick._$bind(this));
        };
        pro.__onStartUploadClick = function(){
        	if(this.files.length == 0){
        		notify.show('图片不能为空');
        		return;
        	}
        	var fd = new FormData();
	        for (var i = 0,l=this.files.length; i < l; i++) {
	            fd.append('img', this.files[i]);
	        }
	        progress.start();
	        _j._$request(c.UPLOAD_URL, {
	            method: 'POST',
	            data: fd,
	            type: 'json',
	            timeout:10*60000,
	            headers: {
	                'Content-Type': 'multipart/form-data'
	            },
	            onuploading:function(_data){
	            	progress.move(_data.progress)
	            },
	            onload: this.__cbSendBatchFiles._$bind(this),
	            onerror: this.__cbSendBatchFiles._$bind(this)
	        });
        };
        pro.__cbSendBatchFiles = function(_result){
        	progress.end();
        	if(_result&&_result.code==200){
        		_._$uploadImage2Category(_result.result,
        				parseInt(this.categoryNode.options[this.categoryNode.selectedIndex].value),
        				this.__cbUploadImageCategory._$bind(this));
        		notify.show('上传成功');
        	} else{
        		notify.showError('上传失败');
        	}
        };
        pro.__cbUploadImageCategory = function(){
        	this.__onClearUploadClick();
        };
        pro.__onClearUploadClick = function(){
        	this.files=[];
        	if(this.items){
        		this.items = PreviewItem._$recycle(this.items);
        	}
        	e._$delClassName(this.uploadNode,'f-dn');
        };
        pro.__initWaterPrint = function(){
        	s._$bind(this.waterform.upload, {
                name: 'img',
                multiple: true,
                accept:'image/*',
                onchange: this.__onWaterPringUpload._$bind(this)
            });
        };
        
        pro.__onWaterPringUpload = function(event){
        	var form = event.form;
        	_j._$upload(form,{onload:function(result){
        		this.waterpringimg.src = result.image.url
        	},
        	onerror:function(e){
        		notify.show('上传图片可能超过2M');
        	}})
        }
        p._$$UploadModule._$allocate();
    });