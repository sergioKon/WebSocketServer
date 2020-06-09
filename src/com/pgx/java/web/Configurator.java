package com.pgx.java.web;



import java.lang.reflect.Field;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class Configurator extends ServerEndpointConfig.Configurator
{
    private static final Object HTTPREQUEST = HttpServletRequest.class.getCanonicalName();
 
	@Override
    public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
   	System.out.println(" name = "+request.getClass().getName());
   
    try {
	  // Field wsRequest= request.getClass().getDeclaredField("request");
	   for(Field field : request.getClass().getDeclaredFields()){
		   if ( field.getType().getCanonicalName().equals(HTTPREQUEST)) {
			   field.setAccessible(true);
			   HttpServletRequest httpRequest = (HttpServletRequest) field.get(request);
			   HttpSession httpSession= httpRequest.getSession();
			   httpSession.setAttribute("remoteAddress", httpRequest.getRemoteAddr());
			   config.getUserProperties().put(HttpSession.class.getCanonicalName(), httpSession );
			   System.out.println(" address = "+httpRequest.getRemoteAddr());
			   System.out.println(" host = "+httpRequest.getRemoteHost());
			   System.out.println(" port = "+httpRequest.getRemotePort());
			   break;
		   }
	   }
	
	 } catch (Exception e) {
		e.printStackTrace();
	 }
    
  
  //  List<String> ll = Collections.list( servletRequest.getHeaderNames());
    }
    
    private static <I, F> F getField(I instance, Class<F> fieldType) {
        try {
            for (Class<?> type = instance.getClass(); type != Object.class; type = type.getSuperclass()) {
                for (Field field : type.getDeclaredFields()) {
                    if (fieldType.isAssignableFrom(field.getType())) {
                        field.setAccessible(true);
                        field.getName();
                  
                        return (F) field.get(instance);
                    }
                }
            }
        } catch (Exception e) {
            // Handle?
        }

        return null;
    }
}
