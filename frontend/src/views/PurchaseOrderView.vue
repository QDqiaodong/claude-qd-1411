<script setup>
import { computed, onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import api from '../api'

const list = ref([]), farmers = ref([]), plots = ref([]), loading = ref(false), dialog = ref(false), drawer = ref(false), detail = ref({})
const filters = ref({ keyword: '', farmerId: null })
const form = ref({ id: null, orderNo: '', farmerId: null, plotId: null, weightKg: null, moisturePercent: null, pickedAt: '', purchaser: '', purchaseDate: '' })
const availablePlots = computed(() => plots.value.filter(item => !form.value.farmerId || item.farmerId === form.value.farmerId))
const nameOf = (items, id, key) => items.find(item => item.id === id)?.[key] || `#${id}`

async function load() { loading.value = true; try { list.value = await api.listPurchases(filters.value) } catch (e) { ElMessage.error(e.message) } finally { loading.value = false } }
function open(row) { form.value = row ? { ...row, pickedAt: row.pickedAt?.slice(0,16) } : { id:null, orderNo:'', farmerId:farmers.value[0]?.id, plotId:plots.value[0]?.id, weightKg:null, moisturePercent:null, pickedAt:'', purchaser:'', purchaseDate:'' }; dialog.value = true }
async function save() { try { const data = { ...form.value, pickedAt: form.value.pickedAt ? `${form.value.pickedAt}:00` : null }; form.value.id ? await api.updatePurchase(form.value.id, data) : await api.createPurchase(data); dialog.value=false; ElMessage.success('收购单已保存'); await load() } catch(e) { ElMessage.error(e.message) } }
async function show(id) { detail.value = await api.getPurchase(id); drawer.value = true }
onMounted(async () => { [farmers.value, plots.value] = await Promise.all([api.listFarmers(), api.listPlots()]); await load() })
</script>

<template><div class="page-card">
  <div class="toolbar"><div class="filters"><el-input v-model="filters.keyword" clearable placeholder="单号或收购人" style="width:210px" /><el-select v-model="filters.farmerId" clearable placeholder="全部农户" style="width:150px"><el-option v-for="x in farmers" :key="x.id" :label="x.name" :value="x.id" /></el-select><el-button @click="load">筛选</el-button></div><el-button type="primary" @click="open()">新增收购单</el-button></div>
  <el-table :data="list" v-loading="loading" border stripe><el-table-column prop="orderNo" label="收购单号" width="150" /><el-table-column label="农户" width="100"><template #default="{row}">{{ nameOf(farmers,row.farmerId,'name') }}</template></el-table-column><el-table-column label="地块"><template #default="{row}">{{ nameOf(plots,row.plotId,'plotName') }}</template></el-table-column><el-table-column prop="weightKg" label="重量(kg)" width="100" /><el-table-column prop="moisturePercent" label="含水率(%)" width="105" /><el-table-column prop="purchaser" label="收购人" width="90" /><el-table-column prop="purchaseDate" label="收购日期" width="115" /><el-table-column label="操作" width="140"><template #default="{row}"><el-button link type="primary" @click="show(row.id)">详情</el-button><el-button link type="primary" @click="open(row)">编辑</el-button></template></el-table-column></el-table>
  <el-dialog v-model="dialog" :title="form.id?'编辑收购单':'新增收购单'" width="560px"><el-form :model="form" label-width="100px"><el-form-item label="收购单号"><el-input v-model="form.orderNo" /></el-form-item><el-form-item label="农户"><el-select v-model="form.farmerId" style="width:100%" @change="form.plotId=null"><el-option v-for="x in farmers" :key="x.id" :label="x.name" :value="x.id" /></el-select></el-form-item><el-form-item label="地块"><el-select v-model="form.plotId" style="width:100%"><el-option v-for="x in availablePlots" :key="x.id" :label="x.plotName" :value="x.id" /></el-select></el-form-item><el-form-item label="重量(kg)"><el-input-number v-model="form.weightKg" :min="0.1" :precision="2" /></el-form-item><el-form-item label="含水率(%)"><el-input-number v-model="form.moisturePercent" :min="0" :max="100" :precision="2" /></el-form-item><el-form-item label="采摘时间"><el-date-picker v-model="form.pickedAt" type="datetime" value-format="YYYY-MM-DDTHH:mm" style="width:100%" /></el-form-item><el-form-item label="收购人"><el-input v-model="form.purchaser" /></el-form-item><el-form-item label="收购日期"><el-date-picker v-model="form.purchaseDate" type="date" value-format="YYYY-MM-DD" style="width:100%" /></el-form-item></el-form><template #footer><el-button @click="dialog=false">取消</el-button><el-button type="primary" @click="save">保存</el-button></template></el-dialog>
  <el-drawer v-model="drawer" title="鲜叶收购单详情" size="420px"><dl class="detail-grid"><template v-for="(v,k) in detail" :key="k"><dt>{{k}}</dt><dd>{{v??'-'}}</dd></template></dl></el-drawer>
</div></template>
