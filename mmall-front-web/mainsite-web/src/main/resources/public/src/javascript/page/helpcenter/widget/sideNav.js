/**
 * 帮助中心-侧栏
 * author zzj(hzzhangzhoujie@corp.netease.com)
 */

define([
  "pro/extend/util",
  "pro/widget/BaseComponent",
  'text!./sideNav.html'
  ], function(_, BaseComponent,tpl){

  var SideNavComponent = BaseComponent.extend({
	url:"/help/leftNav",
    config: function(data){
      _.extend(data, {
    	selCateId:"",
    	selSubCateId:""
      });
    },
    template:tpl,
    init: function(){
      if(!this.url) throw "ListModule未指定url";
      this.__getList();
    },
    toggle:function(item){
    	if(this.data.selCateId==item.id){
    		this.data.selCateId= "";
    	}else{
    		this.data.selCateId=item.id;
    	}
    },
    $setCategeoryId:function(_id){
    	this.data.selSubCateId = _id;
    	this.$update();
    },
    __getList: function(){
      this.$request(this.url, {
    	method:'get',
    	type:"json",
        onload: function(json){
           this.$update(function(data){
              data.list = json.result.list;
           });
        },
        // test
        onerror: function(json){
          // @TODO: remove
        }
      })
    }
  })


  return SideNavComponent;

})