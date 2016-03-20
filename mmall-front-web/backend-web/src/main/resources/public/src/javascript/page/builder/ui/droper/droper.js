/*
 * ------------------------------------------
 * 拖拽容器控件
 * @version  1.0
 * @author   genify(caijf@corp.netease.com)
 * ------------------------------------------
 */
NEJ.define([
    'base/klass',
    'base/event',
    'base/element',
    'base/util',
    'base/constant',
    'ui/base',
    'util/template/tpl',
    'util/cache/share',
    '../../util.js',
    '../../widget.js',
    '../../cache/image.js',
    '../../cache/product.js',
    'text!./droper.html'
],function(_k,_v,_e,_u,_g,_i,_l,_d,_y,_x,_md,_pd,_html,_p,_o,_f,_r,_pro){
    /**
     * 拖拽容器控件
     */
    _p._$$Droper = _k._$klass();
    _pro = _p._$$Droper._$extend(_i._$$Abstract);
    /**
     * 控件重置
     * @return {Void}
     */
    _pro.__reset = function(_options){
        this.__super(_options);
        // init event
        this.__doInitDomEvent([[
            this.__ndel,'click',
            this.__onResRemove._$bind(this)
        ],[
            this.__body,'mouseup',
            this.__onResDrop._$bind(this)
        ],[
            this.__ntst,'load',
            this.__onBannerLoaded._$bind(this,!0)
        ],[
            this.__ntst,'error',
            this.__onBannerLoaded._$bind(this,!1)
        ]]);
        // reset data
        _u._$forIn(
            _options.dataset,function(_value,_key){
                _e._$dataset(this.__body,_key,''+_value);
            },this
        );
        var _dataset = _options.dataset||_o;
        this.__limit = _options.limit;
        this.__match = ''+_dataset.boxMatch;
        this.__ndel.title = _options.title||'删除';
        // update tip and resource
        this._$updateTip(_options.tip);
        this.__doUpdateMatchData(_dataset);
    };
    /**
     * 控件销毁
     * @return {Void}
     */
    _pro.__destroy = function(){
        _u._$safeDelete(this.__ntst,'res');
        this.__ntst.src = _g._$BLANK_IMAGE;
        this.__onResRemove();
        this.__super();
    };
    /**
     * 初始化外观
     * @return {Void} 
     */
    _pro.__initXGui = (function(){
        var _seed_html = _l._$addNodeTemplate(_html);
        return function(){
            this.__seed_html = _seed_html;
        };
    })();
    /**
     * 初始化结构
     * @return {Void}
     */
    _pro.__initNode = function(){
        this.__super();
        // 0 - remove button
        // 1 - tip show
        var _list = _e._$getChildren(this.__body);
        this.__ndel = _list[0];
        this.__ntip = _list[1];
        this.__nimg = _e._$create('img');
        this.__ntst = _e._$create('img');
    };
    /**
     * 更新关联数据
     * @return {Void}
     */
    _pro.__doUpdateMatchData = function(_conf){
        _u._$forEach(
            this.__match.split(','),function(_type){
                var _key = 'boxT'+_type,
                    _rid = _conf[_key];
                if (!!_rid){
                    this.__doUpdateResource({
                        id:_rid,type:_type
                    });
                }else{
                    _e._$dataset(this.__body,_key,'');
                }
            },this
        );
    };
    /**
     * 取图片地址
     * @return {Void}
     */
    _pro.__getImageURL = function(_res){
        var _url,_id = _res.id;
        switch(parseInt(_res.type)){
            case 1:
                var _image = _md._$do(function(_cache){
                    return _cache._$getItemInCache(_id);
                });
                _url = (_image||_o).imgUrl;
            break;
            case 2:
                var _product = _pd._$do(function(_cache){
                    return _cache._$getItemInCache(_id);
                });
                _url = ((_product||_o).listShowPicList||_r)[0];
            break;
        }
        return _url;
    };
    /**
     * 更新图片地址
     * @return {Void}
     */
    _pro.__doUpdateResource = function(_event){
        _event.url = this.__getImageURL(_event);
        this._$dispatchEvent('ondrop',_event);
        if (!!_event.cancel){
            return;
        }
        if (!!_event.stopped){
            // save resource info
            _e._$dataset(
                this.__body,
                'boxT'+_event.type,
                _event.id
            );
            return;
        }
        this._$resumeImage(_event);
    };
    /**
     * 清除资源保留容器大小
     * @return {Void}
     */
    _pro.__doClearResWithoutSize = function(){
        // clear dataset
        this.__doUpdateMatchData(_o);
        // clear image
        _e._$delClassName(this.__body,'j-hasimg');
        this.__nimg.src = _g._$BLANK_IMAGE;
        _e._$removeByEC(this.__nimg);
    };
    /**
     * 清除资源
     * @return {Void}
     */
    _pro.__doClearResource = function(){
        this.__doClearResWithoutSize();
        // clear box size
        _e._$style(
            this.__body,{
                width:'',
                height:'',
                lineHeight:''
            }
        );
    };
    /**
     * BANNER图片加载成功
     * @return {Void}
     */
    _pro.__onBannerLoaded = (function(){
        var _mopt = {
            width:'图片宽度必须',
            height:'图片高度必须'
        };
        var _isOK = function(_size,_limit){
            if (!_u._$isArray(_limit)){
                return !_limit||_size==_limit;
            }
            if (!_limit[0]){
                return _size<=_limit[1];
            }else if(!_limit[1]){
                return _size>=_limit[0];
            }
            return _limit[0]<=_size&&_size<=_limit[1];
        };
        var _isLimitOK = function(_box){
            var _key = _u._$forIn(_box,function(_size,_name){
                return !_isOK(_size,this.__limit[_name]);
            },this);
            if (!!_key){
                this._$dispatchEvent(
                    'onerror',{
                        size:_box[_key],
                        code:_x.UNMATCH_SIZE,
                        message:_mopt[_key]+
                                _y._$limit2str(this.__limit[_key])
                    }
                );
            }
            return !_key;
        };
        return function(_isok){
            var _blank = _g._$BLANK_IMAGE;
            // check blank image
            if (this.__ntst.src.indexOf(_blank)>=0){
                return;
            }
            // save image info 
            var _iurl = this.__ntst.src,
                _ires = this.__ntst.res,
                _ibox = {
                    width:this.__ntst.naturalWidth||this.__ntst.width,
                    height:this.__ntst.naturalHeight||this.__ntst.height
                };
            this.__ntst.src = _blank;
            _u._$safeDelete(this.__ntst,'res');
            // check error
            if (!_isok){
                this._$dispatchEvent(
                    'onerror',{
                        code:_x.FAILD_LOAD_RES,
                        message:'图片载入失败'
                    }
                );
                return;
            }
            // check limitation
            if (!_isLimitOK.call(this,_ibox)){
                return;
            }
            // update show
            this._$showImage(_iurl);
            _e._$dataset(
                this.__body,
                'boxT'+_ires.type,
                _ires.id
            );
            _e._$setStyle(
                this.__body,'height',
                _ibox.height+'px'
            );
            this._$dispatchEvent(
                'onimageok',{
                    url:_iurl,
                    width:_ibox.width,
                    height:_ibox.height
                }
            );
        };
    })();
    /**
     * 清除放入的资源
     * @return {Void}
     */
    _pro.__onResRemove = function(){
        var _event = this._$getResource();
        this._$dispatchEvent('onremove',_event);
        !_event.keep ? this.__doClearResource()
                     : this.__doClearResWithoutSize();
    };
    /**
     * 放入资源
     * @return {Void}
     */
    _pro.__onResDrop = function(){
        // can't drop resource
        if (_e._$hasClassName(this.__body,'j-disable')){
            return;
        }
        // check resource
        var _xres = _d.localCache._$get('resource');
        if (!_xres) return;
        // check resource type match
        if (this.__match.indexOf(_xres.type)<0){
            return;
        }
        // update resource
        this.__doUpdateResource(_u._$merge({},_xres));
    };
    /**
     * 继续图片设置流程
     * @return {Void}
     */
    _pro._$resumeImage = function(_event){
        // check image source
        var _url = _event.value||_event.url;
        if (!_url) return;
        // check image limitation
        if (!this.__limit||_event.nolimit){
            // save resource info
            _e._$dataset(
                this.__body,
                'boxT'+_event.type,
                _event.id
            );
            this._$showImage(_url);
        }else{
            this.__ntst.res = {
                id:_event.id,
                type:_event.type
            };
            this.__ntst.src = _url;
        }
    };
    /**
     * 取图片节点
     * @return {Void}
     */
    _pro._$getImage = function(){
        return this.__nimg;
    };
    /**
     * 取资源设置信息
     * @return {Object} 资源设置信息
     */
    _pro._$getResource = function(){
        var _ret = {};
        _u._$forEach(
            this.__match.split(','),function(_type){
                _ret[_type] = parseInt(_e._$dataset(
                    this.__body,'boxT'+_type
                ))||0;
            },this
        );
        return _ret;
    };
    /**
     * 显示图片
     * @param {Object} _url
     */
    _pro._$showImage = function(_url){
        _e._$addClassName(this.__body,'j-hasimg');
        this.__body.appendChild(this.__nimg);
        this.__nimg.src = _url;
    };
    /**
     * 更新提示信息
     * @param {Object} _tip
     */
    _pro._$updateTip = function(_tip){
        this.__ntip.innerHTML = _tip||'拖拽图片至此区域';
    };
    
    return _p;
});
