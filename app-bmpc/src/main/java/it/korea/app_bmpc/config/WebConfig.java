package it.korea.app_bmpc.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import jakarta.servlet.MultipartConfigElement;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private String storePath;
    private String menuPath;
    private String reviewPath;

    @Value("${server.file.base-path-mac}")
    private String basePathMac;

    @Value("${server.file.store.path}")
    private String storePathWindows;
    @Value("${server.file.menu.path}")
    private String menuPathWindows;
    @Value("${server.file.review.path}")
    private String reviewPathWindows;

    @PostConstruct
    public void init() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("mac") || os.contains("linux")) {
            // Mac/Linux용 경로 설정 (yml에서 읽음)
            storePath = basePathMac + "store/";
            menuPath = basePathMac + "menu/";
            reviewPath = basePathMac + "review/";
        } else {
            // Windows 경로
            storePath = storePathWindows.replace("\\", "/");
            menuPath = menuPathWindows.replace("\\", "/");
            reviewPath = reviewPathWindows.replace("\\", "/");
        }

        // 경로 끝에 '/' 없으면 추가
        if (!storePath.endsWith("/")) storePath += "/";
        if (!menuPath.endsWith("/")) menuPath += "/";
        if (!reviewPath.endsWith("/")) reviewPath += "/";
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/imgs/**")
                .addResourceLocations(
                        "file:" + storePath,
                        "file:" + menuPath,
                        "file:" + reviewPath
                )
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());
    }

    /**
     * 문자 인코딩
     * @return
     */
    @Bean
    public Filter characterEncodingFilter() {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        return encodingFilter;
    }

    /**
     * 파일 제한
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.of(50, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(50, DataUnit.MEGABYTES));

        return factory.createMultipartConfig();
    }
}
