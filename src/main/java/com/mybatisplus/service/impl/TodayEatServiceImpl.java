package com.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mybatisplus.entity.Today_Eat;
import com.mybatisplus.mapper.Today_EatMapper;
import com.mybatisplus.service.TodayEatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.commons.lang.math.RandomUtils.nextDouble;

@Service
public class TodayEatServiceImpl extends ServiceImpl<Today_EatMapper, Today_Eat> implements TodayEatService {
    @Autowired
    private Today_EatMapper todayEatMapper;

    @Override
    public  List<Today_Eat> Send_Today_Eat_Message() {
        List<Today_Eat> today_eats = todayEatMapper.selectList(null);

        return today_eats;
    }

    @Override
    public int Studay_Today_Eat_Message(Today_Eat today_eat) {
        return todayEatMapper.insert(today_eat);
    }

    @Override
    public int Delete_Today_Eat_Message(int id) {
        return todayEatMapper.deleteById(id);

    }

    @Override
    public List<Today_Eat> Send_All_message() {
        List<Today_Eat> today_eats = todayEatMapper.selectList(null);
        return today_eats;
    }

    @Override
    public List<Today_Eat> selectMsg(String text) {
        QueryWrapper<Today_Eat> today_eatQueryWrapper = new QueryWrapper<>();
        today_eatQueryWrapper.like("message",text);
        List<Today_Eat> today_eats = todayEatMapper.selectList(today_eatQueryWrapper);
        return today_eats;
    }

    @Override
    public int select_Id_By_Msg(String text) {
        QueryWrapper<Today_Eat> today_eatQueryWrapper = new QueryWrapper<>();
        today_eatQueryWrapper.eq("message",text);
        Today_Eat today_eats = todayEatMapper.selectOne(today_eatQueryWrapper);
        return today_eats.getId();
    }
    public Today_Eat select_Todayeat_By_id(int id){
        QueryWrapper<Today_Eat> today_eatQueryWrapper = new QueryWrapper<>();
        today_eatQueryWrapper.eq("id", id);
        Today_Eat today_eats = todayEatMapper.selectOne(today_eatQueryWrapper);
        return today_eats;
    };

}
