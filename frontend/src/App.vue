<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const modules = [
  { path: '/gardens', label: '茶园与农户' },
  { path: '/purchases', label: '鲜叶收购单' },
  { path: '/processing', label: '加工批次' },
  { path: '/inventory', label: '成品库存' },
  { path: '/equipment', label: '设备档案' }
]
const pageTitle = computed(() => modules.find(item => item.path === route.path)?.label || '茶叶初制加工厂')
</script>

<template>
  <div class="app-shell">
    <aside class="sidebar">
      <div class="brand">
        <div class="brand-mark">茶</div>
        <div>
          <div class="brand-title">云岭茶叶初制厂</div>
          <div class="brand-subtitle">生产资料管理台账</div>
        </div>
      </div>
      <el-menu :default-active="route.path" class="side-menu" @select="router.push">
        <el-menu-item v-for="item in modules" :key="item.path" :index="item.path">{{ item.label }}</el-menu-item>
      </el-menu>
      <div class="side-footer">数据更新时间 · 2026-11-07</div>
    </aside>
    <section class="workspace">
      <header class="topbar">
        <div><div class="top-kicker">茶叶初制加工厂</div><h1>{{ pageTitle }}</h1></div>
        <el-tag type="success" effect="plain">系统运行正常</el-tag>
      </header>
      <main class="content"><router-view /></main>
    </section>
  </div>
</template>
