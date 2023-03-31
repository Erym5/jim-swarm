package org.erym.im.common.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author cjt
 * @date 2021/6/20 10:28
 */
public class Message implements Serializable {

    private Long id;

    private Long sender;

    private Long receiver;

    private String content;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", content='" + content + '\'' +
                ", gmtCreate=" + gmtCreate +
                '}';
    }
}
