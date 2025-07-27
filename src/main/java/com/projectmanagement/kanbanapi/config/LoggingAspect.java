package com.projectmanagement.kanbanapi.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    // Define a pointcut for all public methods within controller package
    @Pointcut("execution(public * com.projectmanagement.kanbanapi.controllers.*.*(..))")
    public void controllerMethods() {}

    // Define a pointcut for all public methods within service package
    @Pointcut("execution(public * com.projectmanagement.kanbanapi.services.*.*(..))")
    public void serviceMethods() {}

    // Combine controller and service pointcuts
    @Pointcut("controllerMethods() || serviceMethods()")
    public void applicationPackagePointcut() {}

    /**
     * Around advice for logging method entry, exit, and execution time.
     * Also handles exceptions.
     *
     * @param joinPoint the join point for the advice
     * @return result of the method call
     * @throws Throwable if the method call throws an exception
     */
    @Around("applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringTypeName();
        String methodName = methodSignature.getName();

        long startTime = System.currentTimeMillis();

        log.info("{}.{}() - ENTRY", className, methodName);

        try {
            Object result = joinPoint.proceed(); // Execute the actual method

            long endTime = System.currentTimeMillis();
            log.info("{}.{}() - EXIT ({}ms) - Result: {}", className, methodName, (endTime - startTime), result != null ? result.getClass().getSimpleName() : "void");
            return result;
        } catch (Throwable e) {
            long endTime = System.currentTimeMillis();
            log.error("!!!! {}.{}() EXCEPTION ({}ms) - Error: {}", className, methodName, (endTime - startTime), e.getMessage(), e);
            throw e; // Re-throw the exception so it propagates normally
        }
    }

    // Optional: You can also log method arguments if needed, but be careful with sensitive data.
    // @Before("applicationPackagePointcut()")
    // public void logBefore(JoinPoint joinPoint) {
    //     MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    //     String className = methodSignature.getDeclaringTypeName();
    //     String methodName = methodSignature.getName();
    //     log.debug("Arguments for {}.{}(): {}", className, methodName, Arrays.toString(joinPoint.getArgs()));
    // }
}
