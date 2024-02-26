package cn.exp.proj.module.pds.vo;

import java.time.LocalDateTime;

public interface ISchduleTask {
    void setStartDateTime(LocalDateTime taskStartTime);

    void setEndDateTime(LocalDateTime taskEndTime);

    Integer getStartDay();

    Long getStartTime();

    Integer getEndDay();

    Long getEndTime();
}
