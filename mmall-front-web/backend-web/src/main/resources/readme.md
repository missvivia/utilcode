## 启动

0. 安装node
1. 第一次进入resouces目录，在有git环境的里运行nmp install
2. 再运行bower install
3. 运行auto.bat, 即:
  1. `mvn spring-boot:run`
  2. `mcss`


> 先确认node版本升级到最新, nej 需要0.10.x版本



## 开发


### bower模块

1. 第三方bower模块会安装在 `public/javascript/lib` 目录下
2. 安装额外的第三方模块 `npm install xx@version --save` (使用第三方模块前请与 __俞棋军__ 确认)


### 关于mcss

[Sublime 语法高亮](https://github.com/leeluolee/mcss#%E5%B7%A5%E5%85%B7)

项目的css由mcss的方式编写，手册参考: [https://github.com/leeluolee/mcss](https://github.com/leeluolee/mcss) 

如果不关心mcss提供的额外的语法特性, mcss是支持 __所有sass和less__ 的功能的，所以可以放心使用

在样式文件中可以直接引入远程工具库mass,配合[透明引用](https://github.com/leeluolee/mcss#3-transparent-call)可以来方便使用css3的属性

```css
// 引入mass工具库
@import 'http://nec.netease.com/download/framework/mass/mass/index.mcss';

.u-btn{
  $transition: background-color 0.1s ease-in-out;
  $display: inline-block;
}

```

[mass文档](https://github.com/leeluolee/mass), 


示例可以参考参考项目的base.mcss文件


所有关于mcss的疑问可以反馈给作者郑海波.


## 关于freemarker模板

### 菜单相关

在`wrap/var.html`中定义可

```html
<#assign pages = [
  {
    "name": "product", "title": "商品管理", "icon": "inbox" ,
    "children": [
      {"name": "list", "title": "商品列表"},
      {"name": "edit", "title": "商品新建"},
      {"name": "size", "title": "尺寸模板"}
    ]
  },
  {
    "name": "jit", "title": "JIT管理", "icon": "record",
    "children": [
    ]
  },
  {
    "name": "business", "title": "商务管理", "icon": "briefcase",
    "children": [
    ]
  },
  {
    "name": "picture", "title": "图片空间",
    "children": [
    ]
  },
  {
    "name": "authority", 
    "title": "权限管理", 
    "icon": "wrench"
  }
] />

```

表示各个一级菜单和其下的二级菜单, 其中各个属性的意义是

1. name: 菜单名
2. title: 菜单全名
3. icon: 菜单的icon (只有一级菜单有,  默认为name), 我们使用是默认的bootstrap的[glyphicons图标](http://getbootstrap.com/components/#glyphicons) 例如 ``
4. link: 菜单链接(默认为`/name/sub_name?`), 一级菜单在有children的情况下默认没有链接，如果需要手动设置link参数


然后在你的入口ftl定义你的pageName, pageName格式是`${一级名称}-${二级名称}`, 如`/product/edit.ftl`

```html
<#assign pageName="product-edit"/>
<#include "/wrap/common.ftl" />


<!doctype html>
<html lang="en">
...省略...

<@side /> ...这个macro在common.ftl中

```


## 关于分页

仍然在common.ftl

```

```

## 打包构建

### 关于nej打包工具

..


## 