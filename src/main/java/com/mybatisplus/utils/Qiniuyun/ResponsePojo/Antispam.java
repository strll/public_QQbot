/**
  * Copyright 2022 bejson.com 
  */
package com.mybatisplus.utils.Qiniuyun.ResponsePojo;
import java.util.List;

/**
 * Auto-generated: 2022-12-25 10:44:21
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Antispam {
    @Override
    public String toString() {
        return "Antispam{" +
                "details=" + details +
                ", suggestion='" + suggestion + '\'' +
                '}';
    }

    private List<Details> details;
    private String suggestion;
    public void setDetails(List<Details> details) {
         this.details = details;
     }
     public List<Details> getDetails() {
         return details;
     }

    public void setSuggestion(String suggestion) {
         this.suggestion = suggestion;
     }
     public String getSuggestion() {
         return suggestion;
     }

}