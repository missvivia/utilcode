/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js'
        ,'{lib}base/event.js'
        ,'{lib}base/element.js'
        ,'{lib}util/file/select.js'
        ,'{pro}widget/module.js'],
    function(ut,v,e,s,m,p,o,f,r) {
        var pro;

        p._$$CustomModule = NEJ.C();
        pro = p._$$CustomModule._$extend(m._$$Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
			this.__onFileChange();
        };
        
        pro.__getNodes = function(){
            var list = e._$getByClassName('body','j-flag');
            this.label = list[0];
            this.folderSlt = list[1];
            this.size = list[2];
            this.imgCase = list[3];
            this.goods = list[4];
            this.custNode = list[5];
        };
        
        pro.__addEvent = function(){
           s._$bind(this.__label, {
                name: 'img',
                multiple: false,
                accept:'image/*',
                onchange: this.__onFileChange._$bind(this)
            });
          v._$addEvent(this.imgCase,'click',this.__onImageCaseClick._$bind(this)); 
        };
        pro.__onImageCaseClick = function(event){
        	var elm = v._$getElement(event);
        	if(elm.tagName =='IMG'){
        		this.__setCustomBackGround(elm.src,e._$dataset(elm,'width'),e._$dataset(elm,'height'))
        	}
        };
        
        pro.__setCustomBackGround = function(url,width,height){
        	var css = ['.cutbg{'];
        	css.push('background:#373c41 url('+url+') no-repeat center center;');
        	css.push('height:'+height +'px;')
        	css.push('}')
        	e._$appendCSSText(this.custNode,css.join(''));
        };
        pro.__onFileChange = function(_data){
        	this.url = '/res/images/cut.jpg';
        	var css = ['.cutbg{'];
        	css.push('background:#373c41 url('+this.url+') no-repeat center center;');
        	css.push('height:4381px')
        	css.push('}')
        	e._$appendCSSText(css.join(''));
        };
        p._$$CustomModule._$allocate();
    });