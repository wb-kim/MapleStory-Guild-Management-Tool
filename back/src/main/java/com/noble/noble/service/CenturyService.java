package com.noble.noble.service;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noble.noble.data.Century;
import com.noble.noble.mapper.CenturyMapper;

@Service
@Transactional
public class CenturyService {
    
    @Autowired private CenturyMapper centuryMapper;
    
    public boolean insertCentury(Century century) {
        return centuryMapper.insertCentury(century) > 0 ? true : false;
    }

    public List<String> getCenturyListFromMain(int mainChar) {
        return centuryMapper.getCenturyListFromMain(mainChar);
    }

    public List<String> getCenturyUpperListFromMain(int mainChar) {
        return centuryMapper.getCenturyUpperListFromMain(mainChar);
    }

    public List<Integer> getMainCharFromCentury(String nickname) {
        return centuryMapper.getMainCharFromCentury(nickname);
    }

    public List<Century> getCenturyList(Map<String, Object> param) {
        return centuryMapper.getCenturyList(param);
    }

    public boolean deleteCentury(int idx) {
        return centuryMapper.deleteCentury(idx) > 0 ? true : false;        
    }

    public Century getCentury(int idx) {
        return centuryMapper.getCentury(idx);
    }

    public boolean updateCentury(Century century) {
        return centuryMapper.updateCentury(century) > 0 ? true : false;
    }
    
    public boolean deleteCenturyFromMain(int mainChar) {
        return centuryMapper.deleteCenturyFromMain(mainChar) > 0 ? true : false;        
    }

    public boolean deleteCenturyByNickname(String nickname) {
        return centuryMapper.deleteCenturyByNickname(nickname) > 0 ? true : false;        
    }

    public List<String> getCenturyNickname() {
        return centuryMapper.getCenturyNickname();
    }

    public int getCenturyCount() {
        return centuryMapper.getCenturyCount();
    }
}
