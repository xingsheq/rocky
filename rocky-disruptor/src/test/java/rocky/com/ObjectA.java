package rocky.com;

/**
 * Created by xingsheq on 2018/2/6.
 */
public class ObjectA {
    String str;   // 4
    int int1;       // 4
    Integer Int2;
    byte byte1;      // 1
    Byte Byte2;
    char char1; //2
    Character Char2;
    boolean boolean1;//1
    Boolean BooLean2;
    long long1;//4
    Long Long2;
    float float1;//4
    Float Float2;
    double double1;//8
    Double Double2;
    ObjectB obj;  //4
    int[] intArray;
    volatile ObjectB[] objs;
    volatile ObjectB[] objs2;


    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public int getInt1() {
        return int1;
    }

    public void setInt1(int int1) {
        this.int1 = int1;
    }

    public Integer getInt2() {
        return Int2;
    }

    public void setInt2(Integer int2) {
        Int2 = int2;
    }

    public byte getByte1() {
        return byte1;
    }

    public void setByte1(byte byte1) {
        this.byte1 = byte1;
    }

    public Byte getByte2() {
        return Byte2;
    }

    public void setByte2(Byte byte2) {
        Byte2 = byte2;
    }

    public char getChar1() {
        return char1;
    }

    public void setChar1(char char1) {
        this.char1 = char1;
    }

    public Character getChar2() {
        return Char2;
    }

    public void setChar2(Character char2) {
        Char2 = char2;
    }

    public boolean isBoolean1() {
        return boolean1;
    }

    public void setBoolean1(boolean boolean1) {
        this.boolean1 = boolean1;
    }

    public Boolean getBooLean2() {
        return BooLean2;
    }

    public void setBooLean2(Boolean booLean2) {
        BooLean2 = booLean2;
    }

    public long getLong1() {
        return long1;
    }

    public void setLong1(long long1) {
        this.long1 = long1;
    }

    public Long getLong2() {
        return Long2;
    }

    public void setLong2(Long long2) {
        Long2 = long2;
    }

    public float getFloat1() {
        return float1;
    }

    public void setFloat1(float float1) {
        this.float1 = float1;
    }

    public Float getFloat2() {
        return Float2;
    }

    public void setFloat2(Float float2) {
        Float2 = float2;
    }

    public double getDouble1() {
        return double1;
    }

    public void setDouble1(double double1) {
        this.double1 = double1;
    }

    public Double getDouble2() {
        return Double2;
    }

    public void setDouble2(Double double2) {
        Double2 = double2;
    }

    public ObjectB getObj() {
        return obj;
    }

    public void setObj(ObjectB obj) {
        this.obj = obj;
    }

    public int[] getIntArray() {
        return intArray;
    }

    public void setIntArray(int[] intArray) {
        this.intArray = intArray;
    }

    public ObjectB[] getObjs() {
        return objs;
    }

    public void setObjs(ObjectB[] objs) {
        this.objs = objs;
    }

    public ObjectB[] getObjs2() {
        return objs2;
    }

    public void setObjs2(ObjectB[] objs2) {
        this.objs2 = objs2;
    }
}
