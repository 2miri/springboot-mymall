package com.megait.mymall.configuration;
// 시큐리티 관련 Configuration 빈

import com.megait.mymall.service.CustomOAuth2UserService;
import com.megait.mymall.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    private final MemberService memberService;
    private final DataSource dataSource; // DBCP (애플리케이션 프로퍼티에 H2설정했던거)


    // 웹 관련 보안 설정 (예). 방화벽)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(
                        PathRequest.toStaticResources().atCommonLocations()
                );
    }


    // 요청/응답 관련 보안 설정
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 127.0.0.1:8080/  요청은 모든 사용자에게 허용
/*        http.authorizeRequests().mvcMatchers("/").permitAll();

        http.formLogin()  // 로그인 form 사용 및 설정
            .loginPage("/login")  // 로그인 페이지 경로를 "/login"가 아닌 다른 경로로 잡고 싶을 때..
            .defaultSuccessUrl("/", true); // 로그인 성공 시 리다이렉트할 경로

        http.logout() // 로그아웃 설정
            .logoutUrl("/logout") // 로그아웃을 실행할 url. 디폴트는 "/logout"
            .invalidateHttpSession(true) // 로그아웃 완료 시 세션을 종료시킬 것인가.
            .logoutSuccessUrl("/"); // 로그아웃 성공 시 리다이렉트할 경로
*/
        http
                .authorizeRequests()
                        .mvcMatchers("/","email-check")
                        .permitAll()

                        .antMatchers("/mypage/**")
                        .authenticated()
                // mvcMatchers() : MVC 컨트롤러가 사용하는 요청 URL 패턴.
                // antMatchers() : "**"을 사용할 수 있는 URL 패턴.

                .mvcMatchers(HttpMethod.GET, "email-check")
                .permitAll()
                // get방식으로 email-check가 들어오는 것만 퍼밋올 해주겠다!!! 포스트는 안됑!!

                .and()
                        .formLogin()  // 로그인 form 사용 및 설정
                        .loginPage("/login")  // 로그인 페이지 경로를 "/login"가 아닌 다른 경로로 잡고 싶을 때..
                        .defaultSuccessUrl("/", true)
                .and()
                        .logout() // 로그아웃 설정
                        .logoutUrl("/logout") // 로그아웃을 실행할 url. 디폴트는 "/logout"
                        .invalidateHttpSession(true) // 로그아웃 완료 시 세션을 종료시킬 것인가.
                        .logoutSuccessUrl("/")
                .and()
                        .rememberMe()
                            .userDetailsService(memberService)
                            .tokenRepository(tokenRepository()) // 토큰레파지토리 우리가 만든거
                        // 토큰레파지토리 - RememberMe를 저장할 테이블


                .and()
                        .oauth2Login()
                        .loginPage("/login")
                        .userInfoEndpoint()
                        .userService(customOAuth2UserService)

        ;
    }

    @Bean
    public PersistentTokenRepository tokenRepository(){
        // PersistentTokenRepository : RememberMe 선택한 사용자들의 토큰값을 관리한다.
        // private은 빈이 될 수 없음 퍼블릭으로 하셈

        JdbcTokenRepositoryImpl jdbcTokenRepository =
                new JdbcTokenRepositoryImpl();

        // JdbcTokenRepositoryImpl 들어가보면 이미 sql 쿼리가 지정되어있음
        // 필드이름 봐두기

        jdbcTokenRepository.setDataSource(dataSource);
        // 내가 연결시켜놓은 h2데이터소스를 집어넣는거임

        return jdbcTokenRepository;

    }
}
