package com.test;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * @param <T> wrapped type
 */
@SuppressWarnings("unchecked")
public abstract class TypeLiteral<T> implements Serializable
{
    private static final long serialVersionUID = 6993258591899719600L;
    
    private Type definedType;

    protected TypeLiteral()
    {
        this.definedType = getDefinedType(this.getClass());
    }

    public final Type getType()
    {
        return definedType;
    }

    public final Class<T> getRawType()
    {
        Class<T> rawType = null;

        if (this.definedType instanceof Class)
        {
            rawType = (Class<T>) this.definedType;
        }
        else if (this.definedType instanceof ParameterizedType)
        {
            ParameterizedType pt = (ParameterizedType) this.definedType;
            rawType = (Class<T>) pt.getRawType();

        }
        else if (this.definedType instanceof GenericArrayType)
        {
            rawType = (Class<T>) Object[].class;
        }
        else
        {
            throw new RuntimeException("Illegal type for the Type Literal Class");
        }

        return rawType;
    }

    private Type getDefinedType(Class<?> clazz)
    {
        Type type = null;

        if (clazz == null)
        {
            throw new RuntimeException("Class parameter can not be null!");
        }

        Type superClazz = clazz.getGenericSuperclass();

        if (superClazz.equals(Object.class))
        {
            throw new RuntimeException("Super class must be parametrized type!");
        }
        else if (superClazz instanceof ParameterizedType)
        {
            ParameterizedType pt = (ParameterizedType) superClazz;            
            Type[] actualArgs = pt.getActualTypeArguments();

            if (actualArgs.length == 1)
            {
                type = actualArgs[0];
            }
            else
            {
                throw new RuntimeException("More than one parametric type!");
            }
        }
        else
        {
            type = getDefinedType((Class<?>) superClazz);
        }

        return type;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        
        result = prime * result + ((definedType == null) ? 0 : definedType.hashCode());
        
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;   
        }
        
        if (obj == null)
        {
            return false;   
        }
        
        if (getClass() != obj.getClass())
        {
            return false;   
        }
        
        TypeLiteral other =  (TypeLiteral)obj;
        if (definedType == null)
        {
            if (other.definedType != null)
            {
                return false;   
            }
        }
        else if (!definedType.equals(other.definedType))
        {
            return false;   
        }
        
        return true;
    }

}