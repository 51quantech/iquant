关于项目的API和工具路径下载可以参考：http://51quantech.com/

# Iquant
一个关于量化回测的开源系统
## 起步
首先您可能需要先安装以下的东西
- python （必装）
- tomcat （选装）
- jdk （必装）
- maven （选装）

推荐使用IDEA来导入项目，目前我们是在IDEA下开发

##目录结构

<code>
Iquant
    
    ├── src
    │     └main
    │       ├── java
    │       ├── resources
    │       └── webapp
    └── pom.xml
</code>

java目录用来存放的源文件<br/>
java用来存放资源文件<br/>
webapp用来存放系统的页面<br/>

主要事项：

账号：service@51quantech.com
密码：service@51quantech.com

复制resources/python/文件夹下的python文件到com.iquant.common.FoldeUtil类中getPythonHome方法返回的目录中（默认为/data-iquant/resources/目录，后续会改到配置文件中）


一、通过Tomcat函数来启动

##编译方式

项目是用maven来管理的，在pom文件中配置了profile属性，在编译项目时可以通过maven命令或者IDE来对项目进行打包</br>
maven命令：mvn clean package -Dmaven.test.skip=true -Pdevelopment</br>
项目通过maven命令打完包后会在项目下生成一个target目录，在此目录下会有一个ROOT.war的文件</br>

##项目运行

- 安装tomcat
- 将编译后的ROOT.war放入tomcat的webapp下（删除tomcat原来的ROOT目录）

注意：如果本地安装了IDEA并且导入了项目，

##CONTACTS

-Mailing-list: service@51quantech.com

-Group:

-Sending a mail to the address above will subcribe to the mailing list. The subject and message do not matter.


##License

-iquant is under the Apache License Version 2.0, January 2004 LICENSE. See the LICENSE file for details.
