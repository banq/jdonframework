package sample;

import java.net.MalformedURLException;

import sample.HelloService;

import com.caucho.hessian.client.HessianProxyFactory;

public class HelloworldClient {

	public static HelloworldClient CLIENT = new HelloworldClient(
			"http://localhost:8080/remote/helloService");;

	private String _url;

	private HelloworldClient(String string) {
		_url = string;
	}

	private HelloService _service;

	public static void setServerUrl(String url) {
		CLIENT = new HelloworldClient(url);
	}

	private HelloService getService() {
		if (_service == null) {
			try {
				HessianProxyFactory factory = new HessianProxyFactory();
				_service = (HelloService) factory.create(HelloService.class,
						_url);
			} catch (MalformedURLException ex) {
				System.out.println(ex);
			}

		}

		return _service;
	}

	public String hello(String s) {
		return getService().hello(s);
	}

}
