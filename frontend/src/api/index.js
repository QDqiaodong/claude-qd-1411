import axios from 'axios'

const http = axios.create({ baseURL: '/api', timeout: 20000 })
http.interceptors.response.use(r => r, error => Promise.reject(new Error(error?.response?.data?.message || error.message || '请求失败')))

async function call(promise) {
  const body = (await promise).data
  if (!body || body.ok === false) throw new Error(body?.message || '请求失败')
  return body.data
}

const query = values => ({ params: Object.fromEntries(Object.entries(values || {}).filter(([, v]) => v !== '' && v !== null && v !== undefined)) })

export default {
  listFarmers: p => call(http.get('/farmers', query(p))), getFarmer: id => call(http.get(`/farmers/${id}`)),
  createFarmer: d => call(http.post('/farmers', d)), updateFarmer: (id, d) => call(http.put(`/farmers/${id}`, d)),
  listPlots: p => call(http.get('/tea-plots', query(p))), getPlot: id => call(http.get(`/tea-plots/${id}`)),
  createPlot: d => call(http.post('/tea-plots', d)), updatePlot: (id, d) => call(http.put(`/tea-plots/${id}`, d)),
  listPurchases: p => call(http.get('/purchase-orders', query(p))), getPurchase: id => call(http.get(`/purchase-orders/${id}`)),
  createPurchase: d => call(http.post('/purchase-orders', d)), updatePurchase: (id, d) => call(http.put(`/purchase-orders/${id}`, d)),
  listBatches: p => call(http.get('/processing-batches', query(p))), getBatch: id => call(http.get(`/processing-batches/${id}`)),
  createBatch: d => call(http.post('/processing-batches', d)), updateBatch: (id, d) => call(http.put(`/processing-batches/${id}`, d)),
  listInventory: p => call(http.get('/finished-inventory', query(p))), getInventory: id => call(http.get(`/finished-inventory/${id}`)),
  createInventory: d => call(http.post('/finished-inventory', d)), updateInventory: (id, d) => call(http.put(`/finished-inventory/${id}`, d)),
  listEquipment: p => call(http.get('/equipment', query(p))), getEquipment: id => call(http.get(`/equipment/${id}`)),
  createEquipment: d => call(http.post('/equipment', d)), updateEquipment: (id, d) => call(http.put(`/equipment/${id}`, d))
}
