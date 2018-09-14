//package com.qyy.jyshop.supple.impl;
//
//import java.io.Serializable;
//import java.math.BigInteger;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import javax.persistence.EntityManager;
//import javax.persistence.Query;
//
//import org.hibernate.SQLQuery;
//import org.hibernate.transform.Transformers;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
//
//import com.qyy.jyshop.conf.CustomerSpecs;
//import com.qyy.jyshop.supple.CustomRepository;
//
//
//public class CustomRepositoryImpl <T, ID extends Serializable> 
//					extends SimpleJpaRepository<T, ID>  implements CustomRepository<T,ID> {
//	
//	private final EntityManager entityManager;
//	
//	public CustomRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
//		super(domainClass, entityManager);
//		this.entityManager = entityManager;
//	}
//
//	@Override
//	public Page<T> findByAuto(T example, Pageable pageable) {
//		return findAll(CustomerSpecs.byAuto(entityManager, example),pageable);
//	}
//
//	@Override
//	public List<T> findByParam(String sql, Map<String, Object> params) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Page<T> findByParam(String sql, Map<String, Object> params,
//			final Pageable pageable) {
//		
//		String countSql = "select count(*) from ("+sql+") t";
//		//获取所有的数据行数
//		Query countQuery = entityManager.createNativeQuery(countSql);
//		
//		Query query = entityManager.createNativeQuery(sql);
//		query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//		int firstIndex = (pageable.getPageNumber() * pageable.getPageSize());
//		query.setFirstResult(firstIndex);
//	    query.setMaxResults(pageable.getPageSize());
//		
//	    //添加条件参数
//	    if(params!=null){
//	    	 for (Map.Entry<String, Object> paramMap :params.entrySet())  {  
//	             countQuery.setParameter(paramMap.getKey(), paramMap.getValue());
//	             query.setParameter(paramMap.getKey(), paramMap.getValue());
//	         }  
//	    }
//		
//		//数据总数
//		final BigInteger total = (BigInteger) countQuery.getSingleResult();
//		
//		final List<T> dataList=query.getResultList();
//		
//		//得到页数
//	    final int totalPage = total.divide(new BigInteger(String.valueOf(pageable.getPageSize()))).intValue()+1;
//    
//	    Page<T> page=new Page<T>() {
//			
//			@Override
//			public Iterator<T> iterator() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public Pageable previousPageable() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public Pageable nextPageable() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			//是否最后一页
//			@Override
//			public boolean isLast() {
//				if(totalPage==(pageable.getPageNumber()+1))
//					return true;
//				return false;
//			}
//			
//			//是否第一页
//			@Override
//			public boolean isFirst() {
//				if(pageable.getPageNumber()==0)
//					return true;
//				return false;
//			}
//			
//			@Override
//			public boolean hasPrevious() {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public boolean hasNext() {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public boolean hasContent() {
//				// TODO Auto-generated method stub
//				return false;
//			}
//			
//			@Override
//			public Sort getSort() {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			//页数
//			@Override
//			public int getSize() {
//				return pageable.getPageSize();
//			}
//			
//			//当前页数据量
//			@Override
//			public int getNumberOfElements() {
//				if(dataList!=null)
//					return dataList.size();
//				return 0;
//			}
//			
//			//页码
//			@Override
//			public int getNumber() {
//				return pageable.getPageNumber();
//			}
//			
//			//数据列表
//			@Override
//			public List<T> getContent() {
//				return dataList;
//			}
//			
//			@Override
//			public <S> Page<S> map(Converter<? super T, ? extends S> converter) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			//总页数
//			@Override
//			public int getTotalPages() {
//				return totalPage;
//			}
//			
//			//总数据数
//			@Override
//			public long getTotalElements() {
//				return total.intValue();
//			}
//		};
//		return page;
//	}
//}
