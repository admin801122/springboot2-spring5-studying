package filterChain;

import java.util.List;

/**
 * Created by zhangshukang.
 */
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
