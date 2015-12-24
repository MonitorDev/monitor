package com.rongji.websiteMonitor.persistence;
// default package

import java.util.Date;


/**
 * AlarmInformHistory entity. @author MyEclipse Persistence Tools
 */

public class AlarmInformHistory  implements java.io.Serializable {


    // Fields    

     private String aiId;
     private Date aiTime;
     private String aiType;
     private String aiWay;
     private String aiContent;
     private String taskId;


    // Constructors

    /** default constructor */
    public AlarmInformHistory() {
    }

	/** minimal constructor */
    public AlarmInformHistory(String aiId) {
        this.aiId = aiId;
    }
    
    /** full constructor */
    public AlarmInformHistory(String aiId, Date aiTime, String aiType, String aiWay, String aiContent, String taskId) {
        this.aiId = aiId;
        this.aiTime = aiTime;
        this.aiType = aiType;
        this.aiWay = aiWay;
        this.aiContent = aiContent;
        this.taskId = taskId;
    }

   
    // Property accessors

    public String getAiId() {
        return this.aiId;
    }
    
    public void setAiId(String aiId) {
        this.aiId = aiId;
    }

    public Date getAiTime() {
        return this.aiTime;
    }
    
    public void setAiTime(Date aiTime) {
        this.aiTime = aiTime;
    }

    public String getAiType() {
        return this.aiType;
    }
    
    public void setAiType(String aiType) {
        this.aiType = aiType;
    }

    public String getAiWay() {
        return this.aiWay;
    }
    
    public void setAiWay(String aiWay) {
        this.aiWay = aiWay;
    }

    public String getAiContent() {
        return this.aiContent;
    }
    
    public void setAiContent(String aiContent) {
        this.aiContent = aiContent;
    }

    public String getTaskId() {
        return this.taskId;
    }
    
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
   








}