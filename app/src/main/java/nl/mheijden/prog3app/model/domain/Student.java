package nl.mheijden.prog3app.model.domain;

import java.io.Serializable;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class Student implements Serializable {
    private String studentNumber;
    private String firstname;
    private String insertion;
    private String lastname;
    private String email;
    private String phonenumber;
    private String password="";

    public Student(String studentNumber, String firstname, String insertion, String lastname, String email, String phonenumber) {
        this.studentNumber = studentNumber;
        this.firstname = firstname;
        if(insertion!=null){
            this.insertion = insertion;
        } else {
            this.insertion="";
        }
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
    }
    public Student(){
    }
    public Student(String studentNumber){
        this.studentNumber=studentNumber;
    }

    public String getstudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getInsertion() {
        return insertion;
    }

    public void setInsertion(String insertion) {
        this.insertion = insertion;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Student student = (Student) o;

        return studentNumber.equals(student.studentNumber);
    }

    @Override
    public int hashCode() {
        return studentNumber.hashCode();
    }
}
