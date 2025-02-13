package rmc.backend.rmc.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import rmc.backend.rmc.security.JwtAuthenticationFilter;
import rmc.backend.rmc.services.RUserService;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@EnableWebMvc
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final RUserService rUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .csrf().disable()
                .addFilterAfter(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/company/**").hasAnyAuthority("COMPANY", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/company/**").hasAnyAuthority("COMPANY", "ADMIN")
                .antMatchers(HttpMethod.GET, "/company/**").permitAll()
                .antMatchers(HttpMethod.POST, "/member/**").hasAnyAuthority("MEMBER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/member/**").hasAnyAuthority("MEMBER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/member/**").permitAll()
                .antMatchers( HttpMethod.GET,"/rating/**").permitAll()
                .antMatchers(HttpMethod.DELETE,"/rating/**").hasAnyAuthority("MEMBER","ADMIN")
                .antMatchers( HttpMethod.POST,"/rating/**").hasAnyAuthority("MEMBER","ADMIN")
                .antMatchers( "/admin/**").hasAnyAuthority( "ADMIN")
                .antMatchers( "/s3/**").permitAll()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(rUserService);
        return provider;
    }
}
