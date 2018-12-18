package com.transaction.entity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_config")
public class SysConfig {
    @Id
    private String variable;

    private String value;

    @Column(name = "set_time")
    private Date setTime;

    @Column(name = "set_by")
    private String setBy;

    /**
     * @return variable
     */
    public String getVariable() {
        return variable;
    }

    /**
     * @param variable
     */
    public void setVariable(String variable) {
        this.variable = variable;
    }

    /**
     * @return value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return set_time
     */
    public Date getSetTime() {
        return setTime;
    }

    /**
     * @param setTime
     */
    public void setSetTime(Date setTime) {
        this.setTime = setTime;
    }

    /**
     * @return set_by
     */
    public String getSetBy() {
        return setBy;
    }

    /**
     * @param setBy
     */
    public void setSetBy(String setBy) {
        this.setBy = setBy;
    }
}