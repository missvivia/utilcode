## 为了以后活动能够顺利的上线，下线
## 便于代码管理，保证在活动结束后，原有的代码保存干净，活动的代码剔除
## 因此，任何涉及改动到的代码，如ftl，js，mcss，都要复制一份，分别放到对应的activity目录中
## ftl放到template/activity js放到javascript/activity, mcss放到 mcss/activity.图片放到专门的activity
## 后台controller 相关的接口 指定到activity目录
## 活动结束后，接口换回来即可
## 有可能遇到的问题是 旧代码有更新，所以再要拷贝一次。