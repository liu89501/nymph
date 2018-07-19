#ѧϰ�� ����ʵ��Spring��һЩ����

## =============================================

#### �����ļ�

###### nymph-demo.yml�����ļ�
```yml
webConfig:
  port: 9900
  contextPath: ''
  urlPattern: '/'
  suffix: .jsp
  prefix: /WEB-INF
  exclutions:   #����еľ�̬��Դ  Ҳ����ֱ�ӷ��������ļ��� �� /css/*   /js/* ���ָ�ʽ
   - '*.css'
   - '*.ico'
   - '*.jpg'
  filters:
   - com.nymph.filter.TestFilter@*.do # @����ı�ʾ���ص�urlPattern �����õĻ�Ĭ����/* ��������
  #�쳣���������� ���õ�����Ҫʵ��ExceptionHandler�ӿ�
  exceptionHandler: com.nymph.exception.impl.ExceptionHandlerImpl
scanner:
  - com.nymph.web
component: #�����ʵ���ŵ�IOC����
  - com.nymph.bean.Woman
  - com.nymph.bean.Man
```
###### nymph-demo.xml�����ļ�
```xml
<?xml version="1.0" encoding="UTF-8"?>
<nymph xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://www.nymph.com/nymph" 
	xsi:schemaLocation="http://www.nymph.com/nymph http://www.nymph.com/nymph">
	<!-- ��ʾ�˰��µ��ཫ�ᱻ����ɨ�赽, ���Ҵ���@Bean���ע�����ᱻע�ᵽbean���� -->	
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
#### HttpBean����ʵ��
```java
@Http("/start")
@Starter
public class HelloWorld {

	// �Զ�ע��Man��ʵ��, ��������д���
	private @Injection Man man;

	// ֻ����Get������ʴ˷��� @UrlHolder��ʾurl�������ı���@test
	@GET("/get/@test")
	public String test(@UrlHolder("test") String field, Transfer transfer) {
		// transfer�����õ��࣬ ���������ݴ浽servlet�ĸ�������(request, session)
		transfer.ofRequest("q", man);
		// ��ʾת����/WEB-INF/index.jsp
		// ������ֵΪ"->/index"ʱ��ʾ�ض���
		return "/index";
	}

	// ֻ����Post������ʴ˷���, @JSON��ʾ���صĶ���ᱻת��Ϊjson�ַ�����Ӧ��ҳ��
	@POST("/post/@test")
	@JSON
	public Man test2(@UrlHolder String test) {
		System.out.println(test);
		return man;
	}
	
	// �ļ��ϴ�
	@GET("/upload")
	public void test3(Multipart multipart) throws IOException {
		// file��ʾҳ��input��ǩ��name
		FileInf fileInf = multipart.getFileInf("file");
		// ���ļ�д��ָ����λ��
		fileInf.writeTo("c:/data/demo.jpg");
	}
	
	// �ļ�����
	@GET("/downloads")
	public void test4(Share share) {
		share.shareFile("C:/hello.jpg");
	}

	// ��Ƕtomcat����ʽ����Ӧ��
	public static void main(String[] args) {
		MainStarter.start(HelloWorld.class);
	}
}
```

