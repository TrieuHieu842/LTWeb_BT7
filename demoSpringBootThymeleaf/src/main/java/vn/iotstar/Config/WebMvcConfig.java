package vn.iotstar.Config;

import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // Định nghĩa LocaleResolver
    @Bean(name = "localeResolver")
    public LocaleResolver getLocaleResolver() {
        CookieLocaleResolver resolver = new CookieLocaleResolver();
        resolver.setCookieDomain("iotstar.vn");      // tên cookie
        resolver.setCookieMaxAge(60 * 60);           // thời gian sống cookie (1h)
        resolver.setDefaultLocale(Locale.ENGLISH);   // mặc định là EN
        return resolver;
    }

    // Định nghĩa MessageSource (chỉ ra file messages_xxx.properties)
    @Bean(name = "messageSource")
    public MessageSource getMessageResource() {
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
        messageResource.setBasename("classpath:i18n/messages");   // đường dẫn đến file message
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }

    // Cấu hình Interceptor để bắt param "language"
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
        localeInterceptor.setParamName("language");  // ?language=en
        registry.addInterceptor(localeInterceptor).addPathPatterns("/**");
    }
}
