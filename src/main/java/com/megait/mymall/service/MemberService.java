package com.megait.mymall.service;

import com.megait.mymall.domain.Member;
import com.megait.mymall.domain.MemberType;
import com.megait.mymall.repository.MemberRepository;
import com.megait.mymall.util.MemberUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;

@Service // @Controller 와 @Repository 사이의 비지니스 로직 담당
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    // 기본 관리자 계정 생성
    @PostConstruct  // MemberService 객체 생성 시 무조건 실행할 메서드
    public void createAdminMember(){
        /*Member member = new Member();
        member.setEmail("admin@test.com");
        member.setPassword("1q2w3e4r!");
        member.setJoinedAt(LocalDateTime.now());
        member.setMemberType(MemberType.ROLE_ADMIN);
        memberRepository.save(member);*/

        memberRepository.save(Member.builder()
                .email("admin@test.com")
                .password(passwordEncoder.encode("1q2w3e4r!"))
                .joinedAt(LocalDateTime.now())
                .memberType(MemberType.ROLE_ADMIN)
                .build()
        );
    }

    /**
     *
     * 클라이언트가 아이디(username)와 패스워드(password)를 로그인페이지에 입력함.
     * 스프링시큐리티는 우리가 만들어 둔
     * UserDetailsService 빈의 loadUserByUsername() 을 호출함.
     *  loadUserByUsername()
     *      ~> 시큐리티가 '클라이언트로부터 받은 아이디, 비번'이 맞는지 대조할 수 있도록
     *         (+ 만료 되었는 지, 비밀번호 만료 되었는지, 사용 가능한 계정인지..)
     *         유저 정보를 UserDetails 형 객체에 담아서 보내줘야 함.
     * 스트링 시큐리티는 반환받은 UserDetails 객체를 가지고
     * 클라이언트가 입력한 아디/비번과 대조함.
     *
     * @param username 로그인 처리를 할 username(id)
     * @return UserDetails 로그인 처리를 해줄 유저의 정보
     * @throws UsernameNotFoundException
     *
     */
@Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Member> optional = memberRepository.findByEmail(username);
    Member member = optional.orElseThrow(
            () -> new UsernameNotFoundException("미등록 계정")
    );

    // authentication : 인증 (로그인)
    // authority : 인가(권한). 인증 뒤에 체크하는 것.
    //          -> 'ROLE_'로 시작하는 문자열. ~> GrantedAuthority 인터페이스

    /*MemberType memberType = member.getMemberType();
               //  MemberType.ROLE_ADMIN / MemberType.ROLE_USER
    String type = memberType.name();
                // "ROLE_ADMIN"  /  "ROLE_USER"
    GrantedAuthority authority = new SimpleGrantedAuthority(type);

    HashSet<GrantedAuthority> set = new HashSet<>();
    set.add(authority);*/

    return new MemberUser(member);
}
}
