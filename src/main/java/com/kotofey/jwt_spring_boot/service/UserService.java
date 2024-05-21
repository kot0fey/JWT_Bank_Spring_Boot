package com.kotofey.jwt_spring_boot.service;

import com.kotofey.jwt_spring_boot.domain.request.UpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Transactional
    public String update(UpdateRequest request) {
        //get username from JWT
return null;
    }
}
