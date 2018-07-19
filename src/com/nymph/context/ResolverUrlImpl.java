package com.nymph.context;

import com.nymph.context.core.ResovlerUrl;
import com.nymph.exception.NoSuchClassException;
import com.nymph.ioc.web.MapperInfoContainer;
import com.nymph.ioc.web.MapperInfoContainer.MapperInfo;
import com.nymph.queue.NyQueue;
import com.nymph.transfer.Multipart;
import com.nymph.utils.StringUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * url解析器实现
 * @author NYMPH
 * @date 2017年10月7日下午8:22:08
 */
public class ResolverUrlImpl extends AbstractResolver implements ResovlerUrl {

	private static final Logger LOGGER = LoggerFactory.getLogger(ResolverUrlImpl.class);
	/**
	 *  MapperInfo的容器
	 */
	private static final MapperInfoContainer container = beansFactory.getHttpBeansContainer();
	/**
	 *  文件上传时需要的对象
	 */
	private Multipart multipart;
	/**
	 *  参数队列
	 */
	private final NyQueue<ContextParam> queue;
	
	public ResolverUrlImpl(ContextWrapper wrapper, NyQueue<ContextParam> queue) {
		super(wrapper);
		this.queue = queue;
	}

	@Override
	public void resolver() throws Exception {
		String contextPath = wrapper.contextPath();
		String urlMapping = StringUtils.delete(wrapper.getUri(), contextPath);
		MapperInfo mapperInfo = placeHolderHandler(urlMapping);
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("httpBeans: {}", container);
		}
		urlMappingCheck(mapperInfo);
		Map<String, String[]> params = judgeContentType();
		queue.put(new ContextParam(mapperInfo, params, wrapper, multipart));
	}
	
	@Override
	public MapperInfo placeHolderHandler(String url) throws Exception {
		MapperInfo mapperInfo = container.getMapperInfo(url);

		if (mapperInfo == null) {
			Iterator<Entry<String, MapperInfo>> iterator = container.getIterator();
			out:while (iterator.hasNext()) {
				Entry<String, MapperInfo> entry = iterator.next();
				String[] requestUrls = url.split("/");
				String[] nativeUrls = entry.getKey().split("/");

				if (requestUrls.length == nativeUrls.length) {
					MapperInfo info = entry.getValue();
					Map<String, String> placeHolder = new HashMap<>();
					for (int i = 0; i < nativeUrls.length; i++) {
						if (nativeUrls[i].startsWith("@")) {
							String keyVal = nativeUrls[i].substring(1);
							placeHolder.put(keyVal, requestUrls[i]);
						} else if (! requestUrls[i].equals(nativeUrls[i])) {
							continue out;
						}
					}
					return info.initialize(placeHolder);
				}
			}
		}
		return mapperInfo;
	}
	
	@Override
	public Map<String, String[]> multipartHandler() throws Exception {
		DiskFileItemFactory disk = new DiskFileItemFactory();
		ServletFileUpload servletFileUpload = new ServletFileUpload(disk);
		List<FileItem> items = servletFileUpload.parseRequest(wrapper.httpRequest());

		Map<String, String[]> params = new HashMap<>();

		for (FileItem fileItem : items) {
			if (fileItem.isFormField()) {
				String fieldName = fileItem.getFieldName();
				String fieldVal = fileItem.getString("UTF-8");
				String[] param = params.get(fieldName);

				if (param != null) {
					String[] newArray = new String[param.length + 1];
					System.arraycopy(param, 0, newArray, 0, param.length);
					newArray[param.length] = fieldVal;
					param = newArray;
				} else {
					param = new String[]{fieldVal};
				}
				params.put(fieldName, param);
			}
			else {
				if (multipart == null) {
					multipart = new Multipart();
				}
				multipart.addFiles(fileItem);
			}
		}
		return params;
	}
	
	/**
	 * 判断请求的Content-Type
	 * @return 				表示请求中所有参数的map
	 * @throws Exception	multipartHandler方法抛出的异常
	 */
	private Map<String, String[]> judgeContentType() throws Exception {
		if (wrapper.contentType().startsWith("multipart")) {
			return multipartHandler();
		} else {
			return wrapper.getParameters();
		}
	}
	
	/**
	 * url映射检查, 如果匹配不到任何对象则抛出异常
	 * @param mapperInfo	匹配到的MapperInfo对象
	 */
	private void urlMappingCheck(MapperInfo mapperInfo) {
		if (mapperInfo == null) {
			throw new NoSuchClassException(wrapper.getUri());
		}
	}
}
