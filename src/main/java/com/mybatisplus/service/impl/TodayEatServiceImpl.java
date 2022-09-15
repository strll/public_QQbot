package com.mybatisplus.service.impl;

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
    public Today_Eat Send_Today_Eat_Message() {
        List<Today_Eat> today_eats = todayEatMapper.selectList(null);
        int size = today_eats.size();
        double v = nextDouble();
        double v1 = size * v;
        Today_Eat today_eat = today_eats.get((int) v1);
        return today_eat;
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

}
