package rocky.com.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Comparator;
import java.util.TreeSet;

public class UnSafeUtils {
    private static final Unsafe UNSAFE;
    static {
        try {
            PrivilegedExceptionAction e = new PrivilegedExceptionAction() {
                public Unsafe run() throws Exception {
                    Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    return (Unsafe)theUnsafe.get((Object)null);
                }
            };
            UNSAFE = (Unsafe) AccessController.doPrivileged(e);
        } catch (Exception var1) {
            throw new RuntimeException("Unable to load unsafe", var1);
        }
    }

    public static Unsafe getUnsafe() {
        return UNSAFE;
    }


    //**获得对象大小
    public static long sizeOf(Object obj) {
        Class clazz = obj.getClass();
        //为了观察每个field大小，对field的offSet进行了排序，后者-前者=前者大小
        TreeSet<Field> hashSet = new TreeSet(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Field f1=(Field)o1;
                Field f2=(Field)o2;
                long offSet1 = UNSAFE.objectFieldOffset(f1);
                long offSet2 = UNSAFE.objectFieldOffset(f2);
                return Integer.parseInt((offSet1 - offSet2)+"");
            }
        });

        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (!Modifier.isStatic(field.getModifiers())) {
                    hashSet.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        long maxOffSet=0;
        for (Field field : hashSet) {
            long offSet = UNSAFE.objectFieldOffset(field);
            System.out.println(field.getName() + " offSet = " + offSet);
            if(offSet>maxOffSet){
                maxOffSet=offSet;
            }
        }
        System.out.println("maxOffSet = "+maxOffSet);

        return (maxOffSet/8+1)*8;
    }

    //浅拷贝
    public static Object shallowCopy(Object obj) {
        long size = sizeOf(obj);//获得对象大小
        long start = getObjectPos(obj);//获得obj起始内存地址
        long address = UNSAFE.allocateMemory(size);//分配size大小的内存空间，返回内存起始地址
        UNSAFE.copyMemory(start, address, size); //从start位copy size个大小的内存内容到目标地址address
        return createObjByPointer(address);
    }

    //获得对象内存位置
    public static Long getObjectPos(Object obj) {
        Object[] array = new Object[]{obj};
        return UNSAFE.getLong(array, Long.valueOf(UNSAFE.ARRAY_OBJECT_BASE_OFFSET));//获得baseOffset位置内存地址
    }
    //通过制定内存指针位置的方式创建对象
    public static Object createObjByPointer(long address) {
        Object[] array = new Object[] {null};
        long baseOffset = UNSAFE.arrayBaseOffset(Object[].class);
        UNSAFE.putLong(array, baseOffset, address);//baseOffset指向address
        return array[0];
    }

}
