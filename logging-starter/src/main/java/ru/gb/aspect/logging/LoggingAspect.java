package ru.gb.aspect.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j // Slf4j - Simple logging facade for java
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

  private final LoggingProperties properties;

  @Pointcut("@annotation(ru.gb.aspect.logging.Logging)") // method
  public void loggingMethodsPointcut() {}

  @Pointcut("@within(ru.gb.aspect.logging.Logging)") // class
  public void loggingTypePointcut() {}

  @Around(value = "loggingMethodsPointcut() || loggingTypePointcut()")
  public Object loggingMethod(ProceedingJoinPoint pjp) throws Throwable {
    String methodName = pjp.getSignature().getName();
    log.atLevel(properties.getLevel()).log("Before -> TimesheetService#{}", methodName);
    try {
     return pjp.proceed();
    } finally {
      log.atLevel(properties.getLevel()).log("After -> TimesheetService#{}", methodName);
    }
  }

  @Around(value = "loggingMethodsPointcut() || loggingTypePointcut()")
  public Object logMethodArguments(ProceedingJoinPoint joinPoint) throws Throwable {
    if (properties.isPrintArgs()) {
      log.atLevel(properties.getLevel()).log("Method arguments: {}", Arrays.toString(joinPoint.getArgs().clone()));
    }
    return joinPoint.proceed();
  }

}
