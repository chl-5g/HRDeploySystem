<template>
  <div>
    <h2 style="margin-bottom:16px">老员工评估</h2>
    <div class="card">
      <div style="display:grid;grid-template-columns:1fr 1fr;gap:12px">
        <div class="form-group"><label>员工工号（可选）</label><input v-model="form.staffId" placeholder="自动查询岗位信息" /></div>
        <div></div>
        <div class="form-group"><label>当前部门（手动输入）</label><input v-model="form.post" placeholder="如：研发部" /></div>
        <div class="form-group"><label>当前职级（手动输入）</label><input v-model="form.rank" placeholder="如：P3" /></div>
        <div class="form-group"><label>能力得分 (1-10)</label><input v-model="form.score1" type="number" min="1" max="10" /></div>
        <div class="form-group"><label>业绩得分 (1-10)</label><input v-model="form.score2" type="number" min="1" max="10" /></div>
        <div class="form-group"><label>态度得分 (1-10)</label><input v-model="form.score3" type="number" min="1" max="10" /></div>
      </div>
      <button class="btn btn-primary" @click="evaluate">开始评估</button>
    </div>
    <div v-if="result" class="result-box">
      <h3>评估结果</h3>
      <p><strong>部门：</strong>{{ result.deptName }}</p>
      <p><strong>建议级别：</strong>{{ result.level }}</p>
    </div>
  </div>
</template>

<script>
import http from '../api/http.js'
export default {
  data() { return { form: { staffId:'', post:'', rank:'', score1:'5', score2:'5', score3:'5' }, result: null } },
  methods: {
    async evaluate() {
      const { data } = await http.post('/match/old', this.form)
      this.result = data
    }
  }
}
</script>
