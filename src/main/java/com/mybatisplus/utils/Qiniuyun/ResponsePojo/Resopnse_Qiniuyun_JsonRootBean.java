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
public class Resopnse_Qiniuyun_JsonRootBean {

    private String message;
    private int code;
    private Result result;

    public void setMessage(String message) {
         this.message = message;
     }
     public String getMessage() {
         return message;
     }

    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

    public void setResult(Result result) {
         this.result = result;
     }
     public Result getResult() {
         return result;
     }

     public String returnMessage_Mean(){
         String suggestion = null;
         switch (this.result.getSuggestion()){
             case "block":
                 suggestion="表示系统确认审核内容违规，建议您将其删除";
                 break;
             case "review":
                 suggestion="表示系统无法确认审核内容是否违规，建议您进行人工复核";
                 break;
             case "pass":
                 suggestion="表示系统确认审核内容正常，建议您忽略该文件";
                 break;
         }
         return suggestion;
     }

    public String returnMessage(){
        String suggestion = null;
        switch (this.result.getSuggestion()){
            case "block":
                suggestion="no";
                break;
            case "review":
                suggestion="do_not_konw";
                break;
            case "pass":
                suggestion="yes";
                break;
        }
        return suggestion;
    }


    @Override
    public String toString() {
        return "Resopnse_Qiniuyun_JsonRootBean{" +
                "message='" + message.toString() + '\'' +
                ", code=" + code +
                ", result=" + result.toString() +
                '}';
    }
}