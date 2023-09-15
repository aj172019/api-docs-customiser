package com.example.apidocs.security.service;

import com.example.apidocs.security.model.Member;
import com.example.apidocs.security.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String username, String password) {
        Member member = Member.registerUser(username, passwordEncoder.encode(password));
        memberRepository.save(member);
    }
}
