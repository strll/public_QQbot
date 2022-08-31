package com.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author wxt
 * @since 2022-08-03
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "admin")
public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "admin_id", type = IdType.AUTO)
    private Integer  id;

    private String account;

    private String permission;


    @Override
    public String toString() {
        return "Admin{" +
            " id=" +  id +
            ", account=" + account +
            ", permission=" + permission +
        "}";
    }
}
