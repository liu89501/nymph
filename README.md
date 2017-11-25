# Quick Start

<<<<<<< HEAD
#### 配置文件
##### * 默认加载classpath下的所有nymph开头的xml或者yml配置文件

###### nymph-demo.yml配置文件
```yml
webConfig: #注意层次 每个子的配置用一个空格或者tab缩进
  port: 9900 #内嵌tomcat可以在此设置端口号。对读取 web.xml的tomcat来说这项配置没用, 只能自己去server.xml配置
  contextPath: '' #对于内嵌tomcat来说他就表示项目名, 对于读取web.xml的tomcat来说这个配置没有任何意义
  urlPattern: /   #表示的是你希望让Nymph处理哪些url, / 和 /*表示所有  区别是 / 不会截取到.jsp后缀的url
  suffix: .jsp   #方法返回值路径的后缀
  prefix: /WEB-INF #同上, 前缀
  exclutions:   #想放行的静态资源  也可以直接放行整个文件夹 如 /css/*   /js/* 这种格式
=======
#### 閰嶇疆鏂囦欢
##### * 榛樿鍔犺浇classpath涓嬬殑鎵�鏈塶ymph寮�澶寸殑xml鎴栬�厃ml閰嶇疆鏂囦欢

###### nymph-demo.yml閰嶇疆鏂囦欢
```yml
webConfig: #娉ㄦ剰灞傛 姣忎釜瀛愮殑閰嶇疆鐢ㄤ竴涓┖鏍兼垨鑰卼ab缂╄繘
  port: 9900 #鍐呭祵tomcat鍙互鍦ㄦ璁剧疆绔彛鍙枫�傚璇诲彇 web.xml鐨則omcat鏉ヨ杩欓」閰嶇疆娌＄敤, 鍙兘鑷繁鍘籹erver.xml閰嶇疆
  contextPath: '' #瀵逛簬鍐呭祵tomcat鏉ヨ浠栧氨琛ㄧず椤圭洰鍚�, 瀵逛簬璇诲彇web.xml鐨則omcat鏉ヨ杩欎釜閰嶇疆娌℃湁浠讳綍鎰忎箟
  urlPattern: /   #琛ㄧず鐨勬槸浣犲笇鏈涜Nymph澶勭悊鍝簺url, / 鍜� /*琛ㄧず鎵�鏈�  鍖哄埆鏄� / 涓嶄細鎴彇鍒�.jsp鍚庣紑鐨剈rl
  suffix: .jsp   #鏂规硶杩斿洖鍊艰矾寰勭殑鍚庣紑
  prefix: /WEB-INF #鍚屼笂, 鍓嶇紑
  exclutions:   #鎯虫斁琛岀殑闈欐�佽祫婧�  涔熷彲浠ョ洿鎺ユ斁琛屾暣涓枃浠跺す 濡� /css/*   /js/* 杩欑鏍煎紡
>>>>>>> b151ce2e14ffcc6001f3d432fab7a9f2e1536c46
   - '*.css'
   - '*.ico'
   - '*.jpg'
  filters:
<<<<<<< HEAD
   - com.nymph.filter.TestFilter@*.do # @后面的表示拦截的urlPattern 不设置的话默认是/* 拦截所有
  #异常处理器配置 配置的类需要实现ExceptionHandler接口
  exceptionHandler: com.nymph.exception.impl.ExceptionHandlerImpl
  
scanner: #使用了@Beans @HTTP 相关注解的必须得配置这个, 让容器能扫描到你的类
  - com.nymph.web
component: #将给出的类交给容器管理
  - com.nymph.bean.Woman
  - com.nymph.bean.Man
```
###### nymph-demo.xml配置文件
=======
   - com.nymph.filter.TestFilter@*.do # @鍚庨潰鐨勮〃绀烘嫤鎴殑urlPattern 涓嶈缃殑璇濋粯璁ゆ槸/* 鎷︽埅鎵�鏈�
  #寮傚父澶勭悊鍣ㄩ厤缃� 閰嶇疆鐨勭被闇�瑕佸疄鐜癊xceptionHandler鎺ュ彛
  exceptionHandler: com.nymph.exception.impl.ExceptionHandlerImpl
  
scanner: #浣跨敤浜咢Beans @HTTP 鐩稿叧娉ㄨВ鐨勫繀椤诲緱閰嶇疆杩欎釜, 璁╁鍣ㄨ兘鎵弿鍒颁綘鐨勭被
  - com.nymph.web
component: #灏嗙粰鍑虹殑绫讳氦缁欏鍣ㄧ鐞�
  - com.nymph.bean.Woman
  - com.nymph.bean.Man
```
###### nymph-demo.xml閰嶇疆鏂囦欢
>>>>>>> b151ce2e14ffcc6001f3d432fab7a9f2e1536c46
```xml
<?xml version="1.0" encoding="UTF-8"?>
<nymph xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://www.nymph.com/nymph" 
	xsi:schemaLocation="http://www.nymph.com/nymph http://www.nymph.com/nymph">
<<<<<<< HEAD
	<!-- 表示此包下的类将会被容器扫描到, 并且带有@Bean相关注解的类会被注册到bean工厂 -->	
=======
	<!-- 琛ㄧず姝ゅ寘涓嬬殑绫诲皢浼氳瀹瑰櫒鎵弿鍒�, 骞朵笖甯︽湁@Bean鐩稿叧娉ㄨВ鐨勭被浼氳娉ㄥ唽鍒癰ean宸ュ巶 -->	
>>>>>>> b151ce2e14ffcc6001f3d432fab7a9f2e1536c46
	<scanners>
		<scanner location="com.test"/>
	</scanners>
	
<<<<<<< HEAD
	<!-- web应用的相关配置 -->
=======
	<!-- web搴旂敤鐨勭浉鍏抽厤缃� -->
>>>>>>> b151ce2e14ffcc6001f3d432fab7a9f2e1536c46
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
<<<<<<< HEAD
#### HttpBean代码实例
```java
@HTTP("/start") // 表示此类是一个Http请求的映射类
public class HelloWorld {

	// 自动注入Man的实例, 如果容器中存在
	private @Injection Man man;

	// 只允许Get请求访问此方法 @UrlHolder表示url上声明的变量@test
	@GET("/yes/@test")
	public String test(@UrlHolder("test") String field, Transfer transfer) {
		// transfer是内置的类， 用来将数据存到servlet的各作用域(request, session)
		transfer.ofRequest("q", man);
		// 表示转发到/WEB-INF/index.jsp
		// 当返回值为"->/index"时表示重定向
		return "/index";
	}

	// 只允许Post请求访问此方法, @JSON表示返回的对象会被转换为json字符串响应到页面
	@POST("/no")
	@JSON
	public Man test2() {
		return man;
	}
	
	// 文件上传的处理
	@GET("/upload")
	public void test5(Multipart multipart) throws IOException {
		// file表示页面input标签的name
		FileInf fileInf = multipart.getFileInf("file");
		// 将文件写入指定的位置
		fileInf.writeTo("c:/data/demo.jpg");
	}
	

	// 内嵌tomcat的形式启动应用
=======
#### HttpBean浠ｇ爜瀹炰緥
```java
@HTTP("/start") // 琛ㄧず姝ょ被鏄竴涓狧ttp璇锋眰鐨勬槧灏勭被
public class HelloWorld {

	// 鑷姩娉ㄥ叆Man鐨勫疄渚�, 濡傛灉瀹瑰櫒涓瓨鍦�
	private @Injection Man man;

	// 鍙厑璁窯et璇锋眰璁块棶姝ゆ柟娉� @UrlHolder琛ㄧずurl涓婂０鏄庣殑鍙橀噺@test
	@GET("/get/@test")
	public String test(@UrlHolder("test") String field, Transfer transfer) {
		// transfer鏄唴缃殑绫伙紝 鐢ㄦ潵灏嗘暟鎹瓨鍒皊ervlet鐨勫悇浣滅敤鍩�(request, session)
		transfer.ofRequest("q", man);
		// 琛ㄧず杞彂鍒�/WEB-INF/index.jsp
		// 褰撹繑鍥炲�间负"->/index"鏃惰〃绀洪噸瀹氬悜
		return "/index";
	}

	// 鍙厑璁窹ost璇锋眰璁块棶姝ゆ柟娉�, @JSON琛ㄧず杩斿洖鐨勫璞′細琚浆鎹负json瀛楃涓插搷搴斿埌椤甸潰
	@POST("/post/@test")
	@JSON
	public Man test2(@UrlHolder String test) {
		System.out.println(test);
		return man;
	}
	
	// 鏂囦欢涓婁紶
	@GET("/upload")
	public void test3(Multipart multipart) throws IOException {
		// file琛ㄧず椤甸潰input鏍囩鐨刵ame
		FileInf fileInf = multipart.getFileInf("file");
		// 灏嗘枃浠跺啓鍏ユ寚瀹氱殑浣嶇疆
		fileInf.writeTo("c:/data/demo.jpg");
	}
	
	// 鏂囦欢涓嬭浇
	@GET("/downloads")
	public void test4(Share share) {
		share.shareFile("C:/hello.jpg");
	}

	// 鍐呭祵tomcat鐨勫舰寮忓惎鍔ㄥ簲鐢�
>>>>>>> b151ce2e14ffcc6001f3d432fab7a9f2e1536c46
	public static void main(String[] args) {
		MainStarter.start(HelloWorld.class);
	}
}
```

<<<<<<< HEAD
#### 通过HttpChannel获取HttpBean发出的序列化对象
```java
@HTTP("/demo")
public class HttpTest {

	// 关于序列化对象的传输
	@GET("/class")
	@Serialize
	public Man test3(Share share) {
		// 发送一个序列化对象
		Man man = new Man();
		man.setName("张学友");
=======
#### 閫氳繃HttpChannel鑾峰彇HttpBean鍙戝嚭鐨勫簭鍒楀寲瀵硅薄
```java
// 鏈嶅姟绔�
@HTTP("/demo")
public class HttpTest {

	// 鍏充簬搴忓垪鍖栧璞＄殑浼犺緭 @Serialize娉ㄨВ琛ㄧず杩斿洖鐨勫璞″皢琚簭鍒楀寲鍒板搷搴斿ご涓紙杩斿洖鐨勫璞￠渶瑕佸疄鐜癝erializable鎺ュ彛锛�
	@GET("/class")
	@Serialize
	public Man test() {
		// 鍙戦�佷竴涓簭鍒楀寲瀵硅薄
		Man man = new Man();
		man.setName("寮犲鍙�");
>>>>>>> b151ce2e14ffcc6001f3d432fab7a9f2e1536c46
		return man;
	}
	
	public static void main(String[] args) {
		MainStarter.start(HttpTest.class);
	}
}

<<<<<<< HEAD
=======
// 瀹㈡埛绔�
>>>>>>> b151ce2e14ffcc6001f3d432fab7a9f2e1536c46
public class Test {
	public static void main(String[] args) {
		HttpChannel channel = new HttpChannel("127.0.0.1", 9900);
		Man man = (Man)channel.getObject("/demo/class", Pattern.GET);
		System.out.println(man.getName());
<<<<<<< HEAD
		// 此处man的name为 "张学友"
		
		// 只使用一次的时候应该关掉socket连接
		channel.close();
=======
		// 姝ゅman鐨刵ame涓� "寮犲鍙�"
>>>>>>> b151ce2e14ffcc6001f3d432fab7a9f2e1536c46
	}
}
```

<<<<<<< HEAD
* author: 刘洋, 梁天东
=======
* author: 鍒樻磱, 姊佸ぉ涓�
>>>>>>> b151ce2e14ffcc6001f3d432fab7a9f2e1536c46
