<template>
  <div id="app">
    <nav v-if="loggedIn" class="nav-bar">
      <router-link to="/dashboard">首页</router-link>
      <router-link to="/staff">员工管理</router-link>
      <router-link to="/match/new">新员工匹配</router-link>
      <router-link to="/match/old">老员工评估</router-link>
      <span class="spacer"></span>
      <span class="user-info">{{ username }}</span>
      <button @click="logout">退出</button>
    </nav>
    <main class="container">
      <router-view />
    </main>
  </div>
</template>

<script>
export default {
  computed: {
    loggedIn() {
      return !!sessionStorage.getItem('user')
    },
    username() {
      try { return JSON.parse(sessionStorage.getItem('user')).username } catch { return '' }
    }
  },
  methods: {
    logout() {
      sessionStorage.removeItem('user')
      this.$router.push('/login')
    }
  }
}
</script>

<style>
* { margin: 0; padding: 0; box-sizing: border-box; }
body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; background: #f5f5f5; color: #333; }
.nav-bar { display: flex; align-items: center; gap: 16px; padding: 12px 24px; background: #1a73e8; color: #fff; }
.nav-bar a { color: #fff; text-decoration: none; padding: 4px 8px; border-radius: 4px; }
.nav-bar a.router-link-active { background: rgba(255,255,255,0.2); }
.nav-bar .spacer { flex: 1; }
.nav-bar .user-info { font-size: 14px; opacity: 0.9; }
.nav-bar button { background: rgba(255,255,255,0.2); color: #fff; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; }
.container { max-width: 960px; margin: 24px auto; padding: 0 16px; }
.card { background: #fff; border-radius: 8px; padding: 24px; margin-bottom: 16px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; margin-bottom: 4px; font-weight: 600; font-size: 14px; }
.form-group input, .form-group select { width: 100%; padding: 8px 12px; border: 1px solid #ddd; border-radius: 4px; font-size: 14px; }
.btn { padding: 8px 20px; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
.btn-primary { background: #1a73e8; color: #fff; }
.btn-danger { background: #e53935; color: #fff; }
.btn-sm { padding: 4px 12px; font-size: 12px; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 10px 12px; text-align: left; border-bottom: 1px solid #eee; }
th { background: #fafafa; font-weight: 600; font-size: 13px; text-transform: uppercase; }
.result-box { background: #e8f5e9; border: 1px solid #a5d6a7; border-radius: 8px; padding: 20px; margin-top: 16px; }
.result-box h3 { color: #2e7d32; margin-bottom: 8px; }
.msg-error { color: #e53935; margin-top: 8px; }
</style>
