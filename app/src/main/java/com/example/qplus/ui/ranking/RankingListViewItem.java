package com.example.qplus.ui.ranking;

public class RankingListViewItem {
    private String rank;
    private String profileuri;
    private String name;
    private Integer stamp;
    private String grade;

    public void setRank(String rank){
        this.rank = rank;
    }
    public void setProfileUri(String profileuri){
        this.profileuri = profileuri;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setStamp(Integer stamp) {
        this.stamp = stamp;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }



    public String getRank(){
        return rank;
    }
    public String getProfileUri(){
        return profileuri;
    }
    public String getName() {
        return name;
    }
    public Integer getStamp() {
        return stamp;
    }
    public String getGrade(){
        return grade;
    }
}
