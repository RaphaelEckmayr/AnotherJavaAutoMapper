package net.AJAM.mapper.test.Utils;

import java.time.LocalDate;

public class Person3
{
    public int id;
    public String firstname;
    public String lastname;
    public String eMail;
    public String birthDate;
    public LocalDate registrationDate;
    public String phone;

    public Person3() {
    }

    public Person3(int id, String firstname, String lastname, String eMail, String birthDate, LocalDate registrationDate, String phone) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
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
        return "Person3{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", eMail='" + eMail + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", registrationDate=" + registrationDate +
                ", phone='" + phone + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person3 person3 = (Person3) o;

        if (id != person3.id) return false;
        if (firstname != null ? !firstname.equals(person3.firstname) : person3.firstname != null) return false;
        if (lastname != null ? !lastname.equals(person3.lastname) : person3.lastname != null) return false;
        if (eMail != null ? !eMail.equals(person3.eMail) : person3.eMail != null) return false;
        if (birthDate != null ? !birthDate.equals(person3.birthDate) : person3.birthDate != null) return false;
        if (registrationDate != null ? !registrationDate.equals(person3.registrationDate) : person3.registrationDate != null)
            return false;
        return phone != null ? phone.equals(person3.phone) : person3.phone == null;
    }
}
