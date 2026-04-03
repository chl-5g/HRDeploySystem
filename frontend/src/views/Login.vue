<template>
  <div class="login-wrap">
    <div class="card login-card">
      <p class="login-badge">智能人力资源系统</p>
      <h2 class="login-title">欢迎登录 HR Deploy</h2>
      <div class="form-group">
        <label>邮箱</label>
        <input v-model="email" type="email" placeholder="请输入邮箱" @keyup.enter="login" />
      </div>
      <div class="form-group">
        <label>密码</label>
        <input v-model="password" type="password" placeholder="请输入密码" @keyup.enter="login" />
      </div>
      <p v-if="error" class="msg-error">{{ error }}</p>
      <button class="btn btn-primary full-width" @click="login">登录</button>
      <p class="switch-text">
        没有账号？<a href="#" @click.prevent="showRegister=true">注册</a>
      </p>
    </div>

    <div v-if="showRegister" class="card register-card">
      <h3 class="register-title">注册新用户</h3>
      <div class="form-group"><label>邮箱</label><input v-model="regEmail" /></div>
      <div class="form-group"><label>用户名</label><input v-model="regUsername" /></div>
      <div class="form-group"><label>密码</label><input v-model="regPassword" type="password" /></div>
      <p v-if="regMsg" :class="regOk ? 'msg-success' : 'msg-error'">{{ regMsg }}</p>
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

<style scoped>
.login-wrap {
  max-width: 420px;
  margin: 70px auto 0;
}
.login-card { padding-top: 28px; }
.login-badge {
  display: inline-block;
  font-size: 12px;
  color: #2563eb;
  background: #e8f0ff;
  border: 1px solid #bcd2ff;
  padding: 4px 10px;
  border-radius: 999px;
}
.login-title {
  margin: 12px 0 20px;
  font-size: 24px;
}
.full-width { width: 100%; }
.switch-text {
  text-align: center;
  margin-top: 12px;
  font-size: 13px;
  color: #6b7280;
}
.switch-text a { color: #1d4ed8; text-decoration: none; }
.register-card { margin-top: 14px; }
.register-title { margin-bottom: 14px; font-size: 18px; }
.msg-success { color: #0f766e; margin-top: 8px; font-size: 13px; }
</style>
