<template>
  <div>
    <h2 style="margin-bottom:16px">员工详情</h2>
    <div v-if="data" class="card">
      <h3>基本信息</h3>
      <table>
        <tr><th style="width:100px">工号</th><td>{{ data.staff.id }}</td></tr>
        <tr><th>姓名</th><td>{{ data.staff.name }}</td></tr>
        <tr><th>性别</th><td>{{ data.staff.sex }}</td></tr>
        <tr><th>民族</th><td>{{ data.staff.nation }}</td></tr>
        <tr><th>籍贯</th><td>{{ data.staff.locate }}</td></tr>
        <tr><th>电话</th><td>{{ data.staff.tel }}</td></tr>
      </table>
    </div>
    <div v-if="data && data.education" class="card">
      <h3>学历信息</h3>
      <table>
        <tr><th style="width:100px">类别</th><td>{{ data.education.category }}</td></tr>
        <tr><th>学历</th><td>{{ data.education.qual }}</td></tr>
        <tr><th>专业</th><td>{{ data.education.major }}</td></tr>
        <tr><th>毕业院校</th><td>{{ data.education.graduate }}</td></tr>
      </table>
    </div>
    <div v-if="data && data.position" class="card">
      <h3>岗位信息</h3>
      <table>
        <tr><th style="width:100px">部门</th><td>{{ data.position.post }}</td></tr>
        <tr><th>职级</th><td>{{ data.position.rank }}</td></tr>
        <tr><th>薪资</th><td>{{ data.position.salary }}</td></tr>
      </table>
    </div>
    <router-link to="/staff" class="btn btn-primary" style="margin-top:16px;display:inline-block">返回列表</router-link>
  </div>
</template>

<script>
import http from '../api/http.js'
export default {
  data() { return { data: null } },
  async mounted() {
    const { data } = await http.get('/staff/' + this.$route.params.id)
    this.data = data
  }
}
</script>
