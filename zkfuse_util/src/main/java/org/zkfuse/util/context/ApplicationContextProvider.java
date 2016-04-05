package org.zkfuse.util.context;

import org.springframework.beans.BeansException; 
import org.springframework.context.ApplicationContext; 
import org.springframework.context.ApplicationContextAware; 

/**
 * This class provides an application-wide access to the
 * Spring ApplicationContext. The ApplicationContext is
 * injected in a static method of the class "AppContext".
 *
 * Use AppContext.getApplicationContext() to get access
 * to all Spring Beans.
 * 
 * Code from http://blog.jdevelop.eu/2008/07/06/access-the-spring-applicationcontext-from-everywhere-in-your-application/
 *
 */ 
public class ApplicationContextProvider implements ApplicationContextAware { 
 
    public void setApplicationContext(ApplicationContext ctx) throws BeansException { 
        // Wiring the ApplicationContext into a static method 
        AppContext.setApplicationContext(ctx); 
    } 
}