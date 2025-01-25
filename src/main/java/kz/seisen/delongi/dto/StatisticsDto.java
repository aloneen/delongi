package kz.seisen.delongi.dto;

import lombok.Data;

@Data
public class StatisticsDto {

    private String name;
    private int count;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
