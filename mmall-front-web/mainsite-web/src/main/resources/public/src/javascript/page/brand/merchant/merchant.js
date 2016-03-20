/**
 * 商家信息
 * author hzzhangweidong(hzzhangweidong@corp.netease.com)
 */
NEJ.define([
'{pro}extend/util.js',
  "util/chain/chainable",
  "base/element",
  'pro/extend/util',
  "text!./merchant.html",
  "pro/widget/layer/zoom.window/zoom.window",
  "pro/widget/BaseComponent"
  ], function(_,$, e ,_,tpl,Window,BaseComponent){

  var Merchant = BaseComponent.extend({
    name: "merchant",
    template: tpl,
    config: function(data){
    	var _ration="?imageView&thumbnail=820x820",_vars=_._$queryStrings();
    	_.extend(data,{
    		registrationImg2:this.data.registrationImg+_ration,
        	brandAuthImg2:this.data.brandAuthImg+_ration,
        	id:_vars["id"]
    	})
    },
    init: function(){
    	
    },
    popic:function(_src){
		this.__zoomin = Window._$allocate({
			mask:true,
			src:_src
		})
		setTimeout(this.__zoomin._$show._$bind(this.__zoomin),0);
    	
    },
    hideBox:function(){
    	if(this.__zoomin)
    	this.__zoomin._$hide();
    }
  });

  return Merchant;

})