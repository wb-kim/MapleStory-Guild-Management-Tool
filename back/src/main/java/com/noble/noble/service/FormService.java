package com.noble.noble.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.noble.noble.data.Form;
import com.noble.noble.mapper.FormMapper;

@Service
@Transactional
public class FormService {
    
    @Autowired private FormMapper formMapper;
    
    public boolean insertForm(Form form) {
        return formMapper.insertForm(form) > 0 ? true : false;
    }

    public List<Form> getFormNotChecked() {   
        return formMapper.getFormNotChecked();
    }
    
    public List<Form> getFormChecked() {   
        return formMapper.getFormChecked();
    }
    
    public boolean join(int idx) {
        return formMapper.join(idx) > 0 ? true : false;
    }

    public Form getForm(int idx) {
        return formMapper.getForm(idx);
    }
}
