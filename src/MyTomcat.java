import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class MyTomcat {
    private int port = 8080;
    private Map<String,String> urlServletMap = new HashMap<>();
    MyTomcat(int port){
        this.port = port;
    }

    public void start(){
        //
        initServletMapping();

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("MyTomcat is start...");
            while (true){
                //accept()在连接传入前一直阻塞
                Socket socket = serverSocket.accept();
                InputStream inputStream = socket.getInputStream();
                OutputStream outputStream = socket.getOutputStream();
                System.out.println("有请求接入。。。");

                MyRequest myRequest = new MyRequest(inputStream);
                MyResponse myResponse = new MyResponse(outputStream);

                //请求分发
                dispatch(myRequest,myResponse);

                socket.close();
            }


        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(serverSocket != null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void initServletMapping(){
        for (ServletMapping servletMapping:ServletMappingConfig.servletMappingList){
            urlServletMap.put(servletMapping.getUrl(),servletMapping.getClazz());
        }
    }
    private void dispatch(MyRequest myRequest,MyResponse myResponse){
        String clazz = urlServletMap.get(myRequest.getUrl());
        System.out.println("clazz:"+clazz);
        //反射
        try {
            Class<MyServlet> myServletClass = (Class<MyServlet>) Class.forName(clazz);
            MyServlet myServlet = myServletClass.newInstance();
            myServlet.service(myRequest,myResponse);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }
}
