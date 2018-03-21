package rocky.com;

import rocky.com.pojo.TrapEntity;
import rocky.com.util.UnSafeUtils;
import sun.misc.Unsafe;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;


public class UnSafeTest {


    private static final Unsafe UNSAFE = UnSafeUtils.getUnsafe();

    public static void main(String[] args) {
        changeValueByOffSetTest();
//        printConstant();
//        changeValueByPositionTest();
//       long size= UnSafeUtils.sizeOf(new ObjectA());
//       System.out.println(size);
//        shallowCopyTest();
//        objectStaticFieldTest();
//        defineClassTest();

        System.exit(0);//不加这句会error
    }

    private static void defineClassTest() {

        byte[] classContents = new byte[0];
        try {
            classContents = getClassContent();
            Class c = UNSAFE.defineClass(null, classContents, 0, classContents.length,null,null);
            Method m=c.getMethod("getId");
            Object res =m.invoke(c.newInstance(),null);
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
}
    //getClassContent()方法，将一个class文件，读取到一个byte数组。
    private static byte[] getClassContent() throws Exception {
        File f = new File("E:\\GAE\\rocky\\rocky-disruptor\\target\\classes\\rocky\\com\\pojo\\TrapEntity.class");
        FileInputStream input = new FileInputStream(f);
        byte[] content = new byte[(int)f.length()];
        input.read(content);
        input.close();
        return content;
    }

    //测试staticFieldBase方法，返回Class对象
private static void objectStaticFieldTest(){
    try {
        Class objB=   (Class) UNSAFE.staticFieldBase(ObjectB.class.getDeclaredField("staticFile"));
        System.out.println(objB.getName());
    } catch (NoSuchFieldException e) {
        e.printStackTrace();
    }
}

    //通过内存位置修改对象属性
    private static void changeValueByPositionTest() {
        TrapEntity entity = new TrapEntity();
        entity.setId(1);
        entity.setAlarmName("alarm1");
        Long pos = UnSafeUtils.getObjectPos(entity);
        System.out.println("getObjectPos = " + pos);

        try {
            long idOffSet = UNSAFE.objectFieldOffset(entity.getClass().getDeclaredField("id"));
            long idPos = pos + idOffSet;
            System.out.println("old value = " + UNSAFE.getInt(idPos));
//            System.out.println("byte = "+b);
            UNSAFE.putInt(idPos, 2);
            System.out.println("new value = " + entity.getId());

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public static void printConstant() {
        System.out.println("ADDRESS_SIZE = "+UNSAFE.ADDRESS_SIZE);
        System.out.println("ARRAY_OBJECT_BASE_OFFSET = "+UNSAFE.ARRAY_OBJECT_BASE_OFFSET);
        System.out.println("ARRAY_OBJECT_INDEX_SCALE = "+UNSAFE.ARRAY_OBJECT_INDEX_SCALE);

    }
    public static void shallowCopyTest() {
        ObjectA objectA=new ObjectA();
        objectA.setStr("I'm ObjectA");
        objectA.setInt1(1);
        long size = UnSafeUtils.sizeOf(objectA);//获得对象大小
        long start = UnSafeUtils.getObjectPos(objectA);//获得obj起始内存地址
        System.out.println("getObjectPos = "+start);
        long address = UNSAFE.allocateMemory(size);//分配size大小的内存空间，返回内存起始地址
        System.out.println("allocateMemory address = "+address);
        UNSAFE.copyMemory(start, address, size); //从start位copy size个大小的内存内容到目标地址address
        ObjectA ObjectACopy= (ObjectA) UnSafeUtils.createObjByPointer(address);
        ObjectACopy.setInt1(11);
        //浅复制只能修改基本数据类型，修改对象引用运行会报error
//        ObjectACopy.setStr("I'm ObjectA Copy");//A fatal error has been detected by the Java Runtime Environment
        System.out.println(objectA.getInt1());
        System.out.println(ObjectACopy.getInt1());
        UNSAFE.freeMemory(address);
    }


    //通过offSet修改对象属性
    private static void changeValueByOffSetTest() {
        TrapEntity entity = new TrapEntity();
        entity.setId(1);
        entity.setAlarmName("alarm1");
        try {
            long alarmNameOffSet = UNSAFE.objectFieldOffset(entity.getClass().getDeclaredField("alarmName"));
            long idOffSet = UNSAFE.objectFieldOffset(entity.getClass().getDeclaredField("id"));
            System.out.println(alarmNameOffSet);
            UNSAFE.putObject(entity, alarmNameOffSet, "alarmName2");
            System.out.println(entity.getAlarmName());
            System.out.println(idOffSet);
            UNSAFE.putInt(entity, idOffSet, 2);
            System.out.println(entity.getId());

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

}
