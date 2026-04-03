<template>
  <div style="max-width:360px;margin:80px auto">
    <div class="card">
      <h2 style="text-align:center;margin-bottom:24px">HR Deploy System</h2>
      <div class="form-group">
        <label>邮箱</label>
        <input v-model="email" type="email" placeholder="请输入邮箱" @keyup.enter="login" />
      </div>
      <div class="form-group">
        <label>密码</label>
        <input v-model="password" type="password" placeholder="请输入密码" @keyup.enter="login" />
      </div>
      <p v-if="error" class="msg-error">{{ error }}</p>
      <button class="btn btn-primary" style="width:100%" @click="login">登录</button>
      <p style="text-align:center;margin-top:12px;font-size:13px">
        没有账号？<a href="#" @click.prevent="showRegister=true">注册</a>
      </p>
    </div>

    <div v-if="showRegister" class="card">
      <h3 style="margin-bottom:16px">注册新用户</h3>
      <div class="form-group"><label>邮箱</label><input v-model="regEmail" /></div>
      <div class="form-group"><label>用户名</label><input v-model="regUsername" /></div>
      <div class="form-group"><label>密码</label><input v-model="regPassword" type="password" /></div>
      <p v-if="regMsg" :class="regOk?'':'msg-error'" :style="regOk?'color:#2e7d32':''">{{ regMsg }}</p>
      <button class="btn btn-primary" @click="register">注册</button>
    </div>
  </div>
</template>

<script>
import http from '../api/http.js'
export default {
  data() { return { email:'', password:'', error:'', showRegister:false, regEmail:'', regUsername:'', regPassword:'', regMsg:'', regOk:false } },
  methods: {
    async login() {
      this.error = ''
      try {
        const { data } = await http.post('/auth/login', { email: this.email, password: this.password })
        if (data.success) {
          sessionStorage.setItem('user', JSON.stringify(data))
          this.$router.push('/dashboard')
        } else {
          this.error = data.message
        }
      } catch (e) {
        this.error = e.response?.data?.message || '登录失败'
      }
    },
    async register() {
      this.regMsg = ''
      try {
        const { data } = await http.post('/auth/register', { email: this.regEmail, username: this.regUsername, password: this.regPassword })
        this.regOk = data.success
        this.regMsg = data.message
      } catch (e) {
        this.regOk = false
        this.regMsg = e.response?.data?.message || '注册失败'
      }
    }
  }
}
</script>
