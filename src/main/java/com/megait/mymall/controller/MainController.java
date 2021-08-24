package com.megait.mymall.controller;

import com.megait.mymall.domain.Member;
import com.megait.mymall.repository.MemberRepository;
import com.megait.mymall.service.MemberService;
import com.megait.mymall.util.CurrentMember;
import com.megait.mymall.validation.JoinFormValidator;
import com.megait.mymall.validation.JoinFormVo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @InitBinder("joinFormVo") // MVC 요청 전에 추가할 설정들 (Controller에서만 사용)
    protected void initBinder(WebDataBinder dataBinder){
        dataBinder.addValidators(new JoinFormValidator(memberRepository));
    }

    @RequestMapping("/")
    public String index(Principal principal, Model model){
        String message = "안녕하세요, 손님!";
        if(principal != null){
            message = "안녕하세요, " + principal.getName() + "님!";
        }
        model.addAttribute("msg", message);
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "member/login";
    }


    /*@GetMapping("/mypage")
    public String mypage(Model model, Principal principal) {
        Member member = memberRepository.findByEmail(principal.getName()).orElseThrow();
        model.addAttribute("member", member);
        return "member/mypage";
    }*/


    /*
    @GetMapping("/mypage2")
    public String mypage(Model model, @AuthenticationPrincipal User user){
        if(user != null) {
            Member member = memberRepository.findByEmail(user.getUsername()).orElseThrow();
            model.addAttribute("member", member);
        }
        return "member/mypage";
    }*/


    @RequestMapping("/mypage2")
    public String mypage(Model model,
                         @CurrentMember Member member){

            // #this   ==> 이 객체 (자바의 this를 의미함)
            //              이 곳에서의 this는 로그인 중인 User형 객체. ==> 시큐리티의 User 객체를 의미.
            // member ==> this.getNam

        if(member != null) {
            model.addAttribute("member", member);
        }
        return "member/mypage";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("joinFormVo", new JoinFormVo());
        return "member/signup";
        // vo를 보내서 signup홈페이지에서 vo에 내용을 담아가지고 포스트로 보내는거임
    }

    @PostMapping("/signup")
    public String signupSubmit(@Valid JoinFormVo JoinFormVo, Errors errors){
        if(errors.hasErrors()){
            log.info("회원가입 에러 : {}", errors.getAllErrors());
            return "member/signup";
        }

        log.info("회원가입 정상!");
        memberService.processNewMember(JoinFormVo);
        //멤버 서비스에서 메서드 만들기

        return "redirect:/"; // "/"로 리다이렉트
    }

}
