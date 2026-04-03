<template>
  <div>
    <h1 class="page-title">人力资源智能部署系统</h1>
    <div class="dash-grid">
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
.dash-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(230px, 1fr));
  gap: 16px;
}
.dash-card {
  display: block;
  background: linear-gradient(150deg, #ffffff, #f6faff);
  border-radius: 14px;
  padding: 24px;
  text-decoration: none;
  color: #1f2937;
  border: 1px solid #dae5f4;
  box-shadow: 0 8px 26px rgba(30, 64, 175, 0.08);
  transition: transform 0.18s ease, box-shadow 0.18s ease;
}
.dash-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 14px 30px rgba(30, 64, 175, 0.13);
}
.dash-card h3 { color: #1d4ed8; margin-bottom: 8px; font-size: 18px; }
.dash-card p { font-size: 14px; color: #64748b; }
</style>
