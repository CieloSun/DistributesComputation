# 实验一：简单的Socket通讯
# 实验二：利用Java RMI实现一个简单的学生管理系统
RMI是现代Java web的实现基础，学习一下RMI是一件有收获的事情。
该实验中我使用了Redis，并手写了一个RedisUtil用于获取Redis对象（单例模式实现），暂时用一个数字代替每个学生存储的内容，完整版应对应存储一个JSON。
# 实验三：利用Socket实现一个HTTP协议服务器
通过Socket编程实现了一个伪·HTTP协议服务器，能够接受GET和POST的HTTP请求，并调用了远古的Java cgi对其进行响应。
本项目的重点，实现中还有很多不足之处。