package server.yogoyogu.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import server.yogoyogu.config.jwt.JwtAccessDeniedHandler;
import server.yogoyogu.config.jwt.JwtAuthenticationEntryPoint;
import server.yogoyogu.config.jwt.JwtSecurityConfig;
import server.yogoyogu.config.jwt.TokenProvider;

import java.util.List;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**", "/swagger/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CSRF 설정 Disable
        http.csrf().disable();

        // CORS
        http.cors().configurationSource(request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(List.of("https://633ec6989ec820004a30086c--cheery-kheer-fe145b.netlify.app/"));
            cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
            cors.setAllowedHeaders(List.of("*"));
            return cors;
        });
        http
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll();


        http
                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)


                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()

                .antMatchers("/swagger-ui/**", "/v3/**", "/test", "/", "/test").permitAll() // swagger
                .antMatchers(HttpMethod.GET, "/image/**").permitAll() // images
                .antMatchers("/api/sign-up", "/api/sign-in", "/api/reissue", "/api/sign-up/email", "/api/sign-up/email/check").permitAll() // auth

                .antMatchers(HttpMethod.GET, "/api/boards/**").access("hasRole('ROLE_USER') or hasRole('ROLE_SEOUL_MANAGER') or hasRole('ROLE_YONGIN_MANAGER')  or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/boards").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/boards/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_SEOUL_MANAGER') or hasRole('ROLE_YONGIN_MANAGER')  or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/boards/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.DELETE, "/api/boards/{id}").access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")

                .antMatchers(HttpMethod.GET, "/api/boards/{id}/replies").access("hasRole('ROLE_USER') or hasRole('ROLE_SEOUL_MANAGER') or hasRole('ROLE_YONGIN_MANAGER')  or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.POST, "/api/boards/{id}/replies").access("hasRole('ROLE_SEOUL_MANAGER') or hasRole('ROLE_YONGIN_MANAGER')  or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers(HttpMethod.PUT, "/api/boards/{id}/replies").access("hasRole('ROLE_SEOUL_MANAGER') or hasRole('ROLE_YONGIN_MANAGER')  or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")


                .antMatchers("/test").permitAll()


                .anyRequest().hasAnyRole("ROLE_ADMIN")
//                .anyRequest().authenticated() // 나머지는 전부 인증 필요
//                .anyRequest().permitAll()   // 나머지는 모두 그냥 접근 가능

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));
    }
}