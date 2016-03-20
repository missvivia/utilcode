/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{lib}util/file/select.js'
        ,'{pro}widget/module.js'
        ,'{pro}widget/layer/countdown/countdown.setting.js'],
    function(ut,v,e,s,Module,ProductModuleSetting,p,o,f,r) {
        var pro;
        var product = {
        			show:true,
        			cssText:''
        		};
        p._$$IndexModule = NEJ.C();
        pro = p._$$IndexModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
			this.__setData();
        };
        pro.__setData = function(){
        	this.show = e._$getByClassName(this.__productMdl,'hideshow')[0];
        	if(product.show){
        		e._$dataset(this.show,'show','1');
        		this.show.innerText ='隐藏';
        	} else{
        		e._$dataset(this.show,'show','0');
        		this.show.innerText ='显示';
        	}
        };
        pro.__getNodes = function(){
        	var list = e._$getByClassName('body','j-flag');
        	this.__custMdl = list[0];
        	this.__productMdl = list[1];
        	this.__mapMdl = list[2];
        };
        
        pro.__addEvent = function(){
           v._$addEvent('body','click',this.__onBodyClick._$bind(this));
        };
        pro.__onBodyClick = function(event){
        	var elm = v._$getElement(event);
        	if(e._$hasClassName(elm,'hideshow')){
        		product.show = !product.show;
        		this.__setData();
        	} else if(e._$hasClassName(elm,'pedit')){
        		ProductModuleSetting._$allocate({onok:this.__onSettingOK._$bind(this),cssText:product.cssText})._$show();
        	}
        };
        pro.__onSettingOK = function(style){
        	this.cssText =[];
        	this.cssText.push('font:'+style.font)
        	this.cssText.push('color:'+style.color)
        	this.cssText.push('border:1px solid '+style.borderColor)
        	this.cssText.push('background-color:'+style.bgColor)
        	this.cssText.push('opacity:'+style.bgOpcity||1)
        	
        	product.cssText = this.cssText.join(';');
        }
        p._$$IndexModule._$allocate();
    });