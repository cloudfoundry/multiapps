package com.sap.activiti.common;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.ProcessInstanceQueryImpl;
import org.activiti.engine.query.Query;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.mockito.Mockito;

/**
 * The class provides a very easy to use mechanism for creating {@link Mockito #spy spies} of the {@link Query} interface and its
 * extensions. It mocks all {@link Query} methods that make calls to the database: {@link Query #list list()}, {@link Query #count()
 * count()} and {@link Query #singleResult() singleResult()}. <br />
 * <br />
 * The {@link Query #listPage listPage()} method is not mocked, because it requires more complex parameters. It is left to be mocked after
 * the {@link Mockito #spy spy} instance is created. <br />
 * <br />
 * All other methods remain the same, but can be mocked using {@link Mockito} when the {@link Mockito #spy spy} is created.
 * 
 * @author I312695
 * 
 * @param <Q>The type of the {@link Query} to be mocked (e.g. {@link ProcessInstanceQuery}).
 * @param <T> The type of the objects that the query returns (e.g. {@link ProcessInstance}).
 */
public class ActivitiQuerySpyBuilder<Q extends Query<Q, T>, T> {

    /**
     * Creates a new {@link ActivitiQuerySpyBuilder} instance.
     * 
     * @param instace A specific {@link Query} implementation instance to be spied on (e.g. {@link ProcessInstanceQueryImpl}).
     * @return The newly created {@link ActivitiQuerySpyBuilder} instance.
     */
    public static <Q extends Query<Q, T>, T> ActivitiQuerySpyBuilder<Q, T> createBuilder(Q instace) {
        return new ActivitiQuerySpyBuilder<Q, T>(instace);
    }

    private Q queryInstance;

    private ActivitiQuerySpyBuilder(Q instace) {
        queryInstance = createDefaultQuery(instace);
    }

    private Q createDefaultQuery(Q instace) {
        Q query = spy(instace);
        List<T> elements = new ArrayList<T>();

        doReturn(0L).when(query)
            .count();
        doReturn(elements).when(query)
            .list();
        doReturn(null).when(query)
            .singleResult();

        return query;
    }

    /**
     * Mocks the {@link Query #list list()} method to return a specific list of elements. This also mocks the return value of the
     * {@link Query #count() count()} method.
     * 
     * @param elements The list of elements to be returned from the {@link Query #list list()} method.
     * @return The current {@link ActivitiQuerySpyBuilder} instance.
     */
    public ActivitiQuerySpyBuilder<Q, T> setElementsList(List<T> elements) {
        long count = elements.size();
        doReturn(elements).when(queryInstance)
            .list();
        doReturn(count).when(queryInstance)
            .count();

        return this;
    }

    /**
     * Mocks the {@link Query #singleResult singleResult()} method to return a specific object.
     * 
     * @param element The element to be returned.
     * @return The current {@link ActivitiQuerySpyBuilder} instance.
     */
    public ActivitiQuerySpyBuilder<Q, T> setSingleResult(T element) {
        doReturn(element).when(queryInstance)
            .singleResult();
        return this;
    }

    /**
     * Returns the created {@link Mockito #spy spy} instance. All its methods that make calls to the database are mocked, except the
     * {@link Query #listPage listPage} method. The spy instance could be further mocked by using {@link Mockito}.
     * 
     * @return A {@link Mockito #spy spy} of the provided {@link Query} implementation instance.
     */
    public Q getQuery() {
        return queryInstance;
    }
}
