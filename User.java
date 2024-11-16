public abstract class User {
    protected String hospitalID;
    protected String password;
    protected String name;
    protected boolean firstLogin;
  

    // Constructor method
    public User(String hospitalID, String password, String name) {
        this.hospitalID = hospitalID;
        PasswordUtils passwordUtils = new PasswordUtils();
        this.password = passwordUtils.hashPassword(password);
        this.name = name;   
        this.firstLogin = true; // Default to true for first login
    }

    // Setter for Hospital ID
    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    // Getter for Hospital ID
    public String getHospitalID() {
        return hospitalID;
    }

    //Getter for Name
    public String getName(){
        return name;
    }

    // Login Method
    public boolean login(String hospitalID, String password) {
        return this.hospitalID.equals(hospitalID) && this.password.equals(password);
    }

    // Change Password Method
    public void setPassword(String newPassword) {
        password = newPassword;
        System.out.println("Password changed successfully.");
    }

    // Getter for Password
    public String getPassword() {
        return password;
    }

    // Abstract Method
    public abstract void displayMenu();

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }
}