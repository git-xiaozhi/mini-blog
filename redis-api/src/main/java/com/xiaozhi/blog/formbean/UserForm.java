package com.xiaozhi.blog.formbean;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class UserForm {

    @Email
    @NotBlank
    private String name;

    @Pattern(regexp="([-_a-zA-Z0-9\u4e00-\u9fa5]{2,20}+)")
    private String nickname;

    private String pass;

    private String pass2;

    private String company;
    private String school;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getPass2() {
        return pass2;
    }
    public void setPass2(String pass2) {
        this.pass2 = pass2;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public String getSchool() {
        return school;
    }
    public void setSchool(String school) {
        this.school = school;
    }


    @Override
    public String toString() {
        return "UserForm [name=" + name + ", nickname=" + nickname + ", pass="
                + pass + ", pass2=" + pass2 + ", company=" + company
                + ", school=" + school + "]";
    }



}
