package com.tencent.supersonic.headless.server.rest;

import com.tencent.supersonic.auth.api.authentication.pojo.User;
import com.tencent.supersonic.auth.api.authentication.utils.UserHolder;
import com.tencent.supersonic.headless.api.pojo.request.TermSetReq;
import com.tencent.supersonic.headless.api.pojo.response.TermSetResp;
import com.tencent.supersonic.headless.server.service.TermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/semantic/term")
public class TermController {

    @Autowired
    private TermService termService;

    @PostMapping("/saveOrUpdate")
    public boolean saveOrUpdate(@RequestBody TermSetReq termSetReq,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        User user = UserHolder.findUser(request, response);
        termService.saveOrUpdate(termSetReq, user);
        return true;
    }

    @GetMapping
    public TermSetResp getTermSet(@RequestParam("domainId") Long domainId) {
        return termService.getTermSet(domainId);
    }

}
