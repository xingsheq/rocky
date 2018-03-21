package rocky.com.common.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by xingsheq on 2018/3/14.
 */
public class SortTest {


    public static void shellSort(int[] arrays){
        if(arrays == null || arrays.length <= 1){
            return;
        }
        //增量
        int incrementNum = arrays.length/2;
        while(incrementNum >=1){
            System.out.println("incr = "+incrementNum);
            for(int i=0;i<arrays.length;i++){
                System.out.println(" i = "+i);
                //进行插入排序
                for(int j=i;j<arrays.length-incrementNum;j=j+incrementNum){
                    System.out.println("   j = "+j+" j+incr = "+(j+incrementNum));
                    if(arrays[j]>arrays[j+incrementNum]){
                        int temple = arrays[j];
                        arrays[j] = arrays[j+incrementNum];
                        arrays[j+incrementNum] = temple;
                    }
                }
            }
            //设置新的增量
            incrementNum = incrementNum/2;
        }
    }

    /**
     * O(nlog2n)

     O(nlog2n)

     O(nlog2n)

     O(1)
     * @param arr
     */

    public static void heapSort(int []arr){
        //1.构建大顶堆,获得堆顶最大值
        for(int i=arr.length/2-1;i>=0;i--){
            //从最后一个非叶子结点从下至上，从右至左调整结构
            heapAdjust(arr, i, arr.length);
        }
        System.out.println(" over");
        //2.调整堆结构+交换堆顶元素与末尾元素
        for(int j=arr.length-1;j>0;j--){
            swap(arr,0,j);//将堆顶元素与末尾元素进行交换
            heapAdjust(arr, 0, j);//重新对n-1个元素，构建堆，获得次大值，递归
        }

    }

    /**
     * 假设数组为堆，从i到i+length调整为大顶堆
     * @param arr
     * @param i
     * @param length
     *    0
     *  1     2
     *3   4  5 6
     7 8 9

     * i=4：temp=4  k=9 => a[4] = a[9] i=9 =>a[9] = temp
     * i=3:temp=3 k=7 k=8 =>a[3]=a[8] i=8 =>a[8] = temp
     *          9
     *      8       6
     *  7    4   5  2
     * 0 3  1
     *
     *
     *          1
     *      8       6
     *  7    4   5  2
     * 0 3  9
     *
     *
     *
     */

    /**
     * 前提：parent下级子节点以下已经是堆，或者无非叶子节点
     * 从父节点开始往下遍历，把大的子节点的值放到父节点,大的子节点值设置为父节点
     * @param array
     * @param parent
     * @param length
     */
    public static void heapAdjust(int[] array, int parent, int length) {

        int temp = array[parent]; // temp保存当前父节点
        int child = 2 * parent + 1; // 先获得左孩子,child实际作用是指向最大的子节点
        System.out.println("parent = "+parent+" child "+child);
        while (child < length) {
            System.out.println("|--parent = "+parent+" child "+child);
            // 如果有右孩子结点，并且右孩子结点的值大于左孩子结点，则选取右孩子结点
            if (child + 1 < length && array[child] < array[child + 1]) {
                child++;
                System.out.println("|----child change to "+child);
            }
            // 如果父结点的值已经大于孩子结点的值，则直接结束
            if (temp >= array[child]) {
                break;
            }
            // 把大的孩子结点的值赋给父结点
            array[parent] = array[child];
            parent = child;// 选取大的孩子结点做为父节点,继续向下比较筛选
            child = 2 * child + 1; //获得大的孩子结点的左孩子结点，继续下一循环

        }
        array[parent] = temp;//把最后被替换的最大子节点设值为父节点值
    }
    /**
     * 交换元素
     * @param arr
     * @param a
     * @param b
     */
    public static void swap(int []arr,int a ,int b){
        int temp=arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    public static void main(String[] args) {
//        int[] data = new int[] { 26, 53, 67, 48, 57, 13, 48, 32, 60,50};
        int[] data = new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1,0};
        Arrays.sort(data);

//        shellSortSmallToBig(data);
//        shellSort(data);
        heapSort(data);
        System.out.println(Arrays.toString(data));

    }
}
