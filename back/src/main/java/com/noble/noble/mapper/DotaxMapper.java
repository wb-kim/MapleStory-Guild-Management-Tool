package com.noble.noble.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.noble.noble.data.Dotax;

@Mapper
public interface DotaxMapper {
    public int insertDotax(Dotax dotax);
    public List<String> getDotaxListFromMain(int mainChar);
    public List<String> getDotaxUpperListFromMain(int mainChar);
    public List<Integer> getMainCharFromDotax(String nickname);
    public List<Dotax> getDotaxList(Map<String, Object> param);
    public int updateDotax(Dotax dotax);
    public int deleteDotax(int idx);
    public Dotax getDotax(int idx);
    public int deleteDotaxFromMain(int mainChar);
    public int deleteDotaxByNickname(String nickname);
    public List<String> getDotaxNickname();
    public int getDotaxCount();
    public List<Dotax> getDotaxer();
}
