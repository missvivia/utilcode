<div class="m-table">
  <table class="table  table-striped">
    <thead>
      <tr>
        <th>序号</th>
        <th>条形码</th>
        <th>尺码</th>
        <th>操作</th>
      </tr>
    </thead>
    <tbody>
      {{#list templates as tpl}}
      <tr r-animated="on: enter; class: fadeIn;">
        <td>{{tpl_index}}</td>
        <td>{{@(tpl.code)}}</td>
        <td>{{@(tpl.size)}}</td>
        <td>
          <a class='btn btn-xs btn-default' href="#" on-click={{templates.splice(tpl_index,1)}}>删除</a>
        </td>
      </tr>
      {{/list}}
    </tbody>
  </table>
</div>