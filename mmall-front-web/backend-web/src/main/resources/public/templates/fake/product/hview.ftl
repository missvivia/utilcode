<#if RequestParameters.id??>
  <#assign id=RequestParameters.id/>
</#if>

<#if id??>
<#assign helperVO = {
  "id": 1000,
  "name": "助手名称",
  "haxis": {
    "name": '体重kg',
    "list": [1,2,3,4,5,6,7,8,9]
  },
  "vaxis": {
    "name": "身高cm",
    "list": [111,112,113,114,115,116,117]
  },
  "body": [
    ['','','s', 's', 's', 's', 's', 's', 's'],
    ['','','s', 'm', 'm', 'm', 'm', 'm', 'm'],
    ['','','s', 'l', 'l', 'l', 'l', 'l', 'l'],
    ['','','xl', 'xl', 'xl', 'xl', 'xl', 'xl', 'xl'],
    ['','','xxl', 'xxl', 'xxl', 'xxl', 'xxl', 'xxl', 'xxl'],
    ['','','xs', 'xs', 'xs', 'xs', 'xs', 'xs', 'xs'],
    ['','xl','xxs', 'xxs', 'xxs', 'xxs', 'xxs', 'xxs', 'xxs']
  ]
} />
<#else>
<#assign helperVO = {
  "id": 1000,
  "haxis": {
    "name": '体重kg',
    "list": [1,2,3,4,5,6,7,8,9]
  },
  "vaxis": {
    "name": "身高cm",
    "list": [111,112,113,114,115,116,117]
  },
  "body": []
} />
</#if>