package com.noble.noble.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.noble.noble.data.Century;

@Mapper
public interface CenturyMapper {
    public int insertCentury(Century century);
    public List<String> getCenturyListFromMain(int mainChar);
    public List<String> getCenturyUpperListFromMain(int mainChar);
    public List<Integer> getMainCharFromCentury(String nickname);
    public List<Century> getCenturyList(Map<String, Object> param);
    public int updateCentury(Century century);
    public int deleteCentury(int idx);
    public int deleteCenturyByNickname(String nickname);
    public Century getCentury(int idx);
    public int deleteCenturyFromMain(int mainChar);
    public List<String> getCenturyNickname();
    public int getCenturyCount();
}
