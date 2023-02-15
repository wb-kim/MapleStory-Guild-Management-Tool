package com.noble.noble.API;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.noble.noble.data.Century;
import com.noble.noble.data.Dotax;
import com.noble.noble.data.Form;
import com.noble.noble.data.Log;
import com.noble.noble.data.Noble;
import com.noble.noble.service.CenturyService;
import com.noble.noble.service.DotaxService;
import com.noble.noble.service.FormService;
import com.noble.noble.service.GuildCrawlingService;
import com.noble.noble.service.LogService;
import com.noble.noble.service.NobleService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AdminAPI {
    @Autowired private NobleService nobleService;
    @Autowired private CenturyService centuryService;
    @Autowired private DotaxService dotaxService;
    @Autowired private LogService logService;
    @Autowired private FormService formService;
    @Autowired private GuildCrawlingService crawlingService;

    @PostMapping("/Admin/insertForm")
    public String insertForm(@RequestBody Map<String, Object> param) {
        String response = "ERROR";
        Form form = new Form();
        form.setNickname((String)param.get("nickname"));
        form.setReason((String)param.get("reason"));
        form.setAge((String)param.get("age"));
        form.setFlag((String)param.get("flag"));
        form.setLatestGuild((String)param.get("latestGuild"));
        form.setManner((String)param.get("manner"));
        form.setCommunity((String)param.get("community"));
        form.setDojang((String)param.get("dojang"));

        System.out.println(form);
        if (formService.insertForm(form)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/joinToForm")
    public String joinToForm(@RequestBody Map<String, Object> param) throws IOException, InterruptedException {
        String response = "ERROR";
        int formIdx = param.get("formIdx") != null ? (int)param.get("formIdx") : 0;
        String grantor = param.get("grantor") != null ? (String)param.get("grantor") : "히비낏";
        Noble noble = new Noble();
        Form form = formService.getForm(formIdx);

        noble.setNickname((String)param.get("nickname"));
        noble.setDojangAgree((form.getDojang() == "예") ? 1 : 0);
        noble.setGrantor(grantor);

        if(insertNoble(noble) == "SUCCESS" && formService.join(formIdx)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/getAdmin")
    public List<String> getAdmin() {
        return nobleService.getAdmin();
    }

    @PostMapping("/Admin/checkMainChar")
    public int checkMainChar(@RequestBody Map<String, Object> param) {
        String name = (String)param.get("nickname");
        
        if (nobleService.checkDuplicate(name)) {
            return nobleService.getIdx(name);        
        } else {
            return 0;
        }
    }

    @PostMapping("/Admin/getFormList")
    public Map<String, Object> getFormList() {
        Map<String, Object> response = new HashMap<>();
        response.put("checked", formService.getFormChecked());
        response.put("unchecked", formService.getFormNotChecked());
        return response;
    }

    @PostMapping("/Admin/getForm")
    public Form getForm(@RequestBody Map<String, Object> param) {
        return formService.getForm((int)param.get("idx"));
    }

    @PostMapping("/Admin/getLogList")
    public List<Log> getLogList(@RequestBody Map<String, Object> param) {
        Map<String, Object> searchParam = new HashMap<>();
        
        String searchStr = param.get("searchStr") != null ? (String)param.get("searchStr") : "";
        String order = param.get("order") != null ? (String)param.get("order") : "create_dt DESC";
        int type = param.get("type") != null ? (int)param.get("type") : 1;
        searchParam.put("searchStr", searchStr);
        searchParam.put("order", order);
        searchParam.put("type", type);

        return logService.getLogList(searchParam);
    }

    @PostMapping("/Admin/getNobleList")
    public List<Noble> getNobleList(@RequestBody Map<String, Object> param) {
        Map<String, Object> searchParam = new HashMap<>();
        
        String searchStr = param.get("searchStr") != null ? (String)param.get("searchStr") : "";
        String order = param.get("order") != null ? (String)param.get("order") : "idx ASC";

        searchParam.put("searchStr", searchStr);
        searchParam.put("type", 1);
        searchParam.put("order", order);

        List<Noble> nobleList = nobleService.getNobleList(searchParam);
        List<Noble> response = new ArrayList<>();
        for (Noble noble : nobleList) {
            if (noble.getMainChar() != 0) {
                Noble nobleFromIdx = nobleService.getNoble(noble.getMainChar());
                noble.setMainCharNick(nobleFromIdx.getNickname());
            }
            response.add(noble);
        }

        return response;
    }

    @PostMapping("/Admin/getNoble")
    public Noble getNoble(@RequestBody Map<String, Object> param) {
        int idx = param.get("idx") != null ? (int)param.get("idx") : 0;
        Noble noble = nobleService.getNoble(idx);
        if (noble.getMainChar() != 0) {
            noble.setMainCharNick((nobleService.getNoble(noble.getMainChar())).getNickname());
        }
        return noble;
    }

    @PostMapping("/Admin/insertNoble")
    public String insertNoble(@RequestBody Noble noble) throws IOException, InterruptedException {
        String response = "ERROR";

        Map<String, Object> userInfo = crawlingService.getUserInfo(noble.getNickname());
        if ((int)userInfo.get("level") == 0) {
            response = "MGERROR";
            return response;
        }
        noble.setDojang((int)userInfo.get("dojang"));
        noble.setLevel((int)userInfo.get("level"));
        noble.setJob((String)userInfo.get("job"));
        int mainChar = ((Integer)noble.getMainChar() != null) ? noble.getMainChar() : 0;
        noble.setMainChar(mainChar);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Log log = new Log();
        log.setNickname(noble.getNickname());
        Dotax dotax = new Dotax();
        if (noble.getDotax() == 1) {
            log.setWhat("도탁스 길드 가입(본캐)");  
            dotax.setNickname(noble.getNickname());
            dotax.setLevel((int)userInfo.get("level"));
            dotax.setJob((String)userInfo.get("job"));
            dotax.setGrantor(noble.getGrantor());
            dotaxService.insertDotax(dotax);  
        } else {
            log.setWhat("노블 길드 가입");
        }
        log.setCreateDt((String)now.format(formatter));
        log.setWho(noble.getGrantor());
        
        if (nobleService.insertNoble(noble) && logService.insertLog(log)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/updateNoble")
    public String updateNoble(@RequestBody Noble noble) throws IOException, InterruptedException {
        String response = "ERROR";

        Map<String, Object> userInfo = crawlingService.getUserInfo(noble.getNickname());
        if ((int)userInfo.get("level") == 0) {
            response = "MGERROR";
            return response;
        }
        noble.setDojang((int)userInfo.get("dojang"));
        noble.setLevel((int)userInfo.get("level"));
        noble.setJob((String)userInfo.get("job"));
        int mainChar = ((Integer)noble.getMainChar() != null) ? noble.getMainChar() : 0;
        noble.setMainChar(mainChar);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Log log = new Log();
        log.setNickname(noble.getNickname());
        log.setWhat("정보 수정");
        log.setCreateDt((String)now.format(formatter));
        log.setWho(noble.getGrantor());
        
        if (nobleService.updateNoble(noble) && logService.insertLog(log)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/useExempt")
    public String useExempt(@RequestBody Map<String, Object> param) throws IOException, InterruptedException {
        String response = "ERROR";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        param.put("exemptedDate", ((String)now.format(formatter)));

        Noble noble = nobleService.getNoble((int)param.get("idx"));

        Log log = new Log();
        log.setWhat("면제권 사용");
        log.setNickname(noble.getNickname());
        log.setCreateDt((String)now.format(formatter));
        log.setWho((String)param.get("grantor"));
        
        if (nobleService.useExempt(param) && logService.insertLog(log)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/warning")
    public String warning(@RequestBody Map<String, Object> param) throws IOException, InterruptedException {
        String response = "ERROR";

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Noble noble = nobleService.getNoble((int)param.get("idx"));

        Log log = new Log();
        log.setWhat("경고");
        log.setNickname(noble.getNickname());
        log.setCreateDt((String)now.format(formatter));
        log.setWho((String)param.get("grantor"));
        log.setReason((String)param.get("reason"));
        
        if (nobleService.warning((int)param.get("idx")) && logService.insertLog(log)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/deleteNoble")
    public String deleteNoble(@RequestBody Map<String, Object> param) {
        String response = "ERROR";

        int idx = param.get("idx") != null ? (int)param.get("idx") : -1;
        String reason = param.get("reason") != null ? (String)param.get("reason") : "";
        String grantor = param.get("grantor") != null ? (String)param.get("grantor") : "";

        Noble noble = nobleService.getNoble(idx);

        if (noble.getAdmin() == 1) {
            response = "ADMIN";
        } else {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            Log log = new Log();
            log.setNickname(noble.getNickname());
            log.setCreateDt((String)now.format(formatter));
            log.setWho(grantor);
            log.setReason(reason);

            log.setWhat("노블 길드 탈퇴");
            if (logService.insertLog(log) && nobleService.deleteNoble(idx)) {
                nobleService.deleteSubChar();
                response = "SUCCESS";
            }
        }
        
        return response;
    }

    @Scheduled(cron = "0 0 3 * * 3")
    public String updateAllDojang() throws IOException, InterruptedException {
        String response = "ERROR";

        List<Noble> nobleList = nobleService.getNobleForDojang();

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Log log = new Log();
        log.setWhat("무릉 갱신");
        log.setCreateDt((String)now.format(formatter));

        for (Noble noble : nobleList) {
            Map<String, Object> userInfo = crawlingService.getUserInfo(noble.getNickname());
            noble.setDojang((int)userInfo.get("dojang"));
            noble.setLevel((int)userInfo.get("level"));
            noble.setJob((String)userInfo.get("job"));
            nobleService.updateNoble(noble);
            if (noble.getNickname() == nobleList.get(nobleList.size() - 1).getNickname() && logService.insertLog(log)) {
                response = "SUCCESS";
            }
            Thread.sleep(500);
        }

        return response;
    }

    @PostMapping("/Admin/getCenturyList")
    public List<Century> getCenturyList(@RequestBody Map<String, Object> param) {
        Map<String, Object> searchParam = new HashMap<>();
        
        String searchStr = param.get("searchStr") != null ? (String)param.get("searchStr") : "";
        String order = param.get("order") != null ? (String)param.get("order") : "idx ASC";
        

        searchParam.put("searchStr", searchStr);
        searchParam.put("order", order);

        List<Century> centuryList = centuryService.getCenturyList(searchParam);
        List<Century> response = new ArrayList<>();
        for (Century century : centuryList) {
            if (century.getMainChar() != 0) {
                Noble nobleFromIdx = nobleService.getNoble(century.getMainChar());
                century.setMainCharNick(nobleFromIdx.getNickname());
            }
            response.add(century);
        }

        return centuryList;
    }

    @PostMapping("/Admin/getCentury")
    public Century getCentury(@RequestBody Map<String, Object> param) {
        int idx = param.get("idx") != null ? (int)param.get("idx") : 0;
        Century century = centuryService.getCentury(idx);
        if (century.getMainChar() != 0) {
            century.setMainCharNick(nobleService.getNoble(century.getMainChar()).getNickname());
        }
        return century;
    }

    @PostMapping("/Admin/insertCentury")
    public String insertCentury(@RequestBody Century century) throws IOException, InterruptedException {
        String response = "ERROR";

        Map<String, Object> userInfo = crawlingService.getUserInfo(century.getNickname());
        if ((int)userInfo.get("level") == 0) {
            response = "MGERROR";
            return response;
        }
        century.setLevel((int)userInfo.get("level"));
        century.setJob((String)userInfo.get("job"));
        int mainChar = ((Integer)century.getMainChar() != null) ? century.getMainChar() : 0;
        century.setMainChar(mainChar);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Log log = new Log();
        log.setNickname(century.getNickname());
        log.setWhat("20세기 길드 가입");
        log.setCreateDt((String)now.format(formatter));
        log.setWho(century.getGrantor());

        if (centuryService.insertCentury(century) && logService.insertLog(log)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/updateCentury")
    public String updateCentury(@RequestBody Century century) throws IOException, InterruptedException {
        String response = "ERROR";

        Map<String, Object> userInfo = crawlingService.getUserInfo(century.getNickname());
        if ((int)userInfo.get("level") == 0) {
            response = "MGERROR";
            return response;
        }
        century.setLevel((int)userInfo.get("level"));
        century.setJob((String)userInfo.get("job"));
        int mainChar = ((Integer)century.getMainChar() != null) ? century.getMainChar() : 0;
        century.setMainChar(mainChar);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Log log = new Log();
        log.setNickname(century.getNickname());
        log.setWhat("정보 수정");
        log.setCreateDt((String)now.format(formatter));
        log.setWho(century.getGrantor());
        
        if (centuryService.updateCentury(century) && logService.insertLog(log)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/deleteCentury")
    public String deleteCentury(@RequestBody Map<String, Object> param) {
        String response = "ERROR";

        int idx = param.get("idx") != null ? (int)param.get("idx") : -1;
        String reason = param.get("reason") != null ? (String)param.get("reason") : "";

        Century century = centuryService.getCentury(idx);
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        Log log = new Log();
        log.setNickname(century.getNickname());
        log.setWhat("20세기 길드 탈퇴");
        log.setReason(reason);
        log.setCreateDt((String)now.format(formatter));
        log.setWho((String)param.get("grantor"));

        if (centuryService.deleteCentury(idx) && logService.insertLog(log)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/getDotaxList")
    public List<Dotax> getDotaxList(@RequestBody Map<String, Object> param) {
        Map<String, Object> searchParam = new HashMap<>();
        
        String searchStr = param.get("searchStr") != null ? (String)param.get("searchStr") : "";
        String order = param.get("order") != null ? (String)param.get("order") : "idx ASC";
        
        searchParam.put("searchStr", searchStr);
        searchParam.put("order", order);

        List<Dotax> dotaxList = dotaxService.getDotaxList(searchParam);
        List<Dotax> response = new ArrayList<>();
        for (Dotax dotax : dotaxList) {
            if (dotax.getMainChar() != 0) {
                Noble nobleFromIdx = nobleService.getNoble(dotax.getMainChar());
                dotax.setMainCharNick(nobleFromIdx.getNickname());
            }
            response.add(dotax);
        }

        return response;
    }

    @PostMapping("/Admin/getDotax")
    public Dotax getDotax(@RequestBody Map<String, Object> param) {
        int idx = param.get("idx") != null ? (int)param.get("idx") : 0;
        Dotax dotax = dotaxService.getDotax(idx);
        if (dotax.getMainChar() != 0) {
            dotax.setMainCharNick((nobleService.getNoble(dotax.getMainChar())).getNickname());
        }
        return dotax;
    }

    @PostMapping("/Admin/insertDotax")
    public String insertDotax(@RequestBody Dotax dotax) throws IOException, InterruptedException {
        String response = "ERROR";

        Map<String, Object> userInfo = crawlingService.getUserInfo(dotax.getNickname());
        if ((int)userInfo.get("level") == 0) {
            response = "MGERROR";
            return response;
        }
        dotax.setLevel((int)userInfo.get("level"));
        dotax.setJob((String)userInfo.get("job"));
        int mainChar = ((Integer)dotax.getMainChar() != null) ? dotax.getMainChar() : 0;
        dotax.setMainChar(mainChar);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        Log log = new Log();
        log.setNickname(dotax.getNickname());
        log.setWhat("도탁스 길드 가입");
        log.setCreateDt((String)now.format(formatter));
        log.setWho(dotax.getGrantor());

        if (dotaxService.insertDotax(dotax) && logService.insertLog(log)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/updateDotax")
    public String updateDotax(@RequestBody Dotax dotax) throws IOException, InterruptedException {
        String response = "ERROR";

        Map<String, Object> userInfo = crawlingService.getUserInfo(dotax.getNickname());
        if ((int)userInfo.get("level") == 0) {
            response = "MGERROR";
            return response;
        }
        System.out.println((String)userInfo.get("job"));
        dotax.setLevel((int)userInfo.get("level"));
        dotax.setJob((String)userInfo.get("job"));
        int mainChar = ((Integer)dotax.getMainChar() != null) ? dotax.getMainChar() : 0;
        dotax.setMainChar(mainChar);
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        Log log = new Log();
        log.setNickname(dotax.getNickname());
        log.setWhat("정보 수정");
        log.setCreateDt((String)now.format(formatter));
        log.setWho(dotax.getGrantor());
        
        if (dotaxService.updateDotax(dotax) && logService.insertLog(log)) {
            response = "SUCCESS";
        }

        return response;
    }

    @PostMapping("/Admin/deleteDotax")
    public String deleteDotax(@RequestBody Map<String, Object> param) {
        String response = "ERROR";

        int idx = param.get("idx") != null ? (int)param.get("idx") : -1;
        String reason = param.get("reason") != null ? (String)param.get("reason") : "";

        Dotax dotax = dotaxService.getDotax(idx);

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        Log log = new Log();
        log.setNickname(dotax.getNickname());
        log.setWhat("도탁스 길드 탈퇴");
        log.setReason(reason);
        log.setCreateDt((String)now.format(formatter));
        log.setWho((String)param.get("grantor"));

        if (dotaxService.deleteDotax(idx) && logService.insertLog(log)) {
            response = "SUCCESS";
        }
        
        return response;
    }

    @PostMapping("/Admin/manageList")
    public Map<String, Object> manageList() throws IOException {
        Map<String, Object> response = new HashMap<>();

        List<String> nobleList = nobleService.getNobleNickname();
        List<String> centuryList = centuryService.getCenturyNickname();
        List<String> dotaxList = dotaxService.getDotaxNickname();
        
        List<String> backUpNoble = nobleService.getNobleNickname();
        List<String> backUpCentury = centuryService.getCenturyNickname();
        List<String> backUpDotax = dotaxService.getDotaxNickname();
        
        List<String> nobleRealList = crawlingService.crawlingNobleNickname();
        List<String> centuryRealList = crawlingService.crawlingCenturyNickname();
        List<String> dotaxRealList = crawlingService.crawlingDotaxNickname();

        nobleList.removeAll(nobleRealList);
        centuryList.removeAll(centuryRealList);
        dotaxList.removeAll(dotaxRealList);

        nobleRealList.removeAll(backUpNoble);
        centuryRealList.removeAll(backUpCentury);
        dotaxRealList.removeAll(backUpDotax);
        
        List<Dotax> dotaxerList = dotaxService.getDotaxer();
        List<String> dotaxerStringList = new ArrayList<>();
        for (Dotax dotax : dotaxerList) {
            if (dotax.getMainChar() == 0) {
                dotaxerStringList.add(dotax.getNickname());
            } else {
                Noble noble = nobleService.getNoble(dotax.getMainChar());
                dotaxerStringList.add(dotax.getNickname() + "(" + noble.getNickname() + ")");
            }
        }

        response.put("nobleSheet", nobleList);
        response.put("centurySheet", centuryList);
        response.put("dotaxSheet", dotaxList);
        response.put("nobleGame", nobleRealList);
        response.put("centuryGame", centuryRealList);
        response.put("dotaxGame", dotaxRealList);
        response.put("dotaxer", dotaxerStringList);
        
        return response;
    }

    @PostMapping("/Admin/getNoNobleList")
    public List<Map<String, Object>> getNoNobleList() {
        List<Map<String, Object>> response = new ArrayList<>();

        Map<String, Object> searchParam = new HashMap<>();

        searchParam.put("searchStr", "");
        searchParam.put("order", "idx ASC");

        List<Noble> nobleList = nobleService.getNobleListForTotal(searchParam);
        List<String> nobleNicknameList = new ArrayList<>();
        for (Noble noble : nobleList) {
            if (noble.getDotax() == 1) {
                nobleNicknameList.add(noble.getNickname() + "(도탁스)");        
            } else {
                nobleNicknameList.add(noble.getNickname());
            }
        }

        for (Noble noble : nobleList) {
            Map<String, Object> temp = new HashMap<>();
            temp.put("main", noble.getNickname());
            temp.put("admin", noble.getAdmin());
            temp.put("sub", nobleService.getNobleSubList(noble.getIdx()));
            List<String> centuryList = centuryService.getCenturyListFromMain(noble.getIdx());
            temp.put("century", centuryList);
            List<String> centuryUpperList = centuryService.getCenturyUpperListFromMain(noble.getIdx());
            temp.put("centuryUpper", centuryUpperList);
            List<String> dotaxList = dotaxService.getDotaxListFromMain(noble.getIdx());
            temp.put("dotax", dotaxList);
            List<String> dotaxUpperList = dotaxService.getDotaxUpperListFromMain(noble.getIdx());
            temp.put("dotaxUpper", dotaxUpperList);
            temp.put("exemptedDate", noble.getExemptedDate());
            if ((centuryList.size() < 2 && centuryUpperList.size() > 0) || (dotaxList.size() < 1 && dotaxUpperList.size() > 0)) {
                response.add(temp);
            }
        }

        return response;
    }
}
