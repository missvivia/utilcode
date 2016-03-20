NEJ.define([
    'pro/extend/util',
    'base/element',
    'base/util',
    'text!./articleList.html',
    'pro/widget/BaseComponent',
    'pro/components/pager/ucenterpager'
],function(_,_e,_u,_html,BaseComponent,ucenterpager){
    return BaseComponent.extend({
    	config: function(data){
	      _.extend(data, {
	        selectedId:""
	      });
	    },
        url:'/help/article',
        template:_html,
        toggle:function(item){
        	item.flag = !item.flag;
        },
        init: function(){
            if(!this.url) throw "ListModule未指定url";

            // 需要自定义复杂的更新策略, $emit('updatelist')事件即可
            this.$on("updatelist", this.__getList.bind(this));
            this.__getList();

          },
          // @子类修改
          shouldUpdateList: function(data){
            return true;
          },
          getExtraParam:function(){
            return this.data.condition;
          },
          refresh:function(_data){
        	this.data.selectedId = _data.selectedId;
            this.data.condition = _data.condition;
            this.data.requestId = _data.requestId;
            this.$emit('updatelist');
          },
          // update loading
          __getList: function(){
            var data = this.data;
            this.$request(this.url + '?categoryId=' + data.requestId, {
              //data: this.getExtraParam(),
              method:'get',
              onload: function(json){
                var result = json.result||{};
                data.articleList = result.list||[];
                data.title = !!data.articleList[0]?data.articleList[0].categoryName:"";
                for(var i=0;i<data.articleList.length;i++){
                	data.articleList[i].content = _._$unescape(data.articleList[i].content);
                  if(data.selectedId!=""){
                    data.selectedId==data.articleList[i].id?(data.articleList[i].flag =!0):(data.articleList[i].flag =!1);
                  }else{
                    data.articleList[i].flag =!0;
                  }
                }    
                this.$update();
              },
              // test
              onerror: function(json){
                // @TODO: remove
              }
            });
          }
    });
});