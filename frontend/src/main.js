import { createApp } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'
import App from './App.vue'
import Login from './views/Login.vue'
import Dashboard from './views/Dashboard.vue'
import StaffList from './views/StaffList.vue'
import StaffDetail from './views/StaffDetail.vue'
import NewMatch from './views/NewMatch.vue'
import OldMatch from './views/OldMatch.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/dashboard', component: Dashboard },
  { path: '/staff', component: StaffList },
  { path: '/staff/:id', component: StaffDetail },
  { path: '/match/new', component: NewMatch },
  { path: '/match/old', component: OldMatch },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const user = sessionStorage.getItem('user')
  if (to.path !== '/login' && !user) {
    next('/login')
  } else {
    next()
  }
})

createApp(App).use(router).mount('#app')
