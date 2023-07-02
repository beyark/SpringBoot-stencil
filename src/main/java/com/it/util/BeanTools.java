package com.it.util;

import com.it.exception.CustomError;
import com.it.exception.CustomErrorType;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Objects;

public class BeanTools extends BeanUtils {
    public static void copyList(List sourceList, List targetList,Class targetClass) {
        if ((!Objects.isNull(sourceList)) && (!Objects.isNull(targetList))) {
            //如果sourceList与targetList是list的子类，先复制该子类的其它普通属性
            //如com.github.pagehelper.Page，该类基于ArrayList进行扩展
            BeanUtils.copyProperties(sourceList, targetList);
            sourceList.forEach(item -> {
                try {
                    Object data = targetClass.newInstance();
                    BeanUtils.copyProperties(item, data);
                    targetList.add(data);
                } catch (InstantiationException e) {
                    throw new CustomError(CustomErrorType.OTHER_ERROR,"实例化对象出错！");
                } catch (IllegalAccessException e) {
                    throw new CustomError(CustomErrorType.OTHER_ERROR,"没有权限访问对象私有属性或方法！");
                }
            });
        }
    }
}
