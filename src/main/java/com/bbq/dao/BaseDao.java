package com.bbq.dao;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// 基于泛型的BaseDao

public class BaseDao<T> {

    private Class<T> entityClass;

    private HibernateTemplate hibernateTemplate;

//    获取子类的泛型类
    public BaseDao(){
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class) params[0];
    }

//    根据id加载PO

    public T load(Serializable id) {
        return (T) getHibernateTemplate().load(entityClass, id);
    }

    public T get(Serializable id) {
        return (T) getHibernateTemplate().get(entityClass, id);
    }

    public List<T> loadAll() {
        return getHibernateTemplate().loadAll(entityClass);
    }

    public void save(T entity) {
        getHibernateTemplate().save(entity);
    }

    public void remove(T entity) {
        getHibernateTemplate().delete(entity);
    }


    public void removeAll(String tableName) {
        getSession().createSQLQuery(" truncate Table " + tableName + "").executeUpdate();
    }

    public void update(T entity) {
        getHibernateTemplate().update(entity);
    }

    public List find(String hql) {
        return this.getHibernateTemplate().find(hql);
    }

    public void initialize(Object entity) {
        this.getHibernateTemplate().initialize(entity);
    }

//    分页查询

    public Page pagedQuery(String hql, int pageNo, int pageSize, Object... values) {
        Assert.hasText(hql);
        Assert.isTrue(pageNo >= 1, "pageNo should start from 1");

        String countQueryString = " select count(*) " + removeSelect(removeOrders(hql));
        List countList = getHibernateTemplate().find(countQueryString, values);
        long totalCount = (Long) countList.get(0);


        if (totalCount < 1)
            return new Page();

        int startIndex = Page.getStartOfPage(pageNo, pageSize);
        Query  query = createQuery(hql, values);
        List list = query.setFirstResult(startIndex).setMaxResults(pageSize).list();

        return new Page(startIndex, totalCount, pageSize, list);

    }

    public Query createQuery(String hql, Object... values) {
        Assert.hasText(hql);
        Query query = getSession().createQuery(hql);
        for(int i = 0; i < values.length; i++) {
            query.setParameter(i, values[i]);
        }

        return query;
    }

// 去除select字句
    private static String removeSelect(String hql) {
        Assert.hasText(hql);
        int beginPos = hql.toLowerCase().indexOf("from");
        Assert.isTrue(beginPos != -1, " hql : " + hql + " must has a keyword 'from'");
        return hql.substring(beginPos);
    }

// 去除order by字句

    private static String removeOrders(String hql) {
        Assert.hasText(hql);

        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(hql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }

        m.appendTail(sb);
        return sb.toString();
    }


    public HibernateTemplate getHibernateTemplate() {

        return hibernateTemplate;
    }

    @Autowired
    public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }
    public Session getSession() {
        return hibernateTemplate.getSessionFactory().getCurrentSession();
    }
}
