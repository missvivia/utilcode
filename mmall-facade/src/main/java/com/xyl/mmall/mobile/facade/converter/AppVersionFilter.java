package com.xyl.mmall.mobile.facade.converter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xyl.mmall.mobile.facade.param.MobileHeaderAO;


public class AppVersionFilter {

	
	public static void filter(){
		try{
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
		MobileHeaderAO ao = (MobileHeaderAO) request.getAttribute("ao");
		}catch(Exception e){
			
		}
	}
	/*public static void main(String args[]){
		MobileFavoriteListVO b = hello1();
		String a = JsonUtils.toJson(b);
		Class c = b.getClass();
			
        for(Field f :fs){
        	f.setAccessible(true);
        	Desc t = f.getAnnotation(Desc.class);
        	System.out.println("| "+f.getName()+ " | "+t.necessary() + " | " + f.getType().getName().substring(f.getType().getName().lastIndexOf(".")+1) +" | "+t.value()+" |");
        
        }
		for(Field m1 : c.getDeclaredFields()){
			System.out.println("--");
			if(m1.isAnnotationPresent(AppVersion.class)){
				m1.setAccessible(true);
				AppVersion wc =m1.getAnnotation(AppVersion.class);
				System.out.println(m1.getName());
			}
			
		}
		//System.out.println(a);
	}*/
}
