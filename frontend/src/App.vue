<template>
  <div id="app">
    <nav v-if="loggedIn" class="nav-bar">
      <router-link to="/dashboard" class="brand">HR Deploy</router-link>
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
:root {
  --bg: #f4f7fb;
  --surface: #ffffff;
  --text: #1f2a37;
  --muted: #5b6777;
  --primary: #2563eb;
  --primary-dark: #1e40af;
  --danger: #dc2626;
  --border: #d8e0eb;
  --shadow: 0 10px 28px rgba(15, 23, 42, 0.08);
}
body {
  font-family: "Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
  background:
    linear-gradient(rgba(244, 247, 251, 0.82), rgba(244, 247, 251, 0.82)),
    url('/legacy-bg.jpg') center center / cover no-repeat fixed;
  color: var(--text);
}
#app { min-height: 100vh; }
.nav-bar {
  position: sticky;
  top: 0;
  z-index: 20;
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 24px;
  backdrop-filter: blur(10px);
  background: rgba(17, 24, 39, 0.9);
  color: #fff;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}
.nav-bar a {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: none;
  padding: 8px 12px;
  border-radius: 999px;
  font-size: 14px;
  transition: all 0.2s ease;
}
.nav-bar a:hover { background: rgba(255, 255, 255, 0.14); color: #fff; }
.nav-bar a.router-link-active { background: rgba(255, 255, 255, 0.2); color: #fff; }
.nav-bar .brand { font-weight: 700; letter-spacing: 0.2px; margin-right: 6px; }
.nav-bar .spacer { flex: 1; }
.nav-bar .user-info {
  font-size: 13px;
  color: rgba(255, 255, 255, 0.85);
  padding: 0 6px;
}
.nav-bar button {
  background: rgba(255, 255, 255, 0.2);
  color: #fff;
  border: none;
  padding: 7px 14px;
  border-radius: 999px;
  cursor: pointer;
}
.nav-bar button:hover { background: rgba(255, 255, 255, 0.3); }

.container { max-width: 1080px; margin: 30px auto; padding: 0 18px; }
.card {
  background: var(--surface);
  border-radius: 14px;
  padding: 22px;
  margin-bottom: 18px;
  box-shadow: var(--shadow);
  border: 1px solid var(--border);
}
.page-title {
  margin-bottom: 18px;
  font-size: 30px;
  letter-spacing: -0.4px;
}
.section-head {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;
}
.section-head h2 { font-size: 22px; }

.grid-2 {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 14px;
}
.form-group { margin-bottom: 14px; }
.form-group label {
  display: block;
  margin-bottom: 7px;
  font-weight: 600;
  font-size: 13px;
  color: var(--muted);
}
.form-group input, .form-group select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid var(--border);
  border-radius: 10px;
  font-size: 14px;
  color: var(--text);
  background: #fff;
  transition: border-color 0.2s ease, box-shadow 0.2s ease;
}
.form-group input:focus, .form-group select:focus {
  outline: none;
  border-color: var(--primary);
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.16);
}

.btn {
  padding: 9px 18px;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
.btn-primary {
  background: linear-gradient(120deg, var(--primary), #3b82f6);
  color: #fff;
}
.btn-primary:hover { background: linear-gradient(120deg, var(--primary-dark), var(--primary)); }
.btn-danger { background: #ef4444; color: #fff; }
.btn-danger:hover { background: var(--danger); }
.btn-sm { padding: 5px 12px; font-size: 12px; border-radius: 8px; }

table { width: 100%; border-collapse: separate; border-spacing: 0; overflow: hidden; border-radius: 12px; border: 1px solid var(--border); }
th, td { padding: 11px 12px; text-align: left; border-bottom: 1px solid #e8edf5; }
tbody tr:last-child td { border-bottom: none; }
th { background: #f7f9fc; font-weight: 700; font-size: 12px; color: var(--muted); letter-spacing: 0.3px; }
tbody tr:nth-child(even) td { background: #fcfdff; }

.result-box {
  background: linear-gradient(180deg, #effcf6, #e8f9ee);
  border: 1px solid #9dd9b4;
  border-radius: 14px;
  padding: 18px;
  margin-top: 16px;
}
.result-box h3 { color: #166534; margin-bottom: 8px; }
.msg-error { color: var(--danger); margin-top: 8px; font-size: 13px; }
.muted { color: #8b95a7; }

@media (max-width: 768px) {
  .container { margin-top: 20px; }
  .page-title { font-size: 24px; }
  .grid-2 { grid-template-columns: 1fr; }
  .nav-bar { overflow-x: auto; white-space: nowrap; }
}
</style>
