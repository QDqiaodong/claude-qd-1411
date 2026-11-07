<script setup>
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../api'

const farmers = ref([]), plots = ref([]), loading = ref(false), keyword = ref('')
const farmerDialog = ref(false), plotDialog = ref(false), detailDrawer = ref(false), detail = ref({}), detailTitle = ref('')
const farmerForm = ref({ id: null, name: '', phone: '', villageGroup: '' })
const plotForm = ref({ id: null, farmerId: null, plotName: '', areaMu: null, altitudeM: null, pickingSeason: '春季' })

const farmerName = id => farmers.value.find(item => item.id === id)?.name || `#${id}`

async function load() {
  loading.value = true
  try { [farmers.value, plots.value] = await Promise.all([api.listFarmers({ keyword: keyword.value }), api.listPlots({ keyword: keyword.value })]) }
  catch (e) { ElMessage.error(e.message) } finally { loading.value = false }
}
function editFarmer(row) { farmerForm.value = row ? { ...row } : { id: null, name: '', phone: '', villageGroup: '' }; farmerDialog.value = true }
function editPlot(row) { plotForm.value = row ? { ...row } : { id: null, farmerId: farmers.value[0]?.id, plotName: '', areaMu: null, altitudeM: null, pickingSeason: '春季' }; plotDialog.value = true }
async function saveFarmer() {
  try { farmerForm.value.id ? await api.updateFarmer(farmerForm.value.id, farmerForm.value) : await api.createFarmer(farmerForm.value); farmerDialog.value = false; ElMessage.success('农户档案已保存'); await load() }
  catch (e) { ElMessage.error(e.message) }
}
async function savePlot() {
  try { plotForm.value.id ? await api.updatePlot(plotForm.value.id, plotForm.value) : await api.createPlot(plotForm.value); plotDialog.value = false; ElMessage.success('茶园地块已保存'); await load() }
  catch (e) { ElMessage.error(e.message) }
}
async function showDetail(type, id) {
  detail.value = type === 'farmer' ? await api.getFarmer(id) : await api.getPlot(id)
  detailTitle.value = type === 'farmer' ? '农户档案详情' : '茶园地块详情'
  detailDrawer.value = true
}
onMounted(load)
</script>

<template>
  <div class="page-card">
    <div class="toolbar">
      <div class="filters"><el-input v-model="keyword" clearable placeholder="姓名、电话、村组或地块" style="width:260px" @keyup.enter="load" /><el-button @click="load">筛选</el-button></div>
      <el-button type="primary" @click="editFarmer()">新增农户</el-button>
    </div>
    <h2 class="section-title">农户档案</h2>
    <el-table :data="farmers" v-loading="loading" border stripe style="margin-top:12px">
      <el-table-column prop="name" label="姓名" /><el-table-column prop="phone" label="联系电话" /><el-table-column prop="villageGroup" label="村组" />
      <el-table-column label="操作" width="150"><template #default="{ row }"><div class="actions"><el-button link type="primary" @click="showDetail('farmer', row.id)">详情</el-button><el-button link type="primary" @click="editFarmer(row)">编辑</el-button></div></template></el-table-column>
    </el-table>
  </div>

  <div class="page-card">
    <div class="toolbar"><h2 class="section-title">茶园地块</h2><el-button type="primary" @click="editPlot()">新增地块</el-button></div>
    <el-table :data="plots" v-loading="loading" border stripe>
      <el-table-column prop="plotName" label="地块名" /><el-table-column label="所属农户"><template #default="{ row }">{{ farmerName(row.farmerId) }}</template></el-table-column>
      <el-table-column prop="areaMu" label="面积(亩)" /><el-table-column prop="altitudeM" label="海拔(m)" /><el-table-column prop="pickingSeason" label="采摘季" />
      <el-table-column label="操作" width="150"><template #default="{ row }"><div class="actions"><el-button link type="primary" @click="showDetail('plot', row.id)">详情</el-button><el-button link type="primary" @click="editPlot(row)">编辑</el-button></div></template></el-table-column>
    </el-table>
  </div>

  <el-dialog v-model="farmerDialog" :title="farmerForm.id ? '编辑农户' : '新增农户'" width="460px">
    <el-form :model="farmerForm" label-width="90px"><el-form-item label="姓名"><el-input v-model="farmerForm.name" /></el-form-item><el-form-item label="联系电话"><el-input v-model="farmerForm.phone" /></el-form-item><el-form-item label="村组"><el-input v-model="farmerForm.villageGroup" /></el-form-item></el-form>
    <template #footer><el-button @click="farmerDialog=false">取消</el-button><el-button type="primary" @click="saveFarmer">保存</el-button></template>
  </el-dialog>
  <el-dialog v-model="plotDialog" :title="plotForm.id ? '编辑地块' : '新增地块'" width="500px">
    <el-form :model="plotForm" label-width="90px"><el-form-item label="所属农户"><el-select v-model="plotForm.farmerId" style="width:100%"><el-option v-for="item in farmers" :key="item.id" :label="item.name" :value="item.id" /></el-select></el-form-item><el-form-item label="地块名"><el-input v-model="plotForm.plotName" /></el-form-item><el-form-item label="面积(亩)"><el-input-number v-model="plotForm.areaMu" :min="0.1" :precision="2" /></el-form-item><el-form-item label="海拔(m)"><el-input-number v-model="plotForm.altitudeM" :min="1" /></el-form-item><el-form-item label="采摘季"><el-select v-model="plotForm.pickingSeason" style="width:100%"><el-option v-for="v in ['春季','夏季','秋季']" :key="v" :label="v" :value="v" /></el-select></el-form-item></el-form>
    <template #footer><el-button @click="plotDialog=false">取消</el-button><el-button type="primary" @click="savePlot">保存</el-button></template>
  </el-dialog>
  <el-drawer v-model="detailDrawer" :title="detailTitle" size="420px"><dl class="detail-grid"><template v-for="(value,key) in detail" :key="key"><dt>{{ key }}</dt><dd>{{ value ?? '-' }}</dd></template></dl></el-drawer>
</template>
