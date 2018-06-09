package com.bbq.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionId;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "t_user")
public class User extends BaseDomain {

    public static final int USER_LOCK = 1;
    public static final int USER_UNLOCK = 2;
    public static final int FORUM_ADMIN= 3;
    public static final int NORMAL_USER = 4;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_type")
    private int userType = NORMAL_USER;


    @Column(name = "last_ip")
    private String lastIp;

    @Column(name = "last_visit")
    private Date lastVisit;


    private String password;

    private int locked;

    private int credit;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(name = "t_board_manager", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {
            @JoinColumn(name = "board_id")})
    private Set<Board> manBoards = new HashSet<Board>();


    public static int getUserLock() {
        return USER_LOCK;
    }

    public static int getUserUnlock() {
        return USER_UNLOCK;
    }

    public static int getForumAdmin() {
        return FORUM_ADMIN;
    }

    public static int getNormalUser() {
        return NORMAL_USER;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public Date getLastVisit() {
        return lastVisit;
    }

    public void setLastVisit(Date lastVisit) {
        this.lastVisit = lastVisit;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public Set<Board> getManBoards() {
        return manBoards;
    }

    public void setManBoards(Set<Board> manBoards) {
        this.manBoards = manBoards;
    }
}
