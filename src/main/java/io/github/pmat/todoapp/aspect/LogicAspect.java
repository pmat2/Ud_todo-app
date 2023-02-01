package io.github.pmat.todoapp.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogicAspect {

    public static final Logger logger = LoggerFactory.getLogger(LogicAspect.class);

    private final Timer groupCreateTimer;

    public LogicAspect(final MeterRegistry registry) {
        groupCreateTimer = registry.timer("logic.project.create.group");
    }

    @Before("execution(* io.github.pmat.todoapp.service.ProjectService.createGroup(..))")
    void logMethodCall(JoinPoint jp){
        logger.info("[logMethodCall] - signature: {}, args: {}", jp.getSignature().getName(), jp.getArgs());
    }

    @Around("execution(* io.github.pmat.todoapp.service.ProjectService.createGroup(..))")
    Object aroundProjectCreateGroup(ProceedingJoinPoint joinPoint) {
        logger.info("[aroundProjectCreateGroup] - recoding");
        return groupCreateTimer.record(() -> {
            try {
                logger.info("[aroundProjectCreateGroup] - proceeding");
                return joinPoint.proceed();
            } catch (Throwable e) {
                if (e instanceof  RuntimeException){
                    throw (RuntimeException) e;
                }
                throw new RuntimeException(e);
            }
        });
    }
}
