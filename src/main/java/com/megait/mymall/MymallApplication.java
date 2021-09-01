package com.megait.mymall;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MymallApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymallApplication.class, args);
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public ServletWebServerFactory servletContainer(){
//		스키마가 http고, 포트번호 8080 인 요청도 허용하겠다.

		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setSecure(false); // SSL 적용되지 않은? 뭐야 아니래
		connector.setRedirectPort(8443);

		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory(){
			//익명클래스 사용

			@Override
			protected void postProcessContext(Context context) {

				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTITAL");

				SecurityCollection collection = new SecurityCollection();

				// 이 패턴의 경로로 https(8443)이 아닌 다른 방식의 프로토콜로 요청이 들어왔을 경우
				// https(8443)으로 재요청(리다이렉트) 실행함
				// 위에 http 허용을 해놨지만 개인정보들이 포함된
				// 로그인, 마이페이지 등 꼭 https로 사용해야되는 페이지들은 https로 변환하는거임
				collection.addPattern("/login");
				collection.addPattern("/logout");
				collection.addPattern("/mypage/*");
				collection.addPattern("/signup");

				// 모든 요청을 https로 리다이렉트 시키고싶다면
				// collection.addPattern("/*")

				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);



			}
		};
		factory.addAdditionalTomcatConnectors(connector);

		return factory;
	}
	// 이거까지하고 실행하면? http도 실행이된다.
	// 대신 https 로 할땐 포트번호 8443, http로 할땐 포트번호 8080

}
