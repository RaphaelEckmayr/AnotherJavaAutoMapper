package net.AJAM.mapper.test.Utils;

import java.time.LocalDate;

public class Person2 {
    public String id;
    public String name;
    public String eMail;
    public LocalDate birthDate;
    public String registrationDate;
    public String phone2;

    public Person2() {
    }

    public Person2(String id, String name, String eMail, LocalDate birthDate, String registrationDate, String phone2) {
        this.id = id;
        this.name = name;
        this.eMail = eMail;
        this.birthDate = birthDate;
        this.registrationDate = registrationDate;
        this.phone2 = phone2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone) {
        this.phone2 = phone;
    }

    @Override
    public String toString() {
        return "Person2{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", eMail='" + eMail + '\'' +
                ", birthDate=" + birthDate +
                ", registrationDate='" + registrationDate + '\'' +
                ", phone='" + phone2 + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person2 person2 = (Person2) o;

        if (id != null ? !id.equals(person2.id) : person2.id != null) return false;
        if (name != null ? !name.equals(person2.name) : person2.name != null) return false;
        if (eMail != null ? !eMail.equals(person2.eMail) : person2.eMail != null) return false;
        if (birthDate != null ? !birthDate.equals(person2.birthDate) : person2.birthDate != null) return false;
        if (registrationDate != null ? !registrationDate.equals(person2.registrationDate) : person2.registrationDate != null)
            return false;
        return phone2 != null ? phone2.equals(person2.phone2) : person2.phone2 == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (eMail != null ? eMail.hashCode() : 0);
        result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (phone2 != null ? phone2.hashCode() : 0);
        return result;
    }
}
