package m2pfe.elivret;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

/**
 * <p>
 * Service used to configure the security of the web application.
 * </p>
 * <p>
 * This service allows authentifcated user to access or not certain controller.
 * </p>
 * 
 * @author Gaëtan PUPET
 * @version 1.1
 */
@Configuration
public class SpringConfiguration extends SpringBootServletInitializer implements WebMvcConfigurer {

	/**
	 * Configure the source of the application.
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Starter.class);
	}

	/**
	 * On startup, set the servlet context to the one passed by parameters.
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
	}

	/**
	 * Configure the path for the resources
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System.out.println("--- addResourceHandlers");
		registry.addResourceHandler("/webjars/**")//
				.addResourceLocations("/webjars/");
	}

	/**
	 * Configure so the application can use the Matrix Variables
	 */
	@Override
	// Pour activer les variables matrix
	public void configurePathMatch(PathMatchConfigurer configurer) {
		var urlPathHelper = new UrlPathHelper();
		// Nous gardons le contenu après le point virgule
		urlPathHelper.setRemoveSemicolonContent(false);
		configurer.setUrlPathHelper(urlPathHelper);
	}

	// TODO : Should be in the configuration method of Spring security
	/**
	 * Configure CORS mapping to allow any source to use the API.
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping("/**")
				.allowedOrigins("*")
				.allowedMethods("*");
	}

}
