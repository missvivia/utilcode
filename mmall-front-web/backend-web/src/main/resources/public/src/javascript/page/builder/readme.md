# 资源说明

## 资源标识

可拖拽资源统一使用节点的data-res-id属性记录资源标识

## 资源类型

可拖拽的资源使用节点的data-res-type属性进行类别标识

根据资源的使用特性不同可分为以下几类：

| 标识 | 类型 |
| :--- | :--- |
| 1    | 图片  |
| 2    | 商品  |
| 3    | 组件  |

## 资源状态

可拖拽资源根据目标容器和使用情况可处于以下几种状态

* 可用状态：当前资源可被拖拽使用
* 禁用状态：当前资源不可被拖拽使用，使用j-disabled样式标识


# 容器说明

## 容器配对

拖拽的资源可被放入指定的容器中，
容器通过data-box-match属性标识当前容器可放置的资源类型，
多个资源使用逗号分隔

## 容器资源

每个容器同一个时间仅允许放入一个同类型的资源，
容器已放入的资源使用data-box-t[RES_TYPE]=[RES_ID]的形式记录

## 容器状态

容器根据拖拽的资源和匹配的资源，已经已经包含的资源可分为以下几种状态

* 可放入：当前拖拽的资源可被放入到容器中，是用j-droppable
* 可替换：容器中已有同类资源，可用当前拖拽的资源来替换j-replacable
* 不可用：当前拖拽的资源不允许放入到容器中j-disable

## 容器调整

容器可拖拽进行数值相关属性的调整，使用data-box-adjust属性标识，标识规则为

```
  [ATTR_LIST]:[PARENT_RULE]
```

ATTR_LIST   - 样式属性列表，逗号分隔，如height,lineHeight
PARENT_RULE - 父容器查找规则，使用点分隔的任意字符，如x表示当前data-box-adjust节点的父容器，x.x表示两层父容器

# 组件说明

## 组件插入点

拖拽的组件可插入到页面组件的上下位置，
页面可插入组件的位置使用data-box-insert属性标识，该属性值可设置为

| 属性值 | 插入说明 |
| :---  | :---    |
| top   | 可在插入点之上插入组件 |
| bottom| 可在插入点之下插入组件 |
| both  | 可在插入点上下插入组件 |

## 组件设置信息

### 系统组件设置

#### 背景设置

    bgImgId             - 图片ID
    bgSetting           - 背景设置信息，JSON(bgColor/bgRepeat)

#### 头图设置

    headerImgId         - 图片ID
    headerSetting       - 头图设置，JSON(top/left/fontFamily/fontWeight/fontSize/fontColor/bgColor/opacity/height)

#### 全部商品设置

    allListPartVisiable - 组件是否可见
    allListPartOthers   - 组件设置，JSON(sortType/sortList/bgColor/opacity/spaceTop)

#### 商店地图设置

    mapPartVisiable     - 组件是否可见
    mapPartOthers       - 组件设置，JSON

### 自定义组件设置

    udImgIds            - 组件中涉及的图片ID列表，逗号分隔
    udProductIds        - 组件中涉及的产品ID列表，逗号分隔
    udSetting           - 组件设置信息，JSON[WIDGET_CONFIG_LIST]

#### 文字栏设置

    height/spaceTop/textContent/textAlign/fontFamiliy/fontWeight/fontSize/fontColor/bgColor/opacity

#### BANNER设置

    spaceTop/imgId/hotspots
    hotspot:(top/left/width/height/id)

#### 商品橱窗设置

    spaceTop/bannerIds/productIds





