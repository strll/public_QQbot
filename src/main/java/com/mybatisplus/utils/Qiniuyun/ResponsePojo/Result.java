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
public class Result {
    @Override
    public String toString() {
        return "Result{" +
                "scenes=" + scenes +
                ", suggestion='" + suggestion + '\'' +
                '}';
    }

    private Scenes scenes;
    private String suggestion;
    public void setScenes(Scenes scenes) {
         this.scenes = scenes;
     }
     public Scenes getScenes() {
         return scenes;
     }

    public void setSuggestion(String suggestion) {
         this.suggestion = suggestion;
     }
     public String getSuggestion() {
         return suggestion;
     }

}