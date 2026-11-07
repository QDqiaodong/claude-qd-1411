import { createRouter, createWebHashHistory } from 'vue-router'
import GardenFarmerView from '../views/GardenFarmerView.vue'
import PurchaseOrderView from '../views/PurchaseOrderView.vue'
import ProcessingBatchView from '../views/ProcessingBatchView.vue'
import FinishedInventoryView from '../views/FinishedInventoryView.vue'
import EquipmentView from '../views/EquipmentView.vue'

export default createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', redirect: '/gardens' },
    { path: '/gardens', component: GardenFarmerView },
    { path: '/purchases', component: PurchaseOrderView },
    { path: '/processing', component: ProcessingBatchView },
    { path: '/inventory', component: FinishedInventoryView },
    { path: '/equipment', component: EquipmentView },
    { path: '/:pathMatch(.*)*', redirect: '/gardens' }
  ]
})
