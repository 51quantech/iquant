# Iquant
一个关于量化回测的开源系统
## 起步
首先您需要先安装以下的东西
- mysql 
- python 
- tomcat 
- jdk
- maven

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

##编译方式

项目是用maven来原理的，在pom文件中配置了profile属性，在编译项目时可以通过maven命令或者IDE来对项目进行打包</br>
maven命令：mvn clean package -Dmaven.test.skip=true -Pdevelopment</br>
项目通过maven命令打完包后会在项目下生成一个target目录，在此目录下会有一个ROOT.war的文件</br>

##项目运行

- 导入mysql的数据库文件（新建数据库quantitative_analysis，导入resources/db/quantitative_analysis.sql）
- 复制resources/python/文件夹下的python文件到com.iquant.common.FoldeUtil类中getPythonHome方法返回的目录中（默认为/data-iquant/resources/目录，后续会改到配置文件中）
- 更改resources/jdbc.properties文件中的数据库的地址、用户名、密码为自己的数据库ip、数据库用户名和数据库密码
- 将编译后的ROOT.war放入tomcat的webapp下（删除tomcat原来的ROOT目录）
- 运行项目需要登陆，在数据库的user表中，增加一个用户名和密码，用来登陆系统

##CONTACTS

-Mailing-list: service@51quantech.com

-Group:

-Sending a mail to the address above will subcribe to the mailing list. The subject and message do not matter.


##License

-iquant is under the Apache License Version 2.0, January 2004 LICENSE. See the LICENSE file for details.
