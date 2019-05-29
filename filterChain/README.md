[**SpringBoot2 | SpingBoot FilterRegistrationBean 注册组件 | FilterChain 责任链源码分析（九）**](https://blog.csdn.net/woshilijiuyi/article/details/85014183)


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


------

概述

>SpringBoot 摒弃了繁琐的 xml 配置的同时，提供了几种注册组件：ServletRegistrationBean，
FilterRegistrationBean，ServletListenerRegistrationBean，DelegatingFilterProxyRegistrationBean，用于注册自定义的 bean。

本篇来分析过滤器注册组件`FilterRegistrationBean`，理解实现原理，有助于平时开发遇到对应的问题，能够快速的分析和定位。
内容涉及以下几点：

-  `FilterRegistrationBean`加载机制
- `FilterChain`责任链构造方式
- 自定义`FilterChain`
--------

### 一  FilterRegistrationBean 加载机制
先来看一下该类 uml：

Springboot 1.x 版本：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181215125418803.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

Springboot 2.x 版本：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181215154847921.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

`ServletContextInitializer`是 Servlet 容器初始化的时候，提供的初始化接口。`FilterRegistrationBean`最终实现了`ServletContextInitializer`，所以，Servlet 容器初始化会获取并触发所有的`FilterRegistrationBean`实例化。

来看一下源码。
Spring 刷新容器会执行`onRefresh`：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181215140933397.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)
跟进该方法：

```
private void createWebServer() {
		WebServer webServer = this.webServer;
		ServletContext servletContext = getServletContext();
		if (webServer == null && servletContext == null) {
			//获取指定的 Servlet类型
			ServletWebServerFactory factory = getWebServerFactory();
			//指定 ServletContextInitializer 触发逻辑
			this.webServer = factory.getWebServer(getSelfInitializer());
		}
		else if (servletContext != null) {
			try {
				getSelfInitializer().onStartup(servletContext);
			}
			catch (ServletException ex) {
				throw new ApplicationContextException("Cannot initialize servlet context",
						ex);
			}
		}
		initPropertySources();
	}
```

上述首先获取当前 Servlet 容器类型，本篇以 Jetty 为例进行分析。
上面有一个参数比较重要：
`			this.webServer = factory.getWebServer(getSelfInitializer());`
这里传入了一个回调函数 `getSelfInitializer()`:

```
private org.springframework.boot.web.servlet.ServletContextInitializer getSelfInitializer() {
		return this::selfInitialize;
	}
```
这是用来获取所有的`ServletContextInitializer`并实例化的回调函数，什么时候触发呢？
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181215142649106.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

当容器启动时，会执行`callInitializers`，来回调前面注册的回调函数，执行函数中的`selfInitialize`方法。
跟进该方法：
```
private void selfInitialize(ServletContext servletContext) throws ServletException {
		prepareWebApplicationContext(servletContext);
		ConfigurableListableBeanFactory beanFactory = getBeanFactory();
		ExistingWebApplicationScopes existingScopes = new ExistingWebApplicationScopes(
				beanFactory);
		WebApplicationContextUtils.registerWebApplicationScopes(beanFactory,
				getServletContext());
		existingScopes.restore();
		WebApplicationContextUtils.registerEnvironmentBeans(beanFactory,
				getServletContext());
		//这里便是获取所有的 ServletContextInitializer 实现类，会获取所有的注册组件
		for (ServletContextInitializer beans : getServletContextInitializerBeans()) {
			beans.onStartup(servletContext);
		}
	}
```

跟进上面的`getServletContextInitializerBeans`方法：
```
protected Collection<ServletContextInitializer> getServletContextInitializerBeans() {
		return new ServletContextInitializerBeans(getBeanFactory());
	}
```
`ServletContextInitializerBeans`对象是对`ServletContextInitializer`的一种包装，构造函数如下：
```
public ServletContextInitializerBeans(ListableBeanFactory beanFactory) {
		this.initializers = new LinkedMultiValueMap<>();
		//获取所有的 ServletContextInitializer
		addServletContextInitializerBeans(beanFactory);
		addAdaptableBeans(beanFactory);
		List<ServletContextInitializer> sortedInitializers = new ArrayList<>();
		//监听器，过滤器，以及 servlet的排序逻辑
		this.initializers.values().forEach((contextInitializers) -> {
			AnnotationAwareOrderComparator.sort(contextInitializers);
			sortedInitializers.addAll(contextInitializers);
		});
		this.sortedList = Collections.unmodifiableList(sortedInitializers);
	}
```
可以看到其构造函数中执行了`addServletContextInitializerBeans`方法，该方法传入了 beanFactory，也就是从容器中获取所有的`ServletContextInitializer`，并进行实例化，然后进行排序。如下图：

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181215153045358.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

上述方法获取所有的`ServletContextInitializer`，进行循环注册，跟进`onStartup`方法：
![在这里插入图片描述](https://img-blog.csdnimg.cn/2018121515331351.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)
`RegistrationBean`类提供了一个模板方法：`register`，对应的注册组件执行各自的注册逻辑。这里来看一下过滤器注册组件的实现：
```
	@Override
	protected Dynamic addRegistration(String description, ServletContext servletContext) {
		Filter filter = getFilter();
		return servletContext.addFilter(getOrDeduceName(filter), filter);
	}
```
上述方法获取过滤器，并通过`ServletContext`注入到`Servlet`容器中，继续跟进`addFilter`方法：
```
@Override
        public FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass)
        {
            //......
            final ServletHandler handler = ServletContextHandler.this.getServletHandler();
            //判断filter是否已注册
            FilterHolder holder = handler.getFilter(filterName);
            if (holder == null)
            {
                //new filter
                //创建一个新的holder，注入到ServletHandler中
                holder = handler.newFilterHolder(Source.JAVAX_API);
                holder.setName(filterName);
                //将filter设置到holder中
                holder.setFilter(filter);
                handler.addFilter(holder);
                return holder.getRegistration();
            }
            if (holder.getClassName()==null && holder.getHeldClass()==null)
            {
                //preliminary filter registration completion
                holder.setHeldClass(filterClass);
                return holder.getRegistration();
            }
            else
                return null; //existing filter
        }
```
至此，自定义的 Filter 就注入到了 Servlet 容器中。

-----------

### 二  FilterChain责任链构造方式

>FilterChain 采用了责任链模式，也是责任链模式的一种典型使用方式。类似于 Pipeline 模式。

Jetty中的 FilterChain 对象默认是懒加载的形式，只有第一次请求进来的时候才会初始化，如下图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/201812151606152.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dvc2hpbGlqaXV5aQ==,size_16,color_FFFFFF,t_70)

请求进来，首先会判断`_filterMappings`是否为空，不为空则获取`FilterChain`对象。
继续来看`getFilterChain`方法：
```
protected FilterChain getFilterChain(Request baseRequest, String pathInContext, ServletHolder servletHolder)
    {
        String key=pathInContext==null?servletHolder.getName():pathInContext;
        int dispatch = FilterMapping.dispatch(baseRequest.getDispatcherType());
        
       	//通过 url，从缓存中获取 FilterChain
        if (_filterChainsCached && _chainCache!=null)
        {
            FilterChain chain = (FilterChain)_chainCache[dispatch].get(key);
            if (chain!=null)
                return chain;
        }

       //如果未获取到，则构造一个FilterChain对象
        FilterChain chain = null;
        //判断是否开启了缓存
        if (_filterChainsCached)
        {
            if (filters.size() > 0)
                chain= new CachedChain(filters, servletHolder);

            final Map<String,FilterChain> cache=_chainCache[dispatch];
            final Queue<String> lru=_chainLRU[dispatch];

                // Do we have too many cached chains?
                //判断缓存中是否有了太多的FilterChain，如果大于最大长度，进行删除。
                while (_maxFilterChainsCacheSize>0 && cache.size()>=_maxFilterChainsCacheSize)
                {
                    // The LRU list is not atomic with the cache map, so be prepared to invalidate if
                    // a key is not found to delete.
                    // Delete by LRU (where U==created)
                    String k=lru.poll();
                    if (k==null)
                    {
                        cache.clear();
                        break;
                    }
                    cache.remove(k);
                }

                cache.put(key,chain);
                lru.add(key);
        }
        else if (filters.size() > 0)
            chain = new Chain(baseRequest,filters, servletHolder);

        return chain;
    }
```
Jetty 实现了一个对 FilterChain 缓存的功能，以 URL为key，每次请求进来，根据 URL 获取对应的过滤器链。
另外实现了 LRU 算法，当缓存长度超过最大限度时，清理掉最早未使用的键值对。

但是很多请求下，不同的 URL 获取的过滤器链是一样的，所以这里没必要开启缓存。Jetty提供了`_filterChainsCached`进行设置，上述代码也是通过此变量进行判断。
默认为 true，默认使用了缓存。

>需要注意一点：只有开启 FilterChain 缓存，创建`CachedChain`对象，才会采用责任链模式。
如果创建的是`Chain`对象，则直接遍历所有过滤器处理。


来看一下`CachedChain`构造方法,责任链相关代码：
```
CachedChain(List<FilterHolder> filters, ServletHolder servletHolder)
        {
            if (filters.size()>0)
            {
                _filterHolder=filters.get(0);
                filters.remove(0);
                //递归处理
                _next=new CachedChain(filters,servletHolder);
            }
            else
                _servletHolder=servletHolder;
        }
```
代码比较简洁，对构造方法进行递归处理，创建`CachedChain`链表，最终生成的对象如下形式：
```
characterEncodingFilter->hiddenHttpMethodFilter->httpPutFormContentFilter->requestContextFilter->webRequestLoggingFilter->authenticationFilter->traceFilter->applicationContextIdFilter->Jetty_WebSocketUpgradeFilter
```

-------

### 三  自定义 FilterChain

以下实例代码提供了两种方式创建`FilterChain`，构造方法递归实现和普通方法递归实现。

定义一个 Filter接口：
```
public interface MyFilter {

    String getName();
    void execute(FilterChain filterChain);
}
```

定义两个实现类：
```
public class MyFilters{

    /**
     * 定义两个个Myfilter
     *
     */
    public static class MyFilter1 implements  MyFilter{
        @Override
        public String getName() {
            return "myFilter1";
        }

        @Override
        public void execute(FilterChain filterChain) {
            System.out.println(getName()+"before...");
            if (null != filterChain) {
                filterChain.doFilter(filterChain);
            }
            System.out.println(getName()+"after...");
        }
    }


    public static class MyFilter2 implements  MyFilter{
        @Override
        public String getName() {
            return "myFilter2";
        }

        @Override
        public void execute(FilterChain filterChain) {
            System.out.println(getName()+"before...");
            if (null != filterChain) {
                filterChain.doFilter(filterChain);
            }
            System.out.println(getName()+"after...");
        }
    }
}
```

FilterChain 对象：
```
@Data
public class FilterChain {

    private MyFilter currentFilter;
    private FilterChain next;
    private List<MyFilter> filters;


    /**
     *
     * 递归实现责任链
     */
    public FilterChain(MyFilter myFilter) {
        this.currentFilter = myFilter;
    }


    /**
     *
     * 模拟 SpringBoot Jetty 中的 fiterChain 责任链实现机制
     */
    public FilterChain(List<MyFilter> filters) {
        if (filters.size() > 0) {
            this.currentFilter = filters.get(0);
            filters.remove(0);
            this.next = new FilterChain(filters);
        }
    }

    public void doFilter(FilterChain filterChain) {
        MyFilter currentFilter = filterChain.getCurrentFilter();
        if (null != currentFilter) {
            currentFilter.execute(filterChain.next);
        }
    }
}
```

通过构造方法递归实现：
```
public class FilterChainBuilder {

    static List<MyFilter> filters;

    public static FilterChain buildFilterChainBuild(List<MyFilter> myFilters){
        filters = myFilters;
        return FilterChainInstanceFactory.FILTER_CHAIN;
    }

    private  static class FilterChainInstanceFactory{
        final static FilterChain FILTER_CHAIN = new FilterChain(filters);
    }
}
```
通过普通方法递归实现：
```
public class FilterChainBuilder2 {

    public static FilterChain buildFilterChain(List<MyFilter> filters) {

        if (CollectionUtils.isEmpty(filters)) {
            return null;
        }
        MyFilter currentFilter = filters.get(0);
        FilterChain filterChain2 = new FilterChain(currentFilter);
        filters.remove(0);

        if (filters.size() > 0) {
            filterChain2.setNext(buildFilterChain(filters));
        }
        return filterChain2;
    }
}
```

**具体代码 Github：
https://github.com/admin801122/springboot2-spring5-studying/tree/master/filterChain**

-----------

### 总结
SpringBoot 在加载 Servlet 容器时，会获取扩展接口`ServletContextInitializer`的所有实现类。过滤器，监听器等注册组件正是实现了该接口，从而完成了对应各自注册的机制。另外过滤器链采用了 LRU 算法实现了缓存机制，并通过在 FilterChain 构造方法中递归实现了责任链机制。

