define([
	'{lib}base/global.js',
	'{lib}base/element.js',
    '{lib}base/event.js',
    '{pro}components/notify/notify.js',
	'{pro}widget/module.js',
    '{pro}extend/request.js',
    '{pro}components/modal/modal.js',
    '{pro}lib/jquery/dist/jquery.js'
],function(NEJ,e,v,notify,Module,Request,Modal,jquery){
	
	$$ScanModule = NEJ.C();
    pro = $$ScanModule._$extend(Module);
    
    pro.__init = function(_options) {
        this.__supInit(_options);
        this.__addEvent();
        this.__initEdit();    
    };
    
    pro.__initEdit = function(){
        var detail = e._$get('productDetail');
        detail.innerHTML = window.prodDetail;
    };
    
    pro.__addEvent = function(){
        v._$addEvent('onShelve','click',this.__onShelve._$bind(this));
        v._$addEvent('unShelve','click',this.__unShelve._$bind(this));
        v._$addEvent('onBack','click',this.__onBack._$bind(this));
		v._$addEvent('onDelete','click',this.__onDelete._$bind(this));
    };

    // 上架商品
    pro.__onShelve = function(_event){
    	var elm = v._$getElement(_event);
    	var skuId = e._$dataset(elm,'skuid');
    	
        Request("/item/product/action?action=shelve&productSKUId="+skuId,{
            method:'GET',
            onload:function(_json){
              if(_json.code==200){
                notify.show("上架已成功！");
                // 刷新当前页面
                location.reload(true);
              }
            },
            onerror:function(_error){
                notify.show("上架提交失败！");
            }
        })
    };
    
    // 下架商品
    pro.__unShelve = function(_event){
    	var elm = v._$getElement(_event);
    	var skuId = e._$dataset(elm,'skuid');
    	
        var modal = new Modal({
            data:{
              'title':'商品下架',
              'content':'下架后，该商品将不能出售！<br/>再次上架后可以再次出售',
              'width':500}
        });

        modal.$on('confirm',function(){
            Request("/item/product/action?action=unshelve&productSKUId="+skuId,{
                method:'GET',
                onload:function(_json){
                    if(_json.code==200){
                      notify.show("下架成功！");
                      location.reload(true);
                    }
                },
                onerror:function(){
                    notify.show('下架商品失败');
                }
            })
            modal.destroy();
      }.bind(this));
      
      modal.$on('close',function(){modal.destroy();}.bind(this));
    };

    // 返回
    pro.__onBack = function(){
    	location.href='/item/product/list?type='+window.listType;
    };
    
    // 删除
    pro.__onDelete = function(_event){
    	var elm = v._$getElement(_event);
    	var skuId = e._$dataset(elm,'skuid');
    	
    	var modal = new Modal({
            data:{
              'title':'删除商品',
              'content':'确定删除商品？',
              'width':500}
        });

        modal.$on('confirm',function(){
            Request("/item/product/delete/"+skuId,{
                method:'GET',
                onload:function(_json){
                    if(_json.code==200){
                      notify.show("删除成功！");
                      location.href='/item/product/list?type='+window.listType;
                    }
                },
                onerror:function(){
                    notify.show('删除商品失败');
                }
            })
            modal.destroy();
        }.bind(this));
      
        modal.$on('close',function(){modal.destroy();}.bind(this));
    };

    $$ScanModule._$allocate();
    //return $$ScanModule;
})