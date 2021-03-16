package com.bigid.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.support.ErrorPageFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.bigid.web.JsonHttpResponseInterceptor;
import com.bigid.web.common.CommonUtil;
import com.bigid.web.common.Constants;

@EnableWebMvc
@Configuration
@EnableAutoConfiguration	
public class appConfig extends WebMvcConfigurerAdapter {
	@Bean
	public ErrorPageFilter errorPageFilter() {
	    return new ErrorPageFilter();
	}

	@Bean
	public FilterRegistrationBean disableSpringBootErrorFilter(ErrorPageFilter filter) {
	    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
	    filterRegistrationBean.setFilter(filter);
	    filterRegistrationBean.setEnabled(false);
	    return filterRegistrationBean;
	}

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    	String jvmFileProtocol = System.getProperty("os.name").toLowerCase().contains("windows") ? "file:///" : "file:/";
    	
        /*registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/META-INF/resources/webjars/").setCachePeriod(31556926);
        registry.addResourceHandler("/css/**").addResourceLocations("/css/").setCachePeriod(31556926);
        registry.addResourceHandler("/img/**").addResourceLocations("/img/").setCachePeriod(31556926);
        registry.addResourceHandler("/js/**").addResourceLocations("/js/").setCachePeriod(31556926);*/
    	registry.addResourceHandler("/s/**").addResourceLocations(jvmFileProtocol + CommonUtil.getImgBaseDirPath() + Constants.FORWARDSLASH ).setCachePeriod(31556926);
    	registry.addResourceHandler("/static/**").addResourceLocations(jvmFileProtocol + CommonUtil.getStaticResourceBaseDirPath() + Constants.FORWARDSLASH ).setCachePeriod(31556926);
    	System.out.println("========final=="+jvmFileProtocol);
    }
 
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new JsonHttpResponseInterceptor()).addPathPatterns("/services/**");
    }
 
    @Bean
    public InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
}