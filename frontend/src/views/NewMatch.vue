<template>
  <div>
    <h2 style="margin-bottom:16px">新员工智能匹配</h2>
    <div class="card">
      <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px">
        <div class="form-group">
          <label>学历类别</label>
          <select v-model="form.category">
            <option>理工类</option><option>经济管理类</option><option>其他</option>
          </select>
        </div>
        <div class="form-group">
          <label>学历</label>
          <select v-model="form.qual">
            <option>专科</option><option>本科</option><option>硕士</option><option>博士</option>
          </select>
        </div>
        <div class="form-group"><label>专业</label><input v-model="form.major" placeholder="如：计算机科学与技术" /></div>
        <div class="form-group"><label>毕业院校</label><input v-model="form.graduate" placeholder="如：南京邮电大学" /></div>
      </div>
      <button class="btn btn-primary" @click="match">开始匹配</button>
    </div>
    <div v-if="result" class="result-box">
      <h3>匹配结果</h3>
      <p><strong>推荐部门：</strong>{{ result.deptName }}</p>
      <p><strong>推荐级别：</strong>{{ result.level }}</p>
    </div>
  </div>
</template>

<script>
import http from '../api/http.js'
export default {
  data() { return { form: { category: '理工类', qual: '本科', major: '', graduate: '' }, result: null } },
  methods: {
    async match() {
      const { data } = await http.post('/match/new', this.form)
      this.result = data
    }
  }
}
</script>
