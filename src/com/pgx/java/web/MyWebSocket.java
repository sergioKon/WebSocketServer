package com.pgx.java.web;
import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;


@ServerEndpoint(value="/endpoint",
		  configurator = Configurator.class)

/**/
//@Interceptors(LoggingInterceptor.class)
public class MyWebSocket {
	
   private static PushTimeService pst;
    @OnOpen
    public void onOpen(Session session,EndpointConfig  config) {
    	 System.out.println("onOpen::" + session.getId());
    //	 WsSession wsSession= (WsSession) session;
    	
    	 HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getCanonicalName());
        
   
         System.out.println(" remote address  = "+httpSession.getAttribute("remoteAddress"));
    	// System.out.print(" ip = "+ getClientIp(session));
    	//  viewProperties(session);
    //	  readOutPutStream(session);
       
         //
         Map<String, List<String>> params = session.getRequestParameterMap();
         
         if (params.get("push") != null && (params.get("push").get(0).equals("TIME"))) {
             
           PushTimeService.initialize();
           PushTimeService.add(session);
           
         }     
    }
    @OnClose
    public void onClose(Session session) {
        System.out.println("onClose::" +  session.getId());
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("onMessage::From=" + session.getId() + " Message=" + message + " request = "+  session.getRequestURI().getPath());
     //   viewProperties(session);
        try {
            session.getBasicRemote().sendText("Hello Client " + session.getId() + "!");
           readOutPutStream(session);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	private void readOutPutStream(Session session)  {
		Writer out;
		try {
			out = session.getBasicRemote().getSendWriter();
			
	           String data = null;
	            System.out.println(" data "+data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getClientIp(Session session) {
		   String ip = session.getUserProperties().get("javax.websocket.endpoint.remoteAddress").toString();
		   int i1 = ip.indexOf("/");
		   int i2 = ip.indexOf(":");
		   return ip.substring(i1 + 1, i2);
		 }
	
	private void viewProperties(Session session) {
		Map<String,Object> map= session.getUserProperties();
		
	
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
	}
    
    @OnError
    public void onError(Throwable t) {
        System.out.println("onError::" + t.getMessage());
    }
}
