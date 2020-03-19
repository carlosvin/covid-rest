package com.carlosvin.covid;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addFormatters(FormatterRegistry registry) {
    	registry.addConverter(new Converter<String, String>() {

			@Override
			public String convert(String source) {
				return source.toUpperCase();
			}
		});
        ApplicationConversionService.configure(registry);
    }
}