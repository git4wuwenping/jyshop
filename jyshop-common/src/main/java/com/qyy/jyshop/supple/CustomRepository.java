//package com.qyy.jyshop.supple;
//
//import java.io.Serializable;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.repository.NoRepositoryBean;
//
//@NoRepositoryBean
//public interface CustomRepository<T, ID extends Serializable>extends JpaRepository<T, ID> ,JpaSpecificationExecutor<T>{
//	
//	Page<T> findByAuto(T example,Pageable pageable);
//	
//	List<T> findByParam(String sql,Map<String,Object> params);
//	
//	Page<T> findByParam(String sql,Map<String,Object> params,Pageable pageable);
//}
