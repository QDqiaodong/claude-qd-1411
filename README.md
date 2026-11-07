# 茶叶初制加工厂管理台账

面向茶叶初制加工厂的基础资料与生产台账系统，包含茶园与农户、鲜叶收购单、加工批次、成品库存、设备档案五个业务模块。

## 技术栈

- 后端：Spring Boot 3.3.0、Java 17、Spring Data JPA
- 前端：Vue 3、Element Plus、Vite、hash 路由
- 基础设施：MySQL 8、Redis 7、Docker Compose

## 启动

```bash
docker compose up -d --build
```

默认地址：

- 前端：http://127.0.0.1:8361
- 后端：http://127.0.0.1:8472
- 健康检查：http://127.0.0.1:8472/actuator/health

停止并清理容器及匿名卷：

```bash
docker compose down -v
```

端口与数据库配置统一放在 `.env` 中。
