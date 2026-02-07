# 数据库备份 API 使用说明

## 接口列表

### 1. 执行数据库备份

**接口地址：** `POST /api/database/backup/execute`

**请求示例：**
```bash
curl -X POST http://localhost:8001/api/database/backup/execute
```

**前端调用示例（JavaScript）：**
```javascript
// 使用 fetch
fetch('http://localhost:8001/api/database/backup/execute', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    }
})
.then(response => response.json())
.then(data => {
    console.log('备份成功:', data);
    alert('数据库备份成功！文件路径：' + data.data.filePath);
})
.catch(error => {
    console.error('备份失败:', error);
    alert('数据库备份失败！');
});

// 使用 axios
axios.post('http://localhost:8001/api/database/backup/execute')
    .then(response => {
        console.log('备份成功:', response.data);
        alert('数据库备份成功！文件路径：' + response.data.data.filePath);
    })
    .catch(error => {
        console.error('备份失败:', error);
        alert('数据库备份失败！');
    });
```

**响应示例：**
```json
{
    "code": 200,
    "success": true,
    "data": {
        "message": "数据库备份成功",
        "filePath": "D:\\project\\src\\main\\resources\\sql\\primary_db_20260207.sql"
    }
}
```

---

### 2. 获取备份文件列表

**接口地址：** `GET /api/database/backup/files`

**请求示例：**
```bash
curl -X GET http://localhost:8001/api/database/backup/files
```

**前端调用示例（JavaScript）：**
```javascript
// 使用 fetch
fetch('http://localhost:8001/api/database/backup/files')
    .then(response => response.json())
    .then(data => {
        console.log('备份文件列表:', data);
        // 显示文件列表
        data.data.forEach(file => {
            console.log(`文件名: ${file.fileName}, 大小: ${file.fileSize} bytes`);
        });
    })
    .catch(error => {
        console.error('获取列表失败:', error);
    });

// 使用 axios
axios.get('http://localhost:8001/api/database/backup/files')
    .then(response => {
        console.log('备份文件列表:', response.data);
        // 显示文件列表
        response.data.data.forEach(file => {
            console.log(`文件名: ${file.fileName}, 大小: ${file.fileSize} bytes`);
        });
    })
    .catch(error => {
        console.error('获取列表失败:', error);
    });
```

**响应示例：**
```json
{
    "code": 200,
    "success": true,
    "data": [
        {
            "fileName": "primary_db_20260207.sql",
            "filePath": "D:\\project\\src\\main\\resources\\sql\\primary_db_20260207.sql",
            "fileSize": 1024567,
            "lastModified": "2026-02-07T10:30:00"
        }
    ]
}
```

---

### 3. 查询备份记录列表（需要先创建备份记录表）

**接口地址：** `GET /api/database/backup/list`

**请求示例：**
```bash
curl -X GET http://localhost:8001/api/database/backup/list
```

**前端调用示例（JavaScript）：**
```javascript
fetch('http://localhost:8001/api/database/backup/list')
    .then(response => response.json())
    .then(data => {
        console.log('备份记录:', data);
    })
    .catch(error => {
        console.error('获取记录失败:', error);
    });
```

---

## 完整的前端页面示例（HTML + JavaScript）

```html
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>数据库备份管理</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1200px;
            margin: 0 auto;
            padding: 20px;
        }
        .btn {
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            font-size: 16px;
            border-radius: 4px;
        }
        .btn:hover {
            background-color: #45a049;
        }
        .file-list {
            margin-top: 20px;
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
        .loading {
            display: none;
            color: #666;
            margin-left: 10px;
        }
    </style>
</head>
<body>
    <h1>数据库备份管理</h1>
    
    <div>
        <button class="btn" onclick="backupDatabase()">执行数据库备份</button>
        <button class="btn" onclick="loadBackupFiles()">刷新备份列表</button>
        <span class="loading" id="loading">处理中...</span>
    </div>

    <div class="file-list">
        <h2>备份文件列表</h2>
        <table id="fileTable">
            <thead>
                <tr>
                    <th>文件名</th>
                    <th>文件大小</th>
                    <th>创建时间</th>
                    <th>文件路径</th>
                </tr>
            </thead>
            <tbody id="fileTableBody">
                <tr>
                    <td colspan="4" style="text-align: center;">暂无数据</td>
                </tr>
            </tbody>
        </table>
    </div>

    <script>
        const API_BASE_URL = 'http://localhost:8001/api/database/backup';

        // 执行数据库备份
        function backupDatabase() {
            showLoading(true);
            
            fetch(`${API_BASE_URL}/execute`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
            .then(response => response.json())
            .then(data => {
                showLoading(false);
                if (data.success) {
                    alert('数据库备份成功！\n文件路径：' + data.data.filePath);
                    loadBackupFiles(); // 刷新列表
                } else {
                    alert('备份失败：' + data.data);
                }
            })
            .catch(error => {
                showLoading(false);
                console.error('备份失败:', error);
                alert('备份失败，请查看控制台日志');
            });
        }

        // 加载备份文件列表
        function loadBackupFiles() {
            showLoading(true);
            
            fetch(`${API_BASE_URL}/files`)
            .then(response => response.json())
            .then(data => {
                showLoading(false);
                if (data.success) {
                    displayFiles(data.data);
                } else {
                    alert('获取列表失败：' + data.data);
                }
            })
            .catch(error => {
                showLoading(false);
                console.error('获取列表失败:', error);
                alert('获取列表失败，请查看控制台日志');
            });
        }

        // 显示文件列表
        function displayFiles(files) {
            const tbody = document.getElementById('fileTableBody');
            
            if (files.length === 0) {
                tbody.innerHTML = '<tr><td colspan="4" style="text-align: center;">暂无备份文件</td></tr>';
                return;
            }

            tbody.innerHTML = files.map(file => `
                <tr>
                    <td>${file.fileName}</td>
                    <td>${formatFileSize(file.fileSize)}</td>
                    <td>${formatDate(file.lastModified)}</td>
                    <td style="font-size: 12px;">${file.filePath}</td>
                </tr>
            `).join('');
        }

        // 格式化文件大小
        function formatFileSize(bytes) {
            if (bytes < 1024) return bytes + ' B';
            if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(2) + ' KB';
            return (bytes / (1024 * 1024)).toFixed(2) + ' MB';
        }

        // 格式化日期
        function formatDate(dateStr) {
            const date = new Date(dateStr);
            return date.toLocaleString('zh-CN');
        }

        // 显示/隐藏加载状态
        function showLoading(show) {
            document.getElementById('loading').style.display = show ? 'inline' : 'none';
        }

        // 页面加载时自动获取备份列表
        window.onload = function() {
            loadBackupFiles();
        };
    </script>
</body>
</html>
```

---

## 注意事项

1. **跨域问题**：如果前端和后端不在同一域名下，需要配置 CORS
2. **权限控制**：建议添加权限验证，防止未授权访问
3. **备份记录表**：如果需要记录备份历史，请先执行 `db_backup_record.sql` 创建表
4. **文件大小**：大型数据库备份可能需要较长时间，建议添加超时处理
5. **存储空间**：定期清理旧的备份文件，避免占用过多磁盘空间

---

## Swagger 文档访问

启动应用后，访问：`http://localhost:8001/swagger-ui.html`

在 Swagger 中可以直接测试所有接口。
