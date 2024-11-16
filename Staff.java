public class Staff{
    private String staffID;
    private String name;
    private String role;
    private String gender;
    private int age;

    public Staff(String staffID, String name, String role, String gender, int age) {
        this.staffID = staffID;
        this.name = name;
        this.role = role;
        this.gender = gender;
        this.age = age;
    }

    public String getStaffID() {
        return staffID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String toCSV() {
        return String.join(",", staffID, name, role, gender, String.valueOf(age));
    }

    @Override
    public String toString() {
        return "Staff ID: " + staffID + ", Name: " + name + ", Role: " + role +
                ", Gender: " + gender + ", Age: " + age;
    }
}
