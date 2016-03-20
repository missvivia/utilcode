/*
 * ------------------------------------------
 * 装修主模块
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/util',
    'base/event',
    'base/element',
    'util/cache/share',
    'util/sort/vertical',
    'util/dispatcher/module',
    'pro/extend/config',
    'pro/page/builder/util',
    'pro/page/builder/module',
    'pro/page/builder/widget',
    'pro/page/builder/cache/image',
    'pro/page/builder/cache/widget',
    'pro/page/builder/cache/product'
],function(_k,_u,_v,_e,_y,_z,_x,_1,_j,_m,_w,_n,_d,_h,_p,_o,_f,_r,_pro){
    /**
     * 装修主模块
     * @class   _$$Module
     * @extends _$$Module
     */
    _p._$$Module = _k._$klass();
    _pro = _p._$$Module._$extend(_m._$$Module);
    /**
     * 构建模块
     * @return {Void}
     */
    _pro.__doBuild = function(){
        this.__widgets = {};
        // init node
        this.__super({
            tid:'builder-module-main'
        });
        this.__mover = _e._$create('div','m-bd-dragger');
        this.__holder = _e._$create('div','m-bd-holder');
        this.__tpshow = _e._$create('div','m-bd-tip');
        // init event
        _v._$addEvent(
            this.__holder,'mousemove',
            _v._$stop._$bind(_v)
        );
        _v._$addEvent(
            document,'click',
            this.__onAction._$bind(this)
        );
        _v._$addEvent(
            document,'keydown',
            this.__onSaveCheck._$bind(this)
        );
        _v._$addEvent(
            document,'mousedown',
            this.__onResDragStart._$bind(this)
        );
        _v._$addEvent(
            document,'mousemove',
            this.__onResDraging._$bind(this)
        );
        _v._$addEvent(
            document,'mouseup',
            this.__onResDragEnd._$bind(this)
        );
        _v._$addEvent(
            _h._$$CacheProduct,'sortupdate',
            this.__doSaveLayout._$bind(this)
        );
        _v._$addEvent(
            window,'beforeunload',
            this.__onBeforeLeave._$bind(this)
        );
        // init widget
        this.__sorter = _z._$$VSortable._$allocate({
            clazz:'j-widget',
            trigger:'j-move',
            parent:this.__body,
            placeholder:_e._$create('div','j-holder'),
            thumbnail:_e._$create('div','m-bd-dragger'),
            onthumbupdate:this.__onSortThumbUpdate._$bind(this),
            onholderupdate:this.__onSortHolderUpdate._$bind(this)
        });
        this.__wcache = _d._$$CacheWidget._$allocate({
            onsave:this.__cbSave._$bind(this),
            onerror:this.__cbError._$bind(this),
            onpublish:this.__cbPublish._$bind(this)
        });
    };
    /**
     * 显示模块
     * @param {Object} _options
     */
    _pro.__onShow = function(_options){
        this.__super(_options);
        // get layout config
        var _layout = _d._$do(function(_cache){
            return _cache._$getLayout();
        }).layout;
        // build layout
        _u._$forEach(
            _layout,function(_conf){
                // background widget layout out from page body
                if (_conf.type=='a'){
                    var _parent = this.__body;
                    _conf.parent = function(_body){
                        _parent.insertAdjacentElement('beforeBegin',_body);
                        return _parent;
                    };
                }
                // allocate widget
                this.__doAppendWidget(_conf);
            },this
        );
    };
    /**
     * 离开装修提示
     * @return {Void}
     */
    _pro.__onBeforeLeave = function(_event){
        
    };
    /**
     * 添加组件
     * @return {Void}
     */
    _pro.__doAppendWidget = (function(){
        var _doShowMessage = function(_event){
            _j._$showMessage(_event.message,'alert-info');
        };
        return function(_conf){
            var _options = {
                parent:this.__body,
                onmessage:_doShowMessage,
                onerror:this.__onError._$bind(this),
                onremove:this.__onWidgetRemove._$bind(this)
            };
            var _inst = _w._$get(_conf.type)._$allocate(
                _u._$merge(_options,_conf)
            );
            this.__widgets[_inst._$getId()] = _inst;
        };
    })();
    /**
     * 组件删除事件
     * @return {Void}
     */
    _pro.__onWidgetRemove = function(_event){
        delete this.__widgets[_event.id];
    };
    /**
     * 错误信息处理
     * @return {Void}
     */
    _pro.__onError = function(_error){
        _j._$showMessage(_error.message);
    };
    /**
     * 导出布局配置信息
     * @return {Void}
     */
    _pro.__doDumpLayout = (function(){
        var _dump = function(_xmap,_conf,_test){
            // for image
            var _arr = _xmap.udImgIds,
                _map = _test.i;
            _u._$forEach(
                _conf.bannerIds,function(_id){
                    if (!!_id&&!_map[_id]){
                        _arr.push(_id);
                        _map[_id] = !0;
                    } 
                }
            );
            // for product
            var _arr = _xmap.udProductIds,
                _map = _test.p;
            _u._$forEach(
                _conf.productIds,function(_id){
                    if (!!_id&&!_map[_id]){
                        _arr.push(_id);
                        _map[_id] = !0;
                    } 
                }
            );
        };
        var _fmap = {
            2:function(_xmap,_conf,_test){
                // for image
                var _id = _conf.imgId,
                    _map = _test.i;
                if (!!_id&&!_map[_id]){
                    _map[_id] = !0;
                    _xmap.udImgIds.push(_id);
                }
                // for product
                var _arr = _xmap.udProductIds,
                    _map = _test.p;
                _u._$forEach(
                    _conf.hotspots,function(_data){
                        var _id = _data.id;
                        if (!!_id&&!_map[_id]){
                            _map[_id] = !0;
                            _arr.push(_id);
                        }
                    }
                );
            },
            3:_dump,
            4:_dump,
            5:_dump,
            6:_dump
        };
        return function(){
            // dump widget body list
            var _list = _e._$getByClassName(
                this.__body.parentNode,'j-layout'
            );
            // dump layout config
            var _test = {i:{},p:{}};
            var _ret = {
                udImgIds:[],
                udProductIds:[],
                udSetting:[]
            };
            _u._$forEach(
                _list,function(_node){
                    var _id = _e._$dataset(_node,'id'),
                        _inst = this.__widgets[_id];
                    if (!!_inst){
                        var _conf = _inst._$getLayout(),
                            _type = _inst._$getType();
                        // merge config
                        if (isNaN(parseInt(_type))){
                            _u._$merge(_ret,_conf);
                        }else{
                            _conf.id = _id;
                            _conf.type = _type;
                            _ret.udSetting.push(_conf);
                            // dump banner and product
                            var _func = _fmap[_type];
                            if (!!_func){
                                _func(_ret,_conf,_test);
                            }
                        }
                    }
                },this
            );
            // format data
            var _imgs = _ret.udImgIds,
                _urls = _n._$do(function(_cache){
                    return _cache._$dump(_imgs);
                });
            _ret.udImgIds = _imgs.join(',');
            _ret.udProductIds = _ret.udProductIds.join(',');
            _ret.udSetting = JSON.stringify(_ret.udSetting);
            _ret.mapPartOthers = JSON.stringify(_urls);
            
            return _ret;
        };
    })();
    /**
     * 判断布局信息是否符合条件
     * @return {Void}
     */
    _pro.__doCheckLayout = (function(){
        var _fmap = {
            2:function(_conf){
                // check banner image
                if (!_conf.imgId){
                    return {
                        data:_conf,
                        message:'BANNER组件未设置图片'
                    };
                }
                // check hotspot
                var _result;
                _u._$forIn(
                    _conf.hotspots,function(_holder){
                        if (!_holder.id){
                            _result = {
                                data:_conf,
                                holder:_holder,
                                message:'BANNER组件自定义热区内有热区未添加链接'
                            };
                            return !0;
                        }
                    }
                );
                return _result;
            },
            3:_isProductOK,
            4:_isProductOK,
            5:function(_conf){
                // check banner
                if (!(_conf.bannerIds||_r)[0]){
                    return {
                        data:_conf,
                        message:'商品组件内有BANNER图未添加'
                    };
                }
                return _isProductOK(_conf);
            },
            6:_isProductOK
        };
        var _isProductOK = function(_conf){
            var _result;
            var _images = _conf.bannerIds||_r;
            _u._$reverseEach(
                _conf.productIds,function(_id,_index){
                    if (!!_images[_index]&&!_id){
                        _result = {
                            data:_conf,
                            index:_index,
                            message:'商品组件内有BANNER未添加商品'
                        };
                        return !0;
                    }
                }
            );
            return _result;
        };
        return function(_layout){
            var _result = null,
                _list = JSON.parse(_layout.udSetting);
            // check ud setting
            _u._$forIn(
                _list,function(_conf){
                    var _func = _fmap[_conf.type];
                    if (!!_func){
                        _result = _func(_conf);
                        return !!_result;
                    }
                }
            );
            return _result;
        };
    })();
    /**
     * 更新布局信息
     * @return {Void}
     */
    _pro.__doSaveLayout = function(){
        this.__wcache._$save(
            this.__doDumpLayout()
        );
    };
    /**
     * 取消
     * @return {Void}
     */
    _pro.__doCancel = function(){
        location.href = '/schedule/pages';
    };
    /**
     * CTRL+S保存
     * @return {Void}
     */
    _pro.__onSaveCheck = function(_event){
        if (_event.ctrlKey&&_event.keyCode==83){
            _v._$stop(_event);
            this.__doSaveLayout();
        }
    };
    /**
     * 操作行为检测
     * @return {Void}
     */
    _pro.__onAction = (function(){
        var _doScrollTo = function(_id){
            var _parent = this.__body.parentNode,
                _list = _e._$getByClassName(_parent,'j-layout');
            _u._$forIn(
                _list,function(_node){
                    if (_id==_e._$dataset(_node,'id')){
                        _parent.scrollTop = 
                            _e._$offset(_node,_parent).y-50;
                        return !0;
                    }
                }
            );
        };
        return function(_event){
            var _node = _v._$getElement(_event,'d:do');
            if (!_node) return;
            switch(_e._$dataset(_node,'do')){
                case 'back':
                    this.__doCancel();
                break;
                case 'save':
                    this.__doSaveLayout();
                break;
                case 'submit':
                    var _layout = this.__doDumpLayout(),
                        _result = this.__doCheckLayout(_layout);
                    if (!_result){
                        this.__wcache._$publish(_layout);
                    }else{
                        // scroll to widget and show message
                        _doScrollTo.call(this,_result.data.id);
                        _j._$showMessage(_result.message+'，请检查');
                    }
                break;
                case 'preview':
                    var _form = _node.parentNode,
                        _layout = this.__wcache._$getLayout(),
                        _data = this.__doDumpLayout();
                    _u._$merge(_data,{
                        preview:!0,
                        id:_layout.id,
                        brandId:_layout.brandId,
                        scheduleId:_layout.scheduleId
                    });
                    _form.action = _1.MAINSITE+'/schedule/preview';
                    _form.pageId.value = _layout.id;
                    _form.layout.value = JSON.stringify(_data);
                    _form.submit();
                break;
            }
        };
    })();
    /**
     * 保存回调
     * @return {Void}
     */
    _pro.__cbSave = function(){
        _j._$showMessage('保存成功！','alert-success');
    };
    /**
     * 发布回调
     * @return {Void}
     */
    _pro.__cbPublish = function(){
        //_j._$showMessage('发布成功！');
        this.__doCancel();
    };
    /**
     * 错误回调
     * @return {Void}
     */
    _pro.__cbError = (function(){
        var _msgs = {
            onsave:'保存失败',
            onpublish:'发布失败'
        };
        return function(_event){
            _j._$showMessage(_msgs[_event.type]);
        };
    })();
    /**
     * 排序缩略图更新
     * @return {Void}
     */
    _pro.__onSortThumbUpdate = (function(){
        var _fmap = {
            1:function(_event){
                var _widget = this.__wcache._$getItemInCache(
                    _e._$dataset(_event.source,'type')
                );
                if (!!_widget){
                    _event.target.innerHTML = '<img src="'+_widget.thumb+'"/>';
                }
            },
            2:function(_event){
                _event.top -= 30;
                _event.left -= 30;
            },
            3:function(_event){
                _event.target.innerHTML = '&nbsp;';
            }
        };
        return function(_event){
            (_fmap[_event.state]||_f).call(this,_event);
        };
    })();
    /**
     * 排序占位符更新
     * @return {Void}
     */
    _pro.__onSortHolderUpdate = (function(){
        var _fmap = {
            beforeBegin:function(_event){
                var _height = _doAdjustPosition(
                    _event,_event.ref
                );
                _event.top += _height;
            },
            afterEnd:function(_event){
                _doAdjustPosition(
                    _event,_e._$getSibling(_event.ref)
                );
            }
        };
        var _doAdjustPosition = function(_event,_element){
            _event.left += (_event.source.offsetWidth-
                            _event.target.offsetWidth)/2;
            var _space = _j._$getNumber(
                    _element,'paddingTop'
                ),
                _height = _event.target.offsetHeight;
            _event.top += (_space-_height)/2;
            return _height;
        };
        return function(_event){
            (_fmap[_event.position]||_f).call(this,_event);
        };
    })();
    /**
     * 显示拖拽占位符
     * @return {Void}
     */
    _pro.__doShowDragMover = (function(){
        var _dcls = {
            1:'m-bd-dragger-img',
            2:'m-bd-dragger-prd',
            3:'m-bd-dragger-wgt'
        };
        var _fmap = {
            1:function(_id){
                var _img = _n._$do(function(_cache){
                    return _cache._$getItemInCache(_id);
                });
                if (!!_img){
                    var _url = _img.imgUrl||'';
                    return _url+(_url.indexOf('?')<0?'?':'&')+'imageView&thumbnail=80x80';
                }
            },
            2:function(_id){
                var _product = _h._$do(function(_cache){
                    return _cache._$getItemInCache(_id);
                });
                if (!!_product){
                    var _url = _product.listShowPicList[0]||'';
                    if (!!_url){
                        return _url+(_url.indexOf('?')<0?'?':'&')+'imageView&thumbnail=64x80';
                    }
                }
            },
            3:function(_id){
                var _widget = _d._$do(function(_cache){
                    return _cache._$getItemInCache(_id);
                });
                return _widget.thumb;
            }
        };
        return function(_res,_pointer){
            var _type = _res.type;
            _e._$style(
                this.__mover,{
                    top:_pointer.y-30+'px',
                    left:_pointer.x-30+'px'
                }
            );
            _e._$addClassName(
                this.__mover,_dcls[_type]
            );
            document.body.appendChild(this.__mover);
            // thumbnail has been setted
            if (!!this.__mover.getElementsByTagName('img')[0]){
                return;
            }
            // show thumbnail
            var _url = (_fmap[_type]||_f).call(null,_res.id);
            if (!!_url){
                this.__mover.innerHTML = '<img src="'+_url+'"/>';
            }
        };
    })();
    /**
     * 显示间距
     * @return {Void}
     */
    _pro.__doShowSpaceMover = function(_text,_pointer){
        _e._$style(
            this.__tpshow,{
                top:_pointer.y-30+'px',
                left:_pointer.x-10+'px'
            }
        );
        this.__tpshow.innerText = _text;
        document.body.appendChild(this.__tpshow);
    };
    /**
     * 还原容器状态
     * @return {Void}
     */
    _pro.__doRevertBoxState = function(){
        if (!!this.__argc){
            _e._$delClassName.apply(
                _e,this.__argc
            );
            delete this.__argc;
        }
        _e._$removeByEC(this.__holder);
    };
    /**
     * 清除拖拽占位
     * @return {Void}
     */
    _pro.__doHideDragMH = function(_type){
        this.__mover.innerHTML = '&nbsp;';
        this.__mover.className = 'm-bd-dragger';
        _e._$removeByEC(this.__mover);
    };
    /**
     * 资源开始拖拽判断
     * @param {Object} _event
     */
    _pro.__onResDragStart = (function(){
        var _xfrm = {
            x1:function(_conf,_delta){
                var _list = _e._$getChildren(_conf.node.parentNode),
                    _width = _j._$getNumber(_list[0],'width');
                _e._$setStyle(_list[0],'width',_width+_delta.x+'px');
                // check limit
                _width = _list[0].offsetWidth;
                _e._$setStyle(_conf.node,'left',_width-3+'px');
                _e._$setStyle(_list[0],'marginRight',-1*_width+'px');
                _e._$setStyle(_list[1],'marginLeft',_width-1+'px');
                _e._$setStyle(_list[2],'marginLeft',_width-1+'px');
            }
        };
        var _amap = {
            boxAdjust:function(_value,_node,_event){
                _v._$stop(_event);
                // height:x or paddingTop:x.x
                var _arr = _value.split(':');
                this.__adjust = {
                    y:_v._$clientY(_event),
                    attr:_arr[0].split(','),
                    parent:_getGapParent(_node,_arr[1])
                };
                this.__doShowSpaceMover(
                    _e._$getStyle(
                        this.__adjust.parent,
                        this.__adjust.attr[0]
                    ),{
                        x:_v._$clientX(_event),
                        y:this.__adjust.y
                    }
                );
            },
            boxFrame:function(_value,_node,_event){
                _v._$stop(_event);
                var _func = _xfrm[_value];
                if (!_func) return;
                this.__ofit = {
                    x:_v._$clientX(_event),
                    y:_v._$clientY(_event),
                    node:_node, func:_func
                };
            },
            resType:function(_value,_node,_event){
                if (_e._$hasClassName(_node,'j-disabled')){
                    return;
                }
                // cache resource info
                _v._$stop(_event);
                var _xres = {
                    id:_e._$dataset(_node,'resId'),
                    type:_e._$dataset(_node,'resType'),
                    data:_e._$dataset(_node,'resData')
                };
                _y.localCache._$set('resource',_xres);
                // show mover holder
                this.__doShowDragMover(
                    _xres,{
                        x:_v._$clientX(_event),
                        y:_v._$clientY(_event)
                    }
                );
            }
        };
        var _getGapParent = function(_node,_parent){
            var _arr = _parent.split('.');
            while(_arr.pop()){
                _node = _node.parentNode;
            }
            return _node;
        };
        return function(_event){
            // disable image draggable
            var _node = _v._$getElement(_event);
            if (_node.tagName=='IMG'){
                _v._$stopDefault(_event);
            }
            // check action config
            _u._$forIn(
                _amap,function(_func,_name){
                    var _node = _v._$getElement(_event,'d:'+_name);
                    if (!!_node){
                        _func.call(
                            this,_e._$dataset(_node,_name),
                            _node,_event
                        );
                        return !0;
                    }
                },this
            );
        };
    })();
    /**
     * 资源拖拽过程状态判断
     * @param {Object} _event
     */
    _pro.__onResDraging = (function(){
        var _fmap = {
            boxMatch:function(_match,_xres,_node,_event){
                // check box state
                this.__argc = [_node];
                if (_match.indexOf(''+_xres.type)<0){
                    // check disable
                    this.__argc.push('j-disable');
                }else{
                    // check replace
                    var _rid = _e._$dataset(
                        _node,'boxT'+_xres.type
                    );
                    this.__argc.push(
                        !_rid?'j-droppable':'j-replacable'
                    );
                }
                // update state
                if (!!this.__argc){
                    _e._$addClassName.apply(
                        _e,this.__argc
                    );
                }
            },
            boxInsert:function(_insert,_xres,_node,_event){
                // only for widget
                if (_xres.type!=3) return;
                var _oh = _node.offsetHeight,
                    _oy = _e._$offset(_node,this.__body).y,
                    _dy = _v._$pageY(_event)-_e._$offset(_node).y;
                // check position
                var _half = _oh/2;
                _xres.element = _node;
                if (_insert=='top'||(_insert=='both'&&_dy<=_half)){
                    _xres.position = 'beforeBegin';
                }else if(_insert=='bottom'||(_insert=='both'&&_dy>_half)){
                    _xres.position = 'afterEnd';
                    _oy = _oy+_oh;
                }
                // update place holder
                if (!_xres.position){
                    delete _xres.element;
                }else{
                    this.__body.appendChild(this.__holder);
                    _e._$style(
                        this.__holder,{
                            top:_getHolderTop(_node,_xres.position,_oy,this.__holder.offsetHeight)+'px',
                            left:Math.floor((this.__body.clientWidth-this.__holder.offsetWidth)/2)+'px'
                        }
                    );
                }
            }
        };
        var _getHolderTop = function(_node,_position,_oy,_oh){
            if (_position=='beforeBegin'){
                var _space = _j._$getNumber(_node,'paddingTop');
                return _oy+(_space-_oh)/2;
            }else{
                var _space = _j._$getNumber(
                    _e._$getSibling(_node),'paddingTop'
                );
                return _oy+(_space-_oh)/2;
            }
        };
        return function(_event){
            var _xx = _v._$clientX(_event),
                _yy = _v._$clientY(_event);
            // check style adjust
            if (!!this.__ofit){
                var _dx = _xx-this.__ofit.x,
                    _dy = _yy-this.__ofit.y;
                this.__ofit.x = _xx;
                this.__ofit.y = _yy;
                this.__ofit.func(
                    this.__ofit,{
                        x:_dx,y:_dy
                    }
                );
                return;
            }
            // check adjust
            if (!!this.__adjust){
                var _dy = _yy-this.__adjust.y;
                this.__adjust.y = _yy;
                // adjust margin top
                var _value = _e._$getStyle(
                    this.__adjust.parent,
                    this.__adjust.attr[0]
                );
                _value = Math.max(0,(parseInt(_value)||0)+_dy)+'px';
                _u._$forEach(
                    this.__adjust.attr,function(_name){
                        _e._$setStyle(this.__adjust.parent,_name,_value);
                    },this
                );
                this.__doShowSpaceMover(_value,{
                    x:_xx,y:_yy
                });
                return;
            }
            // check dragging resouce
            this.__doRevertBoxState();
            var _xres = _y.localCache._$get('resource');
            if (!_xres) return;
            // remove last position
            delete _xres.element;
            delete _xres.position;
            // update mover holder
            this.__doShowDragMover(
                _xres,{x:_xx,y:_yy}
            );
            // check action
            _u._$forIn(
                _fmap,function(_func,_name){
                    var _node = _v._$getElement(_event,'d:'+_name);
                    if (!!_node){
                        _func.call(
                            this,_e._$dataset(
                                _node,_name
                            ),_xres,_node,_event
                        );
                    }
                },this
            );
        };
    })();
    /**
     * 资源拖拽结束清理临时缓存
     * @param {Object} _event
     */
    _pro.__onResDragEnd = function(_event){
        // style adjust
        if (!!this.__ofit){
            delete this.__ofit;
            return;
        }
        // widget space adjust
        if (!!this.__adjust){
            delete this.__adjust;
            _e._$removeByEC(this.__tpshow);
            return;
        }
        // insert new widget
        var _xres = _y.localCache._$get('resource');
        if (!!_xres&&_xres.type==3&&!!_xres.element){
            var _parent = _xres.element,
                _pos = _xres.position;
            this.__doAppendWidget({
                type:_xres.id,
                parent:function(_node){
                    _parent.insertAdjacentElement(
                        _pos,_node
                    );
                    return _node.parentNode;
                }
            });
        }
        // release resource
        this.__doHideDragMH();
        this.__doRevertBoxState();
        _y.localCache._$remove('resource');
    };
    
    // regist module
    _x._$regist('main',_p._$$Module);
});
