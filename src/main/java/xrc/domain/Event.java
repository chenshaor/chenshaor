package xrc.domain;

import java.sql.Time;

import java.util.Date;

public class Event {

    private Integer id;
    private String username;
    private String voteItem;
    private String voterList;
    private Date timeKeyStart;
    private Date timeVoteStart;
    private Date timeSumStart;
    private Date timeSumEnd;

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", voteItem='" + voteItem + '\'' +
                ", voterList='" + voterList + '\'' +
                ", timeKeyStart=" + timeKeyStart +
                ", timeVoteStart=" + timeVoteStart +
                ", timeSumStart=" + timeSumStart +
                ", timeSumEnd=" + timeSumEnd +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVoteItem() {
        return voteItem;
    }

    public void setVoteItem(String voteItem) {
        this.voteItem = voteItem;
    }

    public String getVoterList() {
        return voterList;
    }

    public void setVoterList(String voterList) {
        this.voterList = voterList;
    }

    public Date getTimeKeyStart() {
        return timeKeyStart;
    }

    public void setTimeKeyStart(Date timeKeyStart) {
        this.timeKeyStart = timeKeyStart;
    }

    public Date getTimeVoteStart() {
        return timeVoteStart;
    }

    public void setTimeVoteStart(Date timeVoteStart) {
        this.timeVoteStart = timeVoteStart;
    }

    public Date getTimeSumStart() {
        return timeSumStart;
    }

    public void setTimeSumStart(Date timeSumStart) {
        this.timeSumStart = timeSumStart;
    }

    public Date getTimeSumEnd() {
        return timeSumEnd;
    }

    public void setTimeSumEnd(Date timeSumEnd) {
        this.timeSumEnd = timeSumEnd;
    }
}
