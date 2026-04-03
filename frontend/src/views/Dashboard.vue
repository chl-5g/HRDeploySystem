<template>
  <div>
    <h1 style="margin-bottom:24px">人力资源智能部署系统</h1>
    <div style="display:grid;grid-template-columns:repeat(auto-fit,minmax(200px,1fr));gap:16px">
      <router-link to="/staff" class="dash-card">
        <h3>员工管理</h3>
        <p>查看和管理员工信息</p>
      </router-link>
      <router-link to="/match/new" class="dash-card">
        <h3>新员工匹配</h3>
        <p>ID3 决策树智能分配</p>
      </router-link>
      <router-link to="/match/old" class="dash-card">
        <h3>老员工评估</h3>
        <p>基于绩效的岗位评估</p>
      </router-link>
    </div>
  </div>
</template>

<style scoped>
.dash-card { display:block; background:#fff; border-radius:8px; padding:24px; text-decoration:none; color:#333; box-shadow:0 1px 3px rgba(0,0,0,0.1); transition:box-shadow 0.2s; }
.dash-card:hover { box-shadow:0 4px 12px rgba(0,0,0,0.15); }
.dash-card h3 { color:#1a73e8; margin-bottom:8px; }
.dash-card p { font-size:14px; color:#666; }
</style>
