how to Debug:
使用eclipse调试jdonframework的tesweb:

http://www.jdon.com/jivejdon/thread/38583

1、下载jdonframwork的源码包和jboss6的源码包
2、配置jboss6的Jdonframework环境，参见详细文档http://www.jdon.com/jivejdon/thread/38522
3、打开eclipse，将jdonframwork的tesweb示例导入进项目
4、配置tesweb项目，是项目可以编译成功，编辑tesweb的引用库，确保库路径正确，打开build.xml文件，将文件中的<delete dir="bin"/>项取消，如果不取消则每次编译之后都会将bin目录删除，从而导致源文件找不到对应的class文件而出错，使用ant编译build.xml，确定项目能够成功编译
5、配置启用jboss6的远程调试选项，jboss6的启用远程调试设置在run.conf.bat批处理文件中，将rem set "JAVA_OPTS=%JAVA_OPTS% -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=n"项的rem去掉，将suspend=n改为suspend=y，保存，启动jboss。此时jboss会在Listening for transport dt_socket at address: 8787处停止，等待eclipse调试连接
6、设置eclipse启动调试，debug configration-》remote Java application-》双击-》设置name：webtest，connection type：standard(socket attach), host:localhost,port: 8787
allow termination of remote vm:ture.启动调试，等待jboss启动完毕，即可在线条是jboss程序