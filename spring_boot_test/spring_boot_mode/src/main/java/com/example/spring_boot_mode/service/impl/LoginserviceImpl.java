package com.example.spring_boot_mode.service.impl;

import com.example.spring_boot_mode.dao.LoginDao;
import com.example.spring_boot_mode.service.Loginservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LoginserviceImpl implements Loginservice {
    @Autowired
    private LoginDao loginDao;
    @Override
    public List<Map<String, String>> getalltest() {
        return loginDao.getalltest();
    }
}
