import { createRouter, createWebHistory } from 'vue-router'
//import HomeView from '../views/HomeView.vue'


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'MainView',
      component: () => import('../views/MainView.vue')
    },
    {
      path: '/newAppointment',
      name: 'newAppo',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/NewAppointmentView.vue')
    },
    {
      path: '/allApointment',
      name: 'allAppos',
      component: () => import('../views/NextAppointmentView.vue')
    },
    {
      path: '/AdminView',
      name: 'adminView',
      component: () => import('../views/MainAdminView.vue')
    },
    {
      path: '/UserView',
      name: 'userView',
      component: () => import('../views/MainUserView.vue')
    }
  ]
})

export default router
