import { createRouter, createWebHistory } from 'vue-router';
import ShopList from '../components/ShopList.vue';
import ShopForm from '../components/ShopForm.vue';
import ShopEdit from '../components/ShopEdit.vue';

const routes = [
  { path: '/', component: ShopList },
  { path: '/create', component: ShopForm },
  { path: '/shops/:id', component: ShopEdit }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
