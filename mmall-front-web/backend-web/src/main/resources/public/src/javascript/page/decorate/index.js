/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js','{lib}base/event.js','{lib}base/element.js','{lib}util/file/select.js','{pro}widget/module.js'],
    function(ut,v,e,s,m,p,o,f,r) {
        var pro;

        p._$$IndexModule = NEJ.C();
        pro = p._$$IndexModule._$extend(m._$$Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
			this.__addEvent();
			this.__onFileChange();
        };
        
        pro.__getNodes = function(){
            this.__label = e._$get('uploadlbl');
            var list = e._$getByClassName('body','j-flag');
            this.uploadNode = list[0];
            this.decorateNode = list[1];
        };
        
        pro.__addEvent = function(){
           s._$bind(this.__label, {
                name: 'img',
                multiple: false,
                accept:'image/*',
                onchange: this.__onFileChange._$bind(this)
            });
        };

        pro.__onFileChange = function(_data){
        	this.url = '/res/images/cut.jpg';
        	var css = ['.cutbg{'];
        	css.push('background:#373c41 url('+this.url+') no-repeat center center;');
        	css.push('height:4381px')
        	css.push('}')
        	e._$appendCSSText(css.join(''));
        	
//        	s._$bind(_data.target, {
//                name: 'img',
//                accept:'image/*',
//                multiple: false,
//                onchange: this.__onFileChange._$bind(this)
//            });
        };
        p._$$IndexModule._$allocate();
    });