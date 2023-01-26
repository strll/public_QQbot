/**
  * Copyright 2022 bejson.com 
  */
package com.mybatisplus.utils.Qiniuyun.ResponsePojo;

/**
 * Auto-generated: 2022-12-25 10:44:21
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Details {

    private double score;
    private String label;
    public void setScore(double score) {
         this.score = score;
     }
     public double getScore() {
         return score;
     }

    public void setLabel(String label) {
         this.label = label;
     }
     public String getLabel() {
         return label;
     }

    @Override
    public String toString() {
        return "Details{" +
                "score=" + score +
                ", label='" + label + '\'' +
                '}';
    }
}