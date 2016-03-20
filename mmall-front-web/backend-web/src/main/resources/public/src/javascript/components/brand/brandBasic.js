define([
    "text!./brandBasic.html",
    "{pro}widget/BaseComponent.js",
    '{lib}util/form/form.js',
    '{lib}base/element.js',
    '{pro}components/brand/imageView.js',
    "{pro}extend/util.js",
    'base/util',
    "pro/components/notify/notify"
], function (tpl, BaseComponent, _f, _e, ImageView, _, _u,notify) {
    var BrandBasic = BaseComponent.extend({
        name: "m-brandbasic",
        template: tpl,
        config: function (data) {
            this.supr(data);
            _.extend(data, {
                fixop: {}
            })
        },
        init: function () {
            this.$on('inject', function () {
                this.initForm();
                this.initImageViews();
            })

        },
        initForm: function () {
            this.__form = _f._$$WebForm._$allocate({
                form: 'webForm'
            });
        },
        initImageViews: function () {
            var that = this;
            this.basicMaxImages = that.data.basic.maxImages || [];
            this.__maxContainImageList = this.basicMaxImages.filter(function (item) {
                return !!item.src
            });
            this.__maxImages = new ImageView({
                data: {
                    limit: 3,
                    min: 1,
                    clazz:"m-imgview m-imgview-4",
                    imgs: this.__maxContainImageList
                }
            }).$inject("#maximages", "top");

            this.__fixImages = new ImageView({
                data: {
                    limit: 6,
                    clazz:"m-imgview m-imgview-3",
                    imgs: that.data.basic.fixImages || [{},{},{},{},{},{}],
                    descVisible: true,
                    eternal: true
                }
            }).$inject("#fiximages", "top");
        },
        getReq: function () {
            if(!this.__form._$checkValidity()){
                return false;
            }
            var formData = this.__form._$data(), maxMin = this.__maxImages.data.min, maxLimit = this.__maxImages.data.limit;
            var fixCheck=true,maxImages = this.__maxImages.getImageList(),fixImages = this.__fixImages.getImageList(),basicMaxImages=this.basicMaxImages;
            if (maxMin <= maxImages.length && maxImages.length <= maxLimit) {
                fixImages.forEach(function(item,index){
                    if(!item.desc){
                        notify.notify({type:"error",message:"第"+(++index)+"橱窗展示图请写上说明"});
                        fixCheck= false;
                    }
                    else if(!item.src){
                        notify.notify({type:"error",message:"第"+(++index)+"请添加图片"});
                        fixCheck= false;
                    }
                });
                if(!fixCheck){
                    return false;
                }
                //添加maxImages个数 为了merge
                var jLen = maxLimit - maxImages.length;
                for (var j = 0; j < jLen; j++) {
                    maxImages.push({src: ""});
                }
                _.merge(basicMaxImages,maxImages);
                this.__maxImages.data.imgs=maxImages.filter(function(maxImage){
                    return maxImage.src
                });
                formData["maxImages"] = basicMaxImages;
                formData["fixImages"] = fixImages;
                return formData;
            } else {
                notify.notify({type:"error",message:"请确认品牌形象图和橱窗展示图图片张数"});
                return false;
            }
        }
    });

    return BrandBasic;

})