package com.java.utils;

import org.aspectj.lang.ProceedingJoinPoint;

public class Logger {

    public void before(){
        System.out.println("前置通知.....");
    }


    public void afterReturning(){
        System.out.println("后置通知......");
    }

    public void afterThrowing(){
        System.out.println("异常通知.......");
    }

    public void after(){
        System.out.println("最终通知");
    }

    public Object around(ProceedingJoinPoint pjp){
        Object rtValue=null;
        try {
            System.out.println("前置通知.....");
            //获取方法执行所需的参数
            Object[] args = pjp.getArgs();
            //执行.在此之前是前置通知
            rtValue=pjp.proceed(args);
            System.out.println("后置通知......");
            //在此之后是后置通知
            //返回
            return rtValue;
        }catch (Throwable e){
            //异常通知
            System.out.println("异常通知.......");
            throw new RuntimeException(e);
        }finally {
            //最终通知
            System.out.println("最终通知");
        }
    }

}
