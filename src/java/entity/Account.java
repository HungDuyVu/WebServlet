package entity;

import java.util.Date;

public class Account {
    public enum Status {
        IS_ACTIVE("isActive"),
        IS_BLOCK("isBlock"),
        IS_DELETE("isDelete"),;

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static Status fromValue(String value) {
            for (Status status : Status.values()) {
                if (status.value.equals(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown status: " + value);
        }
    }

    private int id;
    private String nameAccount; 
    private String user;
    private String pass;
    private int isSell;
    private int isAdmin;
    private Status status; // Trạng thái sử dụng enum
    private Date deletedAt;

    public Account() {
    }

    public Account(int id, String nameAccount, String user, String pass, int isSell, int isAdmin, Status status, Date deletedAt) {
        this.id = id;
        this.nameAccount = nameAccount;
        this.user = user;
        this.pass = pass;
        this.isSell = isSell;
        this.isAdmin = isAdmin;
        this.status = status;
        this.deletedAt = deletedAt;
    }

    // Getter và setter cho các trường
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameAccount() {
        return nameAccount;
    }

    public void setNameAccount(String nameAccount) {
        this.nameAccount = nameAccount;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getIsSell() {
        return isSell;
    }

    public void setIsSell(int isSell) {
        this.isSell = isSell;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", nameAccount=" + nameAccount + ", user=" + user + ", pass=" + pass + ", isSell=" + isSell + ", isAdmin=" + isAdmin + ", status=" + status + ", deletedAt=" + deletedAt + '}';
    }

    
}
