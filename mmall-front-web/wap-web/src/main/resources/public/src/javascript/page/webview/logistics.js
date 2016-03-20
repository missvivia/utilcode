/**
 *下拉菜单 设计思路：
 *  通过css3 transition 的动画属性  + Js的展示状态获取
 *  赋予对应目标元素 不同的 class 属性  使其拥有下拉展示的动画效果
 *
 *
 *@author：ppfyang(hzyang_fan@corp.netease.com)
 */

define([
    '{pro}/widget/webview/wap.js' // 对应对象_wap(基础工具函数)
], function (_wap, p, o, f, r) {
    _wap._$domReady(function () {
        /**************************************************
         *下拉菜单
         ****************************************************/
        /*绑定一级菜单事件*/
        var dropdown = _wap._$('.m-package-dropdown'), dropL, dropH; // 一级菜单node
        if (!dropdown[0]) {
            dropdown = [dropdown];
        }
        dropL = dropdown.length;
        dropH = dropdown[0].offsetHeight;

        if (!!dropdown) {

            // 二级菜单展示绑定
            dropdown._$forEach(function (el) {
                var
                    button = el.querySelector('.m-package-tip1'), // 点击展示二级菜单
                    dropdownMenu = el.querySelector('.g-package-detail'), // 二级菜单
                    menuClassList = dropdownMenu.classList, // 二级菜单classList
                    dropdownMenuH = dropdownMenu.offsetHeight; // 标记二级菜单高度

                if (dropL == 1) {
                    el.classList.add('check');
                    menuClassList.add('show');
                    dropdownMenu.style.top = 0;
                    dropdownMenu.style.zIndex = 999;
                    el.style.height = dropdownMenuH + dropH + 'px';
                } else {
                    // 二级菜单隐藏 (为下拉展示做准备)
                    dropdownMenu.style.top = -dropdownMenuH + 'px';
                }

                // 二级菜单展示
                button.onclick = function () {
                    // 显示二级菜单
                    dropdownMenu.style.zIndex = -999;
                    // 右侧图标旋转
                    el.classList.toggle("check");
                    // 二级菜单缓动展示
                    menuClassList.toggle("show");

                    // 判定二级菜单状态 并按不同状态 初始化
                    if (menuClassList.contains("show")) {
                        // 二级菜单展示
                        dropdownMenu.style.top = 0;
                        el.style.height = dropdownMenuH + dropH + 'px';

                        // 缓动展示完成    二级菜单转化为可点击状态
                        _wap._$atTransitionEnd(dropdownMenu, function () {

                            // 避免用户多次点击 动画完成时状态错误
                            if (menuClassList.contains("show")) {
                                dropdownMenu.style.zIndex = 999;
                            }
                        })

                    } else {

                        // 二级目录收起   二级菜单转化为隐藏状态
                        dropdownMenu.style.top = -dropdownMenuH + 'px';
                        el.style.height = dropH + 'px';
                        dropdownMenu.style.zIndex = -999;

                    }
                };
            })
        }
    })
})