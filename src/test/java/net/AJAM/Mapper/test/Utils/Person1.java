package net.AJAM.Mapper.test.Utils;

import java.time.LocalDate;

public class Person1
{
    public int id;
    public String name;
    public String eMail;
    public String birthDate;
    public LocalDate registrationDate;
    public String phone;

    public Person1() {
    }

    public Person1(int id, String name, String eMail, String birthDate, LocalDate registrationDate, String phone) {
        this.id = id;
        this.name = name;
        this.eMail = eMail;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Person1{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", eMail='" + eMail + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", registrationDate=" + registrationDate +
                ", phone='" + phone + '\'' +
                '}';
    }
}
