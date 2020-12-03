
# SpringBoot的RESTful接口日志

## 一、功能介绍

在SpringBoot的项目应用中，常常需要记录日志到数据库中，例如系统访问日志（登录日志）、用户操作日志等，日志文本中需要含有动态的参数（发送http请求的请求参数），此情景下，可基于Spring Expression Language的动态日志信息记录功能。

## 二、实现方法

```

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
```
