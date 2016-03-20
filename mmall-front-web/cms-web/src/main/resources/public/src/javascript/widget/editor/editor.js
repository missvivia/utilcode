/**
 * ------------------------------------------
 * 自定义富媒体编辑器实现文件
 * @version  1.0
 * @author   weiwenqing(wqwei@corp.netease.com)
 * ------------------------------------------
 */
define([
    '{pro}components/notify/notify.js',
    '{lib}util/file/select.js',
    '{lib}util/ajax/xdr.js',
    '{pro}extend/config.js',
    '{lib}util/query/query.js',
    '{lib}ui/editor/editor.js',
    '{lib}util/editor/command/bold.js',
    '{lib}util/editor/command/italic.js',
    '{lib}util/editor/command/insertorderedlist.js',
    '{lib}util/editor/command/insertunorderedlist.js',
    '{lib}util/editor/command/underline.js',
    '{lib}util/editor/command/strikethrough.js',
    '{pro}widget/editor/forecolor.js',
    '{pro}widget/editor/backcolor.js',
    '{pro}widget/editor/crtcolor.js',
    '{pro}widget/editor/crtbgcolor.js',
    '{lib}util/editor/command/justifyleft.js',
    '{lib}util/editor/command/justifycenter.js',
    '{lib}util/editor/command/justifyright.js',
    '{pro}widget/editor/links.js',
    '{lib}util/editor/command/blockquote.js',
    '{lib}util/editor/command/removeformat.js',
    '{pro}widget/editor/insertImage.js',
    '{lib}util/editor/command/space.js'
], function(notify, s, j, config) {
    var using = NEJ.P;
    var e = using('nej.e');
    var _c = using('nej.c');
    var v = using('nej.v');
    var i = using('nej.ui');
    var dw = using('dd.widget');
    /**
     * 富媒体编辑器封装
     * @class   富媒体编辑器封装
     * @extends {nej.ui._$$Editor}
     * @param   {Object} _options 可选配置参数，已处理参数列表如下
     *
     */
    dw._$$Editor = NEJ.C();
    var proto = dw._$$Editor._$extend(i._$$Editor);
    /**
     * 控件重置
     */
    proto.__reset = function(_options) {
        // debugger
        _options.style = 'body{padding:10px;-moz-box-sizing:border-box;box-sizing:border-box;font-size:12px;}';
        this.__supReset(_options);
        this.__initImageUpdateLoad();
    };

    proto.__initImageUpdateLoad = function(){
        var area = this.__area =  this.__editor.__copt.area;
        var label = document.createElement("label");
        label.className = "zicn zbg z-i-81"

        

        var elem = nes.one( '.zicn.z-i-81', this.__body)

        elem.parentNode.replaceChild(label, elem);
        function onUpload(json){
            if(json && json.code === 200){ //success
                area._$focus(2);
                if(!json.result || !json.result[0]) return;
                var html = '<img src="' + json.result[0].imgUrl + '" />';
                try{
                  area._$execCommand('inserthtml', html);
                }catch(e){
                  du.showError('插入图片失败');
                }
            }else{
              
            }
            bindSelectFile();
        }
        function onFileChange(ev){
            ev.form.setAttribute("action", config.UPLOAD_URL)
            j._$upload(ev.form, {
              onload: onUpload,
              onerror: onUpload
            })
        }
        function bindSelectFile(){
            var id = s._$bind(label, {
              parent: label.parentNode,
              name: 'img',
              multiple: false,
              accept:'image/*',
              onchange:onFileChange 
            });
        }
        bindSelectFile();
    }

    /**
     * 动态构建控件节点模板
     * @return {Void}
     */
    proto.__initNodeTemplate = (function() {
        var list0 = [{
            cmd: 'bold',
            txt: '加粗',
            icn: 'z-i-30'
        }, {
            cmd: 'italic',
            txt: '斜体',
            icn: 'z-i-31'
        }, {
            cmd: 'underline',
            txt: '下划线',
            icn: 'z-i-32'
        }, {
            cmd: 'strikethrough',
            txt: '删除线',
            icn: 'z-i-40'
        }];
        var list1 = [{
            cmd: 'insertorderedlist',
            txt: '有序列表',
            icn: 'z-i-61'
        }, {
            cmd: 'insertunorderedlist',
            txt: '无序列表',
            icn: 'z-i-62'
        }, {
            cmd: 'justifyLeft',
            txt: '左对齐',
            icn: 'z-i-50'
        }, {
            cmd: 'justifyCenter',
            txt: '居中对齐',
            icn: 'z-i-51'
        }, {
            cmd: 'justifyRight',
            txt: '右对齐',
            icn: 'z-i-52'
        }];
        var list2 = [{
            cmd: 'crtColor',
            txt: '当前字体颜色',
            icn: 'z-i-crtColor'
        }, {
            cmd: 'foreColor',
            txt: '字体颜色',
            icn: 'z-i-41'
        }];
        var list3 = [{
            cmd: 'crtBgColor',
            txt: '当前背景颜色',
            icn: 'z-i-crtBgColor'
        }, {
            cmd: 'hiliteColor',
            txt: '背景颜色',
            icn: 'z-i-122'
        }];
        var list4 = [{
            cmd: 'link',
            txt: '超链接',
            icn: 'z-i-42'
        }, {
            cmd: 'RemoveFormat',
            txt: '清除格式',
            icn: 'z-i-72'
        }, {
            cmd: 'insertImage',
            txt: '照片上传',
            icn: 'z-i-81'

        }];
        return function() {
            this.__seed_html = e._$addNodeTemplate(
                this.__doGenEditorXhtml({
                    toolbar: this.__doGenCmdXhtml({
                        xlist: list0,
                        hr: !0
                    }) + this.__doGenCmdXhtml({
                        xlist: list1,
                        hr: !0
                    }) + this.__doGenCmdXhtml({
                        xlist: list2,
                        hr: !0
                    }) + this.__doGenCmdXhtml({
                        xlist: list3,
                        hr: !0
                    }) + this.__doGenCmdXhtml({
                        xlist: list4
                    })
                }));
        };
    })();
});