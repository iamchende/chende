package cn.suse;

import org.junit.Test;

public class _190408CommonSort {
    @Test
    public void test(){
        for(int i=1;i<=10;i++){
            System.out.println("----------第"+i+"次测试结果如下：");
            SortStart(i);
        }
    }
    public void SortStart(int range){
        int base = 5000;
        int[] arr = new int[base*range];//每一次测试数据量增加5倍
        for(int i=0;i<arr.length;i++){
            arr[i] = (int)(Math.random()*1000);
        }
        //System.arraycopy(Object src, int srcPos, Object dest, int destPos, int length);
        //System.arraycopy(源数组, 从源数组的起始位置开始, 新数组, 新数组的开始起始位置, 要copy的数组的长度);
        int[] arr1 = new int[arr.length];
        int[] arr2 = new int[arr.length];
        int[] arr3 = new int[arr.length];
        int[] arr4 = new int[arr.length];
        int[] arr5 = new int[arr.length];
        int[] arr6 = new int[arr.length];
        int[] arr7 = new int[arr.length];
        int[] arr8 = new int[arr.length];
        System.arraycopy(arr, 0, arr1, 0, arr.length);
        bubbleSort1(arr1);
        System.arraycopy(arr, 0, arr2, 0, arr.length);
        bubbleSort2(arr2);
        System.arraycopy(arr, 0, arr3, 0, arr.length);
        bubbleSort3(arr3);
        System.arraycopy(arr, 0, arr4, 0, arr.length);
        bubbleSort4(arr4);
        System.arraycopy(arr, 0, arr5, 0, arr.length);
        bubbleSort5(arr5);
        System.arraycopy(arr, 0, arr6, 0, arr.length);
        quickSort(arr6);
        System.arraycopy(arr, 0, arr7, 0, arr.length);
        selectSort1(arr7);
        System.arraycopy(arr, 0, arr8, 0, arr.length);
        selectSort2(arr8);

    }
    public void swap(int[] arr,int i, int j){
        arr[i] = arr[i]^arr[j];
        arr[j] = arr[i]^arr[j];
        arr[i] = arr[i]^arr[j];
    }
    public void swap(int a, int b){
        a = a^b;
        b = a^b;
        a = a^b;
    }
    public void print(int[] arr,long start){
        //new Exception().getStackTrace()[0].getMethodName()表示当前的方法名
        System.out.println(new Exception().getStackTrace()[1].getMethodName()+"总共耗时"+ (System.currentTimeMillis()-start)+"ms");
        /*for(int i:arr){System.out.print(i+",");}
        System.out.println();*/
    }
    /**
     * 冒泡排序1.0
     * 最原始的冒泡方式
     * 最后一个数在j=length-2的时候就参与比较了，因此内侧可以小于-1
     * @param arr
     */
    public void bubbleSort1(int[] arr){
        long start = System.currentTimeMillis();
        for(int i=0;i<arr.length;i++){
            for(int j=0;j<arr.length-1;j++){
                if(arr[j] > arr[j+1]){
                    swap(arr, j, j+1);
                }
            }
        }
        print(arr, start);
    }
    /**
     * 冒泡排序2.0
     * 我们这里每次轮询会有个最大值跑到右边，所以又可以写成j<arr.length-1-i
     * 当有n个数时，前面n-1个数比较好了，最后一个数的位置其实也定好了，
     * 因此最后一轮比较（就只有他一个数）没有意义，即i<arr.length》》i<arr.length-1
     * @param arr
     */
    public void bubbleSort2(int[] arr){
        long start = System.currentTimeMillis();
        for(int i=0;i<arr.length-1;i++){
            for(int j=0;j<arr.length-1-i;j++){
                if(arr[j] > arr[j+1]){
                    swap(arr, j, j+1);
                }
            }
        }
        print(arr, start);
    }
    /**
     * 冒泡排序3.0
     * 显然对于一个数列，我们完全有可能只经过a(a<n)次轮询比较交换就完成排序了
     * 即数列的大部分数据本来就是有序的，此时就没有必要继续轮询有序的部分数据了
     * 如何判断，因为我们是j和j+a进行比较的，因此我们发现本次轮询当从j开始后面
     * 的数据就不再发生交换了，那么就说明j及其后面的数据刚好是有序的，不用再轮询
     * 比较，于是我们可以定义一个变量来记录
     * @param arr
     */
    public void bubbleSort3(int[] arr){
        long start = System.currentTimeMillis();
        for(int i=0;i<arr.length-1;i++){
            boolean isSorted = true;
            for(int j=0;j<arr.length-1-i;j++){
                if(arr[j] > arr[j+1]){
                    swap(arr, j, j+1);
                    isSorted = false;//有交换，说明数据还无序
                }
            }
            if(isSorted)break;
        }
        print(arr, start);
    }
    /**
     * 冒泡排序4.0
     * 经过上面的优化之后，我们发现还有个不足，
     * 就是在我们某次轮询比较时，有值的交换，
     * 但是某个值交换之后，其后面的数据是有序的，我们还在比较
     * 即使下一次轮询，我们还得遍历比较有序部分的数据
     * 因此，我们可以考虑记录下最有一次交换的位置，下一次轮询时我们只遍历到这个位置即可
     * 这个值k减小的速度肯定是大于等于i的自增速度的（正常情况下就-1，碰到有序就可以跳跃着减）
     * 即k <= arr.length-1-i,所以用k替换，可以让遍历的次数更少
     * @param arr
     */
    public void bubbleSort4(int[] arr){
        long start = System.currentTimeMillis();
        int k = arr.length-1;
        int lastChanged = 0;//每次轮询的最后一次修改才是我们要的值
        for(int i=0;i<arr.length-1;i++){
            boolean isSorted = true;
            for(int j=0;j<k;j++){
                if(arr[j] > arr[j+1]){
                    swap(arr, j, j+1);
                    lastChanged = j;//此处记录左边这个角标最好，因为我们是小于等于这个值(下一轮最后一次的j+1会包含此时的j)
                    isSorted = false;//有交换，说明数据还无序
                }
            }
            k = lastChanged;
            if(isSorted)break;
        }
        print(arr, start);
    }
    /**
     * 冒泡排序5.0
     * 经过一系列优化之后，感觉还能再优化一点点
     * 就是在某一次轮询时，我们只取了一个当次最大值，
     * 如果我们在一次轮询时，同时取出最大和最小值，
     * 效果是不是会更高点？显然后者能够让总轮询次数更少
     * @param arr
     */
    public void bubbleSort5(int[] arr){
        long start = System.currentTimeMillis();
        int k_max = arr.length-1;
        int k_min = 0;
        int lastChanged_max = 0;//每次轮询的最后一次修改才是我们要的值
        int lastChanged_min = arr.length-1;//每次轮询的最后一次修改才是我们要的值
        for(int i=0;i<arr.length-1;i++){
            boolean isSorted_max = true;
            boolean isSorted_min = true;
            for(int j=k_min;j<k_max;j++){//取当次轮询最大值
                if(arr[j] > arr[j+1]){
                    swap(arr, j, j+1);
                    lastChanged_max = j;//记录J,J+1此时是有序的，J还没排序
                    isSorted_max = false;//有交换，说明数据还无序
                }
            }
            k_max = lastChanged_max;
            for(int j=k_max;j>k_min;j--){//取当次轮询最小值
                if(arr[j] < arr[j-1]){
                    swap(arr, j, j-1);
                    lastChanged_min = j;//记录J,J-1此时是有序的，J还没排序
                    isSorted_min = false;//有交换，说明数据还无序
                }
            }
            k_min = lastChanged_min;
            if(isSorted_max && isSorted_min)break;
        }
        print(arr, start);
    }
    /**
     * 快速排序1.0
     * 快速排序实际上也是在冒泡排序的基础上优化而来
     * 取第一个数为比较的基准a，然后和后面的数据比较
     * 第一次轮询时，将数据分割成两部分（左边比a大，右边比a小）
     * 然后再对这两部分【单独的】轮询进行上面的操作，依次递归
     * 显然，如果数据参照基本有序，其性能接近冒泡，基准很重要
     * @param arr
     */
    public void quickSort(int[] arr){
        long start = System.currentTimeMillis();
        quickSort(arr, 0, arr.length-1);
        print(arr, start);
    }
    public void quickSort(int[] arr, int begin, int end){
        int compare = arr[begin];//取第一个元素作为比较的对象
        int low = begin;
        int high = end;
        while (low < high){//当两个角标相遇时，说明数据全部被compare所在元素分割成两部分了
            //右侧数据比分割值大，角标直接左移
            while(low < high && arr[high] >= compare)high--;
            //上面左移不了了，说明碰到的数据比分割值小，交换位置；右侧现在又有可能左移了
            if(low < high){swap(arr, low++, high);}//交换过来的数肯定比分割值大，所以直接加1
            //同上，左侧的数据比分割值小，角标直接右移
            while(low < high && arr[low] <= compare)low++;
            //上面右移不了了，说明碰到的数据比分割值大，交换位置；左侧现在又有可能右移了
            if(low < high){swap(arr, high--, low);}//交换过来的数肯定比分割值小，所以直接减1
        }
        //数据现在被分割成两部分(low此时和high已经相遇了,并且这个元素的位置就是排好序的位置)，再对两个子集分别进行排序
        if(low > high)swap(low, high);//当low > high时，说明已经相遇过了，且中间的数据相同，不用继续排序了
        if(low > begin)quickSort(arr, begin, low-1);
        if(high < end)quickSort(arr, high+1, end);
    }
    /**
     * 选择排序1.0
     * 在要排序的数组中，选出最小的数与第一个位置的数交换；
     * 然后在剩下的序列当中再找当前最小的数与第二个位置的数交换，
     * 依次递归到倒数第二个数和最后一个数比较为止（不一定会交换）。
     * @param arr
     */
    public void selectSort1(int[] arr){
        long start = System.currentTimeMillis();
        for(int i=0;i<arr.length;i++){//每次循环取出当前最小值
            for(int j=i+1;j<arr.length;j++){
                if(arr[i] > arr[j])swap(arr,i,j);
            }
        }
        print(arr, start);
    }
    /**
     * 选择排序2.0
     * 每一轮确定位置了 才交换元素
     * @param arr
     */
    public void selectSort2(int[] arr){
        long start = System.currentTimeMillis();
        for(int i=0;i<arr.length;i++){//每次循环取出当前最小值
        	int index = i;
            for(int j=i+1;j<arr.length;j++){
                if(arr[index] > arr[j]){
                	index = j;
                }
            }
            swap(arr,i,index);
        }
        print(arr, start);
    }
}