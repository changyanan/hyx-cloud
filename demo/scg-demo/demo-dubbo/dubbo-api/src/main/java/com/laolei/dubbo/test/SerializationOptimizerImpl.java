//package com.laolei.dubbo.test;
//
//import java.util.Collection;
//import java.util.LinkedList;
//import java.util.List;
//
//import com.alibaba.dubbo.common.serialize.support.SerializationOptimizer;
//import com.laolei.dubbo.model.User;
//import com.globalegrow.core.exception.GlobalException;
//
///**
// * This class must be accessible from both the provider and consumer
// *
// * @author lishen
// */
//public class SerializationOptimizerImpl implements SerializationOptimizer {
//
//    @SuppressWarnings("rawtypes")
//	public Collection<Class> getSerializableClasses() {
//        List<Class> classes = new LinkedList<Class>();
//        classes.add(Integer.class);
//        classes.add(User.class);
//        classes.add(GlobalException.class);
//        classes.add(GlobalException.class);
//        classes.add(User.class);
//        return classes;
//    }
//}
