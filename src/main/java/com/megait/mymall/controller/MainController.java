package com.megait.mymall.controller;

import com.megait.mymall.domain.Member;
import com.megait.mymall.repository.MemberRepository;
import com.megait.mymall.util.CurrentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {

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
    private final MemberRepository memberRepository;

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
}
