package nl.mheijden.prog3app.model.domain;

/**
 * Gemaakt door Maarten van der Heijden on 9-1-2018.
 */

public class Student {
    private String studentNumber;
    private String firstname;
    private String insertion;
    private String lastname;
    private String email;
    private String phonenumber;

    public Student(String studentNumber, String firstname, String insertion, String lastname, String email, String phonenumber) {
        this.studentNumber = studentNumber;
        this.firstname = firstname;
        this.insertion = insertion;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public Student(){

    }

    @Override
    public String toString() {
        return "Student{" +
                "studentNumber='" + studentNumber + '\'' +
                ", firstname='" + firstname + '\'' +
                ", insertion='" + insertion + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                '}';
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
}
