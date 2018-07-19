#学习用 尝试实现Spring的一些功能

## =============================================

#### 配置文件

###### nymph-demo.yml配置文件
```yml
webConfig:
  port: 9900
  contextPath: ''
  urlPattern: '/'
  suffix: .jsp
  prefix: /WEB-INF
  exclutions:   #想放行的静态资源  也可以直接放行整个文件夹 如 /css/*   /js/* 这种格式
   - '*.css'
   - '*.ico'
   - '*.jpg'
  filters:
   - com.nymph.filter.TestFilter@*.do # @后面的表示拦截的urlPattern 不设置的话默认是/* 拦截所有
  #异常处理器配置 配置的类需要实现ExceptionHandler接口
  exceptionHandler: com.nymph.exception.impl.ExceptionHandlerImpl
scanner:
  - com.nymph.web
component: #将类的实例放到IOC容器
  - com.nymph.bean.Woman
  - com.nymph.bean.Man
```
###### nymph-demo.xml配置文件
```xml
<?xml version="1.0" encoding="UTF-8"?>
<nymph xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://www.nymph.com/nymph" 
	xsi:schemaLocation="http://www.nymph.com/nymph http://www.nymph.com/nymph">
	<!-- 表示此包下的类将会被容器扫描到, 并且带有@Bean相关注解的类会被注册到bean工厂 -->	
	<scanners>
		<scanner location="com.test"/>
	</scanners>
	

	<webConfig>
		<port value="9900"/>
		<encoding value="UTF-8"/>
		<contextPath value=""/>
		<exclutions>
			<exclution value="/css/*"/>
			<exclution value="*.ico"/>
		</exclutions>
		<prefix value="/WEB-INF"/>
		<suffix value=".jsp"/>
		<urlPattern value="/"/>
	</webConfig>
</nymph>
```
#### HttpBean代码实例
```java
@Http("/start")
@Starter
public class HelloWorld {

	// 自动注入Man的实例, 如果容器中存在
	private @Injection Man man;

	// 只允许Get请求访问此方法 @UrlHolder表示url上声明的变量@test
	@GET("/get/@test")
	public String test(@UrlHolder("test") String field, Transfer transfer) {
		// transfer是内置的类， 用来将数据存到servlet的各作用域(request, session)
		transfer.ofRequest("q", man);
		// 表示转发到/WEB-INF/index.jsp
		// 当返回值为"->/index"时表示重定向
		return "/index";
	}

	// 只允许Post请求访问此方法, @JSON表示返回的对象会被转换为json字符串响应到页面
	@POST("/post/@test")
	@JSON
	public Man test2(@UrlHolder String test) {
		System.out.println(test);
		return man;
	}
	
	// 文件上传
	@GET("/upload")
	public void test3(Multipart multipart) throws IOException {
		// file表示页面input标签的name
		FileInf fileInf = multipart.getFileInf("file");
		// 将文件写入指定的位置
		fileInf.writeTo("c:/data/demo.jpg");
	}
	
	// 文件下载
	@GET("/downloads")
	public void test4(Share share) {
		share.shareFile("C:/hello.jpg");
	}

	// 内嵌tomcat的形式启动应用
	public static void main(String[] args) {
		MainStarter.start(HelloWorld.class);
	}
}
```

