>微信公众号：吉姆餐厅ak
学习更多源码知识，欢迎关注。

![在这里插入图片描述](https://img-blog.csdnimg.cn/2018121521020813.jpg)
-------


[**SpringBoot2 | SpringBoot启动流程源码分析（一）**](https://blog.csdn.net/woshilijiuyi/article/details/82219585)

[**SpringBoot2 | SpringBoot启动流程源码分析（二）**](https://blog.csdn.net/woshilijiuyi/article/details/82350057)

[**SpringBoot2 | @SpringBootApplication注解 自动化配置流程源码分析（三）**](https://blog.csdn.net/woshilijiuyi/article/details/82388509)

[**SpringBoot2 | SpringBoot Environment源码分析（四）**](https://blog.csdn.net/woshilijiuyi/article/details/82720478)

[**SpringBoot2 | SpringBoot自定义AutoConfiguration | SpringBoot自定义starter（五）**](https://blog.csdn.net/woshilijiuyi/article/details/82792601)

[**SpringBoot2 | SpringBoot监听器源码分析 | 自定义ApplicationListener（六）**](https://blog.csdn.net/woshilijiuyi/article/details/82805649)

[**SpringBoot2 | 条件注解@ConditionalOnBean原理源码深度解析（七）**](https://blog.csdn.net/woshilijiuyi/article/details/84147483)

[**SpringBoot2 | Spring AOP 原理源码深度剖析（八）**](https://blog.csdn.net/woshilijiuyi/article/details/83934407)

[**SpringBoot2 | SpingBoot FilterRegistrationBean 注册组件 | FilterChain 责任链源码分析（九）**](https://blog.csdn.net/woshilijiuyi/article/details/85014183)


## 概述
AOP(Aspect-Oriented Programming) 面向切面编程。Spring Aop 在 Spring框架中的地位举足轻重，主要用于实现事务、缓存、安全等功能。本篇主要是对源码进行深度分析。

主要介绍以下三个方面：

- Spring AOP 多种代理机制相关核心类介绍。
- Spring Boot 中AOP注解方式源码分析。
- Spring Boot 1.x 版本和 2.x版本 AOP 默认配置的变动。

------
## Spring AOP 多种代理机制相关核心类介绍
先介绍一些Spring Aop中一些核心类，大致分为三类：
- `advisorCreator`，继承 spring ioc的扩展接口 beanPostProcessor，主要用来扫描获取 advisor。
- `advisor`：顾问的意思，封装了spring aop中的切点和通知。
- `advice`：通知，也就是aop中增强的方法。

对以上三类核心类对应的 UML 分别来看。
### advisorCreator：
![在这里插入图片描述](https://img-blog.csdnimg.cn/2018111820182284.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

- `AbstractAutoProxyCreator`：Spring 为Spring AOP 模块暴露的可扩展抽象类，也是 AOP 中最核心的抽象类。`Nepxion Matrix` 框架便是基于此类对AOP进行扩展和增强。

- `BeanNameAutoProxyCreator`：根据指定名称创建代理对象（阿里大名鼎鼎的连接池框架`druid`也基于此类做了扩展）。通过设置 advisor，可以对指定的 beanName 进行代理。支持模糊匹配。

- `AbstractAdvisorAutoProxyCreator`：功能比较强大，默认扫描所有`Advisor`的实现类。相对于根据Bean名称匹配，该类更加灵活。动态的匹配每一个类，判断是否可以被代理，并寻找合适的增强类，以及生成代理类。

- `DefaultAdvisorAutoProxyCreator`：`AbstractAdvisorAutoProxyCreator`的默认实现类。可以单独使用，在框架中使用AOP，尽量不要手动创建此对象。

- `AspectJAwareAdvisorAutoProxyCreator`：Aspectj的实现方式，也是Spring Aop中最常用的实现方式，如果用注解方式，则用其子类`AnnotationAwareAspectJAutoProxyCreator`。

- `AnnotationAwareAspectJAutoProxyCreator`：目前最常用的AOP使用方式。spring aop 开启注解方式之后，该类会扫描所有`@Aspect()`注释的类，生成对应的`adviosr`。目前`SpringBoot`框架中默认支持的方式，自动配置。


### advisor：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181125104618106.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

- `StaticMethodMatcherPointcut`：静态方法切面，抽象类。定义了一个`classFilter`，通过重写`getClassFilter()`方法来指定切面规则。另外实现了`StaticMethodMatcher`接口，通过重写`matches`来指定方法匹配规则。

- `StaticMethodMatcherPointcutAdvisor`：静态方法匹配切面顾问，同未抽象类，扩展了切面排序方法。

- `NameMatchMethodPointcut`：名称匹配切面，通过指定方法集合变量`mappedNames`，模糊匹配。

- `NameMatchMethodPointcutAdvisor`：方法名称切面顾问，内部封装了
`NameMatchMethodPointcut`，通过设置方法名称模糊匹配规则和通知来实现切面功能。

- `RegexpMethodPointcutAdvisor`：正则表达式切面顾问，可设置多个正则表达式规则，通过内部封装的
`JdkRegexpMethodPointcut`解析正则表达式。

- `DefaultPointcutAdvisor`：默认切面顾问，比较灵活。可自由组合切面和通知。
- `InstantiationModelAwarePointcutAdvisorImpl`：`springboot`自动装配的顾问类型，也是最常用的一种顾问实现。在注解实现的切面中，所有`@Aspect`类，都会被解析成该对象。

--------



### advice：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181118201911474.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

- `AspectJMethodBeforeAdvice`：前置通知，AspectJ中 before 属性对应的通知（@Before标注的方法会被解析成该通知），，在切面方法执行之前执行。
- `AspectJAfterReturningAdvice`：后置通知，AspectJ中 afterReturning 属性对应的通知（@AfterReturning 标注的方法会被解析成该通知），在切面方法执行之后执行，如果有异常，则不执行。
注意：该通知与`AspectJMethodBeforeAdvice`对应。
- `AspectJAroundAdvice`：环绕通知，AspectJ中 around 属性对应的通知（@Around标注的方法会被解析成该通知），在切面方法执行前后执行。
- `AspectJAfterAdvice`：返回通知，AspectJ中 after 属性对应的通知（@After 标注的方法会被解析成该通知），不论是否异常都会执行。

-----

可以看出 Spring AOP 提供的实现方式很多，但是殊途同归。

具体使用方式已上传至 github：
https://github.com/admin801122/springboot2-spring5-studying
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181125225045547.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

----


## Spring Boot 中AOP注解方式源码分析

`Spring Aop`使用方式很多，从上面的 API 也可以看出。本篇就基于最常用的注解实现方式，对源码深入分析。

```
@Aspect
@Component
public class LogableAspect {

    @Pointcut("@annotation(com.springboot2.spring5.springAop.aspect.Logable)")
    public void aspect() {
    }
    
    @Around("aspect()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        //...
        Object returnValue =  point.proceed(point.getArgs());
        //...
        return returnValue;
    }
}
```

这是实际项目中，使用AOP最常见的形式，基于注解实现。如今`springboot`大行其道，我们就从`springboot`中的`EnableAspectJAutoProxy`自动配置开始。

----

**大致流程主要分为三个步骤：
1： 创建`AnnotationAwareAspectJAutoProxyCreator`对象
2： 扫描容器中的切面，创建`PointcutAdvisor`对象
3： 生成代理类**

------


分别来分析以上三个步骤。


####  1： 创建`AnnotationAwareAspectJAutoProxyCreator`对象

首先来看`AnnotationAwareAspectJAutoProxyCreator`对象初始化的过程。`springboot`中，aop同样以自动装配的方式，所以还是要从`spring.factories`开始：
```
# Auto Configure
org.springframework.boot.autoconfigure.aop.AopAutoConfiguration,\
```
```
@Configuration
@ConditionalOnClass({ EnableAspectJAutoProxy.class, Aspect.class, Advice.class,
		AnnotatedElement.class })
@ConditionalOnProperty(prefix = "spring.aop", name = "auto", havingValue = "true", matchIfMissing = true)
public class AopAutoConfiguration {

	@Configuration
	@EnableAspectJAutoProxy(proxyTargetClass = false)
	@ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "false", matchIfMissing = false)
	public static class JdkDynamicAutoProxyConfiguration {

	}

	@Configuration
	@EnableAspectJAutoProxy(proxyTargetClass = true)
	@ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "true", matchIfMissing = true)
	public static class CglibAutoProxyConfiguration {

	}

}
```
具体来看：
（1）该配置类的加载前提是什么？
```
@ConditionalOnClass({ EnableAspectJAutoProxy.class, Aspect.class, Advice.class,
		AnnotatedElement.class })
```
条件注解依赖的配置类均被引入到`spring-boot-starter-aop`中，只需引入该依赖即可自动装配。
而且可以看到`spring.aop.auto`默认为`true`，并不需要手动开启。
所以很多同学在`springboot`项目中使用 aop 的时候，习惯在启动类上引入`@EnableAspectJAutoProxy`，其实完全不必要。保证项目中有`spring-boot-starter-aop`依赖即可。

（2）上述代码通过`spring.aop.proxy-target-class`变量来控制`proxyTargetClass`的变量，最终都会加载`@EnableAspectJAutoProxy`配置。
`spring.aop.proxy-target-class`默认为`true`，该变量相当关键，控制 spring aop 代理类的生成方式，具体后面详细介绍。

------

继续跟进`EnableAspectJAutoProxy`：
```
class AspectJAutoProxyRegistrar implements ImportBeanDefinitionRegistrar {

	@Override
	public void registerBeanDefinitions(
			AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {

		//注册 AnnotationAwareAspectJAutoProxyCreator
		AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary(registry);

		AnnotationAttributes enableAspectJAutoProxy =
				AnnotationConfigUtils.attributesFor(importingClassMetadata, EnableAspectJAutoProxy.class);
		//将 aop 代理方式相关的变量设置到 AopConfigUtils，创建代理类时会读取变量
		if (enableAspectJAutoProxy != null) {
			if (enableAspectJAutoProxy.getBoolean("proxyTargetClass")) {
				AopConfigUtils.forceAutoProxyCreatorToUseClassProxying(registry);
			}
			if (enableAspectJAutoProxy.getBoolean("exposeProxy")) {
				AopConfigUtils.forceAutoProxyCreatorToExposeProxy(registry);
			}
		}
	}
}
```
```
	@Nullable
	public static BeanDefinition registerAspectJAnnotationAutoProxyCreatorIfNecessary(BeanDefinitionRegistry registry,
			@Nullable Object source) {

		return registerOrEscalateApcAsRequired(AnnotationAwareAspectJAutoProxyCreator.class, registry, source);
	}
```
上述代码可以看到注册了一个切面相关`BeanDefinition`，正是上面提到的类：
`AnnotationAwareAspectJAutoProxyCreator`，并设置了代理方式配置变量： `proxyTargetClass`，默认为true。

这里只是创建`BeanDefinition`，并没有实例化和初始化该对象。那什么时候会触发呢？
上面的 uml 图可以看到，该类继承的顶层接口为 `BeanPostProcessor`。我们知道`BeanPostProcessor`实现类会提前初始化，由`PostProcessorRegistrationDelegate`触发，具体细节之前博客有提到：
[SpringBoot2 | @SpringBootApplication注解 自动化配置流程源码分析（三）](https://blog.csdn.net/woshilijiuyi/article/details/82388509)

该类又继承`BeanFactoryAware`，所以在其在实例化 bean 后，会触发`setBeanFactory()`方法，最终会触发
`initBeanFactory`方法：
```
	@Override
	protected void initBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		super.initBeanFactory(beanFactory);
		if (this.aspectJAdvisorFactory == null) {
			//advisor 工厂类
			this.aspectJAdvisorFactory = new ReflectiveAspectJAdvisorFactory(beanFactory);
		}
		//用于创建 advisor
		this.aspectJAdvisorsBuilder =
				new BeanFactoryAspectJAdvisorsBuilderAdapter(beanFactory, this.aspectJAdvisorFactory);
	}
```
至此，`AnnotationAwareAspectJAutoProxyCreator BeanDefinition`创建完毕。

-----


#### 2： 扫描容器中的切面，创建`PointcutAdvisor`对象


在spring ioc流程加载的过程中，会触发 `beanPostProcessor` 扩展接口，
而`AnnotationAwareAspectJAutoProxyCreator`又是`SmartInstantiationAwareBeanPostProcessor`的子类，所以该扩展接口正是 aop 实现的入口。

该接口的触发在实例化 bean 之后，初始化 bean之前，具体来看：
```
@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		Object cacheKey = getCacheKey(beanClass, beanName);

		if (!StringUtils.hasLength(beanName) || !this.targetSourcedBeans.contains(beanName)) {
			//advisedBeans用于存储不可代理的bean，如果包含直接返回
			if (this.advisedBeans.containsKey(cacheKey)) {
				return null;
			}
			//判断当前bean是否可以被代理，然后存入advisedBeans
			if (isInfrastructureClass(beanClass) || shouldSkip(beanClass, beanName)) {
				this.advisedBeans.put(cacheKey, Boolean.FALSE);
				return null;
			}
		}

		// Create proxy here if we have a custom TargetSource.
		// Suppresses unnecessary default instantiation of the target bean:
		// The TargetSource will handle target instances in a custom fashion.
		//到这里说明该bean可以被代理，所以去获取自定义目标类，如果没有定义，则跳过。
		TargetSource targetSource = getCustomTargetSource(beanClass, beanName);
		if (targetSource != null) {
			if (StringUtils.hasLength(beanName)) {
				this.targetSourcedBeans.add(beanName);
			}
			Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(beanClass, beanName, targetSource);
			Object proxy = createProxy(beanClass, beanName, specificInterceptors, targetSource);
			this.proxyTypes.put(cacheKey, proxy.getClass());
			//如果最终可以获得代理类，则返回代理类，直接执行实例化后置通知方法
			return proxy;
		}

		return null;
	}
```

来看一下判定 bean 是否被代理的方法依据：

```
	@Override
	protected boolean isInfrastructureClass(Class<?> beanClass) {
		return (super.isInfrastructureClass(beanClass) ||
				(this.aspectJAdvisorFactory != null && this.aspectJAdvisorFactory.isAspect(beanClass)));
	}
```

```
	private boolean hasAspectAnnotation(Class<?> clazz) {
		//判定当前类是否有 Aspect 注解，如果有，则不能被代理
		return (AnnotationUtils.findAnnotation(clazz, Aspect.class) != null);
	}
```
```
	protected boolean isInfrastructureClass(Class<?> beanClass) {
		//判定当前bean是否是 Advice、Pointcut、Advisor、AopInfrastructureBean等子类或实现类，如果是，则不能被代理
		boolean retVal = Advice.class.isAssignableFrom(beanClass) ||
				Pointcut.class.isAssignableFrom(beanClass) ||
				Advisor.class.isAssignableFrom(beanClass) ||
				AopInfrastructureBean.class.isAssignableFrom(beanClass);
		if (retVal && logger.isTraceEnabled()) {
			logger.trace("Did not attempt to auto-proxy infrastructure class [" + beanClass.getName() + "]");
		}
		return retVal;
	}
```


重点来看 shouldSkip方法：
```
	@Override
	protected boolean shouldSkip(Class<?> beanClass, String beanName) {
		// TODO: Consider optimization by caching the list of the aspect names
		//获取所有的候选顾问类 Advisor
		List<Advisor> candidateAdvisors = findCandidateAdvisors();
		for (Advisor advisor : candidateAdvisors) {
			if (advisor instanceof AspectJPointcutAdvisor &&
					((AspectJPointcutAdvisor) advisor).getAspectName().equals(beanName)) {
				return true;
			}
		}
		return super.shouldSkip(beanClass, beanName);
	}
```

上述代码通过`findCandidateAdvisors()`方法来获取所有的候选 advisor：
```
@Override
	protected List<Advisor> findCandidateAdvisors() {
		// Add all the Spring advisors found according to superclass rules.
		//获得 Advisor 实现类
		List<Advisor> advisors = super.findCandidateAdvisors();
		// Build Advisors for all AspectJ aspects in the bean factory.
		//将@Aspect注解类， 解析成Advisor 
		if (this.aspectJAdvisorsBuilder != null) {
			advisors.addAll(this.aspectJAdvisorsBuilder.buildAspectJAdvisors());
		}
		return advisors;
	}
```
继续跟进`buildAspectJAdvisors`方法，会触发
`ReflectiveAspectJAdvisorFactory`中的`getAdvisors`方法：
```
@Override
	public List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory aspectInstanceFactory) {
		//从 aspectMetadata 中获取 Aspect()标注的类 class对象
		Class<?> aspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
		//获取Aspect()标注的类名
		String aspectName = aspectInstanceFactory.getAspectMetadata().getAspectName();
		validate(aspectClass);

		// We need to wrap the MetadataAwareAspectInstanceFactory with a decorator
		// so that it will only instantiate once.
		MetadataAwareAspectInstanceFactory lazySingletonAspectInstanceFactory =
				new LazySingletonAspectInstanceFactoryDecorator(aspectInstanceFactory);

		List<Advisor> advisors = new LinkedList<>();
		//遍历该类所有方法，根据方法判断是否能获取到对应 pointCut，如果有，则生成 advisor 对象
		for (Method method : getAdvisorMethods(aspectClass)) {
			Advisor advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, advisors.size(), aspectName);
			if (advisor != null) {
				advisors.add(advisor);
			}
		}

		// If it's a per target aspect, emit the dummy instantiating aspect.
		if (!advisors.isEmpty() && lazySingletonAspectInstanceFactory.getAspectMetadata().isLazilyInstantiated()) {
			Advisor instantiationAdvisor = new SyntheticInstantiationAdvisor(lazySingletonAspectInstanceFactory);
			advisors.add(0, instantiationAdvisor);
		}

		// Find introduction fields.
		//获取 @DeclareParents 注解修饰的属性（并不常用）
		for (Field field : aspectClass.getDeclaredFields()) {
			Advisor advisor = getDeclareParentsAdvisor(field);
			if (advisor != null) {
				advisors.add(advisor);
			}
		}

		return advisors;
	}
```

继续来看`getAdvisor`方法：
```
	@Override
	@Nullable
	public Advisor getAdvisor(Method candidateAdviceMethod, MetadataAwareAspectInstanceFactory aspectInstanceFactory,
			int declarationOrderInAspect, String aspectName) {

		validate(aspectInstanceFactory.getAspectMetadata().getAspectClass());
		//根据候选方法名，来获取对应的 pointCut
		AspectJExpressionPointcut expressionPointcut = getPointcut(
				candidateAdviceMethod, aspectInstanceFactory.getAspectMetadata().getAspectClass());
		if (expressionPointcut == null) {
			return null;
		}
		//如果能获取到 pointCut，则将切点表达式 expressionPointcut、当前
		对象ReflectiveAspectJAdvisorFactory、 方法名等包装成 advisor 对象
		return new InstantiationModelAwarePointcutAdvisorImpl(expressionPointcut, candidateAdviceMethod,
				this, aspectInstanceFactory, declarationOrderInAspect, aspectName);
	}
```
```
	protected static AspectJAnnotation<?> findAspectJAnnotationOnMethod(Method method) {
		//定义class对象数组，如果方法中有以下注解中任何一种，则返回该注解
		Class<?>[] classesToLookFor = new Class<?>[] {
				Before.class, Around.class, After.class, AfterReturning.class, AfterThrowing.class, Pointcut.class};
		for (Class<?> c : classesToLookFor) {
			AspectJAnnotation<?> foundAnnotation = findAnnotation(method, (Class<Annotation>) c);
			if (foundAnnotation != null) {
				return foundAnnotation;
			}
		}
		return null;
	}
```

`InstantiationModelAwarePointcutAdvisorImpl`的构造方法会触发构造通知对象：

```
public Advice getAdvice(Method candidateAdviceMethod, AspectJExpressionPointcut expressionPointcut,
			MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrder, String aspectName) {
		//......
		//根据注解类型，匹配对应的通知类型
		switch (aspectJAnnotation.getAnnotationType()) {
			//前置通知
			case AtBefore:
				springAdvice = new AspectJMethodBeforeAdvice(
						candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
				break;
			//最终通知
			case AtAfter:
				springAdvice = new AspectJAfterAdvice(
						candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
				break;
			//后置通知
			case AtAfterReturning:
				springAdvice = new AspectJAfterReturningAdvice(
						candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
				AfterReturning afterReturningAnnotation = (AfterReturning) aspectJAnnotation.getAnnotation();
				if (StringUtils.hasText(afterReturningAnnotation.returning())) {
					springAdvice.setReturningName(afterReturningAnnotation.returning());
				}
				break;
			//异常通知
			case AtAfterThrowing:
				springAdvice = new AspectJAfterThrowingAdvice(
						candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
				AfterThrowing afterThrowingAnnotation = (AfterThrowing) aspectJAnnotation.getAnnotation();
				if (StringUtils.hasText(afterThrowingAnnotation.throwing())) {
					springAdvice.setThrowingName(afterThrowingAnnotation.throwing());
				}
				break;
			//环绕通知
			case AtAround:
				springAdvice = new AspectJAroundAdvice(
						candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
				break;
			//切面
			case AtPointcut:
				if (logger.isDebugEnabled()) {
					logger.debug("Processing pointcut '" + candidateAdviceMethod.getName() + "'");
				}
				return null;
			default:
				throw new UnsupportedOperationException(
						"Unsupported advice type on method: " + candidateAdviceMethod);
		}

		//......
	}
```

可以看到，根据`@Aspect`类中方法的注解类型，生成对应的`advice`，并通过通知的构造方法，将通知增强方法，切面表达式传入到通知当中。

到这里`InstantiationModelAwarePointcutAdvisorImpl`对象构造完毕。

----

#### 3： 生成代理类
上面创建`advisor`的逻辑发生在扩展接口中的`postProcessBeforeInstantiation`，实例化之前执行，如果有自定义的`TargetSource`指定类，则则直接生成代理类，并直接执行初始化之后的方法`postProcessAfterInitialization`。这种情况使用不多，常规代理类还是在`postProcessAfterInitialization`中创建，也就是 IOC 最后一个扩展方法。
```
	@Override
	public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) throws BeansException {
		if (bean != null) {
			Object cacheKey = getCacheKey(bean.getClass(), beanName);
			//处理循环依赖的判断
			if (!this.earlyProxyReferences.contains(cacheKey)) {
				return wrapIfNecessary(bean, beanName, cacheKey);
			}
		}
		return bean;
	}
```
```
protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
		if (StringUtils.hasLength(beanName) && this.targetSourcedBeans.contains(beanName)) {
			return bean;
		}
		if (Boolean.FALSE.equals(this.advisedBeans.get(cacheKey))) {
			return bean;
		}
		if (isInfrastructureClass(bean.getClass()) || shouldSkip(bean.getClass(), beanName)) {
			this.advisedBeans.put(cacheKey, Boolean.FALSE);
			return bean;
		}

		// Create proxy if we have advice.
		//获取到合适的advisor，如果为空。如果不为空，则生成代理类。
		Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
		if (specificInterceptors != DO_NOT_PROXY) {
			this.advisedBeans.put(cacheKey, Boolean.TRUE);
			Object proxy = createProxy(
					bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean));
			this.proxyTypes.put(cacheKey, proxy.getClass());
			return proxy;
		}

		this.advisedBeans.put(cacheKey, Boolean.FALSE);
		return bean;
	}
```
上述方法通过调用`getAdvicesAndAdvisorsForBean()`方法来获取`advisor`，该方法最终会调用`findEligibleAdvisors()`，`Eligible`意为有资格的，合适的。具体来看下：
```
protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
		List<Advisor> candidateAdvisors = findCandidateAdvisors();
		//这里会对获取的advisor进行筛选
		List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
		//添加一个默认的advisor，执行时用到。
		extendAdvisors(eligibleAdvisors);
		if (!eligibleAdvisors.isEmpty()) {
			eligibleAdvisors = sortAdvisors(eligibleAdvisors);
		}
		return eligibleAdvisors;
	}
```
最终的筛选规则在`AopUtils`中：
```
public static List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> clazz) {
		//......
		for (Advisor candidate : candidateAdvisors) {
			if (candidate instanceof IntroductionAdvisor) {
				// already processed
				continue;
			} 
			//调用 canApply 方法，遍历所有的方法进行匹配
			if (canApply(candidate, clazz, hasIntroductions)) {
				eligibleAdvisors.add(candidate);
			}
		}
		//......
	}
```
调用`canApply `方法，遍历被代理类的所有的方法，跟进切面表达式进行匹配，如果有一个方法匹配到，也就意味着该类会被代理。
匹配方法是借助`org.aspectj.weaver.internal.tools`实现，也就是`AspectJ`框架中的工具类，有兴趣的可以自行查看。

------


重点来看一下代理生成方式：
```
public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
		if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
			Class<?> targetClass = config.getTargetClass();
			if (targetClass == null) {
				throw new AopConfigException("TargetSource cannot determine target class: " +
						"Either an interface or a target is required for proxy creation.");
			}
			//如果代理目标是接口或者Proxy类型，则走jdk类型
			if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
				return new JdkDynamicAopProxy(config);
			}
			return new ObjenesisCglibAopProxy(config);
		}
		else {
			return new JdkDynamicAopProxy(config);
		}
	}
```
上述方法通过三个变量来进行筛选代理方法：
- `optimize`：官方文档翻译为设置代理是否应执行积极的优化，默认为false。
- `proxyTargetClass`：这个在上面已经提到了，`AopAutoConfiguration`中指定，默认为true，也就是选择使用 cglib 代理。可以看到该变量和`optimize`意义一样，之所以这么做，个人理解是为了可以在不同的场景中使用。
- `hasNoUserSuppliedProxyInterfaces`：是否设置了实现接口。

`hasNoUserSuppliedProxyInterfaces`方法如下：
```
private boolean hasNoUserSuppliedProxyInterfaces(AdvisedSupport config) {
		Class<?>[] ifcs = config.getProxiedInterfaces();
		return (ifcs.length == 0 || (ifcs.length == 1 && SpringProxy.class.isAssignableFrom(ifcs[0])));
	}
```
主要就是判断`AdvisedSupport`中`interfaces`变量中是否设置了接口，

意思是如果一个类实现了接口，把接口设置到该方法的变量中，但是不是一定会设置到该变量中，具体设置接口的代码如下：



**可以看到如果用默认配置也就是`proxyTargetClass`为true时，只有一种情况会走jdk代理方法，就是代理类为接口类型（注意：代理类是接口类型，并不是指接口类是否实现了接口）或者代理类是Proxy类型，否则全部走cglib代理。所以，平时使用中，代理类大部分还是用cglib的方式来生成。**

-----

### Spring Boot 1.x 版本和 2.x版本 AOP 默认配置的变动
配置类`AopAutoConfiguration`：

1.5x版本：
```
    @Configuration
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "true", matchIfMissing = false)
    public static class CglibAutoProxyConfiguration {

    }
```

2.x版本：
```
    @Configuration
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    @ConditionalOnProperty(prefix = "spring.aop", name = "proxy-target-class", havingValue = "true", matchIfMissing = true)
    public static class CglibAutoProxyConfiguration {

    }
```

可以看到，在SpringBoot2.x中最主要的变化就是`proxy-target-class`默认为true，意味着类代理的时候全部走cglib代理方式，只有为接口代理时才走jdk代理(注意：这里为接口代理，不是指代理目标类是否实现了接口)。所以，在使用springboot2.x的版本中，除了代理目标类是接口外，其余的代理方式全部采用cglib类型。

## 总结

`Springboot`通过自动装配`AopAutoConfiguration`配置类，默认自动开启 AOP 功能。通过注册
`AnnotationAwareAspectJAutoProxyCreator`类，来扫描创建所有的`Advisor`，再通过 `Advisor`在 Spring IOC的扩展接口中来创建代理类。
