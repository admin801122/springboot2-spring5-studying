


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

[**SpringBoot2 | BeanDefinition 注册核心类 ImportBeanDefinitionRegistrar （十）**](https://blog.csdn.net/woshilijiuyi/article/details/85268659)

[**SpringBoot2 | Spring 核心扩展接口 | 核心扩展方法总结（十一）**](https://blog.csdn.net/woshilijiuyi/article/details/85396492)

-----

### 概述

**Spring 的核心思想即是容器。整个容器 refresh 时，外部看似风平浪静，内部实则一片汪洋大海。另外整个流程严格遵守开闭原则，内部对修改关闭，对扩展开放。**

>可以这么理解：
把 Spring 容器理解为一个钥匙环，上面挂满了钥匙，每个钥匙理解为一个扩展接口。钥匙的顺序是固定的，可理解为接口的调用顺序固定，对修改关闭。每个钥匙可以用来做不同的事情，可理解为扩展接口的不同实现，对扩展开放。

Spring 提供了各种丰富的扩展接口，本篇主要对 IOC 过程中涉及的扩展接口做个整理。

对应的 UML 如下：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181230213437509.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

调用顺序如下：

![](https://img-blog.csdnimg.cn/20181230212707950.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

分别来看。

--------

**1。BeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry**

`BeanDefinitionRegistryPostProcessor`接口在读取项目中的`beanDefinition`之后执行，提供的一个补充扩展接口，
用来动态注册`beanDefinition`。调用点：
在`PostProcessorRegistrationDelegate`中：
```
if (beanFactory instanceof BeanDefinitionRegistry) {
			//......
			// Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				//获取所有的 BeanDefinitionRegistryPostProcessor 类型的bean
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				for (String ppName : postProcessorNames) {
					if (!processedBeans.contains(ppName)) {
						//通过 getBean 方法进行实例化
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						processedBeans.add(ppName);
						reiterate = true;
					}
				}
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				registryProcessors.addAll(currentRegistryProcessors);
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
				currentRegistryProcessors.clear();
			}
		//......
		}
```

示例：手动注册`BeanDefinition`：：

```
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("postProcessBeanDefinitionRegistry ...");
        //手动注册 beanDefinition
        registry.registerBeanDefinition("myBeanDefinitionRegistrar",new AnnotatedGenericBeanDefinition(MyBeanDefinitionRegistrar.class));
    }
}
```

-----

**2 。BeanFactoryPostProcessor.postProcessBeanFactory**

`BeanFactoryPostProcessor`和`BeanPostProcessor` 接口比较相似，从字面不难看出，前者多了一个 factory，所以该接口正是`beanFactory`的扩展接口，使用场景：一般用来在读取所有的`beanDefinition`信息之后，实例化之前，通过该接口可进一步自行处理，比如修改`beanDefinition`等。调用点在上面第一个扩展接口之后，也在`PostProcessorRegistrationDelegate`中：

```
if (beanFactory instanceof BeanDefinitionRegistry) {
		//......
		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// 获取所有的 BeanFactoryPostProcessor 类型
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
		List<String> orderedPostProcessorNames = new ArrayList<>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<>();
		for (String ppName : postProcessorNames) {
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
			}
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		//执行所有的 BeanFactoryPostProcessor 实现逻辑
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		//......
		}
```

示例：动态修改`BeanDefinition`：
```
@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition myBeanDefinitionRegistrar = beanFactory.getBeanDefinition("myBeanDefinitionRegistrar");
        //可以修改 beanDefinition 信息。这里将bean 设置为单例
        myBeanDefinitionRegistrar.setScope(BeanDefinition.SCOPE_SINGLETON);
    }
}
```

------

**3。InstantiationAwareBeanPostProcessor.postProcessBeforeInstantiation**

`Instantiation` 实例化的意思，和`Initialization`初始化 比较相似，容易混淆。
`postProcessBeforeInstantiation`用来获取 bean，如果获取到，则不再执行对应 bean的初始化之前流程，直接执行后面要讲的`postProcessAfterInitialization`方法。
调用点在`AbstractAutowireCapableBeanFactory`中：
```
protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
			throws BeanCreationException {

		//......
		try {
			//执行实例化之前的方法
			Object bean = resolveBeforeInstantiation(beanName, mbdToUse);
			if (bean != null) {
				return bean;
			}
		}
		catch (Throwable ex) {
			throw new BeanCreationException(mbdToUse.getResourceDescription(), beanName,
					"BeanPostProcessor before instantiation of bean failed", ex);
		}
		//......
	}
```
```
protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
		Object bean = null;
		if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
			// Make sure bean class is actually resolved at this point.
			if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
				Class<?> targetType = determineTargetType(beanName, mbd);
				if (targetType != null) {
					//开始执行 postProcessBeforeInstantiation 方法
					bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
					//如果获得结果不为空，则直接执行实例化之后的扩展接口。结束 bean 实例化流程。
					if (bean != null) {
						bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
					}
				}
			}
			mbd.beforeInstantiationResolved = (bean != null);
		}
		return bean;
	}
```
如果`postProcessBeforeInstantiation `获得结果不为空，则结束 bean 实例化流程。

----

**4。SmartInstantiationAwareBeanPostProcessor.determineCandidateConstructors**

该扩展点决定判断合适的 bean 构造方法。
具体可参考`AutowiredAnnotationBeanPostProcessor`实现类，针对以下使用场景：
在`MyComponent1`中通过构造方法注入`MyComponent2`:
```
@Autowired
    public MyComponent(MyComponent2 component2){
        System.out.println("myComponent init...");
    }
```
这里会判断选择出合适的构造方法，并实例化需要的参数 bean。
调用点在`AbstractAutowireCapableBeanFactory`中：
```
protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
		//......
		//获取合适的构造方法，如果为空，则走默认的构造方法。
		Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName);
		if (ctors != null ||
				mbd.getResolvedAutowireMode() == RootBeanDefinition.AUTOWIRE_CONSTRUCTOR ||
				mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args))  {
		   //如果发现有构造方法引用了依赖注入注解，比如：@AutoWired,则调用autowireConstructor方法进行注入
			return autowireConstructor(beanName, mbd, ctors, args);
		}

		// No special handling: simply use no-arg constructor.
		return instantiateBean(beanName, mbd);
	}
```

--------

**5。MergedBeanDefinitionPostProcessor.postProcessMergedBeanDefinition**

该接口用来合并`BeanDefinition`，也是对`BeanDefinition`处理一种扩展接口。
最常用的使用场景：`AutowiredAnnotationBeanPostProcessor`实现类中，通过该接口解析当前 bean 中所有
指定注解类型的熟悉：
```
  	    this.autowiredAnnotationTypes.add(Autowired.class);
	    this.autowiredAnnotationTypes.add(Value.class);
```
默认解析上两种注解的属性，将其描述信息合并到当前对象的`beanDefinition`中，在后面属性填充`populateBean`的过程中，会取出这些对象，进行注入。
调用点在`AbstractAutowireCapableBeanFactory`中：
```
protected Object doCreateBean(final String beanName, final RootBeanDefinition mbd, final @Nullable Object[] args)
			throws BeanCreationException {

		// Instantiate the bean.
		BeanWrapper instanceWrapper = null;
		if (mbd.isSingleton()) {
			instanceWrapper = this.factoryBeanInstanceCache.remove(beanName);
		}
		//实例化 bean
		if (instanceWrapper == null) {
			instanceWrapper = createBeanInstance(beanName, mbd, args);
		}
		final Object bean = instanceWrapper.getWrappedInstance();
		Class<?> beanType = instanceWrapper.getWrappedClass();
		if (beanType != NullBean.class) {
			mbd.resolvedTargetType = beanType;
		}

		// Allow post-processors to modify the merged bean definition.
		synchronized (mbd.postProcessingLock) {
			if (!mbd.postProcessed) {
				try {
				    //执行 postProcessMergedBeanDefinition 逻辑
					applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
				}
				catch (Throwable ex) {
					throw new BeanCreationException(mbd.getResourceDescription(), beanName,
							"Post-processing of merged bean definition failed", ex);
				}
				mbd.postProcessed = true;
			}
		}
		//......
		return exposedObject;
	}
```

-----

**6。InstantiationAwareBeanPostProcessor.postProcessAfterInstantiation**

实例化之后调用的方法，在`AbstractAutowireCapableBeanFactory.populateBean()`填充方法中会触发。
该方法默认返回为true，如果返回false，则中断`populateBean`方法，即不再执行属性注入的过程。
实际项目中，该扩展方法使用不多。
```
protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {
		// ......
		boolean continueWithPropertyPopulation = true;

		if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
			for (BeanPostProcessor bp : getBeanPostProcessors()) {
				if (bp instanceof InstantiationAwareBeanPostProcessor) {
					InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
					if (!ibp.postProcessAfterInstantiation(bw.getWrappedInstance(), beanName)) {
						continueWithPropertyPopulation = false;
						break;
					}
				}
			}
		}

		if (!continueWithPropertyPopulation) {
			return;
		}
		//......
	}
```


-----

**7。SmartInstantiationAwareBeanPostProcessor.getEarlyBeanReference**

`getEarlyBeanReference`方法只要有在 Spring 发生循环依赖时调用。首先，当bean 创建时，为了防止后续有循环依赖，会提前暴露回调方法，用于 bean 实例化的后置处理。`getEarlyBeanReference`方法就是在提前暴露的回调方法中触发。

具体调用点在`DefaultSingletonBeanRegistry`：
```
@Nullable
	protected Object getSingleton(String beanName, boolean allowEarlyReference) {
		Object singletonObject = this.singletonObjects.get(beanName);
		//如果 bean 还未实例化，并且正在创建中。
		if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
			synchronized (this.singletonObjects) {
			//判断是否已经提前提前暴露了bean 引用。
				singletonObject = this.earlySingletonObjects.get(beanName);
				//如果运行循环依赖
				if (singletonObject == null && allowEarlyReference) {
					ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
					if (singletonFactory != null) {
					//则调用 getObject() 方法
						singletonObject = singletonFactory.getObject();
						this.earlySingletonObjects.put(beanName, singletonObject);
						this.singletonFactories.remove(beanName);
					}
				}
			}
		}
		return singletonObject;
	}
```

在`getObject()`中调用`getEarlyBeanReference`方法完成 bean的初始化流程。
```
protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean) {
		Object exposedObject = bean;
		if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) {
			for (BeanPostProcessor bp : getBeanPostProcessors()) {
				if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
					SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor) bp;
					exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
				}
			}
		}
		return exposedObject;
	}
```
大致的流程：

**当A实例化之后，Spring IOC会对A依赖的属性进行填充，此时如果发现A依赖了B，会去实例化B。同样在填充B的属性时，如果B也引用了A，就发生了循环依赖。因为A还未创建完成，还未注入Spring中。**

**Spring的做法是通过对创建中的缓存一个回调函数，类似于一个埋点操作，如果后续填充属性阶段，发生了循环依赖，则通过触发该回调函数来结束该bean的初始化。**

**​当对A实例化时，会提前暴露一个回调方法 ObjectFactory（Spring5中改为了函数式接口） 放入缓存。当B引用A，发现A还未实例化结束，就会通过缓存中的回调方法结束A的初始化流程，然后注入B。然后继续A的填充属性流程，将B注入A，然后结束循环依赖。**

```
boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
				isSingletonCurrentlyInCreation(beanName));
		//......
		if (earlySingletonExposure) {
			if (logger.isDebugEnabled()) {
				logger.debug("Eagerly caching bean '" + beanName +
						"' to allow for resolving potential circular references");
			}
			//添加回调方法，循环依赖时会回调
			addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
		}
		try {
		//填充属性，也就是发生循环依赖的地方
			populateBean(beanName, mbd, instanceWrapper);
			exposedObject = initializeBean(beanName, exposedObject, mbd);
		}
		catch (Throwable ex) {
			if (ex instanceof BeanCreationException && beanName.equals(((BeanCreationException) ex).getBeanName())) {
				throw (BeanCreationException) ex;
			}
			else {
				throw new BeanCreationException(
						mbd.getResourceDescription(), beanName, "Initialization of bean failed", ex);
			}
		}
		//......
```

------


**8。InstantiationAwareBeanPostProcessor.postProcessPropertyValues**

该方法用于属性注入，在 bean 初始化阶段属性填充时触发。`@Autowired，@Resource` 等注解原理基于此方法实现。
具体调用点在`AbstractAutowireCapableBeanFactory`中`populateBean`方法：
```
protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {
		//......
	if (hasInstAwareBpps || needsDepCheck) {
			if (pvs == null) {
				pvs = mbd.getPropertyValues();
			}
			PropertyDescriptor[] filteredPds = filterPropertyDescriptorsForDependencyCheck(bw, mbd.allowCaching);
			if (hasInstAwareBpps) {
				for (BeanPostProcessor bp : getBeanPostProcessors()) {
					if (bp instanceof InstantiationAwareBeanPostProcessor) {
						InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
						//获取该方法的所有实现类
						pvs = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
						if (pvs == null) {
							return;
						}
					}
				}
			}
			if (needsDepCheck) {
				checkDependencies(beanName, mbd, filteredPds, pvs);
			}
		}

		//......
	}
```
上述方法会获取所有`postProcessPropertyValues`的实现方法。例如：在`AutowiredAnnotationBeanPostProcessor`中实现方式如下，也就是依赖注入的实现代码：
```
@Override
	public PropertyValues postProcessPropertyValues(
			PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeanCreationException {

		InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
		try {
			metadata.inject(bean, beanName, pvs);
		}
		catch (BeanCreationException ex) {
			throw ex;
		}
		catch (Throwable ex) {
			throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
		}
		return pvs;
	}
```
------

**9。ApplicationContextAwareProcessor.invokeAwareInterfaces**

该扩展点用于执行各种驱动接口。在 bean实例化之后，属性填充之后，通过扩展接口，执行如下驱动接口：

```
private void invokeAwareInterfaces(Object bean) {
		if (bean instanceof Aware) {
			if (bean instanceof EnvironmentAware) {
				((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
			}
			if (bean instanceof EmbeddedValueResolverAware) {
				((EmbeddedValueResolverAware) bean).setEmbeddedValueResolver(this.embeddedValueResolver);
			}
			if (bean instanceof ResourceLoaderAware) {
				((ResourceLoaderAware) bean).setResourceLoader(this.applicationContext);
			}
			if (bean instanceof ApplicationEventPublisherAware) {
				((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(this.applicationContext);
			}
			if (bean instanceof MessageSourceAware) {
				((MessageSourceAware) bean).setMessageSource(this.applicationContext);
			}
			if (bean instanceof ApplicationContextAware) {
				((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
			}
		}
	}
```

所以，只需要实现以上6种驱动接口，就可以获得对应的容器相关的变量。这些变量在实际项目中是比较常用的了。

使用方式：
```
@Component
public class MyComponent implements ApplicationContextAware, InitializingBean, BeanClassLoaderAware ,ResourceLoaderAware,EnvironmentAware {

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet init...");
    }
    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("setBeanClassLoader init...");
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        System.out.println("setApplicationContext init...");
    }
    @Override
    public void setEnvironment(Environment environment) {

        System.out.println("setEnvironment init...");
    }
    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        System.out.println("setResourceLoader init...");
    }
}
```

------
**10。BeanFactoryPostProcessor.postProcessBeforeInitialization**

`BeanFactoryPostProcessor`中的两个扩展接口是 `Spring IOC`过程中最后两个扩展接口。其中`postProcessBeforeInitialization`用于在 bean 实例化之后，`afterPropertiesSet`方法之前执行的前置接口。
用于对 bean 进行一些属性设置，上面的设置驱动的方法`invokeAwareInterfaces`便是实现了此接口。
调用点如下：
```
protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
		if (System.getSecurityManager() != null) {
			AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
				invokeAwareMethods(beanName, bean);
				return null;
			}, getAccessControlContext());
		}
		else {
			invokeAwareMethods(beanName, bean);
		}

		Object wrappedBean = bean;
		if (mbd == null || !mbd.isSynthetic()) {
		//执行前置扩展方法
			wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
		}
		try {
		//执行 afterPropertiesSet 方法
			invokeInitMethods(beanName, wrappedBean, mbd);
		}
		catch (Throwable ex) {
			throw new BeanCreationException(
					(mbd != null ? mbd.getResourceDescription() : null),
					beanName, "Invocation of init method failed", ex);
		}
		if (mbd == null || !mbd.isSynthetic()) {
		//执行后置扩展方法
			wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
		}

		return wrappedBean;
	}
```


-----

**11。InitializingBean.afterPropertiesSet**

用于bean实例化之后，设置熟悉的方法。
上面已经提到，在`invokeInitMethods`方法中会触发该方法调用：
```
protected void invokeInitMethods(String beanName, final Object bean, @Nullable RootBeanDefinition mbd)
			throws Throwable {
		boolean isInitializingBean = (bean instanceof InitializingBean);
		if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
			if (logger.isDebugEnabled()) {
				logger.debug("Invoking afterPropertiesSet() on bean with name '" + beanName + "'");
			}
			if (System.getSecurityManager() != null) {
				try {
				//执行afterPropertiesSet
					AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () -> {
						((InitializingBean) bean).afterPropertiesSet();
						return null;
					}, getAccessControlContext());
				}
				catch (PrivilegedActionException pae) {
					throw pae.getException();
				}
			}
			else {
			//执行afterPropertiesSet
				((InitializingBean) bean).afterPropertiesSet();
			}
		}
	}
```

-----

**12。BeanFactoryPostProcessor.postProcessAfterInitialization**

该方法`Spring IOC`过程中最后一个常用的扩展点，用于 bean 初始化之后的后置处理。IOC 流程执行到此处，一个完整的 bean 已经创建结束，可在此处对 bean 进行包装或者代理。Spring AOP 原理便是基于此扩展点实现，实现方式在`AbstractAutoProxyCreator`中：

```
public Object postProcessAfterInitialization(@Nullable Object bean, String beanName) throws BeansException {
		if (bean != null) {
			Object cacheKey = getCacheKey(bean.getClass(), beanName);
			if (!this.earlyProxyReferences.contains(cacheKey)) {
				return wrapIfNecessary(bean, beanName, cacheKey);
			}
		}
		return bean;
	}
```

有兴趣的可以移步 Spring AOP相关的文章：[SpringBoot2 | Spring AOP 原理源码深度剖析（八）](https://blog.csdn.net/woshilijiuyi/article/details/83934407)

具体使用方式已上传至 github：
**https://github.com/admin801122/springboot2-spring5-studying/tree/master/ioc-beanPostProcessor**

![在这里插入图片描述](https://img-blog.csdnimg.cn/201901191808071.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

-----

### 总结
**我们使用 Spring 或者 SpringBoot 时，通过 Spring 预留的以上扩展接口，可以方便的实现对 Spring IOC 过程中的逻辑做一些扩展和增强。通 Servlet 规范一样，可以理解为面向接口编程。**




