package sa.springframework.spring6restmvc.genUtils;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public  class GenUtilMap<E,T> {

    private static final ModelMapper getModelMapper(){
        return  new ModelMapper();
    }
     public final  E pojoToDto(E targetClass, T source){
        return (E) getModelMapper()
                .map(source,targetClass.getClass());
    }
    public final T dtoToPojo(E targetClass,T source){
        return (T) getModelMapper()
                .map(targetClass,source.getClass());
    }
    public final List<E> pojoToListDto(E targetClass, List<T> source){
        List<E>  eList = new ArrayList<>();
        for(T t : source){
            E obj  = (E) getModelMapper().map(t,targetClass.getClass());
            eList.add(obj);
        }
        return eList;
    }
    public final List<T> dtoToListPojo(List<E> targetClass, T source){
        List<T>  eList = new ArrayList<>();
        for(E cls : targetClass){
            T obj  = (T) getModelMapper().map(cls,source.getClass());
            eList.add(obj);
        }
        return eList;
    }
    public  final Set<E> pojoToSetDto(E targetClass, Set<T> source){
        Set<E>  eSet  = new HashSet<>();
        for(T t : source){
            E obj  = (E) getModelMapper().map(t,targetClass.getClass());
            eSet.add(obj);
        }
        return eSet;
    }
    public  final Set<T> dtoToSetPojoSet(Set<E> targetClass, T source){
       Set<T>  eSet = new HashSet<>();
        for(E cls : targetClass){
            T obj  = (T) getModelMapper().map(cls,source.getClass());
            eSet.add(obj);
        }
        return eSet;
    }
}
