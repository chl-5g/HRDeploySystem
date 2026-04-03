<template>
  <div>
    <div class="section-head">
      <h2>员工列表</h2>
      <button class="btn btn-primary" @click="showAdd=!showAdd">{{ showAdd ? '取消' : '添加员工' }}</button>
    </div>

    <div v-if="showAdd" class="card">
      <div class="grid-2">
        <div class="form-group"><label>工号</label><input v-model="form.id" /></div>
        <div class="form-group"><label>姓名</label><input v-model="form.name" /></div>
        <div class="form-group"><label>性别</label><select v-model="form.sex"><option>男</option><option>女</option></select></div>
        <div class="form-group"><label>民族</label><input v-model="form.nation" /></div>
        <div class="form-group"><label>籍贯</label><input v-model="form.locate" /></div>
        <div class="form-group"><label>身份证号</label><input v-model="form.idNum" /></div>
        <div class="form-group"><label>电话</label><input v-model="form.tel" /></div>
      </div>
      <button class="btn btn-primary" @click="addStaff">保存</button>
    </div>

    <div class="card">
      <table>
        <thead><tr><th>工号</th><th>姓名</th><th>性别</th><th>民族</th><th>电话</th><th>操作</th></tr></thead>
        <tbody>
          <tr v-for="s in list" :key="s.id">
            <td>{{ s.id }}</td>
            <td>{{ s.name }}</td>
            <td>{{ s.sex }}</td>
            <td>{{ s.nation }}</td>
            <td>{{ s.tel }}</td>
            <td>
              <router-link :to="'/staff/'+s.id" class="btn btn-primary btn-sm">详情</router-link>
              <button class="btn btn-danger btn-sm ml-6" @click="del(s.id)">删除</button>
            </td>
          </tr>
          <tr v-if="!list.length"><td colspan="6" class="empty-cell">暂无数据</td></tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import http from '../api/http.js'
export default {
  data() { return { list: [], showAdd: false, form: { id:'', name:'', sex:'男', nation:'', locate:'', idNum:'', tel:'' } } },
  async mounted() { await this.load() },
  methods: {
    async load() { this.list = (await http.get('/staff')).data },
    async addStaff() {
      await http.post('/staff', this.form)
      this.showAdd = false
      this.form = { id:'', name:'', sex:'男', nation:'', locate:'', idNum:'', tel:'' }
      await this.load()
    },
    async del(id) {
      if (confirm('确定删除？')) { await http.delete('/staff/' + id); await this.load() }
    }
  }
}
</script>

<style scoped>
.ml-6 { margin-left: 6px; }
.empty-cell { text-align: center; color: #94a3b8; }
</style>
