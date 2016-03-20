NEJ.define([
        	'{lib}base/util.js',
        	'{lib}base/event.js',
        	'{lib}base/element.js',
            'text!./proxylist.html?v=1.0.0.1',
            '{pro}components/MmallListComponent.js',
            '{pro}components/notify/notify.js'
],function(ut,v,e,_html,ListComponent,notify){

    var proxyList = ListComponent.extend({
        url:'/userInfo/proxy/searchProxyUser',
        template:_html,
	    refresh:function(_data){
	    	if (!!_data.url){
	            this.url = _data.url;
	            delete _data.url;
	        }
	        this.data.condition = _data;
	        this.$emit('updatelist');
	    }
    });

    return proxyList;
});