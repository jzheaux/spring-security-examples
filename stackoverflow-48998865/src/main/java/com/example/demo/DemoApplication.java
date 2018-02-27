package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SpringBootApplication
public class DemoApplication {

	@Controller
	public class ViewsController {
		@GetMapping("/views/{viewName}.html")
		public String view(@PathVariable("viewName") String viewName) {
			return "/views/" + viewName;
		}

		@PostMapping("/indirection")
		public void indirection(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
			request.getRequestDispatcher("/login").forward(request, response);
		}
	}

	@Configuration
	public class WebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter
	{
		// copied from stackoverflow question:
		//   https://stackoverflow.com/questions/48998865/spring-security-behavior-is-different-between-xml-and-java-configs

		@Override
		protected void configure(HttpSecurity http) throws Exception
		{
			http
				.authorizeRequests()
					.antMatchers("/resources/css/**, /resources/images/**").permitAll()
					.antMatchers("/javax.faces.resource/**").permitAll()
					.antMatchers("/views/login*").permitAll()
					.antMatchers("/views/error.html").permitAll()
					.antMatchers("/indirection").permitAll()
					.anyRequest().authenticated()
				.and()
					.formLogin()
					.loginPage("/views/login.html")
					.loginProcessingUrl("/login")
					.defaultSuccessUrl("/views/persones.html", true)
				.and()
					.httpBasic()
				.and()
					.logout().logoutSuccessUrl("/views/login.html")
					.invalidateHttpSession(true)
					.deleteCookies("JSESSIONID", "SPRING_SECURITY_REMEMBER_ME_COOKIE")
				.and()
					.csrf().disable();
		}

		@Autowired
		protected void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
			builder
				.inMemoryAuthentication()
				.withUser("joe")
				.password("joe")
				.roles("USER");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}
