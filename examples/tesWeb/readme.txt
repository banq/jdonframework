this sample show how simply developing javaEE program by jdonfamework.

1. only need two annotations:

@Service(name="helloService")
public class HelloServiceImpl implements HelloService 
..

@Component
public class UserRepositoryInMEM implements UserRepository {


2. the client call the service's code:

HelloService helloService = (HelloService) WebAppUtil.getService("helloService", req);
String result = helloService.hello(myname);



http://localhost:8080/myweb/

