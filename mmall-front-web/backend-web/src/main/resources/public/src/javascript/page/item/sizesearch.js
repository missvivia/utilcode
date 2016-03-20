/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{pro}components/product/sizeList.js',
    '{pro}widget/form.js'
    ],
    function(_ut,v,e,Module,SizeList,_ut0,p) {
        var pro;

        p._$$SizeModule = NEJ.C();
        pro = p._$$SizeModule._$extend(Module);
        
        pro.__init = function(_options) {
            this.__supInit(_options);

            this.__form = _ut0._$$WebForm._$allocate({form:'webForm',
                onsubmit: this.__onSearch._$bind(this)
            })
			// this.__getNodes();
			// this.__addEvent();
        };

        pro.__onSearch = function(data){
            if(!this.__sizeList){
                data['lastId'] = 0;
                this.__sizeList = new SizeList({
                    data: {condition:data}
                }).$inject("#size-list-box");
            }else{
                var len = this.__sizeList.data.list.length;
                var _list = e._$getByClassName(document,'j-cate');
                if(len == 0){
                    data['lastId'] = 0;
                }else{
                    data['lastId'] = this.__sizeList.data.list[len-1].id;
                }
                //级联类目的最低级categoryId
                if(_list[2].value != 0){
                    data['lowCategoryId'] = _list[2].value;
                }else if(_list[1].value != 0){
                    data['lowCategoryId'] = _list[1].value;
                }else{
                    data['lowCategoryId'] = _list[0].value;
                }
                this.__sizeList.refresh(data);
            }
        }

        pro.__initSizeComponent = function(){
            if(!this.__sizeList){
                this.__sizeList = new SizeList({
                    data: {}
                }).$inject("#size-list-box");
            }
        }
        pro.__getNodes = function(){
            var _list = e._$getByClassName(document,'j-flag');
            this.__sizeSearch = _list[0];
        };
        
        pro.__addEvent = function(){
            v._$addEvent(this.__sizeSearch,'click',this.__onsearchBtnClick._$bind(this));
        };

        pro.__onsearchBtnClick = function(event){
            v._$stop(event);
            this.__initSizeComponent();
        }

        p._$$SizeModule._$allocate();
    });