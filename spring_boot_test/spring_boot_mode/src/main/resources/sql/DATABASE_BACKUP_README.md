# 数据库备份功能使用说明

## 功能概述

该功能实现了主数据源（test_all）的完整备份，包括：
- 数据库表结构（CREATE TABLE 语句）
- 所有表的数据（INSERT 语句）
- 自动生成带日期的备份文件名：`primary_db_YYYYMMDD.sql`
- 备份文件保存在 `src/main/resources/sql/` 目录

## 文件结构

```
src/main/java/com/example/spring_boot_mode/
├── model/
│   ├── entity/
│   │   └── DatabaseBackup.java          # 备份实体类
│   ├── dao/
│   │   └── DatabaseBackupDao.java       # 数据访问层
│   ├── service/
│   │   ├── DatabaseBackupService.java   # 服务接口
│   │   └── impl/
│   │       └── DatabaseBackupServiceImpl.java  # 服务实现
│   └── web/
│       └── DatabaseBackupController.java  # 控制器

src/main/resources/
├── mapper/mode/
│   └── DatabaseBackupMapper.xml         # MyBatis 映射文件
└── sql/
    ├── db_backup_record.sql             # 备份记录表（可选）
    ├── primary_db_YYYYMMDD.sql          # 生成的备份文件
    └── API_USAGE.md                     # API 使用文档
```

## API 接口

### 1. 执行数据库备份

**接口：** `POST /api/database/backup/execute`

**功能：** 备份主数据源的所有表结构和数据

**请求示例：**
```bash
curl -X POST http://localhost:8001/api/database/backup/execute
```

**响应示例：**
```json
{
    "code": 200,
    "success": true,
    "data": {
        "message": "数据库备份成功",
        "filePath": "E:\\project\\src\\main\\resources\\sql\\primary_db_20260207.sql"
    }
}
```

### 2. 获取备份文件列表

**接口：** `GET /api/database/backup/files`

**功能：** 获取所有备份文件的列表

**请求示例：**
```bash
curl -X GET http://localhost:8001/api/database/backup/files
```

**响应示例：**
```json
{
    "code": 200,
    "success": true,
    "data": [
        {
            "fileName": "primary_db_20260207.sql",
            "filePath": "E:\\project\\src\\main\\resources\\sql\\primary_db_20260207.sql",
            "fileSize": 1024567,
            "lastModified": "2026-02-07T10:30:00"
        }
    ]
}
```

## 前端调用示例

### JavaScript (Fetch API)

```javascript
// 执行备份
async function backupDatabase() {
    try {
        const response = await fetch('http://localhost:8001/api/database/backup/execute', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        
        const result = await response.json();
        
        if (result.success) {
            alert('备份成功！文件路径：' + result.data.filePath);
        } else {
            alert('备份失败：' + result.data);
        }
    } catch (error) {
        console.error('备份失败:', error);
        alert('备份失败，请查看控制台');
    }
}

// 获取备份列表
async function getBackupFiles() {
    try {
        const response = await fetch('http://localhost:8001/api/database/backup/files');
        const result = await response.json();
        
        if (result.success) {
            console.log('备份文件列表:', result.data);
            // 处理文件列表
            result.data.forEach(file => {
                console.log(`${file.fileName} - ${formatFileSize(file.fileSize)}`);
            });
        }
    } catch (error) {
        console.error('获取列表失败:', error);
    }
}

function formatFileSize(bytes) {
    if (bytes < 1024) return bytes + ' B';
    if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + ' KB';
    return (bytes / (1024 * 1024)).toFixed(2) + ' MB';
}
```

### Vue.js 示例

```vue
<template>
  <div class="backup-manager">
    <h2>数据库备份管理</h2>
    
    <div class="actions">
      <button @click="backupDatabase" :disabled="loading">
        {{ loading ? '备份中...' : '执行备份' }}
      </button>
      <button @click="loadBackupFiles">刷新列表</button>
    </div>

    <div class="file-list">
      <h3>备份文件列表</h3>
      <table v-if="files.length > 0">
        <thead>
          <tr>
            <th>文件名</th>
            <th>文件大小</th>
            <th>创建时间</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="file in files" :key="file.fileName">
            <td>{{ file.fileName }}</td>
            <td>{{ formatFileSize(file.fileSize) }}</td>
            <td>{{ formatDate(file.lastModified) }}</td>
          </tr>
        </tbody>
      </table>
      <p v-else>暂无备份文件</p>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'DatabaseBackup',
  data() {
    return {
      loading: false,
      files: []
    };
  },
  mounted() {
    this.loadBackupFiles();
  },
  methods: {
    async backupDatabase() {
      this.loading = true;
      try {
        const response = await axios.post('http://localhost:8001/api/database/backup/execute');
        if (response.data.success) {
          this.$message.success('数据库备份成功！');
          this.loadBackupFiles();
        } else {
          this.$message.error('备份失败：' + response.data.data);
        }
      } catch (error) {
        console.error('备份失败:', error);
        this.$message.error('备份失败，请查看控制台');
      } finally {
        this.loading = false;
      }
    },
    
    async loadBackupFiles() {
      try {
        const response = await axios.get('http://localhost:8001/api/database/backup/files');
        if (response.data.success) {
          this.files = response.data.data;
        }
      } catch (error) {
        console.error('获取列表失败:', error);
      }
    },
    
    formatFileSize(bytes) {
      if (bytes < 1024) return bytes + ' B';
      if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + ' KB';
      return (bytes / (1024 * 1024)).toFixed(2) + ' MB';
    },
    
    formatDate(dateStr) {
      return new Date(dateStr).toLocaleString('zh-CN');
    }
  }
};
</script>

<style scoped>
.backup-manager {
  padding: 20px;
}

.actions {
  margin: 20px 0;
}

.actions button {
  margin-right: 10px;
  padding: 10px 20px;
  background-color: #4CAF50;
  color: white;
  border: none;
  cursor: pointer;
  border-radius: 4px;
}

.actions button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

th, td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}

th {
  background-color: #4CAF50;
  color: white;
}

tr:hover {
  background-color: #f5f5f5;
}
</style>
```

## 备份文件格式

生成的 SQL 文件包含：

```sql
-- =============================================
-- Database Backup
-- Database: test_all
-- Date: 2026-02-07 10:30:00
-- =============================================

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for table_name
-- ----------------------------
DROP TABLE IF EXISTS `table_name`;
CREATE TABLE `table_name` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  ...
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of table_name
-- ----------------------------
INSERT INTO `table_name` VALUES (1, 'data1', ...);
INSERT INTO `table_name` VALUES (2, 'data2', ...);

SET FOREIGN_KEY_CHECKS=1;
```

## 注意事项

1. **权限要求**：确保应用有读取数据库元数据和数据的权限
2. **磁盘空间**：大型数据库备份可能占用较多磁盘空间
3. **备份时间**：数据量大时备份可能需要较长时间
4. **并发控制**：建议在低峰期执行备份
5. **定期清理**：定期清理旧的备份文件，避免占用过多空间

## 可选功能

### 创建备份记录表

如果需要在数据库中记录备份历史，可以执行以下 SQL：

```sql
-- 在主数据源（test_all）中执行
source src/main/resources/sql/db_backup_record.sql
```

然后取消 `DatabaseBackupServiceImpl.java` 中 `saveBackupRecord` 方法的注释。

## 测试

### 使用 Swagger 测试

1. 启动应用
2. 访问：`http://localhost:8001/swagger-ui.html`
3. 找到 "数据库备份管理" 标签
4. 测试 `/api/database/backup/execute` 接口

### 使用 Postman 测试

1. 创建 POST 请求：`http://localhost:8001/api/database/backup/execute`
2. 点击 Send
3. 查看响应和生成的文件

## 故障排查

### 问题1：找不到数据库

**错误：** `Unknown database 'test_all'`

**解决：** 检查 `application-dev.yml` 中的数据库配置是否正确

### 问题2：权限不足

**错误：** `Access denied for user`

**解决：** 确保数据库用户有 SELECT 和 SHOW 权限

### 问题3：文件写入失败

**错误：** `FileNotFoundException` 或 `Permission denied`

**解决：** 检查应用是否有写入 `src/main/resources/sql/` 目录的权限

## 扩展功能建议

1. **定时备份**：使用 Spring 的 `@Scheduled` 注解实现定时自动备份
2. **压缩备份**：将 SQL 文件压缩为 .zip 或 .gz 格式
3. **远程存储**：将备份文件上传到云存储（OSS、S3 等）
4. **增量备份**：只备份变更的数据
5. **备份恢复**：实现从备份文件恢复数据库的功能
6. **邮件通知**：备份完成后发送邮件通知
7. **备份验证**：验证备份文件的完整性

## 联系支持

如有问题，请查看日志文件或联系开发团队。
