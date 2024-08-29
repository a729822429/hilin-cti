# hilin-cti软交换电话集成系统

<img src="https://hilin.icu/img/hilin-logo.png">

[1]: https://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=plastic

[2]: /LICENSE

[3]: https://img.shields.io/badge/hilin-welcome-brightgreen.svg?style=plastic

[4]: https://hilin.icu

[5]: https://img.shields.io/badge/release-1.0.0-blue.svg?style=plastic

[6]: https://hilin.icu
[![license][1]][2]
[![Issue Welcome][3]][4]
[![Release Version][5]][6]  
软交换电话集成系统

## 特点

- 封装freeswitch复杂的逻辑，提供简单接口方便对接
- 纯JAVA开发，理论上全平台兼容，但是freeswitch对linux和windows之外的系统支持不好

## 技术栈

- **JDK21** ： 当前最新的LTS版本
- **maven** ： 项目管理及自动构建工具
- **spring-boot** ：这个只要是javaer应该都知道吧。
- **mysql** ： 最流行的关系型数据库
- **redis** ： 最流行的基于内存的kv存储系统

### 进度

- [x] inbound事件监听封装
- [ ] 席位管理
- [ ] 黑名单管理
- [ ] 自定义席位选择
- [ ] 对外api接口