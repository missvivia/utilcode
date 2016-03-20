/**
 * xx平台首页
 * author xxxx(xxxx@corp.netease.com)
 */

define(['{lib}base/util.js',
    '{lib}base/event.js',
    '{lib}base/element.js',
    '{pro}widget/module.js',
    '{lib}util/template/jst.js',
    'util/form/form',
    'util/selector/cascade',
    '{pro}components/product/sizeTmp.js',
    'util/ajax/xdr',
    'pro/components/notify/notify',
    'pro/extend/request'
    ],
    function(_ut,_v,_e,Module,e2,_t,_t1,SizeTmp,j,notify,Request,p) {
        var pro;

        window.category = __sizeTemplateVO__.categoryList;
        
        p._$$SizeTmpModule = NEJ.C();
        pro = p._$$SizeTmpModule._$extend(Module);
        
        pro.__init = function(_options) {
            _options.tpl = 'jstTemplate';
            this.__supInit(_options);
            this.__initForm();
            this.__getNodes();
			this.__addEvent();
        };
        pro.__initForm = function(){
            this.__form = _t._$$WebForm._$allocate({
                form:'webForm'
            });
            this.__initSelector();
        };
        pro.__initSelector = function(){
            if(!!this.__selector){
                this.__selector._$recycle();
            }
            var _list = _e._$getByClassName('webForm','j-cate');
            if (!!_list&&_list.length>0){
                this.__selector = _t1._$$CascadeSelector._$allocate({
                    select:_list,
                    data:window.category,
                    onchange:this.__getTmpData._$bind(this)
                });
                //初始化值
                // if(!!__sizeTemplateVO__.categories){
                //     this.__selector._$update(__sizeTemplateVO__.categories);
                // }
            }
        };
        pro.__onSubmit = function(_event){
            _v._$stop(_event);
            if(this.__form._$checkValidity()){
                var _data = this.__form._$data();
                _data['id'] = __sizeTemplateId__;
                _data['sizeTable'] = this.__sizeTable._$getData();
                if(this.__checkSize(_data['sizeTable'])){
                    this.__saveBtn.disabled = 'disabled';
                    Request('/rest/sizeTemplate/save',{
                        method:'POST',
                        data:_data,
                        onload:function(json){
                            notify.show('成功');
                            location.href = '/product/size';
                        },
                        onerror:function(e){
                            notify.showError('失败');
                        }
                    });
                }
            }
        };
        pro.__checkSize = function(_data){
            for(var i=0,len=_data.header.length;i<len;i++){
                if(!!_data.header[i].required){
                    for(var j=0,len2=_data.body.length;j<len2;j++){
                        if(!_data.body[j][i]){
                            notify.show({
                                'type':'error',
                                'message':'请在尺码模版中输入必填项！'
                            })
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        pro.__getTmpData = function(_event){
            var _data = {},sizeTable;
            _data['lowCategoryId'] = parseInt(_event.lowCategoryId);
            _data['sizeTemplateId'] = __sizeTemplateId__;
            j._$request('/rest/sizeTemplate/getSizeTemplateTable',{
                query:_data,
                data:{t:+new Date},
                type:'json',
                method:'GET',
                onload:this.__renderSizeTmp._$bind(this),
                onerror:function(e){
                    notify.showError('获取模版失败');
                }
            });
            // this.__sizeList = sizeTable;
        };
        pro.__checkView = function(){
            var _list = _e._$getByClassName(document.body,'j-view');
            for(var i=0,len=_list.length;i<len;i++){
                _list[i].disabled = 'disabled';
            }
        }
        pro.__getNodes = function(){
            this.__sizeTmpBox = _e._$get('size-tmp-box');
            var _list = _e._$getByClassName('pagebox','j-flag');
            this.__saveBtn = _list[0];
        };
        pro.__addEvent = function(){
            _v._$addEvent(this.__saveBtn,'click',this.__onSubmit._$bind(this));
        };
        pro.__renderSizeTmp = function(json){
            if(!!this.__sizeTable){
                this.__sizeTable._$recycle();
            }
            this.__sizeTable = SizeTmp._$allocate({parent:this.__sizeTmpBox,data:json.result});
            if(!!window.__view__){
                this.__checkView();
            }
        };

        p._$$SizeTmpModule._$allocate();
    });