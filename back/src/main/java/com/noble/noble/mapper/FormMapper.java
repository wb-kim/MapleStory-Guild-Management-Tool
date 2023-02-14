package com.noble.noble.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.noble.noble.data.Form;

@Mapper
public interface FormMapper {
    public int insertForm(Form form);
    public int join(int idx);
    public Form getForm(int idx);
    public List<Form> getFormNotChecked();
    public List<Form> getFormChecked();
}
