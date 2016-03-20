NEJ.define([
    'pro/widget/util/fav',
    'pro/extend/util',
    'base/element',
    'base/util',
    'text!./list.html',
    'pro/components/ListComponent',
    'pro/components/pager/ucenterpager',
    'pro/widget/layer/alert/alert',
    'pro/widget/layer/login/login',
    'pro/components/window/base'
],function(fav,_,_e,_u,_html,ListComponent,ucenterpager,at,login,Modal){
    
    return ListComponent.extend({
        watchedAttr: ['current','type'],
        config: function(data){
            _.extend(data, {
              total: 1,
              current: 1,
              limit: 16,
              type:"all",
              list: []
            });
            this.$watch(this.watchedAttr, function(){
              if(this.shouldUpdateList()) this.__getList();
            })
        },
        url:'/brand/settlelist.json',
        template:_html,
        getExtraParam: function(data){
            return {type: data.type};
        },
        getListByType:function(_type){
        	this.data.current = 1;
        	this.data.type = _type;
        },
        focus: function(item,ev){
        	if(!(_.isLogin())){
        		login._$$LoginWindow._$allocate({parent:document.body})._$show();
        		return;
        	}
        	var url = item.favorited?'/brand/unfollow.json':'/brand/follow.json';
        	this.$request(url, {
                data:{brandId:item.brandId} ,
                method:'post',
                onload: function(json){
                    if(!item.favorited){
                        // new Modal({
                        //     content: "<h2><span class='u-success'></span>关注成功</h2>"+
                        //         "<p>将为您推送相关品牌折扣信息</p>",
                        //     data: {
                        //         clazz: "u-win-follow",
                        //         confirmText: "确 定"
                        //     }
                        // });
                            //fav._$doFav(ev.target, {duration: 1});
                    }
                	this.$update(function(){
                		item.favorited = !(item.favorited);
                	})
                },
                // test
                onerror: function(json){
                  at._$$AlertWindow._$allocate({text:"网络异常，请稍后再试！"})._$show();
                }

            });
        }
        
    });
});