####选用的手势框架是hammer.js
手册：http://hammerjs.github.io/getting-started/

####regularjs 
手册 http://regularjs.github.io/guide/zh/syntax/include.html

####脚本入口文件
模块入口在pages的脚本里，每个ftl有一个单独的入口脚本，不可重用入口脚本。
入口脚本都是从widget/module.js继承，index.js有demo文件 尽量参考demo文件开发

####ftl文件
ftl的是3g文件夹下的文件，pages下有template.ftl的模板

####页面请求
主站是 /schedule?scheduleId =xxxx 对应的3g是3g/schedule?scheduleId =xxxx，由于后台把所有的3g开头的的url转成无3g的同步请求，如果静态的用3g文件夹也会被转掉，导致静态资源取不到，所以把静态资源文件夹改成wap，我们的代码都放在wap文件夹，ftl在3g文件夹