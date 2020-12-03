package com.tang.demo.aspect;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import com.tang.demo.annotation.Log;

@Aspect
@Component
public class LogAdvice {

	private final static Logger logger = LoggerFactory.getLogger(LogAdvice.class);

	private final static ExpressionParser spelParser = new SpelExpressionParser();

	@Pointcut("@annotation(com.tang.demo.annotation.Log)")
	public void methodAspect() {
	}

	@After("methodAspect()")
	public void after(JoinPoint joinPoint) throws IOException {
		logger.info("开始记录日志*************************");

		// 获取方法的参数名和参数值
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		List<String> paramNameList = Arrays.asList(methodSignature.getParameterNames());
		List<Object> paramList = Arrays.asList(joinPoint.getArgs());

		// 将方法的参数名和参数值一一对应的放入上下文中
		EvaluationContext ctx = new StandardEvaluationContext();
		for (int i = 0; i < paramNameList.size(); i++) {
			ctx.setVariable(paramNameList.get(i), paramList.get(i));
		}

		Method method = methodSignature.getMethod();
		Log myAnnotation = method.getAnnotation(Log.class);

		// 解析SpEL表达式获取结果
		String description = spelParser.parseExpression(myAnnotation.description()).getValue(ctx).toString();

		saveLog(myAnnotation.name(), description);
	}

	@AfterReturning(pointcut = "methodAspect()", returning = "returnValue")
	public void afterreturningJoinPoint(JoinPoint joinPoint, Object returnValue) {
	}

	@Around("methodAspect()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		Object object = pjp.proceed();
		return object;
	}

	public static void saveLog(String name, String description) {
		logger.info(name + "----->" + description);
	}

}