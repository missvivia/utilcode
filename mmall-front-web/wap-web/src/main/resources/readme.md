#
## 安装node
## 第一次进入resouces目录，在有git环境的里运行nmp install
## 再运行bower install
## public是对外目录
## deploy是打包目录
## psd是视觉文件目录
## public/templates是freemaker目录
## public/src/pages为静态文件目录
## public/src/javascript为脚本目录，lib为nej目录，如果有需要可以添加query到这个目录
## html为单页面目录
## css为样式文件目录，base.css grid.css moudle.css unit.css,如果自己需要可以往这个文件添加css文件，但需要在ftl里单入引入

####选用的手势框架是hammer.js
手册：http://hammerjs.github.io/getting-started/

####regularjs 
手册 http://regularjs.github.io/guide/zh/syntax/include.html

####脚本入口文件
模块入口在pages的脚本里，每个ftl有一个单独的入口脚本，不可重用入口脚本。
入口脚本都是从widget/module.js继承，index.js有demo文件 尽量参考demo文件开发