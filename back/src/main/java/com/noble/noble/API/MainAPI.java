package com.noble.noble.API;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.noble.noble.data.Noble;
import com.noble.noble.service.CenturyService;
import com.noble.noble.service.DotaxService;
import com.noble.noble.service.NobleService;
import com.noble.noble.service.OpenAPIService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class MainAPI {
    private final String NOBLE_USER_KEY = "noble127";
    private final String NOBLE_ADMIN_KEY = "nobleherohibi";    

    @Autowired private NobleService nobleService;
    @Autowired private CenturyService centuryService;
    @Autowired private DotaxService dotaxService;
    @Autowired private OpenAPIService openAPIService;

    @PostMapping("/Main/setToken")
    public String insertToken(@RequestBody Noble param) {
        String response = "ERROR";
        if (nobleService.updateNoble(param))
            response = "SUCCESS";
        return response;
    }

    @PostMapping("/Main/cube")
    public Object getCubeCount(@RequestBody Map<String, Object> param) throws IOException{
        int idx = nobleService.getIdx((String)param.get("nickname"));
        String token = nobleService.getNoble(idx).getToken();
        Map<String, Object> searchParam = new HashMap<>();
        searchParam.put("token", token);
        searchParam.put("item", param.get("item"));
        return openAPIService.getCubeCount(searchParam);
    }
    
    @PostMapping("/Main/login")
    public String login(@RequestBody Map<String, Object> param, HttpServletRequest request) {
        String response = "ERROR";
        
        String code = param.get("code") != null ? (String)param.get("code") : "";

        HttpSession session = request.getSession();

        try {
            if (code.equals(NOBLE_USER_KEY)) {
                session.setAttribute("data", "USER");
                session.setMaxInactiveInterval(3600);
                response = "SUCCESS";
            } else if (code.equals(NOBLE_ADMIN_KEY)) {
                session.setAttribute("data", "ADMIN");
                session.setMaxInactiveInterval(3600);
                response = "SUCCESS";
            } else {
                response = "ERROR";
            }
        } catch (Exception e) {
            response = "ERROR";
        }

        return response;
    }

    @PostMapping("/Main/session")
    public String session(HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println(session);
        return (String)session.getAttribute("data");
    }

    @PostMapping("/Main/logout")
    public String logout(HttpSession session) {
        String response = "ERROR";
        
        session.invalidate();
        response = "SUCCESS";
        
        return response;
    }

    @PostMapping("/Main/getTotalCount")
    public List<Integer> getTotalCount() {
        List<Integer> response = new ArrayList<>();

        response.add(nobleService.getNobleCount());
        response.add(centuryService.getCenturyCount());
        response.add(dotaxService.getDotaxCount());
        
        return response;
    }

    @PostMapping("/Main/getTotalList")
    public List<Map<String, Object>> getTotalList(@RequestBody Map<String, Object> param) {
        List<Map<String, Object>> response = new ArrayList<>();

        Map<String, Object> searchParam = new HashMap<>();
        
        String searchStr = param.get("searchStr") != null ? (String)param.get("searchStr") : "";
        String order = param.get("order") != null ? (String)param.get("order") : "idx ASC";

        searchParam.put("searchStr", searchStr);
        searchParam.put("order", order);

        List<Noble> nobleList = nobleService.getNobleListForTotal(searchParam);
        List<Integer> nobleNicknameCheckList = new ArrayList<>();
        for (Noble noble : nobleList) {
            nobleNicknameCheckList.add(noble.getIdx());
        }

        if (searchStr != "") {
            List<Integer> nobleSubList = nobleService.getMainCharFromNoble(searchStr);
            for (int mainChar : nobleSubList) {
                if (!nobleNicknameCheckList.contains(mainChar)) {
                    Noble noble = nobleService.getNoble(mainChar);
                    nobleNicknameCheckList.add(noble.getIdx());
                    nobleList.add(noble);
                }
            }

            List<Integer> centuryList = centuryService.getMainCharFromCentury(searchStr);
            for (int mainChar : centuryList) {
                if (!nobleNicknameCheckList.contains(mainChar)) {
                    //Noble noble = new Noble();
                    //noble.setNickname(nobleService.getNoble(mainChar).getNickname());
                    Noble noble = nobleService.getNoble(mainChar);
                    nobleNicknameCheckList.add(noble.getIdx());
                    nobleList.add(noble);
                }
            }

            List<Integer> dotaxList = dotaxService.getMainCharFromDotax(searchStr);
            for (int mainChar : dotaxList) {
                if (!nobleNicknameCheckList.contains(mainChar)) {
                    Noble noble = nobleService.getNoble(mainChar);
                    nobleNicknameCheckList.add(noble.getIdx());
                    nobleList.add(noble);
                }
            }
        }

        for (Noble noble : nobleList) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("main", ((noble.getDotax() == 1) ? (noble.getNickname() + "(도탁스)") : noble.getNickname()));
            temp.put("admin", noble.getAdmin());
            temp.put("sub", nobleService.getNobleSubList(noble.getIdx()));
            temp.put("century", centuryService.getCenturyListFromMain(noble.getIdx()));
            temp.put("centuryUpper", centuryService.getCenturyUpperListFromMain(noble.getIdx()));
            temp.put("dotax", dotaxService.getDotaxListFromMain(noble.getIdx()));
            temp.put("dotaxUpper", dotaxService.getDotaxUpperListFromMain(noble.getIdx()));
            temp.put("exemptedDate", noble.getExemptedDate());

            response.add(temp);
        }

        return response;
    }

    @PostMapping("/Main/getDojangList")
    public List<Noble> getDojangList(@RequestBody Map<String, Object> param) {
        List<Noble> response = new ArrayList<>();

        Map<String, Object> searchParam = new HashMap<>();
        
        String searchStr = param.get("searchStr") != null ? (String)param.get("searchStr") : "";
        String order = param.get("order") != null ? (String)param.get("order") : "idx ASC";
        int type = param.get("type") != null ? (int)param.get("type") : 1;
        searchParam.put("searchStr", searchStr);
        searchParam.put("order", order);
        searchParam.put("type", type);

        List<Noble> nobleList = nobleService.getNobleList(searchParam);

        for (Noble noble : nobleList) {
            if (noble.getDojangAgree() == 1) {
                response.add(noble);
            }
        }

        return response;
    }
}
