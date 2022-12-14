package com.mybatisplus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mybatisplus.entity.Today_Eat;

import java.util.List;

public interface TodayEatService extends IService<Today_Eat> {
public List<Today_Eat> Send_Today_Eat_Message();
public int Studay_Today_Eat_Message(Today_Eat today_eat);
public int Delete_Today_Eat_Message(int id);
public List<Today_Eat> Send_All_message();

    List<Today_Eat> selectMsg(String text);
    public int  select_Id_By_Msg(String text);
    public Today_Eat select_Todayeat_By_id(int id);
}
