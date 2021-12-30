package com.reactLab.kpi.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@Table("recent_change_entry")
public class RecentChangeEntry {
    @Id
    Long id;
    String user1;
    String title;
    Date datetime;

    public RecentChangeEntry(String user1, String title, Date datetime) {
        this.user1 = user1;
        this.title = title;
        this.datetime = datetime;
    }
}
