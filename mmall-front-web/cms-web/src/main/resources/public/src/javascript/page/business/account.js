/**
 * 帐号页
 * author yuqijun(yuqijun@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    './account.list/list.js',
    '{pro}widget/module.js',
    '{pro}/widget/util/search.select.js'
    ],
    function(ut,v,e,List,Module,searchForm,p) {
        var pro;

        p._$$AccountModule = NEJ.C();
        pro = p._$$AccountModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);
			this.__getNodes();
            this.__addEvent();

            var _form = searchForm._$allocate({
		        form:'search-form',
		        onsearch:function(_data){
		        	_data["orderColumn"] = "indexWeight";
		        	if(!this.__list){
		        		this.__list = new List({data:{condition:_data}});
		        		this.__list.$inject('#accountlist');
		        		//this.__list.deleteBtn = this._deleteBtn;
		        	} else{
		        		this.__list.refresh(_data);
		        	}
		        }._$bind(this)
		    });
        };
        
        pro.__getNodes = function() {
            this._inputText = e._$get('search-text');
            this._searchList = e._$get('search-list');
            //this._deleteBtn = e._$get('deleteAll');
        }

        pro.__addEvent = function() {
            v._$addEvent(this._searchList,'change',this.__searchSelectChange._$bind(this));
            //v._$addEvent(this._deleteBtn,'click',this.__deleteSelectedAll._$bind(this));
            v._$addEvent(this._inputText,'keyup',this.__onKeyUp._$bind(this));
        }
        
        pro.__onKeyUp = function(event){
        	if(event.keyCode == 13){
        		e._$get('searchBtn').click();
        	}
        }

        pro.__searchSelectChange = function(){
            var index = this._searchList.value;
            if (index == 0) {
                this._inputText.name = 'businessId';
            } else if (index == 1) {
                this._inputText.name = 'account';
            }
        }

		pro.__deleteSelectedAll = function(){
			var table = e._$get('accountlist');
			var list = e._$getByClassName(table,'j-flag');
			for (var i=0;i<list.length;i++) {
                var checkbox = list[i];
                if (checkbox.checked) {
                    var index = checkbox.value;
                    var item = this.__list.data.list[index];
                    this.__list.del(item,index);
                }
            }
			
		};
		
        p._$$AccountModule._$allocate();
    });