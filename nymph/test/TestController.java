package test;

import com.nymph.annotation.GET;
import com.nymph.annotation.HTTP;
import com.nymph.annotation.UrlVar;
import com.nymph.transfer.Transfer;

@HTTP
public class TestController {

	@GET("/test/${page}/qwe/${num}")
	public String test(@UrlVar("page")String qwe, Transfer transfer) {
		transfer.forRequest("q", qwe);
		return "/index";
	}
	
	@GET("/test/${page}/asd/${num}")
	public String test2(@UrlVar("page")String qwe, Transfer transfer) {
		transfer.forRequest("q", qwe);
		return "/index";
	}
}